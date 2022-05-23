package bin;

import bin.Logic.*;
import bin.Orders.*;
import bin.Worker.Waiter;
import bin.Worker.WorkerManager;

import java.time.LocalDateTime;

public class Kitchen extends Thread{
    private static final int millisPerItem = 10_000;

    private static boolean kitchenOpen;
    private int cooksCount;
    private static boolean busy;

    @Override
    public void run(){
        try {
            FinalizedOrders.kitchenStart();
            cooksCount = WorkerManager.getCountOfCooks();
            if(cooksCount<0) {
                Logger.enterLog("The kitchen isn't working, no chefs");
                Restaurant.openOrClose();
                throw new Exception("No cooks available");
            }
            kitchenOpen = true;
            Order orderToDo;
            TimeKeeper.cookWorkingTimerStart();
            while (kitchenOpen) {
                if (!OrderManager.isEmpty()) {
                    busy = true;
                    orderToDo = OrderManager.getNext();
                    if(orderToDo != null) doOrder(orderToDo);
                    //orderToDo=null;
                } else {
                    busy = false;
                    sleep(1000);
                    if(!Restaurant.openStatus()){
                        kitchenOpen=false;
                    }
                }

            }
            Logger.enterLog("The kitchen stop working");
            TimeKeeper.cookWorkingTimerStop();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doOrder(Order order) throws InterruptedException{
        int positionCount=0;
        for(int i=0; order.getProducts().size()>i; i++){
            positionCount += order.getProducts().get(i).getQuantity();
        }
        int cookCount=WorkerManager.getCountOfCooks();
        sleep((int)(Math.pow(0.8,cookCount)* millisPerItem *positionCount));
        Statistics.orderDone(order.getTotal());
        FinalizedOrders.add(order);
        WorkerManager.addPreparedMealsToAllCooks(positionCount);
        Logger.enterLog("The kitchen has completed the order no." + order.getNumber());
        if(order instanceof OnlineOrder){
            Delivery.make((OnlineOrder)order);
        }else{
            try {
                detailsToWaiter((StationaryOrder) order);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static void detailsToWaiter(StationaryOrder order) throws Exception{
        Waiter waiter = WorkerManager.getWaiters().get( (int)(WorkerManager.getWaiters().size()*Math.random()) );
        if(WorkerManager.getCountOfWaiters()<1) throw new Exception("No waiters, order cancel");

        int orderTime = 15;
        if(LocalDateTime.now().getHour() == order.getTime().getHour()){
            orderTime = LocalDateTime.now().getMinute() - order.getTime().getMinute();
        } else if(LocalDateTime.now().getHour()+1 == order.getTime().getHour()){
            orderTime = 60 - LocalDateTime.now().getMinute() + order.getTime().getMinute();
        }
        double tip = 0;
        if(orderTime<15){
            tip = (int)(order.getTotal()*0.1/15*(15-orderTime)*100)/100;
            waiter.addTip(tip);
        }
        waiter.addHandled();
        Logger.enterLog("The waiter " + waiter.getName() + " " + waiter.getSurname() + " took the order to the table no." + order.getTableNumber() + "\n\tand got a tip " + tip + " " + Menu.currency);
    }

    public static void setOpen(boolean isOpen){
        kitchenOpen=isOpen;
        Logger.enterLog("The kitchen changes status to " + (isOpen?"open":"close"));
    };

    public static boolean isKitchenOpen() {
        return kitchenOpen;
    }

    public static boolean isBusy() {
        return busy;
    }
}
