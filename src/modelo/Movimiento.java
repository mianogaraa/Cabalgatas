package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class Movimiento {

    private int id;
    private Caballo caballo;
    private String tipoRecorrido;
    private String fecha;
    private String horaInicio;
    private String cliente;
    private double duracion;
    private double precio;

    public Movimiento(Caballo caballo, String tipoRecorrido, String fecha, String horaInicio, String cliente, double duracion, double precio) {
        this.caballo = caballo;
        this.tipoRecorrido = tipoRecorrido;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.cliente = cliente;
        this.duracion = duracion;
        this.precio = precio;
    }

    public abstract double calcularPrecioFinal();

    public LocalDateTime getInicio() {
        return LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(horaInicio));
    }

    public LocalDateTime getFin() {
        return getInicio().plusMinutes(Math.round(duracion * 60));
    }

    public boolean seSuperponeCon(Movimiento otro) {
        return getInicio().isBefore(otro.getFin()) && otro.getInicio().isBefore(getFin());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Caballo getCaballo() { return caballo; }
    public void setCaballo(Caballo caballo) { this.caballo = caballo; }
    public String getTipoRecorrido() { return tipoRecorrido; }
    public void setTipoRecorrido(String tipoRecorrido) { this.tipoRecorrido = tipoRecorrido; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public double getDuracion() { return duracion; }
    public void setDuracion(double duracion) { this.duracion = duracion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}