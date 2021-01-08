/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.root101.export;

import com.root101.export.json.ExportableConfigJSON;
import com.root101.export.json.JSONListWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public class Main {

    public static void main(String[] args) throws Exception {
        List<ABC> l = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            l.add(new ABC());
        }
        JSONListWriter.builder().config(new ExportableConfigJSON<ABC>() {
            @Override
            public List getValuesList() {
                return l;
            }

            @Override
            public Object[] getRowObjectExport(ABC o) {
                return new Object[]{o.abc, o.chicho};
            }

            @Override
            public String[] getColumnNamesExport() {
                return new String[]{"abc", "chicho"};
            }

            @Override
            public File getFolder() {
                return new File("temp");
            }

            @Override
            public String getFileName() {
                return "test_json_export";
            }

            @Override
            public JSONListWriter.builder exportJSONBuilder() {
                return JSONListWriter.builder().config(this);
            }
        }).write().open();
    }

    public static class ABC {

        private String abc;
        private String chicho;

        public ABC() {
            abc = String.valueOf(new Random().nextInt(10000));
            chicho = String.valueOf(new Random().nextInt(10000));
        }

        public String getAbc() {
            return abc;
        }

        public void setAbc(String abc) {
            this.abc = abc;
        }

        public String getChicho() {
            return chicho;
        }

        public void setChicho(String chicho) {
            this.chicho = chicho;
        }

        @Override
        public String toString() {
            return "ABC{" + "abc=" + abc + ", chicho=" + chicho + '}';
        }

    }
}
