import random
import numpy as np

def load_data(filename='iris.data'):
    data = []
    with open(filename,'r') as file:
        for line in file:
            attributes = line.strip().split(',')
            attributes = [float(value) for value in attributes[:-1]]
            data.append(attributes)
        return data

def initialize_centroids(data,k):
    centroids = []
    indices = set()
    while len(indices) < k:
        index = random.randint(0,len(data)-1)
        if index not in indices:
            indices.add(index)
            centroids.append(data[index].copy())
    return np.array(centroids)

def euclidean_distance(point1, point2):
    return np.sqrt(np.sum((np.array(point1) - np.array(point2))**2))

def find_nearest_centroid(data_point, centroids):
    min_distance = float('inf')
    cluster_index = -1
    for i in range(len(centroids)):
        distance = euclidean_distance(data_point, centroids[i])
        if distance < min_distance:
            min_distance = distance
            cluster_index = i
    return cluster_index

def recalculate_centroids(data, assignments, k):
    n_features = data.shape[1]
    centroids = np.zeros((k, n_features))
    counts = np.zeros(k)
    for i, cluster in enumerate(assignments):
        centroids[cluster] += data[i]
        counts[cluster] += 1

    for i in range(k):
        if counts[i] > 0:
            centroids[i] /= counts[i]

    return centroids

def total_distance(data, assignments, centroids):
    total = 0
    for i in range(len(data)):
        total += euclidean_distance(data[i], centroids[assignments[i]])
    return total

def kmeans(data,k,max_iterations=100):
    data = np.array(data)
    centroids = initialize_centroids(data, k)
    assignments = np.zeros(len(data), dtype=int)
    changed = True

    while changed and max_iterations > 0:
        changed = False
        for i in range(len(data)):
            best_cluster = find_nearest_centroid(data[i], centroids)
            if assignments[i] != best_cluster:
                assignments[i] = best_cluster
                changed = True

        centroids = recalculate_centroids(data, assignments, k)
        print(f"Total Distance: {total_distance(data, assignments, centroids):.2f}")

        max_iterations -= 1

    return assignments, centroids

def print_clusters(data, assignments, k):
    for i in range(k):
        print(f"\nCluster {i + 1}:")
        for idx, point in enumerate(data):
            if assignments[idx] == i:
                print(", ".join(map(str, point)))

if __name__ == '__main__':
    k = int(input("Enter number of clusters:"))
    data = load_data()
    assignments, centroids = kmeans(data,k)
    print_clusters(data, assignments, k)