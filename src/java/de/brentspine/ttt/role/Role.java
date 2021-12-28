package de.brentspine.ttt.role;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum Role {

    INNOCENT("Innocent", ChatColor.YELLOW, Color.GRAY),
    DETECTIVE("Detective", ChatColor.BLUE, Color.BLUE),
    TRAITOR("Traitor", ChatColor.RED, Color.RED);


    private Role(String name, ChatColor chatColor, Color chestPlateColor) {
        this.name = name;
        this.chatColor = chatColor;
        this.chestPlateColor = chestPlateColor;
    }

    private String name;
    private ChatColor chatColor;
    private Color chestPlateColor;


    public String getName() {
        return name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public Color getChestPlateColor() {
        return chestPlateColor;
    }
}
