/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.user.api;

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

/**
 *
 * @author grogdj
 */
@Local
@Path("/public/contact")
public interface PublicContactMessageEndpointService extends Serializable {

    @Path("message")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    Response sendContactMessage(@NotNull @FormParam("email") String email, @NotNull @FormParam("name") String name, @NotNull @FormParam("subject") String subject, @NotNull @FormParam("text") String text, @NotNull @FormParam("type") String type) throws ServiceException;

}
