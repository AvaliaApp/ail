package app.avalia.compiler.provider.instruction.setup;

import app.avalia.compiler.AILCompiler;
import app.avalia.compiler.error.AILErrorLogger;
import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILContent;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.lang.type.AILContentType;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;

public class InsnCompilerVersionProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
        if (component.getArguments().size() != 1) {
            AILErrorLogger.logError(component.getLine(),
                    "incorrect arguments, ailv(<version>)");
            return;
        }
        AILArgument argument = component.getArguments().get(0);
        if (argument.getContentType() != AILContentType.VALUE) {
            AILErrorLogger.logError(component.getLine(),
                    "<version> must be a text value");
            return;
        }

        AILContent content = argument.getContent();
        AILValueContent valueContent = (AILValueContent) content;

        if (valueContent.getType() != AILType.TEXT) {
            AILErrorLogger.logError(component.getLine(),
                    "<version> must be a text value");
            return;
        }

        if (!valueContent.getContent().toString()
                .equalsIgnoreCase(AILCompiler.COMPILER_VERSION)) {
            AILErrorLogger.logError(component.getLine(),
                    "version " + valueContent.getContent() +
                            " doesn't match the compiler version "
                            + AILCompiler.COMPILER_VERSION);
        }
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {
    }
}
