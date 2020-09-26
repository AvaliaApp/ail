package app.avalia.compiler.error;

import java.util.HashMap;
import java.util.Map;

public class AILErrorLogger {

    public static void logError(int line, String error) {
        System.out.println(line + ":" + error);
    }

}
