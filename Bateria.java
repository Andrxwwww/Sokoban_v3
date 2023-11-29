package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;  

public class Bateria extends GameElement implements Interaction{

    public Bateria(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Bateria";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interactWith(GameElement ge) {
        // TODO Auto-generated method stub   
    }


}
