package app.avalia.compiler;

import app.avalia.antlr.AILLexer;
import app.avalia.antlr.AILParser;
import app.avalia.compiler.lang.error.AILErrorLoggerBase;
import app.avalia.compiler.bytecode.jar.JarPackager;
import app.avalia.compiler.lang.AILClass;
import app.avalia.compiler.pool.BaseFunctions;
import app.avalia.compiler.pool.BaseInstructions;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AILMain {

    public static void main0(String[] args) throws IOException {
        InputStream stream = AILCompiler.class.getResourceAsStream("/test.ail");

        AILLexer lexer = new AILLexer(CharStreams.fromStream(stream));
        AILParser parser = new AILParser(new CommonTokenStream(lexer));

        AILParser.ParseContext context = parser.parse();
        TreeViewer viewer = new TreeViewer(Arrays.asList(
                parser.getRuleNames()), context);
        viewer.open();
    }

    public static void main(String[] args) throws IOException {
        BaseInstructions.load();
        BaseFunctions.load();

        if (args.length == 0) {
            System.out.println("Invalid path to AIL class");
            System.exit(-1);
            return;
        }
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("-error")) {
                try {
                    AILTraverser.traverse(args[0], AILErrorLoggerBase.INSTANCE);
                } catch (Exception e) {
                    // ignore exceptions when error checking
                }
                return;
            }
        }
        long startFull = System.currentTimeMillis();
        System.out.println("Compiling...");
        long start = System.currentTimeMillis();

        AILClass clazz = AILTraverser.traverse(args[0]);
        long diff = System.currentTimeMillis() - start;
        System.out.println("Parsed! (" + TimeUnit.MILLISECONDS.toMillis(diff) + "ms)");

        start = System.currentTimeMillis();
        byte[] bytecode = AILCompiler.compile(clazz);
        diff = System.currentTimeMillis() - start;
        System.out.println("Compiled! (" + TimeUnit.MILLISECONDS.toMillis(diff) + "ms)");

        start = System.currentTimeMillis();
        try {
            JarPackager.pack(bytecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        diff = System.currentTimeMillis() - start;
        System.out.println("Packaged! (" + TimeUnit.MILLISECONDS.toMillis(diff) + "ms)");

        long diffFull = System.currentTimeMillis() - startFull;
        System.out.println("Done! (" + TimeUnit.MILLISECONDS.toMillis(diffFull) + "ms)");
    }

}
