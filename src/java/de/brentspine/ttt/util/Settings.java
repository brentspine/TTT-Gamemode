package de.brentspine.ttt.util;

import org.bukkit.Material;

import java.util.ArrayList;

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
    public static Integer roleCountdownSeconds = 10;
    public static boolean allowDropItems = true;
    //allowDropItems must be true for this to work
    public static boolean allowDropRoleItems = false;

    public static Double traitorProbability = 0.9;
    public static Double detectiveProbability = 0.8;

    //Item Names
    public static String traitorBowName = "§aTraitor-Bow";
    public static Integer arrowAmountChestLoot = 12;

    public static Material testerInactiveLampMaterial = Material.WHITE_STAINED_GLASS;
    public static Material testerActiveLampMaterial = Material.YELLOW_STAINED_GLASS;
    public static Material testerResultInnocentLampMaterial = Material.GREEN_STAINED_GLASS;
    public static Material testerResultTraitorLampMaterial = Material.RED_STAINED_GLASS;
    public static Material testerBorderBlockMaterial = Material.GLASS;
    public static Integer testingDelayTicks = 100; //20 Ticks = 1 Second
    public static Integer testingDelayAfterTestTicks = 40; //20 Ticks = 1 Second



    //Man könnte einen Modus machen, bei dem Traitor nicht sehen können wer die anderen Verräter sind.

}
