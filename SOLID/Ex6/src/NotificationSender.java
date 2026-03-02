public abstract class NotificationSender {
    protected final AuditLog audit;

    protected NotificationSender(AuditLog audit) {
        this.audit = audit;
    }

    public final void send(Notification n) {
        if (n == null) {
            throw new IllegalArgumentException("notification must not be null");
        }
        doSend(n);
        audit.add(channelName() + " sent");
    }

    protected abstract void doSend(Notification n);

    protected abstract String channelName();
}
