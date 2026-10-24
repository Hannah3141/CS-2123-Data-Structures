import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
public class compress {
    public static void main(String[] args) throws IOException{
        String bookName = "book0.txt";
        HashMap<Character, Integer> Map = new HashMap<>();//occurrences counting
        FileReader fileReader = new FileReader(bookName);
        char[] charbuff = new char[512]; //read in characters from the book in group of 512
        int charCount = 0; //total character (visible and invisible) count
        while(true) {
            int count = fileReader.read(charbuff);//cbuf may not be fully used if it is the last group
            if(count==-1) break; //end of the book
            charCount += count;
            for(int i=0; i < count; i++) {
                Character c = charbuff[i]; //extract one character, update its counting
                if(Map.containsKey(c)) Map.put(c, Map.get(c) + 1);
                else Map.put(c, 1);
            }
        }
        fileReader.close();
        //now the HashMap named Map has all of the characters that were in the book, correlated to the number of times it appeared
        
        //part 2: build the huffman trie
        PriorityQueue<Node> queue = new PriorityQueue<>(); //make a bunch of trees with one node each
        for(Map.Entry<Character, Integer> entry : Map.entrySet()){
            ArrayList<Character> newChar = new ArrayList<Character>();
            newChar.add(entry.getKey());
            Node node = new Node(newChar, entry.getValue());
            queue.add(node);
        }
        while(queue.size() > 1){
            Node firstHalf = queue.poll();
            Node secondHalf = queue.poll();
            ArrayList combinedChars = new ArrayList(); //the contents of the arrayList should be two smaller arrayLists
            combinedChars.add(firstHalf.getChars());
            combinedChars.add(secondHalf.getChars());
            Node combined = new Node(combinedChars, firstHalf.getValue() + secondHalf.getValue()); //combine the two trees with the smallest weights into one ArrayList with the combined characters and weights
            queue.add(combined); 
        }
        //part 1.3: create a HashMap of the Huffman encodings
        HashMap<Character, String> encodings = new HashMap<>(); //to encode character to encoding
        HashMap<String, Character> revEncodings = new HashMap<>(); //to decode encoding String to character
        Node tree = queue.poll(); //there is only one item in the queue, and it has the whole ArrayList tree
        ArrayList chars = tree.getChars();
        String code = "";
        encode(encodings, revEncodings, chars, code); //constuct the HashMaps
        
        //part 1.4: encode the original file
        fileReader = new FileReader(bookName);
        FileOutputStream byteWriter = new FileOutputStream("binary.bin");
        String encoded = ""; //make one long string with all the encodings in order
        while(true) {
            int count = fileReader.read(charbuff);
            if(count==-1) break; //end of the book
            for(int i=0; i < count; i++) {
                Character c = charbuff[i]; //extract one character
                encoded += encodings.get(c); //add the encoding of that character to the total string
            }
            while(encoded.length() >= 8){
                String s = encoded.substring(0, 8); //store the string in groups of 8 bits into the binary file
                int i = Integer.parseInt(s, 2); //this sign extends the value
                byte b = (byte)i; //this just takes the lowest bits of the int
                byteWriter.write(b);
                encoded = encoded.substring(8); //once the byte is added to the binary file, remove it from the giant encoded string
            }
        }
        while(encoded.length() < 8){
            encoded += "0"; //extend the string to fill the last byte
        }
        int in = Integer.parseInt(encoded, 2);
        byte by = (byte)in; 
        byteWriter.write(by);

        fileReader.close();
        byteWriter.close();

        //part 1.5: calculate the compression ratio
        File bfile = new File("binary.bin");
        System.out.println("Compression ratio: " + bfile.length() / (double)charCount);


        //part 2.1: rebuild the text file from my binary file
        FileInputStream byteReader = new FileInputStream("binary.bin"); //to read from
        FileWriter fileWriter = new FileWriter("bookcopy.txt"); //to write to
        StringBuilder bin = new StringBuilder(); //create a new giant string with all the bytes together
        long length = bfile.length();
        for(int i = 0; i < length; i++){
            byte b = (byte) byteReader.read(); //read each byte as a number
            int temp = Byte.toUnsignedInt(b); 
            String numbers = Integer.toBinaryString(temp); //convert that number to binary as a string
            while(numbers.length() < 8){
                numbers = "0" + numbers; //.toBinaryString() doesn't fill eight bits from the integer value if it doesn't have to, but we expect to it to
            }
            bin.append(numbers);
        }
        String hopefulString = ""; //string to search the HashMap
        int j = 0; //number of bits read
        for(int i = 0; i < charCount;){ 
            hopefulString += bin.charAt(j); //keep adding another bit until it matches something in the hashMap
            j++;
            if(revEncodings.containsKey(hopefulString)) {
                char c = revEncodings.get(hopefulString); 
                fileWriter.append(c); //add the decoded character to the output file
                i++;
                hopefulString = ""; //reset the code to build the next letter
            }
        }
        
        byteReader.close();
        fileWriter.close();

        //part 2.2: check if the original and reconstructed book files are the same
        Path ofile = Path.of(bookName);
        Path nfile = Path.of("bookcopy.txt");
        long equal = Files.mismatch(ofile, nfile);
        System.out.println("Two books are the same: " + (boolean)(equal==-1)); 
    }
    public static HashMap<Character, String> encode(HashMap<Character, String> encodings, HashMap<String, Character> revEncodings, ArrayList chars, String code){
        if(chars.size() <= 1){
            encodings.put((Character)chars.get(0), code); //if there is only one element in the ArrayList, we've found a leaf with a letter, so just add the code we've built
            revEncodings.put(code, (Character)chars.get(0));
            code = code.substring(0, code.length() - 2); //now we'll move back up a level in the tree, so remove a number
        } else {
            encode(encodings, revEncodings, (ArrayList)chars.get(0), code + "0"); //if we went left, add a zero to the code
            encode(encodings, revEncodings, (ArrayList)chars.get(1), code + "1"); //if we went right, add a one
        }
        return encodings;
    }
}
class Node implements Comparable<Node>{
    ArrayList<Character> characters; //one or more arrayLists of characters with the combined weight (if it's a character, there's only one)
    int count; //the number of times the character appears in the book
    
    public Node(ArrayList<Character> c, int i) {
        if(characters == null) {
            characters = c;
        } else characters.addAll(c);
        count = i;
    }
    public ArrayList<Character> getChars(){
        return characters;
    }
    public int getValue() {
        return count;
    }
    public int compareTo(Node other) { //sort by number of occurences/weight
        return this.getValue() - other.getValue();
    }
    public String toString() {
        return String.format("(%d, %s)", characters, count);
    }
}