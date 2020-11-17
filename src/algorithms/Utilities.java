package algorithms;
/* Utilities class
 * Class that contains several util methods that are used in this program algorithms
 *
 * @author  Leslie Perez Caceres
 * @version 1.0
 *
 * This code is based on:
 * Project website: http://adibaba.github.io/ACOTSPJava/
 */
import java.util.Random;

public class Utilities {
    
    private static Random random; /* ranadom number generator */
    static long seed; /* random number seed */
    
    static double mean(int[] values, int max)
    /*
     * FUNCTION: compute the average value of an integer array of length max
     * INPUT:  array, length of array
     * OUTPUT: average
     * (SIDE)EFFECTS: none
     */
    {
        int j;
        double m;
        
        m = 0.;
        for (j = 0; j < max; j++) {
            m += (double) values[j];
        }
        m = m / (double) max;
        return m;
    }
    
    static double meanr(double[] values, int max)
    /*
     * FUNCTION: compute the average value of a floating number array of length max
     * INPUT: pointer to array, length of array
     * OUTPUT: average
     * (SIDE)EFFECTS: none
     */
    {
        int j;
        double m;
        
        m = 0.;
        for (j = 0; j < max; j++) {
            m += values[j];
        }
        m = m / (double) max;
        return m;
    }
    
    static double std_deviation(int[] values, int max, double mean)
    /*
     * FUNCTION: compute the standard deviation of an integer array
     * INPUT: pointer to array, length of array, mean
     * OUTPUT: standard deviation
     * (SIDE)EFFECTS: none
     */
    {
        int j;
        double dev = 0.;
        
        if (max <= 1)
            return 0.;
        for (j = 0; j < max; j++) {
            dev += ((double) values[j] - mean) * ((double) values[j] - mean);
        }
        return Math.sqrt(dev / (double) (max - 1));
    }
    
    static double std_deviationr(double[] values, int max, double mean)
    /*
     * FUNCTION: compute the standard deviation of a floating number array
     * INPUT: pointer to array, length of array, mean
     * OUTPUT: standard deviation
     * (SIDE)EFFECTS: none
     */
    {
        int j;
        double dev;
        
        if (max <= 1)
            return 0.;
        dev = 0.;
        for (j = 0; j < max; j++) {
            dev += ((double) values[j] - mean) * ((double) values[j] - mean);
        }
        return Math.sqrt(dev / (double) (max - 1));
    }
    
    static int best_of_vector(int[] values, int l)
    /*
     * FUNCTION: return the minimum value in an integer value
     * INPUT: pointer to array, length of array
     * OUTPUT: smallest number in the array
     * (SIDE)EFFECTS: none
     */
    {
        int min, k;
        
        k = 0;
        min = values[k];
        for (k = 1; k < l; k++) {
            if (values[k] < min) {
                min = values[k];
            }
        }
        return min;
    }
    
    static int worst_of_vector(int[] values, int l)
    /*
     * FUNCTION: return the maximum value in an integer value
     * INPUT: pointer to array, length of array
     * OUTPUT: largest number in the array
     * (SIDE)EFFECTS: none
     */
    {
        int max, k;
        
        k = 0;
        max = values[k];
        for (k = 1; k < l; k++) {
            if (values[k] > max) {
                max = values[k];
            }
        }
        return max;
    }
    
    static double quantil(int v[], double q, int l)
    /*
     * FUNCTION: return the q-quantil of an ordered integer array
     * INPUT: one array, desired quantil q, length of array
     * OUTPUT: q-quantil of array
     * (SIDE)EFFECTS: none
     */
    {
        int i, j;
        double tmp;
        
        tmp = q * (double) l;
        if ((double) ((int) tmp) == tmp) {
            i = (int) tmp;
            j = (int) (tmp + 1.);
            return ((double) v[i - 1] + (double) v[j - 1]) / 2.;
        } else {
            i = (int) (tmp + 1.);
            return v[i - 1];
        }
    }
    
    static void swap(int v[], int i, int j)
    /*
     * FUNCTION: auxiliary routine for sorting an integer array
     * INPUT: array, two indices
     * OUTPUT: none
     * (SIDE)EFFECTS: elements at position i and j of array are swapped
     */
    {
        int tmp;
        
        tmp = v[i];
        v[i] = v[j];
        v[j] = tmp;
    }
    
    static void sort(int v[], int left, int right)
    /*
     * FUNCTION: recursive routine (quicksort) for sorting an array
     * INPUT: one array, two indices
     * OUTPUT: none
     * (SIDE)EFFECTS: elements at position i and j of the two arrays are swapped
     */
    {
        int k, last;
        
        if (left >= right)
            return;
        swap(v, left, (left + right) / 2);
        last = left;
        for (k = left + 1; k <= right; k++)
            if (v[k] < v[left])
                swap(v, ++last, k);
        swap(v, left, last);
        sort(v, left, last);
        sort(v, last + 1, right);
    }
    
