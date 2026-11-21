package modelo; // paquete (carpeta) donde esta esta clase: el modelo

import java.time.LocalDate; // fecha sin hora (dia, mes, anio)
import java.time.format.DateTimeFormatter; // molde que define como se escribe una fecha
import java.time.format.DateTimeParseException; // error cuando un texto no se puede convertir en fecha
import java.time.format.ResolverStyle; // define que tan estricto es Java al validar una fecha

public class Fechas { // clase de herramientas: traduce fechas entre el formato de pantalla (DD-MM-AAAA) y el de la base (AAAA-MM-DD)

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT); // molde de la fecha: dd = dia, MM = mes, uuuu = anio de 4 cifras; STRICT rechaza fechas imposibles como 31-02-2026; static final = constante unica

    private Fechas() { // constructor privado: nadie puede hacer new Fechas(), porque no hace falta
    } // fin del constructor

    public static String aTexto(LocalDate fecha) { // de fecha a texto para mostrar en pantalla; static = se usa como Fechas.aTexto(...)
        return fecha.format(FORMATO); // escribe la fecha con el molde: "25-10-2026"
    } // fin de aTexto (version LocalDate)

    public static String aTexto(String fechaBase) { // mismo nombre pero recibe un String: es una sobrecarga (mismo nombre, distinto parametro)
        return aTexto(LocalDate.parse(fechaBase)); // convierte el texto de la base ("2026-10-25") en fecha y llama al metodo de arriba
    } // fin de aTexto (version String)

    public static LocalDate leer(String texto) throws DateTimeParseException { // pasa lo que escribio el usuario a fecha; throws avisa que puede fallar
        return LocalDate.parse(texto.replace('/', '-'), FORMATO); // cambia / por - para aceptar las dos formas y convierte el texto con el molde; si esta mal, tira el error
    } // fin de leer

    public static String aBase(String texto) throws DateTimeParseException { // pasa lo que escribio el usuario al formato de la base
        return leer(texto).toString(); // lo convierte en fecha y toString() la escribe como AAAA-MM-DD, el formato que SQL ordena bien
    } // fin de aBase
} // fin de la clase
