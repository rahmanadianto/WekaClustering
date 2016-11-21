/*
 * File         : WekaClustering.java
 * Author       : Rahman Adianto
 * Description  : Main Program
 */
package wekaclustering;

import weka.core.Instances;
import wekaclustering.clusterers.MyKMeans;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaClustering {

    public static void main(String[] args) {
        
        // Set file path
        String filename = "test/data/cpu.arff";
        
        // Setup clusterer
        MyKMeans clusterer = new MyKMeans();
        clusterer.setNumSeed(2);
        clusterer.setNumCluster(2);
        clusterer.setMaxIteration(10);
        
        // Load dataset
        Instances data = null;
        try {
            DataSource source = new DataSource(filename);
            data = source.getDataSet();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Build Clusterer
        try {
            if (data != null) clusterer.buildClusterer(data);
            
            System.out.println("Number of iterations: " + clusterer.getIteration());
            System.out.println("Sum Square Error: " + clusterer.getSumSquaredErrors());
            for (int i = 0; i < clusterer.getNumCluster(); i++) {
                System.out.println("Cluster " + i + " : " + clusterer.getClusterSizes()[i]);
            }
            
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
