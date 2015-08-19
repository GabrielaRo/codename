/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.api;

import java.util.Date;
import java.util.List;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.Coordinates;
import org.codename.model.User;

/**
 *
 * @author grogdj
 */
public interface UsersService {

    public Long newUser(User user) throws ServiceException;

    public boolean existKey(String serviceKey);

    public String getKey(String serviceKey);

    public boolean exist(String email);

    public boolean exist(Long user_id);

    public User getByEmail(String email);
    
    public User getByProviderId(String providerId);
    
    public User getByNickName(String nickname) throws ServiceException;

    public User getById(Long user_id);

    public void updateInterests(Long user_id, List<String> interests) throws ServiceException;

    public void updateFirstLogin(Long user_id) throws ServiceException;

    public void updateFirstName(Long user_id, String firstname) throws ServiceException;

    public void updateLastName(Long user_id, String lastname) throws ServiceException;

    public void updateBio(Long user_id, String bio) throws ServiceException;

    public void updateLocation(Long user_id, String location, Double lon, Double lat) throws ServiceException;

    public void updateAvatar(String nickname, String fileName, byte[] content) throws ServiceException;

    public void updateCover(String nickname, String fileName, byte[] content) throws ServiceException;

    public byte[] getAvatar(String nickname) throws ServiceException;

    public byte[] getCover(String nickname) throws ServiceException;

    public void removeAvatar(String nickname) throws ServiceException;

    public void removeCover(String nickname) throws ServiceException;

    public List<User> getAll();
    
    public List<User> getAllLive();

    public Coordinates getLocation(Long user_id) throws ServiceException;

    public void updateBothNames(Long user_id, String firstname, String lastname) throws ServiceException;

    public void updateOriginallyFrom(Long user_id, String originallyfrom) throws ServiceException;

    public void updateLookingFor(Long user_id, List<String> lookingForList) throws ServiceException;

    public void updateCategories(Long user_id, List<String> categoriesList) throws ServiceException;

    public void updateLongBio(Long user_id, String longbio) throws ServiceException;

    public void updateLive(Long user_id, String live) throws ServiceException;

    public void updateIams(Long user_id, List<String> iAmsList) throws ServiceException;

    public void updateTwitter(Long user_id, String twitter) throws ServiceException;

    public void updateWebsite(Long user_id, String webpage) throws ServiceException;

    public void updateLinkedin(Long user_id, String linkedin) throws ServiceException;

    public void updateBioLongBioIams(Long user_id, String bio, String longbio, List<String> iams) throws ServiceException;

    public void updateNickName(Long user_id, String nickname) throws ServiceException;

    public void updateShare(Long user_id, String share) throws ServiceException;

    public void updateMessageMe(Long user_id, String messageme) throws ServiceException;

    public void updateJobTitle(Long user_id, String jobtitle) throws ServiceException;

    public void removeUser(Long user_id) throws ServiceException;

    public void updateLastLogin(Long user_id, Date date) throws ServiceException;
   
    public int calculateUserProfilePercentage(User u) throws ServiceException;

}
