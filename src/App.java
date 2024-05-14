
// biblioteka, skirta UI pagražinimui
import javax.swing.*;

public class App {
    public static void main(String[] args) {

        // nustatome žaidimo lango parametrus
        int boardWidth = 1200;
        int boardHeight = boardWidth;

        // JFrame - įrankis aplikacijos langui sukurti
        JFrame frame = new JFrame("Snakey");
        // nustatome aplikacijos parametrus
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
