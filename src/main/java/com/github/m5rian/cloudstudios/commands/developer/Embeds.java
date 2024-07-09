package com.github.m5rian.cloudstudios.commands.developer;

import com.github.m5rian.cloudstudios.utils.Config;
import com.github.m5rian.cloudstudios.utils.Permissions.Developer;
import com.github.m5rian.jdaCommandHandler.CommandContext;
import com.github.m5rian.jdaCommandHandler.CommandEvent;
import com.github.m5rian.jdaCommandHandler.CommandHandler;
import net.dv8tion.jda.api.EmbedBuilder;

public class Embeds implements CommandHandler {

    @CommandEvent(
            name = "start-here",
            requires = Developer.class
    )
    public void startHereEmbed(CommandContext ctx) {
        EmbedBuilder welcome = new EmbedBuilder()
                .setAuthor("Welcome!", Config.get().getString("invite"), ctx.getGuild().getIconUrl())
                .setColor(Config.colour)
                // Welcome
                .setDescription("CloudStudios is a server dedicated to improve and share your designs with other designers!\n\n" +
                        "Underneath you can find all information you will need to get you started in our server! If you still have questions after reading this, just contact one of our staff members!")
                // Designer ranks
                .appendDescription("\n\n**Designer ranks**" +
                        "\nYou want to get some cool and shiny designer ranks? Go and post your best designs in <#847047197564338213>! You'll receive a rank depending on how good your design is." +
                        "\nIf you're mad because you didn't get god designer improve your designs and come back later! The better designs you send the higher rank you'll receive!")
                // Support
                // Designer ranks
                .appendDescription("\n\n**You need help?**" +
                        "\nWe made a ticket system, which you can use to get support. For this type in a channel the following command: `.open`. This will create a channel for you where you can explain your problem!\n" +
                        "Don't be afraid to use this feature, our staff members are here to help, even if you think you're question is dumb ;)")
                // Invite link
                .appendDescription("\n\n[**\uD83D\uDD17Invite**](" + Config.get().getString("invite") + ")");
        ctx.getChannel().sendMessage(welcome.build()).queue();
    }

    @CommandEvent(
            name = "rules",
            requires = Developer.class
    )
    public void rulesEmbed(CommandContext ctx) {
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Rules", Config.get().getString("invite"), ctx.getGuild().getIconUrl())
                .setColor(Config.colour)
                .setDescription("I. Through joining our server you **automatically** accept the rules\n" +
                        "\n" +
                        "II. I didn't know the rule is just dumb... So we'll ignore that (:\n" +
                        "\n" +
                        "III. Disrespecting other members is not allowed\n" +
                        "\n" +
                        "IV. Insults are not allowed\n" +
                        "\n" +
                        "V. We give objective opinions\n" +
                        "\n" +
                        "VI. Rule evasion or attempts to test the limits of what is possible is not allowed\n" +
                        "\n" +
                        "VII. Don't pretend to have designs which aren't yours\n" +
                        "\n" +
                        "VIII. Do not ping any roles\n" +
                        "\n" +
                        "IX. You need to follow the teams instructions\n" +
                        "\n" +
                        "X. If you want to advertise, use <#847047197685579798> Don't promote cruelty, violence, self-harm, suicide, pornography or a Discord server. Also do not DM advertise!\n" +
                        "\n" +
                        "XI. Follow [Discord's terms of service](https://discord.com/terms) and make sure you follow [Discord's community](https://discord.com/guidelines) guidelines.");
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

}
