/**
 * Archivo MyGUI.java
 * clase MyGUI
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
 *  En este archivo se encuentra la interfaz del programa.
 * 
 * Es la GUI del cliente, permite visualizar las repuestas del servidor. 
 * Y controlar la logica cliente.
 */
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.*;
//import java.io.*;

//PENDIENTES
 //cambiar textpane por textarea

 public class MyGUI extends JFrame{
   //configuración servidor
     LogicaCliente logica =new LogicaCliente();
    

    //para la interfaz:
    Container contenedor;
    JTabbedPane pestanas;
    JPanel PestanaListas , panelDeListas; 
    JScrollPane scrollListaEstudiantes, srollListaAsig;
    
    //JTextPane terminos;  
    //JScrollPane srollTerminos;
    JButton cancelar, matricular;
	  JLabel instrucciones ;

    //Añadido por el proyecto
    
    JLabel EtqAsig, EtqEst;
    JTextArea listAsig, listEst, Tabulado;
    

    //pestaña matricula  
    JPanel panelMatricula, panelMatCan, panelTabulado, panelTituloList;
    JLabel EtqCodEst, EtqCodAsi;
    JTextField textEst, textAsi;
   

	  ManejaEvento manejador;
    
    public MyGUI()
   {
    //botones del área de matricula
    manejador = new ManejaEvento();
    cancelar = new JButton("Cancelar");
    matricular = new JButton("Matricular");
    cancelar.addActionListener(manejador);
    matricular.addActionListener(manejador);
    
    

    // etiquetas
    EtqAsig = new JLabel("Lista de asignaturas \n en el sistema");
    EtqEst = new JLabel("Lista de estudiantes \n en el sistema");
    instrucciones = new JLabel("Revise las materias disponibles \n y proceda a matricular o cancelar su materia. \n \n \n",JLabel.CENTER);

    EtqCodEst = new JLabel("Cod Estudiante");
    EtqCodAsi = new JLabel("Cod Asignatura");

    //Text field

    textEst = new JTextField();
    textAsi = new JTextField();

    //TextArea
    listAsig = new JTextArea(5,20);
    listAsig.setText("Asignaturas...");
    //listAsig.setEditable(false);
    listEst = new JTextArea();
    listEst.setText("Estudiantes...");

    Tabulado = new JTextArea(5, 20);
    
    //paneles 
    panelMatCan = new JPanel();
    panelMatCan.setLayout(new GridLayout(3,2,2,2));
    panelMatCan.add(EtqCodAsi);
    panelMatCan.add(textAsi);
    panelMatCan.add(EtqCodEst);
    panelMatCan.add(textEst);
    panelMatCan.add(cancelar);
    panelMatCan.add(matricular);

    //funcionalidad con el servidor
    textEst.setEditable(false);
    textAsi.setEditable(false);
    matricular.setEnabled(false);
    cancelar.setEnabled(false);

    //panelTabulado = new JPanel();
    //panelTabulado.add(Tabulado);
   
   JPanel textAreas = new JPanel();
   textAreas.setLayout(new GridLayout(1,2,4,4));
   panelTituloList = new JPanel();
   panelTituloList.setLayout(new GridLayout(1,2,4,4));
   panelTituloList.add(EtqEst);
   panelTituloList.add(EtqAsig);

   panelDeListas = new JPanel();
   panelDeListas.setLayout(new BorderLayout());
   panelDeListas.add(panelTituloList, BorderLayout.NORTH);
   
  scrollListaEstudiantes = new  JScrollPane(listEst);
   srollListaAsig = new JScrollPane( listAsig);

   textAreas.add(scrollListaEstudiantes);
   textAreas.add(srollListaAsig);

   panelDeListas.add(textAreas, BorderLayout.CENTER);
  

    //pestañas
    PestanaListas = new JPanel();
    PestanaListas.setLayout(new BorderLayout());
    PestanaListas.add(instrucciones, BorderLayout.NORTH );
    
    PestanaListas.add(panelDeListas, BorderLayout.CENTER);
    
    

    panelMatricula = new JPanel();
    panelMatricula.setLayout(new BorderLayout());
    panelMatricula.add(panelMatCan, BorderLayout.NORTH);
    panelMatricula.add(Tabulado, BorderLayout.CENTER);
    
    //PestanaMatricula.add(srollTerminos, BorderLayout.CENTER );
    
    
    
    pestanas = new JTabbedPane();
    pestanas.add(PestanaListas, "Listas");
    pestanas.add(panelMatricula, "Matricula");
    
    contenedor = getContentPane();
    contenedor.add(pestanas);
  
    setTitle("Sistema de gestión de cursos");
    setSize(680, 400);
    setVisible(true);
    //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    //Cuando se cierre la pestaña se desconecte del servidor
    addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        try {
          //cuanto le envio 'T' se termina la conexión
          logica.enviarDatos("T","");
          
        } catch (Exception ex) {
          System.out.println("No se encontró servidor");
          JOptionPane.showMessageDialog(null,"No se encontró servidor");
          
        }


      }

    });
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    


  }
 
  public static void main(String[] args){

    // parte del cliente  
   MyGUI GUI = new MyGUI();
   String ip   = (JOptionPane.showInputDialog("Ingrese la ip donde se ejecuta el servidor\n"));
   // "192.168.101.6" esta ip es de uno de los que desarrollaron la app
   String sal= GUI.logica.conectar( ip); 
   if(sal.length()<=0){
   JOptionPane.showMessageDialog(null, "El servidor no se encuentra en ejecución \nCierre el programa y ejecutelo despues del servidor ");


   }
   

        if(sal.length()>0 && sal.substring(0, 1).equals("1"))
        {
          JOptionPane.showMessageDialog(null, "Conexion exitosa");
          GUI.textEst.setEditable(true);
          GUI.matricular.setEnabled(true);
          
          GUI.textAsi.setEditable(true);
          
          GUI.cancelar.setEnabled(true);
                //invocar al método que se queda escuchando al servidor
          GUI.logica.escucharAlServidor(GUI.listEst,GUI.listAsig, GUI.Tabulado);
         
        }
        
        
  }

  class ManejaEvento implements ActionListener
  {

    

    public void actionPerformed(ActionEvent ae)
    {
      

      if(ae.getSource() == matricular)
      {
        JOptionPane.showMessageDialog(null, "Usted Intenta Matricular\n "+textAsi.getText().trim());
        //si le envio "M" es para matricular
        logica.enviarDatos("M "+textEst.getText().trim(),textAsi.getText().trim());
         //si le envio "tab" es para tabular
        logica.enviarDatos("tab","");
         

      }
      
      else if(ae.getSource() == cancelar)
      {
        
        JOptionPane.showMessageDialog(null, "Usted Intenta Cancelar\n "+textAsi.getText().trim());
        //si le envio "Ca" es para cancelar
        logica.enviarDatos("Ca "+textEst.getText().trim(),textAsi.getText().trim());
        logica.enviarDatos("tab","");
        

      }
      
    }
  }
	



 }

