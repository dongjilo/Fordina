package components;

import components.Custom.Panels.SearchPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

public class SellProductWindow extends JFrame {
    private KapeTable table;
    private Vector<String> columnNames;
    private DefaultTableModel tableModel;
    private JSpinner quantitySpinner;

    public SellProductWindow(){
        init();
    }

    private void init() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(750, 750));
        this.setLocationRelativeTo(null);
        this.setTitle("Sell Product");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel titleLabel = new JLabel("Sell Product");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, gbc);

        tableModel = new customTableModel();
        table = new KapeTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        SearchPanel search = new SearchPanel(table);
        gbc.insets = new Insets(0,0,0,250);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(search, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 20, 20, 20);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 0, 20);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        mainPanel.add(quantitySpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 20, 20, 20);

        JButton sellButton = new JButton("Sell");
        sellButton.addActionListener(e -> sellSelectedProduct());
        mainPanel.add(sellButton, gbc);

        this.setContentPane(mainPanel);
    }


    public void populateTable(Vector<Vector<Object>> data, Vector<String> columnNames) {
        this.columnNames = columnNames;
        tableModel.setDataVector(data, columnNames);
    }
    private void sellSelectedProduct(){
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int rowIndex = table.convertRowIndexToModel(selectedRow); // Convert the view index to the model index

            int productId = (int) table.getValueAt(rowIndex, 0);
            String productName = (String) table.getValueAt(rowIndex, 1);
            BigDecimal productPrice = (BigDecimal) table.getValueAt(rowIndex, 2);
            int quantity = (int) table.getValueAt(rowIndex, 3);

            // Perform the necessary operations to record the transaction and update the database
            insertData(productId, productName, productPrice, quantity);
            refreshTableData();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to sell.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void refreshTableData() {
        try {
            Connection con = DBConnector.getInstance().getConnection();
            String selectQuery = "select * from products";
            PreparedStatement ps = con.prepareStatement(selectQuery);
            ResultSet rs = ps.executeQuery();

            // Create a new data vector to hold the updated table data
            Vector<Vector<Object>> data = new Vector<>();

            // Iterate through the result set and populate the data vector
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("product_id"));
                row.add(rs.getString("product_name"));
                row.add(rs.getBigDecimal("price"));
                row.add(rs.getInt("quantity"));
                data.add(row);
            }

            // Update the table model with the new data
            tableModel.setDataVector(data, columnNames);

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error refreshing table data:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void insertData(int productId, String productName, BigDecimal productPrice, int quantity){
        try {
            Connection con = DBConnector.getInstance().getConnection();

            String insertQuery = "insert into sales (product_id, product_name, price, quantity, sale_date) values (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(insertQuery);
            ps.setInt(1, productId);
            ps.setString(2, productName);
            ps.setBigDecimal(3, productPrice);

            int quantityToSell = (int) quantitySpinner.getValue();
            ps.setInt(4, quantityToSell);

            Timestamp salesDate = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(5, salesDate);

            // Checks if there is enough products to sell
            if (quantity - quantityToSell < 0) {
                JOptionPane.showMessageDialog(this, "Not enough products!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                ps.executeUpdate();
                updateProductQuantity(quantityToSell, productId);
                showReceipt(productName, productPrice, quantityToSell, salesDate);
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error selling product:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReceipt(String productName, BigDecimal productPrice, int quantityToSell, Timestamp salesDate) {
        JOptionPane.showMessageDialog(this, "Product Sold: " + productName + "\nPrice: " + productPrice +
                "\nQuantity: " + quantityToSell + "\nSales Date: " + salesDate, "Product Sold", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateProductQuantity(int quantityToSell, int productId) {
        try {
            Connection con = DBConnector.getInstance().getConnection();

            String updateQuery = "update products set quantity = quantity - ? where product_id = ?";
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setInt(1, quantityToSell);
            ps.setInt(2, productId);

            ps.executeUpdate();

            con.close();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Error updating product quantity:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
