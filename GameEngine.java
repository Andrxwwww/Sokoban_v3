package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class GameEngine implements Observer {

	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;

	private static GameEngine INSTANCE; // Referencia para o unico objeto GameEngine (singleton)
	private ImageMatrixGUI gui; // Referencia para ImageMatrixGUI (janela de interface com o utilizador)
	private List<GameElement> gameElementsList; // Lista de GameElements
	private Empilhadora bobcat; // Referencia para a empilhadora
	private int level_num; // Numero do nivel a carregar
	public int numberOfTargetsWithBoxes; // Numero de alvos com caixas
	public int numberOfTargets; // Numero de alvos
	private int moves; // Numero de movimentos da empilhadora

	public final int FIRST_LEVEL = 5;

	private GameEngine() {
		gameElementsList = new ArrayList<>();
	}

	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	public ImageMatrixGUI getGui() {
		return this.gui;
	}
	
	public List<GameElement> getGameElementsList() {
		return this.gameElementsList;
	}

	public void start() {

		gui = ImageMatrixGUI.getInstance(); // 1. obter instancia ativa de ImageMatrixGUI
		gui.setSize(GRID_HEIGHT, GRID_WIDTH); // 2. configurar as dimensoes
		gui.registerObserver(this); // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go(); // 4. lancar a GUI

		this.level_num = FIRST_LEVEL;
		this.numberOfTargetsWithBoxes = 0;
		this.numberOfTargets = 0;
		this.moves = 0;

		createLevel(level_num); // criar o armazem
		sendImagesToGUI(); // enviar as imagens para a GUI
	}

	@Override
	public void update(Observed source) {
		int key = gui.keyPressed(); // obtem o codigo da tecla pressionada

		otherKeyInteractions(key); 
		gui.update();
		if (bobcat != null && Direction.isDirection(key)) {
			bobcatKeyMechanics(key);
			bobcat.pickUpBattery();
			bobcat.pickUpHammer();
			levelIncrementer();
		}
	}

	public GameElement getGameElementAtPosition(Point2D position) {
		for (GameElement gameElement : gameElementsList) {
			if (gameElement.getPosition().equals(position)) {
				return gameElement;
			}
		}
		return null;
	}

	// --- FUNCTIONS FOR KEYS ---
	public void otherKeyInteractions(int key) {
		if (key == KeyEvent.VK_SPACE) {
			restartGame(FIRST_LEVEL);
		}
	}

	public void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}

	private void levelIncrementer() {
		if (numberOfTargetsWithBoxes == numberOfTargets) {
			this.level_num++;
			if (this.level_num > 6) {
				infoBox("press SPACE for restart or ENTER for exit", "You Won the Game :D !!");
				System.exit(0);
			} else {
				infoBox("press ENTER for next level", "Congrats!!");
			}
			restartGame(this.level_num++);
		}
	}

	public void restartGame(int level_num) {
		bobcat.setHammer(false);

		gui.clearImages(); // apaga todas as imagens atuais da GUI
		gameElementsList.clear(); // apaga todos os elementos da lista de elementos
		numberOfTargetsWithBoxes = 0;
		numberOfTargets = 0	;
		moves = 0;
		this.level_num = level_num; // reecria o primeiro nivel

		createLevel(level_num);
		sendImagesToGUI();
	}

	// funcao que cria o nivel
	private void createLevel(int level_num) {
		
		try {
			Scanner scanner = new Scanner(new File("levels\\level" + level_num + ".txt"));
			while (scanner.hasNextLine()) {
				for (int y = 0; y < GRID_HEIGHT; y++) { // loop pela altura da Tela
					String line = scanner.nextLine(); // meter a string/linha numa var
					for (int x = 0; x < line.length(); x++) {// loop pela a length da palavra que vai acabar por ser alargura da tela tambem
						GameElement gameElement = GameElement.create(line.charAt(x), new Point2D(x, y)); // criar o gameElement
						addGameElementToGUI(gameElement); // adicionar a lista correspondente
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) { // se nao encontrar o ficheiro entao
			System.err.println("Erro: ficheiro/level não encontrado :(");
		}
		gui.update();
	}

	// funcao que dado um gameElement ele vai adicionar a lista correspondente
	// (tinha-se de fazer com gameElements é o porquê de ter feito asssim)
	private void addGameElementToGUI(GameElement gameElement) {
		if (gameElement instanceof Caixote || gameElement instanceof Palete 
		|| gameElement instanceof ParedeRachada || gameElement instanceof Bateria 
		|| gameElement instanceof Martelo || gameElement instanceof Buraco) {
			gameElementsList.add(gameElement);
			gameElementsList.add(GameElement.create(' ', gameElement.getPosition()));
		} else if (gameElement instanceof Empilhadora) {
			bobcat = (Empilhadora) gameElement;
			gameElementsList.add(bobcat);
			gameElementsList.add(GameElement.create(' ', gameElement.getPosition()));
		} else if (gameElement instanceof Alvo) {
			gameElementsList.add(gameElement);
			numberOfTargets++;
		} else {
			gameElementsList.add(gameElement);
		}
	} 

	private void bobcatKeyMechanics(int key) {

		if (bobcatCollisionsChecker()) {
			bobcat.move(key);
			bobcat.driveTo(Direction.directionFor(key));
			bobcat.pickUpBattery();
			bobcat.pickUpHammer();
			moves++;
			gui.setStatusMessage("Sokoban" + " - Level " + level_num + " - " + "Battery: " + bobcat.getBattery() + " - " + "Moves: " + moves);
			//System.out.println(bobcat.getBattery()); // debug para ver a bateria se estácorreta
			System.out.println( "Numero total de Targets: " + numberOfTargets); // debug para ver o numero de alvos
			System.out.println( "Numero de Caixotes nos alvos: " + numberOfTargetsWithBoxes); // debug para ver o numero de alvos com caixotes
		}
	}

	/* funcao que checka se a empilhadora pode passar ou nao 
	[ se for um caixote ou uma palete entao move o caixote ou a palete + a empilhadora 
	, se for parede ou paredeRachada entao nao passa ] */
	private boolean bobcatCollisionsChecker() {
		for (GameElement ge : gameElementsList) {
			if (bobcat.nextPosition(gui.keyPressed()).equals(ge.getPosition())) {
				if (ge instanceof Caixote || ge instanceof Palete) {
					// verifica se o caixote ou a palete podem mover
					if (collidableCollisionChecker(ge)) {
						// verifica se esse caixote está num alvo

						// TODO: meter na classe Caixote ou palete	
						if (isMovableOnTarget("Alvo", "Caixote")){
							numberOfTargetsWithBoxes++;
						} else if (numberOfTargetsWithBoxes > 0 && numberOfTargetsWithBoxes <= numberOfTargets 
							&& isSomethingAbove(ge.getPosition(), "Alvo")){ // se o caixote sair do alvo
							numberOfTargetsWithBoxes--;
						}
						
						ge.setPosition(ge.getPosition().plus(Direction.directionFor(gui.keyPressed()).asVector()));
						bobcat.addBattery(-1);
						return true;
					} else {
						return false; // se o caixote ou a palete nao mover entao , a empilhadora nao se move tambem
					}
				} else if (ge instanceof Buraco){                   
					((Buraco)ge).interactWith(bobcat);
					return false;

				} else if (ge instanceof ParedeRachada) {
					// TODO: meter na classe Empilhadora
					if (bobcat.hasHammer()) {
						gui.removeImage(ge);
						gameElementsList.remove(ge);
						return true;
					} else {
						return false;
					}         

				} else if (ge instanceof Parede) {
					return false;
				}
			}
		}
		return true;
	}

	// funcao que verifica se um caixote ou uma palete pode mover
	private boolean collidableCollisionChecker (GameElement ge) {
		for (GameElement next_ge : gameElementsList){
			if (ge.getPosition().plus(Direction.directionFor(gui.keyPressed()).asVector()).equals(next_ge.getPosition())) {
				if (next_ge instanceof Caixote || next_ge instanceof Palete || next_ge instanceof ParedeRachada || next_ge instanceof Parede ) {
					return false;
				} else if (next_ge instanceof Buraco && ge instanceof Caixote) {
					bobcat.move(gui.keyPressed());
					bobcat.driveTo(Direction.directionFor(gui.keyPressed()));
					((Buraco)next_ge).interactWith((Caixote)ge);
					return true;
				} else if (next_ge instanceof Buraco && ge instanceof Palete) {
					((Buraco)next_ge).interactWith((Palete)ge);
					return true;
				}
			}
		}
		return true;
	}

	// funcao que verifica se um caixote ou uma palete está em cima de um alvo
	public boolean isMovableOnTarget(String target, String movable) {
		for (GameElement ge1 : gameElementsList) {
			if (ge1.getName().equals(target)) {
				for (GameElement ge2 : gameElementsList) {
					if (ge2.getName().equals(movable) && ge1.getPosition().equals(ge2.nextPosition(gui.keyPressed())) 
					&& bobcat.nextPosition(gui.keyPressed()).equals(ge2.getPosition())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isSomethingAbove(Point2D point , String name){
		for (GameElement ge : gameElementsList) {
			if (ge.getPosition().equals(point) && ge.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private void sendImagesToGUI() {

		gui.addImage(bobcat);

		for (GameElement ge : gameElementsList) {
			gui.addImage(ge);
		}
	}

}
