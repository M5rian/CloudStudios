package com.github.m5rian.cloudstudios.utils.Permissions;

import com.github.m5rian.cloudstudios.utils.Config;
import com.github.m5rian.jdaCommandHandler.Permission;
import com.github.m5rian.jdaCommandHandler.commandServices.IPermissionService;

public class Developer implements Permission {
    @Override
    public String getName() {
        return "Developer";
    }

    @Override
    public String getUserId() {
        return String.valueOf(Config.get().getJSONObject("roles").getLong("developer"));
    }
}
