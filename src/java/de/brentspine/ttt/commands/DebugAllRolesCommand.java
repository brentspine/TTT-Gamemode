package de.brentspine.ttt.commands;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.role.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugAllRolesCommand implements CommandExecutor {

    private Main plugin;

    public DebugAllRolesCommand(Main plugin) {
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
        String message = "";
        for(Player current : plugin.getPlayers()) {
            Role role = plugin.getRoleManager().getPlayerRole(current);
            message = message + current.getName() + ": " + role.getChatColor() + role.getName() + "\n";
        }
        player.sendMessage(message);
        return false;
    }

}
