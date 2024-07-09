package com.github.m5rian.cloudstudios.commands.member.packs;

import com.github.m5rian.cloudstudios.database.Database;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RegisteredPacks {
    private static final HashMap<List<String>, Pack> registeredPacks = new HashMap<>();

    public static void register() {
        registeredPacks.clear(); // Clear current registered packs
        final JSONArray packs = Database.get("packs");
        for (int i = 0; i < packs.length(); i++) {
            JSONObject pack = packs.getJSONObject(i);

            JSONArray tags = pack.getJSONArray("tags");
            List<String> keywords = new ArrayList();
            for (int tag = 0; tag < tags.length(); tag++) keywords.add(tags.getString(tag));

            Pack generatedPack = new Pack(pack.getString("download"), pack.getString("name"), pack.getJSONArray("tags"));
            registeredPacks.put(keywords, generatedPack);
        }
    }

    public static Pack get(int number) {
        Collection<Pack> values = registeredPacks.values();
        ArrayList<Pack> packs = new ArrayList<>(values);
        if (packs.size() <= number) return null;
        else return packs.get(number);
    }

    public static List<Pack> search(String keyword) {
        final List<Pack> searchResult = new ArrayList<>();

        for (List<String> tags : registeredPacks.keySet()) {

            if (tags.stream().anyMatch(k -> k.toLowerCase().contains(keyword.toLowerCase()))) {
                searchResult.add(registeredPacks.get(tags));
            }
        }

        return searchResult;
    }
}
