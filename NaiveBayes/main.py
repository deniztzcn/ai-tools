class Bayes:
    def __init__(self, training_data, test_data):
        self.possible_classifications = {}
        self.data_matrix = self.read_data(training_data, True)
        self.test_data_matrix = self.read_data(test_data, False)

    def read_data(self, file_name, is_training):
        result = []
        with open(file_name, 'r') as file:
            for line in file:
                line = line.strip()
                _input = line.split(",")
                result.append(_input)
                if is_training:
                    for i, data in enumerate(_input):
                        if i not in self.possible_classifications:
                            self.possible_classifications[i] = {}
                        if data not in self.possible_classifications[i]:
                            self.possible_classifications[i][data] = 1
                        else:
                            self.possible_classifications[i][data] += 1
        return result

    def get_probability_attribute(self, attribute, index, classification):
        counter = 0
        for data in self.data_matrix:
            if data[0] == classification:
                if data[index] == attribute:
                    counter += 1
        #print(self.possible_classifications)
        #print(classification)
        #print(self.possible_classifications[0][classification])
        return (counter + 1) / (self.possible_classifications[0][classification] + len(self.possible_classifications[index]))

    def count_unique_attributes(self, index):
        result = set()
        for data in self.data_matrix:
            result.add(data[index])
        return len(result)

    def compute_probability(self, classification, data):
        number_of_classification = len(self.possible_classifications[0])
        probability_of_classification = number_of_classification / len(self.data_matrix)
        result = probability_of_classification
        for i, attribute in enumerate(data):
            probability_attribute = self.get_probability_attribute(attribute, i, classification)
            result *= probability_attribute
        return result

    def test(self):
        result_of_probabilities = []
        false_positives = 0
        true_positives = 0
        false_negatives = 0
        true_negatives = 0
        classifications = list(self.possible_classifications[0].keys())
        for data in self.test_data_matrix:
            result_of_probabilities.clear()
            for label in self.possible_classifications[0]:
                result_of_probabilities.append((label, self.compute_probability(label, data)))
            predicted = max(result_of_probabilities, key=lambda result: result[1])[0]
            if predicted == classifications[0] and data[0] == classifications[0]:
                true_positives += 1
            elif predicted == classifications[0] and data[0] == classifications[1]:
                false_positives += 1
            elif predicted == classifications[1] and data[0] == classifications[1]:
                true_negatives += 1
            elif predicted == classifications[1] and data[0] == classifications[0]:
                false_negatives += 1

        accuracy = (true_positives + true_negatives) / (true_positives + false_negatives + true_negatives + false_positives)
        precision = true_positives / (true_positives + false_positives)
        recall = true_positives / (true_positives + false_negatives)
        f_measure = 2 * precision * recall / (precision + recall)
        print("Accuracy:", accuracy)
        print("Precision:", precision)
        print("Recall:", recall)
        print("F-measure:", f_measure)


if __name__ == "__main__":
    bayes = Bayes("agaricus-lepiota.data", "agaricus-lepiota.test.data")
    bayes.test()
    #results are okay, but you can try to improve the performance
