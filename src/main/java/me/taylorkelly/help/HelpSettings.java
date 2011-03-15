package me.taylorkelly.help;

import java.io.File;

public class HelpSettings {
    
    private static final String settingsFile = "Help.yml";

    public static int entriesPerPage;

    public static void initialize(File dataFolder) {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        if(new File(dataFolder, "Help.settings").exists()) {
            new File(dataFolder, "Help.settings").delete();
        }

        File configFile  = new File(dataFolder, settingsFile);
        BetterConfig config = new BetterConfig(configFile);
        config.load();
        entriesPerPage = config.getInt("entriesPerPage", 9);
        config.save();
    }
}
