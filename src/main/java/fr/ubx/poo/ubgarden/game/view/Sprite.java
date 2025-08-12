/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.entities.GameObject;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Sprite {

    public static final int size = 40;
    protected final Pane layer;
    protected final GameObject gameObject;
    protected ImageView imageView;
    protected Image image;
    protected Effect effect;

    public Sprite(Pane layer, Image image, GameObject gameObject) {
        this.layer = layer;
        this.image = image;
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public final void setImage(Image image, Effect effect) {
        if (this.image == null || this.image != image || this.effect != effect) {
            this.image = image;
            this.effect = effect;
        }
    }

    public final void setImage(Image image) {
        setImage(image, null);
    }

    public void updateImage() {

    }

    public Position getPosition() {
        return getGameObject().getPosition();
    }

    public  void render() {
        if (gameObject.isModified()) {
            if (imageView != null) {
                remove();
            }
            updateImage();
            imageView = new ImageView(this.image);
            imageView.setEffect(effect);
            imageView.setX(getPosition().x() * size);
            imageView.setY(getPosition().y() * size);
            layer.getChildren().add(imageView);
            gameObject.setModified(false);
        }
    }

    public final void remove() {
        layer.getChildren().remove(imageView);
        imageView = null;
    }
}
