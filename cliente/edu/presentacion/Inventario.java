package cliente.edu.presentacion;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;

import servidor.edu.presentacion.IServidorInventario;
import cliente.edu.logica.builder.AbstractBuilder;
import cliente.edu.logica.builder.MainPanelBuilder;
import cliente.edu.logica.builder.MenuBarBuilder;
import cliente.edu.logica.builder.StatusBarBuilder;
import cliente.edu.logica.builder.ToolBarBuilder;

/**
 * Esta clase es la clase principal de la aplicacion,
 * es una JFrame que se ejecutara desde un archivo
 * JAR en la maquina cliente.<p>
 * 
 * Esta clase hace uso del patron <i>Builder</i> para la
 * creacion secuencial de los componentes de la interfaz
 * grafica.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class Inventario extends JFrame implements MouseMotionListener {

	/**
	 * Representa el constructor de componentes 
	 * @uml.property name="abstractBuilder"
	 * @uml.associationEnd inverse="inventario:cliente.edu.logica.builder.AbstractBuilder"
	 * multiplicity="(0 1)"
	 */
	private AbstractBuilder builder;
	
	/**
	 * Representa la barra de estatus, diponible para toda la aplicacion
	 */
	private static StatusBarBuilder statusBar;
	
	/**
	 * Representa el tipo de ordenamiento de los datos, por defecto <i>id</i>
	 */
	private static String ordenDatos = "id";
	
	/**
	 * Constructor de la clase. Hace uso del patron builder
	 * para la construccion de la interfaz grafica.
	 */
	public Inventario() {
		// Construimos la interfaz grafica
		builder = new MenuBarBuilder();
		setJMenuBar((JMenuBar) builder);
		
		builder = new ToolBarBuilder();
		add((Component) builder, BorderLayout.NORTH);
		
		builder = new MainPanelBuilder();
		add(new JScrollPane((Component) builder), BorderLayout.CENTER);
		
		builder = new StatusBarBuilder();
		add((Component) builder, BorderLayout.SOUTH);
		
		// Instanciamos una copia disponible para la aplicacion
		statusBar = (StatusBarBuilder) builder;
		builder = null;
		
		// Configuracion interna
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ConexionServidor.getInstancia().desconectar();
				dispose();
				System.exit(0);
			}
		});
		addMouseMotionListener(this);
		setTitle("Inventario v1.0 - Modelos de Programacion - 2004");
		setLocationByPlatform(true);
		setSize(800,600);
		setVisible(true);
		
	}
	
	/**
	 * Metodo que muestra en la barra de estatus el
	 * String que recibe como parametro.
	 * @param x				Cadena de texto a mostrar
	 */
	public static void setStatusBarText(String x) {
		statusBar.setText(x);
	}
	
	/**
	 * Metodo que determina el tipo de ordenamiento de datos
	 * @param tipoOrden		Tipo valido de ordenamiento de datos
	 */
	public static void setOrdenamientoDatos(String tipoOrden) {
		ordenDatos = tipoOrden;
		if(ConexionServidor.estaConectado()) {
			try {
				MainPanelBuilder.setDatos(ConexionServidor.getServidor().getAllItems(ConexionServidor.getIdentificadorUnico(), ordenDatos));
				setStatusBarText("Datos Ordenados por "+ordenDatos);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		else
			setStatusBarText("No esta conectado!!!");
	}
	
	/**
	 * Metodo que retorna el tipo de ordenamiento de los datos
	 * @return			Tipo de ordenamiento de los datos
	 */
	public static String getOrdenamientoDatos() {
		return ordenDatos;
	}
	
	/**
	 * Metodo principal de la clase, inicia la ejecucion de
	 * la aplicacion.
	 * @param args
	 */
	public static void main(String[] args) {
		new Inventario();
	}

	/**
	 * Metodo que es llamado cuando el mouse se mueve por encima
	 * de la ventana de la aplicacion y actualiza los datos de la
	 * tabla.
	 */
	public void mouseMoved(MouseEvent e) {
		IServidorInventario servidor = ConexionServidor.getServidor();
		// Verificamos que exista conexion de lo contrario no hacemos nada
		if(ConexionServidor.estaConectado()) {
			try {
				MainPanelBuilder.setDatos(servidor.getAllItems(ConexionServidor.getIdentificadorUnico(), ordenDatos));
				statusBar.setText("Datos Actualizados!!! - Ultima actualizacion a las: "+DateFormat.getTimeInstance().format(new Date()));
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
	}

}
