import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Placement Eligibility ===");

        // Configurable thresholds
        RuleInput config = new RuleInput();

        // Rules in evaluation order (short-circuits on first failure)
        List<EligibilityRule> rules = List.of(
                new DisciplinaryFlagRule(),
                new CgrRule(config),
                new AttendanceRule(config),
                new CreditsRule(config));

        EligibilityStore store = new FakeEligibilityStore();
        EligibilityEngine engine = new EligibilityEngine(rules, new ReportPrinter(), store);

        StudentProfile s = new StudentProfile("23BCS1001", "Ayaan", 8.10, 72, 18, LegacyFlags.NONE);
        engine.runAndPrint(s);
    }
}
