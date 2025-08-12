package fr.ubx.poo.ubgarden.game.entities.decor.ground;
import fr.ubx.poo.ubgarden.game.Position;

public class Land extends Ground {
    public Land(Position position){
        super(position);
    }

    @Override
    public int energyConsumptionWalk() {
        return 2;
    }
}
