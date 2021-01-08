/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.root101.utils.export.excel;

import com.root101.utils.export.utils.ExportableConfig;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public interface ExportableConfigExcel<T> extends ExportableConfig<T> {

    public ExcelListWriter.builder exportExcelBuilder();

    public void personalizeBuilder(ExcelListWriter.builder builder);
}
