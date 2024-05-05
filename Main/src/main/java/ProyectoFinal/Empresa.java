package ProyectoFinal;

import java.util.List;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@XmlRootElement(name = "Empresa")
public class Empresa implements Serializable {
    @XmlElement(name = "Empleado")
    private List<Empleado> empleados;

    private List<Empleado> empleadosApellido;
    private List<Empleado> empleadosSueldo;
    private List<Empleado> empleadosFechaContratacion;

    public Empresa() {
    }

    public Empresa(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public void setEmpresa(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public List<Empleado> getEmpresa() {
        return empleados;
    }

    public void fromCSV() {
        String csvFile = "registrosEmpleados.csv";
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] empleado = line.split(cvsSplitBy);
                Empleado e = new Empleado(empleado[0], empleado[1], Integer.parseInt(empleado[2]), empleado[3],
                        Float.parseFloat(empleado[4]));
                empleados.add(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String csvFile2 = "registrosGerentes.csv";
        String line2 = "";
        String cvsSplitBy2 = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile2))) {
            while ((line2 = br.readLine()) != null) {
                String[] gerente = line2.split(cvsSplitBy2);
                Gerente g = new Gerente(gerente[0], gerente[1], Integer.parseInt(gerente[2]), gerente[3],
                        Float.parseFloat(gerente[4]), gerente[5], Float.parseFloat(gerente[6]));
                empleados.add(g);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ordenarListas() {
        Collections.sort(empleados, Empleado.compararPorApellido());
        empleadosApellido = new ArrayList<>(empleados);
        Collections.sort(empleados, Empleado.compararPorSueldo());
        empleadosSueldo = new ArrayList<>(empleados);
        Collections.sort(empleados, Empleado.compararPorFechaContratacion());
        empleadosFechaContratacion = new ArrayList<>(empleados);
    }

    public void EliminarEmpleado(String empleadoEliminado) {
        // Revisar que exista el empleado
        boolean existe = false;
        for (Empleado e : empleados) {
            if (e.getNombre().equals(empleadoEliminado)) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            throw new IllegalArgumentException("El empleado no existe");
        }
        for (Empleado e : empleados) {
            if (e.getNombre().equals(empleadoEliminado)) {
                empleados.remove(e);
                break;
            }
        }
        for (Empleado e : empleadosApellido) {
            if (e.getNombre().equals(empleadoEliminado)) {
                empleadosApellido.remove(e);
                break;
            }
        }
        for (Empleado e : empleadosSueldo) {
            if (e.getNombre().equals(empleadoEliminado)) {
                empleadosSueldo.remove(e);
                break;
            }
        }
        for (Empleado e : empleadosFechaContratacion) {
            if (e.getNombre().equals(empleadoEliminado)) {
                empleadosFechaContratacion.remove(e);
                break;
            }
        }
    }

    public void AgregarEmpleado(Empleado empleado) {
        empleados.add(empleado);
        empleadosApellido.add(empleado);
        empleadosSueldo.add(empleado);
        empleadosFechaContratacion.add(empleado);

        // Volver a ordenar
        Collections.sort(empleadosApellido, Empleado.compararPorApellido());
        Collections.sort(empleadosSueldo, Empleado.compararPorSueldo());
        Collections.sort(empleadosFechaContratacion, Empleado.compararPorFechaContratacion());

    }

    public void CalcularSueldoMensualtoTXT() {
        try (PrintWriter writer = new PrintWriter("sueldoMensual.txt")) {
            for (Empleado empleado : empleados) {
                float sueldoMensual = empleado.CalcularSueldoMensual();
                writer.println("Tipo: " + empleado.getClass().getSimpleName());
                writer.println("Código: " + empleado.getCodigo());
                writer.println("Nombre: " + empleado.getNombre());
                writer.println("Apellido: " + empleado.getApellido());
                writer.println("Aporte al IESS: " + empleado.aporteIESS());
                writer.println("Fondo de reserva: " + empleado.fondoReserva());
                writer.println("Decimo tercero mensualizado: " + empleado.decimoTercero());
                writer.println("Decimo cuarto mensualizado: " + empleado.decimoCuarto());
                writer.println("Sueldo Líquido: " + sueldoMensual);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void toCSV() {
        try (PrintWriter writer = new PrintWriter("empleados.csv")) {
            for (Empleado empleado : empleados) {
                writer.println(empleado.toCSV());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void toJSON() {
        try (PrintWriter writer = new PrintWriter("empleados.json")) {
            writer.println("{");
            writer.println("  \"empleados\": [");
            for (Empleado empleado : empleados) {
                writer.println("    {");
                writer.println("      \"nombre\": \"" + empleado.getNombre() + "\",");
                writer.println("      \"apellido\": \"" + empleado.getApellido() + "\",");
                writer.println("      \"codigo\": " + empleado.getCodigo() + ",");
                writer.println("      \"fechaContratacion\": \"" + empleado.getFechaContratacion() + "\",");
                writer.println("      \"sueldo\": " + empleado.getSueldo());
                if (empleado instanceof Gerente) {
                    Gerente gerente = (Gerente) empleado;
                    writer.println("      \"titulo\": \"" + gerente.getTitulo() + "\",");
                    writer.println("      \"comision\": " + gerente.getComision());
                }
                writer.println("    },");
            }
            writer.println("  ]");
            writer.println("}");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void toXML() {
        try (PrintWriter writer = new PrintWriter("empleados.xml")) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<empleados>");
            for (Empleado empleado : empleados) {
                writer.println("  <empleado>");
                writer.println("    <nombre>" + empleado.getNombre() + "</nombre>");
                writer.println("    <apellido>" + empleado.getApellido() + "</apellido>");
                writer.println("    <codigo>" + empleado.getCodigo() + "</codigo>");
                writer.println("    <fechaContratacion>" + empleado.getFechaContratacion() + "</fechaContratacion>");
                writer.println("    <sueldo>" + empleado.getSueldo() + "</sueldo>");
                if (empleado instanceof Gerente) {
                    Gerente gerente = (Gerente) empleado;
                    writer.println("    <titulo>" + gerente.getTitulo() + "</titulo>");
                    writer.println("    <comision>" + gerente.getComision() + "</comision>");
                }
                writer.println("  </empleado>");
            }
            writer.println("</empleados>");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void toTXT800() {
        try (PrintWriter writer = new PrintWriter("800.txt")) {
            for (Empleado empleado : empleados) {
                if (empleado.CalcularSueldoMensual() < 800) {
                    writer.println("Tipo: " + empleado.getClass().getSimpleName());
                    writer.println("Código: " + empleado.getCodigo());
                    writer.println("Nombre: " + empleado.getNombre());
                    writer.println("Apellido: " + empleado.getApellido());
                    writer.println("Sueldo líquido: " + empleado.CalcularSueldoMensual());
                    writer.println(DesgloceBilletes(empleado.CalcularSueldoMensual()));

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String DesgloceBilletes(float sueldo) {
        int billetes20 = (int) sueldo / 20;
        int billetes10 = (int) (((sueldo % 100) % 50) % 20) / 10;
        int billetes5 = (int) ((((sueldo % 100) % 50) % 20) % 10) / 5;
        int billetes1 = (int) (((((sueldo % 100) % 50) % 20) % 10) % 5) / 1;
        return "Billetes de 20: " + billetes20 + "\n" +
                "Billetes de 10: " + billetes10 + "\n" +
                "Billetes de 5: " + billetes5 + "\n" +
                "Billetes de 1: " + billetes1;
    }

}
