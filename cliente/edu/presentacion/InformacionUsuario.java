package cliente.edu.presentacion;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import servidor.edu.presentacion.IServidorInventario;

/**
 * Esta clase es un dialogo de informacion de inicio de sesion
 * que se ejecuta cuando el usuario es registrado correctamente
 * en el servidor y obtiene los datos del usuario.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional</b>
 */
public class InformacionUsuario extends JOptionPane {
	
	/**
	 * Representa el servidor remoto
	 */
	private IServidorInventario servidor;
	
	/**
	 * Representa el nombre del usuario
	 */
	private String nombre;
	
	/**
	 * Representa la ultima fecha de acceso del usuario
	 */
	private String fecha;
	
	/**
	 * Constructor de la clase, obtiene la instancia del servidor
	 * remoto y obtiene los datos nombre y fecha de ultimo acceso
	 * para luego mostrarlos en una ventana de informacion.
	 * @param identificador		Identificador Unico de Sesion
	 */
	public InformacionUsuario(String identificador) {
		servidor = ConexionServidor.getServidor();
		try {
			nombre = servidor.getUserName(identificador);
			fecha = servidor.getUserLastAccess(identificador);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		String message = "Bienvenido: "+nombre+"\nSu Ultimo Acceso fue: "+fecha;
			
		JOptionPane.showMessageDialog(null, message, "Bienvenido, Usted ha iniciado sesion", JOptionPane.WARNING_MESSAGE);
	}

}
