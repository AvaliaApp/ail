# Avalia Compiler (IL) ![ail](https://github.com/AvaliaApp/ail/workflows/ail/badge.svg)
**Avalia Intermediate Language** is a sophisticated tool that allows apps or custom languages to easily create fully-featured Minecraft plugins by generating simple to use and understand AIL classes.

### Motivation
Avalia IL was created to create a powerful yet simple communication bridge between [Avalia Node Editor](https://www.avalia.app) and [JVM bytecode](https://en.wikipedia.org/wiki/Java_bytecode), specifically targeted for Minecraft plugin generation.
Custom IL made solely for that purpose gives a lot of posibilities and makes the process of compiling Node schemas to Minecraft plugin artifacts way simpler and more feature-packed.

AIL was not made with human-readability with mind but ease of generation by 3rd party apps, yet it's suprisingly easy to read and understand by humans, too. AIL requires a decent knowledge about 
how JVM bytecode works though, as it's heavily inspired by it. 

### Future
Our long-term goal is to create a platform that can be used in all kinds of applications that require Minecraft plugin generation (i.e custom languages, node editors, web-based editors etc.) 

For this goal to happen, we need to create a stable, well-designed and well-documented instruction set for all kinds of often uses to make generating Minecraft plugins easier than ever.

- Support for array types
- More int-type operations (mul, div)
- Field support (for example getstatic/getfield)
- Referenced objects casting
- Optimize parsing
- Command arguments
- Auto-casting multiple stack values
- More helper instructions (like for loops)

### Usage
```shell script
java -jar compiler.jar class.ail [-error] 
```
* `-error` > only traversing, returns errors in `line:error` format

### Instruction Set (Version b100)
| Instruction 	| Stack      	| Arguments                             	| Description                                        	|
|-------------	|------------	|---------------------------------------	|----------------------------------------------------	|
| ailv        	|            	| `<version>`                           	| Verifies the compiler/class version                	|
| cfgn        	|            	| `<name>`                              	| Sets the output plugin name                        	|
| cfgv        	|            	| `<version>`                           	| Sets the output plugin version                     	|
| ipool`$id`  	|            	| `<itype>` `<mname>` `<isig>` `<msig>` 	| Adds an invoke data to the pool                    	|
| epool       	|            	| `<delegate>` `<msig>`                 	| Delegates a Bukkit event                           	|
| cpool       	|            	| `<delegate>` `<name>`                 	| Delegates a Bukkit command                         	|
| push        	| `-> v`     	| `<value>`                             	| Pushes a value onto the stack                      	|
| store`$id`  	| `v ->`     	| `[type]`                              	| Stores a variable at `id`                          	|
| cast        	| `v -> v`   	| `<itype>` `<otype>`                   	| Converts a value of itype to otype                 	|
| clis        	|            	|                                       	| Marks the position of a command listener           	|
| if`{}`      	| `v, v? ->` 	| `<iftype>`                            	| Creates an if expression                           	|
| inc`$id`    	|            	| `<value>`                             	| Increments an int-type variable by value           	|
| init        	|            	|                                       	| Registers event listeners and commands             	|
| invoke`$id` 	| `v+ -> v`  	|                                       	| Invokes a method/function from invoke pool         	|
| jmp`$id`    	|            	|                                       	| Jumps to a label of `id`                           	|
| label`$id`  	|            	|                                       	| Creates a label of `id`                            	|
| load`$id`   	| `-> v`     	| `[type]`                              	| Loads a value from a local variable of `id`        	|
| new`$id`    	| `v+ -> v`  	|                                       	| Initializes a new object from invoke pool          	|
| nvar`$id`   	| `-> v`     	| `<value>`                             	| Pushes a value and stores it as a variable at `id` 	|
| print`{}`   	|            	|                                       	| Prints a value                                     	|
| ret         	| `v ->`     	| `[type]`                              	| Returns a value                                    	|

### Types
All hand-typed values can be statically typed by marking the type before the value.
```groovy
push(short 10) // pushes 10 as a short value
```

But that's not always required, see [Auto Type Matcher](#auto-type-matcher)

### Auto Type Matcher
AIL Compiler features an automatic type matcher and casting that allows for dynamic-like use of AIL.
Because of that, you're not enforced to do any type declaration.

Compiler will automatically observe the stack and what is pushed onto it, so it always knows what is on the stack at any time.
Not only that, but it also stores variable type info, and it's position in the method.

This allows for compile-time stack checks that will inform you if the stack is not empty at the end of the method declaration.

```groovy
push(20) // pushed int
store$1 // auto type matching, no need for specifying type
```

Without automatic type matching:
```groovy
push(int 20)
store$1(int) // no auto type matcher
```

### Casting
Casting is a very powerful mechanic in AIL Compiler, it not only allows for casting primitives, but also referenced objects and strings (texts).
It uses bytecode casting capabilities for primitives, and Java's Standard Library to convert primitives to text and vice versa.
For referenced objects it uses `toString()` or `#valueOf`.

By default, auto-casting is enabled.
```groovy
push("23.65")
store$1(double) // Automatic cast from text to double
```
Without auto-casting:
```groovy
push("23.65")
cast(text, double) // Manual cast
store$1
```

Auto-casting doesn't work when pushing multiple stack values (for example when pushing invoke arguments), that will change in the future!

In the future it will also allow for casting/converting arrays.

### Jumps and labels
Think about labels like marks in the code. You can jump to a certain point of execution, no matter if it's before the label is created, or later.

```groovy
label$1
jmp$1 // jumps to label$1
```

This also works!
```groovy
jmp$1 // jumps to label$1, omits the code between the jump and the label
// some code
label$1
```

### Delegates
Delegates are just like function pointers. They point to a certain declared function for later execution.
```groovy
epool(delegate MyFunction, "(Lorg/bukkit/event/block/BlockBreakEvent;)V")

decl MyFunction {
    // function body
}
```

### Instructions with inner body
All instructions can have an inner body, but only some of them (marked as `{}`) make use of it.
Some instructions require executing some bytecode *before* your code can be visited.

An example of such instruction:
```groovy
print {               // begin
    push("some text") // body
}                     // end
```
This results in:
```java
System.out.println("some text");
```
Treat `{` and `}` as `begin` and `end` of an instruction.

### AIL Class Example
```groovy
// all AIL classes must start with _ailv instruction
// to properly identify and compare the compiler/class version
ailv("b100")

// setup the plugin info
cfgn("TestPlugin")
cfgv("1.0")

// add invoke pool info
ipool$1("virtual", "getPlayer", "org/bukkit/event/block/BlockBreakEvent", "()Lorg/bukkit/entity/Player;")
ipool$2("interface", "sendMessage", "org/bukkit/entity/Player", "(Ljava/lang/String;)V")

// add event pool info
epool(delegate OnBlockBreak, "(Lorg/bukkit/event/block/BlockBreakEvent;)V")

// add command pool info
cpool(delegate TestCommand, "test")

clis

decl OnEnable {
    init               // register everything
}

decl OnBlockBreak {
    load$1             // load event var
    invoke$1           // invoke getPlayer()
    store$2            // save the result of getPlayer()

    push(0)            // load 0
    store$3            // save i=0
    label$0            // mark the start of the loop
    load$3             // load i
    push(10)           // load 10

    if('<') {          // if condition not met, jump to the end
        load$2
        push("this will be printed 10 times!")
        invoke$2

        inc$3(1)       // increase i by 1 (i++)
        jmp$0          // jump to the loop beginning
    }
}

decl TestCommand {
    nvar$5("23.675")   // create a text variable of 23.675
    load$5             // load text variable
    store$6(double)    // create a double variable with auto-casted contents of prev variable 

    load$6             // load a double variable
    if('>0') {
        print {
            push("23.675 > 0")
        }   
    }

    print {
        nvar$6(long 283529183566) // create a long variable
        load$6                    // load a long variable
    } // print will auto-cast long data to text  
}
```

```java
public class AvaliaAssembly
extends JavaPlugin
implements Listener,
CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (command.getName().equalsIgnoreCase("test")) {
            return this.testCommand(commandSender, command, string, arrstring);
        }
        return true;
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("test").setExecutor((CommandExecutor)this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        Player player = blockBreakEvent.getPlayer();
        for (int i = 0; i < 10; ++i) {
            player.sendMessage("this will be printed 10 times!");
        }
    }

    public boolean testCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        String string2 = "23.675";
        double d = Double.parseDouble(string2);
        if (d > 0) {
            System.out.println("23.675 > 0");
        }
        long l = 283529183566L;
        System.out.println(String.valueOf(l));
        return true;
    }
}
```

### Extensions
AIL Compiler features basic extension support.

- Add the compiler as a dependency.
```groovy
repositories {
    maven {
        url 'https://repo.socketbyte.pl/snapshots'
    }
}

dependencies {
    compileOnly group: 'app.avalia', name: 'ail', version: 'b100-SNAPSHOT'
}
``` 
- Create a class that implements `AILExtension` interface
```java
public class ExampleExtension implements AILExtension {
    @Override
    public String getName() {
        return "ExampleExtension";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void fetchInstructions(Map<String, AILProvider<AILInstruction>> map) {
        map.put("example", new ExampleInstruction());
    }

    @Override
    public void fetchFunctions(Map<String, AILProvider<AILFunction>> map) {
    }
}
```
- You can now create custom functions/instructions on demand!
```java
@IgnoreInnerInstructions
public class ExampleInstruction implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction instruction) {
        System.out.println("This will be executed while parsing/error-checking!");
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction instruction) {
        // All bytecode changes are made through BytecodeVisitor
        visitor.current().visitInsn(Opcodes.ICONST_3);

        // It's very important to mark all your stack changes
        StackObserver.push(AILType.INT);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction instruction) {
    }
}
```
- Load your extension by placing the compiled .jar to `extensions/` folder (where your compiler resides) 

### License
AIL Compiler uses [MIT License](https://github.com/AvaliaApp/ail/blob/master/LICENSE) as it's license.