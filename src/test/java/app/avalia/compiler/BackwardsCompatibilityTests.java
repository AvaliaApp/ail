package app.avalia.compiler;

import app.avalia.compiler.lang.AILClass;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BackwardsCompatibilityTests {

    private static final String CODE_B100 = "// all AIL classes must start with _ailv instruction\n" +
            "// to properly identify and compare the compiler/class version\n" +
            "ailv(\"b100\")\n" +
            "\n" +
            "// setup the plugin info\n" +
            "cfgn(\"TestPlugin\")\n" +
            "cfgv(\"1.0\")\n" +
            "\n" +
            "// add invoke pool info\n" +
            "ipool$1(\"virtual\", \"getPlayer\", \"org/bukkit/event/block/BlockBreakEvent\", \"()Lorg/bukkit/entity/Player;\")\n" +
            "ipool$2(\"interface\", \"sendMessage\", \"org/bukkit/entity/Player\", \"(Ljava/lang/String;)V\")\n" +
            "\n" +
            "// add event pool info\n" +
            "epool(delegate OnBlockBreak, \"(Lorg/bukkit/event/block/BlockBreakEvent;)V\")\n" +
            "\n" +
            "// add command pool info\n" +
            "cpool(delegate TestCommand, \"test\")\n" +
            "\n" +
            "clis\n" +
            "\n" +
            "decl OnEnable {\n" +
            "    init               // register everything\n" +
            "}\n" +
            "\n" +
            "decl OnBlockBreak {\n" +
            "    load$1             // load event var\n" +
            "    invoke$1           // invoke getPlayer()\n" +
            "    store$2            // save the result of getPlayer()\n" +
            "\n" +
            "    push(0)            // load 0\n" +
            "    store$3            // save i=0\n" +
            "    label$0            // mark the start of the loop\n" +
            "    load$3             // load i\n" +
            "    push(10)           // load 10\n" +
            "\n" +
            "    if('<') {          // if condition not met, jump to the end\n" +
            "        load$2\n" +
            "        push(\"this will be printed 10 times!\")\n" +
            "        invoke$2\n" +
            "\n" +
            "        inc$3(1)       // increase i by 1 (i++)\n" +
            "        jmp$0          // jump to the loop beginning\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "decl TestCommand {\n" +
            "    nvar$5(\"23.675\")   // create a text variable of 23.675\n" +
            "    load$5             // load text variable\n" +
            "    store$6(double)    // create a double variable with auto-casted contents of prev variable \n" +
            "\n" +
            "    load$6             // load a double variable\n" +
            "    if('>0') {\n" +
            "        print {\n" +
            "            push(\"23.675 > 0\")\n" +
            "        }   \n" +
            "    }\n" +
            "\n" +
            "    print {\n" +
            "        nvar$6(long 283529183566) // create a long variable\n" +
            "        load$6                    // load a long variable\n" +
            "    } // print will auto-cast long data to text  \n" +
            "}";

    @Test
    public void codeB100Test() {
        AILClass ailClass =
                assertDoesNotThrow(() -> AILTraverser.traverseString(CODE_B100));

        assertDoesNotThrow(() -> AILCompiler.compile(ailClass));
    }

}
