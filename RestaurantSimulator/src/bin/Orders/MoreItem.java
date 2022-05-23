package bin.Orders;

import bin.Logic.Item;

public class MoreItem{
    private Item item;
    private int quantity;

    public MoreItem(Item item, int amount){
        this.item = item;
        this.quantity = amount;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void removeOneAmount(){
        quantity--;
    }
    public void addOneAmount(){
        quantity++;
    }

    public Item getItem() {
        return item;
    }
    public int getQuantity() {
        return quantity;
    }
    public String toString(){
        return quantity +"x "+item.getName()+" "+item.getPrice()+" | "+item.getPrice()* quantity;
    }
}
