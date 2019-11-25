package ua.edu.ucu.tries;


import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import ua.edu.ucu.tries.RWayTrie;
import ua.edu.ucu.tries.Tuple;

/**
 *
 * @author Andrii_Rodionov
 */
public class RWayTrieTest {

    private RWayTrie trie;

    @Before
    public void init() {
        trie = new RWayTrie();
        String[] lst = new String[] {"apples", "cherries", "pears", "app", "cheers", "pearl", "poo"};
        for (int i = 0; i < lst.length; i++) {
            trie.add(new Tuple(lst[i], lst[i].length()));
        }
    }

    @Test
    public void testContains() {
        assertTrue(trie.contains("apples"));
        assertTrue(trie.contains("app"));
        assertTrue(trie.contains("pears"));
        assertFalse(trie.contains("pear"));
    }

    @Test
    public void testDeleteAndContain() {
        trie.delete("app");
        assertFalse(trie.contains("app"));
        assertTrue(trie.contains("apples"));
    }

    @Test
    public void testWordsWithPrefix() {
        String pref = "pea";

        Iterable<String> result = trie.wordsWithPrefix(pref);

        String[] expResult = {"pears","pearl"};

        assertThat(result, containsInAnyOrder(expResult));
    }

    @Test
    public void testWords() {
        Iterable<String> result = trie.words();
        String[] expResult = {"apples", "cherries", "pears", "cheers", "pearl", "poo", "app"};

        assertThat(result, containsInAnyOrder(expResult));
    }

    @Test
    public void testWordsWithNonexistingPrefix() {
        String pref = "aaa";

        Iterable<String> result = trie.wordsWithPrefix(pref);

        String[] expResult = {};

        assertThat(result, containsInAnyOrder(expResult));
    }

    @Test
    public void testAddTheSameWord() {
        for (int i = 0; i < 6; i++) {
            trie.add(new Tuple("hello", 5));
        }
        assertEquals(8, trie.size());
    }

    @Test
    public void testDeleteNonexistingElement() {
        assertFalse(trie.delete("application"));
    }

}
