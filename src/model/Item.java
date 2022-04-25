package model;

public class Item {

    public int no;

    public String name;

    public double price;

    public int count;

    public double total;

    @Override
    public String toString() {
        return "Item{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", total=" + total +
                '}';
    }

    public String itemData() {
        return no + "," +
                name + "," +
                price + "," +
                count + "," +
                total;
    }
}
