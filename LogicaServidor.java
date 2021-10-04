
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;


public class LogicaServidor {
    
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
                    area.append( "Esperando una conexion\n" );                    
                    conexion = servidor.accept(); // permite al servidor aceptar la conexion                    
                    area.append( "Conexion " + contador + " recibida de: " + conexion.getInetAddress().getHostName() );
                                                        
                    salida = new ObjectOutputStream(conexion.getOutputStream());
                    salida.flush();
                    
                    entrada = new ObjectInputStream(conexion.getInputStream());
                    area.append("\nSe obtuvieron los flujos de E/S" );
                    
                    enviarMensaje("Conexion exitosa");
                    procesarConexion(area); 
                    
                } catch ( EOFException excepcionEOF ){
                    System.out.println( "\nServidor termino la conexion" );
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
                
                if(mensaje.toLowerCase().contains("m"))
                    enviarMensaje("MATRICULANDO...");                    
                else if(mensaje.toLowerCase().contains("c"))
                    enviarMensaje("CANCELANDO...");
            }catch ( ClassNotFoundException enc ) {
                System.out.println( "\nSe recibio un tipo de objeto desconocido" );
            } // fin de catch
        } while ( !mensaje.equals( "CLIENTE>>> TERMINAR" ) );
    } // fin del método procesarConexion

    
    /**
     * método que envía al cliente el mensaje de respuesta a la solicitud
     * @param mens 
     */
      public void enviarMensaje(String mens)
      {
          try
              {
                    salida.writeObject( "SERVIDOR>>> " + mens );
                    salida.flush(); // envía toda la salida al cliente
                    System.out.println( "\nSERVIDOR>>> " + mens );
               }catch ( IOException exepcionES ){
                 System.out.println( "\nError al escribir objeto" );
            } // fin de catch
      }
}
