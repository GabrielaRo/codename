/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.codename.core.api.UsersService;
import org.codename.model.Notification;
import org.codename.services.endpoints.api.NotificationsEndpointService;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.User;
import org.hibernate.validator.constraints.NotEmpty;

@Stateless
public class NotificationsServiceImpl implements NotificationsEndpointService {
    
    @Inject
    private UsersService userService;
    
    private Map<Long, List<Notification>> inMemoryNotifications = new HashMap<Long, List<Notification>>();

    @Override
    public Response getAllNotificationsByUser(@PathParam("id") Long userId) throws ServiceException {
        List<Notification> allNotifications = inMemoryNotifications.get(userId);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        for (Notification n : allNotifications) {
            jsonObjectBuilder
                    .add("id", (n.getId() == null) ? "" : n.getId().toString())
                    .add("message", (n.getMessage() == null) ? "" : n.getMessage())
                    .add("action", (n.getAction() == null) ? "" : n.getAction())
                    .add("type", (n.getType() == null) ? "" : n.getType().toString())
                    .add("date", (n.getNotificationDate() == null) ? "" : n.getNotificationDate().toString());

            jsonArrayBuilder.add(jsonObjectBuilder);
        }
        JsonArray jsonArray = jsonArrayBuilder.build();
        return Response.ok(jsonArray.toString()).build();

    }

    @Override
    public Response newNotification(@NotNull @FormParam("user_id") Long userId,
            @NotNull @NotEmpty @FormParam("message") String message,
            @NotNull @NotEmpty @FormParam("action") String action,
            @NotNull @NotEmpty @FormParam("type") String type) throws ServiceException {

        
        if(inMemoryNotifications.get(userId) == null){
            inMemoryNotifications.put(userId, new ArrayList<Notification>());
        }
        User user = userService.getById(userId);
        Notification notification = new Notification(user, message, action, Notification.NotificationType.valueOf(type));
        inMemoryNotifications.get(userId).add(notification);
        return Response.ok(notification.getId()).build();

    }

}
