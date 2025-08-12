/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.entities.decor.ground;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.entities.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.entities.decor.Decor;
import fr.ubx.poo.ubgarden.game.entities.personage.Gardener;
import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Enemy;

public abstract class Ground extends Decor {
    public Ground(Position position) {
        super(position);
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        Bonus bonus = getBonus();
        if (bonus != null) {
            bonus.pickUpBy(gardener);
        }
    }

    @Override
    public void pickUpBy(Enemy enemy) {
        Bonus bonus = getBonus();
        if (bonus != null) {
            bonus.pickUpBy(enemy);
        }
    }
}
