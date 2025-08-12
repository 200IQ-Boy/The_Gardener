package fr.ubx.poo.ubgarden.game.entities.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.entities.personage.Character;

public class Door extends Decor {
    private Boolean isOpen;
    private final boolean  isPrevious;
    public Door(Position position,Boolean isOpen,boolean isPrevious) {
        super(position);
        this.isOpen = isOpen;
        this.isPrevious = isPrevious;
    }

    @Override
    public boolean walkableBy(Character character) {
        return character.canWalkOn(this);
    }

    public Boolean isOpen() {
        return isOpen;
    }

    public void open() {
        this.isOpen = true;
        setModified(true);
    }

    public boolean isPrevious() {
        return isPrevious;
    }
}
