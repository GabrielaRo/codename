/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.api;

import java.util.List;
import org.codename.model.Coordinates;
import org.codename.model.User;
import org.codename.services.exceptions.ServiceException;

/**
 *
 * @author salaboy
 */
public interface UsersService {

    Long newUser(User user) throws ServiceException;

    boolean existKey(String serviceKey);

    String getKey(String serviceKey);

    boolean exist(String email);

    boolean exist(Long user_id);

    User getByEmail(String email);

    User getById(Long user_id);

    void update(Long user_id, String username, String lastname,
            String location, String bio, String title) throws ServiceException;

    void updateInterests(Long user_id, List<String> interests) throws ServiceException;

    void updateFirstLogin(Long user_id) throws ServiceException;

    void updateFirstName(Long user_id, String firstname) throws ServiceException;

    void updateLastName(Long user_id, String lastname) throws ServiceException;

    void updateBio(Long user_id, String bio) throws ServiceException;

    void updateLocation(Long user_id, String location) throws ServiceException;

    void updateTitle(Long user_id, String title) throws ServiceException;

    void updateAvatar(Long user_id, String fileName, byte[] content) throws ServiceException;

    void updateCover(Long user_id, String fileName, byte[] content) throws ServiceException;

    byte[] getAvatar(Long user_id) throws ServiceException;

    byte[] getCover(Long user_id) throws ServiceException;

    void removeAvatar(Long user_id) throws ServiceException;

    void removeCover(Long user_id) throws ServiceException;

    List<User> getAll();
    
    List<User> getAllLive();

    void updateLocation(Long user_id, Double lon, Double lat) throws ServiceException;

    Coordinates getLocation(Long user_id) throws ServiceException;

    void updateBothNames(Long user_id, String firstname, String lastname) throws ServiceException;

    void updateOriginallyFrom(Long user_id, String originallyfrom) throws ServiceException;

    void updateLookingFor(Long user_id, List<String> lookingForList) throws ServiceException;

    void updateCategories(Long user_id, List<String> categoriesList) throws ServiceException;

    void updateLongBio(Long user_id, String longbio) throws ServiceException;

    void updateLive(Long user_id, String live) throws ServiceException;

    void updateIams(Long user_id, List<String> iAmsList) throws ServiceException;

    void updateTwitter(Long user_id, String twitter) throws ServiceException;

    void updateWebsite(Long user_id, String webpage) throws ServiceException;

    void updateLinkedin(Long user_id, String linkedin) throws ServiceException;

    void updateBioLongBioIams(Long user_id, String bio, String longbio, List<String> iams) throws ServiceException;

}
