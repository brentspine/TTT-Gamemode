package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.EndingState;
import de.brentspine.ttt.gamestates.GameState;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.gamestates.LobbyState;
import de.brentspine.ttt.util.Settings;
import net.minecraft.server.v1_16_R3.PacketPlayInClientCommand;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class BlockedListeners implements Listener {

    public Main plugin;

    public BlockedListeners(Main plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        GameState gameState = plugin.getGameStateManager().getCurrentGameState();
        ItemStack itemStack = event.getItemDrop().getItemStack();

        if(gameState instanceof LobbyState) {
            event.setCancelled(true);
            return;
        }
        if(gameState instanceof EndingState) {
            event.setCancelled(true);
            return;
        }

        if(Settings.allowDropItems) return; //Man darf Items droppen

        if(itemStack.getItemMeta() == null) {
            event.setCancelled(true);
            return;
        }

        if(gameState instanceof InGameState) {
            if(!Settings.allowDropRoleItems) {
                Material material = itemStack.getType();
                if(material == Material.LEATHER_CHESTPLATE || material == Material.STICK ||
                        (material == Material.BOW && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase( Settings.traitorBowName ))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;
        if(event.getCurrentItem().getType() == Material.LEATHER_CHESTPLATE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBowShot(EntityShootBowEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) {
            event.setCancelled(true);
            return;
        }

        InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
        if(inGameState.isGrace()) {
            event.setCancelled(true);
            event.getEntity().sendMessage(Main.PREFIX + "§cPvP ist deaktiviert bis die Rollen verteilt wurden");
        }

        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if(inGameState.getSpectators().contains(player)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) {
            event.setCancelled(true);
            return;
        }
        InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
        if(inGameState.isGrace()) {
            event.setCancelled(true);
            event.getDamager().sendMessage(Main.PREFIX + "§cPvP ist deaktiviert bis die Rollen verteilt wurden");
            return;
        }
        if(inGameState.getSpectators().contains(event.getDamager())) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        Player player = event.getEntity();
        PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
        ((CraftPlayer) player).getHandle().playerConnection.a(packet);
        player.spigot().respawn();

        player.getInventory().setChestplate(null);

        if(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState) {
            InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
            inGameState.addSpectator(player);
        } else
            player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) {
            event.setCancelled(true);
            return;
        }

        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
        if(inGameState.getSpectators().contains(player)) {
            event.setCancelled(true);
            return;
        }
    }




}
