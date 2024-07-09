package com.github.m5rian.cloudstudios.utils.Permissions;


import com.github.m5rian.jdaCommandHandler.Permission;
import org.json.JSONArray;
import com.github.m5rian.cloudstudios.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class Administrator implements Permission {
    @Override
    public String getName() {
        return "administrator";
    }

    @Override
    public List<Long> getRoleIdsLong() {
        final List<Long> roles = new ArrayList<>(); // Create list for all administrator roles
        final JSONArray administrators = Config.get().getJSONObject("roles").getJSONArray("administrator"); // Get all administrator roles in a JSONObject

        for (int i = 0; i < administrators.length(); i++) {
            roles.add(administrators.getLong(i));
        }

        return roles;
    }
}
