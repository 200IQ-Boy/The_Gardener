package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Enemy;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteEnemy extends Sprite {
    public SpriteEnemy(Pane layer, Enemy enemy) {
        super(layer,null,enemy);
        updateImage();
    }

    @Override
    public void updateImage() {
        Enemy enemy = (Enemy) getGameObject();
        Image image = getImage(enemy.getDirection(),enemy);
        setImage(image);
    }

    public Image getImage(Direction direction, Enemy enemy) {
        return ImageResourceFactory.getInstance().getEnemy(direction,enemy);
    }
}
