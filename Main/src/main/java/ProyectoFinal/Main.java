package ProyectoFinal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class Main extends Application {
    private Empresa empresa;
    private ObservableList<Empleado> empleadosObservable;
    private TableView<Empleado> tablaEmpleados;

    @Override
    public void start(Stage primaryStage) {
        // Inicializar la empresa y cargar datos desde CSV
        empresa = new Empresa();
        empresa.fromCSV();

        // Convertir la lista a ObservableList para uso en JavaFX
        empleadosObservable = FXCollections.observableArrayList(empresa.getEmpresa());

        // Configuración de la interfaz principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Crear la tabla para mostrar los empleados
        tablaEmpleados = new TableView<>();
        tablaEmpleados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columnas de la tabla
        TableColumn<Empleado, String> nombreColumna = new TableColumn<>("Nombre");
        nombreColumna.setCellValueFactory(cellData -> {
            Empleado empleado = cellData.getValue();
            ObservableValue<String> nombreProperty = new SimpleStringProperty(empleado.getNombre());
            return nombreProperty;
        });
    
        TableColumn<Empleado, String> apellidoColumna = new TableColumn<>("Apellido");
        apellidoColumna.setCellValueFactory(cellData -> {
            Empleado empleado = cellData.getValue();
            ObservableValue<String> apellidoProperty = new SimpleStringProperty(empleado.getApellido());
            return apellidoProperty;
        });

        TableColumn<Empleado, Integer> codigoColumna = new TableColumn<>("Código");
        codigoColumna.setCellValueFactory(cellData -> {
            Empleado empleado = cellData.getValue();
            ObservableValue<Integer> codigoProperty = new SimpleIntegerProperty(empleado.getCodigo()).asObject();
            return codigoProperty;
        });


        TableColumn<Empleado, String> fechaContratacionColumna = new TableColumn<>("Fecha Contratación");
        fechaContratacionColumna.setCellValueFactory(cellData -> {
            Empleado empleado = cellData.getValue();
            ObservableValue<String> fechaContratacionProperty = new SimpleStringProperty(empleado.getFechaContratacion());
            return fechaContratacionProperty;
        });
    

        TableColumn<Empleado, Float> sueldoColumna = new TableColumn<>("Sueldo");
        sueldoColumna.setCellValueFactory(cellData -> {
            Empleado empleado = cellData.getValue();
            ObservableValue<Float> sueldoProperty = new SimpleFloatProperty(empleado.getSueldo()).asObject();
            return sueldoProperty;
        });
        // Agregar columnas a la tabla
        tablaEmpleados.getColumns().addAll(nombreColumna, apellidoColumna, codigoColumna, fechaContratacionColumna, sueldoColumna);

        // Establecer los elementos en la tabla
        tablaEmpleados.setItems(empleadosObservable);

        // Crear botones para las acciones
        Button agregarBtn = new Button("Agregar Empleado");
        Button eliminarBtn = new Button("Eliminar Empleado");
        Button calcularSueldoBtn = new Button("Calcular Sueldo Mensual");
        Button exportarBinarioBtn = new Button("Exportar Binario");
        Button empleadosMenos800Btn = new Button("Generar Listado < 800");

        // Layout para los botones y la tabla
        VBox vbox = new VBox(10, agregarBtn, eliminarBtn, calcularSueldoBtn, exportarBinarioBtn, empleadosMenos800Btn);
        root.setRight(vbox);
        root.setCenter(tablaEmpleados);

        // Eventos de los botones
        agregarBtn.setOnAction(e -> {
            // Implementar agregar empleado
            // actualizarTabla() después de modificar la lista
        });

        eliminarBtn.setOnAction(e -> {
            // Implementar eliminar empleado seleccionado
            Empleado seleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                empresa.EliminarEmpleado(seleccionado.getNombre());
                actualizarTabla(empresa.getEmpresa());
                mostrarAlerta("Éxito", "Empleado eliminado correctamente.");
            } else {
                mostrarAlerta("Error", "Seleccione un empleado para eliminar.");
            }
        });

        calcularSueldoBtn.setOnAction(e -> {
            // Implementar cálculo de sueldo mensual
            empresa.CalcularSueldoMensualtoTXT();
            mostrarAlerta("Éxito", "El cálculo de sueldos ha sido generado correctamente.");
        });

        exportarBinarioBtn.setOnAction(e -> {
            // Implementar exportación a archivo binario
        });

        empleadosMenos800Btn.setOnAction(e -> {
            // Implementar generación de listado de empleados con sueldo < 800
            empresa.toTXT800(empresa.getEmpresa());
            mostrarAlerta("Éxito", "El listado ha sido generado correctamente.");
        });

        // Configurar la escena y mostrar
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("Gestión de Empleados");
        primaryStage.show();
    }

    // Actualiza la tabla con una lista observable
    private void actualizarTabla(List<Empleado> lista) {
        empleadosObservable.setAll(lista);
    }

    // Muestra una alerta de confirmación
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
