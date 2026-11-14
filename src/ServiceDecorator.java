public abstract class ServiceDecorator implements LaundryService {
    protected final LaundryService service;

    public ServiceDecorator(LaundryService service) {
        if (service == null) throw new IllegalArgumentException("A base service is required.");
        this.service = service;
    }
}