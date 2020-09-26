package app.avalia.compiler.provider;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILComponent;

public interface AILProvider<T extends AILComponent> {

    void parse(T component);
    void begin(BytecodeVisitor visitor, T component);
    void end(BytecodeVisitor visitor, T component);

}
