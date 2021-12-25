package de.brentspine.ttt.gamestates;

import de.brentspine.ttt.Main;

public class GameStateManager {

    private Main plugin;
    private GameState[] gameStates;
    private GameState currentGameState;

    public GameStateManager(Main plugin) {
        this.plugin = plugin;
        gameStates = new GameState[3];

        gameStates[GameState.LOBBY_STATE] = new LobbyState(this);
        gameStates[GameState.INGAME_STATE] = new InGameState(plugin);
        gameStates[GameState.ENDING_STATE] = new EndingState();
    }

    public void setCurrentGameState(int gameStateID) {
        if(currentGameState != null)
            currentGameState.stop();
        currentGameState = gameStates[gameStateID];
        currentGameState.start();
    }

    public void stopCurrentGameState() {
        if(currentGameState != null) {
            currentGameState.stop();
            currentGameState = null;
        }
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public Main getPlugin() {
        return plugin;
    }
}
