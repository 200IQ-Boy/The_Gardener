package fr.ubx.poo.ubgarden.game.launcher;

import fr.ubx.poo.ubgarden.game.*;

import java.io.*;
import java.util.List;
import java.util.Properties;


public class GameLauncher {

    private GameLauncher() {
    }

    public static GameLauncher getInstance() {
        return LoadSingleton.INSTANCE;
    }

    private int integerProperty(Properties properties, String name, int defaultValue) {
        return Integer.parseInt(properties.getProperty(name, Integer.toString(defaultValue)));
    }

    private Configuration getConfiguration(Properties properties) {

        // Load parameters
        int waspMoveFrequency = integerProperty(properties, "waspMoveFrequency", 1);
        int hornetMoveFrequency = integerProperty(properties, "hornetMoveFrequency", 2);

        int gardenerEnergy = integerProperty(properties, "gardenerEnergy", 200);
        int energyBoost = integerProperty(properties, "energyBoost", 50);
        long energyRecoverDuration = integerProperty(properties, "energyRecoverDuration", 1_000);
        long diseaseDuration = integerProperty(properties, "diseaseDuration", 5_000);
        int nestWaspItemSpawnInterval = integerProperty(properties, "nestWaspItemSpawnInterval", 5000);
        int nestHornetItemSpawnInterval = integerProperty(properties, "nestHornetItemSpawnInterval", 10000);

        return new Configuration(gardenerEnergy, energyBoost, energyRecoverDuration, diseaseDuration, waspMoveFrequency, hornetMoveFrequency,nestWaspItemSpawnInterval, nestHornetItemSpawnInterval);
    }

    public Game load(File file) throws IOException {
        Properties gameConfig = new Properties();

        try (InputStream in = new FileInputStream(file)) {
            gameConfig.load(in);
        }

        int nbLevels = Integer.parseInt(gameConfig.getProperty("levels"));
        Configuration configuration = getConfiguration(gameConfig);
        World world = new World(nbLevels);
        MapRepoString mapRepoFile = new MapRepoString();
        MapLevel[] mapLevels = new MapLevel[nbLevels];
        Game game = null;

        for (int i = 1; i <= nbLevels; i++) {
            String map = gameConfig.getProperty("level" + i);
            boolean compressionEnabled = Boolean.parseBoolean(gameConfig.getProperty("compression"));
            mapLevels[i - 1] = mapRepoFile.load(map, compressionEnabled); // Load each level

            if (i == 1) {
                // Initializes the game with the gardener's position found on the first level
                Position gardenerPosition = mapLevels[0].getGardenerPosition();
                if (gardenerPosition == null)
                    throw new RuntimeException("Gardener not found");
                game = new Game(world, configuration, gardenerPosition);
            }

            // Creates the Level object associated with this level and inserts it into the world
            Map level = new Level(game, i, mapLevels[i - 1]);
            world.put(i, level);
        }

        return game;
    }


    public Game load() {
        // Loads an empty configuration and hard-coded default levels (test/local mode)
        Properties emptyConfig = new Properties();
        MapLevel mapLevel1 = new MapLevelDefaultStart();
        MapLevel mapLevel2 = new MapLevelSecondTest();
        Position gardenerPosition = mapLevel1.getGardenerPosition();
        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");
        Configuration configuration = getConfiguration(emptyConfig);
        World world = new World(2);
        Game game = new Game(world, configuration, gardenerPosition);
        Map level = new Level(game, 1, mapLevel1);
        Map level2 = new Level(game, 2, mapLevel2);
        world.put(1, level);
        world.put(2, level2);
        return game;
    }

    private static class LoadSingleton {
        static final GameLauncher INSTANCE = new GameLauncher();
    }

}
