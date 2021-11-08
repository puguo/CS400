// --== CS400 File Header Information ==--
// Name: Christopher Hall
// Email: cthall2@wisc.edu
// Team: Blue
// Group: JB
// TA: Xinyi
// Lecturer: Florian
// Notes to Grader: 
public class HashBST<KeyType, ValueType> {

	private ValueType value;
	private KeyType key;
	private HashBST<KeyType, ValueType> next;

	/**
	 * 
	 */
	public HashBST(KeyType key, ValueType value) {
		this.key = key;
		this.value = value;
		this.next = next;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public ValueType getValue() {
		return this.value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public KeyType getKey() {
		return this.key;
	}

	/**
	 * 
	 * @return
	 */
	public void setNext(HashBST<KeyType, ValueType> next) {
		this.next = next;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public HashBST<KeyType, ValueType> getNext() {
		return this.next;

	}

	public boolean hasNext() {
		// If next is null
		if (this.next == null) {
			return false;
		}
		return true;

	}
}
