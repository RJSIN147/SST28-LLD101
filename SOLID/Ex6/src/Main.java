public class Main {
    public static void main(String[] args) {
        System.out.println("=== Notification Demo ===");
        AuditLog audit = new AuditLog();

        Notification n = new Notification("Welcome", "Hello and welcome to SST!", "riya@sst.edu", "9876543210");

        NotificationSender[] senders = { new EmailSender(audit), new SmsSender(audit), new WhatsAppSender(audit) };

        for (NotificationSender sender : senders) {
            sender.send(n);
        }

        System.out.println("AUDIT entries=" + audit.size());
    }
}
