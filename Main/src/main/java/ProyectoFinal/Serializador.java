package ProyectoFinal;
import java.io.*;
import java.util.List;
import org.json.simple.*;
import org.json.simple.parser.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
public class Serializador { 
    public static void serializarEmpresa(Empresa empresa) {
        try {
            FileOutputStream fileOut = new FileOutputStream("empresa.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(empresa);
            out.close();
            fileOut.close();
            System.out.println("Se ha serializado la empresa en empresa.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    public static void serializarEmpleados(List<Empleado> empleados) {
        try {
            FileOutputStream fileOut = new FileOutputStream("empleados.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(empleados);
            out.close();
            fileOut.close();
            System.out.println("Se ha serializado la lista de empleados en empleados.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    public static Empresa deserializarEmpresa() {
        Empresa empresa = null;
        try {
            FileInputStream fileIn = new FileInputStream("empresa.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            empresa = (Empresa) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Empresa class not found");
            c.printStackTrace();
        }
        return empresa; 
    }

    public static List<Empleado> deserializarEmpleados() {
        List<Empleado> empleados = null;
        try {
            FileInputStream fileIn = new FileInputStream("empleados.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            empleados = (List<Empleado>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Empleado class not found");
            c.printStackTrace();
        }
        return empleados; 
    }
     public static void serializarEmpleadosAJson(List<Empleado> empleados) {
        String nombreArchivo = "empleados.json";
        JSONArray listaEmpleadosJson = new JSONArray();

        // Convertir cada Empleado en JSONObject
        for (Empleado empleado : empleados) {
            JSONObject empleadoJson = new JSONObject();
            empleadoJson.put("nombre", empleado.getNombre());
            empleadoJson.put("apellido", empleado.getApellido());
            empleadoJson.put("codigo", empleado.getCodigo());
            empleadoJson.put("fechaContratacion", empleado.getFechaContratacion());
            empleadoJson.put("sueldo", empleado.getSueldo());

            // Si es Gerente, añadir campos adicionales
            if (empleado instanceof Gerente) {
                Gerente gerente = (Gerente) empleado;
                empleadoJson.put("titulo", gerente.getTitulo());
                empleadoJson.put("comision", gerente.getComision());
            }

            listaEmpleadosJson.add(empleadoJson);
        }

        // Crear el JSON final y guardarlo
        try (FileWriter file = new FileWriter(nombreArchivo)) {
            file.write(listaEmpleadosJson.toJSONString());
            file.flush();
            System.out.println("Lista de empleados serializada correctamente a " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       public static void serializarEmpleadosAXml(List<Empleado> empleados) {
        String nombreArchivo = "empleados.xml";
        try {
            // Crear el contexto JAXB para la clase Empresa
            JAXBContext contexto = JAXBContext.newInstance(Empresa.class);

            // Crear el Marshaller para convertir objetos a XML
            Marshaller marshaller = contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Formatear el XML

            // Crear el objeto contenedor de empleados
            Empresa empresa = new Empresa(empleados);

            // Escribir el contenido al archivo
            marshaller.marshal(empresa, new File(nombreArchivo));
            System.out.println("Lista de empleados serializada correctamente a " + nombreArchivo);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    public static void serializarEmpleadosaCSV(List<Empleado> empleados) {
        String nombreArchivo = "empleados.csv";
        try (PrintWriter writer = new PrintWriter(nombreArchivo)) {
            // Escribir la cabecera
            writer.println("nombre,apellido,codigo,fechaContratacion,sueldo,titulo,comision");

            // Escribir cada empleado
            for (Empleado empleado : empleados) {
                writer.print(empleado.getNombre() + ",");
                writer.print(empleado.getApellido() + ",");
                writer.print(empleado.getCodigo() + ",");
                writer.print(empleado.getFechaContratacion() + ",");
                writer.print(empleado.getSueldo() + ",");

                // Si es Gerente, añadir campos adicionales
                if (empleado instanceof Gerente) {
                    Gerente gerente = (Gerente) empleado;
                    writer.print(gerente.getTitulo() + ",");
                    writer.print(gerente.getComision());
                }

                writer.println();
            }
            System.out.println("Lista de empleados serializada correctamente a " + nombreArchivo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}
