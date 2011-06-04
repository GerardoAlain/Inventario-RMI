package servidor.edu.repositorio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Esta clase se encarga de proveer <b>Todos</b> los metodos
 * para el acceso a los datos en la base de datos.<p>
 * 
 * Asegura la modificacion de los datos mediante la comprobacion
 * de un ID unico de sesion el cual es generado para cada usuario
 * que se valide en la base de datos.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class DatabaseManager {
	
	/**
	 * Representa la conexion con la base de datos.
	 */
	private Connection conexion;
	
	/**
	 * Representa la operacion hecha en la base de datos
	 */
	private Statement operacion;
	
	/**
	 * Representa el resultado de la operacion hecha.
	 */
	private ResultSet resultadoOperacion;
	
	/**
	 * Representa el origen de la base de datos 
	 */
	private String DBSource = new String("jdbc:mysql://localhost/inventario");
	
	/**
	 * Representa el usuario de la base de datos
	 */
	private String DBUser = new String("usuario");
	
	/**
	 * Representa el password de la base de datos
	 */
	private String DBPass = new String("password");

	/**
	 * Este metodo crea una conexion a la base de datos.
	 * @return 					True si la conexion fue hecha, False de lo contrario.
	 */
	public boolean conectar() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conexion = DriverManager.getConnection(DBSource, DBUser, DBPass);
			
			if(conexion != null)
				return true;
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Este metodo cierra todos los flujos abiertos durante la conexion a la base de 
	 * datos y cierra la conexion liberando toda la memoria utilizada
	 */
	public void desconectar() {
		try {
			// Cerramos las conexiones que generamos para la consulta
			resultadoOperacion.close();
			operacion.close();
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Este metodo valida la informacion de un usuario que quiere acceder al servidor, 
	 * este metodo solo puede ser llamdo por el metodo iniciarSesion()
	 */
	private boolean validarUsuario(String Usuario, String Password) {
		boolean flag = false;
		
		try {
			operacion = conexion.createStatement();
			resultadoOperacion = operacion.executeQuery("SELECT `Usuario` , `Password`" +
					"FROM `usuarios` WHERE 1 AND `Usuario` = '"+Usuario+"' AND " +
					"`Password` = '"+Password+"' LIMIT 0,1");
			if(resultadoOperacion.next())
				flag = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return flag;
	}

	/**
	 * Este metodo se encarga de crear una sesion y retornar el identificador unico de sesion 
	 * con el cual se deben hacer TODAS las operaciones dentro de la base de datos
	 */
	public String iniciarSesion(String Usuario, char[] Password) {
		if(validarUsuario(Usuario, String.valueOf(Password))) {
			// Generamos una cadena al azar para utilizarla como identificador de sesion
			String cadena = String.valueOf(Math.round(Math.random()*10000));
			// Registramos la cadena en la base de datos
			try {
				operacion = conexion.createStatement();
				operacion.executeUpdate("UPDATE `usuarios` SET " +
						"`IdentificadorRemoto` = '"+cadena+"' WHERE `Usuario` = '"+Usuario+"' LIMIT 1 ;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return cadena;
		}
		else {
			// Usuario no valido
			return null;
		}
	}

	/**
	 * Este metodo cierra la sesion del usuario identificado con
	 * el parametro, y actualiza la fecha de ultimo acceso.
	 * @param Identificador			Identificador Unico de Sesion
	 */
	public void terminarSesion(String Identificador) {
		// Obtenemos del sistema la fecha y la hora actual
		String FechaHora = DateFormat.getDateInstance(0).format(new Date()) +" "+ DateFormat.getTimeInstance().format(new Date());
		// Generamos una cadena aleatoria desconocida por el usuario
		String cadena = String.valueOf(Math.round(Math.random()*10000));

		try {
			operacion = conexion.createStatement();
			operacion.executeUpdate("UPDATE `usuarios` SET `IdentificadorRemoto` = '"+cadena+"', " +
					"`UltimoAcceso` = '"+FechaHora+"' WHERE `IdentificadorRemoto` = '"+Identificador+"' LIMIT 1 ;");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Este metodo se encarga de validar si el Identificador pasado por parametro existe
	 * en la base de datos, con el fin de corroborar que no se acceda indebidamente a los
	 * metodos del servidor.
	 * @param Identificador			Identificador Unico de Sesion
	 * @return						True si el identificador es valido, False si no lo es
	 */
	private boolean validarIdentificador(String Identificador) {
		try {
			operacion = conexion.createStatement();
			resultadoOperacion = operacion.executeQuery("SELECT `IdentificadorRemoto`" +
					"FROM `usuarios` WHERE 1 AND `IdentificadorRemoto` = '"+Identificador+"' LIMIT 0 , 1");
			
			if(resultadoOperacion.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Metodo que al ser llamado retorna el ultimo acceso del usuario identificado
	 * con el parametro recibido.
	 * @param Identificador			Identificador Unico de Sesion
	 * @return						Ultimo Acceso
	 */
	public String getUltimoAcceso(String Identificador) {
		String x = null;
		try {
			operacion = conexion.createStatement();
			resultadoOperacion = operacion.executeQuery("SELECT `UltimoAcceso` FROM `usuarios` " +
					"WHERE 1 AND `IdentificadorRemoto` = '"+Identificador+"' LIMIT 0 , 1");
			if(resultadoOperacion.next())
				x = resultadoOperacion.getString("UltimoAcceso");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return x;
	}

	/**
	 * Metodo que al ser llamado retorna el nombre del usuario identificado
	 * con el parametro recibido.
	 * @param Identificador			Identificador Unico de Sesion
	 * @return						Nombre del usuario
	 */
	public String getNombreUsuario(String Identificador) {
		String x = null;
		try {
			operacion = conexion.createStatement();
			resultadoOperacion = operacion.executeQuery("SELECT `Nombre` FROM `usuarios` " +
					"WHERE 1 AND `IdentificadorRemoto` = '"+Identificador+"' LIMIT 0 , 1");
			if(resultadoOperacion.next())
				x = resultadoOperacion.getString("Nombre");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return x;
	}
	
	/**
	 * Metodo que al ser llamado retorna el usuario identificado con
	 * el parametro recibido.
	 * @param Identificador			Identificador Unico de Sesion
	 * @return						Usuario
	 */
	public String getUsuario(String Identificador) {
		String x = null;
		try {
			operacion = conexion.createStatement();
			resultadoOperacion = operacion.executeQuery("SELECT `Usuario` FROM `usuarios` " +
					"WHERE 1 AND `IdentificadorRemoto` = '"+Identificador+"' LIMIT 0 , 1");
			if(resultadoOperacion.next())
				x = resultadoOperacion.getString("Usuario");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return x;
	}

	//*******************Metodos de acceso a datos **************************************//

	/**
	 * Retorna el producto indicado por los parametros.
	 * @param Identificador		Identificador Unico de Sesion
	 * @param idProducto		ID del producto a retornar
	 * @return					Vector con la informacion del producto
	 */
	public Vector getProducto(String Identificador, String idProducto) {
		Vector data = new Vector();
		
		if(validarIdentificador(Identificador)) {
			try {
				operacion = conexion.createStatement();
				resultadoOperacion = operacion.executeQuery("SELECT * FROM `stock` WHERE 1 AND " +
						"`id` = '"+idProducto+"' LIMIT 0 , 1");
				
				while(resultadoOperacion.next()) {
					data.add(resultadoOperacion.getString("id"));
					data.add(resultadoOperacion.getString("Descripcion"));
					data.add(resultadoOperacion.getString("CantidadDisponible"));
					data.add(resultadoOperacion.getString("CantidadMinima"));
					data.add(resultadoOperacion.getString("PrecioVenta"));
					data.add(resultadoOperacion.getString("Proveedor"));
					data.add(resultadoOperacion.getString("Observaciones"));
					data.add(resultadoOperacion.getString("UsuarioModifico"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return data;
		}
		return null;			
	}

	/**
	 * Metodo que retorna todos los productos de la base de datos, ordenados
	 * segun el parametro tipoOrdenacion.
	 * @param Identificador			Identificador Unico de Sesion
	 * @param tipoOdenacion			Tipo de ordenacion de los datos
	 * @return						Vector bidimensional con la informacion obtenida
	 */
	public Vector getAllProductos(String Identificador, String tipoOrdenacion) {
		Vector data = new Vector();
		Vector allData = new Vector();
		
		if(validarIdentificador(Identificador)) {
			try {
				operacion = conexion.createStatement();
				resultadoOperacion = operacion.executeQuery("SELECT * FROM `stock` ORDER BY `"+tipoOrdenacion+"` ASC");
				
				while(resultadoOperacion.next()) {
					data.add(resultadoOperacion.getString("id"));
					data.add(resultadoOperacion.getString("Descripcion"));
					data.add(resultadoOperacion.getString("CantidadDisponible"));
					data.add(resultadoOperacion.getString("CantidadMinima"));
					data.add(resultadoOperacion.getString("PrecioVenta"));
					data.add(resultadoOperacion.getString("Proveedor"));
					data.add(resultadoOperacion.getString("Observaciones"));
					data.add(resultadoOperacion.getString("UsuarioModifico"));
					allData.add(data.clone());
					data.clear();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return allData;
		}
		return null;
	}
	
	/**
	 * Metodo que agrega un producto nuevo con los parametros especificados,
	 * Este metodo retorna el id del elemento agregado.
	 * @param Identificador			Identificador Unico de Sesion
	 * @param ID					ID del producto
	 * @param Descripcion			Descripcion del articulo
	 * @param CantidadDisponible	Cantidad Disponible
	 * @param CantidadMinima		Cantidad Minima
	 * @param PrecioVenta			Precio Venta
	 * @param Proveedor				Proveedor
	 * @param Observaciones			Observaciones
	 * @return						ID del producto agregado
	 */
	public int addProducto(String Identificador, String ID, String Descripcion, String CantidadDisponible, String CantidadMinima, String PrecioVenta, String Proveedor, String Observaciones) {
		Integer resultadoUpdate = new Integer(0);
		
		if(validarIdentificador(Identificador)) {
			try {
				operacion = conexion.createStatement();
				operacion.executeUpdate("INSERT INTO `stock` ( `id` , `Descripcion` , " +
						"`CantidadDisponible` , `CantidadMinima` , `PrecioVenta` , `Proveedor` , " +
						"`Observaciones` , `UsuarioModifico` )" +
						"VALUES ('"+ID+"', '"+Descripcion+"', '"+CantidadDisponible+"', '"+CantidadMinima+
						"', '"+PrecioVenta+"', '"+Proveedor+"', '"+Observaciones+"', '"+getUsuario(Identificador)+"');");
				
				resultadoOperacion = operacion.executeQuery("SELECT `id` FROM `stock` WHERE 1 AND" +
						" `Descripcion` = '"+Descripcion+"' AND `CantidadDisponible` = '"+CantidadDisponible+"' " +
						"AND `CantidadMinima` = '"+CantidadMinima+"' AND `PrecioVenta` = '"+PrecioVenta+"' " +
						"AND `Proveedor` = '"+Proveedor+"' AND `Observaciones` = '"+Observaciones+"' " +
						"AND `UsuarioModifico` = '"+getUsuario(Identificador)+"' LIMIT 0 , 1");
				if(resultadoOperacion.next()) {
					String id = resultadoOperacion.getString("id");
					resultadoUpdate = Integer.valueOf(id); 
				}
			}
			 catch (SQLException e) {
			 	e.printStackTrace();
			 }
		}
		return resultadoUpdate.intValue();
	}
	
	/**
	 * Metodo que actualiza los datos del producto identificado con el
	 * parametro idProducto.
	 * @param Identificador				Identificador Unico de Sesion
	 * @param idProducto				ID del producto a modificar
	 * @param Descripcion				Descripcion del producto
	 * @param CantidadDisponible		Cantidad Disponible del producto
	 * @param CantidadMinima			Cantidad Minima del producto
	 * @param PrecioVenta				Precio de venta del producto
	 * @param Proveedor					Proveedor del producto
	 * @param Observaciones				Observaciones del producto
	 * @return							True si la actualizacion fue exitosa, False de lo contrario
	 */
	public boolean updateProducto(String Identificador, String idProducto, String Descripcion, String CantidadDisponible, String CantidadMinima, String PrecioVenta, String Proveedor, String Observaciones) {
		int resultado;
		
		if(validarIdentificador(Identificador)) {
			try {
				operacion = conexion.createStatement();
				resultado = operacion.executeUpdate("UPDATE `stock` SET `Descripcion` = '"+Descripcion+"'," +
						"`CantidadDisponible` = '"+CantidadDisponible+"'," +
						"`CantidadMinima` = '"+CantidadMinima+"'," +
						"`PrecioVenta` = '"+PrecioVenta+"'," +
						"`Proveedor` = '"+Proveedor+"'," +
						"`Observaciones` = '"+Observaciones+"'," +
						"`UsuarioModifico` = '"+getUsuario(Identificador)+"' WHERE `id` = '"+idProducto+"' LIMIT 1 ;");
				if(resultado == 1)
					return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Metodo que elimina un elemento de la base de datos
	 * dependiendo del parametro idProducto recibido.
	 * @param Identificador		Identificador Unico de Sesion
	 * @param idProducto		ID del producto a eliminar
	 * @return
	 */
	public boolean deleteProducto(String Identificador, String idProducto) {
		int resultado;
		
		if(validarIdentificador(Identificador)) {
			try {
				operacion = conexion.createStatement();
				resultado = operacion.executeUpdate("DELETE FROM `stock` WHERE `id` = '"+idProducto+"' LIMIT 1");
				if(resultado == 1)
					return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
}
