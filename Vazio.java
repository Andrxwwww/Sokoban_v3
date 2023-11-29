package pt.iscte.poo.sokobanstarter;
import pt.iscte.poo.utils.Point2D;

public class Vazio extends GameElement implements Interaction{

    public Vazio(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Vazio";
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void interactWith(GameElement ge) {
        // TODO Auto-generated method stub
    }
    
}