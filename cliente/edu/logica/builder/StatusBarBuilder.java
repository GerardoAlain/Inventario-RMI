package cliente.edu.logica.builder;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * Esta clase representa una barra de estatus de la aplicacion
 * donde se notificaran los cambios y las acciones realizadas.<p>
 * 
 * Para cambiar el texto de la barra de estado es necesario
 * llamar al metodo <i>setText(String x)</i> de esta clase.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */

public class StatusBarBuilder extends JLabel implements AbstractBuilder {

	/**
	 * Constructor de la clase. Determina el color de fondo
	 * el borde y el texto de bienvenida.
	 */
	public StatusBarBuilder() {
		setBackground(Color.GRAY);
		setBorder(BorderFactory.createRaisedBevelBorder());
		setText("Aplicacion Cargada...");
	}

}
