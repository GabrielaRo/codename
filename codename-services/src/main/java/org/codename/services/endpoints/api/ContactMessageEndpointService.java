/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
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
@Path("/contact")
public interface ContactMessageEndpointService extends Serializable {

    @GET
    @Path("replied")
    @Produces({MediaType.APPLICATION_JSON})
    Response getRepliedMessages(@NotNull @PathParam("replied") String replied) throws ServiceException;

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    Response getAll(@NotNull @PathParam("email") String email) throws ServiceException;


}
