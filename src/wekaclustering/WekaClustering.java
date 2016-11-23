/*
 * File         : WekaClustering.java
 * Author       : Rahman Adianto
 * Description  : Main Program
 */
package wekaclustering;

import weka.core.Instances;
import wekaclustering.clusterers.MyKMeans;
import weka.core.converters.ConverterUtils.DataSource;
import wekaclustering.clusterers.MyAgnes;

public class WekaClustering {

    public static void main(String[] args) {
        
        // Set file path
        String filename = "test/data/weather.nominal.arff";
        
        // Setup clusterer
        MyAgnes clusterer = new MyAgnes(1.4);
        
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
            
            System.out.println(clusterer);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
