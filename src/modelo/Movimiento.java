package modelo; // paquete (carpeta) donde esta esta clase: el modelo

import java.time.LocalDate; // fecha sin hora
import java.time.LocalDateTime; // fecha y hora juntas
import java.time.LocalTime; // hora sin fecha

public abstract class Movimiento { // la cabalgata; abstract = no se puede hacer new Movimiento(): siempre es Diurno o Nocturno (sus hijas)

    private int id; // numero unico de la cabalgata; lo asigna la base
    private Caballo caballo; // el caballo que hace la cabalgata (un objeto Caballo completo, no solo su id)
    private String tipoRecorrido; // "Diurno" o "Nocturno"
    private String fecha; // fecha como texto AAAA-MM-DD (el formato de la base)
    private String horaInicio; // hora de inicio como texto, por ejemplo 09:30
    private String cliente; // nombre del cliente o del grupo
    private double duracion; // duracion en horas (con decimales)
    private double precio; // precio por hora

    public Movimiento(Caballo caballo, String tipoRecorrido, String fecha, String horaInicio, String cliente, double duracion, double precio) { // constructor: lo llaman las hijas con super(...)
        this.caballo = caballo; // guarda el caballo recibido
        this.tipoRecorrido = tipoRecorrido; // guarda el tipo
        this.fecha = fecha; // guarda la fecha
        this.horaInicio = horaInicio; // guarda la hora de inicio
        this.cliente = cliente; // guarda el cliente
        this.duracion = duracion; // guarda la duracion
        this.precio = precio; // guarda el precio por hora
    } 

    public abstract double calcularPrecioFinal(); // metodo abstracto: no tiene cuerpo; cada hija esta obligada a escribirlo a su manera (polimorfismo)

    public LocalDateTime getInicio() { // devuelve el momento exacto en que empieza la cabalgata
        return LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(horaInicio)); // convierte el texto de la fecha y el de la hora a objetos y los junta en uno
    }

    public LocalDateTime getFin() { // devuelve el momento en que termina la cabalgata
        return getInicio().plusMinutes(Math.round(duracion * 60)); // al inicio le suma la duracion pasada a minutos (horas por 60), redondeada
    } 

    public boolean seSuperponeCon(Movimiento otro) { // dice si esta cabalgata se pisa en el tiempo con otra
        return getInicio().isBefore(otro.getFin()) && otro.getInicio().isBefore(getFin()); // se pisan si esta empieza antes de que la otra termine Y la otra empieza antes de que esta termine; si una termina justo cuando empieza la otra, no se pisan
    } 

    public int getId() { return id; } // getter: devuelve el id (los atributos son private, se leen con getters)
    public void setId(int id) { this.id = id; } // setter: cambia el id
    public Caballo getCaballo() { return caballo; } // getter: devuelve el caballo
    public void setCaballo(Caballo caballo) { this.caballo = caballo; } // setter: cambia el caballo
    public String getTipoRecorrido() { return tipoRecorrido; } // getter: devuelve el tipo de recorrido
    public void setTipoRecorrido(String tipoRecorrido) { this.tipoRecorrido = tipoRecorrido; } // setter: cambia el tipo de recorrido
    public String getFecha() { return fecha; } // getter: devuelve la fecha (formato AAAA-MM-DD)
    public void setFecha(String fecha) { this.fecha = fecha; } // setter: cambia la fecha
    public String getHoraInicio() { return horaInicio; } // getter: devuelve la hora de inicio
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; } // setter: cambia la hora de inicio
    public String getCliente() { return cliente; } // getter: devuelve el cliente
    public void setCliente(String cliente) { this.cliente = cliente; } // setter: cambia el cliente
    public double getDuracion() { return duracion; } // getter: devuelve la duracion en horas
    public void setDuracion(double duracion) { this.duracion = duracion; } // setter: cambia la duracion
    public double getPrecio() { return precio; } // getter: devuelve el precio por hora
    public void setPrecio(double precio) { this.precio = precio; } // setter: cambia el precio por hora
} 