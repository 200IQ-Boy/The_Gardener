package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.engine.GameEngine;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.launcher.GameLauncher;
import fr.ubx.poo.ubgarden.game.launcher.Grid;
import fr.ubx.poo.ubgarden.game.launcher.GridRepo;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;


public class GameLauncherView extends BorderPane {
    private final FileChooser fileChooser = new FileChooser();
    private final Stage stage;
    private Grid grid;
    private final PickerView pickerView;
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private final ClipboardContent clipboardContent = new ClipboardContent();
    private final Label editingLevelLabel = new Label();
    private int numberLevelToEdit;

    public GameLauncherView(Stage stage) {
        this.stage = stage;
        this.pickerView = new PickerView();

        setupEditingLabel();
        VBox topContainer = new VBox(createMenuBar(), createLabelContainer());
        this.setTop(topContainer);

        setupWelcomeScene();
        clearPropertiesFile();
        stage.setResizable(false);
    }

    private void setupEditingLabel() {
        editingLevelLabel.setVisible(false);
        editingLevelLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: blue; -fx-font-weight: bold;-fx-font-family: Verdana;");
    }

    private HBox createLabelContainer() {
        HBox labelContainer = new HBox(editingLevelLabel);
        labelContainer.setAlignment(Pos.CENTER);
        return labelContainer;
    }

    private void setupWelcomeScene() {
        Text text = new Text("UBGarden 2025");
        text.getStyleClass().add("message");
        VBox scene = new VBox(text);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        scene.getStyleClass().add("message");
        this.setCenter(scene);
    }

