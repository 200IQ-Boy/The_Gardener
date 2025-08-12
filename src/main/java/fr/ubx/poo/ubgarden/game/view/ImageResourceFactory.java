package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.entities.decor.Door;
import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Enemy;
import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Wasp;
import javafx.scene.image.Image;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class ImageResourceFactory {

    private final Map<ImageResource, Image> cache;

    private ImageResourceFactory() {
        cache = new Hashtable<>(ImageResource.size);
    }

    public static ImageResourceFactory getInstance() {
        return ImageResourceFactory.LoadSingleton.INSTANCE;
    }

    private Image loadImage(ImageResource imageResource) {
        String fileName = imageResource.getFileName();
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + fileName)));
            if (image.getWidth() != ImageResource.size && image.getHeight() != ImageResource.size) {
                String msg = "File " + fileName + " does not have the correct size " + image.getWidth() + " x " + image.getHeight();
                throw new RuntimeException(msg);
            }
            return image;
        } catch (NullPointerException e) {
            System.err.println("Resource not found : " + fileName);
            throw e;
        }
    }

    public Image get(ImageResource imageResource) {
        Image image = cache.get(imageResource);
        if (image == null) {
            image = loadImage(imageResource);
            cache.put(imageResource, image);
        }
        return image;
    }

    public Image getDigit(int i) {
        if (i < 0 || i > 9) throw new IllegalArgumentException("Digit must be in [0-9]");
        return get(ImageResource.valueOf("DIGIT_" + i));
    }

    public Image getGardener(Direction direction) {
        return get(ImageResource.valueOf("GARDENER_" + direction));
    }

    public Image getEnemy(Direction direction, Enemy Enemy) {
        if(Enemy instanceof Wasp)
            return get(ImageResource.valueOf("WASP_" + direction));
        return get(ImageResource.valueOf("HORNET_" + direction));
    }

    public Image getDoor(Door door) {
        return get(ImageResource.valueOf("DOOR_" + (!door.isOpen() ? "CLOSED_PLUS" : door.isPrevious() ? "OPENED_MINUS" : "OPENED_PLUS" )));
    }

    private static class LoadSingleton {
        static final ImageResourceFactory INSTANCE = new ImageResourceFactory();
    }


}
