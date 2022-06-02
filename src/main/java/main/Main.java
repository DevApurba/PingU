package main;

import java.util.Random;
import java.awt.Color;

import commands.BotInfo;
import commands.Help;
import commands.Ping;
import commands.ServerInfo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {
    private final static String TOKEN = System.getenv().get("TOKEN");
    public final static String PREFIX = "!";

    public static void main(String[] args) throws Exception {
        startBot();
    }

    public static void startBot() throws Exception {
        JDA jda = JDABuilder.createDefault(TOKEN)
                .setActivity(Activity.watching("/help"))
                .setStatus(OnlineStatus.ONLINE)
                .enableIntents(GatewayIntent.GUILD_PRESENCES , GatewayIntent.GUILD_MEMBERS)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new Ping(), new BotInfo(), new ServerInfo(), new Help())
                .build().awaitReady();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("botinfo", "Shows information and stats about the bot"),
                Commands.slash("serverinfo", "Shows information about the server"),
                Commands.slash("help", "Get some help").setDescription("Shows list of all the available commands")
        );
        commands.queue();
    }

    public static Color randomColor() {
        Random random = new Random();

        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return new Color(r, g, b).brighter();
    }
}