package bin.Orders;

import bin.Logic.Item;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Order implements Comparable<Order>{
    private static int lastNumber=0;
    private int number;
    private LocalDateTime time;
    private ArrayList<MoreItem> products;

    private boolean highPriority;

    public Order (){
        number = ++lastNumber;
        time = LocalDateTime.now();
        products = new ArrayList<>();
    }
    public void add(Item item, int quantity){
        products.add(new MoreItem(item, quantity));
    }
    public double getTotal(){
        double total=0;
        for(int i=0; i<length(); i++) {
            total+=products.get(i).getItem().getPrice()*products.get(i).getQuantity();
        }
        return total;
    }

    public String toString(){
        return number+" "+(this instanceof OnlineOrder?"online ":"stationary ") +
                time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
    };
    public String toStringAt(int index){
        return products.get(index).toString();
    }
    public String toStringAll(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; products.size()>i; i++) {
            sb.append("\n\t"+products.get(i).toString());
        }
        sb.append("\n");
        return number+" "+(this instanceof OnlineOrder?"online ":"stationary ") +
                time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) +
                sb.toString();
    }

    public int length(){
        return products.size();
    }

    public ArrayList<MoreItem> getProducts(){
        return products;
    }

    public LocalDateTime getTime(){
        return time;
    }

    public static void restartNumbering(){
        lastNumber=0;
    }

    public int getNumber(){
        return number;
    }

    public void setHighPriority(){
        highPriority = true;
    }

    public boolean isHighPriority() {
        return highPriority;
    }

    @Override
    public int compareTo(Order o) {
        if (this.highPriority && !o.highPriority) {
            return -1;
        }else if(!this.highPriority && o.highPriority){
            return 1;
        }else if(this instanceof StationaryOrder && o instanceof OnlineOrder) {
            return -1;
        }else if(this instanceof OnlineOrder && o instanceof StationaryOrder){
            return 1;
        }else if(this.number<o.number){
            return -1;
        }else if(this.number>o.number){
            return 1;
        }else{
            return 0;
        }
    }
}
