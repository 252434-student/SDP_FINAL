import java.util.ArrayList;

public class Order {
    private final int id;
    private final String customer;
    private final String phone;
    private final ArrayList<OrderItem> items;
    private final PricingStrategy pricing;
    private OrderState state = new ReceivedState();
    private final ArrayList<OrderObserver> observers = new ArrayList<>();
    private final ArrayList<String> history = new ArrayList<>();
    private Order(Builder builder) {
        if (builder.id < 1) throw new IllegalArgumentException("Invalid order number.");
        if (builder.customer == null || builder.customer.trim().isEmpty())
            throw new IllegalArgumentException("Customer name is required.");
        if (builder.phone == null || !builder.phone.matches("[+0-9 ()-]+")
                || builder.phone.replaceAll("[^0-9]", "").length() < 7)
            throw new IllegalArgumentException("Phone must contain at least 7 digits.");
        if (builder.items.isEmpty() || builder.items.size() > 20)
            throw new IllegalArgumentException("An order needs 1 to 20 item lines.");
        if (builder.pricing == null) throw new IllegalArgumentException("Choose a pricing plan.");
        id = builder.id;
        customer = builder.customer.trim();
        phone = builder.phone.trim();
        items = new ArrayList<>(builder.items);
        pricing = builder.pricing;
    }
    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public String getPhone() { return phone; }
    public ArrayList<OrderItem> getItems() { return new ArrayList<>(items); }
    public String getPlan() { return pricing.getName(); }
    public String getStatus() { return state.getName(); }
    public int getSubtotal() {
        int subtotal = 0;
        for (OrderItem item : items) subtotal += item.getTotal();
        return subtotal;
    }
    public int getTotal() { return pricing.calculate(getSubtotal()); }
    public ArrayList<String> getHistory() { return new ArrayList<>(history); }
    public void addHistory(String message) { history.add(message); }
    public void addObserver(OrderObserver observer) {
        if (observer == null) throw new IllegalArgumentException("Observer is required.");
        observers.add(observer);
    }
    public void removeObserver(OrderObserver observer) { observers.remove(observer); }
    public void notifyObservers() {
        for (OrderObserver observer : new ArrayList<>(observers)) observer.update(this);
    }
    public void advance() {
        state = state.next();
        notifyObservers();
    }
    public static class Builder {
        private int id;
        private String customer;
        private String phone;
        private final ArrayList<OrderItem> items = new ArrayList<>();
        private PricingStrategy pricing = new StandardPricing();

        public Builder id(int value) { id = value; return this; }
        public Builder customer(String name, String number) { customer = name; phone = number; return this; }
        public Builder addItem(OrderItem item) {
            if (item == null) throw new IllegalArgumentException("Item cannot be empty.");
            items.add(item);
            return this;
        }
        public Builder pricing(PricingStrategy value) { pricing = value; return this; }
        public Order build() { return new Order(this); }
    }
}
