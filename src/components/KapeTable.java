package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class KapeTable extends JTable {
    public KapeTable(Vector<Vector<Object>> data, Vector<String> columnNames) {
        super(new customTableModel(data, columnNames));
        initTable();
    }

    public KapeTable(DefaultTableModel tableModel) {
        super(tableModel);
        initTable();
    }

    private void initTable() {
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(25);
        setGridColor(Color.LIGHT_GRAY);
        setFillsViewportHeight(true);
    }
}

class customTableModel extends DefaultTableModel{

    public customTableModel(Vector<? extends Vector> data, Vector<?> columnNames){
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}