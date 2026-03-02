public class WhatsAppSender extends NotificationSender {
    public WhatsAppSender(AuditLog audit) {
        super(audit);
    }

    @Override
    protected void doSend(Notification n) {
        String phone = normalizePhone(n.phone);
        String body = n.body == null ? "" : n.body;
        System.out.println("WA -> to=" + phone + " body=" + body);
    }

    @Override
    protected String channelName() {
        return "wa";
    }

    private String normalizePhone(String phone) {
        if (phone == null)
            return "";
        if (!phone.startsWith("+"))
            return "+91" + phone;
        return phone;
    }
}
