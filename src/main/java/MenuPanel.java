import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private Container parent;
    private CardLayout cardLayout;

    public MenuPanel(Container parent) {
        this.parent = parent;
        this.cardLayout = (CardLayout) parent.getLayout();
    }
}
