package cliente.edu.logica.command;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import servidor.edu.presentacion.IServidorInventario;
import cliente.edu.logica.builder.MainPanelBuilder;
import cliente.edu.presentacion.ConexionServidor;
import cliente.edu.presentacion.Inventario;

/**
 * Esta clase representa el comando Ingresar Unidades, el cual
 * aumenta unidades de las existencias del producto seleccionado
 * en la tabla.<p>
 * 
 * Esta clase hace parte del patron <i>Command</i>.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class IngresarUnidades extends JDialog implements IComando, KeyListener {
	
	/**
	 * Representa la etiqueta "Unidades a Ingresar"
	 */
	private JLabel ingresarLbl = new JLabel("Unidades a Ingresar:", JLabel.CENTER);
	
	/**
	 * Representa el campo de entrada de datos
	 */
	private JTextField ingresarFdl = new JTextField();
	
	/**
	 * Representa el boton aceptar
	 */
	private JButton aceptarBtn = new JButton("Aceptar");
	
	/**
	 * Representa el boton cancelar
	 */
	private JButton cancelarBtn = new JButton("Cancelar");
	
	/**
	 * Constructor de la clase, agrega los componentes y le
	 * agrega un ActionListener al boton aceptar el cual
	 * se conecta con el servidor y actualiza el producto
	 * a modificar.
	 */
	public IngresarUnidades() {
		setLayout(new GridLayout(2,2));
		
		aceptarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setStatusBarText("Se dispone a ingresar unidades de un producto!!!");
				if(Integer.valueOf(ingresarFdl.getText()).intValue() <= 0){
					Inventario.setStatusBarText("Debe ingresar un numero positivo mayor que cero!!!");
				}
				else {
					if(MainPanelBuilder.getIDproductoSeleccionado() != 0) {
						try {
							IServidorInventario servidor = ConexionServidor.getServidor();
							int IDproducto = MainPanelBuilder.getIDproductoSeleccionado();
							Vector producto = servidor.getProducto(ConexionServidor.getIdentificadorUnico(), IDproducto);
					
							int cantidadDisponible = Integer.valueOf((String) producto.get(2)).intValue();
							int cantidadAAumentar = Integer.valueOf(ingresarFdl.getText()).intValue();
					
							cantidadDisponible += cantidadAAumentar;
							servidor.updateProducto(ConexionServidor.getIdentificadorUnico(),
													IDproducto,
													(String) producto.get(1),
													cantidadDisponible,
													Integer.valueOf((String)producto.get(3)).intValue(),
													Integer.valueOf((String)producto.get(4)).intValue(),
													(String) producto.get(5),
													(String) producto.get(6));
							MainPanelBuilder.setDatos(servidor.getAllItems(ConexionServidor.getIdentificadorUnico(), Inventario.getOrdenamientoDatos()));
							Inventario.setStatusBarText(cantidadAAumentar+" Unidades Agregadas!!!");
						} catch (RemoteException e1) {
							e1.printStackTrace();
						} catch (NumberFormatException e1) {
							Inventario.setStatusBarText("Debe ingresar datos validos!!!");;
						}
					}
					else
						Inventario.setStatusBarText("Debe seleccionar un elemento!!!");
				}
				dispose();
			}
		});
		
		cancelarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setStatusBarText("Accion Cancelada!!!");
				dispose();
			}
		});
		
		add(ingresarLbl);
		add(ingresarFdl);
		add(aceptarBtn);
		add(cancelarBtn);
		ingresarFdl.addKeyListener(this);
		
		if(ConexionServidor.estaConectado()) {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setSize(250,80);
			setTitle("Ingresar Unidades");
			setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-125, Toolkit.getDefaultToolkit().getScreenSize().height/2-40);
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
