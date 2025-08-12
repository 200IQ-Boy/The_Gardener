package fr.ubx.poo.ubgarden.game.entities;

import fr.ubx.poo.ubgarden.game.entities.bonus.*;

public interface PickupVisitor {

    default void pickUp(EnergyBoost boost) {};

    default void pickUp(Carrots carrots) {};

    default void pickUp(Insecticide insecticide) {};

    default void pickUp(Poisoned_Apple malus) {};
}
