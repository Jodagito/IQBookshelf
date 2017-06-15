package Metodo;
import java.io.File;
import java.sql.*;
/*
By Daniel Gil & Daniel Pérez
 */
public class ConexionBD {
    Connection cn;
    File coneccion;
    public void coneccion(){
    coneccion = new File("conection.txt");
}    
public Connection conecxion(String dir, String puerto, String BDnombre, String user, String pass){
    try{
        Class.forName("com.mysql.jdbc.Driver");
        cn=DriverManager.getConnection("jdbc:mysql://"+dir+":"+puerto+"/"+BDnombre,user,pass);
        System.out.println("Conexión correcta");
    }catch(Exception e){
        System.out.println(e.getMessage());
        System.out.println("Error al conectar, datos inválidos");
        return null;
    }return cn;
}
public Connection conecxion(){
    try{
        Class.forName("com.mysql.jdbc.Driver");
        cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema","root","");
        System.out.println("Conexión correcta");
    }catch(Exception e){
            System.out.println(e.getMessage());
    }return cn;       
}  
public Connection CrearBD(String dir, String puerto, String user, String pass){
    try{
        Class.forName("com.mysql.jdbc.Driver");
        cn=DriverManager.getConnection("jdbc:mysql://"+dir+":"+puerto,user,pass);
        System.out.println("Conexión correcta");
    }catch(Exception e){
        System.out.println(e.getMessage());
        System.out.println("Error al conectar, datos inválidos");
        return null;
    }return cn;
}   
Statement createStatement(){
    throw new UnsupportedOperationException("No soportado");
}    
}