package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.role.HealingStation;
import de.brentspine.ttt.role.Role;
import de.brentspine.ttt.role.RoleInventories;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ShopItemListener implements Listener {

    private Main plugin;

    public ShopItemListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreeperArrowHit(ProjectileHitEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player)) return;
        if(event.getEntity().getType() != EntityType.ARROW) return;
        Player player = (Player) event.getEntity().getShooter();
        if(plugin.getRoleManager().getPlayerRole(player) != Role.TRAITOR) return;

        if(RoleInventories.removeMaterialItem(player, Material.GUNPOWDER)) {
            World world = event.getEntity().getWorld();
            world.spawnEntity(event.getEntity().getLocation(), EntityType.CREEPER);
            event.getEntity().remove();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHealingStationPlace(BlockPlaceEvent event) {
        if(event.getBlock().getType() != Material.BEACON) return;
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) return;
        Player player = event.getPlayer();
        if(plugin.getRoleManager().getPlayerRole(player) != Role.DETECTIVE) return;

        new HealingStation(plugin, event.getBlock().getLocation());
        event.setCancelled(false);
    }

}
