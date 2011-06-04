package cliente.edu.logica.command;

import java.rmi.RemoteException;
import java.util.Stack;
import java.util.Vector;

import cliente.edu.logica.builder.MainPanelBuilder;
import cliente.edu.presentacion.ConexionServidor;
import cliente.edu.presentacion.Inventario;

import servidor.edu.presentacion.IServidorInventario;

/**
 * Si se quiere ejecutar un comando se ha de llamar especifcamente a esta clase
 * ya que es la encargada de llamar al comando <i>Command</i> para la ejecucion
 * de un comando en la aplicacion.<p>
 * 
 * Ademas esta clase implementa el patron <i>State</i> de una manera muy simple
 * logrando con esto implementar la funcionalidad de <b>Deshacer</b> un comando.<p>
 * 
 * Esta clase hace parte del patron <i>Command</i> e implementa el patron <i>Singleton</i>.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */

public class ManejadorComandos {

	/**
	 * Representa la instancia unica de la clase. 
	 * @uml.property name="iComando"
	 * @uml.associationEnd aggregation="aggregate" inverse="manejadorComandos:cliente.edu.logica.command.IComando" multiplicity="(0 1)"
	 */
	private IComando comando;

	/**
	 *  
	 * @uml.property name="manejadorComandos"
	 * @uml.associationEnd inverse="manejadorComandos1:cliente.edu.logica.command.ManejadorComandos" multiplicity="(0 1)"
	 */
	private static ManejadorComandos manejadorComandos;

	/**
	 * Representa una instancia del servidor remoto
	 */
	private IServidorInventario servidor;
	
	/**
	 * Representa la pila de acciones de deshacer
	 */
	private Stack pilaComandosDeshacer;
	
	/**
	 * Constructor de la clase, instancia una nueva pila
	 * de comandos de deshacer.
	 */
	private ManejadorComandos() {
		pilaComandosDeshacer = new Stack();
	}
	
