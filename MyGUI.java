/**
 * Archivo MyGUI.java
 En este archivo se encuentra la interfaz del programa
 * @authors
     David Tovar Montoya
    davidtovarmontoya@gmail.com
   
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
    static LogicaCliente logica;
    static String registroClienteServidor="";

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
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    


  }
  

  public static void main(String[] args){


    // parte del cliente
   logica = new LogicaCliente();   
   MyGUI GUI = new MyGUI();
   String sal = logica.conectar("192.168.101.6"); //esta ip es local (puede cambiar)
   registroClienteServidor += sal;
   GUI.Tabulado.append(sal); //en tabulado se va registrando lo que contesta el servidor
   
   // servidor
   /*
   GUIServer aplicacion = new GUIServer();
   LogicaServidor logicaS = new LogicaServidor();
   logicaS.ejecutarServidor(aplicacion.area);
   aplicacion.setDefaultCloseOperation(EXIT_ON_CLOSE);*/
        if(sal.length()>0 && sal.substring(0, 1).equals("1"))
        {
           
          GUI.textEst.setEditable(true);
          GUI.matricular.setEnabled(true);
          
          GUI.textAsi.setEditable(true);
          
          GUI.cancelar.setEnabled(true);
                //invocar al método que se queda escuchando al servidor
          logica.escucharAlServidor(GUI.Tabulado);
          
          
            
        }

  }

  class ManejaEvento implements ActionListener
  {

    

    public void actionPerformed(ActionEvent ae)
    {
      

      if(ae.getSource() == matricular)
      {
       
        JOptionPane.showMessageDialog(null, "Usted Intenta Matricular\n "+logica.enviarDatos("M"+textEst.getText().trim(),textAsi.getText().trim()));


      }
      
      else if(ae.getSource() == cancelar)
      {
        
        
        JOptionPane.showMessageDialog(null, "Usted Intenta Cancelar\n "+logica.enviarDatos("C"+textEst.getText().trim(),textAsi.getText().trim()));
      }

      //JOptionPane.showMessageDialog(null,"registro Cliente servidor \n"+ registroClienteServidor);
    }
  }
	



 }

