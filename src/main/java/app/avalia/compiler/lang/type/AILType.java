package app.avalia.compiler.lang.type;

import org.objectweb.asm.Opcodes;

public enum AILType {

    INT(int.class),
    CHAR(char.class),
    SHORT(short.class),
    BOOL(boolean.class),
    REF(Object.class),
    BYTE(byte.class),
    TEXT(String.class),
    DOUBLE(double.class),
    FLOAT(float.class),
    LONG(long.class);

    private final Class<?> clazz;

    AILType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getJavaClass() {
        return clazz;
    }

    public Object cast(String parsedValue) {
        switch (this) {
            case INT: return Integer.parseInt(parsedValue);
            case FLOAT: return Float.parseFloat(parsedValue);
            case DOUBLE: return Double.parseDouble(parsedValue);
            case SHORT: return Short.parseShort(parsedValue);
            case BOOL: return Boolean.parseBoolean(parsedValue);
            case BYTE: return Byte.parseByte(parsedValue);
            case LONG: return Long.parseLong(parsedValue);
            case CHAR: return parsedValue.charAt(0);
            case TEXT:
            case REF:
                return parsedValue;
        }
        return parsedValue;
    }

    public int toRetInsn() {
        switch (this) {
            case INT:
            case BYTE:
            case CHAR:
            case SHORT:
            case BOOL:
                return Opcodes.IRETURN;
            case LONG:
                return Opcodes.LRETURN;
            case FLOAT:
                return Opcodes.FRETURN;
            case DOUBLE:
                return Opcodes.DRETURN;
            case REF:
            case TEXT:
                return Opcodes.ARETURN;
        }
        return -1;
    }

    public int toLoadInsn() {
        switch (this) {
            case INT:
            case BYTE:
            case CHAR:
            case SHORT:
            case BOOL:
                return Opcodes.ILOAD;
            case LONG:
                return Opcodes.LLOAD;
            case FLOAT:
                return Opcodes.FLOAD;
            case DOUBLE:
                return Opcodes.DLOAD;
            case REF:
            case TEXT:
                return Opcodes.ALOAD;
        }
        return -1;
    }

    public int toStoreInsn() {
        switch (this) {
            case INT:
            case BYTE:
            case CHAR:
            case SHORT:
            case BOOL:
                return Opcodes.ISTORE;
            case LONG:
                return Opcodes.LSTORE;
            case FLOAT:
                return Opcodes.FSTORE;
            case DOUBLE:
                return Opcodes.DSTORE;
            case REF:
            case TEXT:
                return Opcodes.ASTORE;
        }
        return -1;
    }
}
