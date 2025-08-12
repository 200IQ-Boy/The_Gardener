package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.launcher.MapEntity;

public enum ImageResource {
    // Decor elements
    GRASS("grass.jpg"), LAND("land.jpg"), CARROTS("carrots.png"), FLOWERS("flowers.jpg"),

    ENERGY("energy.png"), TREE("tree.jpg"),


    // Bonus
    POISONED_APPLE("poisonedApple.png"), APPLE("apple.png"),


    GROUND("ground.png"),

    DOOR_OPENED("door_opened.png"), DOOR_CLOSED("door_closed.png"),DOOR_OPENED_PLUS("door_opened_plus.png"),DOOR_OPENED_MINUS("door_opened_minus.png"),DOOR_CLOSED_PLUS("door_closed_plus.png"),


    // Gardener and hornets
    GARDENER_UP("gardener_up.png"), GARDENER_RIGHT("gardener_right.png"), GARDENER_DOWN("gardener_down.png"), GARDENER_LEFT("gardener_left.png"),


    HEDGEHOG("hedgehog.png"),

    INSECTICIDE("insecticide.png"),

    // Status bar

    DIGIT_0("banner_0.jpg"), DIGIT_1("banner_1.jpg"), DIGIT_2("banner_2.jpg"), DIGIT_3("banner_3.jpg"), DIGIT_4("banner_4.jpg"), DIGIT_5("banner_5.jpg"), DIGIT_6("banner_6.jpg"), DIGIT_7("banner_7.jpg"), DIGIT_8("banner_8.jpg"), DIGIT_9("banner_9.jpg"),

    NESTWASP("nest_wasp.png"), NESTHORNET("nest_hornet.png"),
    WASP_UP("wasp_up.png"), WASP_RIGHT("wasp_right.png"), WASP_DOWN("wasp_down.png"), WASP_LEFT("wasp_left.png"),
    HORNET_UP("hornet_up.png"), HORNET_RIGHT("hornet_right.png"), HORNET_DOWN("hornet_down.png"), HORNET_LEFT("hornet_left.png");


    public static final int size = 40;
    private final String fileName;

    ImageResource(String fileName) {
        this.fileName = fileName;
    }

    public static ImageResource getImageResourceFromMapEntity(MapEntity mapEntity) {
        return switch (mapEntity) {
            case PoisonedApple -> ImageResource.POISONED_APPLE;
            case Apple -> ImageResource.APPLE;
            case Carrots -> ImageResource.CARROTS;
            case Flowers -> ImageResource.FLOWERS;
            case Grass -> ImageResource.GRASS;
            case Land -> ImageResource.LAND;
            case Tree -> ImageResource.TREE;
            case Insecticide -> ImageResource.INSECTICIDE;
            case Gardener -> ImageResource.GARDENER_DOWN; // Par défaut, direction vers le bas
            case Hedgehog -> ImageResource.HEDGEHOG;
            case DoorPrevOpened -> ImageResource.DOOR_OPENED_MINUS;
            case DoorNextOpened -> ImageResource.DOOR_OPENED_PLUS;
            case DoorNextClosed -> ImageResource.DOOR_CLOSED_PLUS;
            case NestWasp -> ImageResource.NESTWASP;
            case NestHornet -> ImageResource.NESTHORNET;
            default -> throw new IllegalArgumentException("MapEntity non reconnue : " + mapEntity);
        };
    }

    public String getFileName() {
        return fileName;
    }

}