	/**
	 * Captura el estado de la aplicacion y lo
	 * almacena en la pila de deshacer
	 */
	public void setMomento() {
		try {// Obtenemos el primer estado de la aplicacion
			servidor = ConexionServidor.getServidor();
			pilaComandosDeshacer.push(servidor.getAllItems(ConexionServidor.getIdentificadorUnico(), Inventario.getOrdenamientoDatos()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que ejecuta el comando agregarItem.
	 * Captura el estado.
	 */
	public void agregarItem() {
		if(ConexionServidor.estaConectado()) {
			comando = new AgregarItem();
			setMomento();
		}
		else
			Inventario.setStatusBarText("No esta conectado!!!");
	}

	/**
	 * Metodo que ejecuta el comando eliminarItem.
	 * Captura el estado.
	 */
	public void eliminarItem() {
		if(ConexionServidor.estaConectado()) {
			comando = new EliminarItem();
			setMomento();
		}
		else
			Inventario.setStatusBarText("No esta conectado!!!");
	}

	/**
	 * Metodo que ejecuta el comando buscarItem.
	 */
	public void buscarItem() {
		if(ConexionServidor.estaConectado())
			comando = new BuscarItem();
		else
			Inventario.setStatusBarText("No esta conectado!!!");
	}

	/**
	 * Metodo que ejecuta el comando ingresarUnidades.
	 * Captura el estado.
	 */
	public void ingresarUnidades() {
		if(ConexionServidor.estaConectado()) {
			comando = new IngresarUnidades();
			setMomento();
		}
		else
			Inventario.setStatusBarText("No esta conectado!!!");
	}

	/**
	 * Metodo que ejecuta el comando extraerUnidades.
	 * Captura el estado.
	 */
	public void extraerUnidades() {
		if(ConexionServidor.estaConectado()) {
			comando = new ExtraerUnidades();
			setMomento();
		}
		else
			Inventario.setStatusBarText("No esta conectado!!!");
	}
	
	/**
	 * Metodo que deshace la ultima accion almacenada en
	 * la pila de eventos.
	 */
	public void deshacer() {
		if(pilaComandosDeshacer.size() > 1){
			// Almacenamos el momento de rehacer
			Vector productosF = (Vector) pilaComandosDeshacer.pop();
			Vector productosI = (Vector) pilaComandosDeshacer.peek();
		
			if(!productosF.equals(productosI) && productosF.size() == productosI.size()) {
				// Fue una actualizacion de un campo
				for(int i=0; i < productosI.size(); i++) {
					Vector detallesF = (Vector) productosF.elementAt(i);
					Vector detallesI = (Vector) productosI.elementAt(i);
					
					if(!detallesF.equals(detallesI)) {
						try {
							servidor.deleteProducto(ConexionServidor.getIdentificadorUnico(), Integer.valueOf((String)detallesF.elementAt(0)).intValue());
							servidor.addProducto(ConexionServidor.getIdentificadorUnico(),
													(String) detallesI.elementAt(0),
													(String) detallesI.elementAt(1),
													Integer.valueOf((String) detallesI.elementAt(2)).intValue(),
													Integer.valueOf((String) detallesI.elementAt(3)).intValue(),
													Integer.valueOf((String) detallesI.elementAt(4)).intValue(),
													(String) detallesI.elementAt(5),
													(String) detallesI.elementAt(6));
							MainPanelBuilder.setDatos(servidor.getAllItems(ConexionServidor.getIdentificadorUnico(), Inventario.getOrdenamientoDatos()));
							Inventario.setStatusBarText("Ultima accion deshecha!!!");
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			if(!productosF.equals(productosI) && productosF.size() > productosI.size()) {
				// Se agrego un producto nuevo
				Vector detallesF = (Vector) productosF.lastElement();
				
				try {
					servidor.deleteProducto(ConexionServidor.getIdentificadorUnico(), Integer.valueOf((String) detallesF.elementAt(0)).intValue());
					MainPanelBuilder.setDatos(servidor.getAllItems(ConexionServidor.getIdentificadorUnico(), Inventario.getOrdenamientoDatos()));
					Inventario.setStatusBarText("Ultima accion deshecha!!!");
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
			if(!productosF.equals(productosI) && productosF.size() < productosI.size()) {
				// Se elimino un producto
				Vector detallesI = new Vector();
				Vector detallesF = new Vector();
				for(int i=0; i < productosI.size(); i++) {
					if(productosF.size() == i)
						detallesI = (Vector) productosI.lastElement();
					else {
						detallesF = (Vector) productosF.elementAt(i);
						detallesI = (Vector) productosI.elementAt(i);
					}					
					
					if(!detallesF.equals(detallesI)) {
						try {
							servidor.addProducto(ConexionServidor.getIdentificadorUnico(),
													(String) detallesI.elementAt(0),
													(String) detallesI.elementAt(1),
													Integer.valueOf((String) detallesI.elementAt(2)).intValue(),
													Integer.valueOf((String) detallesI.elementAt(3)).intValue(),
													Integer.valueOf((String) detallesI.elementAt(4)).intValue(),
													(String) detallesI.elementAt(5),
													(String) detallesI.elementAt(6));
							i = productosF.size();
							MainPanelBuilder.setDatos(servidor.getAllItems(ConexionServidor.getIdentificadorUnico(), Inventario.getOrdenamientoDatos()));
							Inventario.setStatusBarText("Ultima accion deshecha!!!");
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
			} 
		} else 
			Inventario.setStatusBarText("No se puede deshacer!!!");
	}
	
	/**
	 * Retorna una unica instancia de la clase, haciendo uso del patron <i>Singleton</i>.
	 * @return		Instancia unica de la clase.
	 */
	public static ManejadorComandos getInstancia() {
		if(manejadorComandos == null && ConexionServidor.estaConectado())
			manejadorComandos = new ManejadorComandos();
		return manejadorComandos;
	}

}
