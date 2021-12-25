package de.brentspine.ttt.util;

public abstract class Settings {

    public static boolean oneTimeVote = true;
    public static Integer mapAmountToVote = 2;
    public static Integer countdownStartCommandSeconds = 5;
    public static Integer countdownTime = 60;
    public static Integer idleMessageTime = 55;
    public static Integer minPlayers = 1;
    public static Integer maxPlayers = 12;
    public static String prefix = "§4§lTTT §8» §7";
    public static String noPermission = prefix + "§cYou don't have enough permissions to do that";
    public static String votingInventoryTitle = "§b§lVoting";
    public static String votingItemName = "§cMap-Voting";
    public static Integer roleCountdownSeconds = 30;

}
