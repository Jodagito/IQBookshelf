// Importación de paquetes y librerías
package Interfaz;
import Metodo.Creararchivo;
import Metodo.ConexionBD;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
/*
By Daniel Gil & Daniel Pérez
 */
public class PantallaPrincipal extends javax.swing.JFrame  implements ActionListener  {  // Se llaman los formularios de conexión a BD y de configuración
    ConexionBD con;
    Connection cn;
    public String Seleccion="";
    Creararchivo carchivo= new Creararchivo();
    String dir,puerto,BDnombre,user,pass;



    public PantallaPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);
        SeleccionModulo.addActionListener(this);  // Se da un listener al jComboBox de la selección de módulos
        recuperarDatos();  // Llama la función recuperarDatos para obtener los datos de la configuración
        con = new ConexionBD();  // Inicia la conexión a la base de datos
        cn =con.conecxion(dir,puerto,BDnombre,user,pass);
        if(cn == null){  // Habilita y deshabilita pestañas del panel
            jTabbedPane1.setEnabledAt(3, true);
            jTabbedPane1.setEnabledAt(1, false);
            jTabbedPane1.setEnabledAt(2, false);
            jTabbedPane1.setEnabledAt(0, false);
        }
        else {
            jTabbedPane1.setEnabledAt(3, false);
            jTabbedPane1.setEnabledAt(1, true);
            jTabbedPane1.setEnabledAt(2, true);
            jTabbedPane1.setEnabledAt(0, true);
        }
        jTabbedPane1.setEnabled(false);
        jTabbedPane1.setEnabledAt(2, false);
        mostrartabla();  // Llama a la función mostrartabla para cargar la tabla tabladatos
        if (cn != null){
            jLabel21.setText("Conectado a la base de datos "+BDnombre+". En el puerto "+puerto+". Con el usuario "+user+". Y en el host "+dir+".");
        }  // Valida la conexión a la BD
    }
    public void actionPerformed(ActionEvent e) {  // Obtiene la selección del jComboBox y la almacena en Seleccion
        JComboBox SeleccionModulo = (JComboBox)e.getSource();
        Seleccion = (String) SeleccionModulo.getSelectedItem();
    }    

    public Image getIconImage(){  // Se establece el ícono del JFrame
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("imagenes/avatar.png"));
        return retValue;
    }
    void recuperarDatos(){  // Se declara la función recuperar datos que llama al formulario carchivo y carga el archivo de configuración
        String datos[] = new String [5];
        StringTokenizer tokens;
        int i=0;
        try {
            String socket=carchivo.Larchivo();
            try{
                tokens= new StringTokenizer(socket,",");
                while(tokens.hasMoreTokens()){
                    datos[i]=tokens.nextToken();
                    i++;
                }
            }catch(Exception ex1){
                carchivo.RepararArchivo();
                recuperarDatos();
                return;
            }
        }catch (IOException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }      
        dir=datos[0];
        puerto=datos[1];
        BDnombre=datos[2];
        user=datos[3];
        pass=datos[4];
        txtdirsql.setText(datos[0]);
        txtportsql.setText(datos[1]);
        txtnsql.setText(datos[2]);
        txtusersql.setText(datos[3]);
        txtpasssql.setText(datos[4]);
          // Se cargan los datos de la configuración   
    }
    @SuppressWarnings("unchecked")
    public void consultar(){  // Se declara la función consultar que permite automátizar la facturación
        String sqle = ""; 
        String sqld = "";
        String sqlc = "";
        if (BVenta.isSelected()==true){
            sqle = "Select valorventa * "+Cantidad.getText()+" from libros where referencia like '"+TextTitulo.getText()+"%'";  // Se declaran como strings las consultas a implementar
        }
        else if (BCompra.isSelected()==true){
            sqle = "Select valorcompra * "+Cantidad.getText()+" from libros where referencia like '"+TextTitulo.getText()+"%'";
        }
        String sqlb = "Select titulo from libros where referencia like '"+TextTitulo.getText()+"%'";
        String sqla = "Select referencia from libros where referencia like '"+TextTitulo.getText()+"%'";
        if (BVenta.isSelected()==true){
            sqlc = "Select Nombre from clientes where DI like '"+TextID.getText()+"%'";
        }
        else if (BCompra.isSelected()==true){
            sqlc = "Select RazonSocial from proveedor where nit like '"+TextID.getText()+"%'";  
        }
        if (BVenta.isSelected()==true){
            sqld = "Select DI from clientes where DI like '"+TextID.getText()+"%'";
        }
        else if (BCompra.isSelected()==true){
            sqld = "Select nit from proveedor where nit like '"+TextID.getText()+"%'";    
        }         
        try { 
            Statement st=cn.createStatement();  // Esto inicializa la conexión a BD indicando que se va a realizar un llamado a la BD
            ResultSet rs =st.executeQuery(sqlb);  // Ejecuta la consulta indicada, en este caso sqlb, y la almacena en una variable ResultSet
            String datos[] = new String [1];  // Se crea una cadena de string llamada Datos donde se almacenarán los resultados del ResultSet
            while(rs.next()){  // Se recorre el ResultSet para verificar si hay más registros existentes en la BD
                datos[0]=rs.getString(1);  // Da como primer índice de la cadena de string datos el ResultSet
            }
            String dataf = Arrays.toString(datos);  // Convierte la cadena de string datos a una string llamada dataf
            String datad=dataf.replace("[", "");  // Elimina las llaves en el string
            String data=datad.replace("]", "");
            TextLibro1.setText(data);       
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"Este producto no existe");
        }
         try { 
            Statement st=cn.createStatement();
            String datos[] = new String [1];
            ResultSet rs;
            rs =st.executeQuery(sqle);
            while(rs.next()){
                datos[0]=rs.getString(1);
            }
            String dataf = Arrays.toString(datos);
            String datad=dataf.replace("[", "");
            String data=datad.replace("]", "");
            TextoPrecio.setText(data);
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try { 
            Statement st=cn.createStatement();
            ResultSet rs =st.executeQuery(sqla);
            String datos[] = new String [1];
            while(rs.next()){
                datos[0]=rs.getString(1);
            }
            String dataf = Arrays.toString(datos);
            String datad=dataf.replace("[", "");
            String data=datad.replace("]", "");
            if (TextoReferencia2.getText()=="null"){
                TextoReferencia2.setText("Esta referencia no existe");
            }
            TextoReferencia2.setText(data);
            if (TextTitulo.getText().isEmpty()){
                TextLibro1.setText(" ");
                TextoReferencia2.setText(" ");
            }
            else if  (TextTitulo.getText()==" "){
                TextLibro1.setText(" ");
                TextoReferencia2.setText(" ");
            }          
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try { 
            Statement st=cn.createStatement();
            ResultSet rs =st.executeQuery(sqlc);
            String[] datos = new String [1];
            while(rs.next()){
                datos[0]=rs.getString(1);
            }
            String dataf = Arrays.toString(datos);
            String datad=dataf.replace("[", "");
            String data=datad.replace("]", "");
            TextoDI.setText(data);
             if (TextID.getText().isEmpty()){
                TextoDI.setText(" ");
            }
            else if  (TextID.getText()==" "){
                TextoDI.setText(" ");
            }
            else if (TextoDI.getText()=="null"){
                TextoDI.setText("Este usuario no existe");
            }
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try { 
            Statement st=cn.createStatement();
            ResultSet rs =st.executeQuery(sqld);
            String[] datos= new String [1];
            while(rs.next()){
            datos[0]=rs.getString(1);
            }
            String dataf = Arrays.toString(datos);
            String datad=dataf.replace("[", "");
            String data=datad.replace("]", "");
            TextCC.setText(data);
            if (TextID.getText().isEmpty()){
                TextCC.setText(" ");
            }
            else if  (TextID.getText()==" "){
                TextCC.setText(" ");
            }
            else if (TextoDI.getText()=="null"){
                TextCC.setText("Este usuario no existe");
            }
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void restar(){  // Se declara la función restar que descuenta de existencias el producto comprado
        if (BVenta.isSelected()==true){
            String sqlb = "update libros set existencias = existencias - '"+ Cantidad.getText() +"' where referencia = '"+TextoReferencia2.getText()+"'";
            try{
               PreparedStatement pps = cn.prepareStatement(sqlb);
               pps.executeUpdate();
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);     
            }
        }
        else if (BCompra.isSelected()==true){
            String sqlb = "update libros set existencias = existencias + '"+ Cantidad.getText() +"' where referencia = '"+TextoReferencia2.getText()+"'";
            try{
               PreparedStatement pps = cn.prepareStatement(sqlb);
               pps.executeUpdate();
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);     
            }
        }
        }
    void buscartiemporeal(){  // Se declara la función tiempo real que permite realizar búsquedas en tiempo real
        if (Seleccion=="Clientes"){
            DefaultTableModel modelo =new DefaultTableModel();
            modelo.addColumn("DI");
            modelo.addColumn("Dirección");
            modelo.addColumn("Email");
            modelo.addColumn("Nombre");
            modelo.addColumn("Nota");
            modelo.addColumn("Teléfono Fijo");
            modelo.addColumn("Teléfono Móvil");
            TablaDatos.setModel(modelo); 
            String sql = "SELECT * FROM clientes where DI like '%"+txtbuscar3.getText()+"%'";
            String datos[] = new String [7];
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sql);
                while(rs.next()){
                    datos[0]=rs.getString(1);
                    datos[1]=rs.getString(2);
                    datos[2]=rs.getString(3);
                    datos[3]=rs.getString(4);
                    datos[4]=rs.getString(5);
                    datos[5]=rs.getString(6);
                    datos[6]=rs.getString(7);
                    modelo.addRow(datos);
                }
            TablaDatos.setModel(modelo);
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }       
        }
        if (Seleccion=="Proveedores"){
            DefaultTableModel modelo =new DefaultTableModel();
            modelo.addColumn("Nit");
            modelo.addColumn("Dirección");
            modelo.addColumn("Email");
            modelo.addColumn("Razon Social");
            modelo.addColumn("Teléfono Fijo");
            modelo.addColumn("Teléfono Móil");
            TablaDatos.setModel(modelo);
            String sql = "SELECT * FROM proveedor where Nit like '%"+txtbuscar3.getText()+"%'";
            String datos[] = new String [6];
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sql);
                while(rs.next()){
                    datos[0]=rs.getString(1);
                    datos[1]=rs.getString(2);
                    datos[2]=rs.getString(3);
                    datos[3]=rs.getString(4);
                    datos[4]=rs.getString(5);
                    datos[5]=rs.getString(6);
                    modelo.addRow(datos);
                }
                TablaDatos.setModel(modelo);
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }       
        }
        if (Seleccion=="Libros"){
            DefaultTableModel modelo =new DefaultTableModel();
            modelo.addColumn("Referencia");
            modelo.addColumn("Titulo");
            modelo.addColumn("Autor");
            modelo.addColumn("Editorial");
            modelo.addColumn("Genero");
            modelo.addColumn("Subgenero");
            modelo.addColumn("Fecha Publicacion");
            modelo.addColumn("Proveedor");
            modelo.addColumn("Precio Compra");
            modelo.addColumn("Precio Venta");
            modelo.addColumn("Existencias");
            TablaDatos.setModel(modelo);       
            String sql = "SELECT * FROM libros where Referencia like '"+txtbuscar3.getText()+"%' or Titulo like '%"+txtbuscar3.getText()+"%' or Editorial like '%"+txtbuscar3.getText()+"%' or Autor like '%"+txtbuscar3.getText()+"%'";
            String datos[] = new String [11];       
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sql);
                while(rs.next()){
                    datos[0]=rs.getString(1);
                    datos[1]=rs.getString(2);
                    datos[2]=rs.getString(3);
                    datos[3]=rs.getString(4);
                    datos[4]=rs.getString(5);
                    datos[5]=rs.getString(6);
                    datos[6]=rs.getString(7);
                    datos[7]=rs.getString(8);
                    datos[8]=rs.getString(9);
                    datos[9]=rs.getString(10);
                    datos[10]=rs.getString(11);
                    modelo.addRow(datos);
                }
                TablaDatos.setModel(modelo);
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Seleccion=="Ventas/Compras"){ 
            DefaultTableModel modelo =new DefaultTableModel();
            modelo.addColumn("Número de factura");
            modelo.addColumn("Número de identificación");
            modelo.addColumn("Titulo del libro");
            modelo.addColumn("Referencia");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Valor");
            modelo.addColumn("Fecha de facturación");
            modelo.addColumn("Tipo de factura");
            TablaDatos.setModel(modelo);        
            String sql = "SELECT * FROM facturas where NumFactura like '%"+txtbuscar3.getText()+"%' or TipoFactura like '%"+txtbuscar3.getText()+"%'";       
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sql);
                String datos[] = new String [8];
                while(rs.next()){
                    datos[0]=rs.getString(1);
                    datos[1]=rs.getString(2);
                    datos[2]=rs.getString(3);
                    datos[3]=rs.getString(4);
                    datos[4]=rs.getString(5);
                    datos[5]=rs.getString(6);
                    datos[6]=rs.getString(7);
                    datos[7]=rs.getString(8);
                    modelo.addRow(datos);
                }
                TablaDatos.setModel(modelo);
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }       
        }
    }
    void mostrartabla(){  // Se declara la función mostrartabla que carga en la tabla tabladatos los datos de cada tabla
        if (Seleccion=="Ventas/Compras"){
            DefaultTableModel modelo =new DefaultTableModel();
            modelo.addColumn("Número de factura");
            modelo.addColumn("Número de identificación");
            modelo.addColumn("Titulo del libro");
            modelo.addColumn("Referencia");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Valor");
            modelo.addColumn("Fecha de facturación");
            modelo.addColumn("Tipo de factura");
            TablaDatos.setModel(modelo);       
            String sql = "SELECT * FROM facturas";
            try {
                String datos[] = new String [8];
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sql);
                while(rs.next()){
                    datos[0]=rs.getString(1);
                    datos[1]=rs.getString(2);
                    datos[2]=rs.getString(3);
                    datos[3]=rs.getString(4);
                    datos[4]=rs.getString(5);
                    datos[5]=rs.getString(6);
                    datos[6]=rs.getString(7);
                    datos[7]=rs.getString(8);
                    modelo.addRow(datos);
                }
                TablaDatos.setModel(modelo);
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }                           
        }
        if (Seleccion=="Clientes"){
            DefaultTableModel modelo =new DefaultTableModel();
            modelo.addColumn("DI");
            modelo.addColumn("Direccion");
            modelo.addColumn("Email");
            modelo.addColumn("Nombre");
            modelo.addColumn("Nota");
            modelo.addColumn("TelefonoFijo");
            modelo.addColumn("TelefonoMovil");
            TablaDatos.setModel(modelo);       
            String sql = "SELECT * FROM clientes";
            String datos[] = new String [7];       
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sql);
                while(rs.next()){
                    datos[0]=rs.getString(1);
                    datos[1]=rs.getString(2);
                    datos[2]=rs.getString(3);
                    datos[3]=rs.getString(4);
                    datos[4]=rs.getString(5);
                    datos[5]=rs.getString(6);
                    datos[6]=rs.getString(7);
                    modelo.addRow(datos);
                }
                TablaDatos.setModel(modelo);
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Seleccion=="Proveedores"){
            DefaultTableModel modelo =new DefaultTableModel();
            modelo.addColumn("Nit");
            modelo.addColumn("Direccion");
            modelo.addColumn("Email");
            modelo.addColumn("RazonSocial");
            modelo.addColumn("TelefonoFijo");
            modelo.addColumn("TelefonoMovil");
            TablaDatos.setModel(modelo);       
            String sql = "SELECT * FROM proveedor";       
            String datos[] = new String [6];        
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sql);
                while(rs.next()){
                    datos[0]=rs.getString(1);
                    datos[1]=rs.getString(2);
                    datos[2]=rs.getString(3);
                    datos[3]=rs.getString(4);
                    datos[4]=rs.getString(5);
                    datos[5]=rs.getString(6);
                    modelo.addRow(datos);
                }
                TablaDatos.setModel(modelo);
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Seleccion=="Libros"){
            DefaultTableModel modelo =new DefaultTableModel();
            modelo.addColumn("Referencia");
            modelo.addColumn("Titulo");
            modelo.addColumn("Autor");
            modelo.addColumn("Editorial");
            modelo.addColumn("Genero");
            modelo.addColumn("Subgenero");
            modelo.addColumn("Fecha Publicacion");
            modelo.addColumn("Proveedor");
            modelo.addColumn("Precio Compra");
            modelo.addColumn("Precio Venta");
            modelo.addColumn("Existencias");
            TablaDatos.setModel(modelo);       
            String sql = "SELECT * FROM libros";        
            String datos[] = new String [11];       
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sql);
                while(rs.next()){
                    datos[0]=rs.getString(1);
                    datos[1]=rs.getString(2);
                    datos[2]=rs.getString(3);
                    datos[3]=rs.getString(4);
                    datos[4]=rs.getString(5);
                    datos[5]=rs.getString(6);
                    datos[6]=rs.getString(7);
                    datos[7]=rs.getString(8);
                    datos[8]=rs.getString(9);
                    datos[9]=rs.getString(10);
                    datos[10]=rs.getString(11);
              modelo.addRow(datos);
                }
                TablaDatos.setModel(modelo);
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    void limpiar(){  // Se declara la función limpiar que limpia los campos de texto en el JFrame
        txtnombre.setText("");
        txtdireccion.setText("");
        txtnota.setText("");
        txtcorreo.setText("");
        txtDI.setText("");
        TextoTel.setText("");
        TextoCel.setText("");
        txtnombre1.setText("");
        txtdireccion1.setText("");
        txtcorreo1.setText("");
        txtDI1.setText("");
        TextoTel1.setText("");
        TextoCel1.setText("");
        TextoReferenc.setText("");
        TextTitu.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtGenero.setText("");
        Cantidad.setText("");
        TextSub.setText("");
        txtFPublic.setDate(null);
        TextoProve.setText("");
        TextoVenta.setText("");
        TextoCompra.setText("");
        Existencias.setText("");
        TFact.clearSelection();
        TxtNFac.setText("");
        TextID.setText("");
        TextTitulo.setText("");
        TextoReferencia2.setText("");
        TextoPrecio.setText("");
        TextLibro1.setText("");
        TextCC.setText("");
        TextoDI.setText("");
        txtFFac.setDate(null);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        TFact = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        SeleccionModulo = new javax.swing.JComboBox();
        Seleccionar = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        PanelProveedores = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtnombre1 = new javax.swing.JTextField();
        txtdireccion1 = new javax.swing.JTextField();
        txtcorreo1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtDI1 = new javax.swing.JTextField();
        LabelTel1 = new javax.swing.JLabel();
        TextoTel1 = new javax.swing.JTextField();
        LabelCel1 = new javax.swing.JLabel();
        TextoCel1 = new javax.swing.JTextField();
        txtbuscar3 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtdireccion = new javax.swing.JTextField();
        txtcorreo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDI = new javax.swing.JTextField();
        LabelTel = new javax.swing.JLabel();
        TextoTel = new javax.swing.JTextField();
        LabelCel = new javax.swing.JLabel();
        TextoCel = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtnota = new javax.swing.JTextArea();
        PanelLibros = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        FechaPublicacion = new javax.swing.JLabel();
        txtEditorial = new javax.swing.JTextField();
        txtGenero = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtAutor = new javax.swing.JTextField();
        LabelTel2 = new javax.swing.JLabel();
        TextoProve = new javax.swing.JTextField();
        LabelCel2 = new javax.swing.JLabel();
        TextoReferenc = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        TextSub = new javax.swing.JTextField();
        TextTitu = new javax.swing.JTextField();
        TextoVenta = new javax.swing.JTextField();
        TextoCompra = new javax.swing.JTextField();
        Existencias = new javax.swing.JTextField();
        txtFPublic = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaDatos = new javax.swing.JTable();
        PanelFacturas2 = new javax.swing.JPanel();
        FechaPublicacion3 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        LabelCel3 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        TextID = new javax.swing.JTextField();
        TextTitulo = new javax.swing.JTextField();
        txtFFac = new com.toedter.calendar.JDateChooser();
        jLabel35 = new javax.swing.JLabel();
        TxtNFac = new javax.swing.JLabel();
        TextLibro1 = new javax.swing.JLabel();
        TextoReferencia2 = new javax.swing.JLabel();
        TextoDI = new javax.swing.JLabel();
        TextCC = new javax.swing.JLabel();
        TextoPrecio = new javax.swing.JLabel();
        BVenta = new javax.swing.JRadioButton();
        BCompra = new javax.swing.JRadioButton();
        labelcanti = new javax.swing.JLabel();
        Cantidad = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        TituloInfo = new javax.swing.JLabel();
        Parrafo1 = new javax.swing.JLabel();
        Parrafo2 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        Filtros = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        FechaInicio = new com.toedter.calendar.JDateChooser();
        FechaFin = new com.toedter.calendar.JDateChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        Reportes = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        LPromedio = new javax.swing.JLabel();
        LTotal = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        Librostotales = new javax.swing.JLabel();
        Librosavg = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        txtnsql = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtdirsql = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtportsql = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtusersql = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtpasssql = new javax.swing.JTextField();
        conectar = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        bdname = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();

        jLabel4.setText("jLabel4");

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(143, 176, 170));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(getIconImage());

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jTabbedPane1.setBackground(new java.awt.Color(0, 51, 51));
        jTabbedPane1.setForeground(new java.awt.Color(204, 204, 204));
        jTabbedPane1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Seleccione el módulo a trabajar");

        SeleccionModulo.setBackground(new java.awt.Color(0, 102, 102));
        SeleccionModulo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        SeleccionModulo.setForeground(new java.awt.Color(204, 204, 204));
        SeleccionModulo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione un módulo", "Libros", "Clientes", "Proveedores", "Ventas/Compras" }));
        SeleccionModulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeleccionModuloActionPerformed(evt);
            }
        });

        Seleccionar.setBackground(new java.awt.Color(0, 102, 102));
        Seleccionar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        Seleccionar.setForeground(new java.awt.Color(204, 204, 204));
        Seleccionar.setText("Seleccionar");
        Seleccionar.setBorderPainted(false);
        Seleccionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Seleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeleccionarActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 0, 0));
        jLabel41.setText("Bienvenido a IQBookshelf");

        jLabel42.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setText("Para comenzar seleccione un módulo entre los cuatro disponibles");

        jLabel43.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 0, 0));
        jLabel43.setText("Después cliquee el botón Seleccionar");

        jLabel44.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 0, 0));
        jLabel44.setText("Finalmente prosiga a la pestaña \"área de trabajo\"");

        jLabel45.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 0, 0));
        jLabel45.setText("En caso que se haya activado la pestaña configuración ");

        jLabel46.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 0, 0));
        jLabel46.setText("será necesario realizar una breve configuración");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(Seleccionar)
                    .addComponent(SeleccionModulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45)
                    .addComponent(jLabel44)
                    .addComponent(jLabel43)
                    .addComponent(jLabel42)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addContainerGap(1676, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel46))
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(SeleccionModulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Seleccionar)))
                .addContainerGap(356, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Inicio", jPanel2);

        jPanel3.setBackground(new java.awt.Color(0, 153, 153));
        jPanel3.setForeground(new java.awt.Color(0, 0, 0));

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(204, 204, 204));
        jButton1.setText("Guardar");
        jButton1.setBorderPainted(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton1KeyReleased(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 102, 102));
        jButton2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(204, 204, 204));
        jButton2.setText("Limpiar");
        jButton2.setBorderPainted(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(204, 204, 204));
        jButton3.setText("Modificar");
        jButton3.setBorderPainted(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 102, 102));
        jButton5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(204, 204, 204));
        jButton5.setText("Eliminar");
        jButton5.setBorderPainted(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        PanelProveedores.setBackground(new java.awt.Color(0, 102, 102));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 204, 204));
        jLabel8.setText("Razón Social");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 204, 204));
        jLabel10.setText("Dirección");

        txtnombre1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtnombre1.setForeground(new java.awt.Color(0, 0, 0));
        txtnombre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombre1ActionPerformed(evt);
            }
        });

        txtdireccion1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtdireccion1.setForeground(new java.awt.Color(0, 0, 0));
        txtdireccion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdireccion1ActionPerformed(evt);
            }
        });

        txtcorreo1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtcorreo1.setForeground(new java.awt.Color(0, 0, 0));
        txtcorreo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcorreo1ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 204, 204));
        jLabel11.setText("Email");

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 204, 204));
        jLabel12.setText("NIT");

        txtDI1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtDI1.setForeground(new java.awt.Color(0, 0, 0));
        txtDI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDI1ActionPerformed(evt);
            }
        });

        LabelTel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LabelTel1.setForeground(new java.awt.Color(204, 204, 204));
        LabelTel1.setText("Telefono Fijo");
        LabelTel1.setName("LabelTel"); // NOI18N

        TextoTel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextoTel1.setForeground(new java.awt.Color(0, 0, 0));
        TextoTel1.setName("TextoTel"); // NOI18N
        TextoTel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextoTel1ActionPerformed(evt);
            }
        });

        LabelCel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LabelCel1.setForeground(new java.awt.Color(204, 204, 204));
        LabelCel1.setText("Telefono Movil");

        TextoCel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextoCel1.setForeground(new java.awt.Color(0, 0, 0));
        TextoCel1.setName("TextoTel"); // NOI18N
        TextoCel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextoCel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelProveedoresLayout = new javax.swing.GroupLayout(PanelProveedores);
        PanelProveedores.setLayout(PanelProveedoresLayout);
        PanelProveedoresLayout.setHorizontalGroup(
            PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProveedoresLayout.createSequentialGroup()
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelProveedoresLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnombre1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                            .addComponent(txtdireccion1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtcorreo1)
                            .addComponent(txtDI1)))
                    .addGroup(PanelProveedoresLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelTel1)
                            .addComponent(LabelCel1))
                        .addGap(33, 33, 33)
                        .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TextoCel1)
                            .addComponent(TextoTel1))))
                .addContainerGap())
        );
        PanelProveedoresLayout.setVerticalGroup(
            PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelProveedoresLayout.createSequentialGroup()
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtDI1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtnombre1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtdireccion1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtcorreo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelTel1)
                    .addComponent(TextoTel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCel1)
                    .addComponent(TextoCel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtbuscar3.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtbuscar3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtbuscar3KeyReleased(evt);
            }
        });

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Búsqueda");

        jPanel6.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("Nombre");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Dirección");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Nota");

        txtnombre.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtnombre.setForeground(new java.awt.Color(0, 0, 0));
        txtnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreActionPerformed(evt);
            }
        });

        txtdireccion.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtdireccion.setForeground(new java.awt.Color(0, 0, 0));
        txtdireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdireccionActionPerformed(evt);
            }
        });

        txtcorreo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtcorreo.setForeground(new java.awt.Color(0, 0, 0));
        txtcorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcorreoActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("Email");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("DI");

        txtDI.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtDI.setForeground(new java.awt.Color(0, 0, 0));
        txtDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDIActionPerformed(evt);
            }
        });

        LabelTel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LabelTel.setForeground(new java.awt.Color(204, 204, 204));
        LabelTel.setText("Telefono Fijo");
        LabelTel.setName("LabelTel"); // NOI18N

        TextoTel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextoTel.setForeground(new java.awt.Color(0, 0, 0));
        TextoTel.setName("TextoTel"); // NOI18N
        TextoTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextoTelActionPerformed(evt);
            }
        });

        LabelCel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LabelCel.setForeground(new java.awt.Color(204, 204, 204));
        LabelCel.setText("Telefono Movil");

        TextoCel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextoCel.setForeground(new java.awt.Color(0, 0, 0));
        TextoCel.setName("TextoTel"); // NOI18N
        TextoCel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextoCelActionPerformed(evt);
            }
        });

        txtnota.setColumns(20);
        txtnota.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtnota.setForeground(new java.awt.Color(0, 0, 0));
        txtnota.setRows(5);
        jScrollPane2.setViewportView(txtnota);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtnombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(txtdireccion, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDI)
                            .addComponent(txtcorreo, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCel)
                            .addComponent(jLabel3)
                            .addComponent(LabelTel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TextoCel)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                            .addComponent(TextoTel))))
                .addGap(3, 3, 3))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelTel)
                    .addComponent(TextoTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCel)
                    .addComponent(TextoCel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelLibros.setBackground(new java.awt.Color(0, 102, 102));
        PanelLibros.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(204, 204, 204));
        jLabel13.setText("Editorial");

        FechaPublicacion.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        FechaPublicacion.setForeground(new java.awt.Color(204, 204, 204));
        FechaPublicacion.setText("Fecha Publicación");

        txtEditorial.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtEditorial.setForeground(new java.awt.Color(0, 0, 0));
        txtEditorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEditorialActionPerformed(evt);
            }
        });

        txtGenero.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtGenero.setForeground(new java.awt.Color(0, 0, 0));
        txtGenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGeneroActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(204, 204, 204));
        jLabel14.setText("Género");

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(204, 204, 204));
        jLabel15.setText("Autor");

        txtAutor.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtAutor.setForeground(new java.awt.Color(0, 0, 0));
        txtAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutorActionPerformed(evt);
            }
        });

        LabelTel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LabelTel2.setForeground(new java.awt.Color(204, 204, 204));
        LabelTel2.setText("Proveedor");
        LabelTel2.setName("LabelTel"); // NOI18N

        TextoProve.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextoProve.setForeground(new java.awt.Color(0, 0, 0));
        TextoProve.setName("TextoProve"); // NOI18N
        TextoProve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextoProveActionPerformed(evt);
            }
        });

        LabelCel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LabelCel2.setForeground(new java.awt.Color(204, 204, 204));
        LabelCel2.setText("Referencia");

        TextoReferenc.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextoReferenc.setForeground(new java.awt.Color(0, 0, 0));
        TextoReferenc.setName("TextoTel"); // NOI18N
        TextoReferenc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextoReferencActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(204, 204, 204));
        jLabel16.setText("Subgénero");

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(204, 204, 204));
        jLabel17.setText("Precio Venta");

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(204, 204, 204));
        jLabel18.setText("Título");

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(204, 204, 204));
        jLabel19.setText("Precio Compra");

        jLabel20.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(204, 204, 204));
        jLabel20.setText("Existencias");

        TextSub.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextSub.setForeground(new java.awt.Color(0, 0, 0));

        TextTitu.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextTitu.setForeground(new java.awt.Color(0, 0, 0));

        TextoVenta.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextoVenta.setForeground(new java.awt.Color(0, 0, 0));

        TextoCompra.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextoCompra.setForeground(new java.awt.Color(0, 0, 0));

        Existencias.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        Existencias.setForeground(new java.awt.Color(0, 0, 0));

        txtFPublic.setBackground(new java.awt.Color(0, 102, 102));
        txtFPublic.setForeground(new java.awt.Color(0, 0, 0));
        txtFPublic.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout PanelLibrosLayout = new javax.swing.GroupLayout(PanelLibros);
        PanelLibros.setLayout(PanelLibrosLayout);
        PanelLibrosLayout.setHorizontalGroup(
            PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelLibrosLayout.createSequentialGroup()
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelLibrosLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelLibrosLayout.createSequentialGroup()
                                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel19))
                                .addGap(35, 35, 35)
                                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelLibrosLayout.createSequentialGroup()
                                        .addComponent(Existencias, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(TextoCompra, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(TextoVenta)))
                            .addGroup(PanelLibrosLayout.createSequentialGroup()
                                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelTel2)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel18)
                                    .addComponent(FechaPublicacion))
                                .addGap(10, 10, 10)
                                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelLibrosLayout.createSequentialGroup()
                                        .addComponent(TextoReferenc, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(TextoProve, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtFPublic, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(TextSub, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(TextTitu, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addGroup(PanelLibrosLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelCel2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtGenero, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                            .addComponent(txtEditorial, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtAutor))))
                .addContainerGap())
        );
        PanelLibrosLayout.setVerticalGroup(
            PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelLibrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCel2)
                    .addComponent(TextoReferenc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(TextTitu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(TextSub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(FechaPublicacion)
                    .addComponent(txtFPublic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelTel2)
                    .addComponent(TextoProve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelLibrosLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PanelLibrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(TextoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Existencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelLibrosLayout.createSequentialGroup()
                        .addComponent(TextoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel20)))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jScrollPane1.setBackground(new java.awt.Color(0, 102, 102));
        jScrollPane1.setForeground(new java.awt.Color(204, 204, 204));
        jScrollPane1.setAutoscrolls(true);

        TablaDatos.setBackground(new java.awt.Color(0, 102, 102));
        TablaDatos.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TablaDatos.setForeground(new java.awt.Color(204, 204, 204));
        TablaDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TablaDatos.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        TablaDatos.setSelectionBackground(new java.awt.Color(0, 153, 153));
        jScrollPane1.setViewportView(TablaDatos);

        PanelFacturas2.setBackground(new java.awt.Color(0, 102, 102));
        PanelFacturas2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        FechaPublicacion3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        FechaPublicacion3.setForeground(new java.awt.Color(204, 204, 204));
        FechaPublicacion3.setText("Fecha facturación");

        jLabel31.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(204, 204, 204));
        jLabel31.setText("Título");

        LabelCel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LabelCel3.setForeground(new java.awt.Color(204, 204, 204));
        LabelCel3.setText("Número de factura");

        jLabel32.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(204, 204, 204));
        jLabel32.setText("Referencia");

        jLabel33.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(204, 204, 204));
        jLabel33.setText("ID");

        jLabel34.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(204, 204, 204));
        jLabel34.setText("Valor");

        TextID.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextIDKeyReleased(evt);
            }
        });

        TextTitulo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        TextTitulo.setForeground(new java.awt.Color(0, 0, 0));
        TextTitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextTituloActionPerformed(evt);
            }
        });
        TextTitulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextTituloKeyReleased(evt);
            }
        });

        txtFFac.setBackground(new java.awt.Color(0, 102, 102));
        txtFFac.setForeground(new java.awt.Color(0, 0, 0));
        txtFFac.setDateFormatString("yyyy-MM-dd");

        jLabel35.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(204, 204, 204));
        jLabel35.setText("Tipo de factura");

        TxtNFac.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        TxtNFac.setForeground(new java.awt.Color(204, 204, 204));

        TextLibro1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        TextLibro1.setForeground(new java.awt.Color(204, 204, 204));

        TextoReferencia2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        TextoReferencia2.setForeground(new java.awt.Color(204, 204, 204));

        TextoDI.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        TextoDI.setForeground(new java.awt.Color(204, 204, 204));

        TextCC.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        TextCC.setForeground(new java.awt.Color(204, 204, 204));

        TextoPrecio.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        TextoPrecio.setForeground(new java.awt.Color(204, 204, 204));

        BVenta.setBackground(new java.awt.Color(0, 102, 102));
        TFact.add(BVenta);
        BVenta.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        BVenta.setForeground(new java.awt.Color(204, 204, 204));
        BVenta.setText("Venta");

        BCompra.setBackground(new java.awt.Color(0, 102, 102));
        TFact.add(BCompra);
        BCompra.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        BCompra.setForeground(new java.awt.Color(204, 204, 204));
        BCompra.setText("Compra");

        labelcanti.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        labelcanti.setForeground(new java.awt.Color(204, 204, 204));
        labelcanti.setText("Cantidad");

        Cantidad.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        Cantidad.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout PanelFacturas2Layout = new javax.swing.GroupLayout(PanelFacturas2);
        PanelFacturas2.setLayout(PanelFacturas2Layout);
        PanelFacturas2Layout.setHorizontalGroup(
            PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFacturas2Layout.createSequentialGroup()
                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelFacturas2Layout.createSequentialGroup()
                        .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(jLabel33)
                            .addComponent(jLabel32)
                            .addComponent(jLabel34)
                            .addComponent(FechaPublicacion3)
                            .addComponent(LabelCel3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelFacturas2Layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TextoReferencia2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(TextoPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(PanelFacturas2Layout.createSequentialGroup()
                                        .addComponent(TextoDI, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(TextCC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(PanelFacturas2Layout.createSequentialGroup()
                                        .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(TextTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(TextID, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtFFac, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(PanelFacturas2Layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(TxtNFac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(PanelFacturas2Layout.createSequentialGroup()
                                        .addComponent(BVenta)
                                        .addGap(18, 18, 18)
                                        .addComponent(BCompra)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(PanelFacturas2Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(171, 171, 171)
                        .addComponent(TextLibro1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelFacturas2Layout.createSequentialGroup()
                        .addComponent(labelcanti)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelFacturas2Layout.setVerticalGroup(
            PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFacturas2Layout.createSequentialGroup()
                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(BVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelCel3)
                    .addComponent(TxtNFac, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(TextID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TextoDI, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextCC, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelcanti)
                    .addComponent(Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelFacturas2Layout.createSequentialGroup()
                        .addComponent(TextLibro1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(TextTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextoReferencia2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtFFac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FechaPublicacion3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelFacturas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TextoPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(0, 153, 153));

        TituloInfo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        TituloInfo.setForeground(new java.awt.Color(0, 0, 0));

        Parrafo1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        Parrafo1.setForeground(new java.awt.Color(0, 0, 0));

        Parrafo2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        Parrafo2.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TituloInfo)
            .addComponent(Parrafo1)
            .addComponent(Parrafo2)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(TituloInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Parrafo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Parrafo2)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(0, 153, 153));
        jPanel11.setForeground(new java.awt.Color(0, 0, 0));

        jLabel50.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 0, 0));
        jLabel50.setText("El botón Guardar comprobará la existencia de la información a ingresar");

        jLabel51.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 0, 0));
        jLabel51.setText("de esta manera actualizará o generará un nuevo registro automáticamente");

        jLabel52.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 0, 0));
        jLabel52.setText("Limpiar nos permite dejar todos los campos vacíos.");

        jLabel53.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 0, 0));
        jLabel53.setText("Para eliminar o modificar un registro primero se debe seleccionar en la tabla de datos.");

        jLabel54.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 0, 0));
        jLabel54.setText("Para guardar los cambios tras una modificación se usa el botón Guardar. ");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50)
                    .addComponent(jLabel51)
                    .addComponent(jLabel52)
                    .addComponent(jLabel53)
                    .addComponent(jLabel54))
                .addGap(0, 4, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel51)
                .addGap(18, 18, 18)
                .addComponent(jLabel52)
                .addGap(18, 18, 18)
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel54)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        Filtros.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        Filtros.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(PanelLibros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PanelProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanelFacturas2, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)
                                .addGap(453, 453, 453))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(57, 57, 57)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(txtbuscar3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(82, 82, 82)
                                        .addComponent(Filtros, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(97, 97, 97))))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(PanelProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelLibros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelFacturas2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtbuscar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)
                                        .addComponent(Filtros)
                                        .addGap(41, 41, 41)
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(144, 144, 144)
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton5))))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        PanelLibros.getAccessibleContext().setAccessibleName("");

        jTabbedPane1.addTab("Área de trabajo", jPanel3);

        jPanel4.setBackground(new java.awt.Color(0, 153, 153));

        jPanel9.setBackground(new java.awt.Color(0, 153, 153));

        FechaInicio.setBackground(new java.awt.Color(0, 102, 102));
        FechaInicio.setDateFormatString("yyyy-MM-dd");

        FechaFin.setBackground(new java.awt.Color(0, 102, 102));
        FechaFin.setDateFormatString("yyyy-MM-dd");

        jScrollPane3.setBackground(new java.awt.Color(0, 102, 102));
        jScrollPane3.setForeground(new java.awt.Color(204, 204, 204));

        Reportes.setBackground(new java.awt.Color(0, 102, 102));
        Reportes.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        Reportes.setForeground(new java.awt.Color(204, 204, 204));
        Reportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        Reportes.setToolTipText("");
        jScrollPane3.setViewportView(Reportes);

        jLabel29.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 0));
        jLabel29.setText("Ver facturas desde");

        jLabel30.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 0));
        jLabel30.setText("hasta");

        jLabel36.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(204, 204, 204));
        jLabel36.setText("Ventas totales en el lapso de tiempo");

        jLabel37.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(204, 204, 204));
        jLabel37.setText("Promedio de ventas diarias");

        LPromedio.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LPromedio.setForeground(new java.awt.Color(204, 204, 204));

        LTotal.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LTotal.setForeground(new java.awt.Color(204, 204, 204));

        jButton4.setBackground(new java.awt.Color(0, 102, 102));
        jButton4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(204, 204, 204));
        jButton4.setText("Consultar");
        jButton4.setBorderPainted(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(204, 204, 204));
        jLabel39.setText("Promedio de libros vendidos cada día");

        jLabel40.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(204, 204, 204));
        jLabel40.setText("Libros vendidos en el lapso de tiempo");

        Librostotales.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        Librostotales.setForeground(new java.awt.Color(204, 204, 204));

        Librosavg.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        Librosavg.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 807, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel36)
                            .addComponent(jLabel40)
                            .addComponent(jLabel39))
                        .addGap(148, 148, 148)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(Librosavg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                            .addComponent(Librostotales, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LPromedio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(FechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jButton4))
                        .addGap(48, 48, 48)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LPromedio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel40))
                    .addComponent(Librostotales, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Librosavg, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addContainerGap(228, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2014, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 49, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Reportes", jPanel4);

        jPanel5.setBackground(new java.awt.Color(0, 153, 153));

        jPanel7.setBackground(new java.awt.Color(0, 153, 153));

        txtnsql.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnsqlActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(204, 204, 204));
        jLabel23.setText("Conectarse a SQL");

        jLabel24.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(204, 204, 204));
        jLabel24.setText("Dirección");

        txtdirsql.setText("localhost");

        jLabel25.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(204, 204, 204));
        jLabel25.setText("Puerto");

        txtportsql.setText("3306");

        jLabel26.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(204, 204, 204));
        jLabel26.setText("Usuario");

        txtusersql.setText("root");

        jLabel27.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(204, 204, 204));
        jLabel27.setText("Contraseña");

        conectar.setBackground(new java.awt.Color(0, 102, 102));
        conectar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        conectar.setForeground(new java.awt.Color(204, 204, 204));
        conectar.setText("Conectar");
        conectar.setBorderPainted(false);
        conectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conectarActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(204, 204, 204));
        jLabel28.setText("Nombre base de datos");

        jPanel8.setBackground(new java.awt.Color(0, 153, 153));

        jLabel22.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(204, 204, 204));
        jLabel22.setText("Nombre de la nueva base de datos");

        bdname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bdnameActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(0, 102, 102));
        jButton7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(204, 204, 204));
        jButton7.setText("Crear base de datos");
        jButton7.setBorderPainted(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 0, 0));
        jLabel48.setText("Esta es la pestaña de configuración de la base de datos");

        jLabel49.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 0, 0));
        jLabel49.setText("Si ya tiene una base de datos ingrese los datos del servidor y conéctese");

        jLabel55.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 0, 0));
        jLabel55.setText("De lo contrario, ingrese los datos del servidor, un nombre ");

        jLabel56.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(0, 0, 0));
        jLabel56.setText("para la nueva base de datos y presione el botón \"Crear base de datos\".");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(bdname, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel56)
                            .addComponent(jLabel48)
                            .addComponent(jLabel55))
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel49)
                        .addContainerGap())))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bdname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel48)
                .addGap(18, 18, 18)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56)
                .addContainerGap())
        );

        jLabel57.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(0, 0, 0));
        jLabel57.setText("Después de creada puede proceder a conectarse a esta.");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(conectar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel24))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtdirsql, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtnsql, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtusersql, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel27)
                                        .addComponent(jLabel25)
                                        .addComponent(jLabel26))
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                            .addGap(95, 95, 95)
                                            .addComponent(txtportsql, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtpasssql, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel57)
                                .addGap(122, 122, 122))))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtdirsql, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(txtnsql, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtportsql, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtusersql, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtpasssql, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(conectar)
                .addContainerGap(172, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1561, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 126, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configuración", jPanel5);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeleccionarActionPerformed
        // El boton seleccionar comprueba el valor de la variable Seleccion y habilita o deshabilita pestañas según esta
        if (Seleccion=="Seleccione un módulo"){
            jTabbedPane1.setEnabled(false);
        }
        Rectangle b = jPanel6.getBounds();
        if (Seleccion=="Proveedores"){   
            jPanel6.setVisible(false);
            PanelLibros.setVisible(false); 
            PanelProveedores.setVisible(true);
            PanelFacturas2.setVisible(false);
            jTabbedPane1.setEnabled(true);
            jTabbedPane1.setEnabledAt(2, false);
            TituloInfo.setText("Proveedores");
            Parrafo1.setText("Desde este módulo podrá gestionar toda la información");
            Parrafo2.setText("referente a los proveedores.");
            Filtros.setText("Filtros: Nit");
            mostrartabla();
            String path = "/imagenes/Proveedor.png";
            URL url = this.getClass().getResource(path);
            ImageIcon icon = new ImageIcon(url);
            Icon icono = new ImageIcon(icon.getImage().getScaledInstance(jLabel38.getWidth(), jLabel38.getHeight(), Image.SCALE_DEFAULT));
            jLabel38.setIcon(icono);
            this.repaint();
        }
        if(Seleccion=="Clientes"){
            jPanel6.setVisible(true);
            PanelProveedores.setVisible(false);
            PanelLibros.setVisible(false); 
            PanelFacturas2.setVisible(false);
            jTabbedPane1.setEnabled(true);
            jTabbedPane1.setEnabledAt(2, false);
            TituloInfo.setText("Clientes");
            Parrafo1.setText("Desde este módulo podrá gestionar toda la información");
            Parrafo2.setText("referente a los clientes.");
            Filtros.setText("Filtros: documento de identidad");
            mostrartabla();
            String path = "/imagenes/Cliente.png";
            URL url = this.getClass().getResource(path);
            ImageIcon icon = new ImageIcon(url);
            Icon icono = new ImageIcon(icon.getImage().getScaledInstance(jLabel38.getWidth(), jLabel38.getHeight(), Image.SCALE_DEFAULT));
            jLabel38.setIcon(icono);
            this.repaint();
        }
        if(Seleccion=="Libros"){
           jPanel6.setVisible(false);
           PanelProveedores.setVisible(false);
           PanelLibros.setBounds(b);
           PanelLibros.setVisible(true); 
           PanelFacturas2.setVisible(false);
           jTabbedPane1.setEnabled(true);
           jTabbedPane1.setEnabledAt(2, false);
           TituloInfo.setText("Libros");
           Parrafo1.setText("Desde este módulo podrá gestionar toda la información");
           Parrafo2.setText("referente a los libros.");
           Filtros.setText("Filtros: referencia, título, editorial");
           mostrartabla();
           String path = "/imagenes/Libros.png";
           URL url = this.getClass().getResource(path);
           ImageIcon icon = new ImageIcon(url);
           Icon icono = new ImageIcon(icon.getImage().getScaledInstance(jLabel38.getWidth(), jLabel38.getHeight(), Image.SCALE_DEFAULT));
           jLabel38.setIcon(icono);
           this.repaint();
        }
        if(Seleccion=="Ventas/Compras"){
           jPanel6.setVisible(false);
           PanelProveedores.setVisible(false);
           PanelLibros.setVisible(false); 
           PanelFacturas2.setBounds(b);
           PanelFacturas2.setVisible(true);
           jTabbedPane1.setEnabled(true);
           jTabbedPane1.setEnabledAt(2, true);
           TituloInfo.setText("Ventas/Compras");
           Parrafo1.setText("Desde este módulo podrá gestionar toda la información");
           Parrafo2.setText("referente a las ventas y/o las compras.");
           Filtros.setText("Filtros: número o tipo de factura");
           mostrartabla();
           String path = "/imagenes/Factura.png";
           URL url = this.getClass().getResource(path);
           ImageIcon icon = new ImageIcon(url);
           Icon icono = new ImageIcon(icon.getImage().getScaledInstance(jLabel38.getWidth(), jLabel38.getHeight(), Image.SCALE_DEFAULT));
           jLabel38.setIcon(icono);
           this.repaint();
        }     
    }//GEN-LAST:event_SeleccionarActionPerformed

    private void SeleccionModuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeleccionModuloActionPerformed
        SeleccionModulo.addActionListener((ActionListener) this);  // Se agrega un ActionListener al jComboBox   
    }//GEN-LAST:event_SeleccionModuloActionPerformed

    private void txtnsqlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnsqlActionPerformed

    }//GEN-LAST:event_txtnsqlActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // Este botón comprueba si la BD a crear no existe y si es así la crea
        cn=con.CrearBD(txtdirsql.getText(),txtportsql.getText(),txtusersql.getText(),txtpasssql.getText());
        try {
            Statement st = cn.createStatement();
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS "+bdname.getText());
            st.close();
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        cn=con.conecxion(txtdirsql.getText(),txtportsql.getText(),bdname.getText(),txtusersql.getText(),txtpasssql.getText());
        try {
            Statement st = cn.createStatement();
            st.executeUpdate("CREATE TABLE clientes (DI varchar(11) PRIMARY KEY, Direccion VARCHAR(100), Email VARCHAR(100)"
            + ", Nombre VARCHAR(70), Nota VARCHAR(200), TelefonoFijo varchar (10), TelefonoMovil varchar(20))");
            st.executeUpdate("Create table proveedor (Nit varchar (20) primary key, Direccion varchar (100), Email varchar (100)"
            +", RazonSocial varchar (200), TelefonoFijo int (10), TelefonoMovil bigint (15))");
            st.executeUpdate("Create table libros (Referencia bigint (200) primary key, Titulo varchar (100), Autor varchar (100), Editorial varchar (100)"
            +", Genero varchar (50), Subgenero varchar (50), FechaPublicacion date, Proveedor varchar(20), ValorCompra int (20)"
            +", ValorVenta int (20), Existencias int (11))"); st.executeUpdate("Create table eliminacion (Seccion varchar(20), CampoEliminado varchar (50)"
            +", Justificacion varchar (50))"); st.executeUpdate("Create table Facturas(NumFactura int (6) primary key auto_increment, DI varchar (20)"
            +", Titulo varchar (200), Referencia bigint (100), Cantidad int(11), Valor int (20), FechaFactura date, TipoFactura varchar (20), CONSTRAINT fk_Clientes FOREIGN KEY (DI) REFERENCES CLIENTES (DI), CONSTRAINT fk_Libros FOREIGN KEY (Referencia) REFERENCES Libros (Referencia))");               
            st.close();
            JOptionPane.showMessageDialog(null,"Base de datos: "+bdname.getText()+" creada, ahora conectate a ella");      
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"Error al crear la base de datos");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void conectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conectarActionPerformed
        // Este botón inicializa la conexión a BD una vez se ha introducido la información de la configuración
        String socket=txtdirsql.getText()+","+txtportsql.getText()+","
        +txtnsql.getText()+","+txtusersql.getText()+","+txtpasssql.getText();
        carchivo.CArchivo(socket);
        recuperarDatos();
        cn=con.conecxion(txtdirsql.getText(),txtportsql.getText(),txtnsql.getText(),txtusersql.getText(),txtpasssql.getText());
        if (cn!=null){
            jTabbedPane1.setEnabledAt(3, false);
            jTabbedPane1.setEnabledAt(1, true);
            jTabbedPane1.setEnabledAt(2, true);
            jTabbedPane1.setEnabledAt(0, true);
            JOptionPane.showMessageDialog(null,"Éxito al conectar");
        }
        else {
            JOptionPane.showMessageDialog(null,"Error al conectar");
        }
    }//GEN-LAST:event_conectarActionPerformed

    private void bdnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bdnameActionPerformed

    }//GEN-LAST:event_bdnameActionPerformed

    private void TextoReferencActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextoReferencActionPerformed

    }//GEN-LAST:event_TextoReferencActionPerformed

    private void TextoProveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextoProveActionPerformed
      
    }//GEN-LAST:event_TextoProveActionPerformed

    private void txtAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAutorActionPerformed
      
    }//GEN-LAST:event_txtAutorActionPerformed

    private void txtGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGeneroActionPerformed
      
    }//GEN-LAST:event_txtGeneroActionPerformed

    private void txtEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEditorialActionPerformed
    }//GEN-LAST:event_txtEditorialActionPerformed

    private void TextoCelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextoCelActionPerformed
    }//GEN-LAST:event_TextoCelActionPerformed

    private void TextoTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextoTelActionPerformed
    }//GEN-LAST:event_TextoTelActionPerformed

    private void txtDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDIActionPerformed
    }//GEN-LAST:event_txtDIActionPerformed

    private void txtcorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcorreoActionPerformed
    }//GEN-LAST:event_txtcorreoActionPerformed

    private void txtdireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdireccionActionPerformed
    }//GEN-LAST:event_txtdireccionActionPerformed

    private void txtnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreActionPerformed

    }//GEN-LAST:event_txtnombreActionPerformed

    private void txtbuscar3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscar3KeyReleased
        buscartiemporeal();
    }//GEN-LAST:event_txtbuscar3KeyReleased

    private void TextoCel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextoCel1ActionPerformed

    }//GEN-LAST:event_TextoCel1ActionPerformed

    private void TextoTel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextoTel1ActionPerformed

    }//GEN-LAST:event_TextoTel1ActionPerformed

    private void txtDI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDI1ActionPerformed

    }//GEN-LAST:event_txtDI1ActionPerformed

    private void txtcorreo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcorreo1ActionPerformed
  
    }//GEN-LAST:event_txtcorreo1ActionPerformed

    private void txtdireccion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdireccion1ActionPerformed

    }//GEN-LAST:event_txtdireccion1ActionPerformed

    private void txtnombre1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombre1ActionPerformed

    }//GEN-LAST:event_txtnombre1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Este botón elimina un campo seleccionado del módulo habilitado
        if (Seleccion=="Clientes"){
            int fila=TablaDatos.getSelectedRow();
            String valor=TablaDatos.getValueAt(fila, 0).toString();
            if(fila>=0){
                int reply = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar este dato?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (reply==JOptionPane.YES_OPTION){   
                    try {
                        PreparedStatement pps = cn.prepareStatement("DELETE FROM clientes WHERE DI='"+valor+"'");
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Dato eliminado");
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (Seleccion=="Proveedores"){
            int fila=TablaDatos.getSelectedRow();
            String valor=TablaDatos.getValueAt(fila, 0).toString();
            if(fila>=0){
                int reply = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar este dato?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (reply==JOptionPane.YES_OPTION){   
                    try {
                        PreparedStatement pps = cn.prepareStatement("DELETE FROM proveedor WHERE nit='"+valor+"'");
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Dato eliminado");
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (Seleccion=="Ventas/Compras"){
            int fila=TablaDatos.getSelectedRow();
            String valor=TablaDatos.getValueAt(fila, 0).toString();
            if(fila>=0){
                String razon = JOptionPane.showInputDialog("Ingrese una razón para eliminar el campo");
                int reply = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar este dato?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (reply==JOptionPane.YES_OPTION){   
                    try {
                        PreparedStatement pps = cn.prepareStatement("DELETE FROM Facturas WHERE NumFactura='"+valor+"'");
                        PreparedStatement ppa = cn.prepareStatement("insert into eliminacion (Justificacion,Seccion,CampoElminado) values (?,?,?)");
                        pps.executeUpdate();
                        ppa.setString(1,razon);
                        ppa.setString(2,Seleccion);
                        ppa.setString(3,valor);
                        ppa.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Dato eliminado");
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (Seleccion=="Libros"){
            int fila=TablaDatos.getSelectedRow();
            String valor=TablaDatos.getValueAt(fila, 0).toString();
            if(fila>=0){
                String razon = JOptionPane.showInputDialog("Ingrese una razón para eliminar el campo");
                int reply = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar este dato?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (reply==JOptionPane.YES_OPTION){   
                    try {    
                        PreparedStatement pps = cn.prepareStatement("DELETE FROM libros WHERE referencia='"+valor+"'");
                        PreparedStatement ppa = cn.prepareStatement("insert into eliminacion (Justificacion,Seccion,CampoElminado) values (?,?,?)");
                        pps.executeUpdate();
                        ppa.setString(1,razon);
                        ppa.setString(2,Seleccion);
                        ppa.setString(3,valor);
                        ppa.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Dato eliminado");
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 }
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Este boton carga los datos de la tabladatos y los establece en los campos de texto dispuestos en cada módulo
        if (Seleccion=="Clientes"){
            int fila = TablaDatos.getSelectedRow();
            if(fila>=0){
                txtDI.setText(TablaDatos.getValueAt(fila,0).toString());
                txtdireccion.setText(TablaDatos.getValueAt(fila,1).toString());
                txtcorreo.setText(TablaDatos.getValueAt(fila,2).toString());
                txtnombre.setText(TablaDatos.getValueAt(fila,3).toString());
                txtnota.setText(TablaDatos.getValueAt(fila,4).toString());
                TextoTel.setText(TablaDatos.getValueAt(fila,5).toString());
                TextoCel.setText(TablaDatos.getValueAt(fila,6).toString());
            }else{
                JOptionPane.showMessageDialog(null,"fila no seleccionada");
            }
        }
        if (Seleccion=="Proveedores"){
            int fila = TablaDatos.getSelectedRow();
            if(fila>=0){
                txtDI1.setText(TablaDatos.getValueAt(fila,0).toString());
                txtdireccion1.setText(TablaDatos.getValueAt(fila,1).toString());
                txtcorreo1.setText(TablaDatos.getValueAt(fila,2).toString());
                txtnombre1.setText(TablaDatos.getValueAt(fila,3).toString());
                TextoTel1.setText(TablaDatos.getValueAt(fila,4).toString());
                TextoCel1.setText(TablaDatos.getValueAt(fila,5).toString());
            }else{
                JOptionPane.showMessageDialog(null,"fila no seleccionada");
            }
        }
         if (Seleccion=="Ventas/Compras"){
            int fila = TablaDatos.getSelectedRow();
            if(fila>=0){
                TxtNFac.setText(TablaDatos.getValueAt(fila,0).toString());
                TextID.setText(TablaDatos.getValueAt(fila,1).toString());
                TextLibro1.setText(TablaDatos.getValueAt(fila,2).toString());
                TextoReferencia2.setText(TablaDatos.getValueAt(fila,3).toString());
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
                String mydate = (TablaDatos.getValueAt(fila,6).toString());
                Date dato = null;
                try{
                    dato = formatoDeFecha.parse(mydate);
                }catch(ParseException ex){
                    ex.printStackTrace();
                }
                txtFFac.setDate(dato);
                Cantidad.setText(TablaDatos.getValueAt(fila,4).toString());
                TextoPrecio.setText(TablaDatos.getValueAt(fila,5).toString());
                String tipo = "";
                tipo = (TablaDatos.getValueAt(fila,7).toString());
                switch (tipo) {
                    case "Venta":
                        BVenta.setSelected(true);
                        break;
                    case "Compra":
                        BCompra.setSelected(true);
                        break;
                }
            }else{
                JOptionPane.showMessageDialog(null,"fila no seleccionada");
            }
        }
        if (Seleccion=="Libros"){
            int fila = TablaDatos.getSelectedRow();
            if(fila>=0){
                TextoReferenc.setText(TablaDatos.getValueAt(fila,0).toString());
                TextTitu.setText(TablaDatos.getValueAt(fila,1).toString());
                txtAutor.setText(TablaDatos.getValueAt(fila,2).toString());
                txtEditorial.setText(TablaDatos.getValueAt(fila,3).toString());
                txtGenero.setText(TablaDatos.getValueAt(fila,4).toString());
                TextSub.setText(TablaDatos.getValueAt(fila,5).toString());
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
                String mydate = (TablaDatos.getValueAt(fila,6).toString());
                Date dato = null;
                try{ 
                    dato = formatoDeFecha.parse(mydate);
                }catch(ParseException ex){
                   ex.printStackTrace();
                }
                txtFPublic.setDate(dato);
                TextoProve.setText(TablaDatos.getValueAt(fila,7).toString());
                TextoVenta.setText(TablaDatos.getValueAt(fila,9).toString());
                TextoCompra.setText(TablaDatos.getValueAt(fila,8).toString());
                Existencias.setText(TablaDatos.getValueAt(fila,10).toString());
            }else{
                JOptionPane.showMessageDialog(null,"fila no seleccionada");
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        limpiar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Consulta para verificar si el campo existe, si existe actualiza sino inserta
        String sqla = "SELECT Nombre FROM '"+Seleccion+"' where DI = '"+txtDI.getText()+"'";
        if (Seleccion=="Clientes"){
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sqla);
                if (rs != null && rs.next()){
                    try {
                        // Inserciones y actualizaciones en las diferentes tablas
                        PreparedStatement pps = cn.prepareStatement("UPDATE clientes SET DI='"+txtDI.getText()+
                            "',Direccion='"+txtdireccion.getText()+"',Email='"+txtcorreo.getText()+"',Nombre='"+txtnombre.getText()+"',Nota='"+txtnota.getText()+
                            "',TelefonoFijo='"+TextoTel.getText()+"',TelefonoMovil='"+TextoCel.getText()+
                            "' WHERE DI='"+txtDI.getText()+"'");
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"Datos actualizados");
                        limpiar();
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    try {
                        PreparedStatement pps = cn.prepareStatement("INSERT INTO clientes(DI,Direccion,Email,Nombre,Nota,TelefonoFijo,TelefonoMovil) VALUES(?,?,?,?,?,?,?)");
                        pps.setString(1,txtDI.getText());
                        pps.setString(2,txtdireccion.getText());
                        pps.setString(3,txtcorreo.getText());
                        pps.setString(4,txtnombre.getText());
                        pps.setString(5,txtnota.getText());
                        pps.setString(6,TextoTel.getText());
                        pps.setString(7,TextoCel.getText());
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"Datos guardados");
                        limpiar();
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Seleccion=="Ventas/Compras"){
            sqla = "SELECT NumFactura FROM facturas where NumFactura = '"+TxtNFac.getText()+"'";
            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
            String data="";            
            Date datos = txtFFac.getDate();
            data = String.valueOf(formatoDeFecha.format(datos));
            try{ 
                datos = formatoDeFecha.parse(data);
            }catch(ParseException ex){
                   ex.printStackTrace();
            }
            txtFFac.setDate(datos);
            java.util.Date utilStartDate = txtFFac.getDate();
            java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sqla);
                if (rs != null && rs.next()){
                    try {
                        String v = "";
                    if (BVenta.isSelected()==true){
                       v = BVenta.getText().toString();
                    }
                    else if (BCompra.isSelected()==true){
                        v = BCompra.getText().toString();
                    }
                        // Inserciones y actualizaciones en las diferentes tablas
                        PreparedStatement pps = cn.prepareStatement("UPDATE facturas SET ID='"+TextID.getText()+"',Titulo='"+TextLibro1.getText()+"',Referencia='"+TextoReferencia2.getText()+"',FechaFactura='"+sqlStartDate+
                           "',TipoFactura='"+v+"',Cantidad='"+Cantidad.getText()+"',Valor ='"+TextoPrecio.getText()+
                            "' WHERE NumFactura='"+TxtNFac.getText()+"'");
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"Datos actualizados");
                        restar();
                        limpiar();
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    String v = BVenta.getText().toString();
                    String c = BCompra.getText().toString();
                    try {
                        PreparedStatement pps = cn.prepareStatement("INSERT INTO facturas(ID,Titulo,Referencia,Valor,FechaFactura,Cantidad,TipoFactura) VALUES(?,?,?,?,?,?,?)");
                        pps.setString(1,TextID.getText());
                        pps.setString(2,TextLibro1.getText());
                        pps.setString(3,TextoReferencia2.getText());
                        pps.setString(4,TextoPrecio.getText());
                        pps.setDate(5,sqlStartDate);
                        pps.setString(6,Cantidad.getText());
                        if (BVenta.isSelected()==true){
                            pps.setString(7,v);
                        }
                        else if (BCompra.isSelected()==true){
                            pps.setString(7, c);
                        }
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"Datos guardados");
                        restar();
                        limpiar();
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Seleccion=="Proveedores"){
            sqla = "SELECT RazonSocial FROM proveedor where nit = '"+txtDI1.getText()+"'";
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sqla);
                if (rs != null && rs.next()){
                    try {
                        PreparedStatement pps = cn.prepareStatement("UPDATE proveedor SET Nit='"+txtDI1.getText()+
                            "',Direccion='"+txtdireccion1.getText()+"',Email='"+txtcorreo1.getText()+"',RazonSocial='"+txtnombre1.getText()+
                            "',TelefonoFijo='"+TextoTel1.getText()+"',TelefonoMovil='"+TextoCel1.getText()+
                            "' WHERE nit='"+txtDI1.getText()+"'");
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"Datos actualizados");
                        limpiar();
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    try {
                        PreparedStatement pps = cn.prepareStatement("INSERT INTO proveedor(Nit,Direccion,Email,RazonSocial,TelefonoFijo,TelefonoMovil) VALUES(?,?,?,?,?,?)");
                        pps.setString(1,txtDI1.getText());
                        pps.setString(2,txtdireccion1.getText());
                        pps.setString(3,txtcorreo1.getText());
                        pps.setString(4,txtnombre1.getText());
                        pps.setString(5,TextoTel1.getText());
                        pps.setString(6,TextoCel1.getText());
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"Datos guardados");
                        limpiar();
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Seleccion=="Libros"){
            sqla = "SELECT Titulo FROM Libros where Referencia = '"+TextoReferenc.getText()+"'";
            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
            String data="";            
            Date datos = txtFPublic.getDate();
            data = String.valueOf(formatoDeFecha.format(datos));
            try{ 
                datos = formatoDeFecha.parse(data);
            }catch(ParseException ex){
               ex.printStackTrace();
            }
            txtFPublic.setDate(datos);
            java.util.Date utilStartDate = txtFPublic.getDate();
            java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
            try {
                Statement st=cn.createStatement();
                ResultSet rs =st.executeQuery(sqla);
                if (rs != null && rs.next()){                       
                    try {
                        PreparedStatement pps = cn.prepareStatement("UPDATE libros SET Referencia='"+TextoReferenc.getText()+
                            "',Titulo='"+TextTitu.getText()+"',Autor='"+txtAutor.getText()+"',Editorial='"+txtEditorial.getText()+
                            "',Genero='"+txtGenero.getText()+"',Subgenero='"+TextSub.getText()+"',FechaPublicacion='"+sqlStartDate+
                            "',Proveedor='"+TextoProve.getText()+"',ValorVenta='"+TextoVenta.getText()+"',ValorCompra='"+TextoCompra.getText()+"',Existencias='"+Existencias.getText()+
                            "' WHERE referencia='"+TextoReferenc.getText()+"'");
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"Datos actualizados");
                        limpiar();
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    data = String.valueOf(formatoDeFecha.format(datos));
                    try{ 
                        datos = formatoDeFecha.parse(data);
                    }catch(ParseException ex){
                       ex.printStackTrace();
                    }  
                    txtFPublic.setDate(datos);
                    try {
                        PreparedStatement pps = cn.prepareStatement("INSERT INTO libros(Referencia,Titulo,Autor,Editorial,Genero,Subgenero,FechaPublicacion,Proveedor,ValorCompra,ValorVenta,Existencias) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                        pps.setString(1,TextoReferenc.getText());
                        pps.setString(2,TextTitu.getText());
                        pps.setString(3,txtAutor.getText());
                        pps.setString(4,txtEditorial.getText());
                        pps.setString(5,txtGenero.getText());
                        pps.setString(6,TextSub.getText());
                        pps.setDate(7,sqlStartDate);
                        pps.setString(8,TextoProve.getText());
                        pps.setString(9,TextoCompra.getText());
                        pps.setString(10,TextoVenta.getText());
                        pps.setString(11,Existencias.getText());
                        pps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"Datos guardados");
                        limpiar();
                        mostrartabla();
                    }catch (SQLException ex) {
                        Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }catch (SQLException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void TextTituloKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextTituloKeyReleased
        consultar();
    }//GEN-LAST:event_TextTituloKeyReleased

    private void TextTituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextTituloActionPerformed

    }//GEN-LAST:event_TextTituloActionPerformed

    private void TextIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextIDKeyReleased
        consultar();
    }//GEN-LAST:event_TextIDKeyReleased

    private void jButton1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyReleased
        restar();        
    }//GEN-LAST:event_jButton1KeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
        String data="";            
        Date datos = FechaInicio.getDate();
        data = String.valueOf(formatoDeFecha.format(datos));
        try{ 
            datos = formatoDeFecha.parse(data);
        }catch(ParseException ex){
           ex.printStackTrace();
        }
        java.util.Date utilStartDate = FechaInicio.getDate();
        java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
        String datas="";            
        Date datoss = FechaFin.getDate();
        datas = String.valueOf(formatoDeFecha.format(datoss));
         try{ 
            datoss = formatoDeFecha.parse(datas);
        }catch(ParseException ex){
           ex.printStackTrace();
        }
        java.util.Date utilEndDate = FechaFin.getDate();
        java.sql.Date sqlEndDate = new java.sql.Date(utilEndDate.getTime());
        DefaultTableModel modelo =new DefaultTableModel();
        modelo.addColumn("Número de factura");
        modelo.addColumn("Número de identificación");
        modelo.addColumn("Titulo del libro");
        modelo.addColumn("Referencia");
        modelo.addColumn("Valor");
        modelo.addColumn("Fecha de facturación");
        modelo.addColumn("Tipo de factura");
        Reportes.setModel(modelo);
        String sql = "Select * from facturas where TipoFactura = 'Venta' and FechaFactura between '"+sqlStartDate+"' and '"+sqlEndDate+"'";
        String sqlb = "Select sum(valor) from facturas where TipoFactura = 'Venta' and FechaFactura between '"+sqlStartDate+"' and '"+sqlEndDate+"'";
        String sqlc = "Select count(titulo) from facturas where TipoFactura = 'Venta' and FechaFactura between'"+sqlStartDate+"' and '"+sqlEndDate+"'";
        long fechaInicial = sqlStartDate.getTime(); 
        long fechaFinal = sqlEndDate.getTime(); 
        long diferencia = fechaFinal - fechaInicial; 
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24)); 
        String datos2[] = new String [7];        
        try {
            Statement st=cn.createStatement();
            ResultSet rs =st.executeQuery(sql);
            while(rs.next()){
                datos2[0]=rs.getString(1);
                datos2[1]=rs.getString(2);
                datos2[2]=rs.getString(3);
                datos2[3]=rs.getString(4);
                datos2[4]=rs.getString(5);
                datos2[5]=rs.getString(6);
                datos2[6]=rs.getString(7);
                modelo.addRow(datos2);
            }
        Reportes.setModel(modelo);
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }      
        try {
            Statement st=cn.createStatement();
            ResultSet rs =st.executeQuery(sqlb);
            String promedio[] = new String [1];
            while(rs.next()){
                promedio[0] = rs.getString(1);
            }
            String dataf = Arrays.toString(promedio);
            String datad=dataf.replace("[", "");
            String datag=datad.replace("]", "");
            LTotal.setText(datag);
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Statement st=cn.createStatement();
            ResultSet rs =st.executeQuery(sqlb);
            String promedio[] = new String [1];
            while(rs.next()){
                promedio[0] = rs.getString(1);
            }
            String dataf = Arrays.toString(promedio);
            String datad=dataf.replace("[", "");
            String datag=datad.replace("]", "");
            int dataj = Integer.parseInt(datag);
            double m = dataj/dias;
            String promedios;
            promedios = String.valueOf(m);
            LPromedio.setText(promedios);
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Statement st=cn.createStatement();
            ResultSet rs =st.executeQuery(sqlc);
            String promedio[] = new String [1];
            while(rs.next()){
                promedio[0] = rs.getString(1);
            }
            String dataf = Arrays.toString(promedio);
            String datad=dataf.replace("[", "");
            String datag=datad.replace("]", "");
            Librostotales.setText(datag);
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Statement st=cn.createStatement();
            ResultSet rs =st.executeQuery(sqlc);
            String promedio[] = new String [1];
            while(rs.next()){
                promedio[0] = rs.getString(1);
            }
            String dataf = Arrays.toString(promedio);
            String datad=dataf.replace("[", "");
            String datag=datad.replace("]", "");
            int dataj = Integer.parseInt(datag);
            double m = dataj/dias;
            String promedios;
            promedios = String.valueOf(m);
            Librosavg.setText(promedios);
        }catch (SQLException ex) {
            Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
    }//GEN-LAST:event_jButton4ActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton BCompra;
    private javax.swing.JRadioButton BVenta;
    private javax.swing.JTextField Cantidad;
    private javax.swing.JTextField Existencias;
    private com.toedter.calendar.JDateChooser FechaFin;
    private com.toedter.calendar.JDateChooser FechaInicio;
    private javax.swing.JLabel FechaPublicacion;
    private javax.swing.JLabel FechaPublicacion3;
    private javax.swing.JLabel Filtros;
    private javax.swing.JLabel LPromedio;
    private javax.swing.JLabel LTotal;
    private javax.swing.JLabel LabelCel;
    private javax.swing.JLabel LabelCel1;
    private javax.swing.JLabel LabelCel2;
    private javax.swing.JLabel LabelCel3;
    private javax.swing.JLabel LabelTel;
    private javax.swing.JLabel LabelTel1;
    private javax.swing.JLabel LabelTel2;
    private javax.swing.JLabel Librosavg;
    private javax.swing.JLabel Librostotales;
    private javax.swing.JPanel PanelFacturas2;
    private javax.swing.JPanel PanelLibros;
    private javax.swing.JPanel PanelProveedores;
    private javax.swing.JLabel Parrafo1;
    private javax.swing.JLabel Parrafo2;
    private javax.swing.JTable Reportes;
    private javax.swing.JComboBox SeleccionModulo;
    private javax.swing.JButton Seleccionar;
    private javax.swing.ButtonGroup TFact;
    private javax.swing.JTable TablaDatos;
    private javax.swing.JLabel TextCC;
    private javax.swing.JTextField TextID;
    private javax.swing.JLabel TextLibro1;
    private javax.swing.JTextField TextSub;
    private javax.swing.JTextField TextTitu;
    private javax.swing.JTextField TextTitulo;
    private javax.swing.JTextField TextoCel;
    private javax.swing.JTextField TextoCel1;
    private javax.swing.JTextField TextoCompra;
    private javax.swing.JLabel TextoDI;
    private javax.swing.JLabel TextoPrecio;
    private javax.swing.JTextField TextoProve;
    private javax.swing.JTextField TextoReferenc;
    private javax.swing.JLabel TextoReferencia2;
    private javax.swing.JTextField TextoTel;
    private javax.swing.JTextField TextoTel1;
    private javax.swing.JTextField TextoVenta;
    private javax.swing.JLabel TituloInfo;
    private javax.swing.JLabel TxtNFac;
    private javax.swing.JTextField bdname;
    private javax.swing.JButton conectar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel labelcanti;
    private javax.swing.JTextField txtAutor;
    private javax.swing.JTextField txtDI;
    private javax.swing.JTextField txtDI1;
    private javax.swing.JTextField txtEditorial;
    private com.toedter.calendar.JDateChooser txtFFac;
    private com.toedter.calendar.JDateChooser txtFPublic;
    private javax.swing.JTextField txtGenero;
    private javax.swing.JTextField txtbuscar3;
    private javax.swing.JTextField txtcorreo;
    private javax.swing.JTextField txtcorreo1;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtdireccion1;
    private javax.swing.JTextField txtdirsql;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtnombre1;
    private javax.swing.JTextArea txtnota;
    private javax.swing.JTextField txtnsql;
    private javax.swing.JTextField txtpasssql;
    private javax.swing.JTextField txtportsql;
    private javax.swing.JTextField txtusersql;
    // End of variables declaration//GEN-END:variables
}