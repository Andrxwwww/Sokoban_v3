package pt.iscte.poo.sokobanstarter;
import pt.iscte.poo.utils.Point2D;

public class Buraco extends GameElement implements Interaction {

    public Buraco(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Buraco";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interactWith(GameElement ge) {
        if (ge instanceof Palete) {
            GameEngine.getInstance().getGameElementsList().remove(ge);
            GameEngine.getInstance().getGameElementsList().remove(this);
        } else if ( ge instanceof Caixote) {
            GameEngine.getInstance().getGameElementsList().remove(ge);
            GameEngine.getInstance().getGui().removeImage(ge);
            GameEngine.getInstance().infoBox("Press SPACE for restart", "The number of boxes on inferior than the targets :(");
            GameEngine.getInstance().restartGame(GameEngine.getInstance().FIRST_LEVEL);
        } else if ( ge instanceof Empilhadora) {
            GameEngine.getInstance().getGameElementsList().remove(ge);
            GameEngine.getInstance().getGui().removeImage(ge);
            GameEngine.getInstance().infoBox("Press SPACE for restart", "You Lost :(");
            GameEngine.getInstance().restartGame(GameEngine.getInstance().FIRST_LEVEL);
        }
    }

}