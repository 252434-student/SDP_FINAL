public class Packing extends ServiceDecorator {
    public Packing(LaundryService service) {
        super(service);
    }
    public String getDescription() {
        return service.getDescription() + " + packaging";
    }
    public int getPrice() {
        return service.getPrice() + 300;
    }
}