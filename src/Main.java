import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);
    private static final LaundryManager manager = new LaundryManager();

    public static void main(String[] args) {
        System.out.println("LAUNDRY MANAGEMENT: \n Final Project by Dauren and Ali \nGroups SE-2524 and 37");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Create order\n2. List orders\n3. View order\n4. Next status\n0. Exit");
            try {
                int choice = readNumber("Choose: ", 0, 4);
                if (choice == 1){
                    createOrder();
                }
                else if (choice == 2){
                    listOrders();
                }
                else if (choice == 3){
                    printOrder(manager.getOrder(readNumber("Order number: ", 1, 999999)));
                }
                else if (choice == 4) {
                    Order order = manager.advanceOrder(readNumber("Order number: ", 1, 999999));
                    System.out.println("New status: " + order.getStatus());
                } else running = false;
            } catch (NoSuchElementException error) {
                running = false;
            } catch (IllegalArgumentException | IllegalStateException error) {
                System.out.println("Error: " + error.getMessage());
            }
        }
        System.out.println("Goodbye.");
    }
    private static void createOrder() {
        System.out.print("Customer name: ");
        String name = input.nextLine();

        System.out.print("Phone: ");
        String phone = input.nextLine();

        ArrayList<OrderItem> items = new ArrayList<>();
        int more = 1;
        while (more == 1 && items.size() < 20) {
            System.out.println("Items: 1. Shirt (1500 KZT)  2. Coat (4500 KZT)  3. Bedding set (2500 KZT)");
            int type = readNumber("Item: ", 1, 3);

            LaundryService service;

            if (type == 1){
                service = new BaseLaundry("Shirt", 1500);
            }
            else if (type == 2){
                service = new BaseLaundry("Coat", 4500);
            }
            else{
                service = new BaseLaundry("Bedding set", 2500);
            }

            int quantity = readNumber("Quantity: ", 1, 100);

            if (readNumber("Ironing +500KZT per item? 1=yes, 0=no: ", 0, 1) == 1){
                service = new Ironing(service);
            }
            if (readNumber("Stain removal +800KZT per item? 1=yes, 0=no: ", 0, 1) == 1){
                service = new StainRemoval(service);
            }
            if (readNumber("Packaging +300KZT per item? 1=yes, 0=no: ", 0, 1) == 1){
                service = new Packing(service);
            }
            items.add(new OrderItem(service, quantity));

            if (items.size() < 20){
                more = readNumber("Add another item line? 1=yes, 0=no: ", 0, 1);
            }
        }
        int plan = readNumber("Plan: 1=Standard, 2=Loyalty (-10%), 3=Express (+50%): ", 1, 3);
        PricingStrategy pricing;
        if (plan == 2){
            pricing = new LoyaltyPricing();
        }
        else if (plan == 3){
            pricing = new ExpressPricing();
        }
        else{
            pricing = new StandardPricing();
        }
        Order order = manager.createOrder(name, phone, items, pricing);
        System.out.println("Order created.");
        printOrder(order);
    }
    private static void listOrders() {
        if (manager.getOrders().isEmpty()){
            System.out.println("No orders yet.");
        }
        for (Order order : manager.getOrders()) {
            System.out.println("#" + order.getId() + " | " + order.getCustomer() + " | "
                    + order.getStatus() + " | " + order.getTotal() + " KZT");
        }
    }
    public static void printOrder(Order order) {
        System.out.println("\nORDER #" + order.getId());
        System.out.println("Customer: " + order.getCustomer() + " | Phone: " + order.getPhone());
        for (OrderItem item : order.getItems()) {
            System.out.println(item.getQuantity() + " x " + item.getService().getDescription()
                    + " = " + item.getTotal() + " KZT");
        }
        System.out.println("Subtotal: " + order.getSubtotal() + " KZT");
        System.out.println("Plan: " + order.getPlan());
        System.out.println("Adjustment: " + (order.getTotal() - order.getSubtotal()) + " KZT");
        System.out.println("Total: " + order.getTotal() + " KZT");
        System.out.println("Current status: " + order.getStatus());
        System.out.println("History:");
        for (String entry : order.getHistory()) System.out.println("  " + entry);
    }
    private static int readNumber(String prompt, int minimum, int maximum) {
        while (true) {
            System.out.print(prompt);
            if (!input.hasNextLine()) throw new NoSuchElementException("Input ended.");
            String text = input.nextLine();
            try {
                int value = Integer.parseInt(text.trim());
                if (value >= minimum && value <= maximum) return value;
            } catch (NumberFormatException error) {
                throw new NumberFormatException("Invalid input. Please try again.");
            }
            System.out.println("Enter a whole number from " + minimum + " to " + maximum + ".");
        }
    }
}
