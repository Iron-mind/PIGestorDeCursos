
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
    GestorDB db= new GestorDB();
    String listAEstudiantes= db.consultarEstudiantes();
    String listAAsignaturas= db.consultarAsignatura();
 

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
        String estudiante="";
        String asignatura="";
        do
        {
            try
            {
                mensaje = ( String ) entrada.readObject();
                area.append("\n" + mensaje );
               
                 System.out.print(mensaje);
                 if (mensaje.contains("M")) {
                     estudiante = mensaje.substring(13, 19);
                     asignatura = mensaje.substring(20, 27);

                     if (db.cupDisponible(asignatura) > 0 && db.totalCredAsi(estudiante)<12) {
                         if (db.confirmaIngreso(estudiante, asignatura)) {
                             enviarMensaje("MATRICULADO con exito");
                         } 

                     }else {
                        enviarMensaje("NO se pudo matricular, verifique los datos");

                    }

                }
                    
                else if(mensaje.contains("Ca")){
                    estudiante = mensaje.substring(14, 20);
                    asignatura = mensaje.substring(21,28);
                 

                    if(db.borrarRegistro(estudiante, asignatura)){
                        enviarMensaje("NO se pudo CANCELAR, verifique los datos");
                        
                    }else{
                        enviarMensaje("CANCELADO con exito");
                        
                    }
                   

                }
                    
                else if(mensaje.toLowerCase().contains("tab")) //tab para tabular
                    enviarMensaje("TABULADO DE "+ estudiante+ "\n \n"+ db.tabuladoDe(estudiante));


                else if(mensaje.contains("lisE")) //lis para listar
                    enviarMensaje("Listando Estudiantes\n \n"+listAEstudiantes);
                else if(mensaje.contains("lisA")) //lis para listar
                    enviarMensaje("Listando Asignaturas...\n \n"+listAAsignaturas );

            }catch ( ClassNotFoundException enc ) {
                System.out.println( "\nSe recibio un tipo de objeto desconocido" );
            } // fin de catch
            //System.out.println("mensaje: |"+mensaje+"|");
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
                   
               }catch ( IOException exepcionES ){
                 System.out.println( "\nError al escribir objeto" );
            } // fin de catch
      }
}
