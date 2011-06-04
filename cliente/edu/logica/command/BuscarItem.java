package cliente.edu.logica.command;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import servidor.edu.presentacion.IServidorInventario;
import cliente.edu.logica.builder.MainPanelBuilder;
import cliente.edu.presentacion.ConexionServidor;
import cliente.edu.presentacion.Inventario;

/**
 * Esta clase representa el comando buscar el cual busca
 * la ocurrecia de una cadena de texto en los datos y selecciona
 * de la tabla la primera de ellas.<p>
 * 
 * Esta clase hace parte del patron <i>Command</i>.
 * 
 * @author Camilo
 * @version 1.0 <b>"Funcional"</b>
 */
public class BuscarItem extends JDialog implements IComando, KeyListener {

	/**
	 * Representa la etiqueta "Cadena a buscar"
	 */
	private JLabel buscarLbl = new JLabel("Cadena a buscar:", JLabel.CENTER);
	
	/**
	 * Representa el campo de entrada de datos
	 */
	private JTextField buscarFdl = new JTextField();
	
	/**
	 * Representa el boton aceptar
	 */
	private JButton aceptarBtn = new JButton("Aceptar");
	
	/**
	 * Representa el boton cancelar
	 */
	private JButton cancelarBtn = new JButton("Cancelar");
	
	/**
	 * Constructor de la clase, agrega los componentes y le
	 * agrega un ActionListener al boton aceptar el cual
	 * busca la cadena en los datos.
	 */
	public BuscarItem() {
		setLayout(new GridLayout(2,2));
		
		aceptarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setStatusBarText("Se dispone a buscar una cadena!!!");
				IServidorInventario servidor = ConexionServidor.getServidor();
				String buscar = buscarFdl.getText();
				String cadena;
				int iguales = 0;
				int contenidas = 0;
				int fila = 0;
				int columna = 0;
				
				try {
					Vector productos = servidor.getAllItems(ConexionServidor.getIdentificadorUnico(), Inventario.getOrdenamientoDatos());
					for(int i=0; i < productos.size(); i++) {
						Vector detalles = (Vector) productos.elementAt(i);
						for(int j=0; j < detalles.size(); j++) {
							cadena = (String) detalles.elementAt(j);
							cadena += "";
							
							if(cadena.equalsIgnoreCase(buscar)){
								iguales++;
								if(fila == 0 && columna == 0){
									fila = i;
									columna = j;
								}
							}
							else if(cadena.contains(buscar))
								contenidas++;
						}
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				dispose();
				MainPanelBuilder.setSeleccion(fila, columna);
				JOptionPane.showMessageDialog(null, 
						"Cadenas iguales: "+iguales+"\n" +
						"Cadenas contenidas: "+contenidas,
						"Resultados de la busqueda",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		cancelarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inventario.setStatusBarText("Accion Cancelada!!!");
				dispose();
			}
		});
		
		add(buscarLbl);
		add(buscarFdl);
		add(aceptarBtn);
		add(cancelarBtn);
		buscarFdl.addKeyListener(this);
		
		if(ConexionServidor.estaConectado()) {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setSize(250,80);
			setTitle("Buscar una cadena");
			setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-125, Toolkit.getDefaultToolkit().getScreenSize().height/2-40);
			setResizable(false);
			setModal(true);
			setAlwaysOnTop(true);
			setVisible(true);

		}
		else {
			Inventario.setStatusBarText("No esta conectado al servidor!!!");
			dispose();
		}
	}
	
	/**
	 * Listener de teclado para el dialogo de peticion de datos.
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			aceptarBtn.doClick();
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			cancelarBtn.doClick();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

}
