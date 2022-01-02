package de.brentspine.ttt.role;

import de.brentspine.ttt.util.ItemBuilder;
import de.brentspine.ttt.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RoleInventories implements Listener {

    private ItemStack traitorItem, detectiveItem;
    private Inventory traitorShop, detectiveShop;

    public RoleInventories() {
        traitorItem = new ItemBuilder(Material.BOW).setDisplayName(Settings.traitorShopTitle).setLore("§7Kaufe mit deinen Punkten spezielle Items").build();
        detectiveItem = new ItemBuilder(Material.NETHER_STAR).setDisplayName(Settings.detectiveShopTitle).setLore("§7Kaufe mit deinen Punkten spezielle Items").build();

        traitorShop = Bukkit.createInventory(null, 9*1, Settings.traitorShopTitle + " - undefined Punkte"); //todo
        detectiveShop = Bukkit.createInventory(null, 9*1, Settings.detectiveShopTitle + " - undefined Punkte");
        setInventories();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item.getItemMeta() == null) return;

        if(item.getItemMeta().getDisplayName().equalsIgnoreCase(Settings.traitorShopTitle)) {
            player.openInventory(traitorShop);
            return;
        }
        if(item.getItemMeta().getDisplayName().equalsIgnoreCase(Settings.detectiveShopTitle)) {
            player.openInventory(detectiveShop);
            return;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        ItemStack item = event.getCurrentItem();
        if(item == null) return;
        if(item.getItemMeta() == null) return;

        if(player.getOpenInventory().getTitle().startsWith(Settings.traitorShopTitle)) {
            event.setCancelled(true);
            if(item.getItemMeta().getDisplayName().equals(Settings.traitorCreeperItemName)) {
                player.getInventory().addItem(new ItemBuilder(Material.CREEPER_SPAWN_EGG, 3).setDisplayName(Settings.traitorCreeperItemName).setLore("§7Ein normaler Creeper, der keine Blöcke zerstört").build());
                return;
            }

            if(item.getItemMeta().getDisplayName().equals(Settings.traitorTestFakerItemName)) {
                player.getInventory().addItem(new ItemBuilder(Material.GREEN_STAINED_GLASS).setDisplayName(Settings.traitorTestFakerItemName).setLore("§7" + Settings.traitorTestFakerItemSuccessRatePercent + "% Warscheinlichkeit im Tester als Innocent erkannt zu werden," + "§7wenn im Inventar").build());
                return;
            }

        }
        if(player.getOpenInventory().getTitle().startsWith(Settings.detectiveShopTitle)) {
            event.setCancelled(true);
            if(item.getItemMeta().getDisplayName().equals(Settings.detectiveHealingStationItemName)) {
                player.getInventory().addItem(new ItemBuilder(Material.BEACON).setDisplayName(Settings.detectiveHealingStationItemName).setLore("§7Heilt alle Spieler im Umkreis").build());
                return;
            }
        }
    }

    private void setInventories() {
        traitorShop.setItem(3, new ItemBuilder(Material.CREEPER_SPAWN_EGG, 3).setDisplayName(Settings.traitorCreeperItemName).setLore("§73 Creeper Spawn Eggs").build());
        traitorShop.setItem(5, new ItemBuilder(Material.GREEN_STAINED_GLASS).setDisplayName(Settings.traitorTestFakerItemName).setLore("§7Gibt eine " + Settings.traitorTestFakerItemSuccessRatePercent + "% Wahrscheinlichkeit", "§7im Tester als Innocent erkannt zu werden, ", "§7wenn es im Inventar des Spielers ist").build());

        detectiveShop.setItem(4, new ItemBuilder(Material.BEACON).setDisplayName(Settings.detectiveHealingStationItemName).setLore("§7Heilt alle Spieler die sich in der Nähe", "§7dieser befinden").build());
    }



    public ItemStack getTraitorItem() {
        return traitorItem;
    }

    public ItemStack getDetectiveItem() {
        return detectiveItem;
    }

    public Inventory getTraitorShop() {
        return traitorShop;
    }

    public Inventory getDetectiveShop() {
        return detectiveShop;
    }

}
