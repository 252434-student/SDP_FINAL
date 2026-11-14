public class StatusLogger implements OrderObserver {
    public void update(Order order) {
        order.addHistory("Status: " + order.getStatus());
    }
}
