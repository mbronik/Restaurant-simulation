package bin.Logic;

import bin.MainMenu.SystemIn;

import java.util.HashMap;

public class Menu {
    public static final String currency = "PLN";
    private static final String fileName = "src\\data\\Menu.txt";

    private static HashMap items = new HashMap<Integer,Item>();

    private static Stream stream;

    public static void init(){
        stream = new Stream(fileName);
        loadItem();
    }
    private static int checkSimilars(String name){
        for(int i=1;i<items.size()+1;i++){
            Item temp = (Item)items.get(i);
            if(temp!=null) {
                if (name.toUpperCase().equals(temp.getName().toUpperCase())) {
                    return i;
                }
            }
        }
        return 0;
    }
    private static void loadItem(){
        int id;
        String name;
        double price;
        boolean availability;
        String description;

        String temp = stream.readSegment();
        while(!temp.equals("-1")){
            id = Integer.parseUnsignedInt(temp);
            name = stream.readSegment();
            price = Double.parseDouble(stream.readSegment());
            availability = Boolean.parseBoolean(stream.readSegment());
            description = stream.readSegment();
            items.put(id, new Item(id,name,price,availability,description));
            id++;
            temp = stream.readSegment();
        }
        Logger.enterLog("Items loaded from memory");
    }

    public static void addItem (String name, Double price, String description){
        int similar=checkSimilars(name);
        if(similar==0) {
            int id=0;
            Item temp;
            do{
                id++;
                temp = (Item)items.putIfAbsent(id, new Item(id, name, price, description));
            }while(temp!=null);
            stream.writeToFile(id + "|" + name + "|" + price + "|" + true + "|" + description + "\n");
            stream.save();
            Logger.enterLog("A new item ("+ name +") has been added to the menu");
        }else{
            Logger.enterLog("An attempt was made to add an item but found a similar item ("+ name +")");
            System.out.println("A similar thing was found!\n -> "+items.get(similar).toString()+"\n");
            System.out.println(
                    "\nWhat you want to do?" +
                    "\n1. Add anyway" +
                    "\n2. Replace"+
                    "\nAnother. Cancel"
            );
            SystemIn in = new SystemIn();
            switch (in.anIntPositive()){
                case 1 -> {
                    int id=0;
                    Item temp;
                    do{
                        id++;
                        temp = (Item)items.putIfAbsent(id, new Item(id, name, price, description));
                    }while(temp!=null);
                    stream.writeToFile(name + "|" + price + "|" + description + "\n");
                }
                case 2 -> {
                    items.put(similar, new Item(similar, name, price, description));
                    updateMenu();
                }
            }
        }
    }

    public static boolean removeItem(int id){
        if(items.get(id) == null) return false;
        String temp = ((Item)items.get(id)).getName();
        items.remove(id);
        updateMenu();
        Logger.enterLog("Item removed at ID="+id+" ("+temp+")");
        return true;
    }

    private static void updateMenu(){
        stream.clear();
        for(int i=1,j=1; j<items.size()+1; i++) {
            Item temp = (Item) items.get(i);
            if(temp!=null){
                stream.writeToFile(temp.getId() + "|" + temp.getName() + "|" + temp.getPrice() + "|" + temp.getAvailability() + "|" + temp.getDescription() + "\n");
                stream.save();
                j++;
            }
        }
        Logger.enterLog("Menu file updated");
    }
    public static boolean changeAvailability(int id){
        if(items.get(id) == null) return false;
        boolean temp = ((Item)items.get(id)).changeAvailability();
        updateMenu();
        Logger.enterLog("Item availability changed with id="+id+" to "+temp);
        return true;
    }
    public static Item get(int id){
        return (Item)items.get(id);
    }

    public static Item getRandom(){
        int id = (int)(Math.random()*(items.size()-1))+1;
        for(int i=1,j=1; j<=id; i++) {
            Item temp = (Item)items.get(i);
            if(temp!=null){
                if(j==id) return (Item)items.get(i);
                j++;
            }
        }
        return null;
    }

    public static void printAll(){
        for(int i=1,j=1; j<items.size()+1; i++) {
            Item temp = (Item)items.get(i);
            if(temp!=null){
                System.out.println(temp.toString(true));
                j++;
            }
        }
    }
    public static void printAvailable(){
        for(int i=1,j=1; j<items.size()+1; i++) {
            Item temp = (Item)items.get(i);
            if(temp!=null){
                if(temp.getAvailability()) {
                    System.out.println(temp.toString(false));
                }
                j++;
            }
        }
    }
    public static boolean isAvailable(int id) throws Exception{
        Item temp = (Item)items.get(id);
        if(temp==null){
            throw new Exception("Out of range!");
        }
        else return temp.getAvailability();
    }
    public static void close(){
        stream.closeFile();
    }
}
