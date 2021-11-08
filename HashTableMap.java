import java.util.NoSuchElementException;

// --== CS400 File Header Information ==--
// Name: Christopher Hall
// Email: cthall2@wisc.edu
// Team: Blue
// Group: JB
// TA: Xinyi
// Lecturer: Florian
// Notes to Grader: 
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

	private HashBST<KeyType, ValueType>[] pairs;
	private int capacity1;
	private int size;

	/**
	 * 
	 * @param capacity
	 */
	public HashTableMap(int capacity) {
		// If capacity is negative
		if (capacity < 0) {
			throw new IllegalArgumentException();
		}
		this.pairs = new HashBST[capacity];
		this.capacity1 = capacity;
		this.size = 0;
	}

	public HashTableMap() {
		this.pairs = new HashBST[10];
		this.capacity1 = 10;
		this.size = 0;
	}

	/**
	 * Return true if the value was successfully added to the array, false otherwise
	 */
	@Override
	public boolean put(KeyType key, ValueType value) {

		// If key or value are null, return false
		if (key == null || containsKey(key)) {
			return false;
		}

		HashBST<KeyType, ValueType> table = new HashBST<KeyType, ValueType>(key, value);
		int index = Math.abs(key.hashCode()) % this.capacity1;

		// Check if the index is occupied
		if (this.pairs[index] == null) {
			this.pairs[index] = table;
			this.size++;
			grow();
			return true;
		}
		// Set table to head and set what was there to next
		else {
			HashBST<KeyType, ValueType> temp = this.pairs[index];
			this.pairs[index] = table;
			table.setNext(temp);
			this.size++;
			grow();
			return true;
		}

	}

	@Override
	public ValueType get(KeyType key) throws NoSuchElementException {

		// If table doesn't have the key
		if (!containsKey(key)) {
			throw new NoSuchElementException();
		}
		if (key == null) {
			return null;
		}

		// Find the index
		int index = Math.abs(key.hashCode()) % this.capacity1;

		// Check if null
		if (this.pairs[index] == null) {
			return null;
		}

		// Current object
		HashBST<KeyType, ValueType> current = this.pairs[index];

		// If current equals key, return it
		if (current.getKey().equals(key)) {
			return current.getValue();
		}

		// Else, check the next objects in the list to see if key matches them
		while (current.hasNext()) {
			current = current.getNext();
			if (current.getKey().equals(key)) {
				return current.getValue();
			}
		}
		return null;

	}

	@Override
	public int size() {
		return this.size;
	}

	/**
	 * If the table reaches 85% capacity or more, double the capacity size
	 * 
	 * @return the new capacity of the table
	 */
	private void grow() {
		// If the length is zero
		if (!((double) size / this.capacity1 >= 0.85)) {
			return;
		}

		HashBST<KeyType, ValueType>[] currentHash = new HashBST[this.size];
		int counter = 0;
		for (int i = 0; i < this.capacity1; ++i) {
			if (this.pairs[i] != null) {
				HashBST<KeyType, ValueType> currentObj = this.pairs[i];
				currentHash[counter] = currentObj;
				++counter;
				while (currentObj.getNext() != null) {
					currentObj = currentObj.getNext();
					currentHash[counter] = currentObj;
					++counter;
				}
			}
			continue;
		}
		clear();
		this.capacity1 *= 2;
		this.pairs = new HashBST[this.capacity1];

		for (int i = 0; i < currentHash.length; ++i) {
			if (currentHash[i] == null) {
				continue;
			}
			HashBST<KeyType, ValueType> newObjLoc = currentHash[i];
			put(newObjLoc.getKey(), newObjLoc.getValue());
			while (newObjLoc.getNext() != null) {
				newObjLoc = newObjLoc.getNext();
				put(newObjLoc.getKey(), newObjLoc.getValue());
			}
		}

		int currCapacity = this.pairs.length;
		double loadFactor = this.size / currCapacity;
		// If size is equal to or greater than 85% of the capacity, double the capacity
		if (loadFactor >= 0.85) {
			currCapacity = this.pairs.length * 2;
			this.capacity1 = currCapacity;
		}
		// Else, do nothing

	}

	/**
	 * Checks if the table contains the designated key value
	 */
	@Override
	public boolean containsKey(KeyType key) {

		// Find index
		int index = Math.abs(key.hashCode()) % this.capacity1;

		// Check if null
		if (this.pairs[index] == null) {
			return false;
		}

		// Current object
		HashBST<KeyType, ValueType> current = this.pairs[index];

		// If current equals key, return it
		if (current.getKey().equals(key)) {
			return true;
		}

		// Else, check the next objects in the list to see if key matches them
		while (current.hasNext()) {
			current = current.getNext();
			if (current.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a key from the table. If the key is not found, return null.
	 */
	@Override
	public ValueType remove(KeyType key) {
		// If size is zero
		if (this.size == 0) {
			return null;
		}

		if (key.equals(null)) {
			return null;
		}

		// If the table does not have the key
		if (!containsKey(key)) {
			return null;
		}

		// Find the index
		int index = Math.abs(key.hashCode()) % this.capacity1;

		// Check if null
		if (this.pairs[index] == null) {
			return null;
		}

		// Current object
		HashBST<KeyType, ValueType> current = this.pairs[index];

		// If current equals key, return it
		if (current.getKey().equals(key)) {
			this.pairs[index] = current.getNext();
			--this.size;
			return current.getValue();
		}
		while (current.getNext().hasNext()) {
			if (current.getNext().getKey().equals(key)) {
				HashBST<KeyType, ValueType> temp = current.getNext();
				current.setNext(temp.getNext());
				--this.size;
				return temp.getValue();
			}
			current = current.getNext();
		}
		return null;
	}

	/**
	 * Empties the table and sets the size to zero
	 */
	@Override
	public void clear() {
		for (int i = 0; i < this.capacity1; ++i) {
			this.pairs[i] = null;
		}
		this.size = 0;
	}

}
