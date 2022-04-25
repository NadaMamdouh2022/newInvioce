package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Invoice;
import model.Item;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.InvoiceItemsTable;
import model.InvoiceTable;
import view.InvoiceFrame;

public class InvoiceGenerator implements ActionListener, ListSelectionListener {

    public InvoiceFrame invoiceFrame;
    
    public InvoiceGenerator(InvoiceFrame invoiceFrame) {
        this.invoiceFrame = invoiceFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;
            case "Save New Invoice":
                saveNewInvoice();
                break;
            case "Cancel New Invoice":
                cancelNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Create New Item":
                createNewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "Save New Item":
                saveNewItem();
                break;
            case "Cancel New Item":
                cancelNewItem();
                break;
        }
    }

    public void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
         BufferedReader headerBr = null;
         BufferedReader lineBr = null;
        try {
            List<Invoice> invoiceList = new ArrayList<>();
            List<Item> itemsList = new ArrayList<>();
            String line = "";
            JOptionPane.showMessageDialog(invoiceFrame, "please load Invoice Header file", "load header file", JOptionPane.INFORMATION_MESSAGE);
            int invoiceReader = fileChooser.showOpenDialog(invoiceFrame);
            if (invoiceReader == JFileChooser.APPROVE_OPTION) {
               File headerFile = fileChooser.getSelectedFile();
                String fileName = headerFile.getName();
                if (!fileName.endsWith(".csv")) {
                    JOptionPane.showMessageDialog(invoiceFrame, "Wrong File Format, please select only csv files", "Error Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                headerBr = new BufferedReader(new FileReader(headerFile));
                while ((line = headerBr.readLine()) != null) {
                    String[] values = line.split(",");
                    Invoice invoice = new Invoice();
                    invoice.no = Integer.parseInt(values[0]);
                    //validate date format
                    Date date = new SimpleDateFormat("dd-MM-yyyy").parse(values[1]);
                    invoice.date = new SimpleDateFormat("dd-MM-yyyy").format(date);
                    invoice.customerName = values[2];
                    invoiceList.add(invoice);
                }
            }

            JOptionPane.showMessageDialog(invoiceFrame, "please load Invoice Line file", "load line file", JOptionPane.INFORMATION_MESSAGE);
            int itemReader = fileChooser.showOpenDialog(invoiceFrame);
            if (itemReader == JFileChooser.APPROVE_OPTION) {
               File lineFile = fileChooser.getSelectedFile();
                String fileName = lineFile.getName();
                if (!fileName.endsWith(".csv")) {
                    JOptionPane.showMessageDialog(invoiceFrame, "Wrong File Format, please select only csv files", "Error Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                lineBr = new BufferedReader(new FileReader(lineFile));
                while ((line = lineBr.readLine()) != null) {
                    String[] values = line.split(",");
                    Item item = new Item();
                    item.no = Integer.parseInt(values[0]);

                    item.name = values[1];
                    item.price = Double.parseDouble(values[2]);
                    item.count = Integer.parseInt(values[3]);
                    item.total = item.price * item.count;
                    itemsList.add(item);
                }
            }
            for (Item item : itemsList) {
                for (Invoice invoice : invoiceList) {
                    if (item.no == invoice.no) {
                        if (invoice.itemList == null) {
                            invoice.itemList = new ArrayList<>();
                        }
                        invoice.itemList.add(item);
                        invoice.total = invoice.total + item.total;
                        break;
                    }
                }
            }
            invoiceFrame.setInvoiceList(invoiceList);
            InvoiceTable invoiceTable = new InvoiceTable(invoiceList);
            invoiceFrame.setInvoiceTable(invoiceTable);
            invoiceFrame.getjTable1().setModel(invoiceTable);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(invoiceFrame, "Wrong Date format, format should be 'dd-MM-yyyy'", "Error Message", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (FileNotFoundException e2) {
            JOptionPane.showMessageDialog(invoiceFrame, "File not found", "Error Message", JOptionPane.ERROR_MESSAGE);
            e2.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(invoiceFrame, "something wrong happend", "Error Message", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (headerBr != null) {
                    headerBr.close();
                }
                if (lineBr != null) {
                    lineBr.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(InvoiceGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveFile() {
        FileWriter headerWriter = null;
        FileWriter lineWriter = null;
        try {
            JFileChooser fileChooser = new JFileChooser();
            File headerFile = null;
            File lineFile = null;
            JOptionPane.showMessageDialog(invoiceFrame, "please select Invoice Header file to save", "select header file", JOptionPane.INFORMATION_MESSAGE);
            int invoiceReader = fileChooser.showOpenDialog(invoiceFrame);
            if (invoiceReader == JFileChooser.APPROVE_OPTION) {
                headerFile = fileChooser.getSelectedFile();
                if (!headerFile.getName().endsWith(".csv")) {
                    JOptionPane.showMessageDialog(invoiceFrame, "Wrong File Format, please select only csv files", "Error Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(invoiceFrame, "please select Invoice Line file to save", "select invoice Line file", JOptionPane.INFORMATION_MESSAGE);
            int itemReader = fileChooser.showOpenDialog(invoiceFrame);
            if (itemReader == JFileChooser.APPROVE_OPTION) {
                lineFile = fileChooser.getSelectedFile();
                if (!lineFile.getName().endsWith(".csv")) {
                    JOptionPane.showMessageDialog(invoiceFrame, "Wrong File Format, please select only csv files", "Error Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            headerWriter = new FileWriter(headerFile);
            lineWriter = new FileWriter(lineFile);
            String lineData = "";
            String headerData = "";
            for (Invoice invoice : invoiceFrame.getInvoiceList()) {
                headerData = headerData + invoice.invoiceData() + "\n";

                if (invoice.itemList != null) {
                    for (Item item : invoice.itemList) {
                        lineData = lineData + item.itemData() + "\n";
                    }
                }
            }
            
            System.err.println("header data");
            System.err.println(headerData);
            System.err.println("line data");
            System.err.println(lineData);
            
            headerWriter.write(headerData);
            lineWriter.write(lineData);
            headerWriter.close();
            JOptionPane.showMessageDialog(invoiceFrame, "Header and Line Files saved successfully", "Success Message", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(invoiceFrame, e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
        }
        finally {
            try {
                if (headerWriter != null) {
                    headerWriter.close();
                }
                if (lineWriter != null) {
                    lineWriter.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(InvoiceGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedInvoiceIndex = invoiceFrame.getjTable1().getSelectedRow();
        if (selectedInvoiceIndex != -1) {
            Invoice selectedInvoice = invoiceFrame.getInvoiceList().get(selectedInvoiceIndex);
            List<Item> items = selectedInvoice.itemList;
            InvoiceItemsTable invoiceItemsTable = new InvoiceItemsTable(items);
            invoiceFrame.setItemList(items);
            invoiceFrame.getjTable2().setModel(invoiceItemsTable);
            invoiceFrame.getInvoiceNoValue().setText(String.valueOf(selectedInvoice.no));
            invoiceFrame.getInvoiceDateValue().setText(selectedInvoice.date);
            invoiceFrame.getInvoiceCustomerValue().setText(selectedInvoice.customerName);
            invoiceFrame.getInvoiceTotalValue().setText(String.valueOf(selectedInvoice.total));
        }
    }

    public void createNewInvoice() {
        invoiceFrame.getNewInvoiceDlg().setVisible(true);
    }

    public void saveNewInvoice() {
        try {
            invoiceFrame.getNewInvoiceDlg().setVisible(false);
            Invoice invoice = new Invoice();
            invoice.customerName = invoiceFrame.getNewInvoiceCustomerValue().getText();
            // check validation
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(invoiceFrame.getNewInvoiceDateValue().getText());
            invoice.date = new SimpleDateFormat("dd-MM-yyyy").format(date);

            int maxNo = 0;
            if (invoiceFrame.getInvoiceList() != null) {
                for (Invoice inv : invoiceFrame.getInvoiceList()) {
                    if (inv.no > maxNo) {
                        maxNo = inv.no;
                    }
                }
            }
            invoice.no = maxNo + 1;
            if (invoiceFrame.getInvoiceList() == null) {
                invoiceFrame.setInvoiceList(new ArrayList<Invoice>());
            }

            invoiceFrame.getInvoiceList().add(invoice);
            if (invoiceFrame.getInvoiceTable() == null) {
                invoiceFrame.setInvoiceTable(new InvoiceTable(invoiceFrame.getInvoiceList()));
            }

            invoiceFrame.getInvoiceTable().fireTableDataChanged();

            invoiceFrame.getNewInvoiceCustomerValue().setText(null);
            invoiceFrame.getNewInvoiceDateValue().setText(null);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(invoiceFrame, "Invalid date format, format should be 'dd-MM-yyyy'", "Invalid date Format", JOptionPane.ERROR_MESSAGE);
            invoiceFrame.getNewInvoiceDlg().setVisible(true);
            return;
        }
    }

    public void cancelNewInvoice() {
        invoiceFrame.getNewInvoiceDlg().setVisible(false);
        invoiceFrame.getNewInvoiceCustomerValue().setText(null);
        invoiceFrame.getNewInvoiceDateValue().setText(null);

    }

    public void deleteInvoice() {
        int selectedInvoice = invoiceFrame.getjTable1().getSelectedRow();
        if (selectedInvoice != -1) {
            invoiceFrame.getInvoiceList().remove(selectedInvoice);
            invoiceFrame.getInvoiceTable().fireTableDataChanged();

            invoiceFrame.getjTable2().setModel(new InvoiceItemsTable(null));
            invoiceFrame.setItemList(null);
            invoiceFrame.setItemsTable(null);
            invoiceFrame.getInvoiceNoValue().setText("");
            invoiceFrame.getInvoiceDateValue().setText("");
            invoiceFrame.getInvoiceCustomerValue().setText("");
            invoiceFrame.getInvoiceTotalValue().setText("");
        }
    }

    private void createNewItem() {
        invoiceFrame.getNewItemDlg1().setVisible(true);
    }

    private void saveNewItem() {
        try {
            invoiceFrame.getNewItemDlg1().setVisible(false);
            Item item = new Item();
            item.name = invoiceFrame.getNewItemNameValue().getText();
            item.price = Double.valueOf(invoiceFrame.getNewItemPriceValue().getText());
            item.count = Integer.parseInt(invoiceFrame.getNewItemCountValue().getText());
            if (item.price > 0 && item.count > 0) {
                item.total = item.price * item.count;
            }
            Invoice invoice = new Invoice();
            int selectedInvoice = invoiceFrame.getjTable1().getSelectedRow();
            if (selectedInvoice != -1) {
                invoice = invoiceFrame.getInvoiceList().get(selectedInvoice);
                invoice.total = invoice.total + item.total;
                if (invoice.itemList == null) {
                    invoice.itemList = new ArrayList<>();
                }
                invoice.itemList.add(item);
            }
            invoiceFrame.getInvoiceTotalValue().setText(String.valueOf(invoice.total));
            item.no = invoice.no;
            if (invoiceFrame.getItemList() == null) {
                invoiceFrame.setItemList(new ArrayList<Item>());
            }

            invoiceFrame.setItemList(invoice.itemList);
            if (invoiceFrame.getItemsTable() == null) {
                invoiceFrame.setItemsTable(new InvoiceItemsTable(invoiceFrame.getItemList()));
            }
            invoiceFrame.getjTable2().setModel(invoiceFrame.getItemsTable());
            
            invoiceFrame.getInvoiceTable().fireTableDataChanged();
            invoiceFrame.getItemsTable().fireTableDataChanged();
            invoiceFrame.getjTable1().setRowSelectionInterval(selectedInvoice, selectedInvoice);
            
            invoiceFrame.getNewItemDlg1().setVisible(false);
            invoiceFrame.getNewItemNameValue().setText(null);
            invoiceFrame.getNewItemPriceValue().setText(null);
            invoiceFrame.getNewItemCountValue().setText(null);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(invoiceFrame, "Invalid Number", "Invalid Number", JOptionPane.ERROR_MESSAGE);
            invoiceFrame.getNewItemDlg1().setVisible(true);

        }
    }

    private void cancelNewItem() {
        invoiceFrame.getNewItemDlg1().setVisible(false);
        invoiceFrame.getNewItemNameValue().setText(null);
        invoiceFrame.getNewItemPriceValue().setText(null);
        invoiceFrame.getNewItemCountValue().setText(null);
    }
    
      private void deleteItem() {
          System.err.println("delete item");
        int selectedInvoiceIndex = invoiceFrame.getjTable1().getSelectedRow();
        Invoice selectedInvoice = invoiceFrame.getInvoiceList().get(selectedInvoiceIndex);
        int selectedItemIndex = invoiceFrame.getjTable2().getSelectedRow();
        if (selectedItemIndex != -1) {
            Item seletedItem = invoiceFrame.getItemList().get(selectedItemIndex);
            invoiceFrame.getItemList().remove(selectedItemIndex);
            Double total = selectedInvoice.total - seletedItem.total;
            selectedInvoice.total = total;
            invoiceFrame.getInvoiceTotalValue().setText(String.valueOf(total));
            InvoiceItemsTable invoiceItemsTable = new InvoiceItemsTable(invoiceFrame.getItemList());
            invoiceFrame.setItemsTable(invoiceItemsTable);
            invoiceFrame.getInvoiceTable().fireTableDataChanged();
            invoiceFrame.getItemsTable().fireTableDataChanged();
            invoiceFrame.getjTable1().setRowSelectionInterval(selectedInvoiceIndex, selectedInvoiceIndex);
        }
    }

}
