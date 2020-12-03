package pe.edu.upeu.modelo;

public class VentaDetalle{
  public String idVtnDet;
  public String idVenta;
  public String iDProducto;
  public double cantidad;
  public double precioUnit;
  public double precioTotal;

    public String getIdVtnDet() {
        return idVtnDet;
    }

    public void setIdVtnDet(String idVtnDet) {
        this.idVtnDet = idVtnDet;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getiDProducto() {
        return iDProducto;
    }

    public void setiDProducto(String iDProducto) {
        this.iDProducto = iDProducto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(double precioUnit) {
        this.precioUnit = precioUnit;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }



}