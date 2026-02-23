import java.util.*;

public class StudentValidator {

    public List<String> validate(Map<String, String> fields) {
        String name = fields.getOrDefault("name", "");
        String email = fields.getOrDefault("email", "");
        String phone = fields.getOrDefault("phone", "");
        String program = fields.getOrDefault("program", "");

        List<String> errors = new ArrayList<>();
        if (name.isBlank())
            errors.add("name is required");
        if (email.isBlank() || !email.contains("@"))
            errors.add("email is invalid");
        if (phone.isBlank() || !phone.chars().allMatch(Character::isDigit))
            errors.add("phone is invalid");
        if (!(program.equals("CSE") || program.equals("AI") || program.equals("SWE")))
            errors.add("program is invalid");
        return errors;
    }
}
