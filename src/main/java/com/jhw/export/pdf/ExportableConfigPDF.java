/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhw.export.pdf;

import com.jhw.export.utils.ExportableConfig;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public interface ExportableConfigPDF<T> extends ExportableConfig<T> {

    public PDFListWriter.builder exportExcelBuilder();

    public void personalizeBuilder(PDFListWriter.builder builder);
}
