package bin.Logic;


import bin.Kitchen;
import bin.MainMenu.MenuSystem;
import bin.Orders.Order;
import bin.Orders.OrderManager;
import bin.Restaurant;
import bin.Worker.WorkerManager;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class TimeKeeper{
    private static Thread cookWorkingTimer = new Thread(()->{
        while (Kitchen.isKitchenOpen()) {
            try {
                Thread.sleep(60_000);
            } catch (InterruptedException e) { }
            if(MenuSystem.getSystemOn()) {
                WorkerManager.addMinutesWorkedToAllCooks(1);
            }
        }
    });
    public static void cookWorkingTimerStart(){
        cookWorkingTimer.start();
    }
    public static void cookWorkingTimerStop(){
        cookWorkingTimer.stop();
    }
    public static boolean isCookWorkingTimerWorking(){
        return cookWorkingTimer.isAlive();
    }


    private static Thread tooLongWaitingOrdersGuardian = new Thread(()->{
        while (Restaurant.getOpen()){
            PriorityQueue<Order> orders = OrderManager.getQueue();
            while (!orders.isEmpty()){
                Order order = orders.poll();
                if(!order.isHighPriority()) {
                    LocalDateTime time = order.getTime();
                    LocalDateTime now = LocalDateTime.now();
                    if (time.getHour() == now.getHour()) {
                        if(now.getMinute()-time.getMinute()>15)
                            clientDecision(order);
                    } else {
                        if(60-time.getMinute() + now.getMinute()>15)
                            clientDecision(order);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    public static void clientDecision(Order order){
        if ((int) (Math.random() * 100 + 1) > 50) {
            order.setHighPriority();
            double discount = (int)(order.getTotal()*0.2*100)/100;
            Statistics.subtractFromRevenue(discount);
            Logger.enterLog("Order no." + order.getNumber() + " expired and client gets a discount " + discount + " " + Menu.currency);
        }else{
            Logger.enterLog("Order no." + order.getNumber() + " expired and client decide to cancel order");
            Statistics.subtractFromRevenue(order.getTotal());
            order = null;
        }
    }
    public static void tooLongWaitingOrdersGuardianStart(){
        tooLongWaitingOrdersGuardian.start();
    }
    public static void tooLongWaitingOrdersGuardianStop(){
        tooLongWaitingOrdersGuardian.stop();
    }
    public static boolean isTooLongWaitingOrdersGuardianWorking(){
        return tooLongWaitingOrdersGuardian.isAlive();
    }
}
