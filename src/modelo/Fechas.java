package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Fechas {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

    private Fechas() {
    }

    public static String aTexto(LocalDate fecha) {
        return fecha.format(FORMATO);
    }

    public static String aTexto(String fechaBase) {
        return aTexto(LocalDate.parse(fechaBase));
    }

    public static LocalDate leer(String texto) throws DateTimeParseException {
        return LocalDate.parse(texto.replace('/', '-'), FORMATO);
    }

    public static String aBase(String texto) throws DateTimeParseException {
        return leer(texto).toString();
    }
}
