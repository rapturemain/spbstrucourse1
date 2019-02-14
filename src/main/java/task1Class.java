import javafx.util.Pair;

import java.util.*;

public class task1Class {

     public static void main(String[] args) {
         HashTable table1 = new HashTable(10);
         HashTable table2 = new HashTable(10);
         HashTable table3 = new HashTable(10);
         for (int i = 0; i < 10000000; i++) {
             table1.add(new Pair(i, i + 2));
         }
         table2 = HashTable.toHashTable(table1.toArray(), 10);
         System.out.println(table1.equals(table2));
         System.out.println("end");
     }
}

class IntHashTable {
    IntHashTable() {
        hashTableArray = new List[hashTableArraySize];
        for (int i = 0; i < hashTableArraySize; i++) {
            hashTableArray[i] = new ArrayList<>();
        }
    }

    private Integer hashTableArraySize = 10; // Настройка размера хэш-таблицы.
    private List<Integer>[] hashTableArray;

    private Integer indexOf(Integer number) {
        return number.hashCode() % hashTableArraySize;
    }

    public boolean contains(Integer number) {
        Integer index = indexOf(number);
        if (hashTableArray[index].isEmpty()) return false;
        return binarySearch(0, hashTableArray[index].size() - 1, number, index);
    }

    private boolean binarySearch(Integer left, Integer right, Integer number, Integer index) {
        if (left != right) {
            Integer middle = (left + right) / 2;
            if (hashTableArray[index].get(middle) == number) {
                return true;
            } else {
                if (hashTableArray[index].get(middle) < number) {
                    return binarySearch(middle, right, number, index);
                } else {
                    return binarySearch(left, middle, number, index);
                }
            }
        } else {
            return number == hashTableArray[index].get(left);
        }
    }

    public void add(Integer number) {
        if (!contains(number)) {
            Integer index = indexOf(number);
            for (int i = 0; i < hashTableArray[index].size(); i++) {
                if (hashTableArray[index].get(i) > number) {
                    hashTableArray[index].add(i, number);
                    return;
                }
            }
            hashTableArray[index].add(number);
        }
    }

    public void remove(Integer number) {
        if (contains(number)) {
            hashTableArray[indexOf(number)].remove(number);
        }
    }

    public boolean equals(IntHashTable other) {
        return this.hashTableArray == other.hashTableArray;
    }
}

class HashTable<T> {
    HashTable() {
        totalCells = 0;
        hashTable = new List[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    HashTable(int size) {
        tableSize = size;
        totalCells = 0;
        hashTable = new List[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    private Integer tableSize = 521;
    private Integer totalCells;
    private List<Pair<T, T>>[] hashTable;

    private int indexOf(T key) {
        return key.hashCode() % tableSize;
    }

    private void rebuildTable() {
        tableSize = (int) (tableSize * 1.3);
        List<Pair<T, T>>[] table = new List[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = new ArrayList<>();
        }
        for (List<Pair<T, T>> i : hashTable) {
            for (Pair<T, T> j : i) {
                table[indexOf(j.getKey())].add(j);
            }
        }
        hashTable = table;
    }

    public boolean contains(T key) {
        if (key == null) {
            return false;
        }
        int index = indexOf(key);
        for (Pair<T, T> it : hashTable[index]) {
            T keyBuffer = it.getKey();
            if (keyBuffer.getClass() == key.getClass() && keyBuffer == key) {
                return true;
            }
        }
        if (hashTable[index].size() > 4) rebuildTable();
        return false;
    }

    public T value(T key) {
        if (contains(key)) {
            for (Pair<T, T> it : hashTable[indexOf(key)]) {
                if (it.getKey() == key) return it.getValue();
            }
        }
        return null;
    }

    public void add(Pair<T, T> obj) {
        if (obj != null && !contains(obj.getKey())) {
            hashTable[indexOf(obj.getKey())].add(obj);
        }
        totalCells += 1;
    }

    public void addAll(Pair<T, T>[] obj) {
        for (Pair<T, T> aObj : obj) {
            this.add(aObj);
        }
    }

    public void addAll(HashTable obj) {
        this.addAll(obj.toArray());
    }

    public void remove(T key) {
        if (key != null) {
            int index = indexOf(key);
            int cellSize = hashTable[index].size();
            for (int i = 0; i < cellSize; i++) {
                T keyBuffer = hashTable[index].get(i).getKey();
                if (keyBuffer.getClass() == key.getClass() && keyBuffer == key) {
                    hashTable[index].remove(i);
                    totalCells -= 1;
                    return;
                }
            }
        }
    }

    public void removeAll(T[] keys) {
        for (T key : keys) {
            remove(key);
        }
    }

    public void removeAll(HashTable obj) {
        for (List<Pair<T, T>> i : obj.hashTable) {
            for (Pair<T, T> j : i) {
                this.remove(j.getKey());
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        HashTable other = (HashTable) obj;
        if (!tableSize.equals(other.tableSize)) {
            return false;
        }

        for (int i = 0; i < tableSize; i++) {
            int thisSize = hashTable[i].size();

            if (thisSize != other.hashTable[i].size()) {
                return false;
            }

            for (int j = 0; j < thisSize; j++) {
                boolean contains = false;
                if (other.hashTable[i].contains(hashTable[i].get(j))) {
                    contains = true;
                }
                if (!contains) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hashTable) / 2 + tableSize.hashCode() / 2;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.toArray());
    }

    public List<Pair<T, T>> toList() {
        if (totalCells == 0) return null;
        List<Pair<T, T>> list = new LinkedList<>();
        for (List<Pair<T, T>> i : hashTable) {
            list.addAll(i);
        }
        return list;
    }

    public Pair<T, T>[] toArray() {
        if (totalCells == 0) return null;
        Pair[] array = new Pair[totalCells];
        int index = 0;
        for (int i = 0; i < tableSize; i++) {
            int size = hashTable[i].size();
            for (int j = 0; j < size; j++) {
                array[index] = hashTable[i].get(j);
                index++;
            }
        }
        return array;
    }

    public static HashTable toHashTable(Pair[] obj) {
        HashTable table = new HashTable();
        for (Pair it : obj) {
            table.add(it);
        }
        return table;
    }

    public static HashTable toHashTable(Pair[] obj, int size) {
        HashTable table = new HashTable(size);
        for (Pair it : obj) {
            table.add(it);
        }
        return table;
    }
}
