public class BaseLaundry implements LaundryService {
    private final String description;
    private final int price;

    public BaseLaundry(String description, int price) {
        if (description == null || description.trim().isEmpty() || price < 0 || price > 100000)
            throw new IllegalArgumentException("Invalid basic service.");
        this.description = description;
        this.price = price;
    }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
}