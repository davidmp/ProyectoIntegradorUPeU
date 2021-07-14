package pe.edu.upeu.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import pe.edu.upeu.data.CrudFileRepository;
import pe.edu.upeu.modelo.Productos;
import pe.edu.upeu.modelo.VentaDetalle;
import pe.edu.upeu.modelo.Ventas;
import pe.edu.upeu.utils.LeerArchivo;
import pe.edu.upeu.utils.LeerTeclado;
import pe.edu.upeu.utils.UtilsX;

public class VentasDao extends CrudFileRepository{
    LeerTeclado leerTecla=new LeerTeclado();
    UtilsX util=new UtilsX();
    LeerArchivo leerArc; 
    Ventas ventasTO;
    VentaDetalle ventaDetalle;

    SimpleDateFormat formato=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    SimpleDateFormat formatoFecha=new SimpleDateFormat("dd-MM-yyyy");

    public void reporteVentasFecha(String fInicial, String fFinal){
        leerArc=new LeerArchivo("Ventas.txt");
        Object[][] data=listarContenido(leerArc);
        int cantidadVentas=0;
        double montoRecaudado=0;

        try {
            for(int fila=0; fila<data.length; fila++){
                String[] vectorFecha=data[fila][2].toString().split(" ");
                Date fechaTemp=formatoFecha.parse(vectorFecha[0].toString());
                if((fechaTemp.after(formatoFecha.parse(fInicial)) &&  
                fechaTemp.before(formatoFecha.parse(fFinal))) || vectorFecha[0].equals(fInicial) || vectorFecha[0].equals(fFinal) ){
                    cantidadVentas++;
                }
            }

            Object[][] dataDiaFecha=new Object[cantidadVentas][data[0].length]; 
            int filaX=0, columnaX=0;
            for(int fila=0;fila<data.length;fila++){
                 String[] vectorFecha=data[fila][2].toString().split(" ");
                 Date fechaTemp=formatoFecha.parse(vectorFecha[0].toString());
                if((fechaTemp.after(formatoFecha.parse(fInicial)) &&  
                fechaTemp.before(formatoFecha.parse(fFinal))) || vectorFecha[0].equals(fInicial) || vectorFecha[0].equals(fFinal)){
                    for(int columna=0; columna<data[0].length;columna++){
                        dataDiaFecha[filaX][columnaX]=data[fila][columna];
                        if(columna==3){ montoRecaudado+=Double.parseDouble(data[fila][columna].toString()); }
                        columnaX++;
                    }
                    filaX++;
                    columnaX=0;
                }                 
            }
            System.out.println("IDVenta\t Cliente\t FechaVenta \t Monto");
            imprimirLista(dataDiaFecha);
            System.out.println("***********************Resumen Venta********************");
            System.out.println("Monto Recaudado: "+montoRecaudado+ " CantidadVentas:"+cantidadVentas);

        } catch (Exception e) {
            System.out.println("Error en Reporte"+e.getMessage());
        }


    }

    public void registrarVentas(){
        Ventas ventaX=crearVenta();
        String continuarVenta="SI";
        do{
            util.clearConsole();
            listarProductosDisponibles();
            crearCarritoVenta(ventaX);
            continuarVenta=leerTecla.leer("S", "Desea agregar mas Productos? SI/NO");
        }while(continuarVenta.toUpperCase().equals("SI") || continuarVenta.toUpperCase().equals("S"));

        actualizarMontoVenta(extraerMontoGeneralVenta(ventaX), ventaX);
      
    }

    public double extraerMontoGeneralVenta(Ventas ventaX){
         leerArc=new LeerArchivo("VentasDetalle.txt");
         Object[][] data=buscarContenido(leerArc, 1, ventaX.getIdVenta());
         double monto=0;
         for(int i=0;i<data.length;i++){
            monto=monto+Double.parseDouble(data[i][5].toString());
         }
        return monto;
    }

    public Object[][] actualizarMontoVenta(double monto, Ventas ventaX){
        leerArc=new LeerArchivo("Ventas.txt");
        ventaX.setMontoVenta(monto);
        return editarRegistro(leerArc, 0, ventaX.getIdVenta(), ventaX);       
    }


    public Ventas crearVenta(){
        leerArc=new LeerArchivo("Ventas.txt");
        ventasTO=new Ventas();
        ventasTO.setIdVenta(generarId(leerArc, 0, "V", 1));
        ventasTO.setDniCliente(leerTecla.leer("0", "Ingrese el DNI del cliente:"));
        ventasTO.setMontoVenta(0);
        ventasTO.setFechaVenta(formato.format(new Date()));
        agregarContenido(leerArc, ventasTO);
        return ventasTO;
    }

    public void listarProductosDisponibles(){
       leerArc=new LeerArchivo("Productos.txt"); 
    System.out.println("******************Productos Disponibles***********");
    Object[][] data=listarContenido(leerArc);
    for (int i=0; i<data.length; i++) {
        System.out.print(data[i][0]+"="+data[i][1]+"(stock:"+data[i][4]+" Precio: "+data[i][3]+" ) | " );
        }
    }

    public Object[][] listarProductosDisponiblesX(){
        leerArc=new LeerArchivo("Productos.txt"); 
     System.out.println("******************Productos Disponibles***********");
     Object[][] data=listarContenido(leerArc);
     for (int i=0; i<data.length; i++) {
         System.out.print(data[i][0]+"="+data[i][1]+"(stock:"+data[i][4]+" Precio: "+data[i][3]+" ) | " );
         }
         return data;
     }


    public Object[][] crearCarritoVenta(Ventas venta){            
      leerArc=new LeerArchivo("VentasDetalle.txt");  
      ventaDetalle=new VentaDetalle();
      ventaDetalle.setIdVtnDet(generarId(leerArc, 0, "VD", 2));
      ventaDetalle.setIdVenta(venta.getIdVenta());
      String productoId=leerTecla.leer("", "Ingrese el Id del Producto:");
      ventaDetalle.setiDProducto(productoId);
      ventaDetalle.setCantidad(leerTecla.leer(0, "Ingrese la cantidad:"));
      Productos prodTemp=buscarProducto(productoId);
      ventaDetalle.setPrecioUnit(prodTemp.getPrecio());
      ventaDetalle.setPrecioTotal(ventaDetalle.getCantidad()*prodTemp.getPrecio());
      leerArc=new LeerArchivo("VentasDetalle.txt");
      
      return agregarContenido(leerArc, ventaDetalle);
    }


    public Productos buscarProducto(String idProducto){
        leerArc=new LeerArchivo("Productos.txt");  
       Object[][] data= buscarContenido(leerArc, 0, idProducto);
       Productos productos=new Productos();
       if(data!=null){
        productos.setStock(Double.parseDouble(data[0][4].toString()));
        productos.setPrecio(Double.parseDouble(data[0][3].toString()));
       }
       return productos;
    }

    
}