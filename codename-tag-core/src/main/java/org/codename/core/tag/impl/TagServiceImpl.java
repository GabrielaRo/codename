/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tag.impl;

import java.awt.Color;
import java.util.List;

import javax.inject.Inject;

import org.codename.core.exceptions.ServiceException;
import org.codename.core.tag.api.TagService;
import org.codename.core.user.api.UsersService;
import org.codename.core.util.PersistenceManager;
import org.codename.model.tag.Tag;
import org.codename.model.user.User;

/**
 *
 * @author gabriela
 */
public class TagServiceImpl implements TagService {

	@Inject
    private PersistenceManager pm;
	
	@Inject
    private UsersService usersService;
	
	@Override
	public Long createTag(Long tagCreatorId, String tagName, Color color, boolean isPublic) {
		User tagCreator = usersService.getById(tagCreatorId);
		Tag tag = new Tag(tagCreator, tagName, color, isPublic);
		pm.persist(tag);		
		return tag.getId();
	}
	
	@Override
	public List<Tag> getAllTagsCreatedByUser(Long creatorId) {
		User user = usersService.getById(creatorId);
        return pm.createNamedQuery("Tags.getByUser", Tag.class).setParameter("user", user.getId()).getResultList();
	}
	

	
	@Override
	public void deleteTag(Long tagId) throws ServiceException {
		Tag tag = pm.find(Tag.class, tagId);
        if (tag == null) {
            throw new ServiceException("Tag doesn't exist: " + tagId);
        }
        pm.remove(tag);
	}

}
