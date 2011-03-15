package me.taylorkelly.help;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.reader.UnicodeReader;

public class HelpLoader {

    public static void load(File dataFolder, HelpList list) {
        File helpFolder = new File(dataFolder, "ExtraHelp");
        if (helpFolder.exists()) {
            helpFolder.mkdirs();
        }
        int count = 0;

        for (File insideFile : helpFolder.listFiles(new YmlFilter())) {
            System.out.println(insideFile);
            final Yaml yaml = new Yaml(new SafeConstructor());
            Map<String, Object> root;
            FileInputStream input = null;
            try {
                input = new FileInputStream(insideFile);
                root = (Map<String, Object>) yaml.load(new UnicodeReader(input));

                for (String helpKey : root.keySet()) {
                    Map<String, Object> helpNode = (Map<String, Object>) root.get(helpKey);

                    String command = helpNode.get("command").toString();
                    boolean main = false;
                    String description = helpNode.get("description").toString();
                    String plugin = helpNode.get("plugin").toString();
                    boolean visible = true;
                    ArrayList<String> permissions = new ArrayList<String>();


                    if (!helpNode.containsKey("command")) {
                        HelpLogger.warning("A Help entry node is missing a command name");
                        continue;
                    }
                    if (!helpNode.containsKey("description")) {
                        HelpLogger.warning(command + "'s Help entry is missing a description");
                        continue;
                    }
                    if (!helpNode.containsKey("plugin")) {
                        HelpLogger.warning(command + "'s Help entry is missing a 'plugin'");
                        continue;
                    }

                    if (helpNode.containsKey("main")) {
                        if (helpNode.get("main") instanceof Boolean) {
                            main = (Boolean) helpNode.get("main");
                        } else {
                            HelpLogger.warning(command + "'s Help entry has 'main' as a non-boolean. Defaulting to false");
                        }
                    }

                    if (helpNode.containsKey("visible")) {
                        if (helpNode.get("visible") instanceof Boolean) {
                            visible = (Boolean) helpNode.get("visible");
                        } else {
                            HelpLogger.warning(command + "'s Help entry has 'visible' as a non-boolean. Defaulting to true");
                        }
                    }

                    if (helpNode.containsKey("permissions")) {
                        if (helpNode.get("permissions") instanceof List) {
                            for (Object permission : (List) helpNode.get("permissions")) {
                                permissions.add(permission.toString());
                            }
                        } else {
                            permissions.add(helpNode.get("permissions").toString());
                        }
                    }

                    list.customRegisterCommand(command, description, plugin, main, permissions.toArray(new String[]{}), visible);
                    count++;
                }
            } catch (Exception ex) {
                HelpLogger.severe("Error!", ex);
            } finally {
                try {
                    input.close();
                } catch (IOException ex) {
                    HelpLogger.severe("Error!", ex);
                }
            }
        }
        HelpLogger.info(count + " extra help entries loaded");
    }
}
