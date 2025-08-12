package fr.ubx.poo.ubgarden.game.entities.personage.ennemies;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.entities.personage.Gardener;

public class Wasp extends Enemy {
    public Wasp(Game game, Position position) {
        // Initializes an object with a timer whose interval depends on the wasps' movement frequency
        super(game, position,1, new Timer( 1000L / game.configuration().waspMoveFrequency()));
    }
    public void attackGardener(Gardener gardener) {
        gardener.handleAttackByWasp(this);
        reduceEnergy(1);
    }
}
