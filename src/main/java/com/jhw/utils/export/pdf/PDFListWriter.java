/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhw.utils.export.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.jhw.utils.file.Opener;
import com.jhw.utils.file.PersonalizationFiles;
import static com.jhw.utils.others.SDF.SDF_ALL;
import com.jhw.utils.services.ConverterService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public class PDFListWriter {

    public static final String PDF = "pdf";
    public static final String DEFAULT_FONT_NAME = "Arial";

    public static void write(builder b) throws Exception {

    }

    public static File finalFile(File parent, String file, String extencion) {
        return new File(parent, file + " " + SDF_ALL.format(new Date()) + "." + extencion);
    }

    public static builder builder() {
        return new builder();
    }

    public static class builder {

        private File folder = new File("temp");
        private String fileName = String.valueOf(System.currentTimeMillis());

        private File finalFile;

        public builder config(ExportableConfigPDF config) {
            /*this.folder(config.getFolder())
                    .fileName(config.getFileName())
                    .setColumns(config::getColumnNamesExport)
                    .values(ConverterService.convert(config.getValuesList(), config::getRowObjectExport));

            //personaliza con las cosas que quiera
            config.personalizeBuilder(this);*/
            return this;
        }

        public Opener write() throws Exception {
            //crate Opener with actual date
            Opener op = Opener.from(finalFile(folder, fileName, PDF));

            //set up the final file to export
            finalFile = op.getFile();

            //export
            PDFListWriter.write(this);

            //return opener in case of needed
            return op;
        }

    }
}
