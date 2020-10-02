package app.avalia.compiler.lang.content;

import app.avalia.compiler.lang.AILContent;
import app.avalia.compiler.lang.type.AILType;

/**
 * Value type, always contains non-null type even if it is auto-matched
 * Example: "int 10", "10"
 */
public class AILValueContent implements AILContent {

    private AILType type;
    private Object content;

    public AILType getType() {
        return type;
    }

    public void setType(AILType type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

}
