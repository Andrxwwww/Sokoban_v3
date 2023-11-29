package pt.iscte.poo.sokobanstarter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.utils.Point2D;

public class Level {

    public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;

    private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
    public GameEngine gameEngine = GameEngine.getInstance();

    private List<GameElement> gameElementsList = new ArrayList<>();

    public List<GameElement> getGameElementsList() {
        return this.gameElementsList;
    }

    public Level(int num_level){
        createLevel(num_level);
    }

    // funcao que cria o nivel
	public void createLevel(int level_num) {
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

	// funcao que dado um gameElement ele vai adicionar a lista
	private void addGameElementToGUI(GameElement gameElement) {
		if (gameElement instanceof Caixote || gameElement instanceof Palete 
		|| gameElement instanceof ParedeRachada || gameElement instanceof Bateria 
		|| gameElement instanceof Martelo || gameElement instanceof Buraco) {
			gameElementsList.add(gameElement);
			gameElementsList.add(GameElement.create(' ', gameElement.getPosition()));
		} else if (gameElement instanceof Empilhadora) {
			gameEngine.bobcat = (Empilhadora) gameElement;
			gameElementsList.add(gameEngine.bobcat);
			gameElementsList.add(GameElement.create(' ', gameElement.getPosition()));
		} else if (gameElement instanceof Alvo) {
			gameElementsList.add(gameElement);
			gameEngine.numberOfTargets++;
		} else {
			gameElementsList.add(gameElement);
		}
	}
    
}
