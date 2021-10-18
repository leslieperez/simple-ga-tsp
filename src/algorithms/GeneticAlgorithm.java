package algorithms;

public class GeneticAlgorithm {
	
	enum SelectionStrategy 
	/* Estrategias de seleccion de individuos
	 *   MU_LAMBDA: estrategia (mu, lambda)
	 *   MUPLUSLAMBDA estrategia (mu+lambda)
	 */
	{
		MULAMBDA,
		MUPLUSLAMBDA
	};
	
    /* Problema */
	static TSP problem;
	
	/* Parametro tamaño de la poblacion */
	int pop_size;
	
	/* Cantidad de hijos */
	int offspring_size;
	
	/* Seleccion de padres */
	Population.SelectionType pselection_type;
	
	/* Cruzamiento */
	Population.CrossoverType crossover_type;
	
	/* Mutacion */
	Population.MutationType mutation_type;
	
	/* Probabilidad de mutacion */
	Double mutation_prob;
	
    /* Estrategia de seleccion de la nueva poblacion */
	SelectionStrategy selection_strategy;
	
	/* Tipo de seleccion de la poblacion */
	Population.SelectionType gselection_type;
	
	/* Soluciones seleccionadas por elitismo */
	int elitism;
	
	/* Mejor tour */
	Tour best_tour;
	
	public GeneticAlgorithm (TSP _problem, Population.SelectionType _pselection_type,
		   Population.CrossoverType _crossover_type, Population.MutationType _mutation_type, 
		   SelectionStrategy _selection_strategy, Population.SelectionType _gselection_type,
		   int _pop_size, int _offspring_size, Double _mutation_prob) 
  /* FUNCTION: constructor de la clase GeneticAlgorithm
   * INPUT: instancia de TSP: _problem, tipo de seleccion de padres: _pselection_type, 
   *        tipo de crossover: _crossover_type, tipo de mutacion: _mutation_type, 
   *        estrategia de seleccion de problación: _selection_strategy, tipo de 
   *        seleccion de poblacion: _gselection_type, tamaño de la poblacion: _pop_size,
   *        cantidad de hijos: _offspring_size, probabilidad de mutacion: _mutation_prob
   * 
   */
	{
		problem   = _problem;
		pselection_type = _pselection_type;
		crossover_type   = _crossover_type;
		mutation_type = _mutation_type;
		mutation_prob = _mutation_prob;
		selection_strategy = _selection_strategy;
		gselection_type = _gselection_type;
		pop_size  = _pop_size;
		best_tour = null;
		offspring_size = _offspring_size;
		System.out.println("\nInicializando Algoritmo Genetico ...");
	};
	
    public void print_best_solution (boolean full)
    /* FUNCTION: print_best_solution
     * INPUT: booleano que indica si la solucion debe ser impresa completa: full
     * OUTPUT: ninguno
     */
    {
      System.out.println("\nMejor solucion Algoritmo Genetico: ");
      if (full) {
        best_tour.print();
      } else {
        best_tour.printCost();
      }
    };
	
