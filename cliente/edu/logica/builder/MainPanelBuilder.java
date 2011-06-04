package cliente.edu.logica.builder;

import java.awt.Point;
import java.awt.print.PrinterException;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneLayout;

import cliente.edu.presentacion.Inventario;
import cliente.edu.repositorio.ModeloTablaInventario;

/**
 * Esta clase es la representacion del area donde se muestra
 * la informacion en la pantalla y se cargan los datos provenientes
 * de la base de datos.<p>
 * 
 * Estos datos son mostrados en una tabla la cual se base en un
 * modelo que proviene de la clase <i>ModeloTablaInventario</i>.
 * Ademas provee metodos para imprimir la tabla y demas operaciones
 * referentes al manejo de la misma.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class MainPanelBuilder extends JScrollPane implements AbstractBuilder {

	/**
	 * Representa la tabla de datos
	 */
	private static JTable tabla;
	
	/**
	 * Representa el modelo de la tabla de datos
	 */
	private static ModeloTablaInventario modelo;
	
	/**
	 * Constructor de la clase, genera la tabla de datos.
	 * Parte del codigo es copiado del API de java, especificamente
	 * del constructor de JScrollPane.
	 */
	public MainPanelBuilder() {
		modelo = new ModeloTablaInventario();	
		tabla = new JTable(modelo);
		
		setLayout(new ScrollPaneLayout.UIResource());
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setViewport(createViewport());
        setVerticalScrollBar(createVerticalScrollBar());
        setHorizontalScrollBar(createHorizontalScrollBar());
	   	setViewportView(tabla);
		setOpaque(true);
        updateUI();

        if (!this.getComponentOrientation().isLeftToRight()) {
        	viewport.setViewPosition(new Point(Integer.MAX_VALUE, 0));
        }
	}
	
	/**
	 * Metodo que imprime la tabla de datos, es estatico para que sea
	 * llamado desde cualquier parte de la aplicacion.
	 */
	public static void imprimirTabla() {
		// En este punto estoy orgulloso de mi mismo IMPRIME!!!
		try {
			if(tabla.print(JTable.PrintMode.NORMAL))
				Inventario.setStatusBarText("Impresion exitosa!!!");
			else
				Inventario.setStatusBarText("Impresion cancelada!!!");	
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que retorna el ID del producto seleccionado en la
	 * tabla.
	 * @return			ID del producto seleccionado, 0 si no hay 
	 * 					nada seleccionado.
	 */
	public static int getIDproductoSeleccionado() {
		if(tabla.getSelectedRow() != -1)
			return Integer.valueOf((String) modelo.getValueAt(tabla.getSelectedRow(), 0)).intValue();
		else
			return 0;
	}
	
	/**
	 * Metodo que actualiza los datos de la tabla, es estatico
	 * para que la actualizacion sea fluida para la aplicacion,
	 * de tal manera que se actualizen los datos frecuentemente.
	 * @param datos		Datos a actualizar en la tabla
	 */
	public static void setDatos(Vector datos) {
		modelo.setDataVector(datos);
	}
	
	/**
	 * Metodo que selecciona las columnas y las filas comprendidas
	 * por los parametros.
	 * @param rowToSelect			Filas a seleccionar
	 * @param columnToSelect		Columnas a seleccionar
	 */
	public static void setSeleccion(int rowToSelect, int columnToSelect) {
		tabla.setRowSelectionInterval(rowToSelect, rowToSelect);
		tabla.setColumnSelectionInterval(columnToSelect, columnToSelect);
	}
	
}
