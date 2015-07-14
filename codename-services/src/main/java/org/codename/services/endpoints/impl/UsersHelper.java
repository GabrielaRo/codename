/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import org.codename.model.User;

/**
 *
 * @author grogdj
 */
public class UsersHelper {
    public static JsonObjectBuilder createFullJsonUser(User u) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("userId", (u.getId() == null) ? "" : u.getId().toString());
        jsonObjBuilder.add("bio", (u.getBio() == null) ? "" : u.getBio());
        jsonObjBuilder.add("longbio", (u.getLongBio() == null) ? "" : u.getLongBio());
        jsonObjBuilder.add("location", (u.getLocation() == null) ? "" : u.getLocation());
        jsonObjBuilder.add("originallyFrom", (u.getOriginallyFrom() == null) ? "" : u.getOriginallyFrom());
        jsonObjBuilder.add("firstname", (u.getFirstname() == null) ? "" : u.getFirstname());
        jsonObjBuilder.add("lastname", (u.getLastname() == null) ? "" : u.getLastname());
        jsonObjBuilder.add("nickname", (u.getNickname() == null) ? "" : u.getNickname());
        jsonObjBuilder.add("title", (u.getJobTitle() == null) ? "" : u.getJobTitle());
        jsonObjBuilder.add("website", (u.getWebsite() == null) ? "" : u.getWebsite());
        jsonObjBuilder.add("twitter", (u.getTwitter() == null) ? "" : u.getTwitter());
        jsonObjBuilder.add("linkedin", (u.getLinkedin() == null) ? "" : u.getLinkedin());
        jsonObjBuilder.add("advice", (u.getAdviceMessage() == null) ? "" : u.getAdviceMessage());
        jsonObjBuilder.add("hobbies", (u.getHobbiesMessage() == null) ? "" : u.getHobbiesMessage());
        jsonObjBuilder.add("resources", (u.getResourcesMessage() == null) ? "" : u.getResourcesMessage());
        jsonObjBuilder.add("share", (u.getShareMessage() == null) ? "" : u.getShareMessage());
        jsonObjBuilder.add("messageme", (u.getMessageMeMessage() == null) ? "" : u.getMessageMeMessage());
        jsonObjBuilder.add("live", u.isLive());
        jsonObjBuilder.add("hascover", u.getCoverFileName() != null && !u.getCoverFileName().equals(""));
        jsonObjBuilder.add("hasavatar", u.getAvatarFileName() != null && !u.getAvatarFileName().equals(""));
        JsonArrayBuilder interestsJsonArrayBuilder = Json.createArrayBuilder();
        for (String i : u.getInterests()) {
            interestsJsonArrayBuilder.add(i);
        }
        jsonObjBuilder.add("interests", interestsJsonArrayBuilder);
        JsonArrayBuilder lookingForJsonArrayBuilder = Json.createArrayBuilder();
        for (String l : u.getLookingFor()) {
            lookingForJsonArrayBuilder.add(l);
        }
        jsonObjBuilder.add("lookingFor", lookingForJsonArrayBuilder);
        JsonArrayBuilder iAmJsonArrayBuilder = Json.createArrayBuilder();
        for (String i : u.getiAms()) {
            iAmJsonArrayBuilder.add(i);
        }
        jsonObjBuilder.add("iams", iAmJsonArrayBuilder);
        return jsonObjBuilder;
    }
    
    
    public static JsonObjectBuilder createPublicJsonUser(User u) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("userId", (u.getId() == null) ? "" : u.getId().toString());
        jsonObjBuilder.add("bio", (u.getBio() == null) ? "" : u.getBio());
        jsonObjBuilder.add("location", (u.getLocation() == null) ? "" : u.getLocation());
        
        jsonObjBuilder.add("firstname", (u.getFirstname() == null) ? "" : u.getFirstname());
        jsonObjBuilder.add("lastname", (u.getLastname() == null) ? "" : u.getLastname());
        jsonObjBuilder.add("title", (u.getJobTitle() == null) ? "" : u.getJobTitle());
        jsonObjBuilder.add("website", (u.getWebsite() == null) ? "" : u.getWebsite());
        jsonObjBuilder.add("nickname", (u.getNickname() == null) ? "" : u.getNickname());
        jsonObjBuilder.add("messageme", (u.getMessageMeMessage() == null) ? "" : u.getMessageMeMessage());
        jsonObjBuilder.add("advice", (u.getAdviceMessage() == null) ? "" : u.getAdviceMessage());
        jsonObjBuilder.add("hobbies", (u.getHobbiesMessage() == null) ? "" : u.getHobbiesMessage());
        jsonObjBuilder.add("resources", (u.getResourcesMessage() == null) ? "" : u.getResourcesMessage());
        jsonObjBuilder.add("share", (u.getShareMessage() == null) ? "" : u.getShareMessage());
        
        jsonObjBuilder.add("hascover", u.getCoverFileName() != null && !u.getCoverFileName().equals(""));
        jsonObjBuilder.add("hasavatar", u.getAvatarFileName() != null && !u.getAvatarFileName().equals(""));
        JsonArrayBuilder interestsJsonArrayBuilder = Json.createArrayBuilder();
        for (String i : u.getInterests()) {
            interestsJsonArrayBuilder.add(i);
        }
        jsonObjBuilder.add("interests", interestsJsonArrayBuilder);
        JsonArrayBuilder lookingForJsonArrayBuilder = Json.createArrayBuilder();
        for (String l : u.getLookingFor()) {
            lookingForJsonArrayBuilder.add(l);
        }
        jsonObjBuilder.add("lookingFor", lookingForJsonArrayBuilder);
        JsonArrayBuilder iAmJsonArrayBuilder = Json.createArrayBuilder();
        for (String i : u.getiAms()) {
            iAmJsonArrayBuilder.add(i);
        }
        jsonObjBuilder.add("iams", iAmJsonArrayBuilder);
        return jsonObjBuilder;
    }
}
