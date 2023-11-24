package pt.iscte.poo.sokobanstarter;

import java.util.Iterator;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement{

	private String imageName;
	private int Battery;
	private boolean setHammer;

	private final int BATTERY_RELOAD = 50;
	private final int FULL_BATTERY = 100;
	private final int FIRST_LEVEL = 0;
	
	public Empilhadora(Point2D position){
        super(position);
		this.imageName = "Empilhadora_D";
		this.Battery = FULL_BATTERY;
		this.setHammer = false;
	}

	public int getBattery() {
		return Battery;
	}

	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public int getLayer() {
		return 3;
	}

	@Override
    public boolean isFloor() {
		return false;
	}

	public boolean hasHammer() {
		return setHammer;
	}

	public void setHammer(boolean setHammer) {
		this.setHammer = setHammer;
	}

	public int addBattery(int sumBattery) {
		this.Battery += sumBattery;
		if (Battery > FULL_BATTERY) {
			Battery = FULL_BATTERY;
		}
		return Battery;
	}

	// Muda a imagem segundo a direcao dada 
	public void move(int key) {
		Direction direction = Direction.directionFor(key);
		switch (direction) {
			case UP:
				imageName = "Empilhadora_U";
			break;
			case DOWN:
				imageName = "Empilhadora_D";
			break;
			case LEFT:
				imageName = "Empilhadora_L";
			break;
			case RIGHT:
				imageName = "Empilhadora_R";
			break;
	
			default:
				imageName = "Empilhadora_U";
			break;
		}
	}
	
	// Move a empilhadora para a direcao dada, se estiver dentro dos limites
	public void driveTo(Direction direction) {
		Point2D newPosition = getPosition().plus(direction.asVector());
		if (newPosition.getX()>=0 && newPosition.getX()<10 && newPosition.getY()>=0 && newPosition.getY()<10 ){
			setPosition(newPosition);
			Battery--;
			if( Battery == 0 ) {
				GameEngine.getInstance().infoBox("Click SPACE for restart ", "You ran out of battery :(");
				GameEngine.getInstance().restartGame(FIRST_LEVEL);
			}
		}
	}

	public void pickUpBattery() {
		Iterator<GameElement> iterator = GameEngine.getInstance().getGameElementsList().iterator();
		while (iterator.hasNext()) {
			GameElement item = iterator.next();
			if (item instanceof Bateria) {
				if (item.getPosition().equals(this.getPosition())) {
					this.addBattery(BATTERY_RELOAD);
					iterator.remove();
					GameEngine.getInstance().getGui().removeImage(item);
				}
			}
		}
	}

	public void pickUpHammer() {
		Iterator<GameElement> iterator = GameEngine.getInstance().getGameElementsList().iterator();
		while (iterator.hasNext()) {
			GameElement item = iterator.next();
			if (item instanceof Martelo) {
				if (item.getPosition().equals(this.getPosition())) {
					this.setHammer(true);
					iterator.remove();
					GameEngine.getInstance().getGui().removeImage(item);
				}
			}
		}
	}
	
}