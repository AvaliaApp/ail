package app.avalia.compiler.pool.info;

/**
 * Handles information about a command and it's function declaration
 */
public class CommandPoolInfo {

    private String target;
    private String name;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