	public void search (int max_evaluations, int max_iterations) 
    /* 
     * FUNCTION: search: funcion que ejecuta la busqueda del algoritmo genetico
     * INPUT: numero maximo de evaluaciones: max_evaluations, 
     *        numero maximo de iteraciones: max_iterations
     * OUTPUT: ninguno
     * COMMENT: esta funcion ejecuta la busqueda del algoritmo genetico
     *          desde una poblacion generada aleatoriamente. La mejora
     *          solución final puede ser encontrado en best_tour)
     */
	{
		int evaluation = 0;
		int iteration = 0;
		int[] parents;
        int updated = 0;
        int previous_fitness;
		
		/* Inicializar poblacion */
		System.out.println("Generando poblacion inicial ...");
		Population population = new Population(pop_size, problem);
		
		/* Inicializar poblacion de hijos */
		Population offspring = new Population (problem);
		
		/* Imprimir mejor solución encontrada */
		System.out.println("... mejor individuo ...");
		population.getBestTour().print();
        System.out.println("");
        
        /* Guardar la mejor solución en best_tour */
        if (best_tour == null) 
        	best_tour = new Tour(population.getBestTour());
        else {
        	best_tour.Copy(population.getBestTour());
        }
        
        /* bucle principal del algoritmo */
        System.out.println("\nComenzando busqueda \n");
		while (terminationCondition(evaluation, max_evaluations, iteration, max_iterations)) {
            updated = 0;
            previous_fitness =  best_tour.getCost();
            
			/* Aplicar cruzamiento para generar poblacion de hijos */
			while (offspring.size() < offspring_size) {
				parents = population.selectParents(pselection_type); 
				offspring.add(population.crossover(parents, crossover_type, false));
			}
            
			/* Revisar si algun hijo sin mutacion es la mejor solucion hasta el momento */
			if (offspring.getBestTour().getCost() < best_tour.getCost()) {
                updated = 1;
				best_tour.Copy(offsping.getBestTour());
			} 
			
			/* Aplicar mutacion */
	        offspring.mutation(mutation_prob, mutation_type, false);
 
			/* Revisar si algun hijo es la mejor solucion hasta el momento */
			if (offspring.getBestTour().getCost() < best_tour.getCost()) {
                updated = 1;
				best_tour.Copy(offsping.getBestTour());
			} 
            
            System.out.print("Generacion " + iteration );
            System.out.print(", mejor inicial: " + previous_fitness);
            
            if (updated == 1) {
             	/* Reportar el mejor hijo */
		        System.out.print(", mejor hijo: " + best_tour.getCost(), " (actualizado)\n");   
            } else {
		        System.out.print(", mejor hijo: " + offspring.getBestTour().getCost(), "\n");   
            }
			
		    /* Seleccionar nueva poblacion */
		    if (selection_strategy == SelectionStrategy.MULAMBDA) {
		    	/* Seleccionar solo desde los hijos */
		    	if (offspring_size > pop_size) { 
		    		/* Seleccionar desde los hijos */
		    		offspring.selectPopulation(pop_size, gselection_type);
		    	}
		    	population.Copy(offspring);

		    } else if (selection_strategy == SelectionStrategy.MUPLUSLAMBDA) {
		    	/* Seleccionar de los hijos y los padres */
		    	/* Unir ambas poblaciones (hijos y padres) */
		    	offspring.joinPopulation(population);
		    	/*Seleccionar de estas poblaciones */
		    	offspring.selectPopulation(pop_size, gselection_type);
		    	population.Copy(offspring);
		    }
			
			/* Incrementar contadores */
			iteration = iteration + 1;
			evaluation = evaluation + offspring_size;
			offspring.clear();
		}
	};

    
	public void test () 
    /* 
     * FUNCTION: test
     * INPUT: ninguno
     * OUTPUT: ninguno
     * COMMENT: esta es una funcion de prueba del algoritmo genetico
     */
	{
		int[] parents;
		
		/* Inicializar poblacion */
		System.out.println("Generando poblacion inicial ...");
		Population population = new Population(pop_size, problem);
		population.clear();
		int[] s1 = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,0};
		int[] s2 = {0,1,12,3,4,5,8,7,6,9,10,11,2,13,0};
		int[] s3 = {0,10,2,3,13,5,6,7,8,9,1,11,12,4,0};
		int[] s4 = {0,8,5,3,4,2,11,7,1,9,10,6,12,13,0};
		population.add(new Tour(s1, problem));
		population.add(new Tour(s2, problem));
		population.add(new Tour(s3, problem));
		population.add(new Tour(s4, problem));
		population.lprint();
		population.orderPopulation();
		population.lprint();
		
		/* Inicializar poblacion de hijos */
		Population offspring = new Population (problem);
		
		/* Imprimir mejor solución encontrada */
		System.out.println("... mejor individuo ...");
		population.getBestTour().print();
        System.out.println("");
        
        
        /* Seleccion de padres*/
		System.out.println("Seleccionando padres: ");
		parents = population.selectParents(pselection_type); 
		for (int i=0; i<parents.length; i++) {
			population.lprint(parents[i]);
		}
		
		/* Aplicar cruzamiento para generar poblacion de hijos */
		System.out.println("\nHijos obtenidos del cruzamiento: ");
		offspring.add(population.crossover(parents, crossover_type, true));
		offspring.lprint();

		/* Aplicar mutacion */
		System.out.println("Hijos obtenidos de la mutacion: ");
	    offspring.mutation(1.0, mutation_type, true);
	    offspring.lprint();
	    
	    System.out.println("Seleccion poblacion (mu+lambda): ");
	    offspring.joinPopulation(population);
    	offspring.selectPopulation(4, gselection_type);
    	population.Copy(offspring);
    	population.lprint();
	};

	private boolean terminationCondition (int evaluations, int max_evaluations, 
			                              int iterations, int max_iterations) 
    /* 
     * FUNCTION: terminationCondition
     * INPUT: numero actual de evaluaciones: evaluations, numero maximo de evaluaciones:
     *         max_evaluations, numero de iteraciones: iterations, numero maximo de 
     *         iteraciones: max_iterations
     * OUTPUT: booleano que indica si se debe continuar la ejecucion o no. 
     *         True: si se debe continuar, False: si no se debe continuar
     */	
	{
		
		/* criterio de termino de las evaluaciones */
		if (max_evaluations > 0) {
			if (evaluations >= max_evaluations) return (false);
		}
		if (max_iterations > 0) {
			if (iterations >= max_iterations) return (false);
		}
		
		return (true);
	};

	
}
