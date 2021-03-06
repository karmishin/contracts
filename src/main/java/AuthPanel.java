import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;

public class AuthPanel extends JPanel {
    private final Container parent;
    private final CardLayout cardLayout;
    private final JLabel usernameLabel = new JLabel("Логин:");
    private final JLabel passwordLabel = new JLabel("Пароль:");
    private final JTextField usernameField = new JTextField(10);
    private final JTextField passwordField = new JTextField(10);
    private final JButton loginButton = new JButton("Войти");

    public AuthPanel(Container parent) {
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

        loginButton.addActionListener(e -> new AuthWorker().execute());

        setVisible(true);
    }

    class AuthWorker extends SwingWorker {
        Connection connection = null;

        private boolean checkPassword(String dbPassword, String dbSalt) throws NoSuchAlgorithmException, InvalidKeySpecException {
            Base64.Encoder encoder = Base64.getEncoder();
            Base64.Decoder decoder = Base64.getDecoder();

            byte[] salt = decoder.decode(dbSalt);

            KeySpec spec = new PBEKeySpec(passwordField.getText().toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            String hash = encoder.encodeToString(factory.generateSecret(spec).getEncoded());

            return hash.equals(dbPassword);
        }

        @Override
        protected Object doInBackground() throws SQLException {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:praktika.db");
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);

                // логин: admin, пароль: 1234
                statement.executeUpdate("create table if not exists users (id integer primary key autoincrement, name text unique, password text, salt text, role integer); ");
                statement.executeUpdate("insert or ignore into users values (null, 'admin', 'CmfAFCruDmLrggf9bTPaUg==', '99glxR8/XKldZDl6ADJl7A==', 0)");
                ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE name = '" + usernameField.getText() + "'");

                if (rs.next() && checkPassword(rs.getString("password"), rs.getString("salt"))) {
                    Main.currentUserName = rs.getString("name");
                    Main.currentUserRole = rs.getInt("role");
                    parent.add(new MenuPanel(parent), "Menu");
                    System.out.println("Вход выполнен.");
                    cardLayout.next(parent);
                } else {
                    JOptionPane.showMessageDialog(parent, "Неправильно введён логин или пароль");
                }

            } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            } finally {
                connection.close();
            }

            return null;
        }

        @Override
        protected void done() {
            super.done();
        }
    }
}
