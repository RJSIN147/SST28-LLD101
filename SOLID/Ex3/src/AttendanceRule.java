public class AttendanceRule implements EligibilityRule {
    private final int minAttendance;

    public AttendanceRule(RuleInput config) {
        this.minAttendance = config.minAttendance;
    }

    @Override
    public String check(StudentProfile student) {
        if (student.attendancePct < minAttendance) {
            return "attendance below " + minAttendance;
        }
        return null;
    }
}
