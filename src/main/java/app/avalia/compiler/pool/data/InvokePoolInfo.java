package app.avalia.compiler.pool.data;

import org.objectweb.asm.Opcodes;

public class InvokePoolInfo {

    private final int id;
    private int invokeType;
    private String methodName;
    private String instanceSig;
    private String methodSig;

    public InvokePoolInfo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getInvokeType() {
        return invokeType;
    }

    public void setInvokeType(String invokeType) {
        switch (invokeType) {
            case "static":
                this.invokeType = Opcodes.INVOKESTATIC;
                return;
            case "dynamic":
                this.invokeType = Opcodes.INVOKEDYNAMIC;
                return;
            case "virtual":
                this.invokeType = Opcodes.INVOKEVIRTUAL;
                return;
            case "special":
                this.invokeType = Opcodes.INVOKESPECIAL;
                return;
            case "interface":
                this.invokeType = Opcodes.INVOKEINTERFACE;
                return;
        }
        this.invokeType = -1;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getInstanceSig() {
        return instanceSig;
    }

    public void setInstanceSig(String instanceSig) {
        this.instanceSig = instanceSig;
    }

    public String getMethodSig() {
        return methodSig;
    }

    public void setMethodSig(String methodSig) {
        this.methodSig = methodSig;
    }

}
