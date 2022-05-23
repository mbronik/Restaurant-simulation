package bin.Orders;

public class OnlineOrder extends Order{
    private String street;
    private String number;

    public OnlineOrder(String street, String number){
        super();
        this.street=street;
        this.number=number;
    }

    public String getAddress(){
        return street+" "+number;
    }
}
