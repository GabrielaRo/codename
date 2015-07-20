/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.util;

/**
 *
 * @author salaboy
 */
public interface UsersExporterImporterService {
    String exportToJson();
    void importFromJson(String json);
}
