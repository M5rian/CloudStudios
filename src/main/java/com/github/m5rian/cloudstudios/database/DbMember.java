package com.github.m5rian.cloudstudios.database;

import net.dv8tion.jda.api.entities.Member;
import org.json.JSONArray;
import org.json.JSONObject;

public class DbMember {
    private JSONObject document;
    private long id;
    private int xp;
    private int level;
    private int balance;

    public DbMember(Member member) {
        final JSONArray memberDocuments = Database.get("members"); // Get members from database

        // Search member
        boolean exists = false; // Is the member already in the database?
        for (int i = 0; i < memberDocuments.length(); i++) {
            final JSONObject memberDocument = memberDocuments.getJSONObject(i); // Get current member
            // Member found
            if (memberDocument.getLong("id") == member.getIdLong()) {
                this.document = memberDocument;
                this.id = memberDocument.getLong("id");
                this.xp = memberDocument.getInt("xp");
                this.level = memberDocument.getInt("level");
                this.balance = memberDocument.getInt("balance");
                exists = true; // Member is already in the database
            }
        }

        // Member doesn't exist in database
        if (!exists) {
            final JSONObject memberDocument = new Documents().member(member); // Create new member document
            memberDocuments.put(memberDocument); // Add member to database
            Database.update("members", memberDocuments); // Update database

            this.document = memberDocument;
            this.id = member.getIdLong();
            this.xp = 0;
            this.level = 0;
            this.balance = 0;
        }

    }

    public long getId() {
        return this.id;
    }

    public int getXp() {
        return this.xp;
    }

    public int getLevel() {
        return this.level;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setXp(Integer xp) {
        final JSONArray memberDocuments = Database.get("members"); // Get members from database
        for (int i = 0; i < memberDocuments.length(); i++) {
            final JSONObject memberDocument = memberDocuments.getJSONObject(i); // Get current document
            // Member document found
            if (memberDocument.getLong("id") == this.id) {
                memberDocuments.remove(i); // Remove old document

                this.document.put("xp", xp); // Update value
                memberDocuments.put(this.document); // Update document
                Database.update("members", memberDocuments); // Update database
            }
        }
    }

    public void setLevel(Integer level) {
        final JSONArray memberDocuments = Database.get("members"); // Get members from database
        for (int i = 0; i < memberDocuments.length(); i++) {
            final JSONObject memberDocument = memberDocuments.getJSONObject(i); // Get current document
            // Member document found
            if (memberDocument.getLong("id") == this.id) {
                memberDocuments.remove(i); // Remove old document

                this.document.put("level", level); // Update value
                memberDocuments.put(this.document); // Update document
                Database.update("members", memberDocuments); // Update database
            }
        }
    }

    public void setBalance(Integer balance) {
        final JSONArray memberDocuments = Database.get("members"); // Get members from database
        for (int i = 0; i < memberDocuments.length(); i++) {
            final JSONObject memberDocument = memberDocuments.getJSONObject(i); // Get current document
            // Member document found
            if (memberDocument.getLong("id") == this.id) {
                memberDocuments.remove(i); // Remove old document

                this.document.put("balance", balance); // Update value
                memberDocuments.put(this.document); // Update document
                Database.update("members", memberDocuments); // Update database
            }
        }
    }
}
