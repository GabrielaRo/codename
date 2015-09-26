/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.chat.api;

import java.util.List;
import javax.websocket.Session;
import org.codename.core.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
public interface PresenceService {

    void userJoin(String nickname, Session client) throws ServiceException;

    void userLeave(Session client) throws ServiceException;

    boolean isOnline(String nickname);

    List<Boolean> getUsersState(List<String> nicknames);

    void registerInterstInUser(String nickname, List<String> otherUser);
}
