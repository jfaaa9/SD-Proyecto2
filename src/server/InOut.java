package src.server;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class InOut {
    public static String horaActual() {
        LocalTime now = LocalTime.now();
        String horaComoTexto = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        return horaComoTexto;
    }
}

