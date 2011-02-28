package me.taylorkelly.help;

import java.io.File;

public class HelpSettings {
    
    private static final String settingsFile = "Help.settings";

    public static int entriesPerPage;

    public static void initialize(File dataFolder) {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File configFile  = new File(dataFolder, settingsFile);
        PropertiesFile file = new PropertiesFile(configFile);
        entriesPerPage = file.getInt("entriesPerPage", 9, "Number of entries per page (recom. 9)");
        file.save();
    }
}
