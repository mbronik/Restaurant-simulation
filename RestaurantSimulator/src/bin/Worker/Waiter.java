package bin.Worker;

import java.time.LocalDate;

public class Waiter extends Worker{
    private int handledOrder;
    private double tip;

    public Waiter(String name, String sourname, String phoneNumber){
        super(name, sourname, phoneNumber);
        handledOrder =0;
        tip=0;
    }
    public Waiter(String name, String sourname, String phoneNumber, LocalDate hired, int acceptedOrder, double tip){
        super(name, sourname, phoneNumber, hired);
        this.handledOrder =acceptedOrder;
        this.tip=tip;
    }

    public void addHandled(){
        handledOrder++;
    }
    public void addTip(double tip){
        this.tip+=tip;
    }

    public int getHandledOrder() {
        return handledOrder;
    }
    public double getTip() {
        return tip;
    }
}
