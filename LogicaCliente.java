
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;


public class LogicaCliente {
    
    private ObjectOutputStream salida; 
    private ObjectInputStream entrada;
    private Socket cliente;
    
    /**
     * Método que conecta al cliente con el servidor ycrea los flujos de entrada y salida para
     * comunicarse con él
     * @param direccionServer
     * @return mensaje de conexión exitosa
     */    
    public String conectar(String direccionServer)
    {
        String mensaje = "";
        try {            
            
            cliente = new Socket(direccionServer, 12345);
            mensaje = "Conectado con servidor...."+ cliente.getInetAddress().getHostName();
            
            //obtenemos los fujos de entrada y salida
            entrada = new ObjectInputStream(cliente.getInputStream());
            salida = new ObjectOutputStream(cliente.getOutputStream());
            mensaje = "1"+mensaje;
            salida.flush();  //se vacia el flujo de salida
            
        } catch (UnknownHostException ex) {
            System.out.println("UnknownHostException: "+ ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException: "+ ex.getMessage());
        }
       return mensaje;
    }
    
    
    /**
     * método que se queda escuchando por los mensajes del servidor y lo muestro en el
     * area enviada como parámetro
     * @param area El area de texto de la GUI donde se escribe lo que envía el servidor
     */
    public void escucharAlServidor(JTextArea area) 
    {
        String mensajeRecibido = "";
        do 
        {
            try 
            {   mensajeRecibido = ( String ) entrada.readObject();
                area.append("\n" + mensajeRecibido);
                
            } catch ( ClassNotFoundException ene ){
                System.out.println( "nSe recibio un tipo de objeto desconocido"+ene.getMessage() );
            } catch (IOException ex) {
                System.out.println("Se arrojó un ioexcepcion cuando se trataba de leer del servidor "+ex.getMessage());
            }
        } while ( !mensajeRecibido.equals( "SERVIDOR>>> TERMINAR" ) );
        try {
            //se cierra la conexion
            entrada.close();
            salida.close();
            cliente.close();
            System.out.println("se cerró la conexion");
        } catch (IOException ex) {
            System.out.println("se produjo error al cerrar la conexion");
        }
                
    }  
    
    /**
     * método que envia al servidor un mensaje
     * @param mensaje el mensaje que se va a enviaar al servidor
     * @return el mensaje para ser mostrado por pantalla
     */
     public String enviarDatos( String codEstudiante, String codAsignatura ) {
         String mensajeS = "";
         
        try 
        {
            salida.writeObject( "CLIENTE>>> " + codEstudiante+" "+codAsignatura );
            salida.flush(); // envía todos los datos a la salida
            mensajeS ="\nCLIENTE>>> " + codEstudiante+" "+codAsignatura;
            
        } catch ( IOException excepcionES ) {
            mensajeS = "\nError al escribir objeto";
        } // fin de catch
        
        return mensajeS;
    } // fin del método enviarDatos

    
}
