package ProyectoFinal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;

import java.io.Serializable;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@XmlRootElement(name = "Empresa")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"empleados", "empleadosApellido", "empleadosSueldo", "empleadosFechaContratacion"})
public class Empresa implements Serializable {
    @XmlElement(name = "Empleado")
    private List<Empleado> empleados;

    private List<Empleado> empleadosApellido;
    private List<Empleado> empleadosSueldo;
    private List<Empleado> empleadosFechaContratacion;  
    
    public List<Empleado> getPorApellido() {
        return empleadosApellido;
    }
    public List<Empleado> getPorSueldo() {
        return empleadosSueldo;
    }
    public List<Empleado> getPorFechaContratacion() {
        return empleadosFechaContratacion;
    }

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
    // Actualiza con las rutas correctas
    String csvFile = "registrosEmpleados.csv";
    String csvFile2 = "registrosGerentes.csv";
    String separator = ",";
    String line;

    // Inicializa la lista de empleados
    empleados = new ArrayList<>();

    // Leer registros de empleados
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        boolean esEncabezado = true;
        while ((line = br.readLine()) != null) {
            // Ignorar el encabezado
            if (esEncabezado) {
                esEncabezado = false;
                continue;
            }

            String[] datosEmpleado = line.split(separator);
            if (datosEmpleado.length >= 5) {
                try {
                    Empleado e = new Empleado(
                        datosEmpleado[0],
                        datosEmpleado[1],
                        Integer.parseInt(datosEmpleado[2]),
                        datosEmpleado[3],
                        Float.parseFloat(datosEmpleado[4])
                    );
                    empleados.add(e);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    ex.printStackTrace(); // Muestra el error, pero continúa leyendo otros registros
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Leer registros de gerentes
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile2))) {
        boolean esEncabezado = true;
        while ((line = br.readLine()) != null) {
            // Ignorar el encabezado
            if (esEncabezado) {
                esEncabezado = false;
                continue;
            }

            String[] datosGerente = line.split(separator);
            if (datosGerente.length >= 7) {
                try {
                    Gerente g = new Gerente(
                        datosGerente[0],
                        datosGerente[1],
                        Integer.parseInt(datosGerente[2]),
                        datosGerente[3],
                        Float.parseFloat(datosGerente[4]),
                        datosGerente[5],
                        Float.parseFloat(datosGerente[6])
                    );
                    empleados.add(g);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    ex.printStackTrace(); // Muestra el error, pero continúa leyendo otros registros
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    ordenarListas();
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
        // Escribe encabezados de las columnas
        String headerFormat = "%-12s %-10s %-15s %-15s %-20s %-12s %-15s %-20s %-20s %-25s %-25s %-15s %-25s%n";
        writer.printf(headerFormat,
                "Tipo", "Código", "Nombre", "Apellido", "Fecha de contratación",
                "Sueldo", "Aporte IESS", "Impuesto Renta", "Fondo Reserva",
                "Decimo Tercero", "Decimo Cuarto", "Sueldo Líquido", "Líquido en Palabras");

        // Imprime una línea separadora para claridad
        writer.println("=".repeat(180));

        // Formato para los datos de cada empleado
        String dataFormat = "%-12s %-10d %-15s %-15s %-20s %-12.2f %-15.2f %-20.2f %-20.2f %-25.2f %-25.2f %-15.2f %-25s%n";
        
        // Escribe los datos de cada empleado
        for (Empleado empleado : empleados) {
            float sueldoMensual = empleado.sueldoLiquido();
            writer.printf(dataFormat,
                    empleado.getClass().getSimpleName(),
                    empleado.getCodigo(),
                    empleado.getNombre(),
                    empleado.getApellido(),
                    empleado.getFechaContratacion(),
                    empleado.getSueldo(),
                    empleado.aporteIESS(),
                    empleado.impuestoRenta(),
                    empleado.fondoReserva(),
                    empleado.decimoTercero(),
                    empleado.decimoCuarto(),
                    sueldoMensual,
                    NumerosATexto.numeroATexto((int) sueldoMensual)
            );
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}


    public void toTXT800(List<Empleado> empleados) {
        try (PrintWriter writer = new PrintWriter("800.txt")) {
            for (Empleado empleado : empleados) {
                if (empleado.sueldoLiquido() < 800) {
                    writer.println("Tipo: " + empleado.getClass().getSimpleName());
                    writer.println("Código: " + empleado.getCodigo());
                    writer.println("Nombre: " + empleado.getNombre());
                    writer.println("Apellido: " + empleado.getApellido());
                    writer.println("Sueldo líquido: " + empleado.sueldoLiquido());
                    writer.println(DesgloceBilletes(empleado.sueldoLiquido()) + "\n");

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String DesgloceBilletes(float sueldo) {
    // Convertir a entero para eliminar la fracción decimal
    int sueldoInt = (int) sueldo;

    // Calcular el número de billetes de cada denominación
    int billetes20 = sueldoInt / 20;
    sueldoInt %= 20;

    int billetes10 = sueldoInt / 10;
    sueldoInt %= 10;

    int billetes5 = sueldoInt / 5;
    sueldoInt %= 5;


    int billetes1 = sueldoInt;

    // Formatear el resultado
    return "Billetes de 20: " + billetes20 + "\n" +
           "Billetes de 10: " + billetes10 + "\n" +
           "Billetes de 5: " + billetes5 + "\n" +
           "Billetes de 1: " + billetes1;
}

}
