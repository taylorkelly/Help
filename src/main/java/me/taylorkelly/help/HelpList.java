package me.taylorkelly.help;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.ChatColor;

import org.bukkit.entity.Player;

public class HelpList {

    private HashMap<String, HelpEntry> mainHelpList;
    private HashMap<String, HashMap<String, HelpEntry>> pluginHelpList;
    private LinkedList<HelpEntry> savedList;

    HelpList() {
        mainHelpList = new HashMap<String, HelpEntry>();
        savedList = new LinkedList<HelpEntry>();
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
        if (!pluginHelpList.containsKey(plugin.toLowerCase())) {
            return ret;
        } else {
            List<String> names = new ArrayList<String>(pluginHelpList.get(plugin.toLowerCase()).keySet());
            Collator collator = Collator.getInstance();
            collator.setStrength(Collator.SECONDARY);
            Collections.sort(names, collator);

            int index = 0;
            int currentCount = 0;
            while (index < names.size() && ret.size() < size) {
                String currName = names.get(index);
                HelpEntry entry = pluginHelpList.get(plugin.toLowerCase()).get(currName);
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

    public MatchList getMatches(String query, Player player) {
        ArrayList<HelpEntry> commandMatches = new ArrayList<HelpEntry>();
        ArrayList<HelpEntry> pluginExactMatches = new ArrayList<HelpEntry>();
        ArrayList<HelpEntry> pluginPartialMatches = new ArrayList<HelpEntry>();
        ArrayList<HelpEntry> descriptionMatches = new ArrayList<HelpEntry>();

        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.SECONDARY);

        List<String> plugins = new ArrayList<String>(pluginHelpList.keySet());
        Collections.sort(plugins, collator);

        for (int i = 0; i < plugins.size(); i++) {
            String pluginName = plugins.get(i);
            HashMap<String, HelpEntry> pluginSet = pluginHelpList.get(pluginName);
            List<String> commands = new ArrayList<String>(pluginSet.keySet());
            Collections.sort(commands, collator);
            for (int j = 0; j < commands.size(); j++) {
                String command = commands.get(j);
                HelpEntry entry = pluginSet.get(command);
                if (entry.playerCanUse(player)) {
                    //TODO Separate word matching
                    if (pluginName.equalsIgnoreCase(query)) {
                        pluginExactMatches.add(entry);
                    } else if (pluginName.toLowerCase().contains(query.toLowerCase())) {
                        pluginPartialMatches.add(entry);
                    }
                    if (entry.description.toLowerCase().contains(query.toLowerCase())) {
                        descriptionMatches.add(entry);
                    }
                    if (entry.command.toLowerCase().contains(query.toLowerCase())) {
                        commandMatches.add(entry);
                    }
                }
            }
        }

        return new MatchList(commandMatches, pluginExactMatches, pluginPartialMatches, descriptionMatches);
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

    public boolean registerCommand(String command, String description, String plugin) {
        HelpEntry entry = new HelpEntry(command, description, plugin);
        saveEntry(plugin, entry);
        return true;
    }

    public boolean registerCommand(String command, String description, String plugin, boolean main) {
        HelpEntry entry = new HelpEntry(command, description, plugin, main);
        if (main) {
            mainHelpList.put(command, entry);
        }
        saveEntry(plugin, entry);
        return true;
    }

    public boolean registerCommand(String command, String description, String plugin, String[] permissions) {
        HelpEntry entry = new HelpEntry(command, description, plugin, permissions);
        saveEntry(plugin, entry);
        return true;
    }

    public boolean registerCommand(String command, String description, String plugin, boolean main, String[] permissions) {
        HelpEntry entry = new HelpEntry(command, description, plugin, main, permissions);
        if (main) {
            mainHelpList.put(command, entry);
        }
        saveEntry(plugin, entry);
        return true;
    }

    public boolean customRegisterCommand(String command, String description, String plugin, boolean main, String[] permissions) {
        HelpEntry entry = new HelpEntry(command, description, plugin, main, permissions);
        if (main) {
            mainHelpList.put(command, entry);
        }
        customSaveEntry(plugin, entry);
        return true;
    }

    private void saveEntry(String plugin, HelpEntry entry) {
        customSaveEntry(plugin, entry);
        permaSaveEntry(entry);
    }

    private void customSaveEntry(String plugin, HelpEntry entry) {
        if (pluginHelpList.containsKey(plugin.toLowerCase())) {
            pluginHelpList.get(plugin.toLowerCase()).put(entry.command, entry);
        } else {
            HashMap<String, HelpEntry> map = new HashMap<String, HelpEntry>();
            map.put(entry.command, entry);
            pluginHelpList.put(plugin.toLowerCase(), map);
        }
    }

    public void listPlugins(Player player) {
        StringBuilder list = new StringBuilder();
        for (String plugin : pluginHelpList.keySet()) {
            list.append(ChatColor.RED.toString());
            list.append(plugin);
            list.append(ChatColor.WHITE.toString());
            list.append(", ");
        }
        list.delete(list.length() - 2, list.length());
        player.sendMessage(ChatColor.AQUA + "Plugins with Help entries:");
        player.sendMessage(list.toString());
    }

    public void reload(Player player, File dataFolder) {
        mainHelpList = new HashMap<String, HelpEntry>();
        pluginHelpList = new HashMap<String, HashMap<String, HelpEntry>>();

        HelpLoader.load(dataFolder, this);

        for (HelpEntry entry : savedList) {
            customSaveEntry(entry.plugin, entry);
        }

        player.sendMessage(ChatColor.AQUA + "Successfully reloaded Help");
    }

    private void permaSaveEntry(HelpEntry entry) {
        savedList.add(entry);
    }
}
