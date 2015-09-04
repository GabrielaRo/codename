
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.user.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;
import org.codename.core.user.api.UsersQueryService;

import org.codename.model.user.User;

import org.codename.core.user.api.UsersService;

import org.codename.services.endpoints.user.api.UserEndpointService;
import static org.codename.services.endpoints.user.impl.UsersHelper.createListJsonUser;
import org.codename.core.exceptions.ServiceException;
import static org.codename.services.endpoints.user.impl.UsersHelper.createFullJsonUser;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author grogdj
 */
@Stateless
public class UserEndpointServiceImpl implements UserEndpointService {

    @Inject
    private UsersService usersService;

    @Inject
    private UsersQueryService usersQueryService;


    private final static Logger log = Logger.getLogger(UserEndpointServiceImpl.class.getName());

    private final String UPLOADED_FILE_PARAMETER_NAME = "file";

    public UserEndpointServiceImpl() {

    }

    @Override
    public Response get(@PathParam("id") Long user_id) throws ServiceException {
        User u = usersService.getById(user_id);
        if (u == null) {
            throw new ServiceException("User " + user_id + " doesn't exists");
        }
        JsonObjectBuilder jsonUserObjectBuilder = createFullJsonUser(u);
        JsonObject jsonObj = jsonUserObjectBuilder.build();
        return Response.ok(jsonObj.toString()).build();
    }

    @Override
    public Response getAll() throws ServiceException {
        List<User> users = usersService.getAllLive();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (User u : users) {
            JsonObjectBuilder jsonUserObjectBuilder = createListJsonUser(u);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response search(Double lon, Double lat, String interests, String lookingFors, String iAms, String range, Integer offset, Integer limit, String excludes) throws ServiceException {
        System.out.println("Searching (Range: " + range + ") from " + offset + " to " + limit);
        List<String> interestsList = null;

        if (interests != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(interests.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                interestsList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    log.info("Interest[" + i + "]: " + array.getString(i));

                    interestsList.add(array.getString(i));
                }

            }
        }
        List<String> aImList = null;
        if (iAms != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(iAms.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                aImList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    log.info("Iam[" + i + "]: " + array.getString(i));

                    aImList.add(array.getString(i));
                }

            }
        }
        List<String> lookingForList = null;
        if (lookingFors != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(lookingFors.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                lookingForList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    log.info("Looking For[" + i + "]: " + array.getString(i));

                    lookingForList.add(array.getString(i));
                }

            }
        }

