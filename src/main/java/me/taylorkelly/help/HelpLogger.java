package me.taylorkelly.help;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HelpLogger {

    public static final Logger log = Logger.getLogger("Minecraft");

    public static void severe(String string, Exception ex) {
        log.log(Level.SEVERE, "[HELP] " + string, ex);

    }

    public static void severe(String string) {
        log.log(Level.SEVERE, "[HELP] " + string);
    }

    static void info(String string) {
        log.log(Level.INFO, "[HELP] " + string);
    }

    static void warning(String string) {
        log.log(Level.WARNING, "[HELP] " + string);
    }
}
