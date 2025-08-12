    /*
     * Copyright (c) 2020. Laurent Réveillère
     */

    package fr.ubx.poo.ubgarden.game.engine;

    import fr.ubx.poo.ubgarden.game.Direction;
    import fr.ubx.poo.ubgarden.game.Game;
    import fr.ubx.poo.ubgarden.game.Position;
    import fr.ubx.poo.ubgarden.game.entities.bonus.Insecticide;
    import fr.ubx.poo.ubgarden.game.entities.decor.Decor;
    import fr.ubx.poo.ubgarden.game.entities.decor.Door;
    import fr.ubx.poo.ubgarden.game.entities.decor.ground.Grass;
    import fr.ubx.poo.ubgarden.game.entities.decor.spawners.NestHornet;
    import fr.ubx.poo.ubgarden.game.entities.decor.spawners.NestWasp;
    import fr.ubx.poo.ubgarden.game.entities.personage.Gardener;
    import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Enemy;
    import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Hornet;
    import fr.ubx.poo.ubgarden.game.entities.personage.ennemies.Wasp;
    import fr.ubx.poo.ubgarden.game.view.*;
    import javafx.animation.AnimationTimer;
    import javafx.application.Platform;
    import javafx.scene.Group;
    import javafx.scene.Scene;
    import javafx.scene.layout.Pane;
    import javafx.scene.layout.StackPane;
    import javafx.scene.paint.Color;
    import javafx.scene.text.Font;
    import javafx.scene.text.Text;
    import javafx.scene.text.TextAlignment;

    import java.util.*;

    import static fr.ubx.poo.ubgarden.game.Direction.DOWN;


    public final class GameEngine {

        private static AnimationTimer gameLoop;
        private final Game game;
        private final Gardener gardener;
        private final Map<Integer,List<Enemy>> enemies = new HashMap<>();
        private final Map<Integer,List<Sprite>> sprites = new HashMap<>();
        private final Set<Sprite> cleanUpSprites = new HashSet<>();

        private final Scene scene;

        private StatusBar statusBar;

        private final Pane rootPane = new Pane();
        private final Group root = new Group();
        private final Pane layer = new Pane();
        private Input input;

        public GameEngine(Game game, Scene scene) {
            this.game = game;
            this.scene = scene;
            this.gardener = game.getGardener();
            initialize();
            buildAndSetGameLoop();
        }

        public Pane getRoot() {
            return rootPane;
        }

        private Decor getEmptyGrass() {
            List<Decor> possibles = game.world().getGrid().values().stream().filter(g -> g instanceof Grass && g.getBonus() == null).toList();

            if (possibles.isEmpty()) return null;

            int index = new Random().nextInt(possibles.size());
            return possibles.get(index);
        }

        private void makeInsecticideSpawn(Grass grass) {
            // Create an insecticide bonus and associate it with the grass tile
            if (grass != null) {
                Insecticide insecticide = new Insecticide(grass.getPosition(), grass);
                sprites.get(game.world().currentLevel()).add(SpriteFactory.create(layer, insecticide));
                grass.setBonus(insecticide);
            }
        }
        private void makeWaspSpawn(NestWasp nestWasp) {
            // Spawn a wasp and place an insecticide on an empty grass tile
            Grass grass = (Grass) getEmptyGrass();
            makeInsecticideSpawn(grass);
            Wasp wasp =  new Wasp(game, nestWasp.getPosition());
            sprites.get(game.world().currentLevel()).add(new SpriteEnemy(layer, wasp));
            enemies.get(game.world().currentLevel()).add(wasp);
        }

        private void makeHornetSpawn(NestHornet nestHornet) {
            // Spawn a hornet and place an insecticide on an empty grass tile
            Grass grass = (Grass) getEmptyGrass();
            makeInsecticideSpawn(grass);
            Hornet hornet =  new Hornet(game, nestHornet.getPosition());
            sprites.get(game.world().currentLevel()).add(new SpriteEnemy(layer, hornet));
            enemies.get(game.world().currentLevel()).add(hornet);
        }

        private void initialize() {
            // Configure the scene and build all initial sprites for the level
            // Includes doors, decor elements, some bonuses, and the gardener sprite
            int height = game.world().getGrid().height();
            int width = game.world().getGrid().width();
            int sceneWidth = width * ImageResource.size;
            int sceneHeight = height * ImageResource.size;
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
            input = new Input(scene);

            root.getChildren().clear();
            root.getChildren().add(layer);
            statusBar = new StatusBar(root, sceneWidth, sceneHeight);

            rootPane.getChildren().clear();
            rootPane.setPrefSize(sceneWidth, sceneHeight + StatusBar.height);
            rootPane.getChildren().add(root);

            // Create sprites
            int currentLevel = game.world().currentLevel();
            enemies.putIfAbsent(currentLevel, new ArrayList<>());
            sprites.putIfAbsent(currentLevel, new ArrayList<>());
            if (sprites.get(currentLevel).isEmpty()) {
                for (var decor : game.world().getGrid().values()) {
                    if(decor instanceof Door)
                        sprites.get(game.world().currentLevel()).add(new SpriteDoor(layer,(Door) decor));
                    else{
                        sprites.get(game.world().currentLevel()).add(SpriteFactory.create(layer, decor));
                        var bonus = decor.getBonus();
                        if (bonus != null) {
                            sprites.get(game.world().currentLevel()).add(SpriteFactory.create(layer, bonus));
                        }
                    }
            }

                sprites.get(game.world().currentLevel()).add(new SpriteGardener(layer, gardener));
                System.out.println("width: " + sceneWidth + " height: " + sceneHeight + " ImageResource.size ");
            }
            resizeScene(sceneWidth, sceneHeight);
        }

        void buildAndSetGameLoop() {
            gameLoop = new AnimationTimer() {
                public void handle(long now) {
                    checkLevel();

                    // Check keyboard actions
                    processInput();

                    // Do actions
                    update(now);
                    checkCollision();

                    // Graphic update
                    cleanupSprites();
                    render();
                    statusBar.update(game);
                }
            };
        }


        private void checkLevel() {
            // If a level switch was requested, update gardener's position and reset state
            if (game.isSwitchLevelRequested()) {
                    if(!game.isLevelIncreasing())
                        gardener.setPosition(new Position(game.world().currentLevel(),game.world().getGrid().getNextDoor().getPosition().x(),game.world().getGrid().getNextDoor().getPosition().y() + 1));
                    else
                        gardener.setPosition(new Position(game.world().currentLevel(),game.world().getGrid().getPreviousDoor().getPosition().x(),game.world().getGrid().getPreviousDoor().getPosition().y() + 1));
                    gardener.setDirection(DOWN);
                    gardener.resetCarrotCount();
                    initialize(); // Reload sprites for the new level
                    game.clearSwitchLevel();
            }
        }

        private void checkCollision() {
            // Check if the gardener is colliding with any enemies
            List<Enemy> enemiesToRemove = new ArrayList<>();
            List<Enemy> mapCurrentEnemies = enemies.get(game.world().currentLevel());
            if(mapCurrentEnemies == null)
                return;
            for(Enemy enemy : enemies.get(game.world().currentLevel())) {
                if (enemy.getPosition().equals(gardener.getPosition())) {
                    enemy.attackGardener(gardener);
                    if(enemy.getEnergy() <= 0) {
                        enemy.remove();
                        enemiesToRemove.add(enemy);
                    }
                }
            }
            // Remove defeated enemies from the active list
            enemies.get(game.world().currentLevel()).removeAll(enemiesToRemove);
        }

        private void processInput() {
            if (input.isExit()) {
                gameLoop.stop();
                Platform.exit();
                System.exit(0);
            } else if (input.isMoveDown()) {
                gardener.requestMove(DOWN);
            } else if (input.isMoveLeft()) {
                gardener.requestMove(Direction.LEFT);
            } else if (input.isMoveRight()) {
                gardener.requestMove(Direction.RIGHT);
            } else if (input.isMoveUp()) {
                gardener.requestMove(Direction.UP);
            }
            input.clear();
        }

        private void showMessage(String msg, Color color) {
            Text message = new Text(msg);
            message.setTextAlignment(TextAlignment.CENTER);
            message.setFont(new Font(60));
            message.setFill(color);

            StackPane pane = new StackPane(message);
            pane.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
            rootPane.getChildren().clear();
            rootPane.getChildren().add(pane);

            new AnimationTimer() {
                public void handle(long now) {
                    processInput();
                }
            }.start();
        }

        private void update(long now) {
            game.world().getGrid().values().forEach(decor -> decor.update(now));

            // Spawns from nests
            game.world().getGrid().getNestWasps().stream()
                    .filter(NestWasp::isTimeToMakeSpawn)
                    .forEach(this::makeWaspSpawn);
            game.world().getGrid().getNestHornets().stream()
                    .filter(NestHornet::isTimeToMakeSpawn)
                    .forEach(this::makeHornetSpawn);

            gardener.update(now);

            if (gardener.isHedgehogFound()) {
                showMessage("Gagné!", Color.BLUE);
                gameLoop.stop();
            }

            if (gardener.getEnergy() < 0) {
                showMessage("Perdu!", Color.RED);
                gameLoop.stop();
            }

            for (Enemy enemy : enemies.get(game.world().currentLevel()))
                enemy.update(now);

            // If all carrots are collected, open the next door
            if (gardener.areAllCarrotsCollected() && game.world().getGrid().getNextDoor() != null && !game.world().getGrid().getNextDoor().isOpen())
                openClosedDoor();

            // Handle level transitions when gardener walks through a door
            if (gardener.walkInNextDoor() || gardener.walkInPreviousDoor()) {
                sprites.get(game.world().currentLevel()).forEach(sprite -> sprite.getGameObject().setModified(true));
                cleanUpSprites.addAll(sprites.get(game.world().currentLevel()));
                int nextLevel = gardener.walkInNextDoor() ? game.world().currentLevel() + 1 : game.world().currentLevel() - 1;
                game.requestSwitchLevel(nextLevel);

                // Reset nest's timers
                game.world().getGrid().getNestWasps().forEach(n -> n.getTimerToSpawnItem().reset(now));
                game.world().getGrid().getNestHornets().forEach(n -> n.getTimerToSpawnItem().reset(now));
            }
        }

        public void cleanupSprites() {
            // Remove deleted sprites from the scene and memory
            if(sprites.get(game.world().currentLevel()) == null)
                return;
            sprites.get(game.world().currentLevel()).forEach(sprite -> {
                if (sprite.getGameObject().isDeleted()) {
                    cleanUpSprites.add(sprite);
                }
            });
            cleanUpSprites.forEach(Sprite::remove);
            sprites.get(game.world().currentLevel()).removeAll(cleanUpSprites);
            cleanUpSprites.clear();
        }

        private void render() {
            if(sprites.get(game.world().currentLevel()) == null)
                return;
            sprites.get(game.world().currentLevel()).forEach(Sprite::render);
        }

        public void start() {
            gameLoop.start();
        }

        private void resizeScene(int width, int height) {
            rootPane.setPrefSize(width, height + StatusBar.height);
            layer.setPrefSize(width, height);
            Platform.runLater(() -> scene.getWindow().sizeToScene());
        }

        private void openClosedDoor() {
            Door door= game.world().getGrid().getNextDoor();
            if(door != null)
                door.open();
        }
    }