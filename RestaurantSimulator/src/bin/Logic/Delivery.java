package bin.Logic;

import bin.Orders.OnlineOrder;
import bin.Worker.Deliverer;
import bin.Worker.WorkerManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Delivery extends Thread {
    private Deliverer deliverer;
    private OnlineOrder order;
    private int index;
    private static ArrayList<Delivery> deliveryList = new ArrayList<>();

    private Delivery(OnlineOrder order){
        super();
        this.order = order;
        index = deliveryList.size()-1;
    }

    @Override
    public void run() {
        try {
            deliverer = chooseDeliverer();
            int time = 10_000 + (int)(Math.random()*50_001);
            deliverer.setBusy(true);
            Logger.enterLog("Deliverer "+deliverer.getName()+" "+deliverer.getSurname()+" (tel. "+deliverer.getPhoneNumber()+")" +
                    "\n\t\tstarting delivery of the order no."+order.getNumber()+" from the hour "+order.getTime().format(DateTimeFormatter.ofPattern("hh:mm:ss"))
            );
            sleep(time);

            deliverer.addDelivered();
            int orderTime = 15;
            if(LocalDateTime.now().getHour() == order.getTime().getHour()){
                orderTime = LocalDateTime.now().getMinute() - order.getTime().getMinute();
            } else if(LocalDateTime.now().getHour()+1 == order.getTime().getHour()){
                orderTime = 60 - LocalDateTime.now().getMinute() + order.getTime().getMinute();
            }
            double tip = 0;
            if(orderTime<15){
                tip = (int)(order.getTotal()*0.1/15*(15-orderTime)*100)/100;
                deliverer.addTip(tip);
            }
            Logger.enterLog("The deliverer " + deliverer.getName() + " " + deliverer.getSurname() + "delivered order no." + order.getNumber()+
                    "to "+order.getAddress() + "\n\tand got a tip " + tip + Menu.currency);
            sleep(time);
            Logger.enterLog("The deliverer " + deliverer.getName() + " " + deliverer.getSurname() + "come back after delivery and he's ready for the next");
            deliverer.setBusy(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Deliverer chooseDeliverer() throws Exception{
        ArrayList<Deliverer> deliverers = WorkerManager.getDeliverers();
        if(deliverers.size()<1) throw new Exception("No deliverer, order canceled no." + order.getNumber());

        int index = (int)(Math.random() * deliverers.size());

        for (int i=0; i<deliverers.size(); i++){
            if(!deliverers.get(index).isBusy()){
                return deliverers.get(index);
            }else{
                if(++index >= deliverers.size()){
                    index=0;
                }
            }
        }

        (new Exception("All deliverer are busy")).printStackTrace();
        Logger.enterLog("All deliverer are busy (order no." + order.getNumber() + " waiting)");

        while (true){
            for (int i=0; i<deliverers.size(); i++){
                if(deliverers.get(i).isBusy()){
                    return chooseDeliverer();
                }
            }
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void make(OnlineOrder order){
        deliveryList.add( new Delivery(order) );
        deliveryList.get(deliveryList.size()-1).start();
    }

    public static void forceClose(){
        for(int i=0; i<deliveryList.size(); i++){
            if(deliveryList.get(i).isAlive()) deliveryList.get(i).stop();
        }
    }

    public static int countOfDeliveryInProgress(){
        int temp=0;
        for (int i=0; i<deliveryList.size(); i++){
            if(deliveryList.get(i).isAlive()) temp++;
        }
        return temp;
    }
}
