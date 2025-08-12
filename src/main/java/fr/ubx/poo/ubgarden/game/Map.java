package fr.ubx.poo.ubgarden.game;


import fr.ubx.poo.ubgarden.game.entities.decor.Decor;
import fr.ubx.poo.ubgarden.game.entities.decor.Door;
import fr.ubx.poo.ubgarden.game.entities.decor.spawners.NestHornet;
import fr.ubx.poo.ubgarden.game.entities.decor.spawners.NestWasp;

import java.util.Collection;
import java.util.List;

public interface Map {
    int width();

    int height();

    Decor get(Position position);

    Collection<Decor> values();

    int carrotsToWin();

    Door getPreviousDoor();

    Door getNextDoor();

    boolean hasHedgehog();

    public List<NestWasp> getNestWasps();

    public List<NestHornet> getNestHornets();
}
