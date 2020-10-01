/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhw.export.utils;

import com.clean.core.domain.DomainObject;
import java.io.File;
import java.util.List;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 * @param <Domain>
 */
public interface DomainListFileReader<Domain extends DomainObject> {

    public List<Domain> read(List<File> files) throws Exception;
}
