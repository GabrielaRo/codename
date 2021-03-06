/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.chat.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.codename.core.exceptions.ServiceException;
import javax.websocket.Session;

import org.codename.core.chat.api.PresenceService;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class PresenceServiceImpl implements PresenceService {

    private Map<String, Session> nickToSessionMap = new HashMap<String, Session>();
    private Map<Session, String> sessionToNickMap = new HashMap<Session, String>();
    private Map<String, List<String>> usersInterest = new HashMap<String, List<String>>();

    @Override
    public void userJoin(final String nickname, Session client) throws ServiceException {
        statusUpdate(nickname, true);
        nickToSessionMap.put(nickname, client);
        sessionToNickMap.put(client, nickname);
    }

    @Override
    public void userLeave(Session client) throws ServiceException {
        String nickname = sessionToNickMap.get(client);
        if (nickname == null) {
            return;
        }
        statusUpdate(nickname, false);
        sessionToNickMap.remove(client);
        nickToSessionMap.remove(nickname);
        if (usersInterest.get(nickname) != null) {
            usersInterest.get(nickname).clear();
        }
    }

    @Override
    public void registerInterstInUser(String nickname, List<String> otherUsers) {
        if (usersInterest.get(nickname) == null) {
            usersInterest.put(nickname, new ArrayList<String>());
        }
        usersInterest.get(nickname).addAll(otherUsers);
    }



    @Override
    public boolean isOnline(String nickname) {
        return nickToSessionMap.keySet().contains(nickname);
    }

    @Override
    public List<Boolean> getUsersState(List<String> nicknames) {

        List<Boolean> precenses = new ArrayList<Boolean>();
        for (String n : nicknames) {
            precenses.add(nickToSessionMap.keySet().contains(n));
        }
        return precenses;
    }

    private void statusUpdate(String userNickname, boolean online) throws ServiceException {

        List<String> userInterested = new ArrayList<String>();
        for (String user : usersInterest.keySet()) {

            if (usersInterest.get(user).contains(userNickname)) {

                userInterested.add(user);
            }
        }

        if (userInterested != null && !userInterested.isEmpty()) {

            JsonObjectBuilder jsonUserObjectBuilder = Json.createObjectBuilder();
            if (online) {
                jsonUserObjectBuilder.add("type", "online");
            } else {
                jsonUserObjectBuilder.add("type", "offline");
            }
            jsonUserObjectBuilder.add("user", userNickname);

            for (String sendTo : userInterested) {
                if (nickToSessionMap.keySet().contains(sendTo)) {
                    try {
                        nickToSessionMap.get(sendTo).getBasicRemote().sendText(jsonUserObjectBuilder.build().toString());
                    } catch (IOException ex) {

                        ex.printStackTrace();
                    }

                }

            }
        }
    }

}
