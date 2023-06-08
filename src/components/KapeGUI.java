package components;

import components.Custom.Icons.Icons;
import components.Custom.Panels.SearchPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

public class KapeGUI extends JFrame {
    private KapeTable table;
    private Vector<String> columnNames;
    public KapeGUI(){}

    public void init() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(750, 750));
        this.setLocationRelativeTo(null);
        this.setIconImage(Icons.LOGO);
        this.setTitle("Inventory");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Fordina INVENTORY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        ImageIcon listIcon = new ImageIcon(Icons.list);
        JLabel listIconLabel = new JLabel(listIcon);
        titleLabel.setIconTextGap(5); // Add some spacing between the icon and the label text
        titleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        titleLabel.setVerticalTextPosition(SwingConstants.CENTER);
        titleLabel.setIcon(listIcon);

        Dimension buttonSize = new Dimension(100, 50);

        JButton addButton = new JButton(new ImageIcon(Icons.add));
        addButton.setToolTipText("Add Product");
        addButton.addActionListener(e -> showAddProductDialog());
        addButton.setPreferredSize(buttonSize);

        JButton deleteButton = new JButton(new ImageIcon(Icons.delete));
        deleteButton.setToolTipText("Delete Product");
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int productId = (int) table.getValueAt(selectedRow, 0);
                int choice = JOptionPane.showConfirmDialog(KapeGUI.this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    deleteProduct(productId);
                    refreshTableData();
                }
            } else {
                JOptionPane.showMessageDialog(KapeGUI.this, "Please select a row to delete.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton updateButton = new JButton(new ImageIcon(Icons.update));
        updateButton.setToolTipText("Update Product Information");
        updateButton.addActionListener(e -> updateProduct());
        updateButton.setPreferredSize(buttonSize);

        JButton refreshButton = new JButton(new ImageIcon(Icons.refresh));
        refreshButton.setToolTipText("Refresh");
        refreshButton.addActionListener(e -> refreshTableData());
        refreshButton.setPreferredSize(buttonSize);

        Vector<Vector<Object>> data = fetchDataFromDatabase();
        columnNames = new Vector<>();
        columnNames.add("Product ID");
        columnNames.add("Product Name");
        columnNames.add("Price");
        columnNames.add("Quantity");

        table = new KapeTable(data, columnNames);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new SearchPanel(table), gbc);

        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(table), gbc);

        JButton sellProductButton = new JButton(new ImageIcon(Icons.sell));
        sellProductButton.setToolTipText("Sell Product");
        sellProductButton.addActionListener(e -> showSellProductWindow());
        sellProductButton.setPreferredSize(buttonSize);

        JButton salesHistoryButton = new JButton(new ImageIcon(Icons.history));
        salesHistoryButton.setToolTipText("Sales History");
        salesHistoryButton.addActionListener(e -> openSalesHistory());
        salesHistoryButton.setPreferredSize(buttonSize);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraints.insets = new Insets(15, 10, 15, 10);

        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonPanel.add(addButton, buttonConstraints);

        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 0;
        buttonPanel.add(deleteButton, buttonConstraints);

        buttonConstraints.gridx = 2;
        buttonConstraints.gridy = 0;
        buttonPanel.add(updateButton, buttonConstraints);

        buttonConstraints.gridx = 3;
        buttonConstraints.gridy = 0;
        buttonPanel.add(sellProductButton, buttonConstraints);

        buttonConstraints.gridx = 4;
        buttonConstraints.gridy = 0;
        buttonPanel.add(salesHistoryButton, buttonConstraints);

        buttonConstraints.gridx = 5;
        buttonConstraints.gridy = 0;
        buttonPanel.add(refreshButton, buttonConstraints);

        mainPanel.add(buttonPanel);

        this.setContentPane(mainPanel);
        this.setVisible(true);
    }

    private void showSellProductWindow() {
        SellProductWindow sellProductWindow = new SellProductWindow();
        sellProductWindow.populateTable(fetchDataFromDatabase(), columnNames);
        sellProductWindow.setVisible(true);
    }

    /**
     * Opens the transaction history window.
     */
    private void openSalesHistory() {
        TransactionHistory transactionHistoryWindow = new TransactionHistory();
        transactionHistoryWindow.initHistory();
        transactionHistoryWindow.setVisible(true);
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

            // Delete related sales rows first
            PreparedStatement deleteSalesPs = con.prepareStatement("delete from sales where product_id = ?");
            deleteSalesPs.setInt(1, productId);
            deleteSalesPs.executeUpdate();

            // Delete the product
            PreparedStatement deleteProductPs = con.prepareStatement("delete from products where product_id = ?");
            deleteProductPs.setInt(1, productId);
            deleteProductPs.executeUpdate();

            JOptionPane.showMessageDialog(KapeGUI.this, "Product deleted successfully.", "Product Deleted", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void refreshTableData() {
        Vector<Vector<Object>> data = fetchDataFromDatabase();
        DefaultTableModel tableModel = table.getModel();
        tableModel.setDataVector(data, columnNames);
    }

    /**
     * Displays the Add Product dialog and refreshes the table data in the main frame after disposing the Add Product dialog.
     */
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
            try(ResultSet rs = statement.executeQuery("select * from products")){
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