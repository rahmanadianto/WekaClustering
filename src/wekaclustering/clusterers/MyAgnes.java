/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekaclustering.clusterers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Vector;
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
    private int numCluster = 2;
    private double threshold = 0;
    private DistanceFunction disFunction;
    private String stringBuilder;
    private int iteration = 0;
    private ArrayList<AgnesCluster> clusters = new ArrayList<AgnesCluster>();
    
    public MyAgnes(){
        stringBuilder = "\nMy KMeans\n=========\n";
    }
    
    public MyAgnes(double threshold){
        this.threshold = threshold;
        stringBuilder = "\nMy KMeans\n=========\n";
    }

    @Override
    public void buildClusterer(Instances inpIns) throws Exception {
        
        instances = new Instances(inpIns);
        disFunction = new EuclideanDistance(instances);
        
        // ** Make distance matrix
        DistanceMatrix distanceMatrix = new DistanceMatrix(instances);
        
        // ** Cari nilai paling kecil terurut hingga ketemu
        // ** Init nilai cluster
        for (int i = 0; i < instances.numInstances(); i++){
             Vector<Integer> temp = new Vector<Integer>();
             temp.add(i);
             AgnesCluster cluster = new AgnesCluster(temp, distanceMatrix);
             clusters.add(cluster);
        }
        // ** Menyatukan cluster satu demi satu
        while(clusters.size() != 1){
            // ** Find min distance between cluster 
            iteration++;
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
            
            // ** make string information
            stringBuilder += iteration + "  | ";
            for(int i = 0; i < clusters.size(); i++){
                stringBuilder += "[";
                int memberSize = clusters.get(i).getMembers().size();
                for(int j = 0; j < memberSize-1; j++){
                    stringBuilder += clusters.get(i).getMembers().get(j).toString()+", ";
                }
                stringBuilder += clusters.get(i).getMembers().get(memberSize-1).toString()+"]";
            }
            stringBuilder += "\n";
            
            if(minDistance >= threshold){
                break;
            }
            
            // ** Combine into one cluster
            Vector<Integer> temp1 = clusters.get(finalI).getMembers();
            Vector<Integer> temp2 = clusters.get(finalJ).getMembers();
            temp1.addAll(temp2);
            clusters.add(new AgnesCluster(temp1, distanceMatrix));
            AgnesCluster removeC1 = clusters.get(finalI);
            AgnesCluster removeC2 = clusters.get(finalJ);
            clusters.remove(removeC1);
            clusters.remove(removeC2);
        }
                
    }

    @Override
    public int numberOfClusters() throws Exception {
        return numCluster;
    }
    
    @Override
    public String toString() {
        
        stringBuilder += "\nNumber of iterations: " + iteration + "\n";
        stringBuilder += "\nCluster instance: \n";
        
        for (int i = 0; i < clusters.size(); i++) {
            int n = clusters.get(i).getMembers().size();
            double p = (double) n / instances.numInstances() * 100;
            
            
            Double truncatedP = BigDecimal.valueOf(p)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
            
            stringBuilder += i + "   " + n;
            stringBuilder += " (" + truncatedP + "%)\n";
        }
        
        return stringBuilder;
    }
    
}
