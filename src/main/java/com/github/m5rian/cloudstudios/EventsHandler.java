package com.github.m5rian.cloudstudios;

import com.github.m5rian.cloudstudios.commands.member.Support;
import com.github.m5rian.cloudstudios.listeners.*;
import com.github.m5rian.cloudstudios.utils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.Instant;

public class EventsHandler extends ListenerAdapter {
    private final ReactionRoles reactionRoles = new ReactionRoles();
    private final Support support = new Support();
    private final Leveling leveling = new Leveling();
    private final Welcome welcome = new Welcome();
    private final CustomVoiceCalls customVoiceCalls = new CustomVoiceCalls();

    private final long guildId = Config.get().getLong("guildId");

    //JDA Events
    public void onReady(@Nonnull ReadyEvent event) {
        try {
            new Unbans().onReady(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Message Events
    //Guild (TextChannel) Message Events
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        try {
            if (event.getUser().isBot()) return; // Ignore bots

            reactionRoles.onReactionAdd(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
        try {
            reactionRoles.onReactionRemove(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Message Events
    //Guild (TextChannel) Message Events
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        try {
            if (event.getAuthor().isBot()) return;
            if (event.getGuild().getIdLong() != guildId) return;

            leveling.onMessage(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Guild Member Events
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        try {
            if (event.getGuild().getIdLong() != guildId) return;

            welcome.onJoin(event); // Send welcome message
            welcome.assignAutoRole(event); // Assign auto role
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Guild Voice Events
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        try {
            if (event.getGuild().getIdLong() != guildId) return;

            customVoiceCalls.onVoiceJoin(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        try {
            if (event.getGuild().getIdLong() != guildId) return;

            customVoiceCalls.onVoiceLeave(event.getChannelLeft());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        try {
            if (event.getGuild().getIdLong() != guildId) return;

            customVoiceCalls.onVoiceLeave(event.getChannelLeft());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        if (!event.getName().equals("ping")) return; // make sure we handle the right command
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue(); // Queue both reply and edit
    }
}
