/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DELL
 */
public class InvoiceItemsTable extends AbstractTableModel {

    private List<Item> itemList;
    private String[] columns = {"Item Name", "Item Price", "Count", "Item Total"};

    public InvoiceItemsTable(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getRowCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (itemList == null) {
            return "";
        } else {
            Item item = itemList.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return item.name;
                case 1:
                    return item.price;
                case 2:
                    return item.count;
                case 3:
                    return item.total;
                default:
                    return "";
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
}
