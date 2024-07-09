package com.github.m5rian.cloudstudios.listeners;

import com.github.m5rian.cloudstudios.database.Database;
import com.github.m5rian.cloudstudios.utils.Config;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.json.JSONArray;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Random;

public class Welcome {
    private final long welcomeChannelId = Config.get().getJSONObject("channels").getLong("welcome");
    private final long rulesChannelId = Config.get().getJSONObject("channels").getLong("rules");

    private final String[] messages = {
            "Welcome [member]! Enjoy your stay.",
            "[member] popped onto the server, welcome to [server]!",
            "Whoosh, [member] just landed! Anyone wants to help him?",
            "Hey [member], you don't know what to do first? We suggest taking a look at [rules]! *These are important*",
            "The true and only [member] appeared!"
    };

    public void onJoin(GuildMemberJoinEvent event) throws IOException {
        final String member = event.getMember().getAsMention();
        final String server = event.getGuild().getName();
        final String rules = event.getGuild().getTextChannelById(rulesChannelId).getAsMention();

        final int random = new Random().nextInt(messages.length); // Get random number
        final String message = messages[random]
                .replace("[member]", member)
                .replace("[server]", server)
                .replace("[rules]", rules);

        BufferedImage image = render(event.getUser().getEffectiveAvatarUrl());
        InputStream inputStream = toInputStream(image);

        final TextChannel welcomeChannel = event.getGuild().getTextChannelById(welcomeChannelId); // Get welcome channel
        welcomeChannel.sendMessage(message).addFile(inputStream, String.format("welcome_%s.png", event.getUser().getId())).queue();
    }

    public void assignAutoRole(GuildMemberJoinEvent event) {
        final JSONArray roles = Config.get().getJSONObject("roles").getJSONArray("autorole"); // Get auto roles
        for (int i = 0; i < roles.length(); i++) {
            final Role role = event.getGuild().getRoleById(roles.getLong(i)); // Get role
            event.getGuild().addRoleToMember(event.getMember(), role).queue(); // Add role to member
        }
    }

    private BufferedImage render(String avatarUrl) throws IOException {
        final BufferedImage welcome = ImageIO.read(new File(Database.path + File.separator + "welcome.png")); // Get background
        BufferedImage avatar = getAvatar(avatarUrl); // Get avatar
        avatar = resizeImage(avatar, 175, 175);

        Graphics g = welcome.getGraphics(); // Get graphics object
        g.drawImage(avatar, imageCenter('x', avatar, welcome), imageCenter('y', avatar, welcome), null); // Draw avatar

        return welcome;
    }

    private BufferedImage getAvatar(String avatarUrl) throws IOException {
        // Read image from URL
        BufferedImage avatar = ImageIO.read(new URL(avatarUrl));

        int diameter = Math.min(avatar.getWidth(), avatar.getHeight());
        BufferedImage mask = new BufferedImage(avatar.getWidth(), avatar.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        //applyQualityRenderingHints(g2d);
        g2d.fillOval(0, 0, diameter - 1, diameter - 1);
        g2d.dispose();

        BufferedImage result;
        result = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = result.createGraphics();
        //applyQualityRenderingHints(g2d);
        int x = (diameter - avatar.getWidth()) / 2;
        int y = (diameter - avatar.getHeight()) / 2;
        g2d.drawImage(avatar, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();
        // Return Avatar
        return result;
    }

    private BufferedImage resizeImage(BufferedImage image, Integer x, Integer y) {
        Image temp = image.getScaledInstance(x, y, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    private static int imageCenter(char axis, BufferedImage image, BufferedImage background) {
        int center = 0;
        //x
        if (axis == 'x') {
            int xCenterText = Math.round(image.getWidth() / 2);
            int xCenterBackground = background.getWidth() / 2;
            center = xCenterBackground - xCenterText;
        }
        //y
        else if (axis == 'y') {
            int yCenterText = Math.round(image.getHeight() / 2);
            int yCenterBackground = background.getHeight() / 2;
            center = yCenterBackground - yCenterText;
        }
        return center;
    }

    private InputStream toInputStream(BufferedImage image) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.flush();
        outStream.close();
        ImageIO.write(image, "png", outStream);
        return new ByteArrayInputStream(outStream.toByteArray());
    }
}
