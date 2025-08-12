package fr.ubx.poo.ubgarden.game.entities.personage.ennemies;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.entities.personage.Gardener;

public class Hornet extends Enemy {
    public Hornet(Game game, Position position) {
        super(game, position,2, new Timer( 1000L / game.configuration().hornetMoveFrequency()));
    }

    public void attackGardener(Gardener gardener) {
        reduceEnergy(2);
        gardener.handleAttackByWasp(this);
    }
}
