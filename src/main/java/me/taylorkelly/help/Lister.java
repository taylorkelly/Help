package me.taylorkelly.help;

import java.util.ArrayList;

import org.angelsl.minecraft.randomshit.fontwidth.MinecraftFontWidthCalculator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Lister {

    private HelpList helpList;
    private Player player;
    private String plugin;
    private int maxPages;
    private int page;
    private ArrayList<HelpEntry> sortedEntries;

    Lister(HelpList helpList, String plugin, Player player) {
        this.helpList = helpList;
        this.player = player;
        this.plugin = plugin;
    }

    Lister(HelpList helpList, Player player) {
        this(helpList, null, player);
    }

    public void setPage(int page) {
        this.page = page;
        int start = (page - 1) * HelpSettings.entriesPerPage;
        if (plugin == null) {
            sortedEntries = helpList.getSortedHelp(player, start, HelpSettings.entriesPerPage);
            maxPages = (int) Math.ceil(helpList.getMaxEntries(player) / (double) HelpSettings.entriesPerPage);
        } else {
            sortedEntries = helpList.getSortedHelp(player, start, HelpSettings.entriesPerPage, plugin);
            maxPages = (int) Math.ceil(helpList.getMaxEntries(player, plugin) / (double) HelpSettings.entriesPerPage);
        }
    }

    public void list() {
        ChatColor commandColor = ChatColor.RED;
        ChatColor descriptionColor = ChatColor.WHITE;
        ChatColor introDashColor = ChatColor.AQUA;
        ChatColor introTextColor = ChatColor.WHITE;

        String intro = "---------------------------------------------------";

        if (plugin == null) {
            String subtro = " HELP (" + page + "/" + maxPages + ") ";
            int sizeRemaining = MinecraftFontWidthCalculator.getStringWidth(intro) - MinecraftFontWidthCalculator.getStringWidth(subtro);
            player.sendMessage(introDashColor.toString() + dashes(sizeRemaining / 2) + introTextColor.toString() + subtro + introDashColor.toString() + dashes(sizeRemaining / 2));
        } else {
            if (sortedEntries.isEmpty()) {
                player.sendMessage(ChatColor.RED.toString() + plugin + " has no Help entries");
            } else {
                String subtro = " " + plugin.toUpperCase() + " HELP (" + page + "/" + maxPages + ") ";
                int sizeRemaining = MinecraftFontWidthCalculator.getStringWidth(intro) - MinecraftFontWidthCalculator.getStringWidth(subtro);
                player.sendMessage(introDashColor.toString() + dashes(sizeRemaining / 2) + introTextColor.toString() + subtro + introDashColor.toString() + dashes(sizeRemaining / 2));
            }
        }

        for (HelpEntry entry : sortedEntries) {
            StringBuilder entryBuilder = new StringBuilder();
            entryBuilder.append(commandColor.toString());
            entryBuilder.append("/");
            entryBuilder.append(entry.command);
            entryBuilder.append(ChatColor.WHITE.toString());
            entryBuilder.append(" : ");
            entryBuilder.append(descriptionColor.toString());
            //Find remaining length left
            int sizeRemaining = MinecraftFontWidthCalculator.getStringWidth(intro) - MinecraftFontWidthCalculator.getStringWidth(entryBuilder.toString());
            entryBuilder = new StringBuilder(entryBuilder.toString().replace("[", ChatColor.GRAY.toString() + "[").replace("]", "]" + commandColor.toString()));

            int descriptionSize = MinecraftFontWidthCalculator.getStringWidth(entry.description);
            if (sizeRemaining > descriptionSize) {
                entryBuilder.append(whitespace(sizeRemaining - descriptionSize));
                entryBuilder.append(entry.description.replace("[", ChatColor.GRAY.toString() + "[").replace("]", "]" + descriptionColor.toString()));
            } else if (sizeRemaining < descriptionSize) {
                entryBuilder.append(substring(entry.description.replace("[", ChatColor.GRAY.toString() + "[").replace("]", "]" + commandColor.toString()), sizeRemaining));
            }

            player.sendMessage(entryBuilder.toString());
        }
    }

    /**
     * Lob shit off that string till it fits.
     */
    private String substring(String name, int left) {
        while (MinecraftFontWidthCalculator.getStringWidth(name) > left) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

    public int getMaxPages(Player player) {
        if (plugin == null) {
            return (int) Math.ceil(helpList.getMaxEntries(player) / (double) HelpSettings.entriesPerPage);
        } else {
            return (int) Math.ceil(helpList.getMaxEntries(player, plugin) / (double) HelpSettings.entriesPerPage);
        }
    }

    public String whitespace(int length) {
        int spaceWidth = MinecraftFontWidthCalculator.getStringWidth(" ");

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < length; i += spaceWidth) {
            ret.append(" ");
        }

        return ret.toString();
    }

    public String dashes(int length) {
        int spaceWidth = MinecraftFontWidthCalculator.getStringWidth("-");

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < length; i += spaceWidth) {
            ret.append("-");
        }

        return ret.toString();
    }
}
