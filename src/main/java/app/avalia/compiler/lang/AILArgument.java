package app.avalia.compiler.lang;

import app.avalia.compiler.lang.type.AILContentType;

/**
 * Handles information about a single argument in an instruction
 * Example: instruction(>argument<)
 */
public class AILArgument extends AILComponent {

    /**
     * Argument type
     */
    private AILContentType contentType;
    /**
     * Content implementation based on {@link AILContentType}
     */
    private AILContent content;

    public AILContentType getContentType() {
        return contentType;
    }

    public void setContentType(AILContentType contentType) {
        this.contentType = contentType;
    }

    public AILContent getContent() {
        return content;
    }

    public void setContent(AILContent content) {
        this.content = content;
    }
}
