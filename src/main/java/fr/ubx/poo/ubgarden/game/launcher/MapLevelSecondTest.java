package fr.ubx.poo.ubgarden.game.launcher;

import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.*;
import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.Grass;

public class MapLevelSecondTest extends MapLevel {
    private final static int width = 12;
    private final static int height = 6;
    private final MapEntity[][] level2 = {
            {Tree, Tree, Tree, Grass, Grass, Flowers, Grass, Tree, Tree, Grass, Grass, Tree},
            {Tree, PoisonedApple, Grass, Grass, NestWasp, Grass, Carrots, Grass, NestHornet, Grass, Apple, Tree},
            {Grass, Grass, Tree, Grass, Grass, Flowers, Grass, Grass, Tree, Grass, Grass, Grass},
            {Tree, Grass, Tree, Tree, Tree, Tree, Grass, Tree, Grass, Grass, Grass, Tree},
            {Grass, Grass, Hedgehog, Grass, Tree, Grass, Grass, Grass, Grass, Grass,  DoorPrevOpened, Grass},
            {Tree, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass,Grass, Tree}
    };

    public MapLevelSecondTest() {
        super(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level2[j][i]);
    }
}
