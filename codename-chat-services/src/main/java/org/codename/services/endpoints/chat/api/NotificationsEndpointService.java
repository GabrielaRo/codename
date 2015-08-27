/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.chat.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codename.core.exceptions.ServiceException;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author grogdj
 */
@Local
@Path("/notifications")
public interface NotificationsEndpointService extends Serializable {

    @Path("/new")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    Response newNotification(@NotNull @FormParam("user_id") Long userId,
            @NotNull @NotEmpty @FormParam("message") String message,
            @NotNull @NotEmpty @FormParam("action") String action,
            @NotNull @NotEmpty @FormParam("type") String type) throws ServiceException;

    @GET
    @Path("/user/{id}")
    @Produces({"application/json"})
    Response getAllNotificationsByUser(@PathParam("id") Long userId) throws ServiceException;

}
