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
        this.plugin = helpList.matchPlugin(plugin);
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
        ChatColor introDashColor = ChatColor.GOLD;
        ChatColor introTextColor = ChatColor.WHITE;
        int width = 325;

        if (plugin == null) {
            String subtro = " HELP (" + page + "/" + maxPages + ") ";
            int sizeRemaining = (int) ((width - MinecraftFontWidthCalculator.getStringWidth(subtro)) * 0.93);
            String dashes = dashes(sizeRemaining / 2);
            sizeRemaining = (int) ((width - MinecraftFontWidthCalculator.getStringWidth(dashes + subtro)) * 0.93);
            player.sendMessage(introDashColor.toString() + dashes + introTextColor.toString() + subtro + introDashColor.toString() + dashes(sizeRemaining));
        } else {
            if (sortedEntries.isEmpty()) {
                player.sendMessage(ChatColor.RED.toString() + plugin + " has no Help entries");
            } else {
                String subtro = " " + plugin.toUpperCase() + " HELP (" + page + "/" + maxPages + ") ";
                int sizeRemaining = (int) ((width - MinecraftFontWidthCalculator.getStringWidth(subtro)) * 0.93);
                String dashes = dashes(sizeRemaining / 2);
                sizeRemaining = (int) ((width - MinecraftFontWidthCalculator.getStringWidth(dashes + subtro)) * 0.93);
                player.sendMessage(introDashColor.toString() + dashes + introTextColor.toString() + subtro + introDashColor.toString() + dashes(sizeRemaining));
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
            int sizeRemaining = width - MinecraftFontWidthCalculator.getStringWidth(entryBuilder.toString());
            entryBuilder = new StringBuilder(entryBuilder.toString().replace("[", ChatColor.GRAY.toString() + "[").replace("]", "]" + commandColor.toString()));

            int descriptionSize = MinecraftFontWidthCalculator.getStringWidth(entry.description);
            if (sizeRemaining > descriptionSize) {
                entryBuilder.append(whitespace(sizeRemaining - descriptionSize));
                entryBuilder.append(entry.description.replace("[", ChatColor.GRAY.toString() + "[").replace("]", "]" + descriptionColor.toString()));
            } else if (sizeRemaining < descriptionSize) {
                player.sendMessage(entryBuilder.toString());
                player.sendMessage("  " + entry.description.replace("[", ChatColor.GRAY.toString() + "[").replace("]", "]" + descriptionColor.toString()));
            }
            player.sendMessage(entryBuilder.toString());
        }
    }

    public int getMaxPages(Player player) {
        if (plugin == null) {
            return (int) Math.ceil(helpList.getMaxEntries(player) / (double) HelpSettings.entriesPerPage);
        } else {
            return (int) Math.ceil(helpList.getMaxEntries(player, plugin) / (double) HelpSettings.entriesPerPage);
        }
    }

    public String whitespace(int length) {
        int spaceWidth = MinecraftFontWidthCalculator.getCharWidth(' ');

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < length-spaceWidth; i += spaceWidth) {
            ret.append(" ");
        }

        return ret.toString();
    }

    public String dashes(int length) {
        int spaceWidth = MinecraftFontWidthCalculator.getCharWidth('-');

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < length-spaceWidth; i += spaceWidth) {
            ret.append("-");
        }

        return ret.toString();
    }
}
