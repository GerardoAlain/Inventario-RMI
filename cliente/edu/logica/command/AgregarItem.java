package cliente.edu.logica.command;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import servidor.edu.presentacion.IServidorInventario;
import cliente.edu.logica.builder.MainPanelBuilder;
import cliente.edu.presentacion.ConexionServidor;
import cliente.edu.presentacion.Inventario;

/**
 * Esta clase representa el comando Agregar Item, la cual
 * muestra en pantalla un dialogo para la insercion de datos
 * y posteriormente se conecta al servidor para crear el nuevo
 * elemento.<p>
 * 
 * Esta clase hace parte del patron <i>Command</i>.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class AgregarItem extends JDialog implements IComando, KeyListener {

	/**
	 * Representa la etiqueta "Descripcion"
	 */
	private JLabel descripcionLbl = new JLabel("Descripcion:", JLabel.CENTER);
	
	/**
	 * Representa la etiqueta "Cantidad Disponible"
	 */
	private JLabel cantidadDisponibleLbl = new JLabel("Cantidad Disponible:", JLabel.CENTER);
	
	/**
	 * Representa la etiqueta "Cantidad Minima"
	 */
	private JLabel cantidadMinimaLbl = new JLabel("Cantidad Minima:", JLabel.CENTER);
	
	/**
	 * Representa la etiqueta "Precio Venta"
	 */
	private JLabel precioVentaLbl = new JLabel("Precio Venta:", JLabel.CENTER);
	
	/**
	 * Representa la etiqueta "Proveedor"
	 */
	private JLabel proveedorLbl = new JLabel("Proveedor:", JLabel.CENTER);
	
	/**
	 * Representa la etiqueta "Observaciones"
	 */
	private JLabel observacionesLbl = new JLabel("Observaciones:", JLabel.CENTER);

	
	/**
	 * Representa la caja de texto de Descripcion
	 */
	private JTextField descripcionFld = new JTextField();
	
	/**
	 * Representa la caja de texto de Cantidad Disponible
	 */
	private JTextField cantidadDisponibleFld = new JTextField();
	
	/**
	 * Representa la caja de texto de Cantidad Minima
	 */
	private JTextField cantidadMinimaFdl = new JTextField();
	
	/**
	 * Representa la caja de texto de Precio Venta
	 */
	private JTextField precioVentaFdl = new JTextField();
	
	/**
	 * Representa la caja de texto de Proveedor
	 */
	private JTextField proveedorFdl = new JTextField();
	
	/**
	 * Representa la caja de texto de Observaciones
	 */
	private JTextField observacionesFdl = new JTextField();
	
	
	/**
	 * Representa el boton de aceptar.
	 */
	private JButton aceptarBtn = new JButton("Aceptar");
	
	/**
	 * Representa el boton de cancelar.
	 */
	private JButton cancelarBtn = new JButton("Cancelar");
	
	/**
	 * Constructor de la clase, crea la ventana y le agrega
	 * actionListener a los respectivos botones.<p>
	 * 
	 * El actionListener del boton aceptar hace que
	 * se conecte al servidor y agregue el nuevo
	 * producto con los datos insertados.
	 */
	public AgregarItem() {
		setLayout(new GridLayout(7,2));
		
		aceptarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setStatusBarText("Se esta agregando un nuevo producto!!!");
				try {
					IServidorInventario servidor = ConexionServidor.getServidor(); 
					servidor.addProducto(ConexionServidor.getIdentificadorUnico(),
																"",
																descripcionFld.getText(),
																Integer.valueOf(cantidadDisponibleFld.getText()).intValue(),
																Integer.valueOf(cantidadMinimaFdl.getText()).intValue(),
																Integer.valueOf(precioVentaFdl.getText()).intValue(),
																proveedorFdl.getText(),
																observacionesFdl.getText());
					MainPanelBuilder.setDatos(servidor.getAllItems(ConexionServidor.getIdentificadorUnico(), Inventario.getOrdenamientoDatos()));
					Inventario.setStatusBarText("Producto Agregado!!!");
				} catch (NumberFormatException e1) {
					Inventario.setStatusBarText("Error en la validacion de los datos!!!");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				dispose();
			}
		});
		
		cancelarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setStatusBarText("Se ha cancelado la operacion!!!");
				dispose();
			}
		});
		
		add(descripcionLbl);
		add(descripcionFld);
		add(cantidadDisponibleLbl);
		add(cantidadDisponibleFld);
		add(cantidadMinimaLbl);
		add(cantidadMinimaFdl);
		add(precioVentaLbl);
		add(precioVentaFdl);
		add(proveedorLbl);
		add(proveedorFdl);
		add(observacionesLbl);
		add(observacionesFdl);
		add(aceptarBtn);
		add(cancelarBtn);
		
		descripcionFld.addKeyListener(this);
		cantidadDisponibleFld.addKeyListener(this);
		cantidadMinimaFdl.addKeyListener(this);
		precioVentaFdl.addKeyListener(this);
		proveedorFdl.addKeyListener(this);
		observacionesFdl.addKeyListener(this);

		if(ConexionServidor.estaConectado()) {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setSize(250,250);
			setTitle("Agregar Producto");
			setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-125, Toolkit.getDefaultToolkit().getScreenSize().height/2-125);
			setResizable(false);
			setModal(true);
			setAlwaysOnTop(true);
			setVisible(true);
		}
		else {
			Inventario.setStatusBarText("No esta conectado al servidor!!!");
			dispose();
		}
	}
	
	/**
	 * Listener de teclado para el dialogo de peticion de datos.
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			aceptarBtn.doClick();
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			cancelarBtn.doClick();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

}
