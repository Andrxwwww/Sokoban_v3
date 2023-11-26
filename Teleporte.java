package pt.iscte.poo.sokobanstarter;
import pt.iscte.poo.utils.Point2D;

public class Teleporte extends GameElement implements Interaction{

    public Teleporte(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Teleporte";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interactWith(GameElement ge) {
        for (GameElement ge2 : GameEngine.getInstance().getGameElementsList()) {
            if (ge instanceof Empilhadora && ge2 instanceof Teleporte) {
                if (hasOnTop(ge)) {
                    break;
                }
            }
            if (ge2 instanceof Teleporte && !ge2.getPosition().equals(this.getPosition()) && !hasOnTop(ge2)) {
                ge.setPosition(ge2.getPosition());
            }
        }
    }

    private boolean hasOnTop(GameElement ge) {
        for (GameElement ge2 : GameEngine.getInstance().getGameElementsList()) {
            if ((ge2 instanceof Palete || ge2 instanceof Caixote) && ge2.getPosition().equals(ge.getPosition())) { 
                return true;
            }
        }
        return false;
    }

}