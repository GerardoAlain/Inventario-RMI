package servidor.edu.presentacion;

import java.rmi.RemoteException;
import java.rmi.Remote;


import java.util.Vector;

/**
 * Esta clase es una representacion de los metodos que pueden
 * ser accesados por los clientes remotos. Esta clase hereda
 * de <i>Remote</i> y es una interface de <i>ServidorInventario</i>.
 * Tal especificacion se hace para que sea compatible con
 * la tecnologia RMI que es aplicada a la comunicacion entre el
 * cliente y el servidor de esta aplicacion.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public interface IServidorInventario extends Remote {

	/**
	 * Este metodo remoto se encarga de validar y crear una sesion en el servidor remoto.
	 * Retorna el String de identificacion unico para la realizacion de las transacciones
	 * en el servidor.
	 * @param usuario				El usuario a conectarse al servidor
	 * @param password				El password del usuario
	 * @return						El Identificador Unico de Sesion o null si no es valido
	 * @throws RemoteException
	 */
	public String conectarConServidor(String usuario, char[] password)
		throws RemoteException;
	
	/**
	 * Este metodo remoto se encarga de desconectarse del servidor.
	 * Debe eliminar el string de identificacion del usuario.
	 * @param identificador			El Identificador Unico de Sesion
	 * @throws RemoteException
	 */
	public void desconectarDelServidor(String identificador) 
		throws RemoteException;

	/**
	 * Este metodo remoto retorna todos los productos registrados
	 * en la base de datos, empaquetados en un Vector y estos a la
	 * vez empaquetados en otro Vector.
	 * @param identificador			Identificador Unico de Sesion
	 * @param tipoOrdenacion		Tipo de ordenacion de la tabla
	 * @return						El Vector que contiene todos los productos
	 * @throws RemoteException
	 */
	public Vector getAllItems(String identificador, String tipoOrdenacion) 
		throws RemoteException;
	
	/**
	 * Este metodo remoto retorna el producto identificado con el
	 * parametro idProducto empaquetado en un vector.
	 * @param identificador			Identificador Unico de Sesion
	 * @param idProducto			Identificador del producto
	 * @return						El Vector que contiene todos los datos del producto
	 * @throws RemoteException
	 */
	public Vector getProducto(String identificador, int idProducto)
		throws RemoteException;

	/**
	 * Este metodo remoto retorna el nombre del usuario identificado
	 * con el parametro.
	 * @param identificador			Identificador Unico de Sesion
	 * @return						Nombre del usuario
	 * @throws RemoteException
	 */
	public String getUserName(String identificador) 
		throws RemoteException;

	/**
	 * Este metodo remoto retorna la fecha de ultimo acceso del
	 * usuario identificado con el parametro.
	 * @param identificador			Identificador Unico de Sesion
	 * @return						Fecha de ultimo acceso
	 * @throws RemoteException
	 */
	public String getUserLastAccess(String identificador) 
		throws RemoteException;

	/**
	 * Metodo remoto que agrega un nuevo producto a la base de datos.
	 * @param identificador			Identificador Unico de Sesion
	 * @param ID					ID del producto
	 * @param Descripcion			Descripcion del producto
	 * @param CantidadDisponible	Cantidad disponible del producto
	 * @param CantidadMinima		Cantidad minima del producto (para efectuar pedido)
	 * @param PrecioVenta			Precio de venta del producto
	 * @param Proveedor				Proveedor del producto
	 * @param Observaciones			Observaciones del producto (puede ser null)
	 * @return						id del producto en la base de datos
	 * @throws RemoteException
	 */
	public int addProducto(
		String identificador,
		String ID,
		String Descripcion,
		int CantidadDisponible,
		int CantidadMinima,
		int PrecioVenta,
		String Proveedor,
		String Observaciones) throws RemoteException;
	
	/**
	 * Metodo remoto que modifica un producto existente en la base de datos.
	 * @param identificador			Identificador Unico de Sesion
	 * @param idProducto			ID del producto a modificar
	 * @param Descripcion			Descripcion del producto
	 * @param CantidadDisponible	Cantidad disponible del producto
	 * @param CantidadMinima		Cantidad minima del producto (para efectuar pedido)
	 * @param PrecioVenta			Precio de venta del producto
	 * @param Proveedor				Proveedor del producto
	 * @param Observaciones			Observaciones del producto (puede ser null)
	 * @return						True si fue actualizado correctamente, False de lo contrario
	 * @throws RemoteException
	 */
	public boolean updateProducto(
			String identificador, 
			int idProducto, 
			String Descripcion, 
			int CantidadDisponible, 
			int CantidadMinima, 
			int PrecioVenta, 
			String Proveedor, 
			String Observaciones) throws RemoteException;

	/**
	 * Metodo remoto que elimina un producto de la base de datos.
	 * @param identificador			Identificador Unico de Sesion
	 * @param idProducto			Id del producto en la base de datos
	 * @return						True si fue eliminado correctamente, False de lo contrario
	 * @throws RemoteException
	 */
	public boolean deleteProducto(String identificador, int idProducto)
		throws RemoteException;

}
