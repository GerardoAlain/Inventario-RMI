package servidor.edu.presentacion;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import servidor.edu.repositorio.DatabaseManager;

/**
 * Esta clase representa el servidor remoto de la aplicacion
 * el cual utiliza tecnologia RMI para el llamado a sus metodos
 * desde un cliente remoto.<p>
 * 
 * Esta clase cuenta con una interfaz grafica simple, ya que su
 * unica funcionalidad es la de proporcionar acceso seguro
 * a los datos del servidor via remota.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class ServidorInventario extends UnicastRemoteObject implements IServidorInventario {

	/**
	 * Representa el controlador de la base de datos
	 * @uml.property name="databaseManager"
	 * @uml.associationEnd inverse="servidorInventario:servidor.edu.repositorio.DatabaseManager"
	 * multiplicity="(0 1)"
	 */
	private DatabaseManager manager;

	
	/**
	 * Representa el area de texto en la que se muestra el log de eventos
	 */
	private JTextArea textArea;
	
	/**
	 * Representa el panel de desplazamiento para el area de texto
	 */
	private JScrollPane scrollPane;
	
	/**
	 * Representa el log de eventos en el servidor
	 */
	private String serverLog = new String("Servidor Iniciado!!!");
	
	/**
	 * Representa la ventana en la que se muestra el area de texto y el
	 * boton de cerrar el servidor.
	 */
	private JFrame statusPane;
	
	/**
	 * Representa el panel en el cual se agrega el boton de cerrar el servidor
	 */
	private JPanel southPnl;
	
	/**
	 * Representa el boton de cerrar el servidor
	 */
	private JButton cerrarServidorBtn;

	/**
	 * Metodo principal de la aplicacion, se encarga de definir el
	 * manager de seguridad para la conexion y registra la instancia
	 * en el registro RMI para que sea accesada mediante el llamado:
	 * <code>Naming.lookup("Inventario");</code> El cual retorna la
	 * instancia del servidor que es de tipo <i>IServidorInventario</i>.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Iniciando Servidor...");

		if(System.getSecurityManager() ==  null)
			System.setSecurityManager(new RMISecurityManager());
		
		try {
			IServidorInventario servidor = (IServidorInventario) new ServidorInventario();
			Naming.rebind("Inventario", servidor);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("Servidor en linea!!!");
	}

	/**
	 * Constructor de la clase, hace un llamado al metodo privado
	 * <i>crearInterfaz()</i> y luego hace un llamado al metodo 
	 * <i>conectar()</i> de la clase <i>DatabaseManager</i> para
	 * obtener la conexion a la base de datos. 
	 * 
	 * @throws RemoteException
	 */
	protected ServidorInventario() throws RemoteException {
		super();
		crearInterfaz();
		
		//Conectamos a la base de datos
		updateServerData("Intentando conectar a la base de datos...");
		manager = new DatabaseManager();
		if(manager.conectar()){
			//Estamos conectados
			updateServerData("Intento de conexion exitoso!!!");
		}
		else {
			//Estamos desconectados
			updateServerData("Intento de conexion fallido!!!");
		}
			
	}
	
	/**
	 * El llamado a este metodo provoca que el servidor se cierre
	 * y que la memoria que utiliza sea liberada, de tal forma que
	 * este metodo ha de asignar <i>null</i> a todas las instancias
	 * de memoria que utiliza el servidor.
	 */
	private void TerminarServidor() {
		try {
			System.out.println("\nCerrando Servidor...");
			
			Naming.unbind("Inventario");
			manager.desconectar();
			
			manager = null;
			textArea = null;
			scrollPane = null;
			serverLog = null;
			statusPane = null;
			southPnl = null;
			cerrarServidorBtn = null;
			
			System.gc();
			
			System.out.println("Servidor Cerrado!!!");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo se encarga de crear la interfaz del servidor, 
	 * compone una ventana en la que muestra la informacion
	 * relevante del servidor, en este caso esa informacion
	 * es un log de eventos y un boton para terminar cerrar
	 * el servidor mediante el llamado al metodo <i>TerminarServidor</i>.
	 */
	private void crearInterfaz() {
		southPnl = new JPanel();
		textArea = new JTextArea();
		statusPane = new JFrame("Servidor Remoto - Inventario");
		cerrarServidorBtn = new JButton("Terminar Session");
		scrollPane = new JScrollPane(textArea);
		
		cerrarServidorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusPane.dispose();
				TerminarServidor();
			}
		});
		
		textArea.setAutoscrolls(true);
		textArea.setEditable(false);
		textArea.setText(serverLog);
		
		southPnl.add(cerrarServidorBtn);
		statusPane.add(scrollPane, BorderLayout.CENTER);
		statusPane.add(southPnl, BorderLayout.SOUTH);
		
		statusPane.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width-300, Toolkit.getDefaultToolkit().getScreenSize().height-180);
		statusPane.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		statusPane.setSize(300,150);
		statusPane.setAlwaysOnTop(true);
		statusPane.setResizable(false);
		statusPane.setVisible(true);
		
	}
	
	/**
	 * Metodo que conecta con el servidor y retorna el numero
	 * de sesion unico al usuario.
	 */
	public String conectarConServidor(String usuario, char[] password) throws RemoteException {
		updateServerData("Peticion de acceso del usuario " + usuario+"...");
		
		String x = manager.iniciarSesion(usuario, password);
		
		if(x != null)
			updateServerData("Peticion de acceso concedida #"+x+" !!!");
		else
			updateServerData("Peticion de acceso denegada!!!");
		
		return x;
	}

	/**
	 * Metodo que desconecta del servidor al usuario identificado
	 * con el parametro.
	 */
	public void desconectarDelServidor(String identificador) throws RemoteException {
		updateServerData("Peticion de desconexion de #" +identificador);
		manager.terminarSesion(identificador);
		updateServerData("Peticion de desconexion aprobada #"+identificador+"!!!");
	}
	
	/**
	 * Metodo que actualiza la informacion en el log del servidor.
	 * @param x	Cadena de texto del evento
	 */
	private void updateServerData(String x) {
		serverLog += "\n" + x;
		textArea.setText(serverLog);
	}

	/**
	 * Metodo que retorna el producto identificado con el parametro
	 */
	public Vector getProducto(String identificador, int idProducto) throws RemoteException {
		return manager.getProducto(identificador, String.valueOf(idProducto));
	}

	/**
	 * Metodo que retorna todos los productos de la base de datos.
	 */
	public Vector getAllItems(String identificador, String tipoOrdenacion) throws RemoteException {
		return manager.getAllProductos(identificador, tipoOrdenacion);
	}

	/**
	 * Metodo que retorna el nombre del usuario identificado con el parametro.
	 */
	public String getUserName(String identificador) throws RemoteException {
		return manager.getNombreUsuario(identificador);
	}

	/**
	 * Metodo que retorna la fecha de ultimo acceso del usuario identificado con el parametro.
	 * @return String con los datos de ultimo acceso
	 */
	public String getUserLastAccess(String identificador) throws RemoteException {
		return manager.getUltimoAcceso(identificador);
	}

	/**
	 * Metodo que agrega un producto a la base de datos
	 */
	public int addProducto(String identificador, String ID, String Descripcion, int CantidadDisponible, int CantidadMinima, int PrecioVenta, String Proveedor, String Observaciones) throws RemoteException {
		return manager.addProducto(identificador, ID, Descripcion, String.valueOf(CantidadDisponible), String.valueOf(CantidadMinima), String.valueOf(PrecioVenta), Proveedor, Observaciones);
	}

	/**
	 * Metodo que actualiza un producto de la base de datos
	 */
	public boolean updateProducto(String identificador, int idProducto, String Descripcion, int CantidadDisponible, int CantidadMinima, int PrecioVenta, String Proveedor, String Observaciones) throws RemoteException {
		return manager.updateProducto(identificador, String.valueOf(idProducto), Descripcion, String.valueOf(CantidadDisponible), String.valueOf(CantidadMinima), String.valueOf(PrecioVenta), Proveedor, Observaciones);
	}

	/**
	 * Metodo que elimina un producto de la base de datos
	 */
	public boolean deleteProducto(String identificador, int idProducto) throws RemoteException {
		return manager.deleteProducto(identificador, String.valueOf(idProducto));
	}

}
