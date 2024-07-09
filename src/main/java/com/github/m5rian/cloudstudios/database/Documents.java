package com.github.m5rian.cloudstudios.database;

import net.dv8tion.jda.api.entities.Member;
import org.json.JSONObject;

public class Documents {
    public JSONObject member(Member member) {
        return new JSONObject()
                .put("id", member.getIdLong())
                .put("xp", 0)
                .put("level", 0)
                .put("balance", 0);
    }

    public JSONObject ban(Member member, String banCase, long unbanTime) {
        return new JSONObject()
                .put("id", member.getIdLong())
                .put("banCase", banCase)
                .put("unbanTime", unbanTime);
    }
}
