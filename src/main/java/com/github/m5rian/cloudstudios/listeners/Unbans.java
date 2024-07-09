package com.github.m5rian.cloudstudios.listeners;

import com.github.m5rian.cloudstudios.database.Database;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import com.github.m5rian.cloudstudios.utils.Config;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Unbans {
    private final ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(1);

    public void onReady(ReadyEvent event) {
        JSONArray bans = Database.get("bans"); // Get bans

        for (int i = 0; i < bans.length(); ) {
            final JSONObject ban = bans.getJSONObject(i); // Get current ban

            final long unbanTime = ban.getLong("unbanTime"); // Get time to unban

            // Unban time already reached
            if (unbanTime < System.currentTimeMillis()) {
                final Guild guild = event.getJDA().getGuildById(Config.get().getLong("guildId")); // Get guild
                guild.unban(String.valueOf(ban.getLong("id"))).queue(); // Unban member
                bans = remove(bans, ban); // Remove ban from com.github.m5rian.cloudstudios.database
            }
            // Unban time isn't reached -> start timer
            else {
                final long wait = System.currentTimeMillis() - unbanTime; // Get time to wait
                timer.schedule(() -> {
                    final Guild guild = event.getJDA().getGuildById(Config.get().getLong("guildId")); // Get guild
                    guild.unban(String.valueOf(ban.getLong("id"))).queue(); // Unban member

                    JSONArray newBans = Database.get("bans"); // Get current bans
                    newBans = remove(newBans, ban); // Remove ban
                    Database.update("bans", newBans); // Update com.github.m5rian.cloudstudios.database
                }, wait, TimeUnit.MILLISECONDS);
                i++; // Increase i
            }
        }

        Database.update("bans", bans); // Update com.github.m5rian.cloudstudios.database
    }

    private JSONArray remove(JSONArray source, JSONObject object) {
        for (int i = 0; i < source.length(); i++) {
            final JSONObject currentObject = source.getJSONObject(i);
            if (currentObject.getLong("id") == object.getLong("id")) {
                source.remove(i);
            }
        }
        return source;
    }
}
