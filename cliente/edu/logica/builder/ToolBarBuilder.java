package cliente.edu.logica.builder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import cliente.edu.logica.command.ManejadorComandos;

/**
 * Esta clase es la representacion del menu de herramientas en la aplicacion.<p>
 * 
 * Esta clase hace parte del patron <b>Builder</b> y utiliza
 * el patron <b>Command</b> para la funcionalidad de sus botones.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class ToolBarBuilder extends JToolBar implements AbstractBuilder {

	/**
	 *  Representa el manejador de comandos
	 * @uml.property name="manejadorComandos"
	 * @uml.associationEnd inverse="toolBarBuilder:cliente.edu.logica.command.ManejadorComandos" multiplicity="(0 1)"
	 */
	private ManejadorComandos comandos;

	/**
	 * Representa el boton de Agregar Item.
	 */
	private JButton agregarBtn = new JButton("Agregar");
	
	/**
	 * Representa el boton de Eliminar Item.
	 */
	private JButton eliminarBtn = new JButton("Eliminar");
	
	/**
	 * Representa el boton de Buscar Item.
	 */
	private JButton buscarBtn = new JButton("Buscar");
	
	
	/**
	 * Representa el boton de Extraer Unidades.
	 */
	private JButton extraerBtn = new JButton("Extraer Unidades");

	/**
	 * Representa el boton de Ingresar Unidades.
	 */
	private JButton ingresarBtn = new JButton("Ingresar Unidades");
	
	/**
	 * Representa el boton de Deshacer
	 */
	private JButton deshacerBtn = new JButton("Deshacer");
	
	
	/**
	 * Constructor de la clase. Agrega los botones a la barra
	 * de herramientas y les agrega funcionalidad a cada uno.
	 *
	 */
	public ToolBarBuilder() {
		agregarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.agregarItem();
			}
		});
		agregarBtn.setToolTipText("Agrega un elemento");
		
		eliminarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.eliminarItem();
			}
		});
		eliminarBtn.setToolTipText("Elimina un elemento");
		
		buscarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.buscarItem();
			}
		});
		buscarBtn.setToolTipText("Busca un elemento");
		
		extraerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.extraerUnidades();
			}
		});
		extraerBtn.setToolTipText("Extrae unidades del elemento seleccionado");
		
		ingresarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.ingresarUnidades();
			}
		});
		ingresarBtn.setToolTipText("Ingresa unidades del elemento seleccionado");
		
		deshacerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandos = ManejadorComandos.getInstancia();
				comandos.deshacer();
			}
		});
		deshacerBtn.setToolTipText("Deshacer la ultima accion");
		
		add(agregarBtn);
		add(eliminarBtn);
		add(buscarBtn);
		
		addSeparator();
		
		add(extraerBtn);
		add(ingresarBtn);
		
		addSeparator();
		
		add(deshacerBtn);
		
		setFloatable(false);
		setRollover(true);
	}

}
