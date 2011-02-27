package me.taylorkelly.help;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

public class HelpPermissions {
    private static Permissions permissionsPlugin;
    private static boolean permissionsEnabled = false;

    public static void initialize(Server server) {
        Plugin test = server.getPluginManager().getPlugin("Permissions");
        if (test != null) {
            permissionsPlugin = ((Permissions) test);
            permissionsEnabled = true;
            HelpLogger.info("Permissions enabled.");
        } else {
            HelpLogger.warning("Permissions isn't loaded, all commands will be visible");
        }
    }

    public static boolean permission(Player player, String string) {
        if(permissionsEnabled) {
            return permissionsPlugin.Security.permission(player, string);
        } else {
            return true;
        }
    }
}
