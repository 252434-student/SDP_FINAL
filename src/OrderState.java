// State: the current state decides which state may follow it.
public interface OrderState {
    String getName();
    OrderState next();
}
class ReceivedState implements OrderState {
    public String getName() { return "Accepted"; }
    public OrderState next() { return new WashingState(); }
}
class WashingState implements OrderState {
    public String getName() { return "Washing"; }
    public OrderState next() { return new DryingState(); }
}
class DryingState implements OrderState {
    public String getName() { return "Drying"; }
    public OrderState next() { return new ReadyState(); }
}
class ReadyState implements OrderState {
    public String getName() { return "Ready for pickup"; }
    public OrderState next() { return new DeliveredState(); }
}
class DeliveredState implements OrderState {
    public String getName() { return "Delivered"; }
    public OrderState next() { throw new IllegalStateException("This order has already been delivered."); }
}
