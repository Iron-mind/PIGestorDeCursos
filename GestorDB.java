
import java.sql.*;
/**
 * Archivo: GestorDB.java 
 * clase GestorDB 
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
 * */
 //GestorBD:
//Gestor que se encarga de hacer la conexion con la base de datos, ingresar nuevas entradas en
//la tabla relaciones y sacar los datos necesarios para el manejo de un sistema de matricula
 

public class GestorDB {

    String url, usuario, password;
    Connection conexion;
    Statement instruccion;
    ResultSet tabla;

    public GestorDB(){
    url= "jdbc:postgresql://localhost:5432/PIPF";
    usuario="postgres";
    password="Ingeniestor";

    }//fin constructor


    //conectar:
    //metodo que se encarga de usar el Driver y hacer la conexion con la base de datos (JDBC)
    public Connection conectar(){
        
        try {
            Class.forName("org.postgresql.Driver");
        
        }
        
        catch( Exception e ) {
        System.out.println( "No se pudo cargar el driver." ); }

        try{
        conexion = DriverManager.getConnection(url, usuario, password);
        
        return conexion;
        }

        catch( Exception e ) {
        System.out.println( "No se pudo abrir la conexion." );
        return null; }
        
    }//fin conectar

    //ingresarRegistro:
    //metodo que ingresa una fila en la tabla relacion
    public void ingresarRegistro(String est, String asi) {

        String sql = "INSERT INTO Relacion VALUES(?,?)";

        try {
            PreparedStatement sentencia = conectar().prepareStatement(sql);
            sentencia.setString(1, est);
            sentencia.setString(2, asi);
            // insert into Relacion values(Cod.Est, Cod.Asi);
            int filasInsertadas = sentencia.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("se ingresÃ³ registro con exito");
            }

        } catch (SQLException ex) {
            System.out.println("no se pudo ingresar" + ex.getMessage());
        }

    }//fin ingresarRegistro


    //confirmaIngreso:
    //metodo que se encarga de verificar si ya existe una fila en la tabla relacion con 
    //datos ingresados, si no existe,entonces la ingresa
    public boolean confirmaIngreso(String est, String asi) {

        String sql = "SELECT * FROM Relacion WHERE cod_estudiante =? AND cod_asignatura =?";
        boolean respuesta = true;

        try {
            PreparedStatement sentencia = conectar().prepareStatement(sql);
            sentencia.setString(1, est);
            sentencia.setString(2, asi);
            ResultSet filas = sentencia.executeQuery();
            
            if (filas.next()) {
                System.out.println("ya existe la entrada");
                respuesta = false;
            }else {
                ingresarRegistro(est, asi);
                System.out.println("no existe la entrada");}

        } catch (SQLException ex) {
            System.out.println("no se pudo ingresar" + ex.getMessage());
        }

        return respuesta;

    }//fin confirmaIngreso

    //borrarRegistro
    //metodo que se encargar de borrar un registro en la tabla Relacion de la base de datos
    public boolean borrarRegistro(String est, String asi) {

        String sql = "DELETE FROM Relacion WHERE cod_estudiante =? AND cod_asignatura =?";
        boolean respuesta = true;

        try {
            PreparedStatement sentencia  = conectar().prepareStatement(sql);
            sentencia.setString(1, est);
            sentencia.setString(2, asi);

            int rowsDeleted = sentencia.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(" Borrado exitoso !");
                respuesta = false;
            }

        } catch (SQLException ex) {
             System.out.println("no se pudo borrar registro");
        }

        return respuesta;

    }//fin borrar


    //consultarEstudiante:
     //metodo que devuelve los codigo y nombres de los estudiantes en la base de datos
    public String consultarEstudiantes(){
     String tablaString = "CODIGO                   NOMBRE\n \n";
     String sql = "SELECT codigo, nombre FROM estudiante";

    try{

        Connection conn = conectar();
        Statement sentencia = conn.createStatement();
        ResultSet tabla = sentencia.executeQuery(sql);
        
        while(tabla.next())
        {
        
        tablaString +=tabla.getString(1) + " . ............... " + tabla.getString(2)+"\n" ;
        }

        conn.close();
        System.out.println("Conexion cerrada");

      }

     catch(SQLException e){ System.out.println(e); }
     catch(Exception e){ System.out.println(e); }
     return tablaString;
    }//fin consultarEstudiante


    //consultarAsignatura
    //metodo que devuelve los codigo y nombres de las asignaturas en la base de datos
    public String consultarAsignatura(){
     String tablaString = "CODIGO                   MATERIA\n  \n";
     String sql = "SELECT codigo, nombre FROM asignatura";
       
    try{
       
       Connection conn = conectar();
       Statement sentencia = conn.createStatement();
       ResultSet tabla = sentencia.executeQuery(sql);
               
       while(tabla.next())
        {
       
        tablaString +=tabla.getString(1) + " . ............... " + tabla.getString(2)+"\n" ;
        
        }
       
       conn.close();
       System.out.println("Conexion cerrada");
       
    }
       
    catch(SQLException e){ System.out.println(e); }
    catch(Exception e){ System.out.println(e); }
    return tablaString;
    }//fin consultarAsignatura


    //consultarCredAsi:
    //metodo que devuelve el valor de creditos de una asignatura
    public int consultarCredAsi(String c){

      String sql = "SELECT creditos FROM asignatura WHERE codigo =?";
          
    try{     
        Connection conn = conectar();
        PreparedStatement sentencia  = conn.prepareStatement(sql);
        sentencia.setString(1, c);        
        ResultSet cdt = sentencia.executeQuery();        
         while(cdt.next())
          {
           //System.out.println("Credito: " + cdt.getString(1));
           return cdt.getInt(1);
          }
      }
          
     catch(SQLException e){ System.out.println(e); }
     catch(Exception e){ System.out.println(e); }
     
     return 0;
    }//fin consultarCredAsi


    //totalCredAsi:
    //metodo que deuelve los creditos que tiene matriculado un estudiante
    public int totalCredAsi(String es){

        String sql = "SELECT cod_asignatura FROM Relacion WHERE cod_estudiante =?";
        int total = 0;
            
      try{     
          Connection conn = conectar();
          PreparedStatement sentencia  = conn.prepareStatement(sql);
          sentencia.setString(1, es);        
          ResultSet cdt = sentencia.executeQuery();        
           while(cdt.next())
            {
             total = total + consultarCredAsi(cdt.getString(1));
            }
            System.out.println("CreditosT: " + total);
            return total;
        }
            
       catch(SQLException e){ System.out.println(e); }
       catch(Exception e){ System.out.println(e); }
       
       return total;
      }//fin consultarCredAsi


    //cupopAsi
    //metodo que se encarga de devolver los cupos inciales de una asignatura 
    public int cupoAsi(String asi){

        String sql = "SELECT cupos FROM asignatura WHERE codigo =?";
        int total = 0;
            
      try{     
          Connection conn = conectar();
          PreparedStatement sentencia  = conn.prepareStatement(sql);
          sentencia.setString(1, asi);        
          ResultSet cdt = sentencia.executeQuery();        
           while(cdt.next())
            {
             total = cdt.getInt(1);
            }
            System.out.println(asi+" Cupos: " + total);
            return total;
        }
            
       catch(SQLException e){ System.out.println(e); }
       catch(Exception e){ System.out.println(e); }
       
       return total;
      }//fin cupoAsi
      

    //MatriAsi
    //metodo que devuelve la cantidad de estudiantes matriculados a una asignatura
    public int MatriAsi(String asi){

        String sql = "SELECT cod_estudiante FROM Relacion WHERE cod_asignatura =?";
        int total = 0;
            
      try{     
          Connection conn = conectar();
          PreparedStatement sentencia  = conn.prepareStatement(sql);
          sentencia.setString(1, asi);        
          ResultSet cdt = sentencia.executeQuery();        
           while(cdt.next())
            {
             total = total + 1;
            }
            System.out.println(asi+" Matriculados: " + total);
            return total;
        }
            
       catch(SQLException e){ System.out.println(e); }
       catch(Exception e){ System.out.println(e); }
       
       return total;
      }//fin MatriAsi  


    //cupDisponible:
    //metodo que devuelve la cantidad de cupos disponibles
    public int cupDisponible(String asi){
        int r = 0;
        r = cupoAsi(asi) - MatriAsi(asi);
        System.out.println(asi+" Cupos disponibles: " + r);
        return r;
    }//fin cupDisponible

  
   /**
    * @params codMateria el codigo de la materia que está en base de datos.
    * retorna el nombre de la materia que está en la tabla asignaturas
    */
   public String nombreDeMateria(String codMateria) {
    
    String sql = "SELECT  nombre FROM asignatura WHERE codigo ="+"'"+codMateria+"'";
    String nombreEncontrado ="";

    try {
        Connection conn = conectar();
        Statement sentencia = conn.createStatement();
        ResultSet tabla = sentencia.executeQuery(sql);
        if(tabla.next()){
            nombreEncontrado+= tabla.getString(1) ;
        }
        

    } 
    catch(SQLException e){ System.out.println(e); }
    catch(Exception e){ System.out.println(e); }
       return nombreEncontrado;
   }

    /**
    * @params codEstudiante el codigo de la materia que está en base de datos.
    * retorna el tabulado de un estudiante en forma de string.
    */
    public String tabuladoDe(String codEstudiante){
        String tablaString = "CODIGO                      MATERIA                         CREDITOS\n  \n";
        String sql = "SELECT  cod_asignatura FROM relacion WHERE cod_estudiante ="+"'"+codEstudiante+"'";
        
       try{
          
          Connection conn = conectar();
          Statement sentencia = conn.createStatement();
          ResultSet tabla = sentencia.executeQuery(sql);
                  
          while(tabla.next())
           {
          
           tablaString +=tabla.getString(1) + " ..................... " 
           + nombreDeMateria(tabla.getString(1))+ " .................. "+consultarCredAsi(tabla.getString(1))+"\n" ;
           
           }
          
          conn.close();
          System.out.println("Conexion cerrada");
          
       }
          
       catch(SQLException e){ System.out.println(e); }
       catch(Exception e){ System.out.println(e); }
       return tablaString;
       }

    //   public static void main(String[] args) {
    //       GestorDB db = new GestorDB();
    //       System.out.print( db.tabuladoDe("181546") ) ;
          
    //   }

















  

}