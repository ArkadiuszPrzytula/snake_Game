import javax.swing.*;
import java.awt.*;

public class Main {
    public static final int windowWidth = 905;
    public static final int windowHeight = 700;

    public static void main(String[] args) {

        JFrame obj = new JFrame("Made by Arkadiusz Przytuła");
        GameManager gamePlay = new GameManager();

        obj.setBounds(10, 10, windowWidth, windowHeight);
        obj.setBackground(Color.DARK_GRAY);
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
    }
}
