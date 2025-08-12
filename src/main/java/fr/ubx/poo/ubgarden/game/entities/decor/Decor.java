package fr.ubx.poo.ubgarden.game.entities.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.entities.GameObject;
import fr.ubx.poo.ubgarden.game.entities.Pickupable;
import fr.ubx.poo.ubgarden.game.entities.Walkable;
import fr.ubx.poo.ubgarden.game.entities.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.entities.personage.Character;

public abstract class Decor extends GameObject implements Walkable, Pickupable {

    private Bonus bonus;

    public Decor(Position position) {
        super(position);
    }

    public Decor(Position position, Bonus bonus) {
        super(position);
        this.bonus = bonus;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    @Override
    public boolean walkableBy(Character character) {
        return character.canWalkOn();
    }

    @Override
    public void update(long now) {
        super.update(now);
        if (bonus != null) bonus.update(now);
    }

}