import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
        
        FileReader file_to_read = new FileReader(this.inputFileName);
        BufferedReader br = new BufferedReader(file_to_read);
        //System.out.println(br.readLine());
        
        Integer[] IndexArray = this.getIndexes();      
        List<String> lines = new ArrayList<String>(50000);
        List<String> chosed_lines = new ArrayList<String>(10000);
        List<String> ListWord = new ArrayList<String>();
        String str = "";
        int i = 0;
        //System.out.println(lines.size());
        while((str = br.readLine()) != null)
        {
        	//System.out.println(i);
        	lines.add(i, str);
        	i++;
        }
        
        for(int index : IndexArray)
        {
        	
        	String line = lines.get(index);
            StringTokenizer st = new StringTokenizer(line,delimiters);
            while (st.hasMoreTokens())
            {
	            String word = st.nextToken().toLowerCase().trim();
	            ListWord.add(word);
            }
        }
        
        ArrayList<String> sWA = new ArrayList(Arrays.asList(stopWordsArray));
        ListWord.removeAll(sWA);
        //System.out.println(ListWord);
        
        Map<String, Integer> WordMap = new HashMap<String, Integer>();
        for(String word : ListWord)
        {
        	//WordMap.put(word, WordMap.get(word) + 1 );
        	if( WordMap.containsKey(word) )
        		WordMap.put(word, WordMap.get(word) + 1 );
        	else
        		WordMap.put(word, 1);
        }
        
        ArrayList<MyEntry > SortList = new ArrayList<MyEntry>();
        Iterator it =  WordMap.entrySet().iterator();
        while ( it.hasNext())
        {
        	Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)it.next();
        	MyEntry ListEntry = new MyEntry();
        	ListEntry.key = entry.getKey();
        	ListEntry.value = entry.getValue();
        	SortList.add(ListEntry);
        }
        
        
        Collections.sort(SortList);
        
        for( i = 0; i < 20; i++)
        {
        	ret[i] = SortList.get(i).getKey();
        }

        return ret;    
    }
    

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}


class MyEntry implements Comparable {
	String key;
	Integer value;
	
	public String getKey() {
		return key;
	}
	public Integer getValue() {
		return value;
	}
	@Override
	public int compareTo(Object o) {
		
		return (this.value - ((MyEntry) o).getValue() );

	} 

}