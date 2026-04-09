package testing;

import main.ReaderTask;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestReader {

	public TestReader() {}

	public ArrayList<String> readFile(BufferedReader reader) throws IOException {
		ArrayList<String> cards = new ArrayList<>();
		String line;
		
		while ((line = reader.readLine()) != null) {
			if (line.equals("OK")) break;

			if (line.equals("CARD")) {
				String id = reader.readLine();
				String name = reader.readLine();
				String rank = reader.readLine();
				String lastSale = reader.readLine();
				cards.add(id + " | " + name + " | " + rank + " | " + lastSale);
			}
		}
		return cards;
	}

	public void executeTask(BufferedReader reader) throws IOException{
		ArrayList<String> cards = readFile(reader);

		System.out.println("ran execute");

		// for each statement
		for (String card : cards) {
			System.out.println(card);
		}
	}

	public static void main(String[] args) {

		TestReader testing = new TestReader();

		try (
			BufferedReader reader = new BufferedReader(new FileReader("src/testing/cardList.txt"));
		) {

			// long way of declaring the anonymous method		
			ReaderTask task = new ReaderTask() {
				@Override
				public void execute(BufferedReader reader) throws IOException {
					testing.executeTask(reader);
				}
			};

			task.execute(reader);
			reader.close();
			
		} catch (FileNotFoundException e) { 
			System.err.println("File not found: " + e);
		} catch (IOException e) { }

	}
	
	
}
