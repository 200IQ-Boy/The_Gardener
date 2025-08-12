/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.entities;


import fr.ubx.poo.ubgarden.game.entities.personage.Gardener;
import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Enemy;

public interface Pickupable {
    /**
     * Called when a {@link Gardener} picks up this object.
     *
     * @param gardener the gardener who picks up the object
     */
    default void pickUpBy(Gardener gardener) {
    }

    default void pickUpBy(Enemy enemy) {
    }
}
