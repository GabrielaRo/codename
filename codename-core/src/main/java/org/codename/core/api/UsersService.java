/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.api;

import java.util.List;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.Coordinates;
import org.codename.model.User;

/**
 *
 * @author grogdj
 */
public interface UsersService {

    Long newUser(User user) throws ServiceException;

    boolean existKey(String serviceKey);

    String getKey(String serviceKey);

    boolean exist(String email);

    boolean exist(Long user_id);

    User getByEmail(String email);
    
    User getByProviderId(String providerId);
    
    User getByNickName(String nickname) throws ServiceException;

    User getById(Long user_id);

    void updateInterests(Long user_id, List<String> interests) throws ServiceException;

    void updateFirstLogin(Long user_id) throws ServiceException;

    void updateFirstName(Long user_id, String firstname) throws ServiceException;

    void updateLastName(Long user_id, String lastname) throws ServiceException;

    void updateBio(Long user_id, String bio) throws ServiceException;

    void updateLocation(Long user_id, String location, Double lon, Double lat) throws ServiceException;

    void updateAvatar(String nickname, String fileName, byte[] content) throws ServiceException;

    void updateCover(String nickname, String fileName, byte[] content) throws ServiceException;

    byte[] getAvatar(String nickname) throws ServiceException;

    byte[] getCover(String nickname) throws ServiceException;

    void removeAvatar(String nickname) throws ServiceException;

    void removeCover(String nickname) throws ServiceException;

    List<User> getAll();
    
    List<User> getAllLive();


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

    void updateNickName(Long user_id, String nickname) throws ServiceException;

    public void updateAdvice(Long user_id, String advice) throws ServiceException;

    public void updateHobbies(Long user_id, String hobbies) throws ServiceException;

    public void updateResources(Long user_id, String resources) throws ServiceException;

    public void updateShare(Long user_id, String share) throws ServiceException;

    public void updateMessageMe(Long user_id, String messageme) throws ServiceException;

    public void updateJobTitle(Long user_id, String jobtitle) throws ServiceException;

    public void removeUser(Long user_id) throws ServiceException;
   


}
