package de.brentspine.ttt.role;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.util.ItemBuilder;
import de.brentspine.ttt.util.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class RoleInventories implements Listener {

    private Main plugin;

    private ItemStack traitorItem, detectiveItem;

    private PointManager pointManager;
    public final String NOT_ENOUGH_POINTS = Main.PREFIX + "§cNicht genügend Punkte";

    public RoleInventories(Main plugin) {
        this.plugin = plugin;
        traitorItem = new ItemBuilder(Material.BOW).setDisplayName(Settings.TRAITOR_SHOP_TITLE).setLore("§7Kaufe mit deinen Punkten spezielle Items").build();
        detectiveItem = new ItemBuilder(Material.NETHER_STAR).setDisplayName(Settings.DETECTIVE_SHOP_TITLE).setLore("§7Kaufe mit deinen Punkten spezielle Items").build();

        pointManager = new PointManager();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item.getItemMeta() == null) return;

        if(item.getItemMeta().getDisplayName().equalsIgnoreCase(Settings.TRAITOR_SHOP_TITLE)) {
            event.setCancelled(true);
            openTraitorShop(player);
            player.updateInventory();
            return;
        }
        if(item.getItemMeta().getDisplayName().equalsIgnoreCase(Settings.DETECTIVE_SHOP_TITLE)) {
            event.setCancelled(true);
            openDetectiveShop(player);
            player.updateInventory();
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

        if(player.getOpenInventory().getTitle().startsWith(Settings.TRAITOR_SHOP_TITLE)) {
            event.setCancelled(true);
            if(item.getItemMeta().getDisplayName().startsWith(Settings.TRAITOR_CREEPER_ARROW_ITEM_NAME)) {
                if(!pointManager.canBuy(player, Settings.TRAITOR_CREEPER_ARROW_PRICE)) {
                    player.sendMessage(NOT_ENOUGH_POINTS);
                    return;
                }
                pointManager.buy(player, Settings.TRAITOR_CREEPER_ARROW_PRICE);
                player.getInventory().addItem(new ItemBuilder(Material.GUNPOWDER, 3).setDisplayName(Settings.TRAITOR_CREEPER_ARROW_ITEM_NAME).setLore("§7Erschafft einen Creeper beim Aufprall").build()); //new ItemBuilder(Material.CREEPER_SPAWN_EGG, 3).setDisplayName(Settings.traitorCreeperArrowItemName).setLore("§7Ein normaler Creeper, der keine Blöcke zerstört").build()
                openTraitorShop(player);
                return;
            }

            if(item.getItemMeta().getDisplayName().equals(Settings.TRAITOR_TEST_FAKER_ITEM_NAME)) {
                if(!pointManager.canBuy(player, Settings.TRAITOR_TEST_FAKER_PRICE)) {
                    player.sendMessage(NOT_ENOUGH_POINTS);
                    return;
                }
                pointManager.buy(player, Settings.TRAITOR_TEST_FAKER_PRICE);
                player.getInventory().addItem(new ItemBuilder(Material.GREEN_STAINED_GLASS).setDisplayName(Settings.TRAITOR_TEST_FAKER_ITEM_NAME).setLore("§7" + Settings.TRAITOR_TEST_FAKER_SUCCESS_RATE + "% Warscheinlichkeit im Tester als Innocent erkannt zu werden," + "§7wenn im Inventar").build());
                openTraitorShop(player);
                return;
            }

        }
        if(player.getOpenInventory().getTitle().startsWith(Settings.DETECTIVE_SHOP_TITLE)) {
            event.setCancelled(true);
            if(item.getItemMeta().getDisplayName().equals(Settings.DETECTIVE_HEALING_STATION_ITEM_NAME)) {
                if(!pointManager.canBuy(player, Settings.DETECTIVE_HEALING_STATION_PRICE)) {
                    player.sendMessage(NOT_ENOUGH_POINTS);
                    return;
                }
                pointManager.buy(player, Settings.DETECTIVE_HEALING_STATION_PRICE);
                player.getInventory().addItem(new ItemBuilder(Material.BEACON).setDisplayName(Settings.DETECTIVE_HEALING_STATION_ITEM_NAME).setLore("§7Heilt alle Spieler die sich in der Nähe", "§7dieser befinden").build());
                openDetectiveShop(player);
                return;
            }
        }
    }

    private void openTraitorShop(Player player) {
        Inventory traitorShop = Bukkit.createInventory(null, 9*1, Settings.TRAITOR_SHOP_TITLE + " - " + pointManager.getPlayerPoints(player) + " Punkte");
        traitorShop.setItem(3, new ItemBuilder(Material.GUNPOWDER, 3).setDisplayName(Settings.TRAITOR_CREEPER_ARROW_ITEM_NAME + " §8(3x)").setLore("§7Erschafft einen Creeper beim Aufprall", "", "§7Preis: §6" + Settings.TRAITOR_CREEPER_ARROW_PRICE + " Punkte", "", "§aZum Kaufen klicken").build());
        traitorShop.setItem(5, new ItemBuilder(Material.GREEN_STAINED_GLASS).setDisplayName(Settings.TRAITOR_TEST_FAKER_ITEM_NAME).setLore("§7Gibt eine " + Settings.TRAITOR_TEST_FAKER_SUCCESS_RATE + "% Wahrscheinlichkeit", "§7im Tester als Innocent erkannt zu werden, ", "§7wenn es im Inventar des Spielers ist", "", "§7Preis: §6" + Settings.TRAITOR_TEST_FAKER_PRICE + " Punkte", "", "§aZum Kaufen klicken").build());
        player.openInventory(traitorShop);
    }

    private void openDetectiveShop(Player player) {
        Inventory detectiveShop = Bukkit.createInventory(null, 9*1, Settings.DETECTIVE_SHOP_TITLE + " - " + pointManager.getPlayerPoints(player) + " Punkte");
        detectiveShop.setItem(4, new ItemBuilder(Material.BEACON).setDisplayName(Settings.DETECTIVE_HEALING_STATION_ITEM_NAME).setLore("§7Heilt alle Spieler die sich in der Nähe", "§7dieser befinden", "", "§7Preis: §6" + Settings.DETECTIVE_HEALING_STATION_PRICE + " Punkte", "", "§aZum Kaufen klicken").build());
        player.openInventory(detectiveShop);
    }

    public static boolean removeMaterialItem(Player player, Material material) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if(player.getInventory().getItem(i) == null) continue;
            if(player.getInventory().getItem(i).getType() == material) {
                if(player.getInventory().getContents()[i].getAmount() == 1)
                    player.getInventory().clear(i);
                else
                    player.getInventory().getContents()[i].setAmount(player.getInventory().getContents()[i].getAmount() - 1);
                player.updateInventory();
                return true;
            }
        }
        return false;
    }

    public ItemStack getTraitorItem() {
        return traitorItem;
    }

    public ItemStack getDetectiveItem() {
        return detectiveItem;
    }

    public PointManager getPointManager() {
        return pointManager;
    }

}
