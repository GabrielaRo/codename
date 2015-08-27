/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.chat.websocket.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import org.codename.model.chat.Notification;

/**
 *
 * @author grogdj
 */
public class NotificationEncoder implements Encoder.Text<Notification> {

    @Override
    public String encode(Notification notification) throws EncodeException {

        return null;

    }

    @Override
    public void init(EndpointConfig ec) {
        
    }

    @Override
    public void destroy() {
        
    }

}
