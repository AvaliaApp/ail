package app.avalia.compiler.lang;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an element of parsed AIL class
 * Could be an argument, instruction, function declaration etc.
 */
public abstract class AILComponent {

    /**
     * Line where the component resides in the class
     */
    private int line;

    /**
     * Attributes of the component as annotations of the
     * {@link app.avalia.compiler.provider.AILProvider} implementation
     */
    private final Set<Annotation> attributes = new HashSet<>();

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Set<Annotation> getAttributes() {
        return attributes;
    }

    /**
     * Searches for the attribute by annotation class
     * @param attribute Annotation class
     * @return If the corresponding {@link app.avalia.compiler.provider.AILProvider}
     * implementation has the attribute
     */
    public boolean hasAttribute(Class<? extends Annotation> attribute) {
        for (Annotation annotation : attributes) {
            if (attribute.isAssignableFrom(annotation.getClass()))
                return true;
        }
        return false;
    }
}
