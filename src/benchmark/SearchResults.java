package benchmark;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SearchResults {
    /**
     * Associates a query identifier to the list of resulting texts' IDs
     */
    private Map<Integer, List<Integer>> results;
    private List<int[]> expectedResults;

    public SearchResults() {
        this.results = new HashMap<Integer, List<Integer>>();
        this.expectedResults = new ArrayList<int[]>();
        fetchExpectedResults("resources/cisi/CISI.REL");
    }

    public SearchResults(Map<Integer, List<Integer>> results) {
        this.results = results;
        this.expectedResults = new ArrayList<int[]>();
        fetchExpectedResults("resources/cisi/CISI.REL");
    }


    public Map<Integer, List<Integer>> getResults() {
        return results;
    }

    public void setResults(Map<Integer, List<Integer>> results) {
        this.results = results;
    }

    public void fetchExpectedResults(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {

                Scanner scanner = new Scanner(sCurrentLine);
                int[] numberForCurrentLine = new int[3];
                int i = 0;
                while (scanner.hasNextInt()) {
                    numberForCurrentLine[i++] = scanner.nextInt();
                }

                this.expectedResults.add(numberForCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getUnrecognisedResponses() {
        int unrecognisedResponses = 0;

        for (int[] tab : this.expectedResults) {
            int requestNumber = tab[0];
            int textNumber = tab[1];

            if (!results.containsKey(requestNumber) || !results.get(requestNumber).contains(textNumber)) {
                unrecognisedResponses++;
            }
        }
        return unrecognisedResponses;
    }

    public int getUnexpectedResults() {
        int unexpectedResults = 0;
        boolean isPresent = false;

        for (Integer requestNumber : this.results.keySet()) {
            for (Integer textNumber : this.results.get(requestNumber)) {
                for (int[] tab : expectedResults) {
                    if (tab[0] == requestNumber && tab[1] == textNumber) {
                        isPresent = true;
                    }
                }
            }

            if (isPresent) {
                unexpectedResults++;
                isPresent = false;
            }
        }
        return unexpectedResults;
    }

    public static void main(String[] args) {
        SearchResults sr = new SearchResults();
        int unrecognised = sr.getUnrecognisedResponses();
        System.out.println("Unrecognised responses : " + unrecognised);
        int unexpected = sr.getUnexpectedResults();
        System.out.println("Unexpected responses : " + unexpected);
    }
}