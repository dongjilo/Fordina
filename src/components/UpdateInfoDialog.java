package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateInfoDialog extends JDialog {
    private JTextField productIdField, productNameField, priceField, quantityField;
    private int productId;

    public UpdateInfoDialog(JFrame parent, int productId, String existingName, BigDecimal existingPrice, int existingQuantity) {
        super(parent, "Update Product", true);
        this.productId = productId;

        initComponents(existingName, existingPrice, existingQuantity);
    }

    private void initComponents(String existingName, BigDecimal existingPrice, int existingQuantity) {
        // Create and configure components
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        JLabel productIdLabel = new JLabel("Product ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(productIdLabel, gbc);

        productIdField = new JTextField(String.valueOf(productId));
        productIdField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(productIdField, gbc);

        JLabel nameLabel = new JLabel("Product Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        productNameField = new JTextField(existingName);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(productNameField, gbc);

        JLabel priceLabel = new JLabel("Price:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(priceLabel, gbc);

        priceField = new JTextField(String.valueOf(existingPrice));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(priceField, gbc);

        JLabel quantityLabel = new JLabel("Quantity:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(quantityLabel, gbc);

        quantityField = new JTextField(String.valueOf(existingQuantity));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(quantityField, gbc);

        JButton updateButton = new JButton("Update");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(updateButton, gbc);

        JButton cancelButton = new JButton("Cancel");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(cancelButton, gbc);

        // Configure dialog
        this.setContentPane(panel);
        this.pack();
        this.setSize(400, 275);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Add action listeners
        updateButton.addActionListener(e -> updateProduct());

        cancelButton.addActionListener(e -> dispose());
    }

    private void updateProduct() {
        // Retrieve the input values
        String productName = productNameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        // Validate the input values
        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all the fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update the product in the database
        try {
            Connection con = DBConnector.getInstance().getConnection();
            String sql = "update products set product_name = ?, price = ?, quantity = ? WHERE product_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, productName);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setInt(4, productId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
