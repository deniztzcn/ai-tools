import heapq
from collections import defaultdict, Counter


class Node:
    def __init__(self, char, freq):
        self.char = char
        self.freq = freq
        self.left = None
        self.right = None

    def __lt__(self, other):
        return self.freq < other.freq


def build_huffman_tree(text):
    frequency = Counter(text)


    priority_queue = [Node(char, freq) for char, freq in frequency.items()]
    heapq.heapify(priority_queue)


    while len(priority_queue) > 1:
        left = heapq.heappop(priority_queue)
        right = heapq.heappop(priority_queue)

        merged = Node(None, left.freq + right.freq)
        merged.left = left
        merged.right = right

        heapq.heappush(priority_queue, merged)

    return priority_queue[0]


def huffman_codes(node, prefix="", code_map={}):
    if node is not None:
        if node.char is not None:
            code_map[node.char] = prefix
        huffman_codes(node.left, prefix + "0", code_map)
        huffman_codes(node.right, prefix + "1", code_map)
    return code_map


def encode_text(text, codes):
    return ''.join(codes[char] for char in text)


def huffman_encoding(text):
    if not text:
        return {}, ""

    root = build_huffman_tree(text)

    codes = huffman_codes(root)

    encoded_text = encode_text(text, codes)
    return codes, encoded_text



text = "Example text"
codes, encoded_text = huffman_encoding(text)
print("Huffman Codes:")
for char, code in sorted(codes.items()):
    print(f"'{char}': {code}")
print("\nEncoded Text:")
print(encoded_text)
