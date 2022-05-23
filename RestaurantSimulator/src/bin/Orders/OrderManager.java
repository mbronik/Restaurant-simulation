package bin.Orders;

import bin.Logic.Statistics;

import java.util.PriorityQueue;


public class OrderManager {

    private static PriorityQueue<Order> queue = new PriorityQueue<>();

    public static void addOrder(Order order) {
        queue.add(order);
        Statistics.orderAccepted();
    }

    public static PriorityQueue<Order> getQueue() {
        PriorityQueue<Order> temp = new PriorityQueue<>(queue);
        return temp;
    }

    public static Order getNext() {   //with delete
        return queue.poll();
    }

    public static Order showNext() {   //without delete
        return queue.peek();
    }

    public static void showQueue() {
        PriorityQueue<Order> pq = getQueue();
        while (!pq.isEmpty()) {
            System.out.println(pq.poll().toString());
        }
    }

    public static boolean isEmpty() {
        return getQueue().isEmpty();
    }

}
