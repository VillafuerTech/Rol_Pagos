package ProyectoFinal;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        Empresa empresa = new Empresa();
        empresa.fromCSV();
        Serializador.serializarEmpresa(empresa);
        Serializador.serializarEmpleados(empresa.getEmpresa());
        //Eliminar empresa
        empresa = null;
        empresa = Serializador.deserializarEmpresa();
        System.out.println(empresa.getEmpresa());
        List<Empleado> empleadosDeserializados = Serializador.deserializarEmpleados();
        System.out.println(empleadosDeserializados);

        //Ingresar empleado
        Empleado empleado = new Empleado("Carlos", "Perez", 1543, "01/01/2020", 1000);
        empresa.AgregarEmpleado(empleado);
        System.out.println("\n\n\n");
        empresa.CalcularSueldoMensualtoTXT();
        empresa.toTXT800(empresa.getPorSueldo());

        //Serializar a CSV
        Serializador.serializarEmpleadosaCSV(empresa.getPorSueldo());

        //Serializar a JSON
        Serializador.serializarEmpleadosAJson(empresa.getPorSueldo());
        //Serializa a XML
        Serializador.serializarEmpleadosAXml(empresa.getPorSueldo());
    }
}   