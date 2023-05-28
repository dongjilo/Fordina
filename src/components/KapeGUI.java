package components;

import com.formdev.flatlaf.FlatDarkLaf;
import components.Custom.Panels.SearchPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class KapeGUI extends JFrame {

    private KapeTable table;
    private Vector<String> columnNames;

    public KapeGUI(){}

    public void init() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(750, 750));
        this.setLocationRelativeTo(null);
        this.setTitle("Inventory");

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        // Create and configure components
        JLabel titleLabel = new JLabel("Fordina Cafe INVENTORY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton addButton = new JButton("Add Product");

        addButton.addActionListener(e -> showAddProductDialog());

        JButton deleteButton;
        JButton sellButton;

        // Fetch data from the database
        Vector<Vector<Object>> data = fetchDataFromDatabase();

        // Define column names
        columnNames = new Vector<>();
        columnNames.add("Product ID");
        columnNames.add("Product Name");
        columnNames.add("Price");
        columnNames.add("Quantity");

        // Create the custom table with the table model
        table = new KapeTable(data, columnNames);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new SearchPanel(table), gbc);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(table), gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2, 10, 10));

        addButton = new JButton("Add Product");
        addButton.addActionListener(e -> showAddProductDialog());
        buttonPanel.add(addButton);

        deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int productId = (int) table.getValueAt(selectedRow, 0);
                deleteProduct(productId);
                refreshTableData();
            }else {
                JOptionPane.showMessageDialog(KapeGUI.this, "Please select a row to delete.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(deleteButton);

        JButton updateButton = new JButton("Update Product Information");
        updateButton.addActionListener(e -> updateProduct());
        buttonPanel.add(updateButton);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        mainPanel.add(buttonPanel, gbc);

        // Add the Sell Product, Transaction History and Refresh buttons
        sellButton = new JButton("Sell Product");

        sellButton.addActionListener(e -> openSellProductWindow());
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        mainPanel.add(sellButton, gbc);

        JButton salesButton = new JButton("Transaction History");
        salesButton.addActionListener(e -> openSalesHistory());
        gbc.gridx = 1;
        mainPanel.add(salesButton, gbc);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTableData());
        gbc.gridx = 2;
        mainPanel.add(refreshButton, gbc);

        // Set the main panel as the content pane
        this.setContentPane(mainPanel);
        this.setVisible(true);
    }

    private void openSalesHistory() {
        TransactionHistory transactionHistoryWindow = new TransactionHistory();
        transactionHistoryWindow.initHistory();
        transactionHistoryWindow.setVisible(true);
    }

    private void openSellProductWindow() {
        SellProductWindow sellProductWindow = new SellProductWindow();

        // Fetch data from the database
        Vector<Vector<Object>> data = fetchDataFromDatabase();

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Product ID");
        columnNames.add("Product Name");
        columnNames.add("Price");
        columnNames.add("Quantity");

        sellProductWindow.populateTable(data, columnNames);
        sellProductWindow.setVisible(true);
    }

    private void updateProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int productId = (int) table.getValueAt(selectedRow, 0);
            String existingName = (String) table.getValueAt(selectedRow, 1);
            BigDecimal existingPrice = (BigDecimal) table.getValueAt(selectedRow, 2);
            int existingQuantity = (int) table.getValueAt(selectedRow, 3);

            UpdateInfoDialog updateInfoDialog = new UpdateInfoDialog(this, productId, existingName, existingPrice, existingQuantity);
            updateInfoDialog.setVisible(true);

            refreshTableData();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to update.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteProduct(int productId) {
        try {
            Connection con = DBConnector.getInstance().getConnection();
            Statement statement = con.createStatement();
            String query = "DELETE FROM products WHERE product_id = " + productId;
            statement.executeUpdate(query);
            JOptionPane.showMessageDialog(KapeGUI.this, "Product deleted successfully.", "Product Deleted", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void refreshTableData() {
        Vector<Vector<Object>> data = fetchDataFromDatabase();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setDataVector(data, columnNames);
    }

    private void showAddProductDialog() {
        AddProductDialog addProductDialog = new AddProductDialog(this);
        addProductDialog.setVisible(true);
        refreshTableData();
    }

    private Vector<Vector<Object>> fetchDataFromDatabase() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            Connection konek = DBConnector.getInstance().getConnection();
            Statement statement = konek.createStatement();
            try(ResultSet rs = statement.executeQuery("SELECT * FROM products")){
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <=  rs.getMetaData().getColumnCount(); i++) {
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