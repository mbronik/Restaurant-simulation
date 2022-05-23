package bin.Worker;

import java.time.LocalDate;

public class Cook extends Worker{
    private int minutesWorked;
    private int preparedMeals;

    public Cook(String name, String surname, String phoneNumber){
        super(name, surname, phoneNumber);
        minutesWorked=0;
        preparedMeals=0;
    }
    public Cook(String name, String surname, String phoneNumber, LocalDate hired, int minutesWorked, int preparedMeals){
        super(name, surname, phoneNumber, hired);
        this.minutesWorked=minutesWorked;
        this.preparedMeals=preparedMeals;
    }

    public void addMinutesWorked(int minutes){
        minutesWorked+=minutes;
    }
    public void addPreparedMeals(int count){
        preparedMeals+=count;
    }

    public int getMinutesWorked() {
        return minutesWorked;
    }
    public int getPreparedMeals() {
        return preparedMeals;
    }
}
