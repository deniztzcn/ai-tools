import math
import random
import string
import numpy
import matplotlib.pyplot as plt


class NeuralNetwork:

    def __init__(self, training_file, test_file):
        self.learning_rate = 0.01
        self.wrong_predictions = []
        self.languages = set()
        self.training_set = self.load_data(training_file)
        self.test_set = self.load_data(test_file)
        self.num_perceptrons = len(self.languages)
        self.num_attributes = 26
        self.weight_matrix = numpy.random.uniform(low=-0.1, high=0.1, size=(self.num_perceptrons, self.num_attributes))
        self.bias_vector = numpy.transpose(numpy.random.rand(self.num_perceptrons))
        self.languages = list(self.languages)

    def train(self, number_of_epoch: int):
        for epoch in range(number_of_epoch):
            for vector in self.training_set:
                text = vector[1]
                label = vector[0]
                expected = self.get_matrix(label)
                input_vector = self.normalize_vector(self.count_letters(text))
                y = self.linear_function(self.get_net(input_vector))
                self.delta_rule(y, expected, input_vector)

    def linear_function(self, x):
        return x

    def delta_rule(self, y, expected, input_vector):
        self.bias_vector = self.bias_vector - self.learning_rate * (expected - y) #* (1 - y) * y
        self.weight_matrix = self.weight_matrix + self.learning_rate * numpy.outer((expected - y), input_vector)

    def get_matrix(self, label):
        array = [0 for _ in range(len(self.languages))]
        label_lower = label.lower()
        for i, lang in enumerate(self.languages):
            if label_lower == lang:
                array[i] = 1
                return numpy.transpose(numpy.array(array))

    def normalize_vector(self, occurence):
        total = sum(occurence)
        normalized_vector = [(x / total) for x in occurence]
        return numpy.transpose(numpy.array(normalized_vector))

    def get_net(self, input_vector):
        return numpy.dot(self.weight_matrix, input_vector) - self.bias_vector

    def count_letters(self, text):
        result = [0 for _ in range(26)]
        letters = string.ascii_lowercase
        for letter in text:
            letter = letter.lower()
            if letter in letters:
                index = letters.index(letter)
                result[index] += 1
        return result

    def load_data(self, file_name):
        result = []
        with open(file_name, encoding="UTF-8") as file:
            for line in file:
                line = line.rstrip()
                parts = line.split(",", 1)
                parts[1] = parts[1][1:-1]
                self.languages.add(parts[0].lower())
                result.append(parts)
        return result

    def predict(self, text):
        input_vector = self.normalize_vector(self.count_letters(text))
        activation = self.linear_function(self.get_net(input_vector))
        max_perceptron_index = numpy.argmax(activation, axis=0)
        return self.languages[max_perceptron_index]

    def test(self):
        correct_predictions = 0

        for sample in self.test_set:
            label, text = sample
            label = label.lower()
            predicted = self.predict(text)
            if predicted == label:
                correct_predictions += 1
            else:
                self.wrong_predictions.append(f"Expected: {label}\n"
                                              f"Predicted: {predicted}\n"
                                              f"Text: {text}")

        accuracy = correct_predictions / len(self.test_set)
        print(f"Accuracy: {accuracy}")
        return accuracy


def plot_accuracy(epoch_values, accuracy_list):
    plt.figure(figsize=(10, 5))
    plt.plot(epoch_values, accuracy_list, label='Test Accuracy')
    plt.xlabel('Epochs')
    plt.ylabel('Accuracy')
    plt.title('Neural Network Accuracy Over Epochs')
    plt.legend()
    plt.show()


def menu():
    print("1. Classify a text")
    print("2. Show wrongly predicted texts")
    print("3. Enter epochs to train again")
    print("4. Show the graph")
    print("5. Exit")


if __name__ == "__main__":
    accuracy_list = []
    epoch_values = []
    if False:
        training_data = input("Please enter the training data file name: ")
        test_data = input("Please enter the test data file name: ")
    else:
        training_data = "lang.train.csv"
        test_data = "lang.test.csv"

    epochs = int(input("Please enter the number of epochs: "))
    nn = NeuralNetwork(training_data, test_data)
    nn.train(epochs)
    nn.test()
    while True:
        menu()
        command = input("Please choose an operation:")
        if command == "1":
            text = input("Please enter the text you want to classify:")
            print(nn.predict(text))
        if command == "2":
            for wrong in nn.wrong_predictions:
                print(wrong)
        if command == "3":
            epochs = int(input("Please enter the number of epochs: "))
            nn.train(epochs)
            nn.test()
        if command == "4":
            _accuracy_list = []
            _epoch_values = []
            _nn = NeuralNetwork(training_data, test_data)
            _number = 0
            for i in range(11):
                _nn.train(20)
                _number += 20
                _accuracy_list.append(_nn.test())
                _epoch_values.append(_number)
            plot_accuracy(_epoch_values, _accuracy_list)
        if command == "5":
            break
