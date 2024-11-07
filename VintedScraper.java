import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;
import java.net.http.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VintedScraper {

    private static final Logger logger = Logger.getLogger(VintedScraper.class.getName());
    private static final String DISCORD_WEBHOOK_URL = "";  
    
    // Configuration
    private static JSONObject config;

    public static void main(String[] args) {
        // Load the config file on startup
        loadConfig();
        
        // Setup periodic scraping every 15 minutes
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrapeAndPost("vinted_url_here");  // Example URL, can be dynamic
            }
        }, 0, 15 * 60 * 1000);  // 15 minutes interval
        
        logger.info("Vinted Scraper Bot started.");
    }

    public static void loadConfig() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("config.json")));
            config = new JSONObject(content);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading config file", e);
        }
    }

    public static void scrapeAndPost(String url) {
        try {
            // Make the HTTP request to Vinted
            Document doc = Jsoup.connect(url).get();
            
            // Find item details (JS script embedded in the page)
            Elements items = doc.select("script.js-react-on-rails-component");
            
            // Iterate through items and extract data
            for (int i = 0; i < items.size(); i++) {
                String jsonData = items.get(i).html();
                JSONObject jsonObject = new JSONObject(jsonData);
                
                // Extract details like title, description, price
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                String price = jsonObject.getString("price");
                
                // Post to Discord
                postToDiscord(title, description, price, url);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error scraping Vinted", e);
        }
    }

    private static void postToDiscord(String title, String description, String price, String url) {
        try {
            // Create the payload for Discord
            JSONObject payload = new JSONObject();
            payload.put("content", "New Vinted Item: " + title);
            JSONObject embed = new JSONObject();
            embed.put("title", title);
            embed.put("description", description);
            embed.put("color", 3447003);  //  can be customized
            embed.put("url", url);
            payload.put("embeds", new Object[]{embed});

            // Send the HTTP request to Discord
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DISCORD_WEBHOOK_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Posted new item to Discord: " + title);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error posting to Discord", e);
        }
    }
}
