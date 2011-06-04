package cliente.edu.presentacion;

import javax.swing.JOptionPane;

/**
 * Esta clase representa el cuadro de dialogo del menu
 * about el cual muestra la informacion respectiva de la
 * aplicacion, esta clase hereda de <i>JDialog</i>.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class Creditos extends JOptionPane {
	
	/**
	 * Representa el mensaje a mostrar en el dialogo
	 */
	private String message = "Aplicacion desarrollada por\n" +
			"Camilo Hernando Nova\n" +
			"20022020090\n" +
			"Ingenieria de Sistemas";
	
	/**
	 * Constructor de la clase, muestra el dialogo de informacion
	 * de la aplicacion.
	 */
	public Creditos() {
		JOptionPane.showMessageDialog(null, message, "About...", JOptionPane.INFORMATION_MESSAGE);
	}
}
