package fr.ubx.poo.ubgarden.game.view;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import javafx.geometry.Insets;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class PickerView extends GridPane {
    private final ToggleGroup group = new ToggleGroup();
    private final Text posText = new Text();

    public PickerView() {
        this.setHgap(15); // Espacement horizontal entre les boutons
        this.setVgap(15); // Espacement vertical entre les boutons
        this.setPadding(new Insets(10)); // Marges autour de la grille

        int row = 0;
        int col = 0;
        int maxCols = 5; // Nombre maximum de colonnes par rangée

        for (MapEntity entity : MapEntity.values()) {
            try {
                // Vérifie si l'entité est supportée
                ImageResource imageResource = ImageResource.getImageResourceFromMapEntity(entity);

                // Si aucune exception n'est levée, ajoute le bouton
                ToggleButton btn = new ToggleButton();
                btn.setToggleGroup(group);
                btn.setUserData(entity);
                btn.setGraphic(new ImageView(ImageResourceFactory.getInstance().get(imageResource)));

                // Ajoute le bouton à la grille
                this.add(btn, col, row);

                // Passe à la colonne suivante
                col++;
                if (col >= maxCols) {
                    col = 0; // Retourne à la première colonne
                    row++;  // Passe à la rangée suivante
                }
            } catch (IllegalArgumentException e) {
                // Ignore les entités non reconnues
            }
        }

        // Ajout du texte de position en bas
        this.add(posText, 0, row + 1, maxCols, 1); // Texte sur toute la largeur
    }

    public MapEntity getSelected() {
        Toggle toggle = group.getSelectedToggle();
        if (toggle == null)
            return null;
        return (MapEntity) toggle.getUserData();
    }

}