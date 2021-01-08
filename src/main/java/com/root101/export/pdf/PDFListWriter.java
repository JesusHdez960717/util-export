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
package com.root101.export.pdf;

import java.io.File;
import com.jhw.utils.file.Opener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Root101 (jhernandezb96@gmail.com, +53-5-426-8660)
 * @author JesusHdezWaterloo@Github
 */
public class PDFListWriter {

    public static final String PDF = "pdf";
    public static final String DEFAULT_FONT_NAME = "Arial";

    public static void write(builder b) throws Exception {

    }

    public static File finalFile(File parent, String file, String extencion) {
        return new File(parent, file + " " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()) + "." + extencion);
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
