package me.taylorkelly.help;

import java.util.ArrayList;

import org.angelsl.minecraft.randomshit.fontwidth.MinecraftFontWidthCalculator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
        sortedEntries = helpList.getSortedHelp(player, start, HelpSettings.entriesPerPage);
        maxPages = (int) Math.ceil(helpList.getMaxEntries(player) / (double) HelpSettings.entriesPerPage);
    }

    public void list() {
        String intro = "------------------- Page " + page + "/" + maxPages + " -------------------";
        player.sendMessage(ChatColor.YELLOW + intro);
        for (HelpEntry entry : sortedEntries) {

            StringBuilder entryBuilder = new StringBuilder();
            entryBuilder.append("/");
            entryBuilder.append(entry.command);
            entryBuilder.append(" - ");

            //Find remaining length left
            int sizeRemaining = MinecraftFontWidthCalculator.getStringWidth(intro) - MinecraftFontWidthCalculator.getStringWidth(entryBuilder.toString());

            int descriptionSize = MinecraftFontWidthCalculator.getStringWidth(entry.description);
            if (sizeRemaining > descriptionSize) {
                entryBuilder.append(whitespace(sizeRemaining - descriptionSize));
                entryBuilder.append(entry.description);
            } else if (sizeRemaining < descriptionSize) {
                entryBuilder.append(substring(entry.description, sizeRemaining));
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
        return (int) Math.ceil(helpList.getMaxEntries(player) / (double) HelpSettings.entriesPerPage);
    }

    public String whitespace(int length) {
        int spaceWidth = MinecraftFontWidthCalculator.getStringWidth(" ");

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < length; i += spaceWidth) {
            ret.append(" ");
        }

        return ret.toString();
    }
}
