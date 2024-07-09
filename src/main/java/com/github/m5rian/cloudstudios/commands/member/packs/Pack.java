package com.github.m5rian.cloudstudios.commands.member.packs;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pack {
    private final String download;
    private final String name;
    private final String[] tags;

    public Pack(String download, String name, JSONArray tags) {
        this.download = download;
        this.name = name;

        List<String> keywords = new ArrayList();
        for (int i = 0; i < tags.length(); i++) keywords.add(tags.getString(i));
        this.tags = keywords.toArray(new String[keywords.size()]);
    }

    public List<String> getTags() {
        final List<String> allTags = new ArrayList<>();
        allTags.add(name);
        allTags.addAll(Arrays.asList(tags));
        return allTags;
    }

    public String getName() {
        return this.name;
    }

    public String getDownloadLink() {
        return this.download;
    }
}
