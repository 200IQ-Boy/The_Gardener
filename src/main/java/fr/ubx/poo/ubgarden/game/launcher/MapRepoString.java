package fr.ubx.poo.ubgarden.game.launcher;

public class MapRepoString implements MapRepo {
    final char EOL = 'x';

    public MapLevel load(String string, boolean compressed) {
        if (string == null || string.isEmpty()) {
            throw new MapException("Empty string");
        }

        String[] rawRows = string.split(String.valueOf(EOL));
        int height = rawRows.length;
        if (height == 0) {
            throw new MapException("Empty grid");
        }

        // Uncompress each line if necessary
        String[] rows = new String[height];
        for (int i = 0; i < height; i++) {
            rows[i] = compressed ? simpleDecompress(rawRows[i]) : rawRows[i];
        }

        int width = rows[0].length();

        // Check that all lines have the same length
        for (String row : rows) {
            if (row.length() != width) {
                throw new MapException("Inconsistent row lengths");
            }
        }

        MapLevel mapLevel = new MapLevel(width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char c = rows[i].charAt(j);
                MapEntity e = MapEntity.fromCode(c);
                mapLevel.set(j, i, e);
            }
        }

        return mapLevel;
    }

    private String simpleDecompress(String compressed) {
        StringBuilder result = new StringBuilder();
        char lastChar = 0;
        int count = 0;

        for (char c : compressed.toCharArray()) {
            if (Character.isDigit(c)) {
                // Progressive construction of the number (supports >9)
                count = count * 10 + (c - '0');
            } else {
                // Add the previous character 'count' times
                if (lastChar != 0) {
                    for (int i = 0; i < Math.max(count, 1); i++) {
                        result.append(lastChar);
                    }
                }
                lastChar = c;
                count = 0;
            }
        }

        // Processes the last character read
        if (lastChar != 0) {
            for (int i = 0; i < Math.max(count, 1); i++) {
                result.append(lastChar);
            }
        }

        return result.toString();
    }
}
