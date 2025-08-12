package fr.ubx.poo.ubgarden.game.entities.personage.ennemies;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.entities.*;
import fr.ubx.poo.ubgarden.game.entities.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.entities.decor.Decor;

import fr.ubx.poo.ubgarden.game.entities.decor.Flowers;
import fr.ubx.poo.ubgarden.game.entities.personage.Character;
import fr.ubx.poo.ubgarden.game.entities.personage.Gardener;


public abstract class Enemy extends Character implements Movable, WalkVisitor,PickupVisitor{
    public Enemy(Game game, Position position,int energy,Timer timer) {
        super(game,position,energy,timer);
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = super.move(direction);
        Decor next = game.world().getGrid().get(nextPos);
        next.pickUpBy(this);
        return nextPos;
    }

    @Override
    public void update(long now) {
        if(energy <= 0) {
            remove();
            return;
        }
        cooldownTimer.update(now);
        if(cooldownTimer.isRunning()){
            return;
        }
        game.world().getGrid().get(getPosition()).pickUpBy(this);
        cooldownTimer.start();
        Direction randDir = Direction.random();
        if (canMove(randDir)) {
            move(randDir);
            setDirection(randDir);
        } else {
            Direction opposite = randDir.opposite();
            if (canMove(opposite)) {
                move(opposite);
                setDirection(opposite);
            }
        }
    }

    @Override
    public void pickUp(Insecticide insecticide) {
        reduceEnergy(1);
        if(energy <= 0)
            remove();
    }

    @Override
    public boolean canWalkOn(Flowers flowers) {
        return true;
    }

    public abstract void attackGardener(Gardener gardener);
}
