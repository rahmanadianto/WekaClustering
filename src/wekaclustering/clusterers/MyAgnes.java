/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekaclustering.clusterers;

import java.util.ArrayList;
import java.util.Vector;
import javafx.scene.Node;
import weka.clusterers.AbstractClusterer;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import wekaclustering.utils.AgnesCluster;
import wekaclustering.utils.DistanceMatrix;

/**
 *
 * @author fauzanrifqy
 */
public class MyAgnes extends AbstractClusterer{
    
    private Instances instances;
    private int numCluster;
    private double threshold;
    private DistanceFunction disFunction;
    Node[] node;
    
    public MyAgnes(){
        numCluster = 2;
    }
    
    public MyAgnes(double threshold){
        this.threshold = threshold;
    }

    @Override
    public void buildClusterer(Instances inpIns) throws Exception {
        
        instances = inpIns;
        disFunction = new EuclideanDistance(instances);
        
        // ** Make minimum tree node
            // ** Make distance matrix
        DistanceMatrix distanceMatrix = new DistanceMatrix(instances);
        
            // ** Cari nilai paling kecil terurut hingga ketemu
        // ** Init nilai cluster
        ArrayList<AgnesCluster> clusters = new ArrayList<AgnesCluster>();
        for (int i= 0; i < instances.size(); i++){
             Vector<Integer> temp = new Vector<Integer>();
             temp.add(i);
             AgnesCluster cluster = new AgnesCluster(temp, distanceMatrix);
        }
        // ** Menyatukan cluster satu demi satu
        while(clusters.size() != 1){
            // ** Find min distance between cluster  
            AgnesCluster init1 = clusters.get(0);
            AgnesCluster init2 = clusters.get(1);
            Double minDistance = init1.distance(init2, false);
            int finalI = 0, finalJ = 1;
            for(int i = 0; i < clusters.size(); i++){
                for(int j = i+1; j < clusters.size(); j++){
                    AgnesCluster cluster1 = clusters.get(i);
                    AgnesCluster cluster2 = clusters.get(j);
                    Double temp = cluster1.distance(cluster2, false);
                    if(minDistance > temp){
                        minDistance = temp;
                        finalI = i;
                        finalJ = j;
                    }
                }
            }
            
            if(minDistance >= threshold){
                break;
            }
            
            // ** Combine into one cluster
            Vector<Integer> temp1 = clusters.get(finalI).getMembers();
            Vector<Integer> temp2 = clusters.get(finalJ).getMembers();
            temp1.addAll(temp2);
            clusters.add(new AgnesCluster(temp1, distanceMatrix));
            clusters.remove(finalI);
            clusters.remove(finalJ);
        }
                
    }

    @Override
    public int numberOfClusters() throws Exception {
        return numCluster;
    }
    
}
