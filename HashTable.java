import java.util.Random;

public class HashTable {

	private static final int MAX_KEY_CHARS = 13;
	private static final int POLYNOMIAL_CONSTANT = 33;
	private static final char QUADRATIC_HASHING = 'Q';
	private static final char DOUBLE_HASHING = 'D';
	private static final char AVAILABLE = 'A';
	private static final char NEG_MARKER = 'N';
	private static final char REPLACE_MARKER = 'R';
	private static final int DEFAULT_N = 101;
	private static final int P = 16908799;
	
	private double rehashThreshold;
	private double rehashValue;
	private boolean isFactorRehash;
	private char collisionType;
	private char emptyMarkerScheme;
	
	private int N, n, a, b;
	
	private Entry[] table;
	
	public HashTable() {
		
	}
	
	public HashTable(double rehashThreshold, double rehashValue, char collisionType, char emptyMarkerScheme) {
		setRehashThreshold(rehashThreshold);
		setRehashFactor(rehashValue);
		setCollisionHandling(collisionType);
		setEmptyMarkerScheme(emptyMarkerScheme);
		initializeTable();
	}
	
	public HashTable(double rehashThreshold, int rehashValue, char collisionType, char emptyMarkerScheme) {
		setRehashThreshold(rehashThreshold);
		setRehashFactor(rehashValue);
		setCollisionHandling(collisionType);
		setEmptyMarkerScheme(emptyMarkerScheme);
		initializeTable();
	}
	
	/**
	 * Hashes a String key using polynomial accumulation
	 * Only uses a maximum of 13 characters, starting from the beginning of the key, to calculate the hash code
	 * Calculates the code in O(13)=O(1)
	 * @param key String key to be hashed
	 * @return int hash code
	 */
	private int hashCode(String key) {
		//Trim key to 13 char's if greater than that
		if (key.length() > MAX_KEY_CHARS)
			key = key.substring(0, MAX_KEY_CHARS + 1);
		
		// Set hashVal to the asci/unicode value of the last character in the key
		int hashVal = key.charAt(key.length() - 1);
		
		// Initiate our index to the second last index in the key
		int i = key.length() - 2;
		
		// Use Horners rule to calculate our hashcode
		while (i >= 0) {
			hashVal = hashVal * POLYNOMIAL_CONSTANT + key.charAt(i);
			i--;
		}
		return hashVal;
	}
	
	private int hashCompression(int hashCode) {
		return ((a * hashCode + b) % P) % N;
	}
	
	/**
	 * Defines the HashTable load factor threshold where 0 <= loadFactor <= 1. If the load factor of the HashTable exceeds
	 * loadFactor then the HashTable should increase.
	 * Values less than zero will be set to zero and values greater than one will be set to one
	 * @param loadFactor HashTable load factor threshold
	 */
	public void setRehashThreshold(double loadFactor) {
		if (loadFactor < 0) {
			rehashThreshold = 0;
			System.out.println("Rehash Threshold must be between 0 and 1. Setting threshold to 0");
		} else if (loadFactor > 1) {
			rehashThreshold = 1;
			System.out.println("Rehash Threshold must be between 0 and 1. Setting threshold to 1");
		} else rehashThreshold = loadFactor;
	}
	
	/**
	 *  Specifies the factor for extending the HashTable size(e.g., if factor =  1.2, the new table size is equal to 1.2 times the old size)
	 *  Value must be greater than one otherwise will be set to 2 by default
	 * @param factor The resize factor
	 */
	public void setRehashFactor(double factor) {
		if (factor > 1) 
			rehashValue = factor;
		else {
			rehashValue = 2;
			System.out.println("Rehash factor must be greater than 1, setting rehash factor to 2.");
		}
		
		isFactorRehash = true;
	}
	
	/**
	 * Indicates the number elements to be added to the new table
	 * Must be greater than 0 or will default to 10
	 * @param number Number of elements that the HashTable will be increased by
	 */
	public void setRehashFactor(int number) {
		if (number > 0)
			rehashValue = number;
		else {
			rehashValue = 10;
			System.out.println("Rehash number must be greather than 0, setting rehash number to 10.");
		}
		
		isFactorRehash = false;
	}
	
	/**
	 * Sets the collision handling of the HashTable to either quadtratic('Q') or double ('D').
	 * If type is not 'D' or 'Q' then double will be set by default
	 * Will only be called on an empty HashTable
	 * @param type Type of collision handling either 'D' or 'Q'
	 */
	public void setCollisionHandling(char type) {
		if (type == QUADRATIC_HASHING || type == DOUBLE_HASHING)
			collisionType = type;
		else {
			collisionType = DOUBLE_HASHING;
			System.out.println("Collision type must be either D or Q. Setting to D by default.");
		}
	}
	
	/**
	 * Defines the way we should marker empty cells in our HashTable
	 * Choices are 'A' to set as AVAILABLE, 'N' to use a Negative Marker to define an emtpy cell, or 'R' to replace empty cells with a previous entry that couldn't be placed because of collision
	 * If one of those choices are not input then default is Negative Marker
	 * @param type
	 */
	public void setEmptyMarkerScheme(char type) {
		if (type == AVAILABLE || type == NEG_MARKER || type == REPLACE_MARKER)
			emptyMarkerScheme = type;
		else {
			type = NEG_MARKER;
		}
	}
	
	private void initializeTable() {
		setN(DEFAULT_N);
		table = new Entry[N];
		n = 0;
	}
	
	private void setN(int newN) {
		N = newN;
		setAB();
	}
	
	private void setAB() {
		Random random = new Random();
		a = random.nextInt(N-2) + 1;
		b = random.nextInt(N-2) + 1;
	}
	
}
