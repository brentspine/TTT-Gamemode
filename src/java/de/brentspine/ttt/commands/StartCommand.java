package de.brentspine.ttt.commands;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.countdowns.Countdown;
import de.brentspine.ttt.gamestates.LobbyState;
import de.brentspine.ttt.util.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private static final int START_SECONDS = 5;

    private Main plugin;

    public StartCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.PREFIX + "You are not allowed to execute this command from the console!");
            return true;
        }

        Player player = (Player) sender;
        if(!player.hasPermission("ttt.start")) {
            player.sendMessage(Main.NO_PERMISSION);
            return true;
        }

        if(args.length > 0) {

            return true;
        }

        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) {
            player.sendMessage(Main.PREFIX + "§cThe Game already started!");
            return true;
        }

        LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
        if(lobbyState.getCountdown().getSeconds() <= START_SECONDS) {
            player.sendMessage(Main.PREFIX + "§cThe Game is already starting in " + lobbyState.getCountdown().getSeconds() + " seconds");
            return true;
        }

        if(!lobbyState.getCountdown().isRunning()) {
            player.sendMessage(Main.PREFIX + "§cThere are not enough players to start the Game");
            return true;
        }

        lobbyState.getCountdown().setSeconds(Settings.COUNTDOWN_START_COMMAND_SECONDS);
        //player.sendMessage(Main.PREFIX + "Starting the Game in " + START_SECONDS + " seconds");

        return false;
    }
}
