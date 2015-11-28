
public class Entry {

	private String key;
	private String value;
	
	public Entry() {
		key = "default";
		value = "default";
	}
	
	public Entry(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
}
