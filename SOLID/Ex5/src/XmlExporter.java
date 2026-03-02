import java.nio.charset.StandardCharsets;

public class XmlExporter extends Exporter {
    @Override
    protected ExportResult doExport(ExportRequest req) {
        String body = req.body == null ? "" : req.body;
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<export>\n"
                + "  <title>" + xmlEscape(req.title) + "</title>\n"
                + "  <body>" + xmlEscape(body) + "</body>\n"
                + "</export>";
        return new ExportResult("application/xml", xml.getBytes(StandardCharsets.UTF_8));
    }

    private String xmlEscape(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
