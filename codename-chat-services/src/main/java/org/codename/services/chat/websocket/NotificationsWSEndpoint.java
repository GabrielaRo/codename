/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.chat.websocket;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.codename.core.exceptions.ServiceException;
import org.codename.services.chat.websocket.decoders.NotificationDecoder;
import org.codename.services.chat.websocket.encoders.NotificationEncoder;
import org.codename.core.chat.api.PresenceService;

/**
 *
 * @author grogdj
 */
@ServerEndpoint(value = "/fhellow",
        encoders = {NotificationEncoder.class},
        decoders = {NotificationDecoder.class}
)
public class NotificationsWSEndpoint {

    @Inject
    PresenceService notificationService;

    @OnOpen
    public void onOpen(Session client) {
        List<String> nicks = client.getRequestParameterMap().get("nickname");

        try {
            System.out.println("User Joining via Presence WS: "+ nicks.get(0));
            notificationService.userJoin(nicks.get(0), client);
        } catch (ServiceException ex) {
            ex.printStackTrace();
            Logger.getLogger(NotificationsWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void onMessage(String message, Session client) {

    }

    @OnClose
    public void onClose(Session client) throws ServiceException {

        notificationService.userLeave(client);
    }

}
