import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/** Small, dependency-free checks: run with test.sh or test.bat. */
public class LaundryTests {
    private static int passed;

    public static void main(String[] args) throws Exception {
        check("Decorator adds services", () -> {
            LaundryService service = new Packing(new StainRemoval(new Ironing(new BaseLaundry("Shirt", 1500))));
            equal(3100, service.getPrice());
            String description = service.getDescription().toLowerCase();
            require(description.contains("shirt") && description.contains("iron")
                    && description.contains("stain") && description.contains("pack"), "Missing service description");
        });
        check("Quantity multiplies the decorated item price", () ->
                equal(4600, exampleItems().get(0).getTotal()));
        check("Standard pricing: 9900", () -> equal(9900, order(new StandardPricing()).getTotal()));
        check("Loyalty pricing: 8910", () -> equal(8910, order(new LoyaltyPricing()).getTotal()));
        check("Express pricing: 14850", () -> equal(14850, order(new ExpressPricing()).getTotal()));
        check("Builder preserves customer and items", () -> {
            Order order = builtOrder();
            equal("Test customer", order.getCustomer());
            equal("+7 700 123 4567", order.getPhone());
            equal(1, order.getItems().size());
            equal(1500, order.getSubtotal());
        });
        check("Builder requires mandatory data", () ->
                rejects(IllegalArgumentException.class, () -> new Order.Builder().build()));
        check("Zero and negative quantities are rejected", () -> {
            rejects(IllegalArgumentException.class, () -> new OrderItem(new BaseLaundry("Shirt", 1500), 0));
            rejects(IllegalArgumentException.class, () -> new OrderItem(new BaseLaundry("Shirt", 1500), -2));
        });
        check("Empty order is rejected", () -> rejects(IllegalArgumentException.class,
                () -> new LaundryManager().createOrder("Test customer", "+7 700 123 4567", new ArrayList<>(), new StandardPricing())));
        check("Blank customer is rejected", () -> rejects(IllegalArgumentException.class,
                () -> new LaundryManager().createOrder("  ", "+7 700 123 4567", exampleItems(), new StandardPricing())));
        check("State follows all five stages", () -> {
            Order order = builtOrder();
            String[] expected = {"Accepted", "Washing", "Drying", "Ready for pickup", "Delivered"};
            for (int i = 0; i < expected.length; i++) {
                equal(expected[i], order.getStatus());
                if (i < expected.length - 1) order.advance();
            }
        });
        check("Delivered is a terminal state", () -> {
            Order order = builtOrder();
            for (int i = 0; i < 4; i++) order.advance();
            rejects(IllegalStateException.class, order::advance);
            equal("Delivered", order.getStatus());
        });
        check("Observer receives changes and can unsubscribe", () -> {
            Order order = builtOrder();
            ArrayList<String> events = new ArrayList<>();
            OrderObserver observer = changed -> events.add(changed.getStatus());
            order.addObserver(observer);
            order.notifyObservers();
            order.advance();
            equal(List.of("Accepted", "Washing"), events);
            order.removeObserver(observer);
            order.advance();
            equal(2, events.size());
        });
        check("Facade records initial status and every change", () -> {
            LaundryManager manager = new LaundryManager();
            Order order = manager.createOrder("Test customer", "+7 700 123 4567", exampleItems(), new LoyaltyPricing());
            for (int i = 0; i < 4; i++) manager.advanceOrder(order.getId());
            equal(5, order.getHistory().size());
            require(order.getHistory().get(0).contains("Accepted"), "Initial event missing");
            require(order.getHistory().get(4).contains("Delivered"), "Final event missing");
        });
        check("Multiple orders remain independent", () -> {
            LaundryManager manager = new LaundryManager();
            Order first = manager.createOrder("First customer", "+7 700 123 4567", exampleItems(), new StandardPricing());
            Order second = manager.createOrder("Second customer", "+7 700 123 4568", exampleItems(), new ExpressPricing());
            require(first.getId() != second.getId(), "Order IDs must differ");
            equal(2, manager.getOrders().size());
            equal(first, manager.getOrder(first.getId()));
            manager.advanceOrder(first.getId());
            equal("Accepted", second.getStatus());
            equal(14850, second.getTotal());
        });
        check("Unknown order ID is rejected", () -> {
            LaundryManager manager = new LaundryManager();
            rejects(IllegalArgumentException.class, () -> manager.getOrder(99999));
            rejects(IllegalArgumentException.class, () -> manager.advanceOrder(99999));
        });
        check("Console demo runs successfully", () -> {
            String output = runConsole("", "--demo");
            require(output.contains("9900") && output.contains("8910"), "Demo must show the example calculation");
            require(output.contains("Delivered"), "Demo must reach the final status");
        });
        check("Console exits when input is empty", () ->
                require(runConsole("").contains("Goodbye"), "Empty input must end the application"));
        check("Console exits when input ends during order creation", () ->
                require(runConsole("1\n").contains("Goodbye"), "Interrupted entry must end the application"));
        check("Console create, list, view, advance and invalid ID", () -> {
            String input = String.join("\n", "wrong", "1", "Menu customer", "+7 700 123 4567",
                    "1", "2", "1", "0", "1", "1", "2", "1", "0", "1", "0", "0", "2",
                    "2", "3", "1001", "4", "1001", "4", "1001", "4", "1001", "4", "1001",
                    "3", "1001", "3", "9999", "0", "");
            String output = runConsole(input);
            require(output.contains("Enter a whole number"), "Invalid menu input must be handled");
            require(output.contains("Order created.") && output.contains("ORDER #1001"), "Order was not created or shown");
            require(output.contains("#1001 | Menu customer"), "Order list must include the new order");
            require(output.contains("Subtotal: 9900 KZT") && output.contains("Total: 8910 KZT"), "Incorrect console price");
            for (String status : List.of("Washing", "Drying", "Ready for pickup", "Delivered"))
                require(output.contains("New status: " + status), "Missing console transition: " + status);
            require(output.contains("Order not found: 9999") && output.contains("Goodbye"), "Invalid ID or exit not handled");
        });
        System.out.println("PASS: " + passed + " scenarios");
    }

