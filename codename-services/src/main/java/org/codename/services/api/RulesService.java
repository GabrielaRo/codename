/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.api;

/**
 *
 * @author grogdj
 */
public interface RulesService {
    int insert(Object o);
    void retract(Object o);
    int update(Object o);
    void reset();
}
