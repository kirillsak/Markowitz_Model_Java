package com.mypackage;

import org.ojalgo.matrix.Primitive64Matrix;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Variable;

public class MarkowitzOptimisation {

    private final double[] expectedReturns;
    private final Primitive64Matrix covarianceMatrix;

    public MarkowitzOptimisation(double[] expectedReturns, double[][] covariances) {
        this.expectedReturns = expectedReturns;
        this.covarianceMatrix = Primitive64Matrix.FACTORY.rows(covariances);
    }

    public double[] optimizeForTargetReturn(double targetReturn) {
        final int numAssets = expectedReturns.length;

        ExpressionsBasedModel model = new ExpressionsBasedModel();
        Variable[] assetWeights = new Variable[numAssets];

        // Initialize weight variables
        for (int i = 0; i < numAssets; i++) {
            assetWeights[i] = model.addVariable("Asset_" + i).lower(0).upper(1);
        }

        // Set up quadratic objective
        Expression objective = model.addExpression("Objective");
        for (int i = 0; i < numAssets; i++) {
            for (int j = 0; j < numAssets; j++) {
                double covariance = covarianceMatrix.get(i, j);
                objective.set(assetWeights[i], assetWeights[j], covariance);
            }
        }


        // Constraint: Sum of weights = 1
        Expression totalWeightConstraint = model.addExpression("TotalWeight").level(1);
        for (Variable weight : assetWeights) {
            totalWeightConstraint.set(weight, 1);
        }

        // Constraint: Expected return = target return
        Expression expectedReturnConstraint = model.addExpression("ExpectedReturn").level(targetReturn);
        for (int i = 0; i < numAssets; i++) {
            expectedReturnConstraint.set(assetWeights[i], expectedReturns[i]);
        }

        model.minimise();

        double[] optimizedWeights = new double[numAssets];
        for (int i = 0; i < numAssets; i++) {
            optimizedWeights[i] = assetWeights[i].getValue().doubleValue();
        }

        return optimizedWeights;
    }
}

