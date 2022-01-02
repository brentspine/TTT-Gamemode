package de.brentspine.ttt.commands;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCurrentMapCommand implements CommandExecutor {

    private Main plugin;

    public DebugCurrentMapCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(!player.hasPermission("ttt.debug")) {
            player.sendMessage(Main.NO_PERMISSION);
            return true;
        }
        player.sendMessage(Main.PREFIX + ((InGameState) plugin.getGameStateManager().getCurrentGameState()).getMap().getName());
        return false;
    }

}
