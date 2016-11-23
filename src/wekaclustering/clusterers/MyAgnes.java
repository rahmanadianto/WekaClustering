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
    private String stringBuilder;
    Node[] node;
    
    public MyAgnes(){
        numCluster = 2;
        stringBuilder = "";
    }
    
    public MyAgnes(double threshold){
        this.threshold = threshold;
        stringBuilder = "";
    }

    @Override
    public void buildClusterer(Instances inpIns) throws Exception {
        
        instances = new Instances(inpIns);
        disFunction = new EuclideanDistance(instances);
        
        // ** Make minimum tree node
        // ** Make distance matrix
        DistanceMatrix distanceMatrix = new DistanceMatrix(instances);
        //System.out.print(distanceMatrix.toString());
        
        // ** Cari nilai paling kecil terurut hingga ketemu
        // ** Init nilai cluster
        ArrayList<AgnesCluster> clusters = new ArrayList<AgnesCluster>();
        for (int i = 0; i < instances.numInstances(); i++){
             Vector<Integer> temp = new Vector<Integer>();
             temp.add(i);
             AgnesCluster cluster = new AgnesCluster(temp, distanceMatrix);
             clusters.add(cluster);
        }
        // ** Menyatukan cluster satu demi satu
        Integer counter = 0;
        while(clusters.size() != 1){
            // ** Find min distance between cluster 
            counter++;
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
            stringBuilder += counter.toString()+"  | ";
            for(int i = 0; i < clusters.size(); i++){
                stringBuilder += "[";
                int memberSize = clusters.get(i).getMembers().size();
                for(int j = 0; j < memberSize-1; j++){
                    stringBuilder += clusters.get(i).getMembers().get(j).toString()+", ";
                }
                stringBuilder += clusters.get(i).getMembers().get(memberSize-1).toString()+"]";
            }
            stringBuilder += "\n";
            
            System.out.print("minD: "+minDistance.toString()+"\n");
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
        return stringBuilder;
    }
    
}
