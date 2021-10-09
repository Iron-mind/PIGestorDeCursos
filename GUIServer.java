
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.*;

/**
 * Archivo: GUIServer.java 
 * clase GUIServer 
 * 
 * Autores: 
 * Juan David T. Montoya
 * davidtovarmontoya@gmail.com
 * 
 * Jose Andres S. Florez - 
 * jose.andres.sanchez@correounivalle.edu.co
 * 
 * Wilian Fernando Quintero Tello - 
 * wilian.quintero@correounivalle.edu.co
 * 
 * Es la GUI del servidor, Muestra los mensjes en un textArea que envia el el cliente.
 */
public class GUIServer extends JFrame{

    JTextArea area;
    JScrollPane barras;
    Container contenedor;   
    
    
    public GUIServer() {
        super("Servidor - Ejemplo");
        area = new JTextArea(15, 30);
        barras = new JScrollPane(area);

        contenedor = getContentPane();
        contenedor.setLayout(new BorderLayout());
        contenedor.add(barras, BorderLayout.CENTER);

        pack();
        setVisible(true);

    }
    
    
    /**
     * @param args the command line arguments
     * Acá Se ejecuta la lógica del servidor y la GUI
    */
    public static void main(String[] args) {
        
        GUIServer aplicacion = new GUIServer();
        LogicaServidor logica = new LogicaServidor();
        
        logica.ejecutarServidor(aplicacion.area);
                
        aplicacion.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
   
     
    
}
