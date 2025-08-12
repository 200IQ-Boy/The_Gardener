package fr.ubx.poo.ubgarden.game.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Form {
    private final TextField gridString;

    public Form(Stage stage, String title) {
        GridPane pane = new GridPane();
        gridString = new TextField();
        gridString.setPrefWidth(300);
        pane.setPadding(new Insets(25));
        pane.add(gridString,0,0);

        Stage dialog = new Stage();
        Scene scene = new Scene(pane);
        dialog.setTitle(title);
        dialog.setScene(scene);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);

        gridString.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                dialog.close();
            }
        });

        dialog.showAndWait();


    }

    public String getText() {
        return gridString.getText();
    }
}