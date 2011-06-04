package cliente.edu.logica.builder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import cliente.edu.logica.command.ManejadorComandos;
import cliente.edu.presentacion.ConexionServidor;
import cliente.edu.presentacion.Creditos;
import cliente.edu.presentacion.Inventario;

/**
 * Esta clase se encarga de construir una barra de menu
 * que ha de ser ubicada en la parte superior de la aplicacion.<p>
 * 
 * Es parte del patron <i>Builder</i> el cual es utilizado
 * por la clase <i>Inventario</i> para crear secuencialmente el menu. Esta
 * clase tambien hace uso del patron <i>Command</i> para ejecutar
 * los comandos de sus items respectivos.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class MenuBarBuilder extends JMenuBar implements AbstractBuilder {

	/**
	 *  Representa el manejador de comandos de la aplicacion.
	 * @uml.property name="manejadorComandos"
	 * @uml.associationEnd inverse="menuBarBuilder:cliente.edu.logica.command.ManejadorComandos" multiplicity="(0 1)"
	 */
	private ManejadorComandos comandos;
	
	/**
	 * Representa el menu Archivo.
	 */
	private JMenu archivoMenu = new JMenu("Archivo");

	/**
	 * Representa el item conectar que pertenece al menu Archivo.
	 */
	private JMenuItem conectarMenuItem = new JMenuItem("Conectar");
	
	/**
	 * Representa el item desconectar que pertenece al menu Archivo.
	 */
	private JMenuItem desconectarMenuItem = new JMenuItem("Desconectar");
	
	/**
	 * Representa el item imprimir que pertenece al menu Archivo.
	 */
	private JMenuItem imprimirMenuItem = new JMenuItem("Imprimir");
	
	/**
	 * Representa el item salir que pertenece al menu Archivo.
	 */
	private JMenuItem salirMenuItem = new JMenuItem("Salir");
	
	
	/**
	 * Representa el menu Edicion.
	 */
	private JMenu edicionMenu = new JMenu("Edicion");
	
	/**
	 * Representa el item Deshacer que pertenece al menu Edicion.
	 */
	private JMenuItem deshacerMenuItem = new JMenuItem("Deshacer");
	
	/**
	 * Representa el item Agregar que pertenece al menu Edicion.
	 */
	private JMenuItem agregarItemMenuItem = new JMenuItem("Agregar Item");
	
	/**
	 * Representa el item Eliminar que pertenece al menu Edicion.
	 */
	private JMenuItem eliminarItemMenuItem = new JMenuItem("Eliminar Item");
	
	/**
	 * Representa el item Buscar que pertenece al menu Edicion.
	 */
	private JMenuItem buscarItemMenuItem = new JMenuItem("Buscar Item");
	
	/**
	 * Representa el item Extraer Unidades que pertenece al menu Edicion.
	 */
	private JMenuItem extraerUnidadesMenuItem = new JMenuItem("Extraer Unidades");

	/**
	 * Representa el item Ingresar Unidades que pertenece al menu Edicion.
	 */
	private JMenuItem ingresarUnidadesMenuItem = new JMenuItem("Ingresar Unidades");

	
	/**
	 * Representa el submenu Ordenar Tabla que pertenece al menu Edicion.
	 */
	private JMenu ordenarTablaMenu = new JMenu("Ordenar Tabla");
	
	/**
	 * Representa el item Producto que pertenece al submenu Ordenar Tabla
	 */
	private JMenuItem ordenarPorProductoMenuItem = new JMenuItem("Producto");
	
	/**
	 * Representa el item Descripcion que pertenece al submenu Ordenar Tabla
	 */
	private JMenuItem ordenarPorDescripcionMenuItem = new JMenuItem("Descripcion");
	
	/**
	 * Representa el item Cantidad Disponible que pertenece al submenu Ordenar Tabla
	 */
	private JMenuItem ordenarPorCantDisponibleMenuItem = new JMenuItem("Cant. Disponible");
	
	/**
	 * Representa el item Cantidad Minima que pertenece al submenu Ordenar Tabla
	 */
	private JMenuItem ordenarPorCantMinimaMenuItem = new JMenuItem("Cant. Minima");
	
	/**
	 * Representa el item Precio Venta que pertenece al submenu Ordenar Tabla
	 */
	private JMenuItem ordenarPorPrecioVentaMenuItem = new JMenuItem("Precio Venta");
	
	/**
	 * Representa el item Proveedor que pertenece al submenu Ordenar Tabla
	 */
	private JMenuItem ordenarPorProveedorMenuItem = new JMenuItem("Proveedor");
	
	/**
	 * Representa el item Observacion que pertenece al submenu Ordenar Tabla
	 */
	private JMenuItem ordenarPorObservacionMenuItem = new JMenuItem("Observacion");
	
	/**
	 * Representa el item Modifico que pertenece al submenu Ordenar Tabla
	 */
	private JMenuItem ordenarPorModificoMenuItem = new JMenuItem("Modifico");

	
	/**
	 * Representa el menu Ayuda.
	 */
	private JMenu ayudaMenu = new JMenu("Ayuda");
	
	/**
	 * Representa el item Acerca De que pertenece al menu Ayuda
	 */
	private JMenuItem acercaDeMenuItem = new JMenuItem("Acerca de");

	
	/**
	 * Constructor de la clase. Llama a los metodo internos
	 * para la creacion del menu en su totalidad.
	 */
	public MenuBarBuilder() {		
		menuArchivoBuilder();
		add(archivoMenu);
		
		menuEdicionBuilder();
		add(edicionMenu);
		
		menuAyudaBuilder();
		add(ayudaMenu);
	}
	
	/**
	 * Metodo que se encarga de construir el menu <b>Archivo</b>
	 * y agregar funcionalidad a sus items.
	 */
	private void menuArchivoBuilder() {
		conectarMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConexionServidor conexion = ConexionServidor.getInstancia();
				conexion.conectar();
			}
		});
		conectarMenuItem.setMnemonic('c');
		conectarMenuItem.setToolTipText("Se conecta al servidor de datos");
		archivoMenu.add(conectarMenuItem);
		
		desconectarMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConexionServidor conexion = ConexionServidor.getInstancia();
				conexion.desconectar();
			}
		});
		desconectarMenuItem.setMnemonic('d');
		desconectarMenuItem.setToolTipText("Se desconecta del servidor de datos");
		archivoMenu.add(desconectarMenuItem);
		
		archivoMenu.addSeparator();
		
		imprimirMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanelBuilder.imprimirTabla();
			}
		});
		imprimirMenuItem.setMnemonic('i');
		imprimirMenuItem.setToolTipText("Imprime la tabla de datos");
		archivoMenu.add(imprimirMenuItem);
		
		archivoMenu.addSeparator();
		
		salirMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConexionServidor.getInstancia().desconectar();
				System.exit(0);
			}
		});
		salirMenuItem.setMnemonic('s');
		salirMenuItem.setToolTipText("Cierra la aplicacion");
		archivoMenu.add(salirMenuItem);
		
		archivoMenu.setMnemonic('a');
	}
	
	/**
	 * Metodo que se encarga de crear el menu <b>Edicion</b>
	 * y agregarle funcionalidad a los items.
	 */
	private void menuEdicionBuilder() {
		deshacerMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.deshacer();
			}
		});
		deshacerMenuItem.setMnemonic('d');
		deshacerMenuItem.setToolTipText("Deshace la ultima accion");
		edicionMenu.add(deshacerMenuItem);
		
		edicionMenu.addSeparator();
		
		agregarItemMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.agregarItem();
			}
		});
		agregarItemMenuItem.setMnemonic('a');
		agregarItemMenuItem.setToolTipText("Agrega un elemento a la lista");
		edicionMenu.add(agregarItemMenuItem);
		
		eliminarItemMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.eliminarItem();
			}
		});
		eliminarItemMenuItem.setMnemonic('e');
		eliminarItemMenuItem.setToolTipText("Elimina un elemento de la lista");
		edicionMenu.add(eliminarItemMenuItem);
		
		buscarItemMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.buscarItem();
			}
		});
		buscarItemMenuItem.setMnemonic('b');
		buscarItemMenuItem.setToolTipText("Busca un elemento en la lista");
		edicionMenu.add(buscarItemMenuItem);
		
		edicionMenu.addSeparator();
		
		extraerUnidadesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.extraerUnidades();
			}
		});
		extraerUnidadesMenuItem.setMnemonic('x');
		extraerUnidadesMenuItem.setToolTipText("Extrae unidades del producto seleccionado");
		edicionMenu.add(extraerUnidadesMenuItem);

		ingresarUnidadesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.ingresarUnidades();
			}
		});
		ingresarUnidadesMenuItem.setMnemonic('i');
		ingresarUnidadesMenuItem.setToolTipText("Ingresa unidades del producto seleccionado");
		edicionMenu.add(ingresarUnidadesMenuItem);
		
		edicionMenu.addSeparator();
		
		ordenarPorProductoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setOrdenamientoDatos("id");
			}
		});
		ordenarPorProductoMenuItem.setMnemonic('p');
		ordenarPorProductoMenuItem.setToolTipText("Ordena los datos de la tabla por Producto");
		ordenarTablaMenu.add(ordenarPorProductoMenuItem);

		ordenarPorDescripcionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setOrdenamientoDatos("Descripcion");
			}
		});
		ordenarPorDescripcionMenuItem.setMnemonic('d');
		ordenarPorDescripcionMenuItem.setToolTipText("Ordena los datos de la tabla por Descripcion");
		ordenarTablaMenu.add(ordenarPorDescripcionMenuItem);

		ordenarPorCantDisponibleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setOrdenamientoDatos("CantidadDisponible");
			}
		});
		ordenarPorCantDisponibleMenuItem.setMnemonic('d');
		ordenarPorCantDisponibleMenuItem.setToolTipText("Ordena los datos de la tabla por Cant. Disponible");
		ordenarTablaMenu.add(ordenarPorCantDisponibleMenuItem);

		ordenarPorCantMinimaMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setOrdenamientoDatos("CantidadMinima");
			}
		});
		ordenarPorCantMinimaMenuItem.setMnemonic('m');
		ordenarPorCantMinimaMenuItem.setToolTipText("Ordena los datos de la tabla por Cant. Minima");
		ordenarTablaMenu.add(ordenarPorCantMinimaMenuItem);

		ordenarPorPrecioVentaMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setOrdenamientoDatos("PrecioVenta");
			}
		});
		ordenarPorPrecioVentaMenuItem.setMnemonic('v');
		ordenarPorPrecioVentaMenuItem.setToolTipText("Ordena los datos de la tabla por Precio Venta");
		ordenarTablaMenu.add(ordenarPorPrecioVentaMenuItem);

		ordenarPorProveedorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setOrdenamientoDatos("Proveedor");
			}
		});
		ordenarPorProveedorMenuItem.setMnemonic('e');
		ordenarPorProveedorMenuItem.setToolTipText("Ordena los datos de la tabla por Proveedor");
		ordenarTablaMenu.add(ordenarPorProveedorMenuItem);

		ordenarPorObservacionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setOrdenamientoDatos("Observaciones");
			}
		});
		ordenarPorObservacionMenuItem.setMnemonic('o');
		ordenarPorObservacionMenuItem.setToolTipText("Ordena los datos de la tabla por Observacion");
		ordenarTablaMenu.add(ordenarPorObservacionMenuItem);

		ordenarPorModificoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setOrdenamientoDatos("UsuarioModifico");
			}
		});
		ordenarPorModificoMenuItem.setMnemonic('m');
		ordenarPorModificoMenuItem.setToolTipText("Ordena los datos de la tabla por Modifico");
		ordenarTablaMenu.add(ordenarPorModificoMenuItem);

		
		ordenarTablaMenu.setMnemonic('o');
		edicionMenu.add(ordenarTablaMenu);
		
		edicionMenu.setMnemonic('e');
	}
	
	/**
	 * Este metodo se encarga de crear el menu <b>Ayuda</b>
	 * y de darle funcionalidad a sus items.
	 */
	private void menuAyudaBuilder() {
		acercaDeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Creditos();
			}
		});
		acercaDeMenuItem.setMnemonic('a');
		acercaDeMenuItem.setToolTipText("Muestra los detalles de esta aplicacion");
		ayudaMenu.add(acercaDeMenuItem);
		
		ayudaMenu.setMnemonic('y');
	}

}
