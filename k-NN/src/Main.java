import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the training data file name:");
        String trainingFileName = scanner.nextLine();
        System.out.println("Please enter the test file name:");
        String testFileName = scanner.nextLine();
        System.out.println("Please enter k value:");
        int k = scanner.nextInt();
        scanner.nextLine();

        List<Case> trainingSet = loadData(trainingFileName);
        List<Case> testSet = loadData(testFileName);

        double accuracy = getAccuracy(trainingSet,testSet,k);
        Map<Integer,Double> accuracyResults = accuracyResults(trainingSet,testSet);



        while(true){
            System.out.println("Please choose an operation:");
            System.out.println("1- Check accuracy for k = " + k);
            System.out.println("2- Classify a vector");
            System.out.println("3- Show the chart for all k values");
            System.out.println("anything else for break");

            String command = scanner.nextLine();

            switch (command){
                case "1":
                    System.out.println("Accuracy rate: " + accuracy);
                    break;
                case "2":
                    int size = testSet.getFirst().getAttributes().length;
                    System.out.println("Expected vector dimension: " + size);
                    System.out.println("Please enter a vector with sepearting values with comma e.g 1.2,3.5,3.2:");
                    String vector = scanner.nextLine() + ",null";

                    String[] parts = vector.split(",");
                    if (parts.length - 1 != size){
                        System.out.println("Wrong dimension, please try again");
                    }

                    System.out.println("Please enter k value [1," + trainingSet.size() + "]:");

                    int _k = scanner.nextInt();
                    scanner.nextLine();

                    if (_k < 1 || _k > trainingSet.size()){
                        System.out.println("Invalid k value, please try again");
                    }

                    Case _case = new Case(vector);
                    System.out.println("Classification: " + findKnn(trainingSet,_case,_k));
                    break;
                case "3":
                    plotAccuracy(accuracyResults);
                    break;
                default:
                    System.out.println("why");
                    break;
            }
        }

    }

    public static String findKnn(List<Case> trainingSet, Case testCase, int k){
        Map<Case,Double> neighboursAndDistance = new HashMap<>();

        for (Case trainedCase : trainingSet){
            double distance = euclidianDistance(trainedCase,testCase);
            neighboursAndDistance.put(trainedCase,distance);
        }

        List<Map.Entry<Case,Double>> list = new LinkedList<>(neighboursAndDistance.entrySet());
        list.sort(Map.Entry.comparingByValue());

        String[] labels = new String[k];
        for (int i = 0; i < k; i++){
            labels[i] = list.get(i).getKey().getDecision();
        }

        return mostFrequent(labels);

    }
    public static double euclidianDistance(Case trainedCase, Case testCase){
        double distance = 0.0;
        double[] trained_attributes = trainedCase.getAttributes();
        double[] test_attributes = testCase.getAttributes();
        for (int i = 0; i < trained_attributes.length; i++){
            distance += Math.pow(trained_attributes[i] - test_attributes[i],2);
        }
        return Math.sqrt(distance);
    }

    public static String mostFrequent(String[] labels){
        Map<String,Integer> frequency = new HashMap<>();

        for(String label: labels){
            frequency.put(label,frequency.getOrDefault(label,0) + 1);
        }

        String mostFrequent = "";
        int max = 0;
        for(Map.Entry<String,Integer> entry : frequency.entrySet()){
            if(entry.getValue() > max){
                max = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        return mostFrequent;
    }

    public static List<Case> loadData(String path)  {
        BufferedReader br;
        List<Case> trainingSet = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            while((line = br.readLine()) != null){
                Case _case = new Case(line);
                trainingSet.add(_case);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return trainingSet;
    }

    public static Map<Integer,Double> accuracyResults (List<Case> trainingSet,List<Case> testSet){
        Map<Integer,Double> result = new HashMap<>();
        for (int k = 1; k < trainingSet.size(); k++){
            result.put(k,getAccuracy(trainingSet,testSet,k));
        }
        return result;
    }

    public static double getAccuracy(List<Case> trainingSet,List<Case> testSet, int k){
        String[] knnResults = new String[testSet.size()];
        String[] realClassifications = new String[testSet.size()];

        for(int i = 0; i < realClassifications.length; i++){
            realClassifications[i] = testSet.get(i).getDecision();
        }

        for (int i = 0; i < knnResults.length; i++){
            knnResults[i] = findKnn(trainingSet,testSet.get(i),k);
        }

        int correctResults = testSet.size();
        for(int i = 0; i < knnResults.length; i++){
            if(!realClassifications[i].equals(knnResults[i])){
                correctResults--;
            }
        }
        return (double) correctResults / testSet.size();
    }

    public static void plotAccuracy(Map<Integer,Double> accuracyMap){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(Map.Entry<Integer,Double> entry : accuracyMap.entrySet()){
            dataset.addValue(entry.getValue(),"Accuracy",String.valueOf(entry.getKey()));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Accuracy vs. k",
                "k value",
                "Accuracy",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(400,300));

        JFrame jFrame = new JFrame("Accuracy vs. k");
        jFrame.getContentPane().add(panel, BorderLayout.CENTER);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setAlwaysOnTop(true);
    }
}