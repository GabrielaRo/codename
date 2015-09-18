/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.chat.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codename.core.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
@Local
@Path("/identity")
public interface LayerIdentityEndpointService extends Serializable {

    

    @POST
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getIdentityToken(@NotNull @FormParam("nonce") String nonce, 
            @NotNull @FormParam("nickname") String nickname) throws ServiceException;

   

}
