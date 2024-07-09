package com.github.m5rian.cloudstudios.utils.Permissions;

import com.github.m5rian.jdaCommandHandler.Permission;
import com.github.m5rian.cloudstudios.utils.Config;

import java.util.Collections;
import java.util.List;

public class Owner implements Permission {
    @Override
    public String getName() {
        return "founder";
    }

    @Override
    public List<Long> getRoleIdsLong() {
        return Collections.singletonList(Config.get().getJSONObject("roles").getLong("owner"));
    }
}
