import java.util.*;

public class AlgoritmoGenetico {

    static int numCromosomas = 8;  // Número de cromosomas en la población
    static int numGenes = 5;       // Número de genes por cromosoma
    static int numGeneraciones = 10;  // Número de generaciones
    static Random rand = new Random();  // Generador de números aleatorios

    public static void main(String[] args) {
        List<int[]> poblacion = generarPoblacionInicial();
        mostrarPoblacion("Población Inicial", poblacion, null);

        for (int generacion = 0; generacion < numGeneraciones; generacion++) {
            System.out.println("\nGeneración " + (generacion + 1));

            // Evaluar aptitud
            double[] aptitudes = evaluarAptitud(poblacion);

            // Ordenar por aptitud
            ordenarPorAptitud(poblacion, aptitudes);

            // Cruzar cromosomas
            List<int[]> nuevaGeneracion = cruzarCromosomas(poblacion);
            mostrarPoblacion("Después del Cruzamiento", nuevaGeneracion, null);

            // Mutar solo dos cromosomas
            int[] mutados = mutarDosCromosomas(nuevaGeneracion);
            mostrarPoblacion("Después de la Mutación", nuevaGeneracion, mutados);

            // Reemplazar con nueva generación
            poblacion = nuevaGeneracion;

            // Promedio y mediana de la generación actual
            calcularPromedioYMediana(aptitudes);

            System.out.println("Generación " + (generacion + 1) + " completada.");
        }
    }

    public static List<int[]> generarPoblacionInicial() {
        List<int[]> poblacion = new ArrayList<>();
        for (int i = 0; i < numCromosomas; i++) {
            int[] cromosoma = new int[numGenes];
            for (int j = 0; j < numGenes; j++) {
                cromosoma[j] = rand.nextInt(2);  // Generar 0 o 1
            }
            poblacion.add(cromosoma);
        }
        return poblacion;
    }
            // Evaluar aptitud de cada cromosoma con la fórmula dada
               public static double[] evaluarAptitud(List<int[]> poblacion) {
        double[] aptitudes = new double[poblacion.size()];
        for (int i = 0; i < poblacion.size(); i++) {
            double valor = calcularValor(poblacion.get(i));
            aptitudes[i] = Math.pow((valor + 6), 2) + (valor * 14) - 18;
        }
        return aptitudes;
    }

    public static double calcularValor(int[] cromosoma) {
        double signo = cromosoma[0] == 1 ? -1 : 1;  // Si el primer gen es 1, el signo es negativo
        double valor = cromosoma[1] == 1 ? 0.5 : 0;  // Si el segundo gen es 1, el valor es 0.5
        return signo * valor;
    }

    public static void ordenarPorAptitud(List<int[]> poblacion, double[] aptitudes) {
        for (int i = 0; i < aptitudes.length - 1; i++) {
            for (int j = 0; j < aptitudes.length - i - 1; j++) {
                if (Math.abs(aptitudes[j]) > Math.abs(aptitudes[j + 1])) {
                    double tempAptitud = aptitudes[j];
                    aptitudes[j] = aptitudes[j + 1];
                    aptitudes[j + 1] = tempAptitud;

                    int[] tempCromosoma = poblacion.get(j);
                    poblacion.set(j, poblacion.get(j + 1));
                    poblacion.set(j + 1, tempCromosoma);
                }
            }
        }
    }

    public static List<int[]> cruzarCromosomas(List<int[]> poblacion) {
        List<int[]> nuevaGeneracion = new ArrayList<>();
        int n = poblacion.size();
        for (int i = 0; i < n / 2; i++) {
            int[] padre1 = poblacion.get(i);
            int[] padre2 = poblacion.get(n - 1 - i);
            int[] hijo1 = new int[numGenes];
            int[] hijo2 = new int[numGenes];

            // Cruzar en el punto medio
            for (int j = 0; j < numGenes / 2; j++) {
                hijo1[j] = padre1[j];
                hijo2[j] = padre2[j];
            }
            for (int j = numGenes / 2; j < numGenes; j++) {
                hijo1[j] = padre2[j];
                hijo2[j] = padre1[j];
            }

            nuevaGeneracion.add(hijo1);
            nuevaGeneracion.add(hijo2);
        }
        return nuevaGeneracion;
    }

    // Modificar solo dos cromosomas por generación y devolver los índices de los cromosomas mutados
    public static int[] mutarDosCromosomas(List<int[]> poblacion) {
        int[] indicesMutados = new int[2];

        // Elegir dos cromosomas aleatorios para mutar
        indicesMutados[0] = rand.nextInt(poblacion.size());
        do {
            indicesMutados[1] = rand.nextInt(poblacion.size());
        } while (indicesMutados[1] == indicesMutados[0]);  // Asegurarse de que no sea el mismo cromosoma

        // Mutar un gen aleatorio del primer cromosoma
        int[] cromosoma1 = poblacion.get(indicesMutados[0]);
        int genMutado1 = rand.nextInt(numGenes);
        cromosoma1[genMutado1] = cromosoma1[genMutado1] == 0 ? 1 : 0;

        // Mutar un gen aleatorio del segundo cromosoma
        int[] cromosoma2 = poblacion.get(indicesMutados[1]);
        int genMutado2 = rand.nextInt(numGenes);
        cromosoma2[genMutado2] = cromosoma2[genMutado2] == 0 ? 1 : 0;

        return indicesMutados;  // Retornar los índices de los cromosomas mutados
    }

    public static void calcularPromedioYMediana(double[] aptitudes) {
        double suma = 0;
        for (double aptitud : aptitudes) {
            suma += aptitud;
        }
        double promedio = suma / aptitudes.length;
        System.out.println("Promedio de aptitud: " + promedio);

        Arrays.sort(aptitudes);
        double mediana = aptitudes.length % 2 == 0
                ? (aptitudes[aptitudes.length / 2 - 1] + aptitudes[aptitudes.length / 2]) / 2.0
                : aptitudes[aptitudes.length / 2];
        System.out.println("Mediana de aptitud: " + mediana);
    }

    // Modificado para resaltar cromosomas mutados
    public static void mostrarPoblacion(String titulo, List<int[]> poblacion, int[] indicesMutados) {
        System.out.println("\n" + titulo + ":");
        for (int i = 0; i < poblacion.size(); i++) {
            if (indicesMutados != null && (i == indicesMutados[0] || i == indicesMutados[1])) {
                System.out.print("MUTADO: ");
            } else {
                System.out.print("Cromosoma " + (i + 1) + ": ");
            }

            for (int gen : poblacion.get(i)) {
                System.out.print(gen);
            }
            System.out.println();
        }
    }
}