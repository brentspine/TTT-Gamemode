package de.brentspine.ttt.commands;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.ConfigLocationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            player.sendMessage(Main.PREFIX + "§cUsage: /setup <Lobby>");
        } else
            if(args[0].equalsIgnoreCase("lobby")) {
                new ConfigLocationUtil(plugin, player.getLocation(), "lobby").saveLocation();
                player.sendMessage(Main.PREFIX + "The Lobby Spawn was set");
            }
        return false;
    }

}
