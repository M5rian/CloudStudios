package com.github.m5rian.cloudstudios.utils;

import com.github.m5rian.cloudstudios.database.Database;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    public final static Color colour = Color.decode("#" + Config.get().getString("colour"));

    public static JSONObject get() {
        final Path path = Paths.get(Database.path + File.separator + "config.json");
        final StringBuilder output = new StringBuilder();

        try {
            Files.lines(path).forEachOrdered(line -> output.append(line));
        }catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject(output.toString());
        return json;
    }

    public static void update(JSONObject json) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Database.path + File.separator + "config.json"), StandardCharsets.UTF_8))) {
            writer.write(json.toString());
        }
    }
}