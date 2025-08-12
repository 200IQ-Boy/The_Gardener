package fr.ubx.poo.ubgarden.game.entities.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.entities.GameObject;
import fr.ubx.poo.ubgarden.game.entities.Movable;
import fr.ubx.poo.ubgarden.game.entities.PickupVisitor;
import fr.ubx.poo.ubgarden.game.entities.WalkVisitor;
import fr.ubx.poo.ubgarden.game.entities.decor.Decor;


public abstract class Character extends GameObject implements Movable, WalkVisitor, PickupVisitor {
    protected int energy;
    protected Direction direction;
    protected long lastMoveTime;
    protected boolean moveRequested = false;
    protected Timer cooldownTimer;
    public Character(Game game, Position position,int energy,Timer cooldownTimer) {
        super(game, position);
        lastMoveTime = System.nanoTime();
        setDirection(Direction.random());
        this.energy = energy;
        this.cooldownTimer = cooldownTimer;
        this.cooldownTimer.start();
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor decor = game.world().getGrid().get(nextPos);
        return nextPos != null && nextPos.x() >= 0 && nextPos.y() >= 0 && nextPos.x() < game.world().getGrid().width() && nextPos.y() < game.world().getGrid().height() && decor.walkableBy(this);
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        return nextPos;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void reduceEnergy(int damage) {
        this.energy -= damage;
    }

    public int getEnergy() {
        return this.energy;
    }
}
