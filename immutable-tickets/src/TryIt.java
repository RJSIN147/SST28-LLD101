import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;

import java.util.List;

/**
 * Starter demo that shows why mutability is risky.
 *
 * After refactor:
 * - direct mutation should not compile (no setters)
 * - external modifications to tags should not affect the ticket
 * - service "updates" should return a NEW ticket instance
 */
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        IncidentTicket original = service.createTicket(
                "TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created : " + original);

        // Demonstrate post-creation mutation through service
        IncidentTicket assigned = service.assign(original, "agent@example.com");
        System.out.println("\nAssigned: " + assigned);
        System.out.println("Original unchanged: " + original);

        IncidentTicket escalated = service.escalateToCritical(assigned);
        System.out.println("\nEscalated: " + escalated);
        System.out.println("Assigned unchanged: " + assigned);

        // Demonstrate external mutation via leaked list reference
        try {
            original.getTags().add("HACKED_FROM_OUTSIDE");
            System.out.println("\n[FAIL] Tags were mutated externally!");
        } catch (UnsupportedOperationException e) {
            System.out.println("\n[OK] External tag mutation blocked: " + e.getClass().getSimpleName());
        }
    }
}
