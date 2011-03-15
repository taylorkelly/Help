package me.taylorkelly.help;

import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Help extends JavaPlugin {

    private String name;
    private String version;
    private HelpList helpList;

    public Help() {
        helpList = new HelpList();
        File folder = new File("plugins", "Help");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, "ExtraHelp");
        if (file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        name = this.getDescription().getName();
        version = this.getDescription().getVersion();

        LegacyHelpLoader.load(this.getDataFolder(), helpList);
        HelpLoader.load(this.getDataFolder(), helpList);

        HelpPermissions.initialize(getServer());
        HelpSettings.initialize(getDataFolder());

        this.registerCommand("help Help", "Displays more /help options", this, true);
        this.registerCommand("help", "Displays the basic Help menu", this);
        this.registerCommand("help [plugin]", "Displays the full help for [plugin]", this, true);
        this.registerCommand("help plugins", "Show all the plugins with Help entries", this);
        this.registerCommand("help search [query]", "Search the help entries for [query]", this);
        this.registerCommand("help reload", "Reload the ExtraHelp.yml entries", this);

        HelpLogger.info(name + " " + version + " enabled");
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String[] split = args;
        String commandName = command.getName().toLowerCase();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (commandName.equals("help")) {
                /**
                 * /help (#)
                 */
                if (split.length == 0 || (split.length == 1 && isInteger(split[0]))) {
                    Lister lister = new Lister(helpList, player);
                    if (split.length == 1) {
                        int page = Integer.parseInt(split[0]);
                        if (page < 1) {
                            player.sendMessage(ChatColor.RED + "Page number can't be below 1.");
                            return true;
                        } else if (page > lister.getMaxPages(player)) {
                            player.sendMessage(ChatColor.RED + "There are only " + lister.getMaxPages(player) + " pages of help");
                            return true;
                        }
                        lister.setPage(page);
                    } else {
                        lister.setPage(1);
                    }
                    lister.list();

                    /**
                     * /help plugins
                     */
                } else if (split.length == 1 && split[0].equalsIgnoreCase("plugins")) {
                    helpList.listPlugins(player);

                    /**
                     * /help reload
                     */
                } else if (split.length == 1 && split[0].equalsIgnoreCase("reload")) {
                    helpList.reload(player, getDataFolder());

                    /**
                     * /help search [query]
                     */
                } else if (split.length > 1 && split[0].equalsIgnoreCase("search")) {
                    String name = "";
                    for (int i = 1; i < split.length; i++) {
                        name += split[i];
                        if (i + 1 < split.length) {
                            name += " ";
                        }
                    }
                    Searcher searcher = new Searcher(helpList);
                    searcher.addPlayer(player);
                    searcher.setQuery(name);
                    searcher.search();

                    /**
                     * /help [plugin] (#)
                     */
                } else if (split.length == 1 || (split.length == 2 && isInteger(split[1]))) {
                    Lister lister = new Lister(helpList, split[0], player);
                    if (split.length == 2) {
                        int page = Integer.parseInt(split[1]);
                        if (page < 1) {
                            player.sendMessage(ChatColor.RED + "Page number can't be below 1.");
                            return true;
                        } else if (page > lister.getMaxPages(player)) {
                            player.sendMessage(ChatColor.RED + "There are only " + lister.getMaxPages(player) + " pages of help");
                            return true;
                        }
                        lister.setPage(page);
                    } else {
                        lister.setPage(1);
                    }
                    lister.list();
                } else {
                    return false;
                }
                return true;
            }
        } //TODO Console help
        return false;
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean registerCommand(String command, String description, Plugin plugin) {
        return helpList.registerCommand(command, description, plugin.getDescription().getName(), false, new String[]{}, this.getDataFolder());
    }

    public boolean registerCommand(String command, String description, Plugin plugin, boolean main) {
        return helpList.registerCommand(command, description, plugin.getDescription().getName(), main, new String[]{}, this.getDataFolder());
    }

    public boolean registerCommand(String command, String description, Plugin plugin, String... permissions) {
        return helpList.registerCommand(command, description, plugin.getDescription().getName(), false, permissions, this.getDataFolder());
    }

    public boolean registerCommand(String command, String description, Plugin plugin, boolean main, String... permissions) {
        return helpList.registerCommand(command, description, plugin.getDescription().getName(), main, permissions, this.getDataFolder());
    }

    public enum HelpReciever {

        PLAYER, CONSOLE;
    }
}
