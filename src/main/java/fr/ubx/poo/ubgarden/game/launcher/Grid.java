package fr.ubx.poo.ubgarden.game.launcher;

/**
 * @class Grid
 * @brief Represents a two-dimensional game grid containing entities
 * The grid contains MapEntity entities arranged in a matrix (height x width).
 */
public class Grid {
    private int level;
    private final int width;
    private final int height;
    private final MapEntity[][] grid;

    public Grid(int width, int height,int level) {
        this.width = width;
        this.height = height;
        this.grid = new MapEntity[height][width];
        this.level = level;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLevel() {
        return level;
    }

    public MapEntity get(int i, int j) {
        return grid[j][i];
    }

    public void set(int i, int j, MapEntity entity) {
        grid[j][i] = entity;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}