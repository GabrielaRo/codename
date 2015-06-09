/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.websocket.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import org.codename.model.Notification;

/**
 *
 * @author grogdj
 */
public class NotificationEncoder implements Encoder.Text<Notification> {

    @Override
    public String encode(Notification notification) throws EncodeException {

//        JsonObject jsonObject = Json.createObjectBuilder()
//                .add("notificationId", notification.getId())
//                .add("userId", notification.getUserId())
//                .add("message", notification.getMessage())
//                .add("type", notification.getType()).build();
//        return jsonObject.toString();
        return null;

    }

    @Override
    public void init(EndpointConfig ec) {
        System.out.println("MessageEncoder - init method called");
    }

    @Override
    public void destroy() {
        System.out.println("MessageEncoder - destroy method called");
    }

}
