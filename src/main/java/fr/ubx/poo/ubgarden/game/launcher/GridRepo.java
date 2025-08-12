package fr.ubx.poo.ubgarden.game.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class GridRepo {
    final char EOL = 'x';
    public Grid createGrassGrid(String dimensions,int level) {
        String[] parts = dimensions.split("\\*");
        int width = Integer.parseInt(parts[0]);
        int height = Integer.parseInt(parts[1]);

        Grid grid = new Grid(width, height,level);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid.set(x, y, MapEntity.Grass);
            }
        }
        return grid;
    }

    public String export(Grid grid) {
        if (grid != null) {
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < grid.getHeight(); j++) {
                for (int i = 0; i < grid.getWidth(); i++) {
                    sb.append(grid.get(i, j).getCode());
                }
                sb.append(EOL);
            }

            return sb.toString();
        } else {
            throw new RuntimeException("Grid is null");
        }
    }

    public Properties saveGridToProperties(Grid grid ) {
        Properties props = new Properties();
        File propertiesFile = new File("world/myMaps.properties");

        // Load existing properties if the file is present
        if (propertiesFile.exists()) {
            try (FileInputStream in = new FileInputStream(propertiesFile)) {
                props.load(in);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Add or update the grid in the properties
        props.setProperty("compression", String.valueOf(false));
        String levelKey = "level" + grid.getLevel();
        props.setProperty(levelKey, export(grid));

        // Write properties to the file
        try (FileOutputStream out = new FileOutputStream(propertiesFile)) {
            props.store(out, "Carte personnalisée");
        } catch (IOException er) {
            er.printStackTrace();
        }
        return props;
    }

    public MapLevel loadGridFromProperties(int level) {
        Properties props = new Properties();
        File propertiesFile = new File("world/myMaps.properties");

        // Loading the properties file
        if (propertiesFile.exists()) {
            try (FileInputStream in = new FileInputStream(propertiesFile)) {
                props.load(in);
            } catch (IOException ex) {
                throw new RuntimeException("Erreur lors du chargement du fichier myMaps.properties", ex);
            }
        } else {
            throw new RuntimeException("Le fichier map.properties n'existe pas");
        }

        // Rebuild the level via MapRepoString
        return new MapRepoString().load(props.getProperty("level" + level), false);
    }
}