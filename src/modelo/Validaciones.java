package modelo;

import java.sql.SQLException;
import java.time.LocalDate; 
import java.time.LocalDateTime; 
import java.time.LocalTime; 
import java.time.format.DateTimeParseException; 

public class Validaciones { 

    private static final LocalTime INICIO_DIA = LocalTime.of(8, 0); // hora en que empieza el dia de trabajo: 08:00; constante (static final)
    private static final LocalTime INICIO_NOCHE = LocalTime.of(19, 0); // hora en que empieza la noche: 19:00; tambien es el cierre del horario diurno

    private Validaciones() { // constructor privado: nadie puede hacer new Validaciones(), porque solo tiene metodos static
    } 

    public static String validarHorario(Movimiento m) { // controla que el horario corresponda al tipo; devuelve el mensaje de error, o null si esta bien
        LocalDate dia = m.getInicio().toLocalDate(); // saca solo la fecha del inicio de la cabalgata
        LocalTime horaInicio = m.getInicio().toLocalTime(); // saca solo la hora del inicio

        if ("Nocturno".equals(m.getTipoRecorrido())) { // si el tipo es Nocturno (se escribe el literal adelante para evitar errores si fuera null)
            LocalDateTime medianoche = dia.plusDays(1).atStartOfDay(); // medianoche: el dia siguiente a las 00:00, donde termina la noche
            if (horaInicio.isBefore(INICIO_NOCHE) || m.getFin().isAfter(medianoche)) { // error si empieza antes de las 19:00 O termina despues de medianoche (|| significa "o")
                return "Las cabalgatas nocturnas son de 19:00 a 00:00: tienen que empezar desde las 19:00 y terminar como maximo a medianoche. Para otro horario elegi el tipo Diurno."; // devuelve el mensaje de error
            } 
            return null;
        } 

        LocalDateTime cierreDia = dia.atTime(INICIO_NOCHE); // si llego aca es Diurno: el cierre del dia es ese dia a las 19:00
        if (horaInicio.isBefore(INICIO_DIA) || m.getFin().isAfter(cierreDia)) { // error si empieza antes de las 08:00 O termina despues de las 19:00
            return "Las cabalgatas diurnas son de 08:00 a 19:00: tienen que empezar desde las 08:00 y terminar como maximo a las 19:00. Para otro horario elegi el tipo Nocturno."; // devuelve el mensaje de error
        } 
        return null; 
    } 

    public static String buscarSuperposicion(Movimiento nuevo, MovimientoDAO dao) throws SQLException { // busca si el caballo ya tiene otra cabalgata que se pise; devuelve error o null
        for (Movimiento otro : dao.listarPorCaballo(nuevo.getCaballo().getId())) { // recorre las cabalgatas que ya tiene ese caballo en la base
            if (otro.getId() == nuevo.getId()) { // si es la misma cabalgata (pasa al modificar: ya esta en la base con su id)...
                continue; // ...salta a la siguiente vuelta: no se compara consigo misma
            } // fin del if
            if (nuevo.seSuperponeCon(otro)) { // si la nueva se pisa en el tiempo con esa otra...
                return nuevo.getCaballo().getNombre() + " ya tiene una cabalgata el " + Fechas.aTexto(otro.getFecha()) // ...arma el mensaje: nombre del caballo y la fecha de la otra (convertida a DD-MM-AAAA)...
                        + " de " + otro.getInicio().toLocalTime() + " a " + otro.getFin().toLocalTime() + "."; // ...y el horario de inicio y fin de la que se pisa
            } 
        } 
        return null; 
    } 

    public static String validarDatos(String fecha, String hora, String duracion, String precio) { // controla el formato de los datos escritos; todo llega como texto; devuelve el primer error o null
        try { // intenta convertir la fecha, que puede estar mal escrita
            Fechas.leer(fecha); // convierte el texto en fecha; no guarda el resultado, solo importa si falla
        } catch (DateTimeParseException ex) { // si el texto no es una fecha valida
            return "La fecha debe tener el formato DD-MM-AAAA (por ejemplo 25-10-2026)"; // devuelve el error con un ejemplo
        } 
        try { // intenta convertir la hora
            LocalTime.parse(hora); // convierte el texto en hora (formato HH:mm); si esta mal, tira el error
        } catch (DateTimeParseException ex) { // si el texto no es una hora valida
            return "La hora debe tener el formato HH:mm (por ejemplo 09:30)"; // devuelve el error con un ejemplo
        } 
        Double horas = leerNumero(duracion); // convierte la duracion a numero; Double (con mayuscula) puede valer null si no se pudo leer
        if (horas == null || horas <= 0) { // error si no es un numero O si es menor o igual a 0
            return "La duracion debe ser un numero mayor a 0"; // devuelve el error
        } 
        Double importe = leerNumero(precio); // convierte el precio a numero (o null)
        if (importe == null || importe <= 0) { // error si no es un numero O si es menor o igual a 0
            return "El precio debe ser un numero mayor a 0"; // devuelve el error
        } 
        return null; 
    } 

    public static Double leerNumero(String texto) { // convierte un texto en numero con decimales; devuelve null si no se puede
        try { 
            return Double.parseDouble(texto.replace(',', '.')); // cambia la coma por punto (para aceptar 2,5) y convierte el texto a numero
        } catch (NumberFormatException ex) { // si el texto no era un numero
            return null; // devuelve null: significa "no se pudo leer"
        } 
    } 
} 