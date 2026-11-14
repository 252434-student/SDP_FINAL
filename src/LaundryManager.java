import java.util.ArrayList;

public class LaundryManager {
    private final ArrayList<Order> orders = new ArrayList<>();
    private int nextId = 1001;

    public Order createOrder(String customer, String phone, ArrayList<OrderItem> items, PricingStrategy pricing) {
        if (items == null){
            throw new IllegalArgumentException("Items are required.");
        }
        Order.Builder builder = new Order.Builder().id(nextId).customer(customer, phone).pricing(pricing);
        for (OrderItem item : items){
            builder.addItem(item);
        }
        Order order = builder.build();
        order.addObserver(new StatusLogger());
        order.notifyObservers();
        orders.add(order);
        nextId++;
        return order;
    }

    public ArrayList<Order> getOrders() { return new ArrayList<>(orders); }

    public Order getOrder(int id) {
        for (Order order : orders) {
            if (order.getId() == id) return order;
        }
        throw new IllegalArgumentException("Order not found: " + id);
    }
    public Order advanceOrder(int id) {
        Order order = getOrder(id);
        order.advance();
        return order;
    }
}
