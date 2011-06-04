package cliente.edu.repositorio;

import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import cliente.edu.logica.command.ManejadorComandos;
import cliente.edu.presentacion.ConexionServidor;
import cliente.edu.presentacion.Inventario;

/**
 * Esta clase representa el modelo de datos para la tabla <p>
 * 
 * Fue desarrollada tras duro trabajo, pero ofrece una funcionalidad
 * unica a la tabla, ya que provee todos los metodos necesarios para
 * controlar la tabla y los datos que esta contiene.<p>
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class ModeloTablaInventario extends AbstractTableModel {

	/**
	 * Representa los nombres de las columnas de la tabla
	 */
	private Vector nombresColumnas = new Vector(); 
	
	/**
	 * Representa los datos de la tabla
	 */
	private Vector datosFilas = new Vector();
	
	/**
	 * Constructor de la clase, les da nombres a las columnas
	 * de la tabla.
	 */
	public ModeloTablaInventario() {
		nombresColumnas.add("Producto #");
		nombresColumnas.add("Descripcion");
		nombresColumnas.add("Cant. Disponible");
		nombresColumnas.add("Cant. Minima");
		nombresColumnas.add("Precio Venta");
		nombresColumnas.add("Proveedor");
		nombresColumnas.add("Observacion");
		nombresColumnas.add("Modifico");
		fireTableStructureChanged();
	}

	/**
	 * Metodo que actualiza los datos de la tabla por los del parametro
	 * @param data		Datos a actualizar en la tabla
	 */
	public void setDataVector(Vector data) {
		this.datosFilas = data;
		fireTableStructureChanged();
	}

	/**
	 * Metodo que retorna la cantidad de filas.
	 */
	public int getRowCount() {
		return datosFilas.size();
	}

	/**
	 * Metodo que retorna la cantidad de columnas.
	 */
	public int getColumnCount() {
		return nombresColumnas.size();
	}
	
	/**
	 * Metodo que retorna el nombre de las columnas.
	 */
	public String getColumnName(int column) {
		return (String) nombresColumnas.elementAt(column);
	}

	/**
	 * Metodo que retorna el objeto situado en los parametros.
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Vector vector = (Vector) datosFilas.elementAt(rowIndex);
		return vector.elementAt(columnIndex);
	}
	
	/**
	 * Metodo que determina si una celda es editable o no.
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex == 0 || columnIndex == 7)
			return false;
		else
			return true;
	}
	
	/**
	 * Metodo que actualiza la informacion de una celda.
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		//Vamos a obtener el idProducto
		int idProducto;
		
		Integer id = Integer.valueOf((String) getValueAt(rowIndex, 0));
		idProducto = id.intValue();		// Aqui tenemos el idProducto
		
		try {
			Vector data = ConexionServidor.getServidor().getProducto(ConexionServidor.getIdentificadorUnico(), idProducto);
			
			if(!data.elementAt(columnIndex).equals(aValue)) {
				data.setElementAt(aValue, columnIndex);
				ConexionServidor.getServidor().updateProducto(ConexionServidor.getIdentificadorUnico(), 
																Integer.valueOf((String)data.get(0)).intValue(), 
																String.valueOf(data.get(1)), 
																Integer.valueOf((String)data.get(2)).intValue(), 
																Integer.valueOf((String)data.get(3)).intValue(), 
																Integer.valueOf((String)data.get(4)).intValue(), 
																String.valueOf(data.get(5)), 
																String.valueOf(data.get(6)));
				// Actualizamos datos...
				setDataVector((Vector) ConexionServidor.getServidor().getAllItems(ConexionServidor.getIdentificadorUnico(), Inventario.getOrdenamientoDatos()));
				ManejadorComandos.getInstancia().setMomento();
			}
		} catch (NumberFormatException e) {
			Inventario.setStatusBarText("Dato incorrecto!!!");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
