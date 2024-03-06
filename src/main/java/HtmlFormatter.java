public class HtmlFormatter {

    private static final String CSS = "<style>" +
            "body { text-align: center; color: white; font-size: 20px; } " +
            "span { font-weight: bold; } " +
            ".green { color: green; } " +
            ".red { color: red; } " +
            ".yellow { color: yellow; } " +
            "</style>";

    public static String formatWithCss(String content) {
        return "<html><head>" + CSS + "</head><body>" + content + "</body></html>";
    }
}
