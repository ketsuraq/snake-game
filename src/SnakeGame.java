import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
// importuojame reikalingas klases sklandžiam kodo veikimui

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    // vidinė klasė reprezentuojanti atskirą plytelę
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 50;

    // sukuriame atskirą gyvatės galvos plytelę, nes nuo jos atliekami visi veiksmai
    Tile snakeHead;
    // sukuriame gyvatės kūno ir maisto plyteles
    ArrayList<Tile> snakeBody;
    Tile food;

    Random random;

    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    // klasė, kuri nustato pradinius žaidimo parametrus
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        // sukuriame žaidimo ciklą, kuris kartojasi kas 200ms
        gameLoop = new Timer(200, this);
        gameLoop.start();
    }

    // nupiešiame žaidimo elementus
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g); // kviečiame metodą
    }

    // metodas, kuris nupiešia žaidimo elementus
    public void draw(Graphics g) {
        // nupiešiame plytelių kraštines
        for (int i = 0; i < boardWidth / tileSize; ++i) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
        }
        for (int i = 0; i < boardHeight / tileSize; ++i) {
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // nustatome spalvą ir ja užpildome elementus
        g.setColor(Color.magenta);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.setColor(Color.orange);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        g.setColor(Color.magenta);
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        // nustatome teksto parametrus ir turinį
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.setColor(Color.magenta);
            g.drawString("Game over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    // nustatome atsitiktinę vietą maisto plytelei
    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    // tikriname, ar tarp plytelių buvo atsitrenkimas
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // jei gyvatėlė atsitrenkė į maisto plytelę, prie kno pridedama nauja dalis
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            // vėl atsitiktinai padedame maistą
            placeFood();
        }

        // algoritmas, kuris judina gyvatę. kiekviena dalis pajuda ten, kur byvo
        // ankstesnė gyvatės dalis
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // tikriname, ar įvyko atsitrenkimas tarp gyvatės dalių
        for (int i = 0; i < snakeBody.size(); ++i) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        // tikriname, ar įvyko atsitrenkimas tarp gyvatės ir lentelės krašto
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
                snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    // po kiekvieno įvykio perpiešiame lentą ir tikriname validumą
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    // metodas, kuris iškviečiamas po kiekvieno klavišo paspaudimo
    @Override
    public void keyPressed(KeyEvent e) {
        // su kiekviena sąlyga pakeičiame judėjimo kryptį. taip pat tikriname, kad
        // gyvatė negalėtų judėti priešinga kryptimi.
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityY = 0;
            velocityX = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityY = 0;
            velocityX = 1;
        }
    }
}
