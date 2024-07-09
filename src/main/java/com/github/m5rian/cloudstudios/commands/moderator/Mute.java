package com.github.m5rian.cloudstudios.commands.moderator;

import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import com.github.m5rian.cloudstudios.utils.Config;
import com.github.m5rian.cloudstudios.utils.Permissions.Staff;

import java.util.List;

public class Mute implements CommandHandler {
    private final long muteRoleId = Config.get().getJSONObject("roles").getLong("muted");

    @CommandEvent(
            name = "mute",
            requires = Staff.class
    )
    public void execute(CommandContext ctx) throws Exception {
        // Command usage
        if (ctx.getArguments().length != 1) {
            ctx.getChannel().sendMessage(String.format("```asciidoc%n[Missing arguments]%n= %smute <member>```", Config.get().getString("prefix"))).queue();
            return;
        }

        final List<Member> members = ctx.getEvent().getMessage().getMentionedMembers();
        // No member mentioned
        if (members.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder()
                    .setTitle("No member mentioned")
                    .setColor(0xFF5555);
            ctx.getChannel().sendMessage(error.build()).queue();
        }

        //Mute member
        final Role muted = ctx.getGuild().getRoleById(muteRoleId); // Get mute role
        ctx.getGuild().addRoleToMember(members.get(0), muted); // Add role to member
    }
}
