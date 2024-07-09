package com.github.m5rian.cloudstudios.commands.developer;

import com.github.m5rian.cloudstudios.utils.Permissions.Developer;
import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class ManageRoles implements CommandHandler {

    @CommandEvent(
            name = "shift roles",
            requires = Developer.class
    )
    public void execute(CommandContext ctx) throws Exception {
        if (ctx.getArguments().length == 0) {
            ctx.getChannel().sendMessage("shift roles <role 1> <role2>").queue();
            return;
        }

        final List<Role> roles = ctx.getEvent().getMessage().getMentionedRoles();
        final Role previousRole = roles.get(0);
        final Role newRole = roles.get(1);

        ctx.getGuild().loadMembers().onSuccess(members -> {
            members.forEach(member -> {
                if (member.getRoles().contains(previousRole)) {
                    ctx.getGuild().addRoleToMember(member, newRole).queue();
                }
            });

            ctx.getChannel().sendMessage("updated" + members.size() + " members").queue();
        });
    }

    @CommandEvent(
            name = "roles delete",
            requires = Developer.class
    )
    public void onDeleteCommand(CommandContext ctx) throws Exception {
        if (ctx.getArguments().length == 0) {
            ctx.getChannel().sendMessage("roles delete <@role>").queue();
            return;
        }

        ctx.getEvent().getMessage().getMentionedRoles().get(0).delete().queue();

        ctx.getChannel().sendMessage("Don").queue();

    }


}
