package com.github.m5rian.cloudstudios;

import com.github.m5rian.cloudstudios.commands.administrator.Prefix;
import com.github.m5rian.cloudstudios.commands.developer.Embeds;
import com.github.m5rian.cloudstudios.commands.developer.ManageRoles;
import com.github.m5rian.cloudstudios.commands.developer.Reload;
import com.github.m5rian.cloudstudios.commands.member.Help;
import com.github.m5rian.cloudstudios.commands.member.Support;
import com.github.m5rian.cloudstudios.commands.member.economy.Balance;
import com.github.m5rian.cloudstudios.commands.member.leveling.Rank;
import com.github.m5rian.cloudstudios.commands.member.packs.Packs;
import com.github.m5rian.cloudstudios.commands.member.packs.RegisteredPacks;
import com.github.m5rian.cloudstudios.commands.moderator.Ban;
import com.github.m5rian.cloudstudios.commands.moderator.Mute;
import com.github.m5rian.cloudstudios.database.Database;
import com.github.m5rian.cloudstudios.utils.Config;
import com.github.m5rian.cloudstudios.utils.Permissions.Administrator;
import com.github.m5rian.cloudstudios.utils.Permissions.Developer;
import com.github.m5rian.cloudstudios.utils.Permissions.Owner;
import com.github.m5rian.cloudstudios.utils.Permissions.Staff;
import com.github.m5rian.jdaCommandHandler.CommandListener;
import com.github.m5rian.jdaCommandHandler.commandServices.DefaultCommandService;
import com.github.m5rian.jdaCommandHandler.commandServices.DefaultCommandServiceBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CloudStudios {
    public static final DefaultCommandService COMMAND_SERVICE = new DefaultCommandServiceBuilder()
            .setDefaultPrefix(Config.get().getString("prefix")) // Set a default prefix, which is used in dms
            .setVariablePrefix(guild -> Config.get().getString("prefix")) // Set a variable prefix, which you can change
            .registerCommandClasses(
                    // Administrator
                    new Prefix(),
                    // Developer
                    new Reload(),
                    new Embeds(),
                    new ManageRoles(),
                    // Moderation
                    new Mute(),
                    new Ban(),
                    // Member
                    new Rank(),
                    new Balance(),
                    new Packs(),
                    new Support(),
                    new Help())
            .allowMention()
            .build();

    public CloudStudios() throws LoginException, IOException, InterruptedException {
        checkFolder();

        // Register all permissions
        COMMAND_SERVICE.registerPermission(new Owner(), new Administrator(), new Developer(), new Staff());
        final JDA jda = JDABuilder.create(Config.get().getString("token"),
                // Enable intents
                GatewayIntent.GUILD_MESSAGES, // Guild messages
                GatewayIntent.DIRECT_MESSAGES, // Direct messages
                GatewayIntent.GUILD_MESSAGE_REACTIONS, // Guild message reactions
                GatewayIntent.GUILD_MEMBERS, // For joins and leaves
                GatewayIntent.GUILD_VOICE_STATES) // Voice join, leave and move
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS) // Disable caches (otherwise it would get logged)
                .enableCache(CacheFlag.VOICE_STATE) // Enable caches
                .addEventListeners(
                        new CommandListener(COMMAND_SERVICE),
                        new EventsHandler())
                .setStatus(OnlineStatus.IDLE) // Set online status to idle
                .build()
                .awaitReady();


        changeActivity(jda);
        RegisteredPacks.register(); // Register all packs
        jda.upsertCommand(new CommandData("help", "List all commands")).queue();
        jda.upsertCommand(new CommandData("rank", "Shows your current rank").addOption(OptionType.USER, "member", "the member you want to get the rank from", false)).queue();
        jda.upsertCommand(new CommandData("open", "Open a support channel")).queue();
        jda.upsertCommand(new CommandData("close", "Close a current active support channel")).queue();
    }

    public static void main(String[] args) throws LoginException, IOException, InterruptedException {
        new CloudStudios();
    }

    private void changeActivity(JDA jda) {
        final ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);

        timer.scheduleAtFixedRate(() -> {
            final JSONObject activities = Config.get().getJSONObject("activities"); // Get all activities

            final JSONArray playing = activities.getJSONArray("playing"); // Get playing texts

            final int random = new Random().nextInt(playing.length()); // Get random number
            final Activity activity = Activity.playing(playing.getString(random)); // Create activity
            jda.getPresence().setActivity(activity);

            RegisteredPacks.register(); // Update all packs
        }, 15, 15, TimeUnit.MINUTES);
    }

    private void checkFolder() throws IOException {
        final File folder = new File(Database.path);
        if (!folder.exists()) { // Check if folder exists
            folder.mkdirs(); // Create folder

            final InputStream membersInputStream = CloudStudios.class.getResourceAsStream("members.json"); // Get member file
            final File membersFile = new File(folder.getPath() + File.separator + "members.json"); // Get new file path
            FileUtils.copyInputStreamToFile(membersInputStream, membersFile); // Copy member file

            final InputStream bansInputStream = CloudStudios.class.getResourceAsStream("bans.json"); // Get bans file
            final File bansFile = new File(folder.getPath() + File.separator + "bans.json"); // Get new file path
            FileUtils.copyInputStreamToFile(bansInputStream, bansFile); // Copy bans file

            final InputStream packsInputStream = CloudStudios.class.getResourceAsStream("packs.json"); // Get packs file
            final File packsFile = new File(folder.getPath() + File.separator + "packs.json"); // Get new file path
            FileUtils.copyInputStreamToFile(packsInputStream, packsFile); // Copy packs file
        }
    }
}