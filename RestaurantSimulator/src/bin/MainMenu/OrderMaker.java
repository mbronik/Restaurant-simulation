package bin.MainMenu;

import bin.Logic.Menu;
import bin.Orders.OnlineOrder;
import bin.Orders.Order;
import bin.Orders.OrderManager;
import bin.Orders.StationaryOrder;
import bin.Restaurant;

public class OrderMaker{
    SystemIn in;

    public OrderMaker(){
        in = new SystemIn();
    }

    public void makeOrder() throws Exception {
        if (Restaurant.getOpen()) {
            System.out.println("Select the type of order\n" +
                    "1. At location\n" +
                    "2. Online\n" +
                    "Another. Cancel"
            );
            switch (in.anIntPositive(2)) {
                case 1 -> {
                    System.out.println("Select a table number:");
                    StationaryOrder so = new StationaryOrder(in.anIntPositive());
                    composeOrder(so);
                }
                case 2 -> {
                    System.out.print("Street: ");
                    String st = in.aString();
                    System.out.print("Number: ");
                    String nu = in.aString();
                    OnlineOrder oo = new OnlineOrder(st, nu);
                    composeOrder(oo);
                }
            }
        } else {
            System.out.println("Restaurant close!\nCome back later :)");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void composeOrder(Order order) throws Exception {
        Menu.printAvailable();
        int action;
        do {
            int id;
            boolean available;
            do {
                System.out.println("Enter the product id");
                id = in.anIntPositive();
                available = Menu.isAvailable(id);
                if(!available) System.out.println("There is no such thing");
            } while (!available);

            System.out.println("Quantity");
            int q = in.anIntPositive();
            order.add(Menu.get(id), q);

            printShoppingCart(order);
            System.out.println("What you want to do?\n" +
                    "1. Add next item\n" +
                    "2. Complete the order\n" +
                    "Another. Cancel order");
            action = in.anIntPositive();
        } while (action == 1);
        if (action == 2) {
            OrderManager.addOrder(order);
        }
    }

    private void printShoppingCart(Order order) {
        Separators.separate();
        System.out.println("Your shopping cart");
        Separators.separate();
        for (int i = 0; i < order.length(); i++) {
            System.out.println(order.toStringAt(i));
        }
        Separators.separate();
        System.out.println("Total amount: " + order.getTotal() + " " + Menu.currency);
        Separators.separate();
    }
}
