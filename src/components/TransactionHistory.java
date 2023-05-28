package components;

import components.Custom.Panels.SearchPanel;
import components.DBConnector;
import components.KapeTable;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class TransactionHistory extends JFrame {
    private KapeTable table;
    private Vector<String> columnNames;

    public TransactionHistory(){}

    public void initHistory(){

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(750,750);
        this.setLocationRelativeTo(null);
        this.setTitle("Transaction History");

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // Components
        JLabel titleLabel = new JLabel("Fordina Cafe Transaction History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Fetch data from database 
        Vector<Vector<Object>> data = fetchDataFromDatabase();

        // Define column names
        columnNames = new Vector<>();
        columnNames.add("Sales ID");
        columnNames.add("Product ID");
        columnNames.add("Product Name");
        columnNames.add("Price");
        columnNames.add("Quantity");
        columnNames.add("Sales Date");

        table = new KapeTable(data, columnNames);
        customTableModel tableModel = new customTableModel();
        table.setModel(tableModel);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0,10,0);
        panel.add(titleLabel);

        gbc.gridy = 1;
        panel.add(new SearchPanel(table), gbc);

        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(table), gbc);

        this.add(panel);
        this.setVisible(true);
    }

    private Vector<Vector<Object>> fetchDataFromDatabase() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            Connection konek = DBConnector.getInstance().getConnection();
            Statement statement = konek.createStatement();
            try (ResultSet rs = statement.executeQuery("select * from sales")) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        row.add(rs.getObject(i));
                    }
                    data.add(row);
                }
                return data;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
