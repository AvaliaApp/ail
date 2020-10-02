package app.avalia.compiler.bytecode;

import app.avalia.compiler.lang.type.AILType;

/**
 * Stores bytecode type descriptors
 */
public enum BytecodeType {
    BOOL("Z"),
    CHAR("C"),
    BYTE("B"),
    SHORT("S"),
    INT("I"),
    FLOAT("F"),
    LONG("J"),
    DOUBLE("D"),
    VOID("V"),
    TEXT("Ljava/lang/String;"),
    REF("Ljava/lang/Object;");

    private final String descriptor;

    BytecodeType(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public static BytecodeType byAILType(AILType type) {
        for (BytecodeType bType : values()) {
            if (bType.name().equalsIgnoreCase(type.name()))
                return bType;
        }
        return null;
    }

    public static BytecodeType byDescriptor(String descriptor) {
        for (BytecodeType bType : values()) {
            if (bType.getDescriptor().equalsIgnoreCase(descriptor))
                return bType;
        }
        return BytecodeType.REF;
    }

    public static AILType toAILType(BytecodeType type) {
        if (type == BytecodeType.VOID)
            return null;
        return AILType.valueOf(type.name());
    }
}
