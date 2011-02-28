package me.taylorkelly.help;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Searcher {

    private HelpList helpList;
    private Player player;
    private MatchList matches;
    private String query;

    public Searcher(HelpList warpList) {
        this.helpList = warpList;
    }

    public void addPlayer(Player player) {
        this.player = player;
    }

    public void setQuery(String name) {
        this.query = name;
        this.matches = helpList.getMatches(name, player);
    }

    public void search() {
        ChatColor searchColor = ChatColor.YELLOW;
        
        if (matches.size() == 0) {
            player.sendMessage(ChatColor.RED + "No Help matches for search: " + ChatColor.GRAY + query);
        } else {
            if (matches.commandMatches.size() > 0) {
                player.sendMessage(searchColor.toString() + "Entries with commands similar to: " + ChatColor.GRAY + query);
                for (HelpEntry entry : matches.commandMatches) {
                    player.sendMessage(entry.message());
                }
            }
            if (matches.descriptionMatches.size() > 0) {
                player.sendMessage(searchColor.toString() + "Entries with descriptions similar to: " + ChatColor.GRAY + query);
                for (HelpEntry entry : matches.descriptionMatches) {
                    player.sendMessage(entry.message());
                }
            }
            if (matches.pluginExactMatches.size() > 0) {
                player.sendMessage(searchColor.toString() + "Entries from the plugin: " + ChatColor.GRAY + query);
                for (HelpEntry entry : matches.pluginExactMatches) {
                    player.sendMessage(entry.message());
                }
            } else if (matches.pluginPartialMatches.size() > 0) {
                player.sendMessage(searchColor.toString() + "Entries from plugins similar to: " + ChatColor.GRAY + query);
                for (HelpEntry entry : matches.pluginPartialMatches) {
                    player.sendMessage(entry.message());
                }
            }
        }
    }
}

class MatchList {

    public ArrayList<HelpEntry> commandMatches;
    public ArrayList<HelpEntry> pluginExactMatches;
    public ArrayList<HelpEntry> pluginPartialMatches;
    public ArrayList<HelpEntry> descriptionMatches;

    MatchList(ArrayList<HelpEntry> commandMatches, ArrayList<HelpEntry> pluginExactMatches, ArrayList<HelpEntry> pluginPartialMatches, ArrayList<HelpEntry> descriptionMatches) {
        this.commandMatches = commandMatches;
        this.pluginExactMatches = pluginExactMatches;
        this.pluginPartialMatches = pluginPartialMatches;
        this.descriptionMatches = descriptionMatches;
    }

    public int size() {
        return commandMatches.size() + pluginExactMatches.size() + pluginPartialMatches.size() + descriptionMatches.size();
    }
}
