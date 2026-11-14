// Observer: subscribers react whenever an order publishes its status.
public interface OrderObserver {
    void update(Order order);
}
