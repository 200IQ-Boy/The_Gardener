package fr.ubx.poo.ubgarden.game.view;
import fr.ubx.poo.ubgarden.game.launcher.Grid;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;



public class GridView extends BorderPane {
    private final Grid grid;
    private final PickerView pickerView;

    private final ColorAdjust effect = new ColorAdjust();


    public GridView(Grid grid, PickerView pickerView) {
        this.grid = grid;
        this.pickerView = pickerView;
        effect.setBrightness(0.2); // Visual effect applied on hover
        setPrefSize(grid.getWidth() * ImageResource.size, grid.getHeight() * ImageResource.size);

        // Initial creation of all grid tiles
        for (int i = 0; i < this.grid.getWidth(); i++) {
            for (int j = 0; j < this.grid.getHeight(); j++) {
                createTile(i, j);
            }
        }
    }
    private void createTileOfType(int i, int j, Image image) {
        int layoutX = i * ImageResource.size;
        int layoutY = j * ImageResource.size;
        Tile tile = new Tile(image,layoutX,layoutY);
        getChildren().add(tile);
        tile.setOnMouseClicked(e -> {
            // Right click: delete the entity put and replace by a grass
            if (e.getButton() == MouseButton.SECONDARY) {
                getChildren().remove(tile);
                grid.set(i, j, MapEntity.Grass);
                createTile(i, j);
            }
            else
                update(tile, i, j);

        });
        tile.setOnMouseEntered(e -> {
            tile.setEffect(effect);
            if (e.isShiftDown()) {
                update(tile, i, j);
            }
        });
        tile.setOnMouseExited(e -> {
            tile.setEffect(null);
        });
    }

    private void createTile(int i, int j) {
        MapEntity entity = grid.get(i, j);
        // Displays a specific background layer for certain types
        if(entity == MapEntity.Carrots)
            createTileOfType(i, j, ImageResourceFactory.getInstance().get(ImageResource.LAND));
        else if (entity == MapEntity.Apple || entity == MapEntity.Gardener || entity == MapEntity.Insecticide || entity == MapEntity.PoisonedApple)
            createTileOfType(i, j, ImageResourceFactory.getInstance().get(ImageResource.GRASS));
        // Displays the main entity
        createTileOfType(i,j,ImageResourceFactory.getInstance().get(ImageResource.getImageResourceFromMapEntity(entity)));
    }

    private void update(Tile tile, int i, int j) {
        MapEntity selectedEntity = pickerView.getSelected();

        if (selectedEntity != null && selectedEntity != grid.get(i, j)) {
            grid.set(i, j, selectedEntity);
            getChildren().remove(tile);
            createTile(i, j);
        }
    }
}