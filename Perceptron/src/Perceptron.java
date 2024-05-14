import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Perceptron {
    private double[] weights;
    private double learningRate = 0.01;
    private double bias;
    private List<InputVector> trainingSet;

    public Perceptron(List<InputVector> trainingSet) {
        int dimensionVector = trainingSet.getFirst().getAttributes().length;
        Random random = new Random();
        this.weights = new double[dimensionVector];
        for(int i = 0; i < weights.length; i++){
            this.weights[i] = random.nextDouble();
        }
        this.trainingSet = trainingSet;
        this.bias = random.nextDouble();
    }

    public void train(int numberOfIterations){
        for(int i = 0; i < numberOfIterations; i++){
            for(InputVector inputVector : trainingSet){
                double[] attributes = inputVector.getAttributes();
                String classification = inputVector.getClassification();

                double weightedSum = getWeightedSum(attributes);

                int y = stepFunction(weightedSum);

                updateWeightsAndBias(y,classification,attributes);
            }
        }

    }

    public double getWeightedSum(double[] attributes){
        double weightedSum = 0.0;
        for (int i = 0; i < attributes.length; i++) {
            weightedSum += weights[i] * attributes[i];
        }

        return weightedSum - bias;
    }

    public int stepFunction(double weightedSum){
        if(weightedSum >= 0)
            return 1;
        return 0;
    }

    public void updateWeightsAndBias(int y, String classification, double[] attributes){
        int expectedOutput;
        if(classification.equals("Iris-versicolor"))
            expectedOutput = 1;
        else
            expectedOutput = 0;

        bias = bias - learningRate * (expectedOutput - y);
        for (int i = 0; i < attributes.length; i++) {
            weights[i] = weights[i] + learningRate * (expectedOutput - y) * attributes[i];
        }
    }

    public String classify(double[] attributes){
        double weightedSum = getWeightedSum(attributes);
        int output = stepFunction(weightedSum);

        if (output == 1)
            return "Iris-versicolor";
        else
            return "Iris-virginica";
    }

    public double[] getWeights() {
        return weights;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double getBias() {
        return bias;
    }

    public List<InputVector> getTrainingSet() {
        return trainingSet;
    }
}

