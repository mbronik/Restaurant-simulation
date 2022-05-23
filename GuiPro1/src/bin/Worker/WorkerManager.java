package bin.Worker;

import bin.Logic.Stream;

import java.time.LocalDate;
import java.util.ArrayList;

public class WorkerManager{
    private static final Stream stream = new Stream("src\\data\\Employees.txt");

    private static ArrayList<Cook> cooks = new ArrayList<>();
    private static ArrayList<Waiter> waiters = new ArrayList<>();
    private static ArrayList<Deliverer> deliverers = new ArrayList<>();

    public static ArrayList<Cook> getCooks(){
        return cooks;
    }
    public static ArrayList<Waiter> getWaiters(){
        return waiters;
    }
    public static ArrayList<Deliverer> getDeliverers(){
        return deliverers;
    }

    public static void hireCook(Cook cook){
        cooks.add(cook);
        addToFile(cook);
    }
    public static void hireWaiter(Waiter waiter){
        waiters.add(waiter);
        addToFile(waiter);
    }
    public static void hireDeliverer(Deliverer deliverer){
        deliverers.add(deliverer);
        addToFile(deliverer);
    }

    private static void addToFile(Cook cook){
        stream.writeToFile("cook|"+cook.getName()+"|"+cook.getSurname()+"|"+cook.getPhoneNumber()+"|"+cook.getHired()+"|"+cook.getMinutesWorked()+"|"+cook.getPreparedMeals()+"\n");
        stream.save();
    }
    public static void addToFile(Waiter waiter){
        stream.writeToFile("waiter|"+waiter.getName()+"|"+ waiter.getSurname()+"|"+ waiter.getPhoneNumber()+"|"+ waiter.getHired()+"|"+ waiter.getTip()+"|"+ waiter.getHandledOrder()+"\n");
        stream.save();
    }
    public static void addToFile(Deliverer deliverer){
        stream.writeToFile("deliverer|"+deliverer.getName()+"|"+ deliverer.getSurname()+"|"+ deliverer.getPhoneNumber()+"|"+ deliverer.getHired()+"|"+ deliverer.getTip()+"|"+ deliverer.getDeliveredOrders()+"\n");
        stream.save();
    }

    public static void loadEmp() throws Exception{
        String name, surname, phoneNumber, stat1;
        LocalDate hired;
        int stat2;

        String temp = stream.readSegment();
        while(!temp.equals("-1")){
            name = stream.readSegment();
            surname = stream.readSegment();
            phoneNumber = stream.readSegment();
            hired = LocalDate.parse(stream.readSegment());
            stat1 = stream.readSegment();
            stat2 = Integer.parseInt(stream.readSegment());

            if(temp.equals("cook")){
                cooks.add(new Cook(name, surname, phoneNumber, hired, stat2, Integer.parseInt(stat1)));
            }else if(temp.equals("waiter")){
                waiters.add(new Waiter(name, surname, phoneNumber, hired, stat2, Double.parseDouble(stat1)));
            }else if(temp.equals("deliverer")){
                deliverers.add(new Deliverer(name, surname, phoneNumber, hired, stat2, Double.parseDouble(stat1)));
            }else throw new Exception("Error loading employees. Check the correctness of the data in the file \"Employees.txt\"");
            temp = stream.readSegment();
        }
    }
    public static void updateEmpFile(){
        stream.clear();
        for(int i=0; i<cooks.size(); i++){
            addToFile(cooks.get(i));
        }
        for(int i=0; i<waiters.size(); i++){
            addToFile(waiters.get(i));
        }
        for(int i=0; i<deliverers.size(); i++){
            addToFile(deliverers.get(i));
        }
    }

    public static void dismissCook(int index){
        cooks.remove(index);
        updateEmpFile();
    }
    public static void dismissWaiter(int index){
        waiters.remove(index);
        updateEmpFile();
    }
    public static void dismissDeliverer(int index){
        deliverers.remove(index);
        updateEmpFile();
    }

    public static void showCook(){
        for(int i=0; i<cooks.size(); i++){
            showCook(i);
        }
    }
    public static void showCook(int index){
        System.out.println(index+". "+cooks.get(index).toString());
    }

    public static void showWaiter(){
        for(int i=0; i<waiters.size(); i++){
            showWaiter(i);
        }
    }
    public static void showWaiter(int index){
        System.out.println(index+". "+waiters.get(index).toString());
    }

    public static void showDeliverer(){
        for(int i=0; i<deliverers.size(); i++){
            showDeliverer(i);
        }
    }
    public static void showDeliverer(int index){
        System.out.println(index+". "+deliverers.get(index).toString());
    }

    public static void showEmployees(){
        System.out.println("Cooks:");
        showCook();
        System.out.println("Waiters:");
        showWaiter();
        System.out.println("Deliverers:");
        showDeliverer();
    }
    public static void showCookStat(int index){
        showCook(index);
        Cook temp = cooks.get(index);
        System.out.println("Prepared meals: " + temp.getPreparedMeals() +
                "\nWorked hours: " + (int)(temp.getMinutesWorked()/60) + " minutes: " + temp.getMinutesWorked()%60 +
                "\nHired: " + temp.getHired().toString()
        );
    }
    public static void showWaiterStat(int index){
        showWaiter(index);
        Waiter temp = waiters.get(index);
        System.out.println("Accepted order: " + temp.getHandledOrder() +
                "\nTip: " +  temp.getTip()+
                "\nHired: " + temp.getHired().toString()
        );
    }
    public static void showDelivererStat(int index){
        showDeliverer(index);
        Deliverer temp = deliverers.get(index);
        System.out.println("Delivery: " + temp.getDeliveredOrders() +
                "\nTip: " +  temp.getTip()+
                "\nHired: " + temp.getHired().toString()
        );
    }

    public static void addMinutesWorkedToAllCooks(int minutes){
        for(int i=0; i<cooks.size(); i++){
            cooks.get(i).addMinutesWorked(minutes);
        }
        updateEmpFile();
    }
    public static void addPreparedMealsToAllCooks(int countOfMeals){
        for(int i=0; i<cooks.size(); i++){
            cooks.get(i).addPreparedMeals(countOfMeals);
        }
        updateEmpFile();
    }

    public static int getCountOfCooks(){
        return cooks.size();
    }
    public static int getCountOfWaiters(){
        return waiters.size();
    }
    public static int getCountOfDeliverers(){
        return deliverers.size();
    }

    public static void close(){
        updateEmpFile();
        stream.closeFile();
    }
}
