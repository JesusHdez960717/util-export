/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhw.utils.export.test;

import com.root101.clean.core.domain.DomainObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public class EmpleadoDomain extends DomainObject {

    private static final SecureRandom rdm = new SecureRandom();

    private String nombre;
    private double ci;
    private Date fecha;

    public EmpleadoDomain() {
    }

    public EmpleadoDomain(String nombre, double ci, Date fecha) {
        this.nombre = nombre;
        this.ci = ci;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCi() {
        return ci;
    }

    public void setCi(double ci) {
        this.ci = ci;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public static EmpleadoDomain random() {
        return new EmpleadoDomain(rdm.nextLong() + "", 100000 * rdm.nextDouble(), new Date(100 * rdm.nextInt(Integer.MAX_VALUE)));
    }

    public static List<EmpleadoDomain> randomList() {
        List<EmpleadoDomain> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(random());
        }
        return list;
    }

    public static List<Object[]> randomArrayList() {
        List<Object[]> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            EmpleadoDomain emp = random();
            Object[] row = new Object[]{emp.getNombre(), emp.getCi(), emp.getFecha()};
            list.add(row);
        }
        return list;
    }
}
