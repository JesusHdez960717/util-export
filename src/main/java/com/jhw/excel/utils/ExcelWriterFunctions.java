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

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public class ExcelWriterFunctions {

    public static final String XLSX = "xlsx";

    public static void write(builder b) throws Exception {
        final Workbook workbook = b.workbook;     // new HSSFWorkbook() for generating `.xls` folder

        // Create a Sheet
        Sheet sheet = workbook.createSheet(b.sheetName);

        // Create a CellStyle with the font for headers
        CellStyle headerCellStyle = b.columnCellStyle.apply(workbook);

        // Create a Row for columns
        Row headerRow = sheet.createRow(0);

        // Creating cells of columns
        String[] columns = b.columnsNames.get();
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create a CellStyle with the font for values
        CellStyle valuesCellStyle = b.valuesCellStyle.apply(workbook);

        // Create Other rows and cells with values
        int rowNum = 1;
        List<Object[]> values = b.values.get();
        for (Object[] value : values) {
            Row row = sheet.createRow(rowNum++);

            for (int i = 0; i < value.length; i++) {
                Cell cell = row.createCell(i);//create cell
                cell.setCellValue(value[i].toString());//set the value
                cell.setCellStyle(valuesCellStyle);//format the cell
            }
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a folder
        b.folder.mkdirs();
        try (FileOutputStream fileOut = new FileOutputStream(finalFile(b.folder, b.fileName))) {
            workbook.write(fileOut);
        }

        workbook.close();
    }

    public static File finalFile(File parent, String file) {
        return new File(parent, file + "." + XLSX);
    }

    public static builder builder() {
        return new builder();
    }

    public static class builder {

        private String sheetName = "Hoja 1";
        private Workbook workbook = new XSSFWorkbook();
        private Function<Workbook, CellStyle> columnCellStyle = (Workbook workbook) -> {
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

            return cellStyle;
        };
        private Function<Workbook, CellStyle> valuesCellStyle = (Workbook workbook) -> {
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

        public builder columnCellStyle(CellStyle cellStyle) {
            this.columnCellStyle = (Workbook workbook1) -> {
                return cellStyle;
            };
            return this;
        }

        public builder columnCellStyle(Function<Workbook, CellStyle> cellStyle) {
            this.columnCellStyle = cellStyle;
            return this;
        }

        public builder valuesCellStyle(CellStyle cellStyle) {
            this.valuesCellStyle = (Workbook workbook1) -> {
                return cellStyle;
            };
            return this;
        }

        public builder valuesCellStyle(Function<Workbook, CellStyle> cellStyle) {
            this.valuesCellStyle = cellStyle;
            return this;
        }

        public builder setColumns(String[] columns) {
            columnsNames = () -> columns;
            return this;
        }

        public builder setColumns(List<String> columns) {
            columnsNames = () -> columns.toArray(new String[]{});
            return this;
        }

        public builder setColumns(Supplier<String[]> columns) {
            columnsNames = columns;
            return this;
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
            ExcelWriterFunctions.write(this);
            return new Opener(finalFile(folder, fileName));
        }

    }
}
