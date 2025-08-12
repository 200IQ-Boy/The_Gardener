/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.entities.bonus;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.entities.decor.Decor;
import fr.ubx.poo.ubgarden.game.entities.personage.Gardener;
import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Enemy;

public class EnergyBoost extends Bonus {

    public EnergyBoost(Position position, Decor decor) {
        super(position, decor);
    }

    @Override
    public void pickUpBy(Enemy enemy) {
        enemy.pickUp(this);
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        gardener.pickUp(this);
        remove();
    }

}
