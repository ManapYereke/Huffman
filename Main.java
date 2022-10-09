import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;

class Tree {// Ағашты құру үшін қажет класс
	char ch;// символ
	int freq;// символдың мәтінде кездесу саны
	Tree right, left;// сол жақтағы, оң жақтағы элементтер

	public Tree(char ch, int freq) {// символ мен оның кездесу санын беру үшін
		this.ch = ch;
		this.freq = freq;
	}

	public Tree(char ch, int freq, Tree left, Tree right) {// символ мен оның кездесу саны,
		// сол, оң жақтағы символдар
		this.ch = ch;
		this.freq = freq;
		this.left = left;
		this.right = right;
	}
}

public class Main {
	public static void buildTree(String text, HashMap<Character, String> code) {// Ағашты құру әдісі
		HashMap<Character, Integer> freq = new HashMap<Character, Integer>();// Әр символдың мәтінде кездесу саны
		for (int i = 0; i < text.length(); i++) {
			if (!freq.containsKey(text.charAt(i))) {
				freq.put(text.charAt(i), 0);// Мәтіндегі символды хеш-мапқа салу
			}
			freq.put(text.charAt(i), freq.get(text.charAt(i)) + 1);// Символ кездескен сайын freq арттырып отыру
		}
		PriorityQueue<Tree> pq = new PriorityQueue<>((l, r) -> l.freq - r.freq);// Сұрыптау үшін қажет
		PriorityQueue<Tree> tmp = new PriorityQueue<Tree>((l, r) -> l.freq - r.freq);// Дәл сондай коллекция, тек мән
																						// сақтау үшін
		System.out.println("Simvol\tKezdesu yqtimaldygy");
		for (HashMap.Entry<Character, Integer> entry : freq.entrySet()) {// хеш-маптан pq-ға салу
			pq.add(new Tree(entry.getKey(), entry.getValue()));
			tmp.add(new Tree(entry.getKey(), entry.getValue()));
		}
		while (tmp.size() != 0) {// Экранға сұрыпталған түрде шығару
			Tree t = tmp.poll();
			float p = (float) t.freq / (float) text.length();
			System.out.printf("%c\t%.2f\n", t.ch, p);
		}
		while (pq.size() != 1) {
			Tree left = pq.poll();// ағашқа минималды элементті алады
			Tree right = pq.poll();// ағашқа 2-ші минималды элементті алады
			int sum = left.freq + right.freq;
			pq.add(new Tree('\0', sum, left, right));// Коллекцияға сол жағына жаңа бұтақ қосады
		}
		Tree tree = pq.peek();
		encoding(tree, "", code);// символдарды кодтау
		System.out.println("\nHaffman kody:\nSimvol\tKody");
		for (HashMap.Entry<Character, String> entry : code.entrySet())
			System.out.println(entry.getKey() + "\t" + entry.getValue());
	}

	public static void encoding(Tree tree, String str, HashMap<Character, String> code) {// кодировка жасау әдісі,
		// рекурсивті
		if (tree == null)
			return;
		if (tree.right == null && tree.left == null)
			code.put(tree.ch, str);// егер сол жағы мен оң жағы бос болса символ мен кодты салады
		encoding(tree.left, str + "0", code);// сол жақ болса, 0 қосылып отыру үшін
		encoding(tree.right, str + "1", code);// оң жақ болса, 1 қосылып отыру үшін
	}
	
	public static void main(String[] args) throws IOException {
		try (BufferedReader file = new BufferedReader(new FileReader("file.txt"))) {
			String text = "", str;
			while ((str = file.readLine()) != null) {
				text += str;
			}
			file.close();
			System.out.println("Matin:\n" + text + "\n");
			HashMap<Character, String> code = new HashMap<Character, String>();
			buildTree(text, code);
			String encoded = "";
			for (int i = 0; i < text.length(); i++)  
				encoded += code.get(text.charAt(i));
			System.out.println("Kodtalgan matin:\n" + encoded);
		}
	}
}
