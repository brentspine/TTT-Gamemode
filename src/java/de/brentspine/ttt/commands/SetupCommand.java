package de.brentspine.ttt.commands;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.ConfigLocationUtil;
import de.brentspine.ttt.voting.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class SetupCommand implements CommandExecutor {

    private Main plugin;

    public SetupCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.PREFIX + "This command is not available in the console");
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("ttt.setup")) {
            player.sendMessage(Main.NO_PERMISSION);
        }
        if(args.length == 0) {
            player.sendMessage(Main.PREFIX + "§cUsage: /setup <lobby|map>");
        } else
            if(args[0].equalsIgnoreCase("lobby")) {
                new ConfigLocationUtil(plugin, player.getLocation(), "lobby").saveLocation();
                player.sendMessage(Main.PREFIX + "The Lobby Spawn was set");
            }
            else if(args[0].equalsIgnoreCase("map")) {
                if(args.length <= 1) {
                    player.sendMessage(Main.PREFIX + "§cUsage: §7/setup map <add|remove> <args>");
                    player.sendMessage(Main.PREFIX + "§cMore help: §7/setup map <add|remove>");
                }
                else if(args[1].equalsIgnoreCase("add")) {
                    if(args.length < 4) {
                        player.sendMessage(Main.PREFIX + "§c/setup map add <Name> <Builder>");
                        return true;
                    }
                    Map map = new Map(plugin, args[2], args[3]);
                    if(map.exists()) {
                        player.sendMessage(Main.PREFIX + "§cA Map with the same name already exists!");
                        return true;
                    }
                    map.create();
                    player.sendMessage(Main.PREFIX + "You added the Map §c" + args[2] + "§7 built by §c" + args[3]);
                }
                else if(args[1].equalsIgnoreCase("remove")) {
                    if(args.length < 3) {
                        player.sendMessage(Main.PREFIX + "§c/setup map remove <Name>");
                        return true;
                    }
                    Map map = new Map(plugin, args[2], null);
                    if(map.exists()) {
                        map.remove();
                        player.sendMessage(Main.PREFIX + "The Map §c" + map.getName() + "§7 was removed");
                    } else
                        player.sendMessage(Main.PREFIX + "§cThis Map does not exist!");
                }
                else {
                    player.sendMessage(Main.PREFIX + "§cUsage: §7/setup map <add|remove> <args>");
                    player.sendMessage(Main.PREFIX + "§cMore help: §7/setup map <add|remove>");
                    return true;
                }
            }

        return false;
    }

}
