/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhw.export.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.jhw.files.utils.Opener;
import com.jhw.files.utils.PersonalizationFiles;
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
public class ExcelListWriter {

    public static final String XLSX = "xlsx";
    public static final String DEFAULT_FONT_NAME = "Arial";

    public static final Function<Workbook, CellStyle> DEFAULT_VALUES_CELL_STYLE = (Workbook workbook) -> {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName(DEFAULT_FONT_NAME);
        font.setColor(IndexedColors.BLACK.getIndex());

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);

        return cellStyle;
    };

    public static void write(builder b) throws Exception {
        //get the workbook
        final Workbook workbook = b.workbook;     // new HSSFWorkbook() for generating `.xls` folder

        // Create a Sheet at position 0
        Sheet sheet = workbook.createSheet(b.sheetName);

        // Create a CellStyle with the font for columns headers
        CellStyle headerCellStyle = b.headerCellStyle.apply(workbook);

        // Create a RowNum for counting rows
        int rowNum = 0;

        //retrieve the columns
        String[] columns = b.columnsNames.get();

        //create the header
        Row titleHeaderRow = sheet.createRow(rowNum++);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.length - 1));
        Cell cellTitle = titleHeaderRow.createCell(0);
        cellTitle.setCellValue(b.fileName);
        cellTitle.setCellStyle(headerCellStyle);

        Row columnsHeaderRow = sheet.createRow(rowNum++);

        // Creating cells of columns
        for (int i = 0; i < columns.length; i++) {
            Cell cell = columnsHeaderRow.createCell(i);
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
        try (FileOutputStream fileOut = new FileOutputStream(b.finalFile)) {
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
        return new File(parent, file + " " + SDF_ALL.format(new Date()) + "." + extencion);
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
            headerFont.setFontName(XLSX);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(headerFont);
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle.setBorderRight(BorderStyle.MEDIUM);
            cellStyle.setBorderTop(BorderStyle.MEDIUM);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            return cellStyle;
        };

        private Function<Workbook, CellStyle>[] valuesColumnCellStyle = null;
        private Supplier<String[]> columnsNames = () -> new String[]{};
        private Supplier<List<Object[]>> values = () -> new ArrayList<>();

        private File folder = new File("temp");
        private String fileName = String.valueOf(System.currentTimeMillis());

        private File finalFile;

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
         * ExcelWriterFunctions.builder().sheetName("123") .setColumns(new
         * String[]{"nombre", "ci", "nacimiento"}) .updateColumnStyle(1,
         * (Workbook workbook, CellStyle defaultStyle) → {
         * defaultStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.0000"));
         * return defaultStyle; })
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

        public builder config(ExportableConfigExcel config) {
            this.folder(config.getFolder())
                    .fileName(config.getFileName())
                    .setColumns(config::getColumnNamesExport)
                    .values(ConverterService.convert(config.getValuesList(), config::getRowObjectExport));

            //personaliza con las cosas que quiera
            config.personalizeBuilder(this);
            return this;
        }

        public Opener write() throws Exception {
            //crate Opener with actual date
            Opener op = Opener.from(finalFile(folder, fileName, XLSX));

            //set up the final file to export
            finalFile = op.getFile();

            //export
            ExcelListWriter.write(this);

            //return opener in case of needed
            return op;
        }

    }
}
