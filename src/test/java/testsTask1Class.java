import javafx.util.Pair;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class testsTask1Class {

    @Test
    public void add() {
        HashTable table = new HashTable(10);
        table.add(new Pair("test", 10));
        assertEquals("[test=10]", table.toString());
        table.add(new Pair(24, 'c'));
        assertEquals("[24=c, test=10]", table.toString());
        table.add(new Pair(24, "test2"));
        assertEquals("[24=[c, test2], test=10]", table.toString());
    }

    @Test
    public void remove() {
        HashTable table = new HashTable(10);
        table.add(new Pair("test", 10));
        assertEquals("[test=10]", table.toString());
        table.remove("test");
        table.add(new Pair(24, 'c'));
        assertEquals("[24=c]", table.toString());
        table.add(new Pair(24, "test2"));
        assertEquals("[24=[c, test2]]", table.toString());
        table.remove(24);
        assertEquals("[]", table.toString());
    }

    @Test
    public void contains() {
        HashTable table = new HashTable(10);
        table.add(new Pair("test", 10));
        assertTrue(table.contains("test"));
        assertFalse(table.contains("testFalse"));
        table.add(new Pair("value", "testValue"));
        assertTrue(table.containsValue("testValue"));
        assertFalse(table.containsValue("test"));
    }

    @Test
    public void clear() {
        HashTable table = new HashTable(10);
        table.add(new Pair("test", 10));
        table.add(new Pair(24, 'c'));
        table.add(new Pair(24, "test2"));
        table.clear();
        assertEquals("[]", table.toString());
    }

    @Test
    public void equals() {
        HashTable table1 = new HashTable(10);
        HashTable table2 = new HashTable(10);
        table1.add(new Pair(10, 11));
        table1.add(new Pair(24, 17));
        assertFalse(table1.equals(table2));
        table2.add(new Pair(10, 11));
        assertFalse(table1.equals(table2));
        table2.add(new Pair(24, 17));
        assertTrue(table1.equals(table2));
    }

    @Test
    public void toHashTable() {
        Pair[] array = new Pair[3];
        array[0] = new Pair(24, 11);
        array[1] = new Pair(6, 10);
        array[2] = new Pair(24, 76);
        HashTable table1 = HashTable.toHashTable(array, 10);
        HashTable table2 = new HashTable(10);
        table2.add(new Pair(24, 11));
        table2.add(new Pair(6, 10));
        table2.add(new Pair(24, 76));
        assertTrue(table1.equals(table2));
    }
}
