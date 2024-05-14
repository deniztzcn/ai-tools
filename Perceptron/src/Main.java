import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<InputVector> trainingSet = new ArrayList<>();
    private static List<InputVector> testSet = new ArrayList<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter file name for training: ");
        String trainingFileName = scanner.nextLine();
        System.out.print("Please enter file name for test: ");
        String testFileName = scanner.nextLine();
        try {
            loadSet(trainingSet,trainingFileName);
            loadSet(testSet,testFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            System.out.print("Please input number of iterations for the training: ");
            int numOfIteration = Integer.parseInt(scanner.nextLine());
            Perceptron perceptron = new Perceptron(trainingSet);
            perceptron.train(numOfIteration);


            System.out.println("Please choose operation");
            System.out.println("1- Check accuracy\n" +
                    "2- Classify a vector\n" +
                    "anything else to quit");
            System.out.print("-> ");
            String command = scanner.nextLine();

            switch (command){
                case "1" -> System.out.println("Accuracy: " + testPerceptron(perceptron,testSet));
                case "2" -> {
                    System.out.print("Please enter a vector with separating by comma (dimension = " +
                            trainingSet.getFirst().getAttributes().length + "): " );
                    String input = scanner.nextLine();
                    String[] parts = input.split(",");
                    InputVector vector = new InputVector(parts,true);
                    System.out.println("Classification: " + perceptron.classify(vector.getAttributes()));
                }
                default -> {
                    System.out.println("quiting...");
                    return;
                }
            }
        }

    }

    public static void loadSet(List<InputVector> set,String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null){
            String[] parts = line.split(",");
            InputVector vector = new InputVector(parts,false);
            set.add(vector);
        }
    }

    public static double testPerceptron(Perceptron perceptron, List<InputVector> testSet){
        int totalVectors = testSet.size();
        int numOfTrue = 0;

        for(InputVector vector : testSet){
            String expectedClassification = vector.getClassification();
            double[] attributes = vector.getAttributes();
            String predictedClassification = perceptron.classify(attributes);

            if(expectedClassification.equals(predictedClassification))
                numOfTrue++;
        }

        return (double) numOfTrue / totalVectors;
    }
}