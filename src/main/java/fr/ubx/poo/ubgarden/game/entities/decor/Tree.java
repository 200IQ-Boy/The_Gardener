/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.entities.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.entities.personage.Character;

public class Tree extends Decor {
    public Tree(Position position) {
        super(position);
    }

    @Override
    public boolean walkableBy(Character character) {
        return character.canWalkOn(this);
    }
}
