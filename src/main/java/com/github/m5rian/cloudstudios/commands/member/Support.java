package com.github.m5rian.cloudstudios.commands.member;

import com.github.m5rian.cloudstudios.utils.Config;
import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.json.JSONArray;

import java.util.Collections;
import java.util.EnumSet;

public class Support implements CommandHandler {
    private final String ticketPrefix = "\u300C\uD83C\uDFAB\u300D";

    @CommandEvent(
            name = "open"
    )
    public void onSupportRequest(CommandContext ctx) {
        // Get staff category
        final Long staffCategoryId = Config.get().getLong("staffCategory"); // Get staff category id
        final Category staffCategory = ctx.getGuild().getCategoryById(staffCategoryId); // Get staff category

        final JSONArray staffs = Config.get().getJSONObject("roles").getJSONArray("staff"); // Get all staff role ids

        EnumSet<Permission> read = EnumSet.of(Permission.MESSAGE_READ);

        final ChannelAction<TextChannel> channelCreation = staffCategory.createTextChannel(ticketPrefix + ctx.getAuthor().getName())
                // Deny access for @everyone
                .addRolePermissionOverride(ctx.getGuild().getPublicRole().getIdLong(), Collections.emptyList(), read)
                // Allow access for ticket author
                .addMemberPermissionOverride(ctx.getMember().getIdLong(), read, Collections.emptyList());
        //  Allow access for all staff roles
        for (int i = 0; i < staffs.length(); i++) {
            // Allow access for staff
            channelCreation.addRolePermissionOverride(staffs.getLong(i), read, Collections.emptyList());
        }

        channelCreation.queue(channel -> {
            ctx.getAuthor().openPrivateChannel().queue(dms -> dms.sendMessage("A wild support channel appeared! Go catch it in " + channel.getAsMention()).queue());
        });
    }

    @CommandEvent(
            name = "close"
    )
    public void onSupportClose(CommandContext ctx) {
        final Long staffCategoryId = Config.get().getLong("staffCategory"); // Get staff category id

        final TextChannel channel = (TextChannel) ctx.getChannel(); // Get channel as guild text channel

        if (channel.getParent().getIdLong() != staffCategoryId) return; // Channel isn't in the staff category
        if (!channel.getName().startsWith(ticketPrefix)) return; // Channel isn't a ticket channel

        channel.delete().queue();
    }

}
