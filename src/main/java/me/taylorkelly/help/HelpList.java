package me.taylorkelly.help;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HelpList {
    private HashMap<String, HelpEntry> helpList;

    HelpList() {
        helpList = new HashMap<String, HelpEntry>();
    }

    public ArrayList<HelpEntry> getSortedHelp(Player player, int start, int size) {
        ArrayList<HelpEntry> ret = new ArrayList<HelpEntry>();
        List<String> names = new ArrayList<String>(helpList.keySet());
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.SECONDARY);
        Collections.sort(names, collator);

        int index = 0;
        int currentCount = 0;
        while (index < names.size() && ret.size() < size) {
            String currName = names.get(index);
            HelpEntry entry = helpList.get(currName);
            if (entry.playerCanUse(player)) {
                if (currentCount >= start) {
                    ret.add(entry);
                } else {
                    currentCount++;
                }
            }
            index++;
        }
        return ret;
    }

    public int getSize() {
        return helpList.size();
    }

    public double getMaxEntries(Player player) {
        int count = 0;
        for (HelpEntry entry : helpList.values()) {
            if (entry.playerCanUse(player)) {
                count++;
            }
        }
        return count;
    }

    public boolean registerCommand(String command, String description, Plugin plugin) {
        HelpEntry entry = new HelpEntry(command, description, plugin);
        helpList.put(command, entry);
        return true;
    }

    public boolean registerCommand(String command, String description, Plugin plugin, int priority) {
        HelpEntry entry = new HelpEntry(command, description, plugin, priority);
        helpList.put(command, entry);
        return true;
    }

    public boolean registerCommand(String command, String description, Plugin plugin, String[] permissions) {
        HelpEntry entry = new HelpEntry(command, description, plugin, permissions);
        helpList.put(command, entry);
        return true;
    }

    public boolean registerCommand(String command, String description, Plugin plugin, int priority, String[] permissions) {
        HelpEntry entry = new HelpEntry(command, description, plugin, priority, permissions);
        helpList.put(command, entry);
        return true;
    }
}
