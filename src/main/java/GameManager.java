import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Random;

public class GameManager extends JPanel implements KeyListener, ActionListener {
    private String sourceUrl = "src\\main\\resources\\";
    int offsetX;
    int offsetY;
    int gameFieldWeight;
    int gameFieldHeight;
    int score = 0;
    int heightScore = 0;
    private int jump;


    private int[] snakeLengthX;
    private int[] snakeLengthY;

    private MoveDirect moveDirect;
    private MoveDirect tmpDirect;
    int[] availableXCoordinates;
    int[] availableYCoordinates;
    private Random random;
    private int foodXCoordinate;
    private int foodYCoordinate;

    private int enemyXCoordinate;
    private int enemyYCoordinate;

    private boolean gamePending;

    private int lengthOfSneak;
    private int moves;
    boolean gameOver = false;
    private ImageIcon titleImage;
    private ImageIcon food;
    private ImageIcon enemy;
    private ImageIcon rightMouth;
    private ImageIcon leftMouth;
    private ImageIcon upMouth;
    private ImageIcon downMouth;
    private ImageIcon snakeImage;
    int counter = 0;


    private Timer timer;
    private int daley = 100;

    public GameManager() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(daley, this);
        random = new Random();
        offsetX = 25;
        offsetY = 75;
        gameFieldWeight = 850;
        gameFieldHeight = 575;
        jump = 25;


