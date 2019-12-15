import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {
    static String currentUserName;
    static int currentUserRole;
    CardLayout cardLayout;

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> new Main().create());
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void create() {
        JPanel cards = new JPanel();
        cardLayout = new CardLayout();
        cards.setLayout(cardLayout);
        cards.add(new AuthPanel(cards), "Auth");

        JFrame frame = new JFrame();
        frame.add(cards);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
