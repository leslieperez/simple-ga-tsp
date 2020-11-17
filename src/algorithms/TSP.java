package algorithms;
/* TSP class
 * Clase que lee una instancia, evalua soluciones
 * del TSP y provee metodos para crear soluciones
 *
 * @autor  Leslie Perez Caceres
 * @version 1.0
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class TSP {
    /* nombre de la instancia */
    private String name;
    /* numero de nodos */
    private int n;
    /* matriz de distancia: distance[i][j] distancia de nodos i a j */
    private int[][] distance;
    /* lista de vecinos mas cercanos: nn_list[i][j] para cada nodo
       i una lista de vecinos ordenados*/
    private int[][] nn_list;
    /* clase que lee una instancia del tsp */
    private TSPlibReader tsplib_instance;
    
    public TSP (String tsp_file_name)
    /*
     * FUNCTION: Constuctor clase TSP
     * INPUT: Ruta al archivo de la instancia
     */
    {
        name = tsp_file_name;
        /* leer instancia desde un archivo TSPlib*/
        tsplib_instance = new TSPlibReader(tsp_file_name);
        /* obtener matriz de distancia */
        distance = tsplib_instance.get_distance_matrix();
        /* obtener lista de vecinos mas cercanos*/
        nn_list  = tsplib_instance.get_nn();
        /* obtener tamaño de la instancia */
        n = tsplib_instance.get_size();
        /* Descomente para imprimir matriz de distancias*/
        //print_distances();

    };
    
    public int getSize()
    /*
     * FUNCTION: Obtiene el numero de nodos
     * OUTPUT: numero de nodos
     */
    {
        return n;
    };
    
    public void print_distances()
    /*
     * FUNCTION: imprimir la matriz de distancia entre nodos
     * INPUT: ninguno
     * OUTPUT: ninguno
     */
    {
        for (int i=0; i < n ; i++ ) {
            System.out.print("Desde " + i + ": ");
            for (int j=0; j < n ; j++ ) {
                System.out.print (distance[i][j] + " ");
            }
            System.out.println("");
        }
    };
    
    public int get_distance (int i, int j)
    /*
     * FUNCTION: get_distance
     * INPUT: indexes i, j of the nodes to obtain the distance
     * OUTPUT: distance between nodes i, j
     */
    {
        return distance[i][j];
    };
     
    public long compute_tour_length (int[] t)
    /* A IMPLEMENTAR
     * FUNCTION: computa el costo de un tour
     * INPUT: arreglo que representa un tour t del TSP. 
     *        Tamaño n+1, donde la ultima casilla del arreglo t[n] 
     *        corresponde al retorno a la ciudad inicial.
     * OUTPUT: costo del tour t
     */
    {
        int i;
        long tour_length = 0;
        for (i = 0; i < n; i++) {
            tour_length += distance[t[i]][t[i + 1]];
        }
        return tour_length;
    };
    
    public boolean tsp_check_tour (int[] t)
    /*
     * FUNCTION: revisa la correctitud de una solucion del tsp
     * INPUT: una solución del tsp
     * OUTPUT: TRUE si el tour es valido or FALSE si el tour no es valido
     */
    {
        int i;
        boolean error = false;
        int[] used = new int[n];
        int size = n;
        
        if (t == null) {
            System.err.println("error: permutacion no esta inicializada!");
            System.exit(1);
        }
        
        for (i = 0; i < size; i++) {
            if (used[t[i]] != 0) {
                System.err.println("Error: la solucion tiene dos veces el valor " + t[i] +
                                   "(ultima posicion: " + i  + ")");
                error = true;
            } else
                used[t[i]] = 1;
        }
        
        if (!error)
            for (i = 0; i < size; i++) {
                if (used[i] == 0) {
                    System.out.println("Error: posicion " + i + " en la solucion no esta ocupada");
                    error = true;
                }
            }
        
        if (!error)
            if (t[0] != t[size]) {
                System.err.println("Error: la permutacion no es un tour cerrado.");
                error = true;
            }
        
        if (!error)
            return true;
        System.err.println("Error: vector solucion:");
        for (i = 0; i < size; i++)
            System.err.println(t[i]);
        System.out.println();
        return false;
    };
    
    public void print_solution_and_cost(int[] t) {
        /*
         * FUNCTION: imprime una solucion y su costo
         * INPUT: arreglo de nodos (solucion del tsp)
         * OUTPUT: ninguno
         */
    	System.out.print("Solucion: ");
        for (int i=0; i < n+1; i++) {
            System.out.print(t[i] + " ");
        }
        System.out.println("\nCosto: " + compute_tour_length(t));
    };
    
    public void lprint_solution_and_cost(int[] t) {
        /*
         * FUNCTION: imprime una solucion y su costo
         *           en una linea
         * INPUT: arreglo de nodos (solucion del tsp)
         * OUTPUT: ninguno
         */
        for (int i=0; i < n+1; i++) {
            System.out.print(t[i] + " ");
        }
        System.out.print(", Costo: " + compute_tour_length(t)+"\n");
    };
    
    public int[] random_tour ()
    /*
     * FUNCTION: generar una solucion aleatoria
     * INPUT: ninguno
     * OUTPUT: un arreglo de tamaño nodos+1 con el tour aleatorio
     */
    {
        ArrayList<Integer> nodes = new ArrayList<Integer>();
        int [] tour = new int[n+1];
        int index;
        
        /* generar la lista de nodos disponibles */
        for (int i = 0; i < n; i++)
            nodes.add(Integer.valueOf(i));
        
        for (int i = 0; i < n; i++) {
            index = Utilities.random_n(nodes.size()); /* seleccionar el siguiente nodo */
            tour[i] = ((int) nodes.get((index)));
            nodes.remove(index); /* eliminar el nodo seleccionado de la lista de nodos disponibles */
        }
        tour[n] = tour[0]; /* ultimo nodo retorno al nodo inicial */
        return tour;
    };
    
    public int[] greedy_nearest_n (int start)
    /*
     * FUNCTION: generar una solucion del tsp usando la heuristica del
     *           nodo mas cercano comenzando del nodo start
     * INPUT: numero del nodo de inicio para la construccion del tour.
     *        Use start=-1 para seleccionar un nodo aleatorio
     * OUTPUT: un arreglo de tamaño nodos+1 con el tour
     */
    {
        int [] tour = new int[n+1];
        boolean [] selected = new boolean[n];
        boolean flag;
        Arrays.fill(selected, false);
        
        /* Nodo inicial si start<0 */
        if (start<0) start = Utilities.random_n(n);
        tour[0] = start; /* nodo inicial */
        selected[start] = true;
        
        for (int i=1; i<n; i++) { /* ciclo para seleccionar los nodos del tour */
            flag = true;
            /* ciclo para selccionar los nodos disponibles desde la lista de vecinos mas
               cercanos */
            for (int j=0; j<n && flag; j++) {
                /* revisar si el nodo ya esta seleccionado */
                if (!selected[nn_list[tour[i-1]][j]]) {
                    tour[i] = nn_list[tour[i-1]][j];
                    selected[nn_list[tour[i-1]][j]] = true;
                    flag = false;
                }
            }
        }
        tour[n] = tour[0]; /* nodo final */
        return tour;
    };
  
    public int[] deterministic_tour ()
    /*
     * FUNCTION: generar una solucion determinista
     * INPUT: ninguno
     * OUTPUT: un arreglo de tamaño nodos+1 con el tour determinista
     */
    {
        ArrayList<Integer> nodes = new ArrayList<Integer>();
        int [] tour = new int[n+1];
        int index;
        
        for (int i = 0; i < n; i++) {
            tour[i] = i;
        }
        tour[n] = tour[0]; /* ultimo nodo retorno al nodo inicial */
        return tour;
    };
  
    
}
