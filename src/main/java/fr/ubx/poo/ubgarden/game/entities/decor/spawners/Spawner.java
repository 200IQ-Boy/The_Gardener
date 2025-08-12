package fr.ubx.poo.ubgarden.game.entities.decor.spawners;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.entities.decor.Decor;

public abstract class Spawner extends Decor {
    private final Timer timerToSpawnItem;
    boolean timeToSpawnItem = false;
    public Spawner(Position position,Timer timer) {
        super(position);
        timerToSpawnItem = timer;
        timerToSpawnItem.start();
    }

    public boolean isTimeToMakeSpawn() {
        return timeToSpawnItem;
    }

    @Override
    public void update(long now) {
        timerToSpawnItem.update(now);
        if(!timerToSpawnItem.isRunning()) {
            timeToSpawnItem = true;
            timerToSpawnItem.start();
        }
        else
            timeToSpawnItem = false;
    }

    public Timer getTimerToSpawnItem() {
        return timerToSpawnItem;
    }
}
