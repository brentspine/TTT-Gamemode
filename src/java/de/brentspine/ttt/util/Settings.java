package de.brentspine.ttt.util;

import org.bukkit.Material;

public abstract class Settings {

    public static final boolean ONE_TIME_VOTE = true;
    public static final Integer MAP_AMOUNT_TO_VOTE = 2;
    public static final Integer COUNTDOWN_START_COMMAND_SECONDS = 5;
    public static final Integer COUNTDOWN_TIME = 60;
    public static final Integer IDLE_MESSAGE_TIME = 55;
    public static final Integer MIN_PLAYERS = 1;
    public static final Integer MAX_PLAYERS = 12;
    public static final String PREFIX = "§4§lTTT §8» §7";
    public static final String NO_PERMISSION = PREFIX + "§cYou don't have enough permissions to do that";
    public static final String VOTING_INVENTORY_TITLE = "§b§lVoting";
    public static final String VOTING_ITEM_NAME = "§cMap-Voting";
    public static final Integer ROLE_COUNTDOWN_SECONDS = 10;
    public static final boolean ALLOW_DROP_ITEMS = true;
    //allowDropItems must be true for this to work
    public static final boolean ALLOW_DROP_ROLE_ITEMS = false;

    public static final Double TRAITOR_PROBABILITY = 0.9;
    public static final Double DETECTIVE_PROBABILITY = 0.8;

    //Item Names
    public static final String TRAITOR_SHOP_TITLE = "§cTraitor-Shop";
    public static final String DETECTIVE_SHOP_TITLE = "§9Detective-Shop";
    public static final String TRAITOR_CREEPER_ARROW_ITEM_NAME  = "§cCreeper Arrow";
    public static final String TRAITOR_TEST_FAKER_ITEM_NAME = "§cTest-Faker";
    public static final String DETECTIVE_HEALING_STATION_ITEM_NAME = "§9Healing-Station";
    public static final Integer ARROW_AMOUNT_CHEST_LOOT = 12;

    public static final Integer TRAITOR_CREEPER_ARROW_PRICE = 4;
    public static final Integer TRAITOR_TEST_FAKER_PRICE = 5;
    public static final Integer DETECTIVE_HEALING_STATION_PRICE = 3;

    public static final Integer TRAITOR_TEST_FAKER_SUCCESS_RATE = 75;
    public static final Integer STARTING_CAPITAL_POINTS = 10;
    public static final boolean KILL_TRAITOR_ON_TEAMMATE_KILL = false;
    public static final Integer POINTS_REMOVED_TRAITOR_KILL_TEAMMATE = 6;
    public static final Integer POINTS_ADDED_TRAITOR_KILL_DETECTIVE = 5;
    public static final Integer POINTS_ADDED_TRAITOR_KILL_INNOCENT = 2;
    public static final Integer POINTS_ADDED_DETECTIVE_KILL_TRAITOR = 5;
    public static final Integer POINTS_REMOVED_DETECTIVE_KILL_TEAMMATE = 7;
    public static final Integer POINTS_REMOVED_DETECTIVE_KILL_INNOCENT = 2;

    //Detective steht auch für Innocent
    public static final Integer KARMA_ADDED_FOR_PLAYING = 10;
    public static final Integer KARMA_REMOVED_TRAITOR_KILL_TEAMMATE = 50;
    public static final Integer KARMA_ADDED_TRAITOR_KILL_DETECTIVE = 50;
    public static final Integer KARMA_ADDED_TRAITOR_KILL_INNOCENT = 20;
    public static final Integer KARMA_ADDED_DETECTIVE_KILL_TRAITOR = 50;
    public static final Integer KARMA_REMOVED_DETECTIVE_KILL_TEAMMATE = 50;
    public static final Integer KARMA_REMOVED_DETECTIVE_KILL_INNOCENT = 15;

    public static final Material TESTER_INACTIVE_LAMP_MATERIAL = Material.WHITE_STAINED_GLASS;
    public static final Material TESTER_ACTIVE_LAMP_MATERIAL = Material.YELLOW_STAINED_GLASS;
    public static final Material TESTER_RESULT_INNOCENT_MATERIAL = Material.GREEN_STAINED_GLASS;
    public static final Material TESTER_RESULT_TRAITOR_MATERIAL = Material.RED_STAINED_GLASS;
    public static final Material TESTER_BORDER_BLOCK_MATERIAL = Material.GLASS;
    public static final Integer TESTING_DELAY_TICKS = 100; //20 Ticks = 1 Second
    public static final Integer TESTING_DELAY_AFTER_TEST_TICKS = 40; //20 Ticks = 1 Second





    //Man könnte einen Modus machen, bei dem Traitor nicht sehen können wer die anderen Verräter sind.

}
