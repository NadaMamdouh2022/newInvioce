package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Invoice {

    public int no;

    public String date;

    public String customerName;

    public double total;

    public List<Item> itemList;

    @Override
    public String toString() {
        return "Invoice{" +
                "no=" + no +
                ", date=" + date +
                ", customerName='" + customerName + '\'' +
                ", total=" + total +
                ", itemList=" + itemList +
                '}';
    }

    public String invoiceData(){
//        if(this.date != null && this.date.length() > 0) {
//            try {
//               Date date = new SimpleDateFormat("dd-MM-yyyy").parse(this.date);
//                this.date = new SimpleDateFormat("dd-MM-yyyy").format(date);
//                System.err.println(date);
//                System.err.println(this.date);
//            } catch (ParseException ex) {
//                Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        return  this.no +","+
                this.date +","+
                this.customerName +","+
                this.total;
    }
}
