package bin.Worker;

import java.time.LocalDate;

public class Worker extends Person{
    private LocalDate hired;

    public Worker(String name, String surname, String phoneNumber){
        super(name, surname, phoneNumber);
        hired = LocalDate.now();
    }
    public Worker(String name, String surname, String phoneNumber, LocalDate hired){
        super(name, surname, phoneNumber);
        this.hired = hired;
    }

    public LocalDate getHired() {
        return hired;
    }
    public String toString(){
        return (super.toString())+", Hired "+hired.toString();
    }
}
