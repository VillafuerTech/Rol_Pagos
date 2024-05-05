package ProyectoFinal;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        Empresa empresa = new Empresa();
        empresa.fromCSV();
        Serializable.serializarEmpresa(empresa);
        Serializable.serializarEmpleados(empresa.getEmpresa());
        Empresa empresaDeserializada = Serializable.deserializarEmpresa();
        System.out.println(empresaDeserializada.getEmpresa());
        List<Empleado> empleadosDeserializados = Serializable.deserializarEmpleados();
        System.out.println(empleadosDeserializados);
    }}