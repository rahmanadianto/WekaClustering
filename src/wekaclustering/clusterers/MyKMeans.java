/*
 * File         : MyKMeans.java
 * Author       : Rahman Adianto
 * Description  : Implement Simple KMeans clusterer using abstract clusterer class form Weka 3.8.0
 *                [Algorithm]
 *                1. Clusters the data into k groups where k  is predefined.
 *                2. Select k points at random as cluster centers.
 *                3. Assign objects to their closest cluster center according to the Euclidean distance function.
 *                4. Calculate the centroid or mean of all objects in each cluster.
 *                5. Repeat steps 2, 3 and 4 until the same points are assigned to each cluster in consecutive rounds.
 */
package wekaclustering.clusterers;

import java.util.HashMap;
import java.util.Random;
import weka.classifiers.rules.DecisionTableHashKey;
import weka.clusterers.AbstractClusterer;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.Instance;

public class MyKMeans extends AbstractClusterer {
    
    // Distance function used in this MyKMeans Clusterer
    private EuclideanDistance distanceFunction = new EuclideanDistance();
    // Current iteration
    private int iteration = 0;
    // Threshold number of iteration
    private int maxIteration = 500;
    // Number of initial seed
    private int numSeed = 2;
    // Number of cluster to build
    private int numCluster = 2;
    // Save centroid of each cluster
    private Instances clusterCentroids;
    // Save cluster of each instance
    private int[] clusterAssignments;
    // Save number count of each cluster
    private int[] clusterSizes;
    // Save Square Error of each cluster
    private double[] squaredErrors;
    
    public MyKMeans() {
        super();
    }

    public int getIteration() {
        return iteration;
    }

    public int getMaxIteration() {
        return maxIteration;
    }

    public int getNumSeed() {
        return numSeed;
    }

    public int getNumCluster() {
        return numCluster;
    }

    public Instances getClusterCentroids() {
        return clusterCentroids;
    }

    public int[] getClusterAssignments() {
        return clusterAssignments;
    }

    public int[] getClusterSizes() {
        return clusterSizes;
    }

    public double getSumSquaredErrors() {
        double sum = 0;
        
        for (int i = 0; i < squaredErrors.length; i++) {
            sum += squaredErrors[i];
        }
        
        return sum;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    public void setNumSeed(int numSeed) {
        this.numSeed = numSeed;
    }

    public void setNumCluster(int numCluster) {
        this.numCluster = numCluster;
    }

    @Override
    public Capabilities getCapabilities() {
        Capabilities capabilities = super.getCapabilities();
        
        capabilities.disableAll();
        capabilities.enable(Capability.NO_CLASS);
        capabilities.enable(Capability.NUMERIC_ATTRIBUTES);
        
        // Hold
        //capabilities.enable(Capability.NOMINAL_ATTRIBUTES);
        //capabilities.enable(Capability.MISSING_VALUES);
        
        return capabilities;
    }
    
    @Override
    public void buildClusterer(Instances data) throws Exception {
        // Test capabilities agains data, throw Exception if failed
        getCapabilities().testWithFail(data);
        
        iteration = 0;
        Instances instances = new Instances(data);
        instances.setClassIndex(-1);
        clusterAssignments = new int[instances.numInstances()];
        distanceFunction.setInstances(instances);
        
        initCentroids(instances);
        
        boolean converged = false;
        int emptyClusterCount;
        Instances[] tempInstances = new Instances[numCluster];
        squaredErrors = new double[numCluster];
        
        while (!converged) {
            
            emptyClusterCount = 0;
            iteration++;
            converged = true;
            
            // Assign every instance to cluster
            for (int i = 0; i < instances.numInstances(); i++) {
                Instance toCluster = instances.instance(i);
                int newCluster = clusterProcessedInstance(toCluster, true);
                if (newCluster != clusterAssignments[i]) {
                    converged = false;
                }
                clusterAssignments[i] = newCluster;
            }
            
            // Update centroid
            clusterCentroids = new Instances(instances, numCluster);
            for (int i = 0; i < numCluster; i++) {
                tempInstances[i] = new Instances(instances, 0);
            }
            for (int i = 0; i < instances.numInstances(); i++) {
                tempInstances[clusterAssignments[i]].add(instances.instance(i));
            }
            for (int i = 0; i < numCluster; i++) {
                if (tempInstances[i].size() == 0) {
                    emptyClusterCount++;
                }
                else {
                    updateCentroid(i, tempInstances[i], true);
                }
            }
            
            // Max Iteration
            if (iteration == maxIteration) {
                converged = true;
            }
            
            if (emptyClusterCount > 0) {
                numCluster -= emptyClusterCount;
                
                if (converged) {
                    // Reduced number of cluster
                    Instances[] t = new Instances[numCluster];
                    int index = 0;
                    for (int i = 0; i < tempInstances.length; i++) {
                        if (tempInstances[i].size() > 0) {
                            t[index] = tempInstances[i];
                            index++;
                        }
                    }
                    
                    tempInstances = t;
                }
                else {
                    tempInstances = new Instances[numCluster]; 
                }
            }
            
            if (!converged) {
                squaredErrors = new double[numCluster];
            }
            
        }
        
        clusterSizes = new int[numCluster];
        for (int i = 0; i < numCluster; i++) {
            clusterSizes[i] = tempInstances[i].numInstances();
        }
        
        distanceFunction.clean();
    }
    
    private void initCentroids(Instances instances) throws Exception {
        clusterCentroids = new Instances(instances, numCluster);
        
        Random random = new Random(numSeed);
        int instanceIndex;
        HashMap initialCentroid = new HashMap();
        DecisionTableHashKey hashKey = null;

        Instances initialInstances = new Instances(instances);
        
        int i = initialInstances.numInstances() - 1;
        Boolean breakCondition = false;
        while (!breakCondition && i >= 0) {
            
            instanceIndex = random.nextInt(i + 1);
            hashKey = new DecisionTableHashKey(initialInstances.instance(instanceIndex),
                    initialInstances.numAttributes(), true);
            
            if (!initialCentroid.containsKey(hashKey)) {
                clusterCentroids.add(initialInstances.instance(instanceIndex));
                initialCentroid.put(hashKey, null);
            }
            initialInstances.swap(i, instanceIndex);
            
            if (clusterCentroids.numInstances() == numCluster) breakCondition = true;
            
            i--;
        }
        
        numCluster = clusterCentroids.numInstances();
        initialInstances = null;
        initialCentroid = null;
    }
    
    private int clusterProcessedInstance(Instance instance, boolean updateErrors) {
        
        double minimumDistance = Integer.MAX_VALUE;
        int bestCluster = 0;
        
        for (int i = 0; i < numCluster; i++) {
            double dist = distanceFunction.distance(instance, clusterCentroids.instance(i));
            
            if (dist < minimumDistance) {
                minimumDistance = dist;
                bestCluster = i;
            }
         
        }
        
        if (updateErrors) {
            squaredErrors[bestCluster] += minimumDistance * minimumDistance;
        }
        
        return bestCluster;
    }
    
    private double[] updateCentroid(int centroidIndex, Instances members, boolean updateClusterInfo) {
        double[] vals = new double[members.numAttributes()];
        
        for (int i = 0; i < members.numAttributes(); i++) {
            vals[i] = members.meanOrMode(i);
        }
        
        if (updateClusterInfo) {
            clusterCentroids.add(new DenseInstance(1.0, vals));
        }
        
        return vals;
    }

    @Override
    public int numberOfClusters() throws Exception {
        return numCluster;
    }
    
}
