package webseiteBegriffe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebseiteBegriffe {

    static ArrayList<Words> topWords = new ArrayList<>();
    static ArrayList<String> filteredWordsOfText = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Pumpkin").get();

        System.out.println("\nNumber of words in text: " + doc.text().split("(\\s|\\p{Punct})+").length);
        System.out.println("------------------------------------------------");

        String[] wordsOfText = doc.text().split("(\\s|\\p{Punct})+");

//        System.out.println("\nNumber of words in Array wordsOfText: " + wordsOfText.length);
//        System.out.println("------------------------------------------------");

        filterWordsOfText(wordsOfText);
        
        System.out.println("\nNumber of words (filtered): " + filteredWordsOfText.size());
        System.out.println("------------------------------------------------");
//        System.out.println("\nFiltered selection:\n\n" + filteredWordsOfText.toString());
        
        makeTopList();

        System.out.println("\nNumber of words in a top list (more than 1 time in a text): " + topWords.size());
        System.out.println("-----------------------------------------------------------------");

//        System.out.println("All Words in a top list (more than 1 time in a text): \n");
//        for (int i = 0; i < topWords.size(); i++) {
//            System.out.println(topWords.get(i).getWord() + " kommt " + topWords.get(i).getCounter() + " Mal vor");
//        }
        
        sortTopTenWords();
        
        printTopTenWords();

    }

// -------------------- Methods
    
// -------------------- Filter
    
    static void filterWordsOfText(String[] wordsOfText) throws FileNotFoundException {
        boolean prohibitedWord = false;
        for (String word : wordsOfText) {
            if (word.length() > 2) {
                BufferedReader br = new BufferedReader(new FileReader("src\\webseiteBegriffe\\negativList.txt"));
                String line;
                try {
                    while ((line = br.readLine()) != null) {
                        if (prohibitedWord || word.equalsIgnoreCase(line)) {
                            prohibitedWord = true;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(WebseiteBegriffe.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!prohibitedWord) {
                    filteredWordsOfText.add(word);
                }
                prohibitedWord = false;
            }
        }
    }
    
// -------------------- make top-list
    
    static void makeTopList() {

        boolean alreadyTop = false;
        
        for (int i = 0; i < filteredWordsOfText.size(); i++) { // ein Wort aus dem Array, wo alle Woerter des Textes gespeichert sind,
            for (int x = 0; x < topWords.size(); x++) { // wird in ArrayList der Top-Woerter gesucht
                if (filteredWordsOfText.get(i).equalsIgnoreCase(topWords.get(x).getWord())) { // wenn das Wort schon da ist (erst ab zweite Runde moeglich)
                    alreadyTop = true; // wird alreadyTop auf true gesetzt
                }
            }
            if (!alreadyTop) { // wenn das Wort noch nicht in der ArrayList der Top-Woerter enthalten ist
                for (int j = 0; j < filteredWordsOfText.size(); j++) { // wird es mit allen Woerter in dem gleichen Array vergliechen
                    if (filteredWordsOfText.get(i).equalsIgnoreCase(filteredWordsOfText.get(j)) && i != j) { // wenn 2 gleiche Woerter gefunden werden
                        
                        for (int y = 0; y < topWords.size(); y++) { // wird das Wort in der ArrayList vom Top-Woerter gesucht
                            if (filteredWordsOfText.get(i).equalsIgnoreCase(topWords.get(y).getWord())) { // wenn das Wort schon in Top-Woerter enthalten ist
                                topWords.get(y).counter(); // wird sein Counter hochgezaehlt
//                                System.out.println("Counter von " + topWords.get(y).getWord() + " ist " + topWords.get(y).getCounter());
                                alreadyTop = true; // alreadyTop wird auf true gesetzt damit keinen neuen Eintrag in ArrayList erfolgt
                            }
                        }
                        
                        if (!alreadyTop) { // wenn alreadyTop false ist (also das Wort noch nicht in dem Array der Top-Woerter ist)
                            topWords.add(new Words(filteredWordsOfText.get(i), 2)); // wird das Wort zu dem Array der Top-Woerter hinzugefuegt mit dem Counter-Wert 2
//                            System.out.println("Counter von " + topWords.get(topWords.size()-1).getWord() + " ist " + topWords.get(topWords.size()-1).getCounter());
                            alreadyTop = true;
                        }
                        
                    }
                }
            }
            alreadyTop = false; // fuer die naechste Runde
        }
    }    

// -------------------- sort top-10

static void sortTopTenWords() {
        for (int i = 0; i < 10; i++) { // sortiert nur 
            for (int j = topWords.size() - 1; j > 0; j--) {
                if (topWords.get(j).getCounter() > topWords.get(j - 1).getCounter()) {
                    Words temp = topWords.get(j);
                    topWords.set(j, topWords.get(j - 1));
                    topWords.set(j - 1, temp);
                }
            }
        }
    }

// -------------------- print top-10
    
    static void printTopTenWords() {
        System.out.println("\n10 Top-Woerter:");
        System.out.println("----------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(topWords.get(i).getWord() + " kommt " + topWords.get(i).getCounter() + " Mal vor");
        }
    }

}