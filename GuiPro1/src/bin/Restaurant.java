package bin;

import bin.Logic.*;
import bin.MainMenu.MenuSystem;
import bin.Orders.*;

import static java.lang.Thread.sleep;

public class Restaurant{
    private static final String[] street = {"Bartycka", "Biala", "Kacza", "Drewniana", "Wojciecha Gorskiego", "Graniczna", "Kubusia Puchatka"};
    private static final  int breakBeforeAutoOrderingAfterOpen = 10_000;
    private static final  int maxTimeBetweenOrdering = 60_000;


    private static boolean open = false;

    private static Kitchen kitchen;

    private static Thread thread = new Thread(
            ()->{
                try {
                    sleep(breakBeforeAutoOrderingAfterOpen);
                    while(MenuSystem.getSystemOn()){
                        if(open){
                            makeRandomOrder();
                            sleep((int)(maxTimeBetweenOrdering*Math.random()));
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    );

    public static void init(){
        Statistics.init();
        kitchen = new Kitchen();
        if(open){
            thread.start();
            Kitchen.setOpen(true);
            kitchen.start();
            TimeKeeper.tooLongWaitingOrdersGuardianStart();
        }
    }

    public static boolean getOpen(){
        return open;
    }
    public static void openOrClose() throws Exception{
        open=!open;
        Logger.enterLog("The restaurant changes status to " + (open?"open":"close"));
        if(open && (!Kitchen.isBusy() || Delivery.countOfDeliveryInProgress()==0)){
            thread.start();
            Kitchen.setOpen(true);
            kitchen.start();
            TimeKeeper.tooLongWaitingOrdersGuardianStart();
        }else if(open && (Kitchen.isBusy() || Delivery.countOfDeliveryInProgress()>=1)){
            open = false;
            throw new Exception("Kitchen cannot open, cooks or deliverers haven't finished previous shift");
        }
    }
    public static boolean openStatus(){
        return open;
    }

    public static void makeRandomOrder(){
        Order order;
        if((int)(Math.random()*2) == 1) {
            order = new OnlineOrder(street[(int) (Math.random() * street.length)], String.valueOf((int)(Math.random() * 81)));
            for (int i = 0; i<(int)(Math.random()*4+1); i++){
                order.add(Menu.getRandom(), (int)(Math.random()*4+1));
            }
            OrderManager.addOrder(order);

        }else {
            order = new StationaryOrder((int) (Math.random() * 101));
            for (int i = 0; i<(int)(Math.random()*4+1); i++){
                order.add(Menu.getRandom(), (int)(Math.random()*4+1));
            }
            OrderManager.addOrder(order);

        }
        Logger.enterLog("The machine placed a random order no."+order.getNumber());
    }

    public static void forceClose(){
        if(kitchen.isAlive()) kitchen.stop();
        if(TimeKeeper.isCookWorkingTimerWorking()) TimeKeeper.cookWorkingTimerStop();
        if(TimeKeeper.isTooLongWaitingOrdersGuardianWorking()) TimeKeeper.tooLongWaitingOrdersGuardianStop();
        Delivery.forceClose();
        if(thread.isAlive()) thread.stop();
    }
}
