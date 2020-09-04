/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhw.excel.utils;

import java.io.File;
import java.util.List;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public interface ExportableConfig<T> {

    public List<T> getValuesList();

    public Object[] getRowObjectExport(T object);

    public String[] getColumnNamesExport();

    public File getFolder();

    public String getFileName();
    
}
