/*
 * File         : AgnesCluster.java
 * Author       : Rahman Adianto
 */
package wekaclustering.utils;

import java.util.Vector;

public class AgnesCluster {

    private Vector<Integer> members;
    private Vector<Vector<Double>> distanceMatrix;
    
    public AgnesCluster(Vector<Integer> indexes, DistanceMatrix distanceMatrix) {
        this.members = indexes;
        this.distanceMatrix = distanceMatrix.getDistanceMatrix();
    }
    
    public void merge(AgnesCluster toMerge) {
        members.addAll(toMerge.getMembers());
    }
    
    public double distance(AgnesCluster cluster, boolean isCompleteLinkage) {
        
        Vector<Integer> memberCluster = cluster.getMembers();
        double d;
        
        if (isCompleteLinkage) {
            d = Integer.MIN_VALUE;
        }
        else {
            d = Integer.MAX_VALUE;
        }
        
        for (int i = 0; i < members.size(); i++) {
            for (int j = 0; j < memberCluster.size(); j++) {
                double temp = distanceMatrix.get(i).get(j);
                if (isCompleteLinkage) {
                    if (d < temp) {
                        d = temp;
                    }
                }
                else {
                    if (d > temp) {
                        d = temp;
                    }
                }
            }
        }
        
        return d;
    }

    public Vector<Integer> getMembers() {
        return members;
    }
}
