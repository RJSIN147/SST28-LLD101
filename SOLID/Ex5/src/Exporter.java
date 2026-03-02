public abstract class Exporter {

    public final ExportResult export(ExportRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("req must not be null");
        }
        if (req.title == null) {
            throw new IllegalArgumentException("title must not be null");
        }

        ExportResult result = doExport(req);

        if (result == null || result.bytes == null) {
            throw new IllegalStateException("exporter produced null result");
        }
        return result;
    }

    protected abstract ExportResult doExport(ExportRequest req);
}
