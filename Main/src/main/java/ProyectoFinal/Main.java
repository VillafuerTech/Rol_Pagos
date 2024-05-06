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
        TableColumn<Empleado, String> nombreTipo = new TableColumn<>("Tipo");
        nombreTipo.setCellValueFactory(cellData -> {
            Empleado empleado = cellData.getValue();
            ObservableValue<String> nombreTipoProperty = new SimpleStringProperty(empleado.getClass().getSimpleName());
            return nombreTipoProperty;
        });
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
        tablaEmpleados.getColumns().addAll(nombreTipo,nombreColumna, apellidoColumna, codigoColumna, fechaContratacionColumna, sueldoColumna);

        // Establecer los elementos en la tabla
        tablaEmpleados.setItems(empleadosObservable);

        actualizarTabla(empresa.getEmpresa());
        // Crear botones para las acciones
        Button agregarEmpleadoBtn = new Button("Agregar Empleado");
        Button agregarGerenteBtn = new Button("Agregar Gerente");
        Button eliminarBtn = new Button("Eliminar Empleado");
        Button calcularSueldoBtn = new Button("Calcular Sueldo Mensual");
        Button exportarBinarioBtn = new Button("Exportar y recuperar Binario");
        Button empleadosMenos800Btn = new Button("Generar Listado < 800");
        Button modificarBtn = new Button("Modificar Empleado");
        Button apellidosBtn = new Button("Formatos ordenados por Apellido");
        Button sueldosBtn = new Button("Formatos ordenados por Sueldo");
        Button fechaBtn = new Button("Formatos ordenados por Fecha de Contratación");   
        Button empleadoindividualBtn = new Button("Empleado Individual Rol de Pago");

        // Layout para los botones y la tabla
        VBox vbox = new VBox(10, agregarEmpleadoBtn, agregarGerenteBtn, eliminarBtn, modificarBtn, calcularSueldoBtn,empleadoindividualBtn, exportarBinarioBtn, empleadosMenos800Btn, apellidosBtn, sueldosBtn, fechaBtn);
        root.setRight(vbox);
        root.setCenter(tablaEmpleados);
        
            // Eventos de los botones
            // Crear un cuadro de diálogo para agregar un nuevo empleado
    empleadoindividualBtn.setOnAction(e -> {
        Empleado seleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            mostrarAlerta("Rol de pagos:", empresa.EmpleadoIndividual(seleccionado.getCodigo()));
        } else {
            mostrarAlerta("Error", "Seleccione un empleado para generar el rol de pago.");
        }
    });
    apellidosBtn.setOnAction(e -> {
        Serializador.serializarEmpleadosAJson(empresa.getPorApellido());
        Serializador.serializarEmpleadosAXml(empresa.getPorApellido());
        Serializador.serializarEmpleadosaCSV(empresa.getPorApellido());
        mostrarAlerta("Éxito", "Los empleados han sido ordenados por apellido y exportados correctamente.");
    });
    sueldosBtn.setOnAction(e -> {
        Serializador.serializarEmpleadosAJson(empresa.getPorSueldo());
        Serializador.serializarEmpleadosAXml(empresa.getPorSueldo());
        Serializador.serializarEmpleadosaCSV(empresa.getPorSueldo());
        mostrarAlerta("Éxito", "Los empleados han sido ordenados por sueldo y exportados correctamente.");
    });
    fechaBtn.setOnAction(e -> {
        Serializador.serializarEmpleadosAJson(empresa.getPorFechaContratacion());
        Serializador.serializarEmpleadosAXml(empresa.getPorFechaContratacion());
        Serializador.serializarEmpleadosaCSV(empresa.getPorFechaContratacion());
        mostrarAlerta("Éxito", "Los empleados han sido ordenados por fecha de contratación y exportados correctamente.");
    });
    agregarEmpleadoBtn.setOnAction(e -> {
    Dialog<Empleado> dialogo = new Dialog<>();
    dialogo.setTitle("Agregar Empleado");

    // Crear campos para introducir información
    TextField nombreField = new TextField();
    nombreField.setPromptText("Nombre");
    TextField apellidoField = new TextField();
    apellidoField.setPromptText("Apellido");
    TextField codigoField = new TextField();
    codigoField.setPromptText("Código");
    TextField fechaField = new TextField();
    fechaField.setPromptText("Fecha de Contratación (dd/mm/yyyy)");
    TextField sueldoField = new TextField();
    sueldoField.setPromptText("Sueldo");

    VBox contenido = new VBox(10,
        new Label("Nombre:"), nombreField,
        new Label("Apellido:"), apellidoField,
        new Label("Código:"), codigoField,
        new Label("Fecha de Contratación:"), fechaField,
        new Label("Sueldo:"), sueldoField
    );
    contenido.setPadding(new Insets(20));

    dialogo.getDialogPane().setContent(contenido);
    dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    dialogo.setResultConverter(boton -> {
        if (boton == ButtonType.OK) {
            // Obtener datos introducidos
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String codigoStr = codigoField.getText();
            String fecha = fechaField.getText();
            String sueldoStr = sueldoField.getText();

            // Validar campos vacíos
            if (nombre.isEmpty() || apellido.isEmpty() || codigoStr.isEmpty() || fecha.isEmpty() || sueldoStr.isEmpty()) {
                mostrarAlerta("Error", "No puede haber campos vacíos.");
                return null;
            }

            // Validar código
            int codigo;
            try {
                codigo = Integer.parseInt(codigoStr);
                if (codigo < 0) {
                    mostrarAlerta("Error", "El código debe ser positivo.");
                    return null;
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El código debe ser un número.");
                return null;
            }

            // Validar sueldo
            float sueldo;
            try {
                sueldo = Float.parseFloat(sueldoStr);
                if (sueldo < 800 || sueldo > 3500) {
                    mostrarAlerta("Error", "El sueldo debe estar entre 800 y 3500.");
                    return null;
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El sueldo debe ser un número.");
                return null;
            }

            // Validar formato de fecha
            if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
                mostrarAlerta("Error", "La fecha debe tener el formato dd/mm/yyyy.");
                return null;
            }

            try {
                // Crear un nuevo empleado si todas las validaciones pasaron
                return new Empleado(nombre, apellido, codigo, fecha, sueldo);
            } catch (IllegalArgumentException ex) {
                mostrarAlerta("Error", ex.getMessage());
                return null;
            }
        }
        return null;
    });

    Empleado nuevoEmpleado = dialogo.showAndWait().orElse(null);
    if (nuevoEmpleado != null) {
        try {
            empresa.AgregarEmpleado(nuevoEmpleado);
            actualizarTabla(empresa.getEmpresa());
        } catch (IllegalArgumentException ex) {
            mostrarAlerta("Error", ex.getMessage());
        }
    }
});
agregarGerenteBtn.setOnAction(e -> {
    Dialog<Gerente> dialogo = new Dialog<>();
    dialogo.setTitle("Agregar Gerente");

    // Crear campos para introducir información
    TextField nombreField = new TextField();
    nombreField.setPromptText("Nombre");
    TextField apellidoField = new TextField();
    apellidoField.setPromptText("Apellido");
    TextField codigoField = new TextField();
    codigoField.setPromptText("Código");
    TextField fechaField = new TextField();
    fechaField.setPromptText("Fecha de Contratación (dd/mm/yyyy)");
    TextField sueldoField = new TextField();
    sueldoField.setPromptText("Sueldo");
    TextField tituloField = new TextField();
    tituloField.setPromptText("Título");
    TextField comisionField = new TextField();
    comisionField.setPromptText("Comisión");

    VBox contenido = new VBox(10,
        new Label("Nombre:"), nombreField,
        new Label("Apellido:"), apellidoField,
        new Label("Código:"), codigoField,
        new Label("Fecha de Contratación:"), fechaField,
        new Label("Sueldo:"), sueldoField,
        new Label("Título:"), tituloField,
        new Label("Comisión:"), comisionField
    );
    contenido.setPadding(new Insets(20));

    dialogo.getDialogPane().setContent(contenido);
    dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    dialogo.setResultConverter(boton -> {
        if (boton == ButtonType.OK) {
            // Obtener datos introducidos
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String codigoStr = codigoField.getText();
            String fecha = fechaField.getText();
            String sueldoStr = sueldoField.getText();
            String titulo = tituloField.getText();
            String comisionStr = comisionField.getText();

            // Validar campos vacíos
            if (nombre.isEmpty() || apellido.isEmpty() || codigoStr.isEmpty() || fecha.isEmpty() || sueldoStr.isEmpty() || titulo.isEmpty() || comisionStr.isEmpty()) {
                mostrarAlerta("Error", "No puede haber campos vacíos.");
                return null;
            }

            // Validar código
            int codigo;
            try {
                codigo = Integer.parseInt(codigoStr);
                if (codigo < 0) {
                    mostrarAlerta("Error", "El código debe ser positivo.");
                    return null;
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El código debe ser un número.");
                return null;
            }

            // Validar sueldo
            float sueldo;
            try {
                sueldo = Float.parseFloat(sueldoStr);
                if (sueldo < 800 || sueldo > 3500) {
                    mostrarAlerta("Error", "El sueldo debe estar entre 800 y 3500.");
                    return null;
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "El sueldo debe ser un número.");
                return null;
            }

            // Validar comisión
            float comision;
            try {
                comision = Float.parseFloat(comisionStr);
                if (comision < 0) {
                    mostrarAlerta("Error", "La comisión debe ser positiva.");
                    return null;
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "La comisión debe ser un número.");
                return null;
            }

            // Validar formato de fecha
            if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
                mostrarAlerta("Error", "La fecha debe tener el formato dd/mm/yyyy.");
                return null;
            }

            try {
                // Crear un nuevo gerente si todas las validaciones pasan
                return new Gerente(nombre, apellido, codigo, fecha, sueldo, titulo, comision);
            } catch (IllegalArgumentException ex) {
                mostrarAlerta("Error", ex.getMessage());
                return null;
            }
        }
        return null;
    });

    Gerente nuevoGerente = dialogo.showAndWait().orElse(null);
    if (nuevoGerente != null) {
        try {
            empresa.AgregarEmpleado(nuevoGerente);
            actualizarTabla(empresa.getEmpresa());
        } catch (IllegalArgumentException ex) {
            mostrarAlerta("Error", ex.getMessage());
        }
    }
});

            eliminarBtn.setOnAction(e -> {
                // Implementar eliminar empleado seleccionado
                Empleado seleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Eliminar Empleado");
                alerta.setHeaderText(null);
                alerta.setContentText("¿Está seguro de eliminar a " + seleccionado.getNombre() + "?");
                alerta.showAndWait();
                //No eliminar si el usuario cancela
                if (alerta.getResult() != ButtonType.OK) {
                    return;
                }
                empresa.EliminarEmpleado(seleccionado.getNombre());
                actualizarTabla(empresa.getEmpresa());
                mostrarAlerta("Éxito", "Empleado eliminado correctamente.");
            } else {
                mostrarAlerta("Error", "Seleccione un empleado para eliminar.");
            }
        });
        modificarBtn.setOnAction(e -> {
    Empleado seleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
    
    if (seleccionado instanceof Gerente) {
        // Diálogo para modificar gerente
        Gerente gerenteSeleccionado = (Gerente) seleccionado;
        Dialog<Gerente> dialogo = new Dialog<>();
        dialogo.setTitle("Modificar Gerente");

        // Crear campos para introducir información
        TextField nombreField = new TextField(gerenteSeleccionado.getNombre());
        TextField apellidoField = new TextField(gerenteSeleccionado.getApellido());
        TextField codigoField = new TextField(String.valueOf(gerenteSeleccionado.getCodigo()));
        TextField fechaField = new TextField(gerenteSeleccionado.getFechaContratacion());
        TextField sueldoField = new TextField(String.valueOf(gerenteSeleccionado.getSueldo()));
        TextField tituloField = new TextField(gerenteSeleccionado.getTitulo());

        VBox contenido = new VBox(10,
            new Label("Nombre:"), nombreField,
            new Label("Apellido:"), apellidoField,
            new Label("Código:"), codigoField,
            new Label("Fecha de Contratación:"), fechaField,
            new Label("Sueldo:"), sueldoField,
            new Label("Título:"), tituloField
        );
        contenido.setPadding(new Insets(20));

        dialogo.getDialogPane().setContent(contenido);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogo.setResultConverter(boton -> {
            if (boton == ButtonType.OK) {
                // Validar campos
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                String codigoStr = codigoField.getText();
                String fecha = fechaField.getText();
                String sueldoStr = sueldoField.getText();
                String titulo = tituloField.getText();

                // Validar campos vacíos
                if (nombre.isEmpty() || apellido.isEmpty() || codigoStr.isEmpty() || fecha.isEmpty() || sueldoStr.isEmpty() || titulo.isEmpty()) {
                    mostrarAlerta("Error", "No puede haber campos vacíos.");
                    return null;
                }

                // Validar código
                int codigo;
                try {
                    codigo = Integer.parseInt(codigoStr);
                    if (codigo < 0) {
                        mostrarAlerta("Error", "El código debe ser positivo.");
                        return null;
                    }
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "El código debe ser un número.");
                    return null;
                }

                // Validar sueldo
                float sueldo;
                try {
                    sueldo = Float.parseFloat(sueldoStr);
                    if (sueldo < 800 || sueldo > 3500) {
                        mostrarAlerta("Error", "El sueldo debe estar entre 800 y 3500.");
                        return null;
                    }
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "El sueldo debe ser un número.");
                    return null;
                }

                // Validar formato de fecha
                if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    mostrarAlerta("Error", "La fecha debe tener el formato dd/mm/yyyy.");
                    return null;
                }

                // Actualizar campos
                try {
                    gerenteSeleccionado.setNombre(nombre);
                    gerenteSeleccionado.setApellido(apellido);
                    gerenteSeleccionado.setCodigo(codigo);
                    gerenteSeleccionado.setFechaContratacion(fecha);
                    gerenteSeleccionado.setSueldo(sueldo);
                    gerenteSeleccionado.setTitulo(titulo);
                } catch (IllegalArgumentException ex) {
                    mostrarAlerta("Error", ex.getMessage());
                    return null;
                }

                return gerenteSeleccionado;
            }
            return null;
        });

        dialogo.showAndWait();
        actualizarTabla(empresa.getEmpresa());

    } else if (seleccionado != null) {
        // Diálogo para modificar empleado
        Dialog<Empleado> dialogo = new Dialog<>();
        dialogo.setTitle("Modificar Empleado");

        // Crear campos para introducir información
        TextField nombreField = new TextField(seleccionado.getNombre());
        TextField apellidoField = new TextField(seleccionado.getApellido());
        TextField codigoField = new TextField(String.valueOf(seleccionado.getCodigo()));
        TextField fechaField = new TextField(seleccionado.getFechaContratacion());
        TextField sueldoField = new TextField(String.valueOf(seleccionado.getSueldo()));

        VBox contenido = new VBox(10,
            new Label("Nombre:"), nombreField,
            new Label("Apellido:"), apellidoField,
            new Label("Código:"), codigoField,
            new Label("Fecha de Contratación:"), fechaField,
            new Label("Sueldo:"), sueldoField
        );
        contenido.setPadding(new Insets(20));

        dialogo.getDialogPane().setContent(contenido);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogo.setResultConverter(boton -> {
            if (boton == ButtonType.OK) {
                // Validar campos
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                String codigoStr = codigoField.getText();
                String fecha = fechaField.getText();
                String sueldoStr = sueldoField.getText();

                // Validar campos vacíos
                if (nombre.isEmpty() || apellido.isEmpty() || codigoStr.isEmpty() || fecha.isEmpty() || sueldoStr.isEmpty()) {
                    mostrarAlerta("Error", "No puede haber campos vacíos.");
                    return null;
                }

                // Validar código
                int codigo;
                try {
                    codigo = Integer.parseInt(codigoStr);
                    if (codigo < 0) {
                        mostrarAlerta("Error", "El código debe ser positivo.");
                        return null;
                    }
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "El código debe ser un número.");
                    return null;
                }

                // Validar sueldo
                float sueldo;
                try {
                    sueldo = Float.parseFloat(sueldoStr);
                    if (sueldo < 800 || sueldo > 3500) {
                        mostrarAlerta("Error", "El sueldo debe estar entre 800 y 3500.");
                        return null;
                    }
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "El sueldo debe ser un número.");
                    return null;
                }

                // Validar formato de fecha
                if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    mostrarAlerta("Error", "La fecha debe tener el formato dd/mm/yyyy.");
                    return null;
                }

                // Actualizar campos
                try {
                    seleccionado.setNombre(nombre);
                    seleccionado.setApellido(apellido);
                    seleccionado.setCodigo(codigo);
                    seleccionado.setFechaContratacion(fecha);
                    seleccionado.setSueldo(sueldo);
                } catch (IllegalArgumentException ex) {
                    mostrarAlerta("Error", ex.getMessage());
                    return null;
                }

                return seleccionado;
            }
            return null;
        });

        dialogo.showAndWait();
        actualizarTabla(empresa.getEmpresa());

    } else {
        mostrarAlerta("Error", "Seleccione un empleado para modificar.");
    }
});

        calcularSueldoBtn.setOnAction(e -> {
            // Implementar cálculo de sueldo mensual
            empresa.CalcularSueldoMensualtoTXT();
            mostrarAlerta("Éxito", "El cálculo de sueldos ha sido generado correctamente.");
        });

        exportarBinarioBtn.setOnAction(e -> {
            Serializador.serializarEmpresa(empresa);
            empresa = null;
            empresa = Serializador.deserializarEmpresa();
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
