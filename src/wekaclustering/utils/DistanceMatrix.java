/*
 * File         : DistanceMatrix.java
 * Author       : Rahman Adianto
 * Description  : Implement Distance Matrix 2D - Upper triangle
 *                A B C D E
 *              A 0 1 2 3 4
 *              B   0 3 3 3
 *              C     0 2 5
 *              D       0 7
 *              E         0
 */
package wekaclustering.utils;

import java.util.Vector;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class DistanceMatrix {
    
    private Vector<Vector<Double>> distanceMatrix = new Vector<Vector<Double>>();
    private int numberInstance = 0;
    private EuclideanDistance distanceFunction = new EuclideanDistance();;

    public DistanceMatrix(Instances data) {
        
        this.distanceFunction.setInstances(data);
        
        // Build distance matrix
        buildMatrix(data);
    }
    
    private void buildMatrix(Instances data) {
        
        for (int i = 0; i < data.numInstances()-1; i++) {
            Vector<Double> v = new Vector<Double>();
            for (int j = i + 1; j < data.numInstances(); j++) {
                v.add(distanceFunction.distance(data.instance(i), data.instance(j)));
            }
            distanceMatrix.add(v);
        }
    }
    
    public Vector<Vector<Double>> getDistanceMatrix() {
        return distanceMatrix;
    }
    
}
