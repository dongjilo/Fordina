package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class KapeTable extends JTable {

    customTableModel model;

    public KapeTable(Vector<Vector<Object>> data, Vector<String> columnNames) {
        this.model = new customTableModel(data, columnNames);
        initTable();
    }

    public KapeTable(DefaultTableModel tableModel) {
        this.model = (customTableModel) tableModel;
        initTable();
    }

    // Set up table properties
    private void initTable() {
        this.getTableHeader().setReorderingAllowed(false);
        this.setModel(model);
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(25);
        setGridColor(Color.LIGHT_GRAY);
        setFillsViewportHeight(true);
    }

    @Override
    public customTableModel getModel() {
        return model;
    }
}


class customTableModel extends DefaultTableModel{

    public customTableModel(Vector<? extends Vector> data, Vector<?> columnNames){
        super(data, columnNames);
    }

    public customTableModel(){}

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}