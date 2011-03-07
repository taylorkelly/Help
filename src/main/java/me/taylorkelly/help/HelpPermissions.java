package me.taylorkelly.help;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;
import org.anjocaido.groupmanager.GroupManager;

public class HelpPermissions {
    private enum PermissionHandler {

        PERMISSIONS, GROUP_MANAGER, NONE
    }
    private static PermissionHandler handler;
    private static Plugin permissionPlugin;

    public static void initialize(Server server) {
        Plugin groupManager = server.getPluginManager().getPlugin("GroupManager");
        Plugin permissions = server.getPluginManager().getPlugin("Permissions");

        if (groupManager != null) {
            permissionPlugin = groupManager;
            handler = PermissionHandler.GROUP_MANAGER;
            String version = groupManager.getDescription().getVersion();
            HelpLogger.info("Permissions enabled using: GroupManager v" + version);
        } else if (permissions != null) {
            permissionPlugin = permissions;
            handler = PermissionHandler.PERMISSIONS;
            String version = permissions.getDescription().getVersion();
            HelpLogger.info("Permissions enabled using: Permissions v" + version);
        } else {
            handler = PermissionHandler.NONE;
            HelpLogger.warning("A permission plugin isn't loaded.");
        }
    }

    public static boolean permission(Player player, String permission) {
        switch (handler) {
            case PERMISSIONS:
                return ((Permissions)permissionPlugin).getHandler().permission(player, permission);
            case GROUP_MANAGER:
                return ((GroupManager)permissionPlugin).getWorldsHolder().getWorldPermissions(player).has(player, permission);
            case NONE:
                return true;
            default:
                return true;
        }
    }
}
