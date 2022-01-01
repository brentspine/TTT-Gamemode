package de.brentspine.ttt.util;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.voting.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.PluginManager;

public class TesterSetup implements Listener {

    private Main plugin;
    private Player player;
    private Map map;
    private int phase;
    private boolean finished;

    private Block[] borderBlocks, lamps;
    private Block button;
    private Location playerLocation; //The position the player that gets tested gets teleported to
    private Location blockedTesterLocation; //The Location the players in the tester get teleported to when it's used by another player

    public TesterSetup(Main plugin, Map map, Player player) {
        this.plugin = plugin;
        this.map = map;
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        phase = 1;
        finished = false;

        borderBlocks = new Block[3];
        lamps = new Block[2];

        //startSetup();
    }

    public void startPhase(int phase) {
        switch (phase) {
            case 1:
            case 2:
            case 3:
                player.sendMessage(Main.PREFIX + "§7Klicke den nächsten §cTester-Begrenzungsblock §7an (" + phase + "/3)");
                break;
            case 4:
            case 5:
                player.sendMessage(Main.PREFIX + "§7Klicke die nächste §cTester-Lampe §7an (" + (phase - 3) + "/2)");
                break;
            case 6:
                player.sendMessage(Main.PREFIX + "§7Klicke nun den §cTester-Startknopf §7an (1/1)");
                break;
            case 7:
                player.sendMessage(Main.PREFIX + "§7Bitte §cSneake §7an §cder Position §7an die §cder zu Testende Spieler §7teleportiert werden soll (1/1)");
                break;
            case 8:
                player.sendMessage(Main.PREFIX + "§7Bitte §csneake §7an dem Ort an den die §cSpieler im Tester teleportiert §7werden sollen, wenn dieser §cvon einem anderen Spieler benutzt §7wird (1/1)");
                break;
            case 9:
                finishSetup();
                finished = true; //To avoid errors marked by the IDE
                break;
            default:
                finishSetup();
                break;

        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(!event.getPlayer().getName().equalsIgnoreCase(player.getName())) return;
        if(finished) return;
        switch (phase) {
            case 1:
            case 2:
            case 3:
                event.setCancelled(true);
                borderBlocks[phase - 1] = event.getBlock();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                phase++;
                startPhase(phase);
                break;
            case 4:
            case 5:
                event.setCancelled(true);
                if(event.getBlock().getType() == Material.GLASS) {
                    lamps[phase - 4] = event.getBlock();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    phase++;
                    startPhase(phase);
                } else {
                    player.sendMessage("§cDie Lampe muss aus normalem Glas sein");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 1f);
                }

                break;
            case 6:
                event.setCancelled(true);
                if(event.getBlock().getType() == Material.STONE_BUTTON || event.getBlock().getType() == Material.OAK_BUTTON) {
                    button = event.getBlock();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    phase++;
                    startPhase(phase);
                } else {
                    player.sendMessage("§cDer Knopf muss aus Stein oder Eichenholz sein");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 1f);
                }
                break;
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if(player.getName().equalsIgnoreCase(event.getPlayer().getName())) {
            if(finished) return;
            if(!event.isSneaking()) return;
            switch (phase) {
                case 7:
                    playerLocation = player.getLocation();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    phase++;
                    startPhase(phase);
                    break;
                case 8:
                    blockedTesterLocation = player.getLocation();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    phase++;
                    startPhase(phase);
                    break;
            }
        }
    }

    public void finishSetup() {
        player.sendMessage(Main.PREFIX + "§7Die Eingabe wurde abgeschlossen, §cbereite Speicherung vor");
        for (int i = 0; i < borderBlocks.length; i++) {
            new ConfigLocationUtil(plugin, borderBlocks[i].getLocation(), "maps." + map.getName() + ".tester.borderblocks." + i).saveBlockLocation();
        }
        for (int i = 0; i < lamps.length; i++) {
            new ConfigLocationUtil(plugin, lamps[i].getLocation(), "maps." + map.getName() + ".tester.lamps." + i).saveBlockLocation();
        }
        new ConfigLocationUtil(plugin, button.getLocation(), "maps." + map.getName() + ".tester.button").saveBlockLocation();
        new ConfigLocationUtil(plugin, playerLocation, "maps." + map.getName() + ".tester.playerlocation").saveLocation();
        new ConfigLocationUtil(plugin, blockedTesterLocation, "maps." + map.getName() + ".tester.blockedtester").saveLocation();
        player.sendMessage(Main.PREFIX + "§7Speicherung abgeschlossen");
        player.sendMessage( Main.PREFIX + "§aDas Tester-Setup wurde abgeschlossen!");
        finished = true;
    }

    public void startSetup() {
        player.sendMessage(Main.PREFIX + "§aDas Tester-Setup wurde gestartet");
        startPhase(phase);
    }

}
