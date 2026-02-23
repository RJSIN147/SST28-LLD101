public interface EligibilityRule {
    /** Returns null if the student passes; otherwise a failure reason string. */
    String check(StudentProfile student);
}