        snakeLengthX = new int[gameFieldWeight / jump + gameFieldHeight / jump];
        snakeLengthY = new int[gameFieldWeight / jump + gameFieldHeight / jump];
        availableXCoordinates = new int[gameFieldWeight / jump];
        availableYCoordinates = new int[gameFieldHeight / jump];
        setUpAvailablePosition();
        restartGame();
        timer.start();


    }

    private void showFood() {
        int tmpX = availableXCoordinates[random.nextInt(availableXCoordinates.length - 1)];
        while (Arrays.asList(snakeLengthX).contains(tmpX)) {
            tmpX = availableXCoordinates[random.nextInt(availableXCoordinates.length - 1)];
            System.out.println("Looking X");
        }
        int tmpY = availableYCoordinates[random.nextInt(availableYCoordinates.length - 1)];
        while (Arrays.asList(snakeLengthY).contains(tmpY)) {
            System.out.println("looking Y");
            tmpY = availableYCoordinates[random.nextInt(availableYCoordinates.length - 1)];
        }
        foodXCoordinate = tmpX;
        foodYCoordinate = tmpY;
    }

    private void showEnemy() {
        int tmpX = availableXCoordinates[random.nextInt(availableXCoordinates.length - 1)];
        int tmpY = availableYCoordinates[random.nextInt(availableYCoordinates.length - 1)];

        while (Arrays.asList(snakeLengthX).contains(tmpX)
                || (foodYCoordinate == tmpY && foodXCoordinate == tmpX)) {
            tmpX = availableXCoordinates[random.nextInt(availableXCoordinates.length - 1)];
        }

        while (Arrays.asList(snakeLengthY).contains(tmpY)
                || (foodYCoordinate == tmpY && foodXCoordinate == tmpX)) {

            tmpY = availableYCoordinates[random.nextInt(availableYCoordinates.length - 1)];
        }

        enemyXCoordinate = tmpX;
        enemyYCoordinate = tmpY;
    }

    private void setUpAvailablePosition() {
        int tmpX = offsetX;

        for (int i = 0; i < availableXCoordinates.length; i++) {
            availableXCoordinates[i] = tmpX;
            tmpX += jump;
        }

        int tmpY = offsetY;

        for (int j = 0; j < availableYCoordinates.length; j++) {
            availableYCoordinates[j] = tmpY;
            tmpY += jump;

        }
    }

    private void restartGame() {
        moves = 0;
        counter = 0;
        score = 0;
        lengthOfSneak = 3;
        moveDirect = MoveDirect.NONE;
        tmpDirect = MoveDirect.RIGHT;

        for (int i = lengthOfSneak - 1; i >= 0; i--) {
            snakeLengthX[i] = jump * (lengthOfSneak - i);
            if (i < snakeLengthY.length)
                snakeLengthY[i] = offsetY + jump;
        }

        showFood();
        showEnemy();
        gameOver = false;
        gamePending = true;
    }

    public void paint(Graphics g) {
        counter++;
        printGameBoard(g);
        printStatistics(g);

        for (int i = 0; i < lengthOfSneak; i++) {
            if (i == 0 && moveDirect.equals(MoveDirect.RIGHT)) {
                rightMouth = new ImageIcon(sourceUrl + "rightmouth.png");
                rightMouth.paintIcon(this, g, snakeLengthX[i], snakeLengthY[i]);
            }
            if (i == 0 && moveDirect.equals(MoveDirect.LEFT)) {
                leftMouth = new ImageIcon(sourceUrl + "leftmouth.png");
                leftMouth.paintIcon(this, g, snakeLengthX[i], snakeLengthY[i]);
            }
            if (i == 0 && moveDirect.equals(MoveDirect.DOWN)) {
                downMouth = new ImageIcon(sourceUrl + "downmouth.png");
                downMouth.paintIcon(this, g, snakeLengthX[i], snakeLengthY[i]);
            }
            if (i == 0 && moveDirect.equals(MoveDirect.UP)) {
                upMouth = new ImageIcon(sourceUrl + "upmouth.png");
                upMouth.paintIcon(this, g, snakeLengthX[i], snakeLengthY[i]);
            }
            if (i != 0) {
                snakeImage = new ImageIcon(sourceUrl + "snakeimage.png");
                snakeImage.paintIcon(this, g, snakeLengthX[i], snakeLengthY[i]);
            }
            food = new ImageIcon(sourceUrl + "food.png");

            food.paintIcon(this, g, foodXCoordinate, foodYCoordinate);

            enemy = new ImageIcon(sourceUrl + "enemy.png");
            enemy.paintIcon(this, g, enemyXCoordinate, enemyYCoordinate);
        }

        for (int b = 1; b < lengthOfSneak; b++) {
            if ((snakeLengthY[b] == snakeLengthY[0] && snakeLengthX[b] == snakeLengthX[0])
                    || (snakeLengthY[b] == enemyYCoordinate && snakeLengthX[b] == enemyXCoordinate)) {
                killSnake(g);
                printRequiem(g);
            }
            if (snakeLengthX[0] == foodXCoordinate && snakeLengthY[0] == foodYCoordinate) {
                feedSnake();
            }
            if (counter % 100 == 0) {
                showEnemy();
            }
            if (!gamePending && !gameOver){
                printPauseBoard(g);
            }
        }

        g.dispose();
    }

    private void printPauseBoard(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("PAUSE", offsetX + gameFieldWeight / 3, offsetY + gameFieldHeight / 3);

        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Tap space or move to resume game", offsetX + gameFieldWeight / 3 -70, offsetY + gameFieldHeight / 3 + 40);
    }

    private void printRequiem(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("GAME OVER", offsetX + gameFieldWeight / 3, offsetY + gameFieldHeight / 3);

        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Tap enter to restart", offsetX + gameFieldWeight / 3 + 50, offsetY + gameFieldHeight / 3 + 40);
    }

    private void printStatistics(Graphics g) {
        //draw score
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN, 15));
        g.drawString("score:" + score, gameFieldWeight - 70, 30);

        //draw length
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN, 15));
        g.drawString("length:" + lengthOfSneak, gameFieldWeight - 70, 50);

        //draw record
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 15));
        g.drawString("record:" + heightScore, offsetX + 20, 40);
    }

    private void printGameBoard(Graphics g) {
        //draw title image border
        g.setColor(Color.white);
        g.drawRect(offsetX - 1, 10, gameFieldWeight + 1, 55);

        //draw the title image
        titleImage = new ImageIcon(sourceUrl + "snake_title.jpg");
        titleImage.paintIcon(this, g, offsetX, 11);

        //draw border for gameplay
        g.setColor(Color.WHITE);
        g.drawRect(offsetX - 1, offsetY - 1, gameFieldWeight + 1, gameFieldHeight + 2);

        //draw background for the gameplay
        g.setColor(Color.black);
        g.fillRect(offsetX, offsetY, gameFieldWeight, gameFieldHeight);
        rightMouth = new ImageIcon(sourceUrl + "rightmouth.png");
        rightMouth.paintIcon(this, g, snakeLengthX[0], snakeLengthY[0]);
    }

    private void feedSnake() {
        showFood();
        score++;
        lengthOfSneak++;
    }

    private void killSnake(Graphics g) {
        heightScore = Math.max(score, heightScore);
        moveDirect = MoveDirect.NONE;
        gamePending = false;
        gameOver = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gamePending) {
                pauseGame();
            } else {
                resumeGame();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            restartGame();
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            changeDirect(MoveDirect.RIGHT);
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            changeDirect(MoveDirect.LEFT);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            changeDirect(MoveDirect.UP);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            changeDirect(MoveDirect.DOWN);
        }

    }

    private void resumeGame() {
        if (moveDirect.equals(MoveDirect.NONE)) {
            moveDirect = tmpDirect;
        }

        tmpDirect = MoveDirect.NONE;
        gamePending = true;
    }

    private void pauseGame() {
        tmpDirect = moveDirect;
        moveDirect = MoveDirect.NONE;
        gamePending = false;
    }

    private void changeDirect(MoveDirect changeDirect) {
        if (!gamePending) {
            resumeGame();
        }
        if (!gameOver) {
            moves++;
            if (changeDirect.equals(MoveDirect.RIGHT) && !moveDirect.equals(MoveDirect.LEFT)) {
                moveDirect = changeDirect;
            }
            if (changeDirect.equals(MoveDirect.LEFT) && !moveDirect.equals(MoveDirect.RIGHT)) {
                moveDirect = changeDirect;
            }
            if (changeDirect.equals(MoveDirect.UP) && !moveDirect.equals(MoveDirect.DOWN)) {
                moveDirect = changeDirect;
            }
            if (changeDirect.equals(MoveDirect.DOWN) && !moveDirect.equals(MoveDirect.UP)) {
                moveDirect = changeDirect;
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (moveDirect.equals(MoveDirect.RIGHT)) {
            moveRight();
        }
        if (moveDirect.equals(MoveDirect.LEFT)) {
            leftMove();
        }
        if (moveDirect.equals(MoveDirect.DOWN)) {
            downMove();
        }
        if (moveDirect.equals(MoveDirect.UP)) {
            upMove();
        }
        repaint();

    }

    private void upMove() {
        for (int r = lengthOfSneak - 1; r >= 0; r--) {
            snakeLengthX[r + 1] = snakeLengthX[r];
        }

        for (int r = lengthOfSneak; r >= 0; r--) {
            if (r == 0) {
                snakeLengthY[r] = snakeLengthY[r] - jump;
            } else {
                snakeLengthY[r] = snakeLengthY[r - 1];
            }
            if (snakeLengthY[0] < offsetY) {
                snakeLengthY[r] = offsetY + gameFieldHeight - jump;
            }
        }
    }

    private void downMove() {
        for (int r = lengthOfSneak - 1; r >= 0; r--) {
            snakeLengthX[r + 1] = snakeLengthX[r];
        }

        for (int r = lengthOfSneak; r >= 0; r--) {
            if (r == 0) {
                snakeLengthY[r] = snakeLengthY[r] + jump;
            } else {
                snakeLengthY[r] = snakeLengthY[r - 1];
            }
            if (snakeLengthY[0] > offsetY + gameFieldHeight - jump) {
                snakeLengthY[r] = offsetY;
            }
        }
    }

    private void leftMove() {
        for (int r = lengthOfSneak - 1; r >= 0; r--) {
            snakeLengthY[r + 1] = snakeLengthY[r];
        }

        for (int r = lengthOfSneak; r >= 0; r--) {
            if (r == 0) {
                snakeLengthX[r] = snakeLengthX[r] - jump;
            } else {
                snakeLengthX[r] = snakeLengthX[r - 1];
            }
            if (snakeLengthX[0] < offsetX) {
                snakeLengthX[r] = gameFieldWeight;
            }
        }
    }

    private void moveRight() {
        for (int r = lengthOfSneak - 1; r >= 0; r--) {
            snakeLengthY[r + 1] = snakeLengthY[r];
        }

        for (int r = lengthOfSneak; r >= 0; r--) {
            if (r == 0) {
                snakeLengthX[r] = snakeLengthX[r] + jump;
            } else {
                snakeLengthX[r] = snakeLengthX[r - 1];
            }
            if (snakeLengthX[0] > 850) {
                snakeLengthX[r] = 25;
            }
        }
    }

    enum MoveDirect {
        RIGHT, LEFT, UP, DOWN, NONE;
    }

}

