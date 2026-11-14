public class StainRemoval extends ServiceDecorator {
    public StainRemoval(LaundryService service) {
        super(service);
    }
    public String getDescription() {
        return service.getDescription() + " + stain removal";
    }
    public int getPrice() {
        return service.getPrice() + 800;
    }
}