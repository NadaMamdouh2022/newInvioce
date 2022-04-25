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
public class InvoiceTable extends AbstractTableModel {

    private List<Invoice> invoiceList;
    private String[] columns = {"No.", "Date", "Customer" , "Total"};
    
    public InvoiceTable(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @Override
    public int getRowCount() {
        return invoiceList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Invoice invoice = invoiceList.get(rowIndex);
        switch (columnIndex) {
            case 0: return invoice.no;
            case 1: return invoice.date;
            case 2: return invoice.customerName;
            case 3: return invoice.total;
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
}
