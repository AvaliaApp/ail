package app.avalia.compiler;

import app.avalia.antlr.AILBaseVisitor;
import app.avalia.antlr.AILLexer;
import app.avalia.antlr.AILParser;
import app.avalia.compiler.lang.error.AILErrorLogger;
import app.avalia.compiler.lang.*;
import app.avalia.compiler.lang.content.AILDelegateContent;
import app.avalia.compiler.lang.content.AILTypeContent;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.pool.BaseInstructions;
import app.avalia.compiler.lang.type.AILContentType;
import app.avalia.compiler.lang.type.AILType;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AILTraverser {

    public static AILClass traverseLocal(String classFile) throws IOException {
        InputStream stream = AILCompiler.class.getResourceAsStream(classFile);

        AILLexer lexer = new AILLexer(CharStreams.fromStream(stream));
        AILParser parser = new AILParser(new CommonTokenStream(lexer));

        return new ClassVisitor().visitBase(parser.parse().base());
    }

    public static void traverse(String classFile, ANTLRErrorListener listener) throws IOException {
        InputStream stream = new FileInputStream(new File(classFile));

        AILLexer lexer = new AILLexer(CharStreams.fromStream(stream));
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);

        AILParser parser = new AILParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(listener);

        new ClassVisitor().visitBase(parser.parse().base());
    }

    public static AILClass traverse(String classFile) throws IOException {
        InputStream stream = new FileInputStream(new File(classFile));

        AILLexer lexer = new AILLexer(CharStreams.fromStream(stream));
        AILParser parser = new AILParser(new CommonTokenStream(lexer));

        return new ClassVisitor().visitBase(parser.parse().base());
    }

    private static class ClassVisitor extends AILBaseVisitor<AILClass> {
        @Override
        public AILClass visitBase(AILParser.BaseContext ctx) {
            AILClass clazz = new AILClass();
            clazz.setLine(ctx.getStart().getLine());

            InstructionVisitor instructionVisitor = new InstructionVisitor();
            for (AILParser.InstructionContext insnContext : ctx.instruction()) {
                clazz.getInstructions().add(insnContext.accept(instructionVisitor));
            }
            FunctionVisitor functionVisitor = new FunctionVisitor();
            for (AILParser.DeclarationContext declContext : ctx.declaration()) {
                clazz.getFunctions().add(declContext.accept(functionVisitor));
            }
            return clazz;
        }
    }

    private static class FunctionVisitor extends AILBaseVisitor<AILFunction> {
        @Override
        public AILFunction visitDeclaration(AILParser.DeclarationContext ctx) {
            AILFunction function = new AILFunction();
            function.setLine(ctx.getStart().getLine());

            InstructionVisitor instructionVisitor = new InstructionVisitor();
            for (AILParser.InstructionContext insnContext : ctx.instruction()) {
                function.getInstructions().add(insnContext.accept(instructionVisitor));
            }
            function.setName(ctx.name().getText());
            return function;
        }
    }

    private static class InstructionVisitor extends AILBaseVisitor<AILInstruction> {
        @Override
        public AILInstruction visitInstruction(AILParser.InstructionContext ctx) {
            AILInstruction instruction = new AILInstruction();
            instruction.setLine(ctx.getStart().getLine());

            AILParser.IdContext id = ctx.id();
            if (id != null) {
                instruction.setHasId(true);
                instruction.setId(Integer.parseInt(id.getText()));

                if (instruction.getId() < 0)
                    AILErrorLogger.logError(ctx.getStart().getLine(), "id can't be negative");
            }
            instruction.setName(ctx.name().getText());
            if (ctx.instruction() != null && ctx.instruction().size() > 0) {
                instruction.setHasInnerInstructions(true);

                InstructionVisitor visitor = new InstructionVisitor();
                for (AILParser.InstructionContext insnContext : ctx.instruction()) {
                    instruction.getInstructions().add(insnContext.accept(visitor));
                }
            }

            if (ctx.arguments() != null) {
                ArgumentVisitor visitor = new ArgumentVisitor();
                for (AILParser.ArgumentContext argContext : ctx.arguments().argument())
                    instruction.getArguments().add(argContext.accept(visitor));
            }

            BaseInstructions.getProvider(instruction.getName())
                    .parse(instruction);

            return instruction;
        }
    }

    private static class ArgumentVisitor extends AILBaseVisitor<AILArgument> {
        @Override
        public AILArgument visitArgument(AILParser.ArgumentContext ctx) {
            AILArgument argument = new AILArgument();
            argument.setLine(ctx.getStart().getLine());

            String literalType = AILLexer.VOCABULARY.getDisplayName(ctx.getStart().getType());

            // Delegate content
            if (ctx.delegate() != null) {
                String target = ctx.delegate().name().getText();

                AILDelegateContent content = new AILDelegateContent();
                content.setDelegateTarget(target);

                argument.setContent(content);
                argument.setContentType(AILContentType.DELEGATE);
                return argument;
            }

            // Type content
            if (ctx.type() != null && ctx.value() == null) {
                String parsedType = ctx.type().getText();

                AILTypeContent content = new AILTypeContent();
                content.setType(AILType.valueOf(parsedType.toUpperCase()));

                argument.setContent(content);
                argument.setContentType(AILContentType.TYPE);
                return argument;
            }

            // Using type modifier
            if (ctx.type() != null && ctx.value() != null) {
                String parsedType = ctx.type().getText();
                String parsedValue = ctx.value().getText()
                        .replace("\"", "")
                        .replace("'", "");

                AILType type = AILType.valueOf(parsedType.toUpperCase());

                AILValueContent content = new AILValueContent();
                content.setType(type);
                content.setContent(type.cast(parsedValue));

                argument.setContent(content);
                argument.setContentType(AILContentType.VALUE);
                return argument;
            }

            // Auto-deducing type using lexer
            if (ctx.type() == null && ctx.value() != null) {
                String value = ctx.value().getText()
                        .replace("\"", "")
                        .replace("'", "");

                AILValueContent content = new AILValueContent();
                switch (literalType) {
                    case "TEXT_LITERAL":
                        content.setType(AILType.TEXT);
                        break;
                    case "DECIMAL_LITERAL":
                        content.setType(AILType.DOUBLE);
                        break;
                    case "NULL_LITERAL":
                        content.setType(AILType.REF);
                        break;
                    case "NUMBER_LITERAL":
                        content.setType(AILType.INT);
                        break;
                    case "BOOL_LITERAL":
                        content.setType(AILType.BOOL);
                        break;
                }

                content.setContent(content.getType().cast(value));

                argument.setContent(content);
                argument.setContentType(AILContentType.VALUE);
                return argument;
            }

            return argument;
        }
    }

}
