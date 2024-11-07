import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDABuilder;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

public class DiscordBot extends ListenerAdapter {

    private static JSONObject config;

    public static void main(String[] args) throws Exception {
        // Load configuration
        loadConfig();

        // Start the bot
        String token = config.getString("discord_token");
        JDABuilder.createDefault(token).addEventListeners(new DiscordBot()).build();
    }

    public static void loadConfig() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("config.json")));
            config = new JSONObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        TextChannel channel = event.getTextChannel();

        // Handle command: $sub <vinted_url>
        if (message.startsWith("$sub")) {
            String[] parts = message.split(" ");
            if (parts.length > 1) {
                String vintedUrl = parts[1];
                addSubscription(channel, vintedUrl);
            }
        }
        
        // Handle command: $remove_sub
        if (message.startsWith("$remove_sub")) {
            removeSubscription(channel);
        }
        
        // Handle command: $list_subs
        if (message.startsWith("$list_subs")) {
            listSubscriptions(channel);
        }

        // Handle command: $help
        if (message.startsWith("$help")) {
            displayHelp(channel);
        }
    }

    private void addSubscription(TextChannel channel, String vintedUrl) {
        // Add subscription (you can store it in a file or database)
        JSONObject subscription = new JSONObject();
        subscription.put("url", vintedUrl);
        subscription.put("channel", channel.getName());

        // Respond to the user
        channel.sendMessage("Subscription added for URL: " + vintedUrl).queue();
    }

    private void removeSubscription(TextChannel channel) {
        // Remove the subscription for the current channel (you can modify this to look up from a storage)
        channel.sendMessage("Subscription removed for channel: " + channel.getName()).queue();
    }

    private void listSubscriptions(TextChannel channel) {
        // List all subscriptions (can be fetched from a stored list or file)
        channel.sendMessage("Listing all subscriptions...").queue();
    }

    private void displayHelp(TextChannel channel) {
        // Provide help message with available commands
        channel.sendMessage("**Available Commands:**\n" +
            "$sub <vinted_url> - Add a new subscription\n" +
            "$remove_sub - Remove the current subscription\n" +
            "$list_subs - List all subscriptions\n" +
            "$help - Show this help message").queue();
    }
}
