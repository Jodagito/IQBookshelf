package Metodo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileReader;
/*
By Daniel Gil & Daniel Pérez
 */
public class Creararchivo {  
    public static File coneccion;
        FileWriter escritura;
        FileReader lector;
        String text;
    public String Larchivo() throws IOException{ 
        try {
            coneccion = new File("conection.txt");
            if(!coneccion.exists()){ // Aquí nos aseguramos de que no exista para crear uno
                RepararArchivo();
            }           
            lector=new FileReader(coneccion);          
        }catch (FileNotFoundException ex) {
            Logger.getLogger(Creararchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader texto = new BufferedReader(lector);
        text=texto.readLine();
        texto.close();
        return text;
    }   
    public void CArchivo(String socket){        
        coneccion = new File("conection.txt");  // Aquí se crea el archivo donde vamos a guardar los datos de la conexión
        if(!coneccion.exists()){
            RepararArchivo();
        }   
        try {
            escritura=new FileWriter("conection.txt",false);  // Creamos el escritor, false para que no concatene texto
            escritura.append(socket);  // Escribimos,
            escritura.close();  // Cerramos el escritor
        }catch (IOException ex) {
            Logger.getLogger(Creararchivo.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }   
    public void RepararArchivo(){
        coneccion = new File("conection.txt");  // Aquí se crea el archivo donde vamos a guardar los datos de la conexión
        if(!coneccion.exists()){
            try{
                coneccion.createNewFile();
            }catch(IOException ex){ex.printStackTrace();}
        }       
        try {
            escritura=new FileWriter("conection.txt",false);
            escritura.append(",,,,");
            escritura.close();
        }catch (IOException ex) {
            Logger.getLogger(Creararchivo.class.getName()).log(Level.SEVERE, null, ex);
        }             
    }    
}
