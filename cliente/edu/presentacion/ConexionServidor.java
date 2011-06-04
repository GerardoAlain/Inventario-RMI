package cliente.edu.presentacion;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import servidor.edu.presentacion.IServidorInventario;
import cliente.edu.logica.builder.MainPanelBuilder;
import cliente.edu.logica.command.ManejadorComandos;

/**
 * Esta clase representa la conexion con el servidor desde la parte
 * del cliente, esta clase extiende de <i>JDialog</i> e implementa
 * la interface <i>KeyListener</i> para capturar los eventos del 
 * teclado. Esta clase hace uso del patron <b>Singleton</b> con el
 * fin de que exista solo una instancia de la clase, debido a que
 * solo puede haber una conexion por cliente al servidor.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class ConexionServidor extends JDialog implements KeyListener {
	
	/**
	 * Representa la instancia de la clase
	 * @uml.property name="conexionServidor"
	 * @uml.associationEnd inverse="conexionServidor1:cliente.edu.presentacion.ConexionServidor" multiplicity="(0 1)"
	 */
	private static ConexionServidor conexionServidor;
	
	/**
	 * Representa el Identificador Unico de Sesion
	 */
	private static String identificadorUnico;

	/**
	 * Representa el Servidor Remoto
	 */
	private static IServidorInventario servidor;
	
	/**
	 * Representa el estado de la conexion con el servidor
	 */
	private static boolean isConnected = false;
	
	/**
	 * Representa el area de texto donde se ingresa el usuario
	 */
	private JTextField usuarioFld;
	
	/**
	 * Representa el area de texto donde se ingresa el password
	 */
	private JPasswordField passwordFld;
	
	/**
	 * Representa el area de texto donde se ingresa la direccion IP del servidor
	 */
	private JTextField ipFld;
	
	/**
	 * Representa el boton de entrar, para validar los datos digitados
	 */
	private JButton entrarBtn;
	
	/**
	 * Representa el boton cancelar, para cerrar la ventana.
	 */
	private JButton cancelarBtn;
	
	/**
	 * Constructor de la clase, es privado para que no pueda
	 * ser instanciado desde el exterior, sino que su instancia
	 * sea obtenida mediante el llamado al metodo <i>getInstancia</i>.
	 */
	private ConexionServidor() {
		setLayout(new GridLayout(4,2));
		
		add(new JLabel("Usuario:"));
		usuarioFld = new JTextField(10);
		usuarioFld.addKeyListener(this);
		add(usuarioFld);
		add(new JLabel("Password:"));
		passwordFld = new JPasswordField(10);
		passwordFld.addKeyListener(this);
		add(passwordFld);
		add(new JLabel("Servidor:"));
		ipFld = new JTextField(10);
		ipFld.addKeyListener(this);
		add(ipFld);
		
		entrarBtn = new JButton("Entrar");
		entrarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(System.getSecurityManager() ==  null)
						System.setSecurityManager(new RMISecurityManager());
					
					if(ipFld.getText().equals(""))
						ipFld.setText("127.0.0.1");
					
					// Obtenemos la instancia del servidor Remoto
					servidor = (IServidorInventario) Naming.lookup("//"+ipFld.getText()+"/Inventario");
					identificadorUnico = servidor.conectarConServidor(usuarioFld.getText(), passwordFld.getPassword());
					usuarioFld.setText("");
					passwordFld.setText("");
					ipFld.setText("");
					
					if(identificadorUnico != null && servidor != null) {
						isConnected = true;
						ManejadorComandos.getInstancia().setMomento();
						new InformacionUsuario(identificadorUnico);
						MainPanelBuilder.setDatos(servidor.getAllItems(identificadorUnico, Inventario.getOrdenamientoDatos()));
						Inventario.setStatusBarText("Se ha conectado al servidor con exito");
					}
					else {
						isConnected = false;
						Inventario.setStatusBarText("Datos Incorrectos, conexion fallida");
					}
					
				} catch (MalformedURLException ex) {
					usuarioFld.setText("");
					passwordFld.setText("");
					ipFld.setText("");
					Inventario.setStatusBarText("La direccion IP del servidor no es valida");
					ex.printStackTrace();
				} catch (RemoteException ex) {
					usuarioFld.setText("");
					passwordFld.setText("");
					ipFld.setText("");
					ex.printStackTrace();
				} catch (NotBoundException ex) {
					usuarioFld.setText("");
					passwordFld.setText("");
					ipFld.setText("");
					ex.printStackTrace();
				}
				dispose();
			}
		});
		add(entrarBtn);
		
		cancelarBtn = new JButton("Cancelar");
		cancelarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usuarioFld.setText("");
				passwordFld.setText("");
				ipFld.setText("");
				dispose();
			}
		});
		add(cancelarBtn);
		
		setSize(200,140);
		setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-100, Toolkit.getDefaultToolkit().getScreenSize().height/2-70);
		setTitle("Conectar a Servidor");
		setResizable(false);
		setModal(true);
	}
	
	/**
	 * Metodo que al ser llamado, muestra la ventana de peticion
	 * de datos para el acceso al servidor remoto.
	 */
	public void conectar() {
		if(!isConnected)
			setVisible(true);
		else
			Inventario.setStatusBarText("Ya esta conectado al servidor");
	}
	
	/**
	 * Metodo que al ser llamado se desconecta del servidor
	 * Remoto, si no esta conectado, muestra el estado en la
	 * barra de estado de la aplicacion.
	 */
	public void desconectar() {
		if(isConnected)
			try {
				servidor.desconectarDelServidor(identificadorUnico);
				isConnected = false;
				MainPanelBuilder.setDatos(new Vector());
				Inventario.setStatusBarText("Se ha desconectado del servidor con exito");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		else
			Inventario.setStatusBarText("No esta conectado al servidor");
	}

	/**
	 * Metodo que retorna la instancia del servidor Remoto
	 * @return Servidor Remoto
	 */
	public static IServidorInventario getServidor() {
		return servidor;
	}
	
	/**
	 * Metodo que retorna el String con el identificador unico
	 * de sesion.
	 * @return Identificador Unico de Sesion
	 */
	public static String getIdentificadorUnico() {
		return identificadorUnico;
	}
	
	/**
	 * Metodo que retorna el estado de la conexion con el servidor.
	 * @return	True si esta conectado al servidor, False de lo contrario
	 */
	public static boolean estaConectado() {
		return isConnected;
	}
	
	/**
	 * Metodo que retorna la instancia de la clase, ya que esta
	 * clase hace uso del patron singleton.
	 * @return Instancia unica de la clase.
	 */
	public static ConexionServidor getInstancia() {
		if(conexionServidor == null)
			conexionServidor = new ConexionServidor();
		return conexionServidor;
	}

	/**
	 * Listener de teclado para el dialogo de peticion de datos.
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			entrarBtn.doClick();
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			cancelarBtn.doClick();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

}
