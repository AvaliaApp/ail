package app.avalia.compiler.lang.error;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class AILErrorLoggerBase extends BaseErrorListener {
    public static final AILErrorLoggerBase INSTANCE = new AILErrorLoggerBase();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg, RecognitionException e) {
        System.out.println(line + ":" + msg);
    }
}
