public class OrderItem {
    private final LaundryService service;
    private final int quantity;

    public OrderItem(LaundryService service, int quantity) {
        if (service == null) throw new IllegalArgumentException("Choose a service.");
        if (quantity < 1 || quantity > 100)
            throw new IllegalArgumentException("Quantity must be between 1 and 100.");
        this.service = service;
        this.quantity = quantity;
    }
    public LaundryService getService() { return service; }
    public int getQuantity() { return quantity; }
    public int getTotal() { return service.getPrice() * quantity; }
}
