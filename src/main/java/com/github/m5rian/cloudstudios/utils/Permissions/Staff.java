package com.github.m5rian.cloudstudios.utils.Permissions;

import com.github.m5rian.cloudstudios.utils.Config;
import com.github.m5rian.jdaCommandHandler.Permission;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Staff implements Permission {
    @Override
    public String getName() {
        return "Staff";
    }

    @Override
    public List<Long> getRoleIdsLong() {
        final List<Long> roles = new ArrayList<>(); // Create list for all staff roles
        final JSONArray staffs = Config.get().getJSONObject("roles").getJSONArray("staff"); // Get all staff roles
        for (int i = 0; i < staffs.length(); i++) roles.add(staffs.getLong(i));
        return roles;
    }
}
