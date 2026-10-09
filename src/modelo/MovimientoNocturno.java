package modelo;

public class MovimientoNocturno extends Movimiento {

    private double recargo = 20;

    public MovimientoNocturno(Caballo caballo, String fecha, String horaInicio, String cliente, double duracion, double precio) {
        super(caballo, "Nocturno", fecha, horaInicio, cliente, duracion, precio);
    }

    public double getRecargo() {
        return recargo;
    }

    public void setRecargo(double recargo) {
        this.recargo = recargo;
    }

    @Override
    public double calcularPrecioFinal() {
        double base = getPrecio() * getDuracion();
        return base + base * recargo / 100;
    }
}