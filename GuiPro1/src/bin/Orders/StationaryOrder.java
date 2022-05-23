package bin.Orders;

public class StationaryOrder extends Order{
    private int tableNumber;

    public StationaryOrder(int tableNumber){
        super();
        this.tableNumber=tableNumber;;
    }

    public int getTableNumber(){
        return tableNumber;
    }
}
