package com.github.m5rian.cloudstudios.listeners;

import com.github.m5rian.cloudstudios.database.DbMember;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Leveling {

    /**
     * @param xp The experience points to calculate the level from.
     * @return Returns the level of the specified experience.
     */
    public static int getLevel(long xp) {
        // Parabola
        double dividedNumber = xp / 100;
        double power = Math.sqrt(dividedNumber); // take root

        return (int) Math.round(power); // Round
    }

    /**
     * @param level The level to get the xp from.
     * @return Returns the required xp for the specified level.
     */
    public static int getXp(int level) {
        // Parabola
        double squaredNumber = Math.pow(level, 2); // Square number
        double multiplied = squaredNumber * 100;

        return (int) Math.round(multiplied); // Round
    }

    /**
     * Update the member in the database.
     *
     * @param e A fired {@link GuildMessageReceivedEvent}.
     */
    public void onMessage(GuildMessageReceivedEvent e) {
        final DbMember dbMember = new DbMember(e.getMember()); // Get member from database

        int randomXp = ThreadLocalRandom.current().nextInt(3); // Get random xp
        int randomBalance = ThreadLocalRandom.current().nextInt(5); // Get random amount of balance

        System.out.println(getLevel(dbMember.getXp()));
        final int newLevel = getLevel(dbMember.getXp() + randomXp); // Get new level
        if (newLevel > dbMember.getLevel()) { // Level up
            dbMember.setLevel(dbMember.getLevel() + 1);
            EmbedBuilder levelUp = new EmbedBuilder()
                    .setTitle("\u2601 Level up! \u2601")
                    .setColor(e.getMember().getColor())
                    .setDescription(String.format("You are now level **%s**!", newLevel));
            e.getChannel().sendMessage(e.getMember().getAsMention()).embed(levelUp.build()).queue();
        }

        // Update member
        dbMember.setXp(dbMember.getXp() + randomXp); // Update xp
        dbMember.setBalance(dbMember.getBalance() + randomBalance); // Update xp
    }
}
