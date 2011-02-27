package me.taylorkelly.help;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HelpList {

    private HashMap<String, HelpEntry> mainHelpList;
    private HashMap<String, HashMap<String, HelpEntry>> pluginHelpList;

    HelpList() {
        mainHelpList = new HashMap<String, HelpEntry>();
        pluginHelpList = new HashMap<String, HashMap<String, HelpEntry>>();
    }

    public ArrayList<HelpEntry> getSortedHelp(Player player, int start, int size) {
        ArrayList<HelpEntry> ret = new ArrayList<HelpEntry>();
        List<String> names = new ArrayList<String>(mainHelpList.keySet());
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.SECONDARY);
        Collections.sort(names, collator);

        int index = 0;
        int currentCount = 0;
        while (index < names.size() && ret.size() < size) {
            String currName = names.get(index);
            HelpEntry entry = mainHelpList.get(currName);
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
        return mainHelpList.size();
    }

    public double getMaxEntries(Player player) {
        int count = 0;
        for (HelpEntry entry : mainHelpList.values()) {
            if (entry.playerCanUse(player)) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<HelpEntry> getSortedHelp(Player player, int start, int size, String plugin) {
        ArrayList<HelpEntry> ret = new ArrayList<HelpEntry>();
        if (!pluginHelpList.containsKey(plugin)) {
            return ret;
        } else {
            List<String> names = new ArrayList<String>(pluginHelpList.get(plugin).keySet());
            Collator collator = Collator.getInstance();
            collator.setStrength(Collator.SECONDARY);
            Collections.sort(names, collator);

            int index = 0;
            int currentCount = 0;
            while (index < names.size() && ret.size() < size) {
                String currName = names.get(index);
                HelpEntry entry = pluginHelpList.get(plugin).get(currName);
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
    }

    public int getSize(String plugin) {
        if (pluginHelpList.containsKey(plugin)) {
            return pluginHelpList.get(plugin).size();
        } else {
            return 0;
        }
    }

    public double getMaxEntries(Player player, String plugin) {
        if (pluginHelpList.containsKey(plugin)) {
            int count = 0;
            for (HelpEntry entry : pluginHelpList.get(plugin).values()) {
                if (entry.playerCanUse(player)) {
                    count++;
                }
            }
            return count;
        } else {
            return 0;
        }
    }

    public boolean registerCommand(String command, String description, Plugin plugin) {
        HelpEntry entry = new HelpEntry(command, description, plugin);
        saveEntry(plugin, entry);
        return true;
    }

    public boolean registerCommand(String command, String description, Plugin plugin, boolean main) {
        HelpEntry entry = new HelpEntry(command, description, plugin, main);
        if (main) {
            mainHelpList.put(command, entry);
        }
        saveEntry(plugin, entry);
        return true;
    }

    public boolean registerCommand(String command, String description, Plugin plugin, String[] permissions) {
        HelpEntry entry = new HelpEntry(command, description, plugin, permissions);
        saveEntry(plugin, entry);
        return true;
    }

    public boolean registerCommand(String command, String description, Plugin plugin, boolean main, String[] permissions) {
        HelpEntry entry = new HelpEntry(command, description, plugin, main, permissions);
        if (main) {
            mainHelpList.put(command, entry);
        }
        saveEntry(plugin, entry);
        return true;
    }

    private void saveEntry(Plugin plugin, HelpEntry entry) {
        String name = plugin.getDescription().getName();
        if (pluginHelpList.containsKey(name)) {
            pluginHelpList.get(name).put(entry.command, entry);
        } else {
            HashMap<String, HelpEntry> map = new HashMap<String, HelpEntry>();
            map.put(entry.command, entry);
            pluginHelpList.put(name, map);
        }
    }
}
