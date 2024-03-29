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
package com.root101.export.utils;

import com.root101.export.excel.ExcelListWriter;
import com.root101.export.json.JSONListWriter;

/**
 *
 * @author Root101 (jhernandezb96@gmail.com, +53-5-426-8660)
 */
public abstract class SimpleExportableConfig<T> implements ExportableFull<T> {

    //-----------------<EXCEL>-----------------
    @Override
    public ExcelListWriter.builder exportExcelBuilder() {
        return ExcelListWriter.builder().config(this);
    }

    @Override
    public void personalizeBuilder(ExcelListWriter.builder bldr) {
    }
    //-----------------</EXCEL>-----------------

    //-----------------<JSON>-----------------
    @Override
    public JSONListWriter.builder exportJSONBuilder() {
        return new JSONListWriter.builder().config(this);
    }
    //-----------------</JSON>-----------------
}
