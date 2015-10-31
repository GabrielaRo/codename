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
	public Long createTag(Long tagCreatorId, String tagName, Color color, boolean isPublic) throws ServiceException {
		User tagCreator = usersService.getById(tagCreatorId);
		if (isTagNameFree(tagCreatorId, tagName)) {
			Tag tag = new Tag(tagCreator, tagName, color, isPublic);
			pm.persist(tag);		
			return tag.getId();	
		} else {
			//Add a message that the name already exist?
			return -1L;
		}
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

	@Override
	public void editTag(Long tagId, String tagName, Color color, boolean isPublic) throws ServiceException {
		Tag tag = pm.find(Tag.class, tagId);
		if (tag == null) {
            throw new ServiceException("Tag doesn't exist: " + tagId);
        }
		tag.setTagName(tagName);
		tag.setColor(color);
		tag.setPublic(isPublic);
		pm.merge(tag);
	}

	@Override
	public Tag getTagById(Long tagId) throws ServiceException {
		Tag tag = pm.find(Tag.class, tagId);
		if (tag == null) {
            throw new ServiceException("Tag doesn't exist: " + tagId);
        }
		return tag;
	}

	@Override
	public void editTagName(Long tagId, String tagName) throws ServiceException {
		Tag tag = pm.find(Tag.class, tagId);
		if (tag == null) {
            throw new ServiceException("Tag doesn't exist: " + tagId);
        }
		tag.setTagName(tagName);
		pm.merge(tag);		
	}

	@Override
	public void editTagColor(Long tagId, Color color) throws ServiceException {
		Tag tag = pm.find(Tag.class, tagId);
		if (tag == null) {
            throw new ServiceException("Tag doesn't exist: " + tagId);
        }
		tag.setColor(color);
		pm.merge(tag);
		
	}

	@Override
	public void editTagPublic(Long tagId, boolean isPublic) throws ServiceException {
		Tag tag = pm.find(Tag.class, tagId);
		if (tag == null) {
            throw new ServiceException("Tag doesn't exist: " + tagId);
        }
		tag.setPublic(isPublic);
		pm.merge(tag);		
	}

	@Override
	public List<Tag> getAllPublicTags(boolean isPublic) throws ServiceException {
		Tag tag = pm.find(Tag.class, isPublic);
        return pm.createNamedQuery("Tags.getAllPublic", Tag.class).setParameter("tag", tag.isPublic()).getResultList();
	}

	@Override
	public boolean isTagNameFree(Long creatorId, String tagName) throws ServiceException {
		List <Tag> tagsByUser = getAllTagsCreatedByUser(creatorId);
		for (int i = 0; i < tagsByUser.size(); i++) {
			if (tagsByUser.get(i).equals(tagName)) {
				return false;
			}
		}
		return true;
	}

}
