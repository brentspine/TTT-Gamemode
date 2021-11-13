package de.brentspine.ttt.commands;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.LobbyState;
import de.brentspine.ttt.util.ConfigLocationUtil;
import de.brentspine.ttt.voting.Map;
import org.bukkit.Location;
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
        else if(args.length == 0) {
            player.sendMessage(Main.PREFIX + "§c/setup <lobby | addMap | removeMap | tpMap | modifyMap>");
        } else {
            if(args[0].equalsIgnoreCase("lobby")) {
                new ConfigLocationUtil(plugin, player.getLocation(), "lobby").saveLocation();
                player.sendMessage(Main.PREFIX + "The Lobby Spawn was set");
            }

            else if(args[0].equalsIgnoreCase("addMap")) {
                if(args.length < 3) {
                    player.sendMessage(Main.PREFIX + "§c/setup addMap <Name> <Builder>");
                    return true;
                }
                Map map = new Map(plugin, args[1], args[2]);
                if(map.exists()) {
                    player.sendMessage(Main.PREFIX + "§cA Map with the same name already exists!");
                    return true;
                }
                map.create();
                player.sendMessage(Main.PREFIX + "You added the Map §c" + args[1] + "§7 built by §c" + args[2]);
            }

            else if(args[0].equalsIgnoreCase("removeMap")) {
                if(args.length < 2) {
                    player.sendMessage(Main.PREFIX + "§c/setup removeMap <Name>");
                    return true;
                }
                Map map = new Map(plugin, args[1], null);
                if(map.exists()) {
                    map.remove();
                    player.sendMessage(Main.PREFIX + "The Map §c" + map.getName() + "§7 was removed");
                } else
                    player.sendMessage(Main.PREFIX + "§cThis Map does not exist!");
            }

            else if(args[0].equalsIgnoreCase("tpMap")) {
                if(args.length < 2) {
                    player.sendMessage(Main.PREFIX + "§c/setup tpMap <Name>");
                    return true;
                }
                Map map = new Map(plugin, args[1], null);
                if(!map.exists()) {
                    player.sendMessage(Main.PREFIX + "§cThis map does not exist!");
                    return true;
                }
                try {
                    player.teleport(map.getSpectatorSpawn());
                    player.sendMessage(Main.PREFIX + "Teleported you to Map §c" + map.getName() + "§7 built by §c" + map.getBuilder());
                } catch (Exception e) {
                    player.sendMessage(Main.PREFIX + "This map does not have a spectatorSpawn");
                    player.teleport(new Location(player.getWorld(), player.getLocation().getX(), 100, player.getLocation().getZ()));
                }
            }

            else if(args[0].equalsIgnoreCase("modifyMap")) {
                Map map;
                try {
                    map = new Map(plugin, args[1], null);
                    if(!map.exists()) {
                        player.sendMessage(Main.PREFIX + "§cThis map does not exist!");
                    }
                } catch (Exception e) {
                    player.sendMessage(Main.PREFIX + "§c/setup modifyMap <Map> <addSpawn|removeSpawn|spectatorSpawn>");
                    return true;
                }

                if(args.length < 3) {
                    player.sendMessage(Main.PREFIX + "§c/setup modifyMap <Map> <addSpawn|removeSpawn|spectatorSpawn>");
                    return true;
                }

                else if(args[2].equalsIgnoreCase("addSpawn")) {
                    if(args.length < 4) {
                        player.sendMessage(Main.PREFIX + "§c/setup modifyMap " + map.getName() + " addSpawn <spawnNumber [1-" + LobbyState.MAX_PLAYERS + "]> ");
                        return true;
                    }

                    int spawnNumber;

                    try {
                        spawnNumber = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                        player.sendMessage(Main.PREFIX + "§c/setup modifyMap " + map.getName() + " addSpawn <spawnNumber [1-" + LobbyState.MAX_PLAYERS + "]> ");
                        return true;
                    }

                    if(spawnNumber <= 0 || spawnNumber > LobbyState.MAX_PLAYERS) {
                        player.sendMessage(Main.PREFIX + "The SpawnNumber has to be between 1 and " + LobbyState.MAX_PLAYERS);
                        return true;
                    }

                    map.setSpawnLocation(spawnNumber, player.getLocation());
                    player.sendMessage(Main.PREFIX + "You set the Spawn-Location §c" + spawnNumber + "§7 for Map §c" + map.getName());
                }

                else if(args[2].equalsIgnoreCase("removeSpawn")) {
                    if(args.length < 4) {
                        player.sendMessage(Main.PREFIX + "§c/setup modifyMap " + map.getName() + " removeSpawn <spawnNumber [1-" + LobbyState.MAX_PLAYERS + "]> ");
                        return true;
                    }

                    int spawnNumber;

                    try {
                        spawnNumber = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                        player.sendMessage(Main.PREFIX + "§c/setup modifyMap " + map.getName() + " removeSpawn <spawnNumber [1-" + LobbyState.MAX_PLAYERS + "]> ");
                        return true;
                    }

                    if(spawnNumber <= 0 || spawnNumber > LobbyState.MAX_PLAYERS) {
                        player.sendMessage(Main.PREFIX + "The SpawnNumber has to be between 1 and " + LobbyState.MAX_PLAYERS);
                        return true;
                    }

                    map.removeSpawnLocation(spawnNumber);
                    player.sendMessage(Main.PREFIX + "You removed the Spawn-Location §c" + spawnNumber + "§7 for Map §c" + map.getName());
                    player.sendMessage(Main.PREFIX + "§cNote that you have to set all spawns!");
                }

                else if(args[2].equalsIgnoreCase("spectatorSpawn")) {
                    map.setSpectatorSpawn(player.getLocation());
                    player.sendMessage(Main.PREFIX + "You set the Spectator-Spawn for Map §c" + map.getName());
                }

                else
                    player.sendMessage(Main.PREFIX + "§c/setup modifyMap <Map> <addSpawn | removeSpawn | spectatorSpawn>");

            }

            else {
                player.sendMessage(Main.PREFIX + "§c/setup <lobby | addMap | removeMap | tpMap | modifyMap>");
            }
        }


        return false;
    }

}
