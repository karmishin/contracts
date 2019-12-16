import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

public class ContractsPanel extends JPanel {
    Container parent;
    CardLayout cardLayout;
    Vector<String> columnNames = new Vector<>(Arrays.asList(new String[]{"ID", "Компания", "Дата", "Лицевой счёт", "Сумма"}));
    Vector<Vector<Object>> data = new Vector<>();
    JTable table;
    JButton backButton = new JButton("<< Вернуться");
    JButton deleteButton = new JButton("Удалить");

    public ContractsPanel(Container parent) {
        this.parent = parent;
        this.cardLayout = (CardLayout) parent.getLayout();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        SelectWorker selectWorker = new SelectWorker();
        selectWorker.execute();
        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        add(scrollPane);

        backButton.addActionListener(e -> {
            cardLayout.previous(parent);
        });
        add(backButton);

        deleteButton.addActionListener(e -> {
            if (table.getSelectedRow() != -1) {
                DeleteWorker deleteWorker = new DeleteWorker();
                deleteWorker.execute();
            }
        });
        add(deleteButton);
    }

    class SelectWorker extends SwingWorker {
        private Connection connection = null;

        @Override
        protected Object doInBackground() throws SQLException {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:praktika.db");
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                ResultSet rs = statement.executeQuery("SELECT * FROM contracts");

                int i;
                for (i = 0; rs.next(); i++) {
                    Vector<Object> row = new Vector<>();
                    for (int j = 1; j <= 5; j++) {
                        row.add(rs.getObject(j));
                    }
                    data.add(row);
                    System.out.println(row);
                }
                System.out.println("Загружено " + i + " контрактов.");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(parent, e.getMessage());
            } finally {
                connection.close();
            }

            return null;
        }
    }

    class DeleteWorker extends SwingWorker {

        @Override
        protected Object doInBackground() throws SQLException {
            Connection connection = null;

            try {
                int selectedRow = table.getSelectedRow();

                connection = DriverManager.getConnection("jdbc:sqlite:praktika.db");
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                statement.executeUpdate("DELETE FROM contracts WHERE id=" + (selectedRow + 1));
                table.removeRowSelectionInterval(selectedRow, selectedRow);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(parent, e.getMessage());
            } finally {
                connection.close();
            }

            return null;
        }
    }
}
