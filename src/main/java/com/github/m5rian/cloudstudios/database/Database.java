package com.github.m5rian.cloudstudios.database;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Database {

    public static final String path = System.getProperty("user.home") + File.separator + "cloudstudios";

    public static void update(String fileName, JSONArray updatedDocument) {
        try {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path + File.separator + String.format("%s.json", fileName)), StandardCharsets.UTF_8))) {
                writer.write(updatedDocument.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray get(String fileName) {
        String json = null;
        try {
            InputStream input = new FileInputStream(path + File.separator + fileName + ".json");
            json = IOUtils.toString(input, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONArray(json);
    }

    public static void remove(String fileName,Long memberId) {
        final JSONArray memberDocuments = get(fileName); // Get members from database
        for (int i = 0; i < memberDocuments.length(); i++) {
            final JSONObject memberDocument = memberDocuments.getJSONObject(i); // Get current document
            // Member document found
            if (memberDocument.getLong("id") == memberId) {
                memberDocuments.remove(i); // Remove document
                Database.update("members", memberDocuments); // Update database
            }
        }
    }
}