import javafx.util.Pair;

import java.util.*;

public class task1Class {

     public static void main(String[] args) {

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
            if (hashTableArray[index].get(middle).equals(number)) {
                return true;
            } else {
                if (hashTableArray[index].get(middle) < number) {
                    return binarySearch(middle, right, number, index);
                } else {
                    return binarySearch(left, middle, number, index);
                }
            }
        } else {
            return number.equals(hashTableArray[index].get(left));
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
        return this.hashTableArray.equals(other.hashTableArray);
    }
}

/*
Description
Хэщ мультимапа.
Данные хранятся в виде массива размером, настраиваемом пользователем или 521, если не указано иного.
Индексы значений в таблице являются модулем хэша ключа пары по размеру таблицы,
в соответсвующем данной ячейке подсписке хранится сама пара, а в случае возникновения коллизий в этом подсписке
хранятся и остальные значения, соответсвующие данной ячейке.
Если размер подсписка превысит maxSizeOfCell (по стандарту 5), то происходит пересборка таблицы с увеличением её
размера в multiplierLimitReached раз (по стандарту 1.3).

| Индекс | Подсписок |
    1     {1 to {2}, 5 to {4}, ...}
    2     {4 to {4}, ...}
    3     {11 to {324, 435, ...}, ...}
  */
class HashTable<K, T> implements Iterable<Pair<K, List<T>>> {
    HashTable() {
        hashTable = new ArrayList[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    HashTable(int size) {
        tableSize = size;
        hashTable = new ArrayList[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    HashTable(int size, int maxSizeOfCell) {
        tableSize = size;
        maxSize = maxSizeOfCell;
        hashTable = new ArrayList[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    HashTable(int size, int maxSizeOfCell, double multiplierLimitReached) {
        tableSize = size;
        maxSize = maxSizeOfCell;
        multiplier = multiplierLimitReached;
        hashTable = new ArrayList[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    private Integer maxSize = 5;
    private Integer tableSize = 521;
    private Integer totalCells = 0;
    private Double multiplier = 1.3;
    private ArrayList<Pair<K, List<T>>>[] hashTable;

    /*
    Индекс ячейки таблицы по хэшу ключа.
     */
    private int indexOf(K key) {
        return Math.abs(key.hashCode() % tableSize);
    }

    /*
    Перестройка таблицы с увеличенным в multiplier раз размером.
    Использует прямое добавление в новую таблицу элементов старой.
     */
    private void rebuildTable() {
        tableSize = (int) (tableSize * multiplier);
        ArrayList<Pair<K, List<T>>>[] table = new ArrayList[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = new ArrayList<>();
        }
        for (ArrayList<Pair<K, List<T>>> i : hashTable) {
            for (Pair<K, List<T>> j : i) {
                table[indexOf(j.getKey())].add(j);
            }
        }
        hashTable = table;
    }

    /*
    Определяет, есть ли данный ключ в хэш таблице
    Использует поиск, основанный на хэше и переборе в листе ячейки таблицы.
    В случае, если какая-то ячейка вдруг имеет длину больше maxSize, то вызывает перестройку таблицы.
     */
    public boolean contains(K key) {
        if (key == null) {
            return false;
        }
        int index = indexOf(key);
        for (Pair<K, List<T>> it : hashTable[index]) {
            if (it.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /*
    Определяет, есть ли данное значение в хэш таблице
    Использует полный перебор таблицы для поиска значения.
     */
    public boolean containsValue(T value) {
        if (value == null) {
            return false;
        }
        for (ArrayList<Pair<K, List<T>>> i : hashTable) {
            for (Pair<K, List<T>> j : i) {
                for (T it : j.getValue()) {
                    if (it.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean containsAll(K[] keys) {
        for (K key : keys) {
            if (!contains(key)) {
                return false;
            }
        }
        return true;
    }

    /*
    Возвращает значение по ключу
     */
    public List<T> get(K key) {
        for (Pair<K, List<T>> it : hashTable[indexOf(key)]) {
            if (it.getKey().equals(key)) {
                return it.getValue();
            }
        }
        return null;
    }

    public List<K> getKeys() {
        List<K> list = new LinkedList<>();
        for (int i = 0; i < tableSize; i++) {
            int size = hashTable[i].size();
            for (int j = 0; j < size; j++) {
                list.add(hashTable[i].get(j).getKey());
            }
        }
        return list;
    }

    public List<T>[] getValues() {
        List<T>[] list = new LinkedList[totalCells];
        int last = 0;
        for (int i = 0; i < tableSize; i++) {
            int size = hashTable[i].size();
            for (int j = 0; j < size; j++) {
                List<T> value = hashTable[i].get(j).getValue();
                list[last] = value;
            }
        }
        return list;
    }

    /*
    Добавляет пару в таблицу
     */
    public void add(Pair<K, T> obj) {
        if (obj == null) {
            return;
        }
        K key = obj.getKey();
        if (key == null) {
            return;
        }
        T newValue = obj.getValue();
        int cellIndex = indexOf(obj.getKey());
        int size = hashTable[cellIndex].size();
        for (int i = 0; i < size; i++) {
            if (hashTable[cellIndex].get(i).getKey().equals(key)) {
                List<T> oldValue = hashTable[cellIndex].get(i).getValue();
                oldValue.add(newValue);
                hashTable[cellIndex].remove(i);
                hashTable[cellIndex].add(new Pair(key, oldValue));
                if (hashTable[cellIndex].size() > maxSize - 1) rebuildTable();
                return;
            }
        }
        List<T> list = new LinkedList<>();
        list.add(newValue);
        hashTable[indexOf(obj.getKey())].add(new Pair(key, list));
        totalCells += 1;
    }

    public void addAll(Pair<K, T>[] obj) {
        for (Pair<K, T> aObj : obj) {
            this.add(aObj);
        }
    }

    public void addAll(HashTable obj) {
        this.addAll(obj.toArray());
    }

    /*
    Удаляет пару по ключу, поиск индекса таблицы по хэшу и перебор подтаблицы.
     */
    public void remove(K key) {
        if (key != null) {
            int index = indexOf(key);
            int cellSize = hashTable[index].size();
            for (int i = 0; i < cellSize; i++) {
                K keyBuffer = hashTable[index].get(i).getKey();
                if (keyBuffer.getClass().equals(key.getClass()) && keyBuffer.equals(key)) {
                    hashTable[index].remove(i);
                    totalCells -= 1;
                    return;
                }
            }
        }
    }

    public void removeAll(K[] keys) {
        for (K key : keys) {
            remove(key);
        }
    }

    public void removeAll(HashTable obj) {
        for (List<Pair<K, T>> i : obj.hashTable) {
            for (Pair<K, T> j : i) {
                this.remove(j.getKey());
            }
        }
    }

    public void clear() {
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
        totalCells = 0;
    }

    /*
    Сравнивает таблицы на совпадение размера и наличие всех значений из первой таблицы во второй.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) {
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
                for (int k = 0; k < thisSize; k++) {
                    Pair<K, List<T>> otherPair = (Pair<K, List<T>>) other.hashTable[i].get(k);
                    K otherKey = otherPair.getKey();
                    if (hashTable[i].get(j).getKey().equals(otherKey)) {
                        List<T> thisValue = hashTable[i].get(j).getValue();
                        List<T> otherValue = otherPair.getValue();
                        if (thisValue.containsAll(otherValue) && otherValue.containsAll(thisValue)) {
                            contains = true;
                        }
                    }
                }
                if (!contains) {
                    return false;
                }
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
        if (totalCells.equals(0)) {
            return "[]";
        } else {
            return Arrays.toString(this.toArray());
        }
    }

    @Override
    public Iterator<Pair<K, List<T>>> iterator() {
        List list = this.toList();

        return new Iterator<Pair<K, List<T>>> () {
            private final Iterator<Pair<K, List<T>>> iter = list.iterator();
            private Pair<K, List<T>> current;

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Pair<K, List<T>> next() {
                current = iter.next();
                return current;
            }

            @Override
            public void remove() {
                HashTable.this.remove(current.getKey());
            }
        };
    }

    public List<Pair<K, List<T>>> toList() {
        if (totalCells.equals(0)) return null;
        List<Pair<K, List<T>>> list = new LinkedList<>();
        for (List<Pair<K, List<T>>> i : hashTable) {
            list.addAll(i);
        }
        return list;
    }

    public Pair<K, List<T>>[] toArray() {
        if (totalCells.equals(0)) return null;
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

    public static HashTable toHashTable(Pair[] obj, int size, int maxSizeOfCell) {
        HashTable table = new HashTable(size, maxSizeOfCell);
        for (Pair it : obj) {
            table.add(it);
        }
        return table;
    }

    public static HashTable toHashTable(Pair[] obj, int size, int maxSizeOfCell, double multiplierLimitReached) {
        HashTable table = new HashTable(size, maxSizeOfCell, multiplierLimitReached);
        for (Pair it : obj) {
            table   .add(it);
        }
        return table;
    }
}
