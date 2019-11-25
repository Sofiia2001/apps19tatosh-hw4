package ua.edu.ucu.tries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ua.edu.ucu.collections.Queue;

public class RWayTrie implements Trie {
    private static final int POSSIBLESIZE = 26;
    private int size = 0;
    private TrieNode root;

    public RWayTrie() {
        root = new TrieNode();
    }

    public static class TrieNode {
        TrieNode[] children = new TrieNode[POSSIBLESIZE];
        private boolean endWord;
        private int weight;
        private char value;

        TrieNode() {
            endWord = false;
            for (int i = 0; i < POSSIBLESIZE; i++) {
                children[i] = null;
            }
        }

        private int getChilrdenAmount() {
            int counter = 0;
            for (int j = 0; j < children.length; j++) {
                if (children[j] != null) {
                    counter++;
                }
            }
            return counter;
        }
    }


    private int charInAlphabet(char l) {
        char lower = Character.toLowerCase(l);
        return (int) lower - 97;
    }

    @Override
    public void add(Tuple t) {
        if (!contains(t.term)) {
            char[] word = t.term.toCharArray();
            TrieNode curr = root;
            for (int i = 0; i < word.length; i++) {
                if (curr.children[charInAlphabet(word[i])] == null) {
                    TrieNode newTrieNode = new TrieNode();
                    newTrieNode.value = word[i];
                    curr.children[charInAlphabet(word[i])] = newTrieNode;
                }
                if (i == word.length - 1) {
                    curr.children[charInAlphabet(word[i])].endWord = true;
                    curr.children[charInAlphabet(word[i])].weight = t.weight;
                }
                curr = curr.children[charInAlphabet(word[i])];
            }
            size++;
        }
    }

    @Override
    public boolean contains(String word) {
        TrieNode curr = root;
        char[] w = word.toCharArray();
        boolean result = true;
        for (int i = 0; i < w.length - 1; i++) {
            if (curr.children[charInAlphabet(w[i])] == null) {
                result = false;
                return result;
            }
            curr = curr.children[charInAlphabet(w[i])];
        }
        if ((curr.children[charInAlphabet(w[w.length - 1])] == null) ||
                !curr.children[charInAlphabet(w[w.length - 1])].endWord) {
            result = false;
        }
        return result;
    }

    @Override
    public boolean delete(String word) {
        if (!contains(word)) {
            return false;
        } else {
            TrieNode curr = root;
            char[] charWord = word.toCharArray();
            int deleted = 0;
            for (int k = 0; k < charWord.length; k++) {
                curr = curr.children[charInAlphabet(charWord[k])];
            }
            curr.endWord = false;
            curr = root;

            for (int i = 0; i < charWord.length; i++) {
                for (int j = 0; j < charWord.length - deleted - 1; j++) {
                    curr = curr.children[charInAlphabet(charWord[j])];
                    if (curr.children[charInAlphabet(charWord[j + 1])].getChilrdenAmount() == 0 &&
                            !curr.children[charInAlphabet(charWord[j + 1])].endWord) {
                        curr.children[charInAlphabet(charWord[j + 1])] = null;
                        deleted++;
                    }
                }
                curr = root;
            }
            if (deleted == charWord.length - 1) {
                curr.children[charInAlphabet(charWord[0])] = null;
            }
            size--;
            return true;
        }
    }

    private String[] BFS() {
        Object[] words = new Object[size()];
        Queue queue = new Queue();
        TrieNode curr = root;
        Queue lastNodes = new Queue();
        int indexOfWord = 0;
        for (int i = 0; i < curr.children.length; i++) {
            if (curr.children[i] != null) {
                lastNodes.enqueue(curr.children[i]);
                queue.enqueue(curr.children[i].value);
            }
        }
        while (!queue.isEmpty()) {
            Object s = queue.dequeue();
            TrieNode current = (TrieNode) lastNodes.dequeue();
            if (current.endWord) {
                if (current.getChilrdenAmount() != 0) {
                    queue.enqueue(s);
                    current.endWord = false;
                    lastNodes.enqueue(current);
                }
                words[indexOfWord] = s;
                indexOfWord++;

            } else {
                for (int i = 0; i < current.children.length; i++) {
                    if (current.children[i] != null) {
                        lastNodes.enqueue(current.children[i]);
                        queue.enqueue(s.toString() + current.children[i].value);
                    }
                }
            }
        }
        String[] toReturn = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            if (words[i] != null) {
                toReturn[i] = words[i].toString();
            }
        }
        return toReturn;
    }

    private boolean contains(int[] lst, int val) {
        for (int i = 0; i < lst.length; i++) {
            if (lst[i] == val) {
                return true;
            }
        }
        return false;
    }

    public String[] BFS(String pref, int k) {
        Object[] words = new Object[size()];
        Queue queue = new Queue();
        TrieNode curr = root;

        Queue lastNodes = new Queue();
        int indexOfWord = 0; int prefIndex = 0;
        char[] prefixes = pref.toCharArray();
        int[] weights = new int[size()];
        int possibleWeights = 0;

        for (int i = 0; i < curr.children.length; i++) {
            if (curr.children[i] != null && prefixes[prefIndex] == curr.children[i].value) {
                lastNodes.enqueue(curr.children[i]);
                queue.enqueue(curr.children[i].value);
            }

        }
        prefIndex++;

        while (possibleWeights < k + 1 && !(queue.isEmpty())) {
            Object s = queue.dequeue();
            TrieNode current = (TrieNode) lastNodes.dequeue();
            if (current.endWord && prefIndex == prefixes.length) {
                if (current.getChilrdenAmount() != 0) {
                    queue.enqueue(s);
                    current.endWord = false;
                    lastNodes.enqueue(current);
                }
                if (!contains(weights, current.weight)) {
                    possibleWeights++;
                    weights[possibleWeights] = current.weight;
                }
                if (possibleWeights == k + 1) {
                    break;
                }
                words[indexOfWord] = s;
                indexOfWord++;

            } else {
                for (int i = 0; i < current.children.length; i++) {
                    if (current.children[i] != null) {
                        if (prefIndex < prefixes.length && prefixes[prefIndex] == current.children[i].value) {
                            lastNodes.enqueue(current.children[i]);
                            queue.enqueue(s.toString() + current.children[i].value);
                            prefIndex++;
                        } else if (!(prefIndex < prefixes.length)){
                            lastNodes.enqueue(current.children[i]);
                            queue.enqueue(s.toString() + current.children[i].value);
                        }
                    }
                }
            }
        }
        String[] toReturn = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            if (words[i] != null) {
                toReturn[i] = words[i].toString();
            }
        }
        return toReturn;
    }

    @Override
    public Iterable<String> words() {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int position;
                    private String[] words = BFS();

                    @Override
                    public boolean hasNext() {
                        return position != words.length && words[position] != null;
                    }

                    @Override
                    public String next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return words[position++];
                    }
                };
            }
        };
    }

    @Override
    public Iterable<String> wordsWithPrefix(String s) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int position;
                    private String[] words = BFS(s, size());

                    @Override
                    public boolean hasNext() {
                        return position != words.length && words[position] != null;
                    }

                    @Override
                    public String next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return words[position++];
                    }
                };
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

}
