package cliente.edu.logica.command;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import servidor.edu.presentacion.IServidorInventario;

import cliente.edu.logica.builder.MainPanelBuilder;
import cliente.edu.presentacion.ConexionServidor;
import cliente.edu.presentacion.Inventario;

/**
 * Esta clase representa el comando Eliminar, el cual
 * toma la fila seleccionada en la tabla y la elimina.<p>
 * 
 * Esta clase hace parte del patron <i>Command</i>.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class EliminarItem implements IComando {

	/**
	 * Representa el ID del producto a eliminar
	 */
	private int IDproducto;
	
	/**
	 * Representa el mensaje de confirmacion
	 */
	private String mensaje;
	
	/**
	 * Constructor de la clase, se conecta al servidor y obtiene
	 * los datos del elemento a eliminar, luego muestra una ventana
	 * de confirmacion y posteriormente elimina el elemento de la
	 * base de datos.
	 */
	public EliminarItem() {
		if(ConexionServidor.estaConectado()) {
			Inventario.setStatusBarText("Se dispone a eliminar un producto!!!");
			if(MainPanelBuilder.getIDproductoSeleccionado() != 0) {
				try {
					IServidorInventario servidor = ConexionServidor.getServidor();
					IDproducto = MainPanelBuilder.getIDproductoSeleccionado();
					mensaje = servidor.getProducto(ConexionServidor.getIdentificadorUnico(), IDproducto).toString();
					
					int choice = JOptionPane.showConfirmDialog(null, mensaje, "Desea eliminar el siguiente elemento?", JOptionPane.OK_CANCEL_OPTION);
				
					if(choice == JOptionPane.OK_OPTION) {
						servidor.deleteProducto(ConexionServidor.getIdentificadorUnico(), IDproducto);
						MainPanelBuilder.setDatos(servidor.getAllItems(ConexionServidor.getIdentificadorUnico(), Inventario.getOrdenamientoDatos()));
						Inventario.setStatusBarText("Producto Eliminado!!!");
					}
					else
						Inventario.setStatusBarText("Accion Cancelada!!!");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
			else
				Inventario.setStatusBarText("Debe Seleccionar un elemento!!!");
			}
		else {
			Inventario.setStatusBarText("No esta conectado al servidor!!!");
		}
	}
	
}
