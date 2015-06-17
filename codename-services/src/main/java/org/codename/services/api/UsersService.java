/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.api;

import java.util.List;
import org.codename.model.Coordinates;
import org.codename.model.Interest;
import org.codename.model.User;
import org.codename.services.exceptions.ServiceException;

/**
 *
 * @author salaboy
 */
public interface UsersService {

    public Long newUser(User user) throws ServiceException;

    public boolean existKey(String serviceKey);

    public String getKey(String serviceKey);

    public boolean exist(String email) ;
    
    public boolean exist(Long user_id) ;

    public User getByEmail(String email);
    
    public User getById(Long user_id);
    
    void update(Long user_id, String username,  String lastname,
            String location, String bio, String title) throws ServiceException;
    
    void setInterests(Long user_id, List<Interest> interests) throws ServiceException;
    
    List<Interest> getInterests(Long user_id) throws ServiceException;
    
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
    
    void updateLocation(Long user_id, Double lon, Double lat) throws ServiceException;
    
    Coordinates getLocation(Long user_id) throws ServiceException;
    
}
