package components;

import components.Custom.Icons.Icons;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddProductDialog extends JDialog {
    private JTextField productIdField, productNameField, priceField, quantityField;

    /**
     *
     * @param parent
     */
    public AddProductDialog (JFrame parent) {
        super (parent, "Add Product", true);
        initComponents();
    }

    /**
     * Initialize the components necessary for the add product dialog.
     */
    private void initComponents() {

        this.setIconImage(Icons.LOGO);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        JLabel productIdLabel = new JLabel("Product ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(productIdLabel, gbc);

        productIdField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(productIdField, gbc);

        JLabel nameLabel = new JLabel("Product Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        productNameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(productNameField, gbc);

        JLabel priceLabel = new JLabel("Price:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(priceLabel, gbc);

        priceField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(priceField, gbc);

        JLabel quantityLabel = new JLabel("Quantity:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(quantityLabel, gbc);

        quantityField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(quantityField, gbc);

        JButton addButton = new JButton("Add");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(addButton, gbc);

        JButton cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(cancelButton, gbc);

        // Configure dialog
        this.setContentPane(panel);
        this.pack();
        this.setSize(400, 250);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        getRootPane().setDefaultButton(addButton);
        productIdField.requestFocusInWindow();

        // Add action listeners
        addButton.addActionListener(e -> addProduct());
        cancelButton.addActionListener(e -> dispose());
    }

    /**
     * Inserts the user's input to the database
     */
    private void addProduct() {
        // Retrieve the input values
        String productName = productNameField.getText(), priceText = priceField.getText(), quantityText = quantityField.getText();
        double price;
        int productId = Integer.parseInt(productIdField.getText()), quantity;

        // Validate the input values
        if (productName.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all the fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            price = Double.parseDouble(priceText);
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price and quantity", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert the input values to the database
        try {
            Connection con = DBConnector.getInstance().getConnection();
            String sql = "insert into products (product_id, product_name, price, quantity) values (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, String.valueOf(productId));
            ps.setString(2, productName);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        }catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
