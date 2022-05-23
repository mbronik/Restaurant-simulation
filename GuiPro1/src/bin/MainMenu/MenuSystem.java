package bin.MainMenu;

import bin.Kitchen;
import bin.Logic.*;
import bin.Orders.*;
import bin.Restaurant;
import bin.Worker.Cook;
import bin.Worker.Deliverer;
import bin.Worker.Waiter;
import bin.Worker.WorkerManager;

import java.lang.Thread;
import java.security.spec.ECField;

public class MenuSystem extends Thread {

    static final String password = "admin"; //Access to the admin panel

    private static final String[] adminMainMenuPosition = {"Manage employees", "Control restaurant", "Edit the menu", "Order data", "Logout", "Close applications"};
    private static final String[] empManagePosition = {"Hire an employee", "Dismiss an employee", "Information about an employee", "List of employees", "Go back"};
    private static final String[] resControlPosition = {"Change status", "Go back"};
    private static final String[] menuEditPosition = {"Show menu", "Add an item", "Remove an item", "Set an item availability", "Go back"};
    private static final String[] ordDataPosition = {"Order queue", "Order history", "Restaurant statistics", "Go back"};

    private static boolean systemOn;
    private boolean adminLoggedIn;
    private int lastMainMenuPosition;

    private SystemIn in;
    private OrderMaker orderMaker;

