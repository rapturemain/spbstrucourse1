import javafx.util.Pair;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class testsTask1Class {

    @Test
    public void add() {
        HashTable<String, String> table = new HashTable<>(10);
        table.add(new Pair<>("test", "10"));
        assertEquals("[test=[10]]", table.toString());
        table.add(new Pair<>("other", "c"));
        assertEquals("[other=[c], test=[10]]", table.toString());
        table.add(new Pair<>("other", "test2"));
        assertEquals("[other=[c, test2], test=[10]]", table.toString());
    }

    @Test
    public void remove() {
        HashTable<String, String> table = new HashTable<>(10);
        table.add(new Pair<>("test", "10"));
        assertEquals("[test=[10]]", table.toString());
        table.remove("test");
        table.add(new Pair<>("other", "c"));
        assertEquals("[other=[c]]", table.toString());
        table.add(new Pair<>("other", "test2"));
        assertEquals("[other=[c, test2]]", table.toString());
        table.remove("other");
        assertEquals("[]", table.toString());
    }

    @Test
    public void contains() {
        HashTable<String, String> table = new HashTable<>(10);
        table.add(new Pair<>("test", "10"));
        assertTrue(table.contains("test"));
        assertFalse(table.contains("testFalse"));
        table.add(new Pair<>("value", "testValue"));
        assertTrue(table.containsValue("testValue"));
        assertFalse(table.containsValue("test"));
    }

    @Test
    public void clear() {
        HashTable<String, Integer> table = new HashTable<>(10);
        table.add(new Pair<>("test", 10));
        table.add(new Pair<>("other", 24));
        table.add(new Pair<>("t2", 54));
        table.clear();
        assertEquals("[]", table.toString());
    }

    @Test
    public void equals() {
        HashTable<String, Integer> table1 = new HashTable<>(10);
        HashTable<String, Integer> table2 = new HashTable<>(10);
        table1.add(new Pair<>("tatata", 11));
        table1.add(new Pair<>("tytyty", 17));
        assertNotEquals(table1, table2);
        table2.add(new Pair<>("tatata", 11));
        assertNotEquals(table1, table2);
        table2.add(new Pair<>("tytyty", 17));
        assertEquals(table1, table2);
    }

    @Test
    public void toHashTable() {
        Pair[] array = new Pair[3];
        array[0] = new Pair<>(24, 11);
        array[1] = new Pair<>(6, 10);
        array[2] = new Pair<>(24, 76);
        HashTable table1 = HashTable.toHashTable(array, 10);
        HashTable<Integer, Integer> table2 = new HashTable<>(10);
        table2.add(new Pair<>(24, 11));
        table2.add(new Pair<>(6, 10));
        table2.add(new Pair<>(24, 76));
        assertEquals(table1, table2);
    }

    @Test
    public void iterator() {
        HashTable<String, Integer> table = new HashTable<>(10);
        table.add(new Pair<>("test", 10));
        table.add(new Pair<>("other", 24));
        table.add(new Pair<>("t2", 54));
        Iterator iter = table.iterator();
        while (iter.hasNext()) {
            iter.next();
            iter.remove();
        }
        assertEquals("[]", table.toString());
    }
}
