package fr.ubx.poo.ubgarden.game.entities;


import fr.ubx.poo.ubgarden.game.entities.decor.Door;
import fr.ubx.poo.ubgarden.game.entities.decor.Flowers;
import fr.ubx.poo.ubgarden.game.entities.decor.Tree;


public interface WalkVisitor {

    // By default it's possible to walk on every decor
    default boolean canWalkOn() {
        return true;
    }

    /**
     * Determines whether the visitor can walk on the given {@link Tree}.
     *
     * @param tree the tree to evaluate
     * @return true if the visitor can walk on the tree, false by default
     */
    default boolean canWalkOn(Tree tree) {
        return false;
    }

    default boolean canWalkOn(Door door) {
        return false;
    }

    default boolean canWalkOn(Flowers flowers) {
        return false;
    }
}