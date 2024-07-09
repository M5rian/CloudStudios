package com.github.m5rian.cloudstudios.listeners;

import com.github.m5rian.cloudstudios.utils.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CustomVoiceCalls {
    private final String voicePrefix = "\u300C\uD83D\uDCAB\u300D";

    private final Collection<Permission> allow = new ArrayList<>() {{
        add(Permission.MANAGE_CHANNEL);
        add(Permission.VOICE_MUTE_OTHERS);
        add(Permission.VOICE_DEAF_OTHERS);
        add(Permission.VOICE_MOVE_OTHERS);
    }};

    public void onVoiceJoin(GuildVoiceJoinEvent event) {
        final long joinedChannel = event.getChannelJoined().getIdLong(); // Get joined channel id
        final long hubChannel = Config.get().getJSONObject("customVoiceCalls").getLong("hub"); // Get voice call creation channel id
        // User wants to create a custom voice channel
        if (joinedChannel == hubChannel) {
            final long categoryId = Config.get().getJSONObject("customVoiceCalls").getLong("category"); // Get custom voice category id
            final Category category = event.getGuild().getCategoryById(categoryId); // Get category of custom voice calls

            final String memberName = event.getMember().getEffectiveName();
            category.createVoiceChannel(voicePrefix + memberName) // Set name
                    .addMemberPermissionOverride(event.getMember().getIdLong(), allow, Collections.emptyList()) // Set permission for author
                    .queue(channel -> { // Create channel
                        event.getGuild().moveVoiceMember(event.getMember(), channel).queue(); // Move member
                    });
        }
    }

    public void onVoiceLeave(VoiceChannel channel) {
        // Voice call is empty and voice call is a custom voice call
        if (channel.getMembers().size() == 0 && channel.getName().startsWith(voicePrefix)) {
            channel.delete().queue(); // Delete channel
        }
    }

}
