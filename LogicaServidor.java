
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 * Archivo: LogicaServidor.java 
 * clase LogicaServidor 
 * 
 * Autores: 
 * Juan David Tovar Montoya
 * davidtovarmontoya@gmail.com
 * 
 * Jose Andres S. Florez - 
 * jose.andres.sanchez@correounivalle.edu.co
 * 
 * Wilian Fernando Quintero Tello - 
 * wilian.quintero@correounivalle.edu.co
 * 
 * 
 * Es toda la lógica del servidor. Es la lógica del servidor, recibe y envia los mensajes al cliente. 
 * Esta clase es instanciada en la GUIServer.
 * 
 */

public class LogicaServidor {

    String espacio="  . ............................... .  ";
    String listAADatosDePrueba= "7400026"+espacio+	"Alg.Lineal\n"
    +"7400038"+espacio+	"Calculo\n"
    +"7500065"+espacio+	"Prog.Funcional\n"
    +"7500083"+espacio+	"IPOO\n";
    String listAEDatosDePrueba= "181546"+espacio+"Juan Diegon\n"+
    "181586"+espacio+	"Ana Dayanan\n"+
    "182358"+espacio+	"Rocio Floresn\n"+
    "184951"+espacio+	"Juan Josen\n" ;
    //datos de prueba


    private ServerSocket servidor;
    private Socket conexion;
    
    private ObjectOutputStream salida; 
    private ObjectInputStream entrada; 
    
    private int contador = 1; // contador del numero de conexiones
    
    /**
     * Método que crea el serverSocket, se queda a espera de que un cliente se conecte por el puerto
     * e invoca al método que procesa las solicitudes del cliente
     * @param area el área de texto donde se van a mostrar los mensajes
     */
     public void ejecutarServidor(JTextArea area)
    {
        try 
        {
            servidor = new ServerSocket( 12345, 1 ); 
            while ( true ) {
                try
                {
                    area.append( "\nEsperando una conexion\n" );                    
                    conexion = servidor.accept(); // permite al servidor aceptar la conexion                    
                    area.append( "Conexion " + contador + " recibida de: " + conexion.getInetAddress().getHostName() );
                                                        
                    salida = new ObjectOutputStream(conexion.getOutputStream());
                    salida.flush();
                    
                    entrada = new ObjectInputStream(conexion.getInputStream());
                    area.append("\nSe obtuvieron los flujos de E/S" );
                    
                    enviarMensaje("Conexion exitosa");
                    procesarConexion(area); 
                    
                } catch ( EOFException excepcionEOF ){
                    System.out.println( "\n Servidor termino la conexion" );
                } // fin de catch
                finally {
                    try
                    {   salida.close(); 
                        entrada.close(); 
                        conexion.close(); 
                    } catch ( IOException exepcionES ){
                        System.out.println(exepcionES.getMessage());
                    } // fin de catch
                    
                    contador++;
                } // fin de finally
             } // fin de while
       } catch ( IOException exepcionES ){
            System.out.println(exepcionES.getMessage());
       } // fin de catch
     } // fin del método ejecutarServidor
 
      
    /**
    * procesa los mensajes del cliente, y responde segun lo solicitado por �l.
    * @throws IOException 
    */
    private void procesarConexion(JTextArea area) throws IOException
    {
        String mensaje = "";
        do 
        {
            try 
            {
                mensaje = ( String ) entrada.readObject(); 
                area.append("\n" + mensaje );
                
                if(mensaje.contains("M"))
                    enviarMensaje("MATRICULANDO...");                    
                else if(mensaje.contains("Ca")) //
                    enviarMensaje("CANCELANDO...");
                else if(mensaje.toLowerCase().contains("tab")) //tab para tabular
                    enviarMensaje("TABULANDO...");
                else if(mensaje.contains("lisE")) //lis para listar
                    enviarMensaje("Listando Estudiantes...\n \n"+listAEDatosDePrueba);
                else if(mensaje.contains("lisA")) //lis para listar
                    enviarMensaje("Listando Asignaturas...\n \n"+listAADatosDePrueba );
                
            }catch ( ClassNotFoundException enc ) {
                System.out.println( "\nSe recibio un tipo de objeto desconocido" );
            } // fin de catch
            System.out.println("mensaje: |"+mensaje+"|");
        } while ( !mensaje.equals( "CLIENTE>>> T " ) );
    } // fin del método procesarConexion

    
    /**
     * método que envía al cliente el mensaje de respuesta a la solicitud
     * @param mens 
     */
      public void enviarMensaje(String mens)
      {
          try
              {
                    salida.writeObject( "" + mens );
                    salida.flush(); // envía toda la salida al cliente
                    System.out.println( "\n " + mens );
               }catch ( IOException exepcionES ){
                 System.out.println( "\nError al escribir objeto" );
            } // fin de catch
      }
}
