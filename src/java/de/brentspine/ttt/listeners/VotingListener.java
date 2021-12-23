package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.Settings;
import de.brentspine.ttt.voting.Voting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class VotingListener implements Listener {

    private Main plugin;
    private Voting voting;

    public VotingListener(Main plugin) {
        this.plugin = plugin;
        voting = plugin.getVoting();
    }

    @EventHandler
    public void onVotingMenuOpen(PlayerInteractEvent event) {
        if(!(event.getAction() == Action.RIGHT_CLICK_AIR) && !(event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if(item == null) return;
        if(item.getItemMeta().getDisplayName().equals(Settings.votingItemName))
            player.openInventory(voting.getVotingInventory());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if(!player.getOpenInventory().getTitle().equals(Settings.votingInventoryTitle)) return;
        event.setCancelled(true);
        for (int i = 0; i < voting.getVotingInventoryOrder().length; i++) {
            if(voting.getVotingInventoryOrder()[i] == event.getSlot()) {
                voting.vote(player, i);
                return;
            }
        }
    }

}
