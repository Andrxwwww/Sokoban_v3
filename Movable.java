package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class Movable extends GameElement implements Interaction{
    //TODO: implementar interaction nesta class - com caixote e palete e tambem na classe parede e paredeRachada

    public Movable(Point2D position) {
        super(position);
    }

    public Point2D getPosition() {
        return super.getPosition();
    }

    public void setPosition(Point2D position) {
        super.setPosition(position);
    }

    public void movePosition(Direction direction) {
		Point2D newPosition = super.getPosition().plus(direction.asVector());
		if (newPosition.getX()>=0 && newPosition.getX()<10 && newPosition.getY()>=0 && newPosition.getY()<10 ){
			super.setPosition(newPosition);
		}
	}

    public void interactWith(GameElement ge) {
        if (ge instanceof Empilhadora) {
            if (this.getPosition().equals(((Empilhadora)ge).nextPosition(GameEngine.getInstance().getGui().keyPressed()))) {
                this.movePosition(Direction.directionFor(GameEngine.getInstance().getGui().keyPressed()));
                GameEngine.getInstance().bobcat.addBattery(-1);
            }

        }
    }

}
