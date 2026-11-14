# Laundry Final Project

A Java console application for a Design Patterns final project.

**Team:** Dauren and Ali

The program accepts laundry orders, adds extra services, calculates the price and tracks the order status.

## Run

Requires JDK 17 or newer. Open a terminal in this folder.

macOS / Linux:

```sh
sh run.sh
```

Windows:

```bat
run.bat
```

The menu appears in the terminal:

```text
1. Create order
2. List orders
3. View order
4. Next status
0. Exit
```

To run the prepared example: `sh run.sh --demo` (Windows: `run.bat --demo`).

You can also compile and run the code directly:

```sh
mkdir out
javac --release 17 -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Skip `mkdir out` if the folder already exists. In Windows PowerShell, use `./run.bat`; source files are in `src/`.

## Design patterns

| Pattern | Code | Purpose |
|---|---|---|
| Builder | `Order.Builder` | Build an order and check its required fields |
| Decorator | `Ironing`, `StainRemoval`, `Packing` | Add services to a basic laundry item |
| Strategy | `PricingStrategy` | Standard, loyalty and express prices |
| State | `OrderState` | Control the five processing stages |
| Observer | `OrderObserver`, `StatusLogger` | Record status changes in the order history |
| Facade | `LaundryManager` | Create, find, list and advance orders |

## Example

Two shirts with ironing and packaging cost `(1500 + 500 + 300) * 2 = 4600 KZT`.
One coat with stain removal costs `4500 + 800 = 5300 KZT`.
The subtotal is **9900 KZT**. Loyalty pricing gives a 10% discount, so the total is **8910 KZT**.

The order follows: **Accepted -> Washing -> Drying -> Ready for pickup -> Delivered**.

## Tests and files

Run `sh test.sh` or `test.bat`. The tests check prices, validation, the six patterns and the console menu.

- `src/` - Java source files; start reading with `Main.java`.
- `tests/` - plain Java tests, without external libraries.
- `report/Final_Report.pdf` - English project report.
- `docs/` - UML diagrams, defense notes and recorded console output.

Orders stay in memory and are removed when the program exits. The prices are examples. The history is stored inside the program; no external notifications are sent.

Checked on macOS with JDK 25.0.1, compiling for Java 17. There are 20 passing test scenarios.
