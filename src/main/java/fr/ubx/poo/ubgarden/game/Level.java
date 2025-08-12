package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.entities.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.entities.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.entities.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.entities.bonus.Poisoned_Apple;
import fr.ubx.poo.ubgarden.game.entities.decor.*;
import fr.ubx.poo.ubgarden.game.entities.decor.Flowers;
import fr.ubx.poo.ubgarden.game.entities.decor.ground.Grass;
import fr.ubx.poo.ubgarden.game.entities.decor.ground.Land;
import fr.ubx.poo.ubgarden.game.entities.decor.spawners.NestHornet;
import fr.ubx.poo.ubgarden.game.entities.decor.spawners.NestWasp;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Level implements Map {
    private final int width;
    private final int height;
    private final int carrots;
    private Position nextDoorPosition;
    private boolean hasHedgehog = false;
    private Position previousDoorPosition;
    private final List<NestWasp> nestWasps;
    private final List<NestHornet> nestHornets;

    private final java.util.Map<Position, Decor> decors = new HashMap<>();

    public Level(Game game, int level, MapLevel entities) {
        this.width = entities.width();
        this.height = entities.height();
        int nbCarrots = 0;
        nestWasps = new ArrayList<>();
        nestHornets = new ArrayList<>();

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Position position = new Position(level, i, j);
                MapEntity mapEntity = entities.get(i, j);
                switch (mapEntity) {
                    case Grass: {
                        decors.put(position, new Grass(position));
                        break;
                    }
                    case Land : {
                        decors.put(position, new Land((position)));
                        break;
                    }
                    case Flowers: {
                        decors.put(position,new Flowers(position));
                        break;
                    }
                    case Insecticide: {
                        Grass grass = new Grass(position);
                        grass.setBonus(new Insecticide(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case Hedgehog:{
                        decors.put(position,new Hedgehog(position));
                        hasHedgehog = true;
                        break;
                    }
                    case DoorNextClosed: {
                        decors.put(position,new Door(position,false,false));
                        nextDoorPosition = position;
                        break;
                    }
                    case DoorNextOpened: {
                        decors.put(position,new Door(position,true,false));
                        nextDoorPosition = position;
                        break;
                    }
                    case DoorPrevOpened: {
                        decors.put(position,new Door(position,true,true));
                        previousDoorPosition = position;
                        break;
                    }
                    case Carrots: {
                        Land land = new Land(position);
                        land.setBonus(new Carrots(position, land));
                        decors.put(position, land);
                        nbCarrots++;
                        break;
                    }
                    case Tree: {
                        decors.put(position, new Tree(position));
                        break;
                    }
                    case Apple: {
                        Grass grass = new Grass(position);
                        grass.setBonus(new EnergyBoost(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case PoisonedApple: {
                        Grass grass = new Grass(position);
                        grass.setBonus(new Poisoned_Apple(position,grass));
                        decors.put(position,grass);
                        break;
                    }
                    case NestHornet: {
                        NestHornet nestHornet = new NestHornet(position,new Timer(game.configuration().nestHornetItemSpawnInterval()));
                        decors.put(position,nestHornet);
                        nestHornets.add(nestHornet);
                        break;
                    }
                    case NestWasp: {
                        NestWasp nestWasp = new NestWasp(position,new Timer(game.configuration().nestWaspItemSpawnInterval()));
                        decors.put(position,nestWasp);
                        nestWasps.add(nestWasp);
                        break;
                    }
                    default:
                        throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
                }
            }
        this.carrots = nbCarrots;
    }


    public int width() {
        return this.width;
    }


    public int height() {
        return this.height;
    }

    public Decor get(Position position) {
        return decors.get(position);
    }


    public int carrotsToWin() {
        return carrots;
    }

    public Collection<Decor> values() {
        return decors.values();
    }


    public Door getPreviousDoor() {
        if(previousDoorPosition != null)
            return (Door)decors.get(previousDoorPosition);
        return null;
    }

    public Door getNextDoor() {
        if(nextDoorPosition != null)
            return (Door)decors.get(nextDoorPosition);
        return null;
    }

    public boolean hasHedgehog() {
        return hasHedgehog;
    }

    public List<NestWasp> getNestWasps() {
        return nestWasps;
    }

    public List<NestHornet> getNestHornets() {
        return nestHornets;
    }
}
