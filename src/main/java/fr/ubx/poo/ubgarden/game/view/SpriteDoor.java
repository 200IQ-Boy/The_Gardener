package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.entities.decor.Door;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteDoor extends Sprite {
    public SpriteDoor(Pane layer, Door door) {
        super(layer,null,door);
        updateImage();
    }

    @Override
    public void updateImage() {
        Door door = (Door) getGameObject();
        Image image = getImage(door);
        setImage(image);
    }

    public Image getImage(Door door) {
        return ImageResourceFactory.getInstance().getDoor(door);
    }
}
