package pt.iscte.poo.sokobanstarter;
import pt.iscte.poo.utils.Point2D;

public class Chao extends GameElement implements Interaction {

    public Chao(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Chao";
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