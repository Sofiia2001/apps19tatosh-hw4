package ua.edu.ucu.autocomplete;

import ua.edu.ucu.collections.Queue;
import ua.edu.ucu.tries.RWayTrie;
import ua.edu.ucu.tries.Trie;
import ua.edu.ucu.tries.Tuple;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author andrii
 */
public class PrefixMatches {

    private Trie trie;

    public PrefixMatches(Trie trieNew) {
        trie = trieNew;
    }

    public int load(String... strings) {
        String[][] words = new String[strings.length][];
        int amount = 0;
        for (int i = 0; i < strings.length; i++) {
            words[i] = strings[i].split(" ");
        }
        for (int ind = 0; ind < words.length; ind++) {
            for (int j = 0; j < words[ind].length; j++) {
                if (words[ind][j].length() > 2) {
                    trie.add(new Tuple(words[ind][j], words[ind][j].length()));
                    amount++;
                }
            }
        }
        return amount;
    }

    public boolean contains(String word) {
        return trie.contains(word);
    }

    public boolean delete(String word) {
        return trie.delete(word);
    }

    public Iterable<String> wordsWithPrefix(String pref) {
        if (pref.length() > 1) {
            return trie.wordsWithPrefix(pref);
        }
        return trie.wordsWithPrefix(" ");
    }

    private String[] BFS(String pref, int k) {
        RWayTrie tr = new RWayTrie();

        for (String el : trie.words()) {
            tr.add(new Tuple(el, el.length()));
        }
        return tr.BFS(pref, k);
    }

    public Iterable<String> wordsWithPrefix(String pref, int k) {
        if (pref.length() > 1) {
            return new Iterable<String>() {
                @Override
                public Iterator<String> iterator() {
                    return new Iterator<String>() {
                        private int position;
                        private String[] words = BFS(pref, k);

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
        return trie.wordsWithPrefix(" ");
    }

    public int size() {
        return trie.size();
    }
}
