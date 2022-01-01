package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.util.Dice;
import de.brentspine.ttt.util.ItemBuilder;
import de.brentspine.ttt.util.Settings;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChestListener implements Listener {

    private Main plugin;
    private ItemStack woodenSword, stoneSword, ironSword, bow, arrows, enchantedBow;
    private final Integer normalItemAmount, enderItemAmount;

    public ChestListener(Main plugin) {
        this.plugin = plugin;

        //Normal chests
        woodenSword = new ItemBuilder(Material.WOODEN_SWORD).build();
        stoneSword = new ItemBuilder(Material.STONE_SWORD).build();
        bow = new ItemBuilder(Material.BOW).build();
        arrows = new ItemBuilder(Material.ARROW, Settings.arrowAmountChestLoot).build();

        normalItemAmount = 3;

        //Ender chests
        ironSword = new ItemBuilder(Material.IRON_SWORD).build();
        enchantedBow = new ItemBuilder(Material.BOW).setEnchantment(Enchantment.ARROW_DAMAGE, 1).build();

        enderItemAmount = 2;

    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent event) {
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) {
            event.setCancelled(true);
            return;
        }
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Material clickedMaterial = event.getClickedBlock().getType();
        if(clickedMaterial != Material.CHEST && clickedMaterial != Material.ENDER_CHEST) return;
        event.setCancelled(true);

        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 1f);
        if(clickedMaterial == Material.CHEST) {
            event.getClickedBlock().setType(Material.AIR);
            Integer random = Dice.generateNumberBetween(1, 100); //normalItemAmount
            if(random >= 50) { //50% Rarity
                player.getInventory().addItem(woodenSword);
                return;
            }
            if(random >= 20) { //30% Rarity
                player.getInventory().addItem(bow, arrows);
                return;
            }
            //20% Rarity
            player.getInventory().addItem(stoneSword);
            /*switch (random) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
            }*/
            return;
        }

        if(clickedMaterial == Material.ENDER_CHEST) {
            InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
            if(inGameState.isGrace()) {
                player.sendMessage(Main.PREFIX + "§cNoch " + inGameState.getRoleCountdown().getSeconds() + " Sekunden bis du diese Kiste öffnen darfst");
                return;
            }
            event.getClickedBlock().setType(Material.AIR);
            switch (Dice.generateNumberBetween(1, enderItemAmount)) {
                case 1:
                    player.getInventory().addItem(ironSword);
                    break;
                case 2:
                    player.getInventory().addItem(enchantedBow, arrows);
                    break;
            }
            return;
        }


    }

}
