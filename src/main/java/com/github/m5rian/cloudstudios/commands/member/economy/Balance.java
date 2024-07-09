package com.github.m5rian.cloudstudios.commands.member.economy;

import com.github.m5rian.cloudstudios.database.DbMember;
import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;
import net.dv8tion.jda.api.EmbedBuilder;

public class Balance implements CommandHandler {

    @CommandEvent(
            name = "balance",
            aliases = {"bal"}
    )
    public void execute(CommandContext ctx) throws Exception {
        final DbMember dbMember = new DbMember(ctx.getMember()); // Get member from database

        final EmbedBuilder balance = new EmbedBuilder()
                .setAuthor("Balance", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setColor(ctx.getMember().getColor())
                .setThumbnail(ctx.getAuthor().getEffectiveAvatarUrl())
                .addField("Balance", "`" + dbMember.getBalance() + "`", false);
        ctx.getChannel().sendMessageEmbeds(balance.build()).queue();
    }
}
