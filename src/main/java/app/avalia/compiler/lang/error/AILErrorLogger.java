package app.avalia.compiler.lang.error;

public class AILErrorLogger {

    public static void logError(int line, String error) {
        System.out.println(line + ":" + error);
    }

}
