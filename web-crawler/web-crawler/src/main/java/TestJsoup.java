import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestJsoup {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://example.com").get();
            System.out.println(doc.title());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
