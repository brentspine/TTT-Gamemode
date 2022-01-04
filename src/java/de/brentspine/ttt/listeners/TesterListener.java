package de.brentspine.ttt.listeners;

import de.brentspine.ttt.Main;
import de.brentspine.ttt.gamestates.InGameState;
import de.brentspine.ttt.role.Tester;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TesterListener implements Listener {

    private Main plugin;

    public TesterListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onButtonClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!(plugin.getGameStateManager().getCurrentGameState() instanceof InGameState)) return;
        InGameState inGameState = (InGameState) plugin.getGameStateManager().getCurrentGameState();
        if(inGameState.isGrace()) return;

        Block block = event.getClickedBlock();
        Tester tester = inGameState.getMap().getTester();
        if(tester.getButton() == null) return;
        if(tester.getButton().getLocation().equals(block.getLocation()))
            tester.test(event.getPlayer());
    }

}
