import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final TaxCalculator taxCalculator;
    private final DiscountCalculator discountCalculator;
    private final InvoiceFormatter formatter;
    private final InvoiceStore store;
    private int invoiceSeq = 1000;

    public CafeteriaSystem(TaxCalculator taxCalculator,
            DiscountCalculator discountCalculator,
            InvoiceFormatter formatter,
            InvoiceStore store) {
        this.taxCalculator = taxCalculator;
        this.discountCalculator = discountCalculator;
        this.formatter = formatter;
        this.store = store;
    }

    public void addToMenu(MenuItem i) {
        menu.put(i.id, i);
    }

    public void checkout(List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);

        // 1. Compute subtotal
        double subtotal = 0.0;
        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            subtotal += item.price * l.qty;
        }

        // 2. Delegate tax
        double taxPct = taxCalculator.taxPercent();
        double tax = subtotal * (taxPct / 100.0);

        // 3. Delegate discount
        double discount = discountCalculator.discountAmount(subtotal, lines.size());

        // 4. Compute total
        double total = subtotal + tax - discount;

        // 5. Delegate formatting
        String printable = formatter.format(invId, lines, menu,
                subtotal, taxPct, tax, discount, total);
        System.out.print(printable);

        // 6. Delegate persistence
        store.save(invId, printable);
        System.out.println("Saved invoice: " + invId + " (lines=" + store.countLines(invId) + ")");
    }
}
