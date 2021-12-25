package de.brentspine.ttt.role;

import net.md_5.bungee.api.ChatColor;

public enum Role {

    INNOCENT("Innocent", ChatColor.YELLOW),
    DETECTIVE("Detective", ChatColor.BLUE),
    TRAITOR("Traitor", ChatColor.RED);


    private Role(String name, ChatColor chatColor) {
        this.name = name;
        this.chatColor = chatColor;
    }

    private String name;
    private ChatColor chatColor;


    public String getName() {
        return name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

}
