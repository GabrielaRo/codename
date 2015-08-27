/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.user.api;

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
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author grogdj
 */
@Local
@Path("/public/invite")
public interface PublicInviteEndpointService extends Serializable {

    @POST
    @Path("request")
    @Produces({MediaType.APPLICATION_JSON})
    Response requestInvitation(@NotNull @NotEmpty @Email @FormParam("email") String email) throws ServiceException;

}
