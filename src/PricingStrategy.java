public interface PricingStrategy {
    int calculate(int subtotal);
    String getName();
}
class StandardPricing implements PricingStrategy {
    public int calculate(int subtotal) {
        return subtotal;
    }
    public String getName() {
        return "Standard";
    }
}
class LoyaltyPricing implements PricingStrategy {
    public int calculate(int subtotal) {
        return (int) ((subtotal * 90L + 50) / 100);
    }
    public String getName() {
        return "Loyalty (-10%)";
    }
}
class ExpressPricing implements PricingStrategy {
    public int calculate(int subtotal) {
        return (int) ((subtotal * 150L + 50) / 100);
    }
    public String getName() {
        return "Express (+50%)";
    }
}