package com.github.m5rian.cloudstudios.commands.member.leveling;

import com.github.m5rian.cloudstudios.database.Database;
import com.github.m5rian.cloudstudios.database.DbMember;
import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Rank implements CommandHandler {

    @CommandEvent(
            name = "rank"
    )
    public void execute(CommandContext ctx) throws Exception {
        final List<DbMember> members = new ArrayList<>();

        final JSONArray file = Database.get("members"); // Get members
        for (int i = 0; i < file.length(); i++) {
            final Long userId = file.getJSONObject(i).getLong("id"); // Get id of current database member

            final User user = ctx.getBot().retrieveUserById(userId).complete();
            // User is no member of the server
            if (!ctx.getGuild().isMember(user)) {
                Database.remove("members", userId); // Remove old member from database
            }
            // User is still a member
            else {
                final Member member = ctx.getGuild().retrieveMemberById(userId).complete(); // Get member
                members.add(new DbMember(member)); // Add member to list
            }
        }

        final Member member = ctx.getEvent().getMessage().getMentionedMembers().isEmpty() ? ctx.getMember() : ctx.getEvent().getMessage().getMentionedMembers().get(0); // Get member
        Collections.sort(members, Comparator.comparing(DbMember::getXp).reversed());
        for (int i = 0; i < members.size(); i++) {
            final DbMember dbMember = members.get(i); // Current member
            if (dbMember.getId() != member.getIdLong()) continue;

            final EmbedBuilder rank = new EmbedBuilder()
                    .setTitle("Rank")
                    .setColor(member.getColor())
                    .setThumbnail(member.getUser().getEffectiveAvatarUrl())
                    .addField("Rank", String.format("`%s`", i + 1), false)
                    .addField("Level", String.format("`%s`", dbMember.getLevel()), true)
                    .addField("Xp", String.format("`%s`", dbMember.getXp()), true);
            ctx.getChannel().sendMessageEmbeds(rank.build()).queue();
        }
    }
}