    private static String runConsole(String input, String... args) {
        try {
            ArrayList<String> command = new ArrayList<>(List.of(System.getProperty("java.home") + "/bin/java",
                    "-cp", System.getProperty("java.class.path"), "Main"));
            command.addAll(List.of(args));
            Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
            process.getOutputStream().write(input.getBytes(StandardCharsets.UTF_8));
            process.getOutputStream().close();
            if (!process.waitFor(5, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new AssertionError("Console did not finish within 5 seconds");
            }
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            equal(0, process.exitValue());
            return output;
        } catch (Exception exception) { throw new AssertionError("Could not run console", exception); }
    }

    private static ArrayList<OrderItem> exampleItems() {
        ArrayList<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Packing(new Ironing(new BaseLaundry("Shirt", 1500))), 2));
        items.add(new OrderItem(new StainRemoval(new BaseLaundry("Coat", 4500)), 1));
        return items;
    }

    private static Order order(PricingStrategy pricing) {
        return new LaundryManager().createOrder("Test customer", "+7 700 123 4567", exampleItems(), pricing);
    }

    private static Order builtOrder() {
        return new Order.Builder().id(1).customer("Test customer", "+7 700 123 4567")
                .addItem(new OrderItem(new BaseLaundry("Shirt", 1500), 1)).pricing(new StandardPricing()).build();
    }

    private static void check(String name, Runnable test) {
        test.run();
        passed++;
        System.out.println("PASS " + passed + ": " + name);
    }

    private static void equal(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) throw new AssertionError("Expected " + expected + ", got " + actual);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void rejects(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (type.isInstance(error)) return;
            throw new AssertionError("Expected " + type.getSimpleName() + ", got " + error, error);
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
}
