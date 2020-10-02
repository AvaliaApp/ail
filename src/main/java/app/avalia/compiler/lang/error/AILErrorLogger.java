package app.avalia.compiler.lang.error;

/**
 * Default error logger
 * Format: "line:error"
 */
public class AILErrorLogger {

    public static void logError(int line, String error) {
        System.out.println(line + ":" + error);
    }

}
