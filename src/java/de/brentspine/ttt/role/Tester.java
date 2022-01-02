package de.brentspine.ttt.role;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.util.ConfigLocationUtil;
import de.brentspine.ttt.util.Settings;
import de.brentspine.ttt.voting.Map;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Tester {

    private Main plugin;
    private Map map;

    private Block[] borderBlocks, lamps;
    private Block button;
    private Location blockedTesterLocation; //The Location the players in the tester get teleported to when it's used by another player
    private Location playerLocation;
    private boolean inUse;
    private World world;

    public Tester(Main plugin, Map map) {
        this.plugin = plugin;
        this.map = map;
        borderBlocks = new Block[3];
        lamps = new Block[2];
    }

    public void test(Player player) {
        Role role = plugin.getRoleManager().getPlayerRole(player);
        if(inUse) {
            player.sendMessage(Main.PREFIX + "§cDer Tester wird bereits verwendet");
            return;
        }
        if(role == Role.DETECTIVE) {
            player.sendMessage(Main.PREFIX + "§cAls Detectives kannst du den Tester nicht benutzen"); //Achievement?
            return;
        }
        if(((InGameState) plugin.getGameStateManager().getCurrentGameState()).getSpectators().contains(player)) {
            player.sendMessage(Main.PREFIX + "§cAls Spectator kannst du den Tester nicht benutzen");
            return;
        }

        //Bukkit.broadcastMessage(Main.PREFIX + "§7Ein Spieler hat den §cTester §7betreten");
        inUse = true;
        for(Entity current : player.getNearbyEntities(4, 2, 4)) {
            if(current instanceof Player)
                ((Player) current).teleport(blockedTesterLocation);
        }

        player.teleport(((InGameState) plugin.getGameStateManager().getCurrentGameState()).getMap().getTester().getPlayerLocation());

        for(Block current : borderBlocks)
            world.getBlockAt(current.getLocation()).setType(Settings.testerBorderBlockMaterial);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                endTesting(role);
            }
        }, Settings.testingDelayTicks);
    }

    private void endTesting(Role role) {
        for(Block current : lamps)
            world.getBlockAt(current.getLocation()).setType((role == Role.INNOCENT) ? Settings.testerResultInnocentLampMaterial : Settings.testerResultTraitorLampMaterial);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, Settings.testingDelayAfterTestTicks);
    }

    public void load() {
        try {
            for (int i = 0; i < borderBlocks.length; i++) {
                borderBlocks[i] = new ConfigLocationUtil(plugin, "maps." + map.getName() + ".tester.borderblocks." + i).loadBlockLocation();
            }
            for (int i = 0; i < lamps.length; i++) {
                lamps[i] = new ConfigLocationUtil(plugin, "maps." + map.getName() + ".tester.lamps." + i).loadBlockLocation();
            }
            button = new ConfigLocationUtil(plugin, "maps." + map.getName() + ".tester.button").loadBlockLocation();
            playerLocation = new ConfigLocationUtil(plugin, "maps." + map.getName() + ".tester.playerlocation").loadLocation();
            blockedTesterLocation = new ConfigLocationUtil(plugin, "maps." + map.getName() + ".tester.blockedtester").loadLocation();

            world = map.getSpawnLocations()[0].getWorld();
            reset();

        } catch (NullPointerException e) {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4Ein Tester auf der Map §c" + map.getName() + "§4 wurde nicht richtig aufgesetzt");
        }

    }

    public void reset() {
        inUse = false;
        for(Block current : borderBlocks)
            world.getBlockAt(current.getLocation()).setType(Material.AIR);
        for(Block current : lamps)
            world.getBlockAt(current.getLocation()).setType(Settings.testerInactiveLampMaterial);
    }

    public boolean exists() {
        //return new ConfigLocationUtil(plugin, "maps." + map.getName() + ".tester.blockedtester").exists();
        //return new ConfigLocationUtil(plugin).config.isConfigurationSection("maps." + map.getName() + ".tester");
        return new ConfigLocationUtil( plugin, "maps." + map.getName() + ".tester.playerlocation.world").exists();
    }

    public Map getMap() {
        return map;
    }

    public Block[] getBorderBlocks() {
        return borderBlocks;
    }

    public Block[] getLamps() {
        return lamps;
    }

    public Block getButton() {
        return button;
    }

    public Location getBlockedTesterLocation() {
        return blockedTesterLocation;
    }

    public Location getPlayerLocation() {
        return playerLocation;
    }

    public boolean isInUse() {
        return inUse;
    }

    public World getWorld() {
        return world;
    }

}