    static void swap2(int v[], int v2[], int i, int j)
    /*
     * FUNCTION: auxiliary routine for sorting an integer array
     * INPUT: two arraya, two indices
     * OUTPUT: none
     * (SIDE)EFFECTS: elements at position i and j of the two arrays are swapped
     */
    {
        int tmp;
        
        tmp = v[i];
        v[i] = v[j];
        v[j] = tmp;
        tmp = v2[i];
        v2[i] = v2[j];
        v2[j] = tmp;
    }
    
    static void swap2(long v[], int v2[], int i, int j)
    /*
     * FUNCTION: auxiliary routine for sorting an integer array
     * INPUT: two arraya, two indices
     * OUTPUT: none
     * (SIDE)EFFECTS: elements at position i and j of the two arrays are swapped
     */
    {
        long tmp1;
        int tmp2;
        
        tmp1 = v[i];
        v[i] = v[j];
        v[j] = tmp1;
        tmp2 = v2[i];
        v2[i] = v2[j];
        v2[j] = tmp2;
    }
    
    static void sort2(int v[], int v2[], int left, int right)
    /*
     * FUNCTION: recursive routine (quicksort) for sorting one array; second
     * arrays does the same sequence of swaps
     * INPUT: two arrays, two indices
     * OUTPUT: none
     * (SIDE)EFFECTS: elements at position i and j of the two arrays are swapped
     */
    {
        int k, last;
        
        if (left >= right)
            return;
        swap2(v, v2, left, (left + right) / 2);
        last = left;
        for (k = left + 1; k <= right; k++)
            if (v[k] < v[left])
                swap2(v, v2, ++last, k);
        swap2(v, v2, left, last);
        sort2(v, v2, left, last);
        sort2(v, v2, last + 1, right);
    }
    
    static void sort2(long v[], int v2[], int left, int right)
    /*
     * FUNCTION: recursive routine (quicksort) for sorting one array; second
     * arrays does the same sequence of swaps
     * INPUT: two arrays, two indices
     * OUTPUT: none
     * (SIDE)EFFECTS: elements at position i and j of the two arrays are swapped
     */
    {
        int k, last;
        
        if (left >= right)
            return;
        swap2(v, v2, left, (left + right) / 2);
        last = left;
        for (k = left + 1; k <= right; k++)
            if (v[k] < v[left])
                swap2(v, v2, ++last, k);
        swap2(v, v2, left, last);
        sort2(v, v2, left, last);
        sort2(v, v2, last + 1, right);
    }

    static void sort2(long v[], int v2[])
    /*
     * FUNCTION: recursive routine (quicksort) for sorting one array; second
     * arrays does the same sequence of swaps
     * INPUT: two arrays
     * OUTPUT: none
     * (SIDE)EFFECTS: elements at position i and j of the two arrays are swapped
     */
    {
        int k, last;
        int left = 0;
        int right = v.length-1;
        
        if (left >= right)
            return;
        swap2(v, v2, left, (left + right) / 2);
        last = left;
        for (k = left + 1; k <= right; k++)
            if (v[k] < v[left])
                swap2(v, v2, ++last, k);
        swap2(v, v2, left, last);
        sort2(v, v2, left, last);
        sort2(v, v2, last + 1, right);
    }
   
    static double ran01()
    /*
     * FUNCTION: generate a random number that is uniformly distributed in [0,1]
     * INPUT:
     * OUTPUT: random number uniformly distributed in [0,1]
     */
    {
        if (random == null) {
            random = new Random(seed);
        }
        
        return random.nextDouble();
    }
    
    static int random_n(int N)
    /*
     * FUNCTION: generate a random number that is uniformly distributed in [0,1]
     * INPUT:
     * OUTPUT: random number uniformly distributed in [0,1]
     */
    {
        if (random == null) {
            random = new Random(seed);
        }
        
        return random.nextInt(N);
    }
    
    static int random_number()
    /*
     * FUNCTION: generate an integer random number
     * INPUT:
     * OUTPUT: integer random number uniformly distributed in {0,2147483647}
     */
    {
        if (random == null) {
            random = new Random(seed);
        }
        
        return random.nextInt(2147483647);
    }
    
    static double dtrunc(double x) {
        /*
         * FUNCTION: truncate a double number
         * OUTPUT: integer number
         */
        int k;
        k = (int) x;
        x = (double) k;
        return x;
    }

    

    
}

