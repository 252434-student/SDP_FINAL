# Laundry Final Project

A Java console application for a Design Patterns final project.

**Team:** Dauren and Ali

The program accepts laundry orders, adds extra services, calculates the price and tracks the order status.


The menu appears in the terminal:

```text
1. Create order
2. List orders
3. View order
4. Next status
0. Exit
```

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
