
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.*;


public class GUIServer extends JFrame{

    JTextArea area;
    JScrollPane barras;
    Container contenedor;   
    
    
    public GUIServer()
    {
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
    */
    public static void main(String[] args) {
        
        GUIServer aplicacion = new GUIServer();
        LogicaServidor logica = new LogicaServidor();
        
        logica.ejecutarServidor(aplicacion.area);
                
        aplicacion.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
   
     
    
}
