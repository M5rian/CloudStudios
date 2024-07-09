package com.github.m5rian.cloudstudios.commands.member;

import com.github.m5rian.cloudstudios.CloudStudios;
import com.github.m5rian.cloudstudios.utils.Config;
import com.github.m5rian.cloudstudios.utils.Permissions.Administrator;
import com.github.m5rian.cloudstudios.utils.Permissions.Developer;
import com.github.m5rian.cloudstudios.utils.Permissions.Staff;
import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;
import com.github.m5rian.jdaCommandHandler.exceptions.NotRegisteredException;
import net.dv8tion.jda.api.EmbedBuilder;

public class Help implements CommandHandler {

    @CommandEvent(
            name = "help",
            aliases = {"commands"}
    )
    public void onHelpCommand(CommandContext ctx) throws NotRegisteredException {
        final String[] memberCommands = {"`help`", "`rank`", "`open`", "`close`"};
        final String[] moderatorCommands = {"`ban`", "`mute`", "`reload`"};
        final String[] developerCommands = {"`start-here`"};
        final String[] adminCommands = {"`prefix`", "`leveling user remove`"};

        final EmbedBuilder help = new EmbedBuilder()
                .setTitle("Help")
                .addField("Members", String.join(" ", memberCommands), false)
                .setColor(Config.colour);
        // Moderator commands
        if (CloudStudios.COMMAND_SERVICE.hasPermissions(ctx.getMember(), Staff.class)) {
            help.addField("Moderator", String.join(" ", moderatorCommands), false);
        }
        // Developer commands
        if (CloudStudios.COMMAND_SERVICE.hasPermissions(ctx.getMember(), Developer.class)) {
            help.addField("Developer", String.join(" ", developerCommands), false);
        }
        // Administrator commands
        if (CloudStudios.COMMAND_SERVICE.hasPermissions(ctx.getMember(), Administrator.class)) {
            help.addField("Administrator", String.join(" ", adminCommands), false);
        }

        ctx.getChannel().sendMessage(help.build()).queue();
    }

}
