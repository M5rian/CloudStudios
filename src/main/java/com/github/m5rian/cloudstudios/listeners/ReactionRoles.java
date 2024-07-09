package com.github.m5rian.cloudstudios.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import com.github.m5rian.cloudstudios.utils.Config;

public class ReactionRoles {

    public void onReactionAdd(GuildMessageReactionAddEvent event) {
        final JSONArray reactionRoles = Config.get().getJSONArray("reactionRoles"); // Get reaction roles

        for (int i = 0; i < reactionRoles.length(); i++) {
            final JSONObject reactionRole = reactionRoles.getJSONObject(i); // Get reaction role

            final long messageId = reactionRole.getLong("messageId"); // Get message id of reaction role
            final String unicodeEmoji = reactionRole.getString("unicodeEmoji"); // Get reaction emoji
            final long roleId = reactionRole.getLong("roleId"); // Get role to assign

            if (event.getMessageIdLong() != messageId) continue; // Wrong message
            if (event.getReactionEmote().isEmote()) continue; // Reaction is an emote
            if (!event.getReactionEmote().getEmoji().equals(unicodeEmoji)) continue; // Wrong emoji

            final Role role = event.getGuild().getRoleById(roleId); // Get role to assign
            event.retrieveMember().queue(member -> { // Retrieve member
                event.getGuild().addRoleToMember(member, role).queue(); // Add role to member
            });

        }
    }

    public void onReactionRemove(GuildMessageReactionRemoveEvent event) {
        final JSONArray reactionRoles = Config.get().getJSONArray("reactionRoles"); // Get reaction roles

        for (int i = 0; i < reactionRoles.length(); i++) {
            final JSONObject reactionRole = reactionRoles.getJSONObject(i); // Get reaction role

            final long messageId = reactionRole.getLong("messageId"); // Get message id of reaction role
            final String unicodeEmoji = reactionRole.getString("unicodeEmoji"); // Get reaction emoji
            final long roleId = reactionRole.getLong("roleId"); // Get role to assign

            if (event.getMessageIdLong() != messageId) continue; // Wrong message
            if (event.getReactionEmote().isEmote()) continue; // Reaction is an emote
            if (!event.getReactionEmote().getEmoji().equals(unicodeEmoji)) continue; // Wrong emoji

            final Role role = event.getGuild().getRoleById(roleId); // Get role to remove
            event.retrieveMember().queue(member -> { // Retrieve member
                event.getGuild().removeRoleFromMember(member, role).queue(); // Remove role from member
            });

        }
    }

}