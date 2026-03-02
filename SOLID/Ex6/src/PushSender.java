public class PushSender extends NotificationSender {
    public PushSender(AuditLog audit) {
        super(audit);
    }

    @Override
    protected void doSend(Notification n) {
        String body = n.body == null ? "" : n.body;
        System.out.println("PUSH -> title=" + n.subject + " body=" + body);
    }

    @Override
    protected String channelName() {
        return "push";
    }
}
