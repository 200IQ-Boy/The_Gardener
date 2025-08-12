package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.entities.personage.Gardener;


public class Game {

    private final Configuration configuration;
    private final World world;
    private final Gardener gardener;
    private boolean switchLevelRequested = false;
    private int switchLevel;
    private boolean isLevelIncreasing = false;
    public Game(World world, Configuration configuration, Position gardenerPosition) {
        this.configuration = configuration;
        this.world = world;
        gardener = new Gardener(this, gardenerPosition);
    }

    public Configuration configuration() {
        return configuration;
    }

    public Gardener getGardener() {
        return this.gardener;
    }

    public World world() {
        return world;
    }

    public boolean isSwitchLevelRequested() {
        return switchLevelRequested;
    }

    public void requestSwitchLevel(int level) {
        isLevelIncreasing =  level - switchLevel > 0;
        this.switchLevel = level;
        this.world.setCurrentLevel(level);
        switchLevelRequested = true;
    }

    public void clearSwitchLevel() {
        switchLevelRequested = false;
    }

    public boolean isLevelIncreasing() {
        return isLevelIncreasing;
    }

}
