# Laundry project: defense notes

Team: Dauren and Ali. Group: __________________.

## A short presentation

**1. Explain the task (about 40 seconds).**

The program is a console application for a laundry. It accepts customer details and items, adds services, calculates the total and tracks the order through five stages. It is written in Java and uses six design patterns.

**2. Show the price calculation (about 90 seconds).**

Use two shirts with ironing and packaging, plus one coat with stain removal. The shirts cost `(1500 + 500 + 300) * 2 = 4600 KZT`. The coat costs `4500 + 800 = 5300 KZT`. The subtotal is `9900 KZT`. Standard pricing keeps this amount; loyalty pricing gives `8910 KZT`; express pricing gives `14850 KZT`.

Decorator adds each extra to the basic laundry service. Strategy chooses one of the three pricing rules. Builder collects the customer and items before creating the order.

**3. Show order processing (about 90 seconds).**

Open the order and advance it through Accepted, Washing, Drying, Ready for pickup and Delivered. State decides the next allowed stage. An order cannot advance after Delivered. StatusLogger is an observer: it adds a history entry when the status changes.

**4. Show the classes (about 60 seconds).**

LaundryManager is the Facade. Main calls it to create, list, find and advance orders. The manager uses the builder and subscribes the status logger. Point to these calls in the code and on the UML diagrams.

**5. Run the checks and finish (about 60 seconds).**

Run the test script and show its actual result. Explain that orders are stored in memory and disappear when the application closes. The next improvement would be saving orders to a file. Also useful: cancellation and printing a receipt.

## Suggested preparation

| Person | Patterns to prepare | Demonstration |
| --- | --- | --- |
| Dauren | Builder, Decorator, Strategy | Create an order and explain the price |
| Ali | State, Observer, Facade | Advance an order and explain the history |

This is a proposed division for preparation, not a record of individual authorship. The assignment requires each student to implement at least two patterns individually. Each person needs to understand and explain the assigned code.

## Questions to practise

**Why use Builder?** The order has several values. Named methods such as `customer()` and `addItem()` are easier to read than a long constructor. `build()` creates the order after checking the data.

**Why use Decorator?** Extras can be combined without a separate class for every combination. Each decorator stores another LaundryService and adds its own price.

**Why use Strategy?** The items do not change when the customer chooses another pricing plan. Each strategy implements the same calculation method.

**Why use State?** Each state knows its next stage. DeliveredState rejects another transition, so the order cannot move beyond delivery.

**What does Observer do?** Order notifies its observers when its status changes. StatusLogger adds the new status to history. Another observer could be added later without rewriting the transition code.

**Why use Facade?** Main needs simple operations for the menu. LaundryManager handles the steps behind those operations and keeps that code out of the menu.

**Are messages sent to the customer?** No. This version records the status in the console order history.

**Where are orders stored?** In an ArrayList while the application is running. They are not saved after exit.

**How is the price checked?** The same reference order is tested under all three strategies. The expected totals are 9900, 8910 and 14850 KZT.

**What is still missing?** Saving orders between runs, cancellation and a printable receipt are possible next steps.
