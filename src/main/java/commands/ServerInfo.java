package commands;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerInfo extends ListenerAdapter {
    int onlineUsers, offlineUsers, idleUsers, doNotDisturbUsers;

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() != null) {
            if (event.getName().equals("serverinfo")) {
                serverInfo(event);
            }
        }
    }
    private void serverInfo(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        long bots = event.getGuild().getMembers().stream().filter(member -> member.getUser().isBot()).count();

        event.getHook().sendMessageEmbeds(new EmbedBuilder()
                        .setTitle(":globe_with_meridians: SERVER INFORMATION :globe_with_meridians:")
                        .setThumbnail(event.getGuild().getIconUrl())
                        .addField(":name_badge: Server name", "`" + event.getGuild().getName() + "`", false)
                        .addField(":crown: Owned by", "<@" + event.getGuild().getOwnerIdLong() + ">", false)
                        .addField(":id: Server ID:", "`" + event.getGuild().getId() + "`", false)
                        .addField(":calendar_spiral: Server created on (YYYY/MM/DD)", "`" + event.getGuild().getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE) + "`", false)
                        .addField(String.format(":busts_in_silhouette: Members (%d)", event.getGuild().getMemberCount()), String.format(":robot: `Bots %d`\n:adult: `Humans %d`", bots, event.getGuild().getMemberCount() - bots), true)
                        .addField(":earth_asia: Member Status : ", getMembersStatus(event), true)
                        .addField(":closed_lock_with_key: Roles", "`" +event.getGuild().getRoles().size() + "`", false)
                        .addField(":tv: Default Channel", "`" + event.getGuild().getSystemChannel().getName() + "`",  false)
                        .addField(":file_folder: Categories: ", "`" + event.getGuild().getCategories().size() + "`", true)
                        .addField(":page_facing_up: Text Channels: ", "`" + event.getGuild().getTextChannels().size() + "`", true)
                        .addField(":microphone: Voice Channels: ", "`"+ event.getGuild().getVoiceChannels().size() + "`", true)
                        .setFooter(String.format("Requested by %s #%s", event.getUser().getName(), event.getUser().getDiscriminator()), event.getUser().getAvatarUrl())
                        .setTimestamp(Instant.now())
                        .setColor(Main.randomColor())
                        .build())
                .queue();
    }

    private String getMembersStatus(SlashCommandInteractionEvent event) {
        onlineUsers = 0;
        offlineUsers = 0;
        idleUsers = 0;
        doNotDisturbUsers= 0 ;
        List<Member> users = event.getGuild().getMembers();

        for (Member member : users) {
            if(member.getOnlineStatus() == OnlineStatus.ONLINE) {
                onlineUsers ++;
            }
            else if(member.getOnlineStatus() == OnlineStatus.OFFLINE || member.getOnlineStatus() == OnlineStatus.INVISIBLE) {
                offlineUsers ++;
            }
            else if(member.getOnlineStatus() == OnlineStatus.IDLE ) {
                idleUsers++;
            }
            else if (member.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
                doNotDisturbUsers++;
            }
        }
        return String.format(":green_circle: `Online : %d`\n:white_circle: `Offline : %d`\n:yellow_circle: `Idle : %d`\n:red_circle: `Do Not Disturb : %d`", onlineUsers, offlineUsers, idleUsers, doNotDisturbUsers);
    }
}