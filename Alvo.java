package pt.iscte.poo.sokobanstarter;
import pt.iscte.poo.utils.Point2D;

public class Alvo extends GameElement implements Interaction {

    private boolean BoxOnTarget = false;

    public Alvo(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Alvo";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    public boolean isBoxOnTarget() {
        return BoxOnTarget;
    }

    @Override
    public void interactWith(GameElement ge) {
        if (ge instanceof Caixote) {
            this.BoxOnTarget = true;
        } else {
            this.BoxOnTarget = false;
        }
    }

}