package commands;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotInfo extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() != null) {
            if (event.getName().equals("botinfo")) {
                botInfo(event);
            }
        }
    }

    private void botInfo(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        long ramUsage = (Runtime.getRuntime().totalMemory()) - (Runtime.getRuntime().freeMemory());

        event.getHook().sendMessageEmbeds(new EmbedBuilder()
                        .setTitle(":robot: BOT INFORMATION :robot:")
                        .setDescription(":wave: Hello! I am <@887765765195989073>")
                        .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                        .addField(":technologist: Developer", "YourApurba#5100", false)
                        .addField(":id: Bot ID:", event.getJDA().getSelfUser().getId(), false)
                        .addField(":calendar_spiral: Bot created on (YYYY/MM/DD)", "" + event.getJDA().getSelfUser().getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE), false)
                        .addField(":package: Build with (dependencies)", "JDA: "+JDAInfo.VERSION_MAJOR, false)
                        .addField(":keyboard: Programming language", "```System.out.print(\"Java\")```", false)
                        .addField(":regional_indicator_v: Bot version", "v 1.0.0", false)
                        .addField(":green_circle: UpTime since last reboot", BotInfo.botUptime(), false)
                        .addField(":computer: RAM usage", "`" + ramUsage/(1024 * 1024) + " MB`", false)
                        .addField(":globe_with_meridians: Servers", "Serving " + event.getJDA().getGuildCache().size() + " servers", true)
                        .addField(":tv: Channels", "Serving "+ event.getJDA().getTextChannelCache().size() + " channels", true)
                        .addField(":busts_in_silhouette: Server Users", "Serving "+ event.getJDA().getUserCache().size() + " users", true)
                        .setTimestamp(Instant.now())
                        .setFooter("Made with \uD83D\uDC99")
                        .setColor(Main.randomColor())
                        .build())
                .queue();
    }

    private static String botUptime() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        long uptime = runtime.getUptime();

        long uptimeInSeconds = uptime / 1000;

        long numberOfDays = TimeUnit.SECONDS.toDays(uptimeInSeconds);
        long numberOfHours = uptimeInSeconds / 3600;
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = (uptimeInSeconds % 60);

        return String.format("`%d Days, %d hours, %d minutes and %d seconds`", numberOfDays, numberOfHours, numberOfMinutes, numberOfSeconds);
    }
}