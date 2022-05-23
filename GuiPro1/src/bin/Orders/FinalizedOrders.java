package bin.Orders;

import bin.Logic.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FinalizedOrders {
    private static ArrayList<Order> list = new ArrayList<>();
    private static Logger finishOrder = new Logger("src\\data\\FinishedOrders.txt");;

    public static void kitchenStart(){
        finishOrder.send(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss")) + " - Kitchen start working");
    }

    public static void add(Order order){
        finishOrder.enter((order instanceof OnlineOrder?"Online":"Stationary") + " order no. " + order.getNumber() + " prepared");
        ArrayList<MoreItem> items = order.getProducts();
        for(int i=0; i<items.size(); i++){
            finishOrder.send("\t"+items.get(i).toString());
        }
        list.add(order);
    }
    public static Order[] getArray(){
        return (Order[]) list.toArray();
    }
    public static void showHistory(){
        for(int i=0; i< list.size(); i++){
            System.out.println(list.get(i).toStringAll());
        }
    }
}
