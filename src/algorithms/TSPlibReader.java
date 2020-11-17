package algorithms;
/* TSPLlibInstance class
 * Clase que lee una instancia en el formato de TSPlib
 *
 * @autor  Leslie Perez Caceres
 * @version 1.0
 *
 * Codigo basado en: http://adibaba.github.io/ACOTSPJava/
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class TSPlibReader {
  static class point {
    double x;
    double y;
  };

  /* Tipos de instancias en TSPlib */
  enum Distance_type {
    EUC_2D, CEIL_2D, GEO, ATT
  };

  /* arreglo de estructuras que contiene las coordenadas */
  private point[] nodeptr;  
  /* variable that indicates distance type */
  private Distance_type distance_type;
  /* matriz de distancia: distance[i][j] distancia de nodos i a j */
  private int[][] distance;
  /* lista de vecinos mas cercanos: nn_list[i][j] para cada nodo i una lista de vecinos ordenados */
  private int[][] nn_list;
  /* numero de nodos */
  private int n; 
  /* instance file name */
  private String name;


  public TSPlibReader (String tsp_file_name)
  /*
   * FUNCTION: Constructor clase TSPlibReader class
   * INPUT: Ruta al archivo de la instancia
   */
  {
    try {
      /* leer instancia desde un archivo */
      nodeptr = read_etsp(tsp_file_name); 
    } catch (IOException e) {
      System.err.println("No se pudo leer el archivo. " + e.getMessage());
      System.exit(1);
    }
    /* obtener la matriz de distancias */
    compute_distances();
     /* generar listas de vecinos ordenados */
    compute_nn_lists();
    System.out.println("  instancia "+ name + " con " + n + " nodos");
  }
    
  private point[] read_etsp(String tsp_file_name) throws IOException
    /*
     * FUNCTION: read_etsp: lectura y parsing de instancia TSPlib
     * INPUT: ruta al archivo de instancia
     * OUTPUT: arreglo de coordenadas
     * COMMENTS: archivo de instancia debe estar en formato TSPLIB
     */
    {
    String buf;
    int i=0;
    point[] nodeptr = null;
    boolean found_coord_section = false;
        
    if (tsp_file_name == null) {
      System.err.println("Instacia no especificada, abortando...");
      System.exit(1);
    }
        
    if (!new File(tsp_file_name).canRead()) {
      System.err.println("No se puede leer el archivo " + tsp_file_name);
      System.exit(1);
    }
        
    System.out.println("\nLeyendo archivo TSPlib " + tsp_file_name + " ... ");
    Reader reader = new InputStreamReader(new FileInputStream(tsp_file_name), "UTF8");
    BufferedReader bufferedReader = new BufferedReader(reader);
    String line = bufferedReader.readLine();
    while (line != null) {
      if (line.trim().startsWith("EOF"))
        break;
      if (!found_coord_section) {
        if (line.startsWith("NAME")) {
          name = line.split(":")[1].trim();
        } else if (line.startsWith("COMMENT")) {
        } else if (line.startsWith("TYPE") && !line.contains("TSP")) {
          System.err.println("Instancia no esta en el formato TSPLIB !!");
          System.exit(1);
        } else if (line.startsWith("DIMENSION")) {
          n = Integer.parseInt(line.split(":")[1].trim());
          nodeptr = new point[n];
          assert (n > 2 && n < 6000);
        } else if (line.startsWith("DISPLAY_DATA_TYPE")) {
        } else if (line.startsWith("EDGE_WEIGHT_TYPE")) {
          buf = line.split(":")[1].trim();
          if (buf.equals("EUC_2D")) {
            distance_type = Distance_type.EUC_2D;
          } else if (buf.equals("CEIL_2D")) {
            distance_type = Distance_type.CEIL_2D;
          } else if (buf.equals("GEO")) {
            distance_type = Distance_type.GEO;
          } else if (buf.equals("ATT")) {
            distance_type = Distance_type.ATT;
          } else {
            System.err.println("EDGE_WEIGHT_TYPE " + buf + " no implementado en la clase.");
            System.exit(1);
          }
        }
      } else {
        String[] city_info = line.trim().split("\\s+");
        nodeptr[i] = new point();
        nodeptr[i].x = Double.parseDouble(city_info[1]);
        nodeptr[i].y = Double.parseDouble(city_info[2]);
        i++;
      }
            
      if (line.startsWith("NODE_COORD_SECTION"))
        found_coord_section = true;
      line = bufferedReader.readLine();
    }
    if (!found_coord_section) {
      System.err.println("Error ocurrio al buscar el inicio de las coordenadas !!");
      System.exit(1);
    }
    bufferedReader.close();
    return (nodeptr);
  };

  private int round_distance(int i, int j)
    /*
     * FUNCTION: round_distance: computa la distacia Euclidiana
     *           (redondeada al siguiente entero) entre dos nodos
     * INPUT: indices de dos nodos
     * OUTPUT: distancia entre dos nodos
     * COMMENTS: para una definicion de como calcular esta distancia vea
     *           TSPLIB
     */
  {
    double xd = nodeptr[i].x - nodeptr[j].x;
    double yd = nodeptr[i].y - nodeptr[j].y;
    double r = Math.sqrt(xd * xd + yd * yd) + 0.5;
    return (int) r;
  };
    
  private int ceil_distance(int i, int j)
    /*
     * FUNCTION: ceil_distance: computa la distacia Euclidiana
     *           (usando funcion techo) entre dos nodos
     * INPUT: indices de dos nodos
     * OUTPUT: distancia entre dos nodos
     * COMMENTS: para una definicion de como calcular esta distancia vea
     *           TSPLIB
     */
    {
    double xd = nodeptr[i].x - nodeptr[j].x;
    double yd = nodeptr[i].y - nodeptr[j].y;
    double r = Math.sqrt(xd * xd + yd * yd);
    return (int) Math.ceil(r);
  }
    
  private int geo_distance(int i, int j)
    /*
     * FUNCTION: geo_distance: computa la distancia geometrica (redondeada
     *           al siguiente entero) entre dos nodos
     * INPUT: indices de dos nodos
     * OUTPUT: distancia entre dos nodos
     * COMMENTS: adaptada desde el codigo de concorde. Para una
     *           definicion de como calcular esta distancia vea TSPLIB
     */
    {
    double deg, min;
    double lati, latj, longi, longj;
    double q1, q2, q3;
    int dd;
    double x1 = nodeptr[i].x, x2 = nodeptr[j].x, y1 = nodeptr[i].y, y2 = nodeptr[j].y;
       
    deg = Utilities.dtrunc(x1);
    min = x1 - deg;
    lati = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
    deg = Utilities.dtrunc(x2);
    min = x2 - deg;
    latj = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
        
    deg = Utilities.dtrunc(y1);
    min = y1 - deg;
    longi = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
    deg = Utilities.dtrunc(y2);
    min = y2 - deg;
    longj = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
        
    q1 = Math.cos(longi - longj);
    q2 = Math.cos(lati - latj);
    q3 = Math.cos(lati + latj);
    dd = (int) (6378.388 * Math.acos(0.5 * ((1.0 + q1) * q2 - (1.0 - q1) * q3)) + 1.0);
    return dd;
  }
    
  private int att_distance(int i, int j)
    /*
     * FUNCTION: att_distance: computa la distancia ATT (redondeada
     *           al siguiente entero) entre dos nodos
     * INPUT: indices de dos nodos
     * OUTPUT: distancia entre dos nodos
     * COMMENTS: para una definicion de como calcular esta distancia vea
     *           TSPLIB
     */
    {
    double xd = nodeptr[i].x - nodeptr[j].x;
    double yd = nodeptr[i].y - nodeptr[j].y;
    double rij = Math.sqrt((xd * xd + yd * yd) / 10.0);
    double tij = Utilities.dtrunc(rij);
    int dij;
       
    if (tij < rij)
      dij = (int) tij + 1;
    else
      dij = (int) tij;
    return dij;
  }
    
  private void compute_distances()
    /*
     * FUNCTION: compute_distances: computa las distancias entre todas los
     *           nodos
     * INPUT: ninguno
     * OUTPUT: guarda las distancias en una matrix en la variable distance
     */
    {
    int i, j;
    int matrix[][] = new int[n][n];
    for (i = 0; i < n; i++) {
      for (j = 0; j < n; j++) {
        if (distance_type == Distance_type.ATT) {
          matrix[i][j] = att_distance(i, j);
        } else if (distance_type == Distance_type.CEIL_2D) {
          matrix[i][j] = ceil_distance(i, j);
        } else if (distance_type == Distance_type.EUC_2D) {
          matrix[i][j] = round_distance(i, j);
        } else if (distance_type == Distance_type.GEO) {
          matrix[i][j] = geo_distance(i, j);
        }
      }
    }
    distance = matrix;
  };

  private void compute_nn_lists()
  /*
   * FUNCTION: compute_nn_lists: computa la lista de vecinos mas
   *           cercanos de cada nodo
   * INPUT: ninguno
   * OUTPUT: guarda los vecinos mas cercanos en una matriz en la variable nn_list
   */
  {
      int i, node, nn;
      int[] distance_vector = new int[n];
      int[] help_vector = new int[n];
      nn = n - 1;
      int[][] m_nnear = new int[n][nn];
      
      for (node = 0; node < n; node++) { /* compute cnd-sets for all node */
          for (i = 0; i < n; i++) { /* Copy distances from nodes to the others */
              distance_vector[i] = distance[node][i];
              help_vector[i] = i;
          }
          distance_vector[node] = Integer.MAX_VALUE; /* city is not nearest neighbour */
          Utilities.sort2(distance_vector, help_vector, 0, n - 1);
          for (i = 0; i < nn; i++) {
              m_nnear[node][i] = help_vector[i];
          }
      }
      nn_list = m_nnear;
  };

  public int[][] get_distance_matrix () {
	  return(distance);
  };
  
  public int[][] get_nn () {
	  return(nn_list);
  };
  
  public int get_size() {
	  return (n);
  };
};

  
