public class EmailSender extends NotificationSender {
    public EmailSender(AuditLog audit) {
        super(audit);
    }

    @Override
    protected void doSend(Notification n) {
        String body = n.body == null ? "" : n.body;
        System.out.println("EMAIL -> to=" + n.email + " subject=" + n.subject + " body=" + body);
    }

    @Override
    protected String channelName() {
        return "email";
    }
}
