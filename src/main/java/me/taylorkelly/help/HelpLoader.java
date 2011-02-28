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
        File extraHelp = new File(dataFolder, "ExtraHelp.yml");
        final Yaml yaml = new Yaml(new SafeConstructor());
        Map<String, Object> root;
        if (!extraHelp.exists()) {
            HelpLogger.info("No extra help entries loaded");
            return;
        }
        FileInputStream input = null;
        try {
            input = new FileInputStream(extraHelp);
            root = (Map<String, Object>) yaml.load(new UnicodeReader(input));
            int count = 0;

            for (String helpKey : root.keySet()) {
                Map<String, Object> helpNode = (Map<String, Object>) root.get(helpKey);

                if (!helpNode.containsKey("command")) {
                    HelpLogger.warning("A Help entry node is missing a command name");
                    continue;
                }
                String command = helpNode.get("command").toString();
                if (!helpNode.containsKey("description")) {
                    HelpLogger.warning(command + "'s Help entry is missing a description");
                    continue;
                }
                String description = helpNode.get("description").toString();
                if (!helpNode.containsKey("plugin")) {
                    HelpLogger.warning(command + "'s Help entry is missing a plugin");
                    continue;
                }
                String plugin = helpNode.get("plugin").toString();

                boolean main = false;
                if (helpNode.containsKey("main")) {
                    if (helpNode.get("main") instanceof Boolean) {
                        main = (Boolean)helpNode.get("main");
                    } else {
                        HelpLogger.warning(command + "'s Help entry has main as a non-boolean. Defaulting to false");
                    }
                }

                ArrayList<String> permissions = new ArrayList<String>();
                if (helpNode.containsKey("permissions")) {
                    if (helpNode.get("permissions") instanceof List) {
                        for(Object permission : (List)helpNode.get("permissions")) {
                            permissions.add(permission.toString());
                        }
                    } else {
                        permissions.add(helpNode.get("permissions").toString());
                    }
                }

                list.registerCommand(command, description, plugin, main, permissions.toArray(new String[]{}));
                count++;
            }
            HelpLogger.info(count + " extra help entries loaded");
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
}
