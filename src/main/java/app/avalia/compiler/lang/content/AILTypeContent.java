package app.avalia.compiler.lang.content;

import app.avalia.compiler.lang.AILContent;
import app.avalia.compiler.lang.type.AILType;

/**
 * Type value
 * Example: "int"
 */
public class AILTypeContent implements AILContent {

    private AILType type;

    public AILType getType() {
        return type;
    }

    public void setType(AILType type) {
        this.type = type;
    }

}
