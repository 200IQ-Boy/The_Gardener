/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.entities.GameObject;
import fr.ubx.poo.ubgarden.game.entities.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.entities.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.entities.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.entities.bonus.Poisoned_Apple;
import fr.ubx.poo.ubgarden.game.entities.decor.*;
import fr.ubx.poo.ubgarden.game.entities.decor.Flowers;
import fr.ubx.poo.ubgarden.game.entities.decor.ground.Grass;
import fr.ubx.poo.ubgarden.game.entities.decor.ground.Land;
import fr.ubx.poo.ubgarden.game.entities.decor.spawners.NestHornet;
import fr.ubx.poo.ubgarden.game.entities.decor.spawners.NestWasp;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubgarden.game.view.ImageResource.*;


public final class SpriteFactory {

    public static Sprite create(Pane layer, GameObject gameObject) {
        ImageResourceFactory factory = ImageResourceFactory.getInstance();
        if (gameObject instanceof Grass)
            return new Sprite(layer, factory.get(GRASS), gameObject);
        if (gameObject instanceof Tree)
            return new Sprite(layer, factory.get(TREE), gameObject);
        if (gameObject instanceof EnergyBoost)
            return new Sprite(layer, factory.get(APPLE), gameObject);
        if (gameObject instanceof Carrots)
            return new Sprite(layer, factory.get(CARROTS), gameObject);
        if (gameObject instanceof Flowers)
            return new Sprite(layer, factory.get(FLOWERS), gameObject);
        if (gameObject instanceof Land)
            return new Sprite(layer, factory.get(LAND), gameObject);
        if (gameObject instanceof Poisoned_Apple)
            return new Sprite(layer, factory.get(POISONED_APPLE), gameObject);
        if (gameObject instanceof Hedgehog)
            return new Sprite(layer, factory.get(HEDGEHOG), gameObject);
        if (gameObject instanceof NestHornet)
            return new Sprite(layer,factory.get(NESTHORNET), gameObject);
        if (gameObject instanceof NestWasp)
            return new Sprite(layer,factory.get(NESTWASP), gameObject);
        if (gameObject instanceof Door door) {
            return door.isOpen() ? new Sprite(layer,factory.get(DOOR_OPENED), gameObject) : new Sprite(layer,factory.get(DOOR_CLOSED), gameObject);
        }
        if (gameObject instanceof Insecticide)
            return new Sprite(layer, factory.get(INSECTICIDE), gameObject);
        throw new RuntimeException("Unsupported sprite for decor " + gameObject);
    }
}
