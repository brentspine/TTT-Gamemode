package de.brentspine.ttt.role;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.ConfigLocationUtil;
import de.brentspine.ttt.voting.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Tester {

    private Main plugin;
    private Map map;

    private Block[] borderBlocks, lamps;
    private Block button;
    private Location blockedTesterLocation; //The Location the players in the tester get teleported to when it's used by another player
    private Location playerLocation;
    private boolean inUse;

    public Tester(Main plugin, Map map) {
        this.plugin = plugin;
        this.map = map;
        borderBlocks = new Block[3];
        lamps = new Block[2];
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
        } catch (NullPointerException e) {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4Ein Tester auf der Map §c" + map.getName() + "§4 wurde nicht richtig aufgesetzt");
        }

    }

    public boolean exists() {
        //return new ConfigLocationUtil(plugin, "maps." + map.getName() + ".tester.blockedtester").exists();
        return new ConfigLocationUtil(plugin).config.isConfigurationSection("maps." + map.getName() + ".tester");
    }

}
