/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekaclustering.clusterers;

import java.util.ArrayList;
import javafx.scene.Node;
import weka.clusterers.AbstractClusterer;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author fauzanrifqy
 */
public class MyAgnes extends AbstractClusterer{
    
    private Instances instances;
    private int numCluster;
    private DistanceFunction disFunction;
    Node[] node;
    
    public MyAgnes(){
        numCluster = 2;
    }
    
    public MyAgnes(int numCluster){
        this.numCluster = numCluster;
    }

    @Override
    public void buildClusterer(Instances inpIns) throws Exception {
        
        instances = inpIns;
        disFunction = new EuclideanDistance(instances);
        ArrayList<ArrayList<Double>> distanceMatrix = new ArrayList<ArrayList<Double>>();
        node = new Node[inpIns.size()];
        
        // Make minimum tree node
            // Make distance matrix
        for(int i= 0; i<instances.size(); i++){
            Instance instance1 = instances.instance(i);
            ArrayList<Double> inDistanceMatrix = new ArrayList<Double>();
            for(int j= instances.size()-1; j>=i; j--){
                Instance instance2 = instances.instance(j);
                inDistanceMatrix.add(disFunction.distance(instance1, instance2));
            }
            distanceMatrix.add(inDistanceMatrix);
        }
        
            // Print Check
        for(int i = 0; i< distanceMatrix.size(); i++){
            ArrayList<Double> inDistanceMatrix = distanceMatrix.get(i);
            for(int j = i; j<inDistanceMatrix.size(); j++){
                System.out.print(inDistanceMatrix.get(j).toString()+" - ");
            }
            System.out.print("\n");
        }
        
            // Cari nilai paling kecil terurut hingga ketemu
        for(int i = 0; i< distanceMatrix.size(); i++){
            ArrayList<Double> inDistanceMatrix = distanceMatrix.get(i);
            for(int j = i; j<inDistanceMatrix.size(); j++){
                
                // while clusters.size != 1
                    // foreach cluster : clusters
                    // gabung cluster
                // else
                    // break
                    
                // 
            }
        }
            
        // potong node
            // definisi threshold
            // foreach node in nodes
                // find node-node yang jaraknya > threshold
                // potong
        
        // cluster
            // foreach group
                // foreach instance
                    // set instance.cluster = group.number
                
    }

    @Override
    public int numberOfClusters() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
