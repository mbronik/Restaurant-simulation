package bin.Worker;

import java.time.LocalDate;

public class Deliverer extends Worker{
    private int deliveredOrders;
    private double tip;
    private boolean busy;

    public Deliverer(String name, String surname, String phoneNumber){
        super(name, surname, phoneNumber);
        deliveredOrders=0;
        tip=0;
        busy=false;
    }
    public Deliverer(String name, String surname, String phoneNumber, LocalDate hired, int deliveredOrders, double tip){
        super(name, surname, phoneNumber, hired);
        this.deliveredOrders=deliveredOrders;
        this.tip=tip;
        busy=false;
    }

    public void addDelivered(){
        deliveredOrders++;
    }
    public void addTip(double tip){
        this.tip+=tip;
    }

    public int getDeliveredOrders() {
        return deliveredOrders;
    }
    public double getTip() {
        return tip;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean isBusy() {
        return busy;
    }
}
