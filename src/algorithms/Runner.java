package algorithms;

import algorithms.GeneticAlgorithm.SelectionStrategy;
import algorithms.Tour.InitialSolution;

/* Runner class
 * Clase principal que implementa algoritmos metaheuristicos para
 * resolver instancias del TSP (Travelling Salesman Problem) 
 *
 * @autor  Leslie Perez Caceres
 * @version 1.0
 *
 *
 * INF3144 Investigacion de Operaciones
 * 
 *  La clase GeneticAlgorithm implementa los operadores disponibles para
 *  definir un algoritmo genetico
 *  
 *  La clases Population implementa metodos para el manejo de la poblacion
 *  en el algoritmo genetico
 *
 *  La clase TSPlibReader implementa metodos para leer instancias de TSP
 *  en los diferentes formatos de TSPLIB. 
 *  
 *  La clase TSP provee las interfaces para obtener distancias e implementa
 *  metodos heuristicos para construir soluciones del TSP.
 *  
 *  La clase Tour consiste en un tour de TSP junto con su costo. 
 *
 *
 * Notas:
 *  - En este codigo las soluciones del TSP (en la clase Tour) se 
 *    representan con un arreglo de enteros de tamaño n+1, donde n 
 *    son los nodos (ciudades) del TSP y la ultima ciudad del tour 
 *    corresponde siempre a la primera ciudad.
 *    Ejemplo para tsp n = 5:
 *        2 4 3 1 0 2
 *        
 */


public class Runner {
    /* active esta variable para probar la funcion objetivo*/
    static boolean test_flag = false;
  
    static void print_header () {
        System.out.println ("*************************************************************");
        System.out.println ("*************************************************************");
        System.out.println ("**                                                         **");
        System.out.println ("**           TEMPLATE GENETIC ALGORITTHM FOR TSP           **");
        System.out.println ("**                                                         **");
        System.out.println ("**         ICI 3144: Investigacion de Operaciones          **");
        System.out.println ("**             Profesora: Leslie Perez Caceres             **");
        System.out.println ("**                                                         **");
        System.out.println ("**      Pontificia Universidad Catolica de Valparaiso      **");
        System.out.println ("**                                                         **");
        System.out.println ("*************************************************************");
        System.out.println ("*************************************************************");
    };

    public static void main(String[] args) {
        
      print_header ();
        
      /* Leer opciones desde la linea de comando */
      AlgorithmOptions opciones = new AlgorithmOptions(args);
        
      /* Inicializar objeto problema con la instancia entregada */
      TSP problem = new TSP(opciones.filename);
      
      /* Crear solver genetic algorithm */
      GeneticAlgorithm solver = new GeneticAlgorithm (problem, opciones.pselection_type,
    		           opciones.crossover_type, opciones.mutation_type, 
    		           opciones.selection_strategy, opciones.gselection_type,
    		           opciones.pop_size, opciones.offspring_size, opciones.mutation_prob);
      
      /* Codigo utilizado solo para probar operadores */
      if (test_flag) {
    	solver.test();
        System.exit(0);
      }
      
      /* Ejecutar la busqueda */
      solver.search( opciones.max_evaluations, opciones.max_iterations);
      
      /* Mostrar la mejor solucion encontrada */
      System.out.println("\nTerminando ejecucion ...");
      solver.print_best_solution (true);
     
   };

}