        List<String> excludesList = null;
        if (excludes != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(excludes.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                excludesList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    log.info("Excludes[" + i + "]: " + array.getString(i));

                    excludesList.add(array.getString(i));
                }

            }
        }

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        if (lon == 0.0 && lat == 0.0) {
            List<User> usersInRange = usersQueryService.search(lon, lat, DistanceRange._ALL.getOffsetRange(),
                    DistanceRange._ALL.getLimitRange(), interestsList, lookingForList, aImList, offset, limit, excludesList);
            //Enable for debug only
//            usersQueryService.searchWithScore(lon, lat, DistanceRange._ALL.getOffsetRange(),
//                    DistanceRange._ALL.getLimitRange(), interestsList, lookingForList, aImList, offset, limit, excludesList);
            int i = offset;
            for (User u : usersInRange) {

                JsonObjectBuilder jsonUserObjectBuilder = createListJsonUser(u);
                jsonUserObjectBuilder.add("range", DistanceRange._ALL.getDescription());
                jsonUserObjectBuilder.add("rangeCode", DistanceRange._ALL.name());
                jsonUserObjectBuilder.add("offset", i);
//                jsonUserObjectBuilder.add("onlineStatus", notificationServices.isOnline(u.getNickname()));
                jsonArrayBuilder.add(jsonUserObjectBuilder);
                i++;

            }
        } else {

            int missing = limit;
            DistanceRange incomingRange = null;
            try {
                incomingRange = DistanceRange.valueOf(range);
            } catch (IllegalArgumentException iae) {
            }
            boolean firstRange = true;
            System.out.println("Incoming range is: " + range + " and the offset is:" + offset);
            if (incomingRange == null) {
                for (DistanceRange r : DistanceRange.values()) {
                    System.out.println(">> ANalizing Quering range:  " + r.getDescription());
                    if (!r.equals(DistanceRange._ALL)) {
                        int i = 0;
                        if (firstRange) {
                            i = offset;
                            firstRange = false;
                        }
                        System.out.println(">> Executing Query range:  " + r.getDescription());
                        List<User> usersInRange = usersQueryService.search(lon, lat, r.getOffsetRange(),
                                r.getLimitRange(), interestsList, lookingForList, aImList, i, limit, excludesList);
                        //Enable for debug only
//                        usersQueryService.searchWithScore(lon, lat, r.getOffsetRange(),
//                                r.getLimitRange(), interestsList, lookingForList, aImList, i, limit, excludesList);
                        for (User u : usersInRange) {

                            JsonObjectBuilder jsonUserObjectBuilder = createListJsonUser(u);
                            jsonUserObjectBuilder.add("range", r.getDescription());
                            jsonUserObjectBuilder.add("rangeCode", r.name());
                            jsonUserObjectBuilder.add("offset", i);
//                            jsonUserObjectBuilder.add("onlineStatus", notificationServices.isOnline(u.getNickname()));
                            jsonArrayBuilder.add(jsonUserObjectBuilder);
                            missing = missing - 1;
                            i++;

                        }

                        System.out.println("Missing : " + missing);
                        if (missing <= 0) {
                            break;
                        } else {
                            limit = missing;
                            System.out.println("Limit now : " + missing);
                        }
                    }

                }
            } else {
                boolean enabled = false;
                for (DistanceRange r : DistanceRange.values()) {
                    System.out.println(">> ANalizing Quering range:  " + r.getDescription());
                    if (r.equals(incomingRange) || enabled) {

                        enabled = true;
                        if (!r.equals(DistanceRange._ALL)) {
                            int i = 0;
                            if (firstRange) {
                                i = offset;
                                firstRange = false;
                            }
                            System.out.println(">> Executing Query range:  " + r.getDescription());
                            List<User> usersInRange = usersQueryService.search(lon, lat, r.getOffsetRange(),
                                    r.getLimitRange(), interestsList, lookingForList, aImList, i, limit, excludesList);
                            //Enable for debug only
//                            usersQueryService.searchWithScore(lon, lat, r.getOffsetRange(),
//                                    r.getLimitRange(), interestsList, lookingForList, aImList, i, limit, excludesList);
                            for (User u : usersInRange) {
                                JsonObjectBuilder jsonUserObjectBuilder = createListJsonUser(u);
                                jsonUserObjectBuilder.add("range", r.getDescription());
                                jsonUserObjectBuilder.add("rangeCode", r.name());
                                jsonUserObjectBuilder.add("offset", i);
//                                jsonUserObjectBuilder.add("onlineStatus", notificationServices.isOnline(u.getNickname()));
                                jsonArrayBuilder.add(jsonUserObjectBuilder);
                                missing = missing - 1;
                                i++;
                            }

                            System.out.println("Missing : " + missing);
                            if (missing <= 0) {
                                break;
                            } else {
                                limit = missing;
                                System.out.println("Limit Now : " + missing);
                            }
                        }
                    }

                }
            }

        }

        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response exist(@NotNull @FormParam("user_id") Long user_id) throws ServiceException {
        return Response.ok(usersService.exist(user_id)).build();
    }

    @Override
    public Response updateInterests(@NotNull @PathParam("user_id") Long user_id, @FormParam("interests") String interests) throws ServiceException {
        log.info("Storing from the database: (" + user_id + ") " + interests);
        if (interests != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(interests.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            List<String> interestsList = new ArrayList<String>(array.size());

            if (array != null) {

                for (int i = 0; i < array.size(); i++) {
                    log.info("Interest[" + i + "]: " + array.getString(i));

                    interestsList.add(array.getString(i));
                }

            }

            usersService.updateInterests(user_id, interestsList);
        }

        return Response.ok().build();
    }

    @Override
    public Response uploadAvatar(@NotNull @PathParam("String") String nickname, MultipartFormDataInput input) throws ServiceException {
        log.info(">>>> sit back - starting file upload for user_id..." + nickname);

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);

        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            String filename = getFileName(headers);

            try {
                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                byte[] bytes = IOUtils.toByteArray(inputStream);

                log.log(Level.INFO, ">>> File '''{'{0}'}''' has been read, size: #'{'{1}'}' bytes", new Object[]{filename, bytes.length});
                usersService.updateAvatar(nickname, filename, bytes);

            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }
        return Response.ok().build();
    }

    @Override
    public Response uploadCover(@NotNull @PathParam("nickname") String nickname, MultipartFormDataInput input) throws ServiceException {
        log.info(">>>> sit back - starting file upload for user_id..." + nickname);

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get(UPLOADED_FILE_PARAMETER_NAME);

        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            String filename = getFileName(headers);

            try {
                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                byte[] bytes = IOUtils.toByteArray(inputStream);

                log.log(Level.INFO, ">>> File '''{'{0}'}''' has been read, size: #'{'{1}'}' bytes", new Object[]{filename, bytes.length});
                usersService.updateCover(nickname, filename, bytes);

            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }
        return Response.ok().build();
    }

    @Override
    public Response removeAvatar(@NotNull @PathParam("nickname") String nickname) throws ServiceException {
        usersService.removeAvatar(nickname);
        return Response.ok().build();
    }

    @Override
    public Response removeCover(@NotNull @PathParam("nickname") String nickname) throws ServiceException {
        usersService.removeCover(nickname);
        return Response.ok().build();
    }

    @Override
    public Response getAvatar(@NotNull @PathParam("nickname") String nickname) throws ServiceException {
        final byte[] avatar = usersService.getAvatar(nickname);
        return Response.ok().entity(new StreamingOutput() {
            @Override
            public void write(OutputStream output)
                    throws IOException, WebApplicationException {
                output.write(avatar);
                output.flush();
            }
        }).build();
    }

    /**
     * Extract filename from HTTP heaeders.
     *
     * @param headers
     * @return
     */
    private String getFileName(MultivaluedMap<String, String> headers) {
        String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = sanitizeFilename(name[1]);
                return finalFileName;
            }
        }
        return "unknown";
    }

    private String sanitizeFilename(String s) {
        return s.trim().replaceAll("\"", "");
    }

    @Override
    public Response updateFirstLogin(Long user_id) throws ServiceException {
        usersService.updateFirstLogin(user_id);
        return Response.ok().build();
    }

    @Override
    public Response updateFirstName(Long user_id, String firstname) throws ServiceException {
        usersService.updateFirstName(user_id, firstname);
        return Response.ok().build();
    }

    @Override
    public Response updateNickName(Long user_id, String nickname) throws ServiceException {
        usersService.updateNickName(user_id, nickname);
        return Response.ok().build();
    }

    @Override
    public Response updateLastName(Long user_id, String lastname) throws ServiceException {
        usersService.updateLastName(user_id, lastname);
        return Response.ok().build();
    }

    @Override
    public Response updateLocation(Long user_id, String location, Double lon, Double lat) throws ServiceException {
        usersService.updateLocation(user_id, location, lon, lat);
        return Response.ok().build();
    }

    @Override
    public Response updateBio(Long user_id, String bio) throws ServiceException {
        usersService.updateBio(user_id, bio);
        return Response.ok().build();
    }

    @Override
    public Response updateBothNames(Long user_id, String firstname, String lastname) throws ServiceException {
        usersService.updateBothNames(user_id, firstname, lastname);
        return Response.ok().build();
    }

    @Override
    public Response updateBioLongBioIams(Long user_id, String bio, String longbio, String iams) throws ServiceException {
        List<String> iAmsList = null;
        if (iams != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(iams.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                iAmsList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    log.info("I am [" + i + "]: " + array.getString(i));
                    iAmsList.add(array.getString(i));
                }

            }

        }
        usersService.updateBioLongBioIams(user_id, bio, longbio, iAmsList);
        return Response.ok().build();
    }

    @Override
    public Response updateOriginallyFrom(Long user_id, String originallyfrom) throws ServiceException {
        usersService.updateOriginallyFrom(user_id, originallyfrom);
        return Response.ok().build();

    }

    @Override
    public Response updateIams(Long user_id, String iams) throws ServiceException {
        if (iams != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(iams.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                List<String> iAmsList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {

                    iAmsList.add(array.getString(i));
                }
                usersService.updateIams(user_id, iAmsList);
            }

        }
        return Response.ok().build();
    }

    @Override
    public Response updateLookingForAndIams(Long user_id, String lookingfor, String iams) throws ServiceException {

        if (lookingfor != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(lookingfor.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                List<String> lookingForList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {

                    lookingForList.add(array.getString(i));
                }
                usersService.updateLookingFor(user_id, lookingForList);
            }

        }
        if (iams != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(iams.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                List<String> iAmsList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {

                    iAmsList.add(array.getString(i));
                }
                usersService.updateIams(user_id, iAmsList);
            }

        }
        return Response.ok().build();
    }

    @Override
    public Response updateLongBio(Long user_id, String longbio) throws ServiceException {
        usersService.updateLongBio(user_id, longbio);
        return Response.ok().build();
    }

    @Override
    public Response updateLive(Long user_id, String live) throws ServiceException {
        usersService.updateLive(user_id, live);
        return Response.ok().build();
    }

    @Override
    public Response updateTwitter(Long user_id, String twitter) throws ServiceException {
        usersService.updateTwitter(user_id, twitter);
        return Response.ok().build();
    }

    @Override
    public Response updateWebsite(Long user_id, String website) throws ServiceException {
        usersService.updateWebsite(user_id, website);
        return Response.ok().build();
    }

    @Override
    public Response updateLinkedin(Long user_id, String linkedin) throws ServiceException {
        usersService.updateLinkedin(user_id, linkedin);
        return Response.ok().build();
    }

    @Override
    public Response updateShare(Long user_id, String share) throws ServiceException {
        usersService.updateShare(user_id, share);
        return Response.ok().build();
    }

    @Override
    public Response updateMessageMe(Long user_id, String messageme) throws ServiceException {
        usersService.updateMessageMe(user_id, messageme);
        return Response.ok().build();
    }

    @Override
    public Response updateJobTitle(Long user_id, String jobtitle) throws ServiceException {
        usersService.updateJobTitle(user_id, jobtitle);
        return Response.ok().build();
    }

    @Override
    public Response updatePassword(Long user_id, String oldPassword, String newPassword) throws ServiceException {
        usersService.updatePassword(user_id, oldPassword, newPassword);
        return Response.ok().build();
    }
    
    

}
