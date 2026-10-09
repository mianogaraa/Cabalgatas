package modelo;

public class MovimientoDiurno extends Movimiento {

    public MovimientoDiurno(Caballo caballo, String fecha, String horaInicio, String cliente, double duracion, double precio) {
        super(caballo, "Diurno", fecha, horaInicio, cliente, duracion, precio);
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecio() * getDuracion();
    }
}