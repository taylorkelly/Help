package me.taylorkelly.help;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HelpEntry {
    public String command;
    public String description;
    public String[] permissions;
    public boolean main;
    public Plugin plugin;

    public HelpEntry(String command, String description, Plugin plugin, boolean main, String[] permissions) {
        this.command = command;
        this.description = description;
        this.plugin = plugin;
        this.main = main;
        this.permissions = permissions;
    }

    public HelpEntry(String command, String description, Plugin plugin) {
        this(command, description, plugin, false, new String[]{});
    }

    public HelpEntry(String command, String description, Plugin plugin, boolean main) {
        this(command, description, plugin, main, new String[]{});
    }

    public HelpEntry(String command, String description, Plugin plugin, String[] permissions) {
        this(command, description, plugin, false, permissions);
    }

    public boolean playerCanUse(Player player) {
        if (permissions.length == 0) {
            return true;
        }
        for (String permission : permissions) {
            if(HelpPermissions.permission(player, permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean pluginIsEnabled() {
        return (plugin != null && plugin.isEnabled());
    }
}
