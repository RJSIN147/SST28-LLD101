public class CgrRule implements EligibilityRule {
    private final double minCgr;

    public CgrRule(RuleInput config) {
        this.minCgr = config.minCgr;
    }

    @Override
    public String check(StudentProfile student) {
        if (student.cgr < minCgr) {
            return "CGR below " + minCgr;
        }
        return null;
    }
}
