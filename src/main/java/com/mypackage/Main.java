package com.mypackage;

import com.mypackage.MarkowitzOptimisation;

public class Main {
    public static void main(String[] args) {
        // Example data
        double[] expectedReturns = {0.05, 0.07, 0.06}; // Expected returns for three assets
        double[][] covarianceMatrix = {
                {0.0004, 0.0002, 0.0001},
                {0.0002, 0.0003, 0.00015},
                {0.0001, 0.00015, 0.0005}
        }; // Covariance matrix for three assets

        double targetReturn = 0.06; // Example target return

        // Initialize the optimizer and optimize
        MarkowitzOptimisation optimizer = new MarkowitzOptimisation(expectedReturns, covarianceMatrix);
        double[] optimizedWeights = optimizer.optimizeForTargetReturn(targetReturn);

        // Print the results
        for (int i = 0; i < optimizedWeights.length; i++) {
            System.out.println("Asset " + (i+1) + ": " + optimizedWeights[i]);
        }
    }
}
