/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.root101.utils.export.json;

import com.jhw.utils.file.Opener;
import com.root101.json.jackson.JACKSON;
import com.root101.utils.services.ConverterService;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public class JSONListWriter {

    public static final String JSON = "json";

    public static void write(builder b) throws Exception {
        JACKSON.write(b.finalFile, b.values);
    }

    public static builder builder() {
        return new builder();
    }

    public static File finalFile(File parent, String file, String extencion) {
        return new File(parent, file + " " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()) + "." + extencion);
    }

    public static class builder {

        private List<Map<String, Object>> values = new ArrayList<>();

        private File folder = new File("temp");
        private String fileName = String.valueOf(System.currentTimeMillis());

        private File finalFile;

        public JSONListWriter.builder folder(File folder) {
            this.folder = folder;
            return this;
        }

        public JSONListWriter.builder folder(String folder) {
            this.folder = new File(folder);
            return this;
        }

        public JSONListWriter.builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public JSONListWriter.builder values(List<Map<String, Object>> values) {
            this.values = values;
            return this;
        }

        public JSONListWriter.builder config(ExportableConfigJSON config) {
            this.folder(config.getFolder())
                    .fileName(config.getFileName())
                    .values(ConverterService.convert(config.getColumnNamesExport(), config.getValuesList(), config::getRowObjectExport));
            return this;
        }

        public Opener write() throws Exception {
            //crate Opener with actual date
            Opener op = Opener.from(finalFile(folder, fileName, JSON));

            //set up the final file to export
            finalFile = op.getFile();

            //export
            JSONListWriter.write(this);

            //return opener in case of needed
            return op;
        }
    }
}
