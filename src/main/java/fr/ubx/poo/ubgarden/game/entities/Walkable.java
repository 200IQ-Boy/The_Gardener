package fr.ubx.poo.ubgarden.game.entities;


import fr.ubx.poo.ubgarden.game.entities.personage.Character;

public interface Walkable {
    /**
     * Checks whether the given {@link Character} can walk on this object.
     *
     * @param character the gardener attempting to walk
     * @return true if the gardener can walk on it, false otherwise
     */
    boolean walkableBy(Character character);

    /**
     * Returns the amount of energy consumed when walking over this object.
     *
     * @return the energy cost of walking, defaults to 1
     */
    default int energyConsumptionWalk() {
        return 1;
    }
}
