package app.avalia.compiler.lang;

import app.avalia.compiler.lang.type.AILContentType;

public class AILArgument extends AILComponent {

    private AILContentType contentType;
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