    public MenuSystem() {
        super();
        Logger.init();
        Menu.init();
        in = new SystemIn();
        orderMaker = new OrderMaker();
        try {
            WorkerManager.loadEmp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        systemOn = true;
        adminLoggedIn = false;
    }

    @Override
    public void run() {
        while (systemOn) {
            Separators.clearConsole();

            System.out.println("Write something to open menu!");
            Separators.separate();
            if (password.equals(in.aString())) {
                Logger.enterLog("Logged in as admin");
                adminLoggedIn = true;
                lastMainMenuPosition = 0;
                try {
                    adminControlPanel();
                } catch (Exception a) {
                    System.out.println(a.getMessage());
                }
            } else {
                try {
                    orderMaker.makeOrder();
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        }
    }

    private void adminControlPanel() throws Exception {
        while (adminLoggedIn) {
            if (lastMainMenuPosition == 0) {
                System.out.println();
                for (int i = 0; i < adminMainMenuPosition.length; i++)
                    System.out.println(i + 1 + ". " + adminMainMenuPosition[i]);
                try {
                    lastMainMenuPosition = in.anIntPositive(adminMainMenuPosition.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            switch (lastMainMenuPosition) {
                case 1 -> empManage();
                case 2 -> restaurantControl();
                case 3 -> menuEdit();
                case 4 -> orderDate();
                case 5 -> {
                    adminLoggedIn = false;
                    Logger.enterLog("Admin logged out");
                }
                case 6 -> {
                    adminLoggedIn = false;
                    systemOn = false;
                    if (Restaurant.openStatus() == true){
                        Restaurant.openOrClose();
                    }
                    if (Kitchen.isBusy()||Delivery.activeCount()>0) {
                        System.out.println("The kitchen or deliverers are still working!" +
                                "\nWhat you want to do?" +
                                "\n1. Close anyway WARNING(Orders in progress will be destroyed, can cause errors!)" +
                                "\nAnother. Wait until the job is finished");
                        switch (in.anIntPositive()) {
                            case 1 -> {
                                Logger.enterLog("Admin forced the termination of kitchens and suppliers");
                                Restaurant.forceClose();
                            }
                        }
                    }
                    System.out.println();
                    Statistics.saveClose();
                    Logger.enterLog("Closing the application");
                    WorkerManager.close();
                    Menu.close();
                    Logger.close();
                }
                default -> throw new Exception("Serious out of range problem!");
            }
        }
    }

    private void empManage() throws Exception {
        System.out.println();
        for (int i = 0; i < empManagePosition.length; i++)
            System.out.println(i + 1 + ". " + empManagePosition[i]);

        switch (in.anIntPositive(empManagePosition.length)) {
            case 1 -> hireEmp();
            case 2 -> dismissEmp();
            case 3 -> empInfoPanel();
            case 4 -> WorkerManager.showEmployees();
            case 5 -> lastMainMenuPosition = 0;
            default -> throw new Exception("Serious out of range problem!");
        }
    }

    private void hireEmp() {
        System.out.println("Enter a name");
        String name = in.aString();
        System.out.println("Enter a surname");
        String surname = in.aString();
        System.out.println("Enter a phone number");
        String phoneNumber = in.aString();
        switch (choosePos()) {
            case 1 -> {
                WorkerManager.hireCook(new Cook(name, surname, phoneNumber));
            }
            case 2 -> {
                WorkerManager.hireWaiter(new Waiter(name, surname, phoneNumber));
            }
            case 3 -> {
                WorkerManager.hireDeliverer(new Deliverer(name, surname, phoneNumber));
            }
        }
    }

    private void dismissEmp() {
        switch (choosePos()) {
            case 1 -> {
                WorkerManager.showCook();
                System.out.println("Another. Cancel" +
                        "\nEnter the employee index");
                int index = in.anIntPositiveWithZero(WorkerManager.getCountOfCooks()-1);
                if (confirmRemoval()) WorkerManager.dismissCook(index);
            }
            case 2 -> {
                WorkerManager.showWaiter();
                System.out.println("Another. Cancel" +
                        "\nEnter the employee index");
                int index = in.anIntPositiveWithZero(WorkerManager.getCountOfWaiters()-1);
                if (confirmRemoval()) WorkerManager.dismissWaiter(index);
            }
            case 3 -> {
                WorkerManager.showDeliverer();
                System.out.println("Another. Cancel" +
                        "\nEnter the employee index");
                int index = in.anIntPositiveWithZero(WorkerManager.getCountOfDeliverers()-1);
                if (confirmRemoval()) WorkerManager.dismissDeliverer(index);
            }
        }
    }

    private boolean confirmRemoval() {
        System.out.println("Are you sure to do this\n" +
                "1. Yes\n" +
                "Another. No"
        );
        if (in.anIntPositive() == 1) {
            return true;
        } else {
            return false;
        }
    }

    private void empInfoPanel() {
        switch (choosePos()) {
            case 1 -> {
                WorkerManager.showCook();
                System.out.println("Choose cook");
                int index = in.anIntPositiveWithZero(WorkerManager.getCountOfCooks()-1);
                WorkerManager.showCookStat(index);
            }
            case 2 -> {
                WorkerManager.showWaiter();
                System.out.println("Choose waiter");
                int index = in.anIntPositiveWithZero(WorkerManager.getCountOfWaiters()-1);
                WorkerManager.showWaiterStat(index);
            }
            case 3 -> {
                WorkerManager.showDeliverer();
                System.out.println("Choose deliverer");
                int index = in.anIntPositiveWithZero(WorkerManager.getCountOfDeliverers()-1);
                WorkerManager.showDelivererStat(index);
            }
        }
    }

    private int choosePos() {
        System.out.println("Choose an employee position\n" +
                "1. Cook\n" +
                "2. Waiter\n" +
                "3. Deliverer\n" +
                "Another. Cancel");
        return in.anIntPositive();
    }

    private void restaurantControl() throws Exception {
        System.out.println("\nRestaurant status: " + (Restaurant.getOpen() ? "Open" : "Close"));
        for (int i = 0; i < resControlPosition.length; i++)
            System.out.println(i + 1 + ". " + resControlPosition[i]);

        switch (in.anIntPositive(2)) {
            case 1 -> {
                try {
                    Restaurant.openOrClose();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            case 2 -> lastMainMenuPosition = 0;
            default -> throw new Exception("Serious out of range problem!");
        }
    }

    private void menuEdit() throws Exception {
        System.out.println();
        for (int i = 0; i < menuEditPosition.length; i++)
            System.out.println(i + 1 + ". " + menuEditPosition[i]);

        switch (in.anIntPositive(menuEditPosition.length)) {
            case 1 -> Menu.printAll();
            case 2 -> {
                System.out.println("Enter a name:");
                String name = in.aString();
                System.out.println("Enter a price:");
                Double price = in.aDoublePositive();
                System.out.println("Enter a short description");
                String description = in.aString();
                Menu.addItem(name, price, description);
            }
            case 3 -> {
                Menu.printAll();
                System.out.println("Enter the ID of the item to be deleted (<0 to cancel):");
                boolean available;
                do {
                    int i = in.anInt();
                    if(i>0) available = Menu.removeItem(i);
                    else available = true;
                    if(!available) System.out.println("Out of range");
                }while(!available);
            }
            case 4 -> {
                Menu.printAll();
                System.out.println("Enter the ID of the item in which to change the availability (<0 to cancel):");
                boolean available;
                do {
                    int i = in.anInt();
                    if (i > 0) available = Menu.changeAvailability(i);
                    else available = true;
                    if(!available) System.out.println("Out of range");
                }while(!available);
            }
            case 5 -> lastMainMenuPosition = 0;
            default -> throw new Exception("Serious out of range problem!");
        }
    }

    private void orderDate() throws Exception {
        System.out.println();
        for (int i = 0; i < ordDataPosition.length; i++)
            System.out.println(i + 1 + ". " + ordDataPosition[i]);

        switch (in.anIntPositive(ordDataPosition.length)) {
            case 1 -> OrderManager.showQueue();
            case 2 -> FinalizedOrders.showHistory();
            case 3 -> Statistics.show();
            case 4 -> lastMainMenuPosition = 0;
            default -> throw new Exception("Serious out of range problem!");
        }
    }

    public static boolean getSystemOn() {
        return systemOn;
    }


}
