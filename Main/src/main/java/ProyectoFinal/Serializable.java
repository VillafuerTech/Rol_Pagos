package ProyectoFinal;
import java.io.*;
import java.util.List;

public class Serializable { 
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
}
