package fr.ubx.poo.ubgarden.game;

import java.util.HashMap;
public class World {
    private final java.util.Map<Integer, Map> grids = new HashMap<>();
    private final int Levels;
    private int currentLevel = 1;
    private boolean isHedgeHogSet = false;

    public World(int levels) {
        if (levels < 1) throw new IllegalArgumentException("Levels must be greater than 1");
        this.Levels = levels;
    }


    public int currentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Map getGrid(int level) {
        return grids.get(level);
    }

    public Map getGrid() {
        return getGrid(currentLevel);
    }

    public void put(int level, Map grid) {
        if(level < 1 || level > Levels)
            throw new IllegalArgumentException("Level must be between 1 and " + Levels);
        if(isHedgeHogSet && grid.hasHedgehog())
            throw new IllegalArgumentException("Can't have more than one hedge hog in the world");
        if(level > 1 && grid.getPreviousDoor() == null)
            throw new IllegalArgumentException("The levels above 1 should have a previous door");
        if(level == 1 &&  grid.getPreviousDoor() != null)
            throw new IllegalArgumentException("Level 1 can't have a previous door");
        grids.put(level, grid);
        isHedgeHogSet = grid.hasHedgehog();
    }

}
