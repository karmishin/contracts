import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private Container parent;
    private CardLayout cardLayout;
    private JLabel welcomeLabel = new JLabel();
    private JButton ordersButton = new JButton("Заказы");
    private JButton contractsButton = new JButton("Договоры");

    public MenuPanel(Container parent) {
        this.parent = parent;
        this.cardLayout = (CardLayout) parent.getLayout();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        welcomeLabel.setText("Добро пожаловать, " + Main.currentUserName);
        add(welcomeLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        ordersButton.addActionListener(e -> {
            cardLayout.show(parent, "Orders");
        });
        add(ordersButton, c);

        c.gridx = 0;
        c.gridy = 2;
        contractsButton.addActionListener(e -> {
            cardLayout.show(parent, "Contracts");
        });
        add(contractsButton, c);
    }
}
