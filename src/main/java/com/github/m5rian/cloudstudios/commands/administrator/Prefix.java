package com.github.m5rian.cloudstudios.commands.administrator;

import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;
import org.json.JSONObject;
import com.github.m5rian.cloudstudios.utils.Config;
import com.github.m5rian.cloudstudios.utils.Permissions.Administrator;

public class Prefix implements CommandHandler {
    @CommandEvent(
            name = "prefix",
            requires = Administrator.class
    )
    public void execute(CommandContext ctx) throws Exception {
        // Command usage
        if (ctx.getArguments().length != 1) {
            ctx.getChannel().sendMessage(String.format("```asciidoc%n[Missing arguments]%n= %sprefix <new prefix>```", Config.get().getString("prefix"))).queue();
            return;
        }

        final String prefix = ctx.getArguments()[0]; // Get new prefix
        final JSONObject config = Config.get(); // Get config file
        config.put("prefix", prefix); // Update prefix
        Config.update(config); // Update config file

        ctx.getChannel().sendMessage(String.format("```cs%n'Prefix changed!'%n```%n > **Prefix changed to `%s`**", prefix)).queue();
    }
}
