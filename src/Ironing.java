public class Ironing extends ServiceDecorator {
    public Ironing(LaundryService service) { super(service); }
    public String getDescription() { return service.getDescription() + " + ironing"; }
    public int getPrice() { return service.getPrice() + 500; }
}
