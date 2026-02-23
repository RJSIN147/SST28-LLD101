public class CreditsRule implements EligibilityRule {
    private final int minCredits;

    public CreditsRule(RuleInput config) {
        this.minCredits = config.minCredits;
    }

    @Override
    public String check(StudentProfile student) {
        if (student.earnedCredits < minCredits) {
            return "credits below " + minCredits;
        }
        return null;
    }
}
