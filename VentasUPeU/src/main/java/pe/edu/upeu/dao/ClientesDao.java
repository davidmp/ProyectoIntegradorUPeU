package pe.edu.upeu.dao;

import pe.edu.upeu.data.CrudFileRepository;
import pe.edu.upeu.modelo.Clientes;
import pe.edu.upeu.utils.LeerArchivo;
import pe.edu.upeu.utils.LeerTeclado;
import pe.edu.upeu.utils.UtilsX;

public class ClientesDao extends CrudFileRepository{
LeerTeclado leerTecla=new LeerTeclado();
UtilsX util=new UtilsX();
LeerArchivo leerArc;
Clientes clientes;

public Object[][] crearCliente(){
    leerArc=new LeerArchivo("Clientes.txt");
    clientes=new Clientes();
    clientes.setDni(leerTecla.leer("","Ingrese el DNI del cliente:"));
    clientes.setNombreCliente(leerTecla.leer("", "Ingrese el nombre del Cliente:"));
    return agregarContenido(leerArc, clientes);
}

public void reportarClientes(){
    leerArc=new LeerArchivo("Clientes.txt");
    System.out.println("******************Reporte general de Clientes***********");
    imprimirLista(listarContenido(leerArc));
}

}