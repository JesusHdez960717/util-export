/*
 * Copyright 2021 Root101 (jhernandezb96@gmail.com, +53-5-426-8660).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Or read it directly from LICENCE.txt file at the root of this project.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.root101.export.json;

import com.root101.utils.file.Opener;
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
 * @author Root101 (jhernandezb96@gmail.com, +53-5-426-8660)
 * @author JesusHdezWaterloo@Github
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
