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

/*
Description
Хэш таблица с парами различных типов.
Данные хранятся в виде массива размером, настраиваемом пользователем или 521, если не указано иного.
Индексы значений в таблице являются модулем хэша ключа пары по размеру таблицы,
в соответсвующем данной ячейке подсписке хранится сама пара, а в случае возникновения коллизий в этом подсписке
хранятся и остальные значения, соответсвующие данной ячейке.
Если размер подсписка превысит maxSizeOfCell (по стандарту 5), то происходит пересборка таблицы с увеличением её
размера в multiplierLimitReached раз (по стандарту 1.3).
Список так же позволяет хранить несколько значений, соответсвующих одному ключу (например, телефонная книга).
| Индекс | Подсписок |
    1     {1 to 2, "somethingWithSameHash" to true, ...}
    2     {4 to "test", ...}
    3     {'c' to {"testOther", true, "+71234567890", ...}, ...}
  */
class HashTable<T> {
    HashTable() {
        hashTable = new List[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    HashTable(int size) {
        tableSize = size;
        hashTable = new List[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    HashTable(int size, int maxSizeOfCell) {
        tableSize = size;
        maxSize = maxSizeOfCell;
        hashTable = new List[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    HashTable(int size, int maxSizeOfCell, double multiplierLimitReached) {
        tableSize = size;
        maxSize = maxSizeOfCell;
        multiplier = multiplierLimitReached;
        hashTable = new List[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    private Integer maxSize = 5;
    private Integer tableSize = 521;
    private Integer totalCells = 0;
    private Double multiplier = 1.3;
    private List<Pair<T, T>>[] hashTable;

    /*
    Индекс ячейки таблицы по хэшу ключа.
     */
    private int indexOf(T key) {
        return key.hashCode() % tableSize;
    }

    /*
    Перестройка таблицы с увеличенным в multiplier раз размером.
    Использует прямое добавление в новую таблицу элементов старой.
     */
    private void rebuildTable() {
        tableSize = (int) (tableSize * multiplier);
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

    /*
    Определяет, есть ли данный ключ в хэш таблице
    Использует поиск, основанный на хэше и переборе в листе ячейки таблицы.
    В случае, если какая-то ячейка вдруг имеет длину больше maxSize, то вызывает перестройку таблицы.
     */
    public boolean contains(T key) {
        if (key == null) {
            return false;
        }
        int index = indexOf(key);
        for (Pair<T, T> it : hashTable[index]) {
            T keyBuffer = it.getKey();
            if (keyBuffer == key) {
                return true;
            }
        }
        if (hashTable[index].size() > maxSize - 1) rebuildTable();
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
        for (List<Pair<T, T>> i : hashTable) {
            for (Pair<T, T> j : i) {
                if (j.getValue().getClass() == LinkedList.class) {
                    for (T it : (LinkedList<T>) (j.getValue())) {
                        if (it == value) return true;
                    }
                } else {
                    if (j.getValue() == value) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
    Возвращает значение по ключу
     */
    public T get(T key) {
        for (Pair<T, T> it : hashTable[indexOf(key)]) {
            if (it.getKey() == key) return it.getValue();
        }
        return null;
    }

    public List<T> getKeys() {
        List<T> list = new LinkedList<>();
        for (int i = 0; i < tableSize; i++) {
            int size = hashTable[i].size();
            for (int j = 0; j < size; j++) {
                list.add(hashTable[i].get(j).getKey());
            }
        }
        return list;
    }

    /*
    TODO() ПРОБЛЕМА:
    См. метод add
     */
    public List<T> getValues() {
        List<T> list = new LinkedList<>();
        for (int i = 0; i < tableSize; i++) {
            int size = hashTable[i].size();
            for (int j = 0; j < size; j++) {
                T value = hashTable[i].get(j).getValue();
                if (value.getClass() == LinkedList.class) {
                    list.addAll((List) value);
                } else {
                    list.add(value);
                }
            }
        }
        return list;
    }

    /*
    Добавляет пару в таблицу, если такой ключ уже есть в таблице, то создает Pair<T, LinkedList<T>>, и помещает старые
    и новое значения в этот лист.
    TODO() ПРОБЛЕМА:
    Если пользователь сначала будет в паре хранить List, то все последующие значения будут помещаться в тот самый лист,
    вместо создания нового для разных значений
    "test" to {1, 2, 3} -> "test" to {1, 2, 3, 4} вместо "test" to {1, 2, 3} -> "test" to {{1, 2, 3}, 4}
     */
    public void add(Pair<T, T> obj) {
        if (obj == null) {
            return;
        }
        T key = obj.getKey();
        if (key == null) {
            return;
        }
        T newValue = obj.getValue();
        int cellIndex = indexOf(obj.getKey());
        int size = hashTable[cellIndex].size();
        for (int i = 0; i < size; i++) {
            if (hashTable[cellIndex].get(i).getKey() == key) {
                T oldValue = hashTable[cellIndex].get(i).getValue();
                List<T> newList = new LinkedList();
                if (oldValue.getClass() == LinkedList.class) {
                    newList.addAll((List) oldValue);
                } else {
                    newList.add(oldValue);
                }
                newList.add(newValue);
                Pair<T, LinkedList<T>> newPair = new Pair(key, newList);
                hashTable[cellIndex].remove(i);
                hashTable[cellIndex].add((Pair<T, T>) newPair);
                return;
            }
        }
        hashTable[indexOf(obj.getKey())].add(obj);
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

    /*
    Удаляет пару по ключу, поиск индекса таблицы по хэшу и перебор подтаблицы.
     */
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

    public void clear() {
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    /*
    Сравнивает таблицы на совпадение размера и наличие всех значений из первой таблицы во второй.
     */
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
            table.add(it);
        }
        return table;
    }
}
