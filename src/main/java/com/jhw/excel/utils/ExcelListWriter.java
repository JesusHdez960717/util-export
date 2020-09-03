/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhw.excel.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.jhw.utils.others.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public class ExcelListWriter {

    public static final String XLSX = "xlsx";

    public static final Function<Workbook, CellStyle> DEFAULT_VALUES_CELL_STYLE = (Workbook workbook) -> {
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(headerFont);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    };

    public static void write(builder b) throws Exception {
        final Workbook workbook = b.workbook;     // new HSSFWorkbook() for generating `.xls` folder

        // Create a Sheet
        Sheet sheet = workbook.createSheet(b.sheetName);

        // Create a CellStyle with the font for headers
        CellStyle headerCellStyle = b.headerCellStyle.apply(workbook);

        // Create a Row for columns
        Row headerRow = sheet.createRow(0);

        // Creating cells of columns
        String[] columns = b.columnsNames.get();
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create a CellStyles for values
        CellStyle valuesCellStyle[] = new CellStyle[columns.length];
        for (int i = 0; i < columns.length; i++) {
            //coge un style default y le agrega las cosas nuevas
            valuesCellStyle[i] = b.valuesColumnCellStyle[i].apply(workbook);
        }

        // Create Other rows and cells with values
        int rowNum = 1;
        List<Object[]> values = b.values.get();
        for (Object[] value : values) {
            Row row = sheet.createRow(rowNum++);

            for (int i = 0; i < value.length; i++) {
                //este es el original, el de abajo es para probar el date
                Cell cell = row.createCell(i);//create cell
                cell.setCellStyle(valuesCellStyle[i]);//format the cell
                setCellValue(cell, value[i]);
            }
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a folder
        b.folder.mkdirs();
        try (FileOutputStream fileOut = new FileOutputStream(finalFile(b.folder, b.fileName, XLSX))) {
            workbook.write(fileOut);
        }

        workbook.close();
    }

    /**
     * Trabajo del indio xq la interfaz no tiene un set object, por lo que hay
     * que parsear todos los valores a mano
     *
     * @param cell
     * @param value
     */
    public static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
        } else if (value instanceof RichTextString) {
            cell.setCellValue((RichTextString) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    public static File finalFile(File parent, String file, String extencion) {
        return new File(parent, file + "." + extencion);
    }

    public static builder builder() {
        return new builder();
    }

    public static class builder {

        private String sheetName = "Hoja 1";
        private Workbook workbook = new XSSFWorkbook();
        private Function<Workbook, CellStyle> headerCellStyle = (Workbook workbook) -> {
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(headerFont);
            cellStyle.setBorderBottom(BorderStyle.THICK);
            cellStyle.setBorderLeft(BorderStyle.THICK);
            cellStyle.setBorderRight(BorderStyle.THICK);
            cellStyle.setBorderTop(BorderStyle.THICK);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            return cellStyle;
        };

        private Function<Workbook, CellStyle>[] valuesColumnCellStyle = null;
        private Supplier<String[]> columnsNames = () -> new String[]{};
        private Supplier<List<Object[]>> values = () -> new ArrayList<>();

        private File folder = new File("temp");
        private String fileName = String.valueOf(System.currentTimeMillis());

        public builder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public builder workbook(Workbook workbook) {
            this.workbook = workbook;
            return this;
        }

        public builder headerCellStyle(CellStyle cellStyle) {
            this.headerCellStyle = (Workbook workbook1) -> {
                return cellStyle;
            };
            return this;
        }

        public builder headerCellStyle(Function<Workbook, CellStyle> cellStyle) {
            this.headerCellStyle = cellStyle;
            return this;
        }

        public builder setColumns(String[] columns) {
            columnsNames = () -> columns;
            setUpValuesEditor();
            return this;
        }

        public builder setColumns(List<String> columns) {
            columnsNames = () -> columns.toArray(new String[]{});
            setUpValuesEditor();
            return this;
        }

        public builder setColumns(Supplier<String[]> columns) {
            columnsNames = columns;
            setUpValuesEditor();
            return this;
        }

        /**
         * < pre>
         * ExcelWriterFunctions.builder().sheetName("123")
         * .setColumns(new String[]{"nombre", "ci", "nacimiento"})
         * .updateColumnStyle(1, (Workbook workbook, CellStyle defaultStyle) → {
         * defaultStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.0000"));
         * return defaultStyle;
         * })
         * .values(EmpleadoDomain.randomArrayList()).write().open();</pre>
         *
         * @param idCol
         * @param predicate
         * @return
         */
        public builder updateValuesColumnCellStyle(int idCol, BiFunction<Workbook, CellStyle, CellStyle> predicate) {
            valuesColumnCellStyle[idCol] = (Workbook t) -> predicate.apply(t, DEFAULT_VALUES_CELL_STYLE.apply(t));
            return this;
        }

        /**
         * Start the valuesColumnCellStyle by default
         */
        private void setUpValuesEditor() {
            String[] get = columnsNames.get();
            valuesColumnCellStyle = new Function[get.length];
            for (int i = 0; i < get.length; i++) {
                valuesColumnCellStyle[i] = DEFAULT_VALUES_CELL_STYLE;
            }
        }

        public builder folder(File folder) {
            this.folder = folder;
            return this;
        }

        public builder folder(String folder) {
            this.folder = new File(folder);
            return this;
        }

        public builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public builder values(List<Object[]> values) {
            this.values = () -> values;
            return this;
        }

        public builder values(Supplier<List<Object[]>> values) {
            this.values = values;
            return this;
        }

        public Opener write() throws Exception {
            ExcelListWriter.write(this);
            return new Opener(finalFile(folder, fileName, XLSX));
        }

    }
}
