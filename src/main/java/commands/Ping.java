package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import main.Main;

import java.awt.Color;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Ping extends ListenerAdapter {
    long restPing;

    private final String[] pingEmotes = {
            ":ping_pong::white_small_square::black_small_square::black_small_square::ping_pong:",
            ":ping_pong::black_small_square::white_small_square::black_small_square::ping_pong:",
            ":ping_pong::black_small_square::black_small_square::white_small_square::ping_pong:",
            ":ping_pong::black_small_square::white_small_square::black_small_square::ping_pong:",
    };

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getRestPing().queue(time -> {
            restPing = time;
        });
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (message.getContentRaw().equals(Main.PREFIX + "ping")) {
            channel.sendMessage(":outbox_tray: **Checking ping...**").queue(response -> {
                long ping = message.getTimeCreated().until(response.getTimeCreated(), ChronoUnit.MILLIS);
                event.getJDA().getRestPing().queue(time -> {
                    restPing = time;
                });

                response.editMessageFormat(
                        "**pong!** :ping_pong:\n\n:inbox_tray: ** Bot latency:** `%d` ms\n:hourglass: ** WebSocket latency: ** `%d` ms\n:stopwatch: ** Rest latency: ** `%d` ms",
                        ping, event.getJDA().getGatewayPing(), restPing).queue();
            });

        }

        else if (message.getContentRaw().startsWith(Main.PREFIX + "ping") && args.length == 2 && args[1].matches("fancy")) {
            channel.sendMessageEmbeds(new EmbedBuilder().setTitle("**pong!** :ping_pong:")
                            .setDescription(":outbox_tray: **Checking ping...**").setColor(Color.red).build())
                    .queue(response -> {
                        int numberOfPings = 5, sum = 0;
                        long maxPing = 0, minPing = 1000;

                        for (int i = 1; i <= numberOfPings; i++) {
                            response.editMessageEmbeds(
                                    new EmbedBuilder().setDescription(pingEmotes[i % pingEmotes.length]).setColor(Color.yellow).build()).queue();

                            event.getJDA().getRestPing().queue(time -> {
                                restPing = time;
                            });

                            sum = (int) (sum + restPing);
                            minPing = Math.min(minPing, restPing);
                            maxPing = Math.max(maxPing, restPing);
                            try {
                                Thread.sleep(1500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        response.editMessageEmbeds(new EmbedBuilder()
                                .setTitle("**pong!** :ping_pong:")
                                .setDescription(String.format(
                                        ":sparkles: **Average ping: ** `%d` ms\n:chart_with_upwards_trend: **Max ping:** `%d` ms\n:chart_with_downwards_trend: **Min ping:** `%d` ms",
                                        Math.round(sum / (double) numberOfPings), maxPing, minPing))
                                .setColor(Color.green).setTimestamp(Instant.now()).build()).queue();
                    });
        }
    }
}
