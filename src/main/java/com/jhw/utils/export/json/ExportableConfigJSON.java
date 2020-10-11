/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhw.utils.export.json;

import com.jhw.utils.export.utils.ExportableConfig;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public interface ExportableConfigJSON<T> extends ExportableConfig<T> {

    public JSONListWriter.builder exportJSONBuilder();
}
