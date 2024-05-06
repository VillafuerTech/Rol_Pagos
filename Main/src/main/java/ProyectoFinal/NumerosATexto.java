package ProyectoFinal;

public class NumerosATexto {
   private static final String[] UNIDADES = {
        "", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez", "once", "doce", "trece", 
        "catorce", "quince", "dieciséis", "diecisiete", "dieciocho", "diecinueve"
    };
    
    private static final String[] DECENAS = {
        "", "", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa"
    };
    
    private static final String[] CENTENAS = {
        "", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos", "seiscientos", "setecientos", 
        "ochocientos", "novecientos"
    };

    private static final String MIL = "mil";
    private static final String MILLON = "millón";
    private static final String MILLONES = "millones";
    private static final String CERO = "cero";

    public static String numeroATexto(int num) {
        if (num == 0) return CERO;
        
        return convertirSeccion(num).trim();
    }

    private static String convertirSeccion(int num) {
        if (num < 20) {
            return UNIDADES[num];
        } else if (num < 100) {
            return DECENAS[num / 10] + (num % 10 != 0 ? " y " + UNIDADES[num % 10] : "");
        } else if (num < 1000) {
            return (num == 100 ? "cien" : CENTENAS[num / 100]) + (num % 100 != 0 ? " " + convertirSeccion(num % 100) : "");
        } else if (num < 1000000) {
            return (num / 1000 == 1 ? MIL : convertirSeccion(num / 1000) + " " + MIL) +
                    (num % 1000 != 0 ? " " + convertirSeccion(num % 1000) : "");
        } else {
            return (num / 1000000 == 1 ? MILLON : convertirSeccion(num / 1000000) + " " + MILLONES) +
                    (num % 1000000 != 0 ? " " + convertirSeccion(num % 1000000) : "");
        }
    }
 
}
