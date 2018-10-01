package webseiteBegriffe;

public class Words {
    
    private final String word;
    private int counter;
    
// -------------------- Constructor
    
    public Words(String word, int counter) {
        this.word = word;
        this.counter = counter;
    }
    
// -------------------- Counter

    public void counter() {
        this.counter++;
    }
    
// -------------------- Setters & Getters
    
    public int getCounter() {
        return this.counter;
    }
    
    public String getWord() {
        return this.word;
    }
    
    
}
