import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AuthPanel extends JPanel {
    private JLabel usernameLabel = new JLabel("Логин:");
    private JLabel passwordLabel = new JLabel("Пароль:");
    private JTextField usernameField = new JTextField(10);
    private JTextField passwordField = new JTextField(10);
    private JButton loginButton = new JButton("Войти");

    public AuthPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        add(usernameLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        add(usernameField, c);

        c.gridx = 0;
        c.gridy = 1;
        add(passwordLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        add(passwordField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        add(loginButton, c);

        loginButton.addActionListener(e -> {
            AuthWorker authWorker = new AuthWorker();
            authWorker.execute();
        });

        setVisible(true);
    }

    class AuthWorker extends SwingWorker {
        Connection connection = null;

        @Override
        protected Object doInBackground() {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:praktika.db");
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                ResultSet rs = statement.executeQuery(
                        "SELECT * FROM user WHERE name = '"
                                + usernameField.getText()
                                + "' and password = '"
                                + passwordField.getText()
                                + "'");

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "Неверный логин или пароль!");
                } else {
                    System.out.println("name=" + rs.getString("name"));
                    System.out.println("password=" + rs.getString("password"));
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

            return null;
        }

        @Override
        protected void done() {
            super.done();
        }
    }
}
