package ProyectoFinal;
import java.util.Comparator;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "Gerente")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"titulo", "comision"})
public class Gerente extends Empleado {
        private String titulo;
        private float comision;

        public Gerente(String nombre, String apellido, int codigo, String fechaContratacion, float sueldo) {
            super(nombre, apellido, codigo, fechaContratacion, sueldo);
        }
        public Gerente(String nombre, String apellido, int codigo, String fechaContratacion, float sueldo, String titulo, float comision) {
            super (nombre, apellido, codigo, fechaContratacion, sueldo);
            setTitulo(titulo);
            setComision(comision);
        }
        public Gerente() {
            super();
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            //No puede ser vacio
            if (titulo.isEmpty()) {
                throw new IllegalArgumentException("El titulo no puede ser vacio");
            }
            this.titulo = titulo;
        }

        public float getComision() {
            return comision;
        }

        private void setComision(float comision) {
            //No puede ser negativo
            if (comision < 0) {
                throw new IllegalArgumentException("La comision no puede ser negativa");
            }   
            this.comision = comision;
        }

        @Override
        public String toString() {
            return super.toString() + ", Titulo: " + titulo + ", Comision: " + comision;
        }
        public static Comparator<Empleado> compararPorSueldo() {
        return new Comparator<Empleado>() {
            @Override
            public int compare(Empleado e1, Empleado e2) {
                return Float.compare((float)e1.getSueldo(), (float)e2.getSueldo());
            }
        };
    }
        @Override
        public int compareTo(Empleado empleado) {
            float sueldoYBonificacion1 = (float) (this.getSueldo() + this.getComision());
            float sueldoYBonificacion2 = (float) (empleado.getSueldo() + ((Gerente) empleado).getComision());
            return Float.compare(sueldoYBonificacion1, sueldoYBonificacion2);
        }
      
        @Override
        public float sueldoLiquido() {
        float sueldoMensual = CalcularSueldoMensual();
        float sueldoLiquido = sueldoMensual + comision/12 - impuestoRenta();
        return sueldoLiquido;
    }   
    }
