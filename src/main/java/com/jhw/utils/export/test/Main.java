/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhw.utils.export.test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.*;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public class Main {

    public static void main(String[] args) throws Exception {
        File f = new File("123.pdf");

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(f));
        doc.open();

        List list = new List(true, 30);
        list.add(new ListItem("first List"));
        list.add(new ListItem("second List"));
        list.add(new ListItem("third List"));
        doc.add(list);

        doc.add(Chunk.NEWLINE);

        List list2 = new List(true, true, 30);
        list2.add(new ListItem("a"));
        list2.add(new ListItem("b"));
        list2.add(new ListItem("c"));
        doc.add(list2);

        doc.close();
    }
}
