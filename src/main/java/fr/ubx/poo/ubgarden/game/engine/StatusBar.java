/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.engine;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.view.ImageResource;
import fr.ubx.poo.ubgarden.game.view.ImageResourceFactory;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class StatusBar {
    public static final int height = 55;
    private final HBox level = new HBox();

    private final Text energy = new Text();

    private final Text diseaseLevel = new Text();

    private final Text insecticideNumber = new Text();
    private final int gameLevel = 1;
    private final DropShadow ds = new DropShadow();


    public StatusBar(Group root, int sceneWidth, int sceneHeight) {
        // Create level indicator
        level.getStyleClass().add("level");
        level.getChildren().add(new ImageView(ImageResourceFactory.getInstance().getDigit(gameLevel)));

        // Configure text shadow effect
        ds.setRadius(5.0);
        ds.setOffsetX(3.0);
        ds.setOffsetY(3.0);
        ds.setColor(Color.color(0.5f, 0.5f, 0.5f));

        // Create status elements with icons and labels
        HBox status = new HBox();
        status.getStyleClass().add("status");
        HBox insecticideStatus = statusGroup(ImageResourceFactory.getInstance().get(ImageResource.INSECTICIDE), insecticideNumber);
        HBox energyStatus = statusGroup(ImageResourceFactory.getInstance().get(ImageResource.ENERGY), energy);
        HBox diseaseLevelStatus = statusGroup(ImageResourceFactory.getInstance().get(ImageResource.POISONED_APPLE), diseaseLevel);

        status.setSpacing(40.0);
        status.getChildren().addAll(diseaseLevelStatus, insecticideStatus, energyStatus);

        // Assemble complete status bar
        HBox hBox = new HBox();
        hBox.getChildren().addAll(level, status);
        hBox.getStyleClass().add("statusBar");
        hBox.relocate(0, sceneHeight);
        hBox.setPrefSize(sceneWidth, height);
        root.getChildren().add(hBox);
    }

    private void updateLevel(int n) {
        // Replace level digit if it has changed
        if (n != gameLevel) {
            level.getChildren().clear();
            level.getChildren().add(new ImageView(ImageResourceFactory.getInstance().getDigit(n)));
        }
    }

    private HBox statusGroup(Image kind, Text number) {
        HBox group = new HBox();
        ImageView img = new ImageView(kind);
        group.setSpacing(3);
        number.setEffect(ds);
        number.setCache(true);
        number.setFill(Color.BLACK);
        number.getStyleClass().add("number");
        group.getChildren().addAll(img, number);
        return group;
    }

    public void update(Game game) {
        // display insecticide number
        insecticideNumber.setText(Integer.toString(game.getGardener().getInsecticidesNumber()));

        // display disease level
        diseaseLevel.setText(Integer.toString(game.getGardener().getDiseaseLevel()));

        // display energy remaining
        energy.setText(Integer.toString(game.getGardener().getEnergy()));

        updateLevel(game.world().currentLevel());
    }

}
