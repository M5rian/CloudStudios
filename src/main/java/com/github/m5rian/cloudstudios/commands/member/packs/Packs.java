package com.github.m5rian.cloudstudios.commands.member.packs;

import com.github.m5rian.cloudstudios.utils.Config;
import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

public class Packs implements CommandHandler {

    @CommandEvent(
            name = "packs",
            aliases = {"pack", "rigs", "rig"}
    )
    public void onPackUsage(CommandContext ctx) throws Exception {
        if (ctx.getArguments().length != 0) return;

        final String search = String.format("%n`%spacks search <keyword>`", ctx.getPrefix());
        final String browse = String.format("%n`%spacks browse`", ctx.getPrefix());
        final String request = String.format("%n`%spacks request <pack description>`", ctx.getPrefix());

        EmbedBuilder help = new EmbedBuilder()
                .setAuthor("packs", null, ctx.getAuthor().getEffectiveAvatarUrl())
                .setDescription("Subcommands:")
                .appendDescription(search)
                .appendDescription(browse)
                .appendDescription(request);
        ctx.getChannel().sendMessage(help.build()).queue();
    }

    @CommandEvent(
            name = "packs search",
            aliases = {"pack search", "rigs search", "rig search"}
    )
    public void onPackSearch(CommandContext ctx) throws Exception {
        // Command usage
        if (ctx.getArguments().length == 0) {
            final String usage = String.format("Command usage:%n`%spacks search <keyword>`", ctx.getPrefix());

            EmbedBuilder help = new EmbedBuilder()
                    .setAuthor("packs serach", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setDescription(usage);
            ctx.getChannel().sendMessage(help.build()).queue();
            return;
        }


        final String keyword = ctx.getArgumentsRaw();

        final List<Pack> search = RegisteredPacks.search(keyword);
        EmbedBuilder searchResult = new EmbedBuilder()
                .setAuthor("search result")
                .setDescription(String.format("%s packs found", search.size()));
        if (search.size() != 0) {
            for (int i = 0; i < 10; i++) {
                if (i + 1 > search.size()) break;

                final Pack pack = search.get(i);
                searchResult.appendDescription(String.format("%n**[%s](%s)**", pack.getName(), pack.getDownloadLink()));
            }
        }
        ctx.getChannel().sendMessage(searchResult.build()).queue();
    }

    @CommandEvent(
            name = "packs browse",
            aliases = {"pack browse", "rigs browse", "rig browse"}
    )
    public void onPacksBrowse(CommandContext ctx) throws Exception {
        EmbedBuilder packs = new EmbedBuilder()
                .setAuthor("packs browse");

        // Load first 10 packs
        for (int i = 0; i < 10; i++) {
            final Pack pack = RegisteredPacks.get(i);
            if (pack == null) break;
            packs.appendDescription(String.format("%n**[%s](%s)**", pack.getName(), pack.getDownloadLink()));
        }

        ctx.getChannel().sendMessage(packs.build()).queue();
    }

    @CommandEvent(
            name = "packs request",
            aliases = {"pack request", "rigs request", "rig request"}
    )
    public void onPackRequest(CommandContext ctx) throws Exception {
        if (ctx.getArguments().length == 0) {
            String usage = String.format("Command usage:%n`%spacks request <pack description>`", ctx.getPrefix());

            EmbedBuilder help = new EmbedBuilder()
                    .setAuthor("packs request", null, ctx.getAuthor().getEffectiveAvatarUrl())
                    .setDescription(usage);
            ctx.getChannel().sendMessage(help.build()).queue();
            return;
        }

        long botOwnerId = Config.get().getJSONObject("roles").getLong("botOwner");
        ctx.getEvent().getJDA().retrieveUserById(botOwnerId).queue(user -> {
            user.openPrivateChannel().queue(channel -> {
                final String format = String.format("Pack request by **%s**%n`%s`", ctx.getAuthor().getAsTag(), ctx.getArgumentsRaw());
                channel.sendMessage(format).queue();
            });
        });

        ctx.getEvent().getMessage().addReaction("\u2709").queue();
    }

}
