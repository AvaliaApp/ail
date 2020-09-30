package app.avalia.compiler.lang;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public abstract class AILComponent {

    private int line;

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

    public boolean hasAttribute(Class<? extends Annotation> attribute) {
        for (Annotation annotation : attributes) {
            if (attribute.isAssignableFrom(annotation.getClass()))
                return true;
        }
        return false;
    }
}
