package app.avalia.compiler.bytecode;

import app.avalia.compiler.lang.type.AILType;

import java.util.ArrayList;
import java.util.List;

public class BytecodeDescriptors {

    /**
     * Parses a bytecode method descriptor
     * Currently does not support array types (omits them)
     * @param descriptor Bytecode descriptor
     * @return {@link BytecodeMethodInfo}
     */
    public static BytecodeMethodInfo parse(String descriptor) {
        int beginIndex = descriptor.indexOf('(');
        int endIndex = descriptor.lastIndexOf(')');

        List<AILType> arguments = new ArrayList<>();
        for (int i = beginIndex + 1; i < endIndex; i++) {
            char c = descriptor.charAt(i);

            if (c == '[') {
                continue; // todo (important) support for arrays/multiarrays
            }
            if (c == 'L') {
                int end = descriptor.indexOf(';', i + 1);
                System.out.println(end);

                System.out.println(i + ", " + (end + 1));
                String full = descriptor.substring(i, end + 1);
                arguments.add(full.contains("java/lang/String") ? AILType.TEXT : AILType.REF);

                i = end;

                if (i > endIndex)
                    break;

                continue;
            }
            System.out.println(c);
            arguments.add(byDescriptor(String.valueOf(c)));
        }
        String returnType = descriptor.substring(endIndex + 1, descriptor.toCharArray().length)
                .replace("[", ""); // todo (important) support for arrays/multiarrays

        return new BytecodeMethodInfo(arguments, byDescriptor(returnType));
    }

    /**
     * Gets a type based on it's bytecode representation
     * @param descriptor Type descriptor
     * @return {@link AILType}
     */
    private static AILType byDescriptor(String descriptor) {
        BytecodeType type = BytecodeType.byDescriptor(descriptor);
        return BytecodeType.toAILType(type);
    }

    public static class BytecodeMethodInfo {
        private final List<AILType> arguments;
        private final AILType returnType;

        public BytecodeMethodInfo(List<AILType> arguments, AILType returnType) {
            this.arguments = arguments;
            this.returnType = returnType;
        }

        public boolean isReturnTypePrimitive() {
            return returnType != AILType.REF && returnType != AILType.TEXT;
        }

        public boolean isVoid() {
            return returnType == null;
        }

        public List<AILType> getArguments() {
            return arguments;
        }

        public AILType getReturnType() {
            return returnType;
        }
    }

}
