package me.taylorkelly.help;

import org.angelsl.minecraft.randomshit.fontwidth.MinecraftFontWidthCalculator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpEntry {

    public String command;
    public String description;
    public String[] permissions;
    public boolean main;
    public String plugin;
    public int lineLength;

    public HelpEntry(String command, String description, String plugin, boolean main, String[] permissions) {
        this.command = command;
        this.description = description;
        this.plugin = plugin;
        this.main = main;
        this.permissions = permissions;
        lineLength = process(this);
        System.out.println(lineLength);
    }

    public HelpEntry(String command, String description, String plugin) {
        this(command, description, plugin, false, new String[]{});
    }

    public HelpEntry(String command, String description, String plugin, boolean main) {
        this(command, description, plugin, main, new String[]{});
    }

    public HelpEntry(String command, String description, String plugin, String[] permissions) {
        this(command, description, plugin, false, permissions);
    }

    public boolean playerCanUse(Player player) {
        if (permissions.length == 0) {
            return true;
        }
        for (String permission : permissions) {
            if (permission.equalsIgnoreCase("OP") && player.isOp()) {
                return true;
            } else if (HelpPermissions.permission(player, permission)) {
                return true;
            }
        }
        return false;
    }

    public String message() {
        ChatColor commandColor = ChatColor.RED;
        ChatColor pluginColor = ChatColor.GREEN;
        ChatColor descriptionColor = ChatColor.WHITE;

        StringBuilder builder = new StringBuilder(commandColor.toString());
        builder.append("/");
        builder.append(command);
        builder.append(ChatColor.WHITE.toString());
        builder.append(" : ");
        builder.append("(via ");
        builder.append(pluginColor.toString());
        builder.append(plugin);
        builder.append(ChatColor.WHITE.toString());
        builder.append(") ");
        builder.append(descriptionColor.toString());
        builder.append(description);

        return builder.toString();
    }

    private int process(HelpEntry entry) {
        ChatColor commandColor = ChatColor.RED;
        ChatColor descriptionColor = ChatColor.WHITE;
        int width = 325;

        StringBuilder entryBuilder = new StringBuilder();
        entryBuilder.append(commandColor.toString());
        entryBuilder.append("/");
        entryBuilder.append(entry.command);
        entryBuilder.append(ChatColor.WHITE.toString());
        entryBuilder.append(" : ");
        entryBuilder.append(descriptionColor.toString());
        //Find remaining length left
        int sizeRemaining = width - MinecraftFontWidthCalculator.getStringWidth(entryBuilder.toString());
        entryBuilder = new StringBuilder(entryBuilder.toString().replace("[", ChatColor.GRAY.toString() + "[").replace("]", "]" + commandColor.toString()));

        int descriptionSize = MinecraftFontWidthCalculator.getStringWidth(entry.description);
        if (sizeRemaining > descriptionSize) {
            return 1;
        } else {
            return 1 + (int)Math.ceil((double)MinecraftFontWidthCalculator.getStringWidth("  " + entry.description)/width);
        }
    }
}
