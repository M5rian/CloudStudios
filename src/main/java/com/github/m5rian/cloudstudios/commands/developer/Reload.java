package com.github.m5rian.cloudstudios.commands.developer;

import com.github.m5rian.cloudstudios.commands.member.packs.RegisteredPacks;
import com.github.m5rian.cloudstudios.utils.Permissions.Developer;
import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;

public class Reload implements CommandHandler {

    /**
     * Reloads the registered packs and rigs without a restart.
     *
     * @param ctx The {@link CommandContext} of the fired event.
     */
    @CommandEvent(
            name = "reload",
            requires = Developer.class
    )
    public void onReload(CommandContext ctx) {
        RegisteredPacks.register(); // Reload packs
        ctx.getChannel().sendMessage("Successfully reloaded assets").queue();
    }

}
