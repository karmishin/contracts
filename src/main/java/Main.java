import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Main extends JFrame {

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> new Main().create());
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void create() {
        setLayout(new CardLayout());
        add(new AuthPanel());

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
