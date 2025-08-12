/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.entities.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.entities.Movable;
import fr.ubx.poo.ubgarden.game.entities.PickupVisitor;
import fr.ubx.poo.ubgarden.game.entities.WalkVisitor;
import fr.ubx.poo.ubgarden.game.entities.bonus.*;
import fr.ubx.poo.ubgarden.game.entities.decor.Decor;
import fr.ubx.poo.ubgarden.game.entities.decor.Door;
import fr.ubx.poo.ubgarden.game.entities.decor.Hedgehog;
import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Hornet;
import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Wasp;

import java.util.ArrayList;
import java.util.List;

public class Gardener extends Character implements Movable, PickupVisitor, WalkVisitor {

    private final List<Timer> diseaseTimers = new ArrayList<>();
    private int carrots;
    private int diseaseLevel;
    private int insecticidesNumber;

    public Gardener(Game game, Position position) {
        super(game, position,game.configuration().gardenerEnergy(),new Timer(1000));
        diseaseLevel = 1;
    }

    @Override
    public void pickUp(EnergyBoost boost) {
        diseaseLevel = 1;
        if(energy + game.configuration().energyBoost() <= game.configuration().gardenerEnergy())
            energy+=game.configuration().energyBoost();
        else
            energy = game.configuration().gardenerEnergy();
        diseaseTimers.clear();
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = super.move(direction);
        Decor next = game.world().getGrid().get(nextPos);
        this.energy-= next.energyConsumptionWalk()*diseaseLevel;
        next.pickUpBy(this);
        return nextPos;
    }

    @Override
    public boolean canWalkOn(Door door) {
        return door.isOpen();
    }

    public void pickUp(Carrots carrots) {
        this.carrots+=1;
    }

    public void pickUp(Poisoned_Apple malus) {
        diseaseLevel +=1;
        Timer poisonTimer = new Timer(game.configuration().diseaseDuration());
        poisonTimer.start();
        diseaseTimers.add(poisonTimer);
    }

    public void pickUp(Insecticide insecticide) {
        insecticidesNumber+=1;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    public void update(long now) {
        diseaseTimers.removeIf(timer -> {
            timer.update(now);
            if (!timer.isRunning()) {
                diseaseLevel -= 1;
                return true;
            }
            return false;
        });
        if (moveRequested) {
            if (canMove(direction)) {
                move(direction);
            }
            moveRequested = false;
            cooldownTimer.reset(now);
            return;
        }
       game.world().getGrid().get(getPosition()).pickUpBy(this);
        cooldownTimer.update(now);
        if(cooldownTimer.isRunning())
            return;
        this.energy+= energy < game.configuration().gardenerEnergy() ? 1 : 0;
        cooldownTimer.start();
    }

    public void handleAttackByWasp(Wasp wasp) {
        if(insecticidesNumber == 0)
            reduceEnergy(20);
        else
            insecticidesNumber -= 1;
    }

    public void handleAttackByWasp(Hornet hornet) {
        if (insecticidesNumber == 0)
            reduceEnergy(60);
        else if (insecticidesNumber == 1) {
            reduceEnergy(30);
            insecticidesNumber = 0;
        }
        else
            insecticidesNumber -= 2;
    }

    public int getInsecticidesNumber() {
        return insecticidesNumber;
    }

    public int getDiseaseLevel() {
        return diseaseLevel;
    }

    public void resetCarrotCount() {
        this.carrots = 0;
    }

    public boolean areAllCarrotsCollected() {
        return carrots == game.world().getGrid().carrotsToWin();
    }

    public boolean walkInNextDoor() {
       return game.world().getGrid().getNextDoor() != null && game.world().getGrid().getNextDoor().isOpen() && game.world().getGrid().getNextDoor().getPosition().equals(getPosition()) ;
    }

    public boolean walkInPreviousDoor() {
        return game.world().getGrid().getPreviousDoor() != null && game.world().getGrid().getPreviousDoor().getPosition().equals(getPosition());
    }

    public boolean isHedgehogFound() {
        return game.world().getGrid().values().stream().anyMatch(decor -> decor instanceof Hedgehog && decor.getPosition().equals(getPosition()));
    }

}
