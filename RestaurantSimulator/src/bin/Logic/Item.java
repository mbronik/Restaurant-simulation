package bin.Logic;

public class Item{
    private int id;
    private String name;
    private double price;
    private String description;
    private boolean availability;

    public Item (int id, String name, double price, String description){
        this.id=id;
        this.name=name;
        this.price=price;
        this.description=description;
        availability=true;
    }
    public Item (int id, String name, double price, boolean availability, String description){
        this.id=id;
        this.name=name;
        this.price=price;
        this.description=description;
        this.availability=availability;
    }

    public boolean changeAvailability (){
        availability=!availability;
        return availability;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public double getPrice(){
        return  price;
    }
    public boolean getAvailability(){
        return availability;
    }
    public String getDescription(){
        return description;
    }

    public String toString(){
        return toString(true);
    }

    public String toString(boolean availability){
        if(availability) {
            return id + ". " + name + " | " + price + " " + Menu.currency + " | " + this.availability + "\n\t" + description;
        }else{
            return id + ". " + name + " | " + price + " " + Menu.currency + "\n\t" + description;
        }
    }
}
