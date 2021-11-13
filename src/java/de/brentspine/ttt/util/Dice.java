package de.brentspine.ttt.util;

import java.util.*;

public class Dice {

    private Integer min;
    private Integer max;
    private Integer result;

    boolean running = false;

    public Dice(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Dice() { }


    public Integer roll() {
        if(min == null || max == null) {
            throw new MissingResourceException("Missing max or min", "roll()", "");
        }
        if(min > max) {
            throw new IllegalArgumentException("Min can't be higher than the max value");
        }
        result = (int) (Math.random()*(max-min+1)+min);
        return result;
    }

    private Dice run() {
        if(!running) {
            running = true;
        }
        return this;
    }

    public Integer getResult() {
        if(result == null) {
            throw new NullPointerException("Dice hasn't been rolled yet");
        }
        return result;
    }

    public Dice setMin(Integer min) {
        this.min = min;
        return this;
    }

    public Dice setMax(Integer max) {
        this.max = max;
        return this;
    }
}