package app.avalia.compiler.bytecode.jar;

import app.avalia.compiler.pool.BasePoolProvider;
import app.avalia.compiler.pool.info.CommandPoolInfo;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JarPackager {

    private static final String SEP = File.separator;

    public static void pack(byte[] byteArray) throws IOException {
        ZipFile file = new ZipFile("plugin.jar");

        File metaInfoFolder = new File("META-INF");
        File metaInfo = new File(metaInfoFolder.getName() + SEP + "MANIFEST.MF");
        writeMetaInfo(metaInfo);
        file.addFolder(metaInfoFolder);

        File pluginConfig = new File("plugin.yml");
        writePluginConfig(pluginConfig);
        file.addFile(pluginConfig);

        File assembly = new File("AvaliaAssembly.class");
        FileUtils.writeByteArrayToFile(assembly, byteArray);
        file.addFile(assembly);

        FileUtils.deleteQuietly(pluginConfig);
        FileUtils.deleteDirectory(metaInfoFolder);
    }

    private static void writePluginConfig(File file) throws IOException {
        List<String> list = new ArrayList<>(Arrays.asList(
                "name: " + BasePoolProvider.getPluginName(),
                "version: " + BasePoolProvider.getPluginVersion(),
                "author: AILCompiler",
                "main: AvaliaAssembly",
                "commands:"));
        for (CommandPoolInfo info : BasePoolProvider.getCommandPool().getPool().values()) {
            list.add("  " + info.getName() + ":");
            list.add("    description: Auto-Generated AIL command");
        }
        FileUtils.writeLines(file, list, "\n");
    }

    private static void writeMetaInfo(File file) throws IOException {
        FileUtils.writeLines(file, Arrays.asList(
                "Manifest-Version: 1.0",
                "Created-By: AIL Compiler"), "\n");
    }

}