    private void clearPropertiesFile() {
        try (FileOutputStream out = new FileOutputStream("world/myMaps.properties")) {
            out.write(new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu myLevels = new Menu("My Levels");
        myLevels.setVisible(false);
        MenuItem nextAction = new MenuItem("Next Level »");
        MenuItem prevAction = new MenuItem("« Previous Level");
        myLevels.getItems().addAll(nextAction, new SeparatorMenuItem(), prevAction);

        Menu menuGame = createGameMenu();
        Menu menuEditor = createEditorMenu(myLevels);

        setupLevelNavigationActions(nextAction, prevAction, myLevels);

        menuBar.getMenus().addAll(menuGame, menuEditor, myLevels);
        return menuBar;
    }

    private Menu createGameMenu() {
        Menu menuGame = new Menu("Game");
        MenuItem loadItem = new MenuItem("Load from file ...");
        MenuItem defaultItem = new MenuItem("Load default configuration");
        MenuItem exitItem1 = new MenuItem("Exit");
        exitItem1.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

        loadItem.setOnAction(e -> {
            this.setBottom(null);
            loadGameFromFile();
        });
        defaultItem.setOnAction(e -> {
            this.setBottom(null);
            loadDefaultGame();
        });
        exitItem1.setOnAction(e -> System.exit(0));

        menuGame.getItems().addAll(loadItem, defaultItem, new SeparatorMenuItem(), exitItem1);
        return menuGame;
    }

    private Menu createEditorMenu(Menu myLevels) {
        Menu menuEditor = new Menu("Editor");
        MenuItem editorOpen = new MenuItem("Open map editor");
        MenuItem exportItemSZ = new MenuItem("Export as properties file");
        MenuItem exitItem2 = new MenuItem("Exit");
        exitItem2.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

        editorOpen.setOnAction(e -> openEditor(myLevels));
        exportItemSZ.setOnAction(e -> exportCurrentGrid(true));
        exitItem2.setOnAction(e -> System.exit(0));

        menuEditor.getItems().addAll(editorOpen, exportItemSZ, new SeparatorMenuItem(), exitItem2);
        return menuEditor;
    }

    private void loadGameFromFile() {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Game game = GameLauncher.getInstance().load(file);
                GameEngine engine = new GameEngine(game, stage.getScene());
                this.setCenter(engine.getRoot());
                engine.getRoot().requestFocus();
                engine.start();
                resizeStage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadDefaultGame() {
        Game game = GameLauncher.getInstance().load();
        GameEngine engine = new GameEngine(game, stage.getScene());
        this.setCenter(engine.getRoot());
        engine.getRoot().requestFocus();
        engine.start();
        resizeStage();
    }

    private void openEditor(Menu myLevels) {
        // Displays an information dialog box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Map Editor");
        alert.setHeaderText("Maps will be saved automatically");
        alert.setContentText("All maps you create will be saved in the file:\n\nworld/myMaps.properties");
        alert.showAndWait();

        // Displays a dialog box to ask how many levels will be created
        Form numberLevelsForm = new Form(stage, "Number of Levels you want to create");
        numberLevelToEdit = Integer.parseInt(numberLevelsForm.getText());

        // Reset the configuration file with the correct number of levels
        try (FileOutputStream out = new FileOutputStream("world/myMaps.properties")) {
            Properties props = new Properties();
            props.setProperty("levels", String.valueOf(numberLevelToEdit));
            props.store(out, "Mise à jour du nombre de niveaux");
        } catch (IOException er) {
            er.printStackTrace();
        }

        loadNewEditableGrid(1, true);
        myLevels.setVisible(true);
    }

    private void exportCurrentGrid(boolean displayDialog) {
        GridRepo gridRepo = new GridRepo();
        Properties props = gridRepo.saveGridToProperties(grid);
        if(displayDialog)
            exportDialog(props.getProperty("level" + grid.getLevel()));
    }

    private void setupLevelNavigationActions(MenuItem nextAction, MenuItem prevAction, Menu myLevels) {
        // Save the current grid state  whenever we press next or previous
        File propertiesFile = new File("world/myMaps.properties");

        nextAction.setOnAction(e -> {
            exportCurrentGrid(false);
            // Load properties to check if the next level already exists
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream(propertiesFile)) {
                props.load(in);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            int nextLevel = grid.getLevel() + 1;
            if (props.getProperty("level" + nextLevel) == null) {
                GridRepo gridRepo = new GridRepo();
                gridRepo.saveGridToProperties(grid);
                if (grid.getLevel() < numberLevelToEdit)
                    loadNewEditableGrid(nextLevel, true);
            } else {
                // Otherwise, load the existing version
                loadNewEditableGrid(nextLevel, false);
            }
        });

        prevAction.setOnAction(e -> {
            exportCurrentGrid(false);
            GridRepo gridRepo = new GridRepo();
            gridRepo.saveGridToProperties(grid);
            if (grid.getLevel() > 1)
                loadNewEditableGrid(grid.getLevel() - 1, false);
        });
    }

    private void resizeStage() {
        stage.sizeToScene();
        stage.hide();
        stage.show();
    }

    private void updateGrid(Grid grid) {
        if (grid != null) {
            Pane gridView = new GridView(grid, pickerView);
            this.setCenter(gridView);
            stage.sizeToScene();
        }
    }

    private void exportDialog(String msg) {
        clipboardContent.putString(msg);
        clipboard.setContent(clipboardContent);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export");
        alert.setHeaderText("Saved to clipboard");
        alert.getDialogPane().setContent(new TextArea(msg));
        alert.setResizable(true);
        alert.showAndWait();
    }

    private void loadNewEditableGrid(int level, boolean isNextLevel) {
        // Creation of a new empty grid if we add a new level
        GridRepo gridRepo = new GridRepo();
        if (isNextLevel) {
            Form mapForm = new Form(stage, "Map Size: width*height");
            this.grid = gridRepo.createGrassGrid(mapForm.getText(), level);
        } else {
            MapLevel gridData = gridRepo.loadGridFromProperties(level);
            this.grid = new Grid(gridData.width(), gridData.height(), level);
            for (int i = 0; i < gridData.width(); i++) {
                for (int j = 0; j < gridData.height(); j++) {
                    this.grid.set(i, j, gridData.get(i, j));
                }
            }
        }
        this.setBottom(pickerView);
        updateGrid(grid);
        editingLevelLabel.setText("Editing Level: " + grid.getLevel());
        editingLevelLabel.setVisible(true);
    }
}

