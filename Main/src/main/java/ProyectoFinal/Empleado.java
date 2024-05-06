package ProyectoFinal;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "Empleado")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"nombre", "apellido", "codigo", "fechaContratacion", "sueldo"})
@XmlSeeAlso({Gerente.class}) 

public class Empleado implements Comparable<Empleado>, Comparator<Empleado>, Serializable {
    private String nombre;
    private String apellido;
    private int codigo;
    private String fechaContratacion;
    private float sueldo;
    public Empleado() {
    }

    public Empleado(String nombre, String apellido, int codigo, String fechaContratacion, float sueldo) {
        setNombre(nombre);
        setApellido(apellido);
        setCodigo(codigo);
        setFechaContratacion(fechaContratacion);
        setSueldo(sueldo);
    }

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        //No puede ser vacio
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser vacio");
        }
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        //No puede ser vacio
        if (apellido.isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser vacio");
        }
        this.apellido = apellido;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        //No puede ser negativo y debe ser unico
        if (codigo < 0) {
            throw new IllegalArgumentException("El codigo no puede ser negativo");
        }
        this.codigo = codigo;
    }

    public String getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(String fechaContratacion) {
        //No puede ser vacio
        if (fechaContratacion.isEmpty()) {
            throw new IllegalArgumentException("La fecha de contratacion no puede ser vacia");
        }
        //Debe tener el formato dd/mm/yyyy
        if (!fechaContratacion.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new IllegalArgumentException("La fecha de contratacion debe tener el formato dd/mm/yyyy");
        }
        this.fechaContratacion = fechaContratacion;
    }

    public float getSueldo() {
        return sueldo;
    }

    public void setSueldo(float sueldo) {
        //No puede ser negativo
        if (sueldo < 0) {
            throw new IllegalArgumentException("El sueldo no puede ser negativo");
        }
        //El sueldo debe ser entre 800 y 1500
        if (sueldo < 800 || sueldo > 3500) {
            throw new IllegalArgumentException("El sueldo debe ser entre 800 y 1500");
        }
        this.sueldo = sueldo;
    }

    // toString method
    @Override
    public String toString() {
        return "Empleado{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", codigo=" + codigo +
                ", fechaContratacion='" + fechaContratacion + '\'' +
                ", sueldo=" + sueldo +
                '}'+"\n";
    }

    
    // Comparable interface implementation
         
    // Implementación de Comparable (ordenar por sueldo)
    @Override
    public int compareTo(Empleado otro) {
        return Float.compare(this.sueldo, otro.sueldo);
    }

    // Métodos para obtener comparadores estáticos
    public static Comparator<Empleado> compararPorApellido() {
        return new Comparator<Empleado>() {
            @Override
            public int compare(Empleado e1, Empleado e2) {
                return e1.getApellido().compareTo(e2.getApellido());
            }
        };
    }

    public static Comparator<Empleado> compararPorSueldo() {
        return new Comparator<Empleado>() {
            @Override
            public int compare(Empleado e1, Empleado e2) {
                return Float.compare((float)e1.getSueldo(), (float)e2.getSueldo());
            }   
        };
    }

    public static Comparator<Empleado> compararPorFechaContratacion() {
        return new Comparator<Empleado>() {
            @Override
            public int compare(Empleado e1, Empleado e2) {
                return e1.getFechaContratacion().compareTo(e2.getFechaContratacion());
            }
        };
    }
    @Override
    public int compare(Empleado e1, Empleado e2) {
        return e1.getApellido().compareTo(e2.getApellido());
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Empleado empleado = (Empleado) obj;
        return Float.compare(empleado.sueldo, sueldo) == 0 &&
                Objects.equals(fechaContratacion, empleado.fechaContratacion);
    }
    public static boolean haPasadoUnAño(String fechaStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Parsear la fecha dada al formato LocalDate
            LocalDate fechaInicial = LocalDate.parse(fechaStr, formatter);

            // Obtener la fecha actual
            LocalDate fechaActual = LocalDate.now();

            // Calcular el período entre la fecha inicial y la actual
            Period periodo = Period.between(fechaInicial, fechaActual);

            // Verificar si ha pasado al menos un año
            return periodo.getYears() >= 1;
        } catch (DateTimeParseException e) {
            System.err.println("Formato de fecha incorrecto: " + e.getMessage());
            return false;
        }
    }
    
    protected float CalcularSueldoMensual() {
        aporteIESS();
        fondoReserva();
        decimoTercero();
        decimoCuarto();
        float sueldoMensual = getSueldo();
        float sueldoMensualsinRenta = sueldoMensual - aporteIESS() - fondoReserva() + decimoTercero() + decimoCuarto();
        return sueldoMensualsinRenta;
    }
    public float aporteIESS() {
        return (float) (getSueldo() * 0.0945);
    }
    public float fondoReserva(){
        float fondoReserva = 0;
        if (Empleado.haPasadoUnAño(getFechaContratacion())) {
            fondoReserva = (float) (getSueldo() * 0.0833);
        }
        return fondoReserva;
    }
    public float decimoTercero(){
        return (float) (getSueldo() / 12);
    }
    public float decimoCuarto(){
        return (float) (460 / 12);
    }
    public float impuestoRenta(){
        float impuestoRenta = 0;
        float ingresoAnual = CalcularSueldoMensual() * 12;

        if (ingresoAnual <= 11902) {
            impuestoRenta = 0;
        } else if (ingresoAnual <= 15159) {
            impuestoRenta = (ingresoAnual - 11902) * 0.05f;
        } else if (ingresoAnual <= 19682) {
            impuestoRenta = 163 + (ingresoAnual - 15159) * 0.1f;
        } else if (ingresoAnual <= 26031) {
            impuestoRenta = 615 + (ingresoAnual - 19682) * 0.12f;
        } else if (ingresoAnual <= 34255) {
            impuestoRenta = 1377 + (ingresoAnual - 26031) * 0.15f;
        } else if (ingresoAnual <= 45407) {
            impuestoRenta = 2611 + (ingresoAnual - 34255) * 0.2f;
        } else if (ingresoAnual <= 60450) {
            impuestoRenta = 4841 + (ingresoAnual - 45407) * 0.25f;
        } else if (ingresoAnual <= 80605) {
            impuestoRenta = 8602 + (ingresoAnual - 60450) * 0.3f;
        } else if (ingresoAnual <= 107199) {
            impuestoRenta = 14648 + (ingresoAnual - 80605) * 0.35f;
        } else {
            impuestoRenta = 23956 + (ingresoAnual - 107199) * 0.37f;
        }

        return impuestoRenta/12;
    }
    public float sueldoLiquido(){
        return CalcularSueldoMensual() - impuestoRenta();
    }
}