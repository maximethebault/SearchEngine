package benchmark;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QueryBenchmark {
    private SearchResults actualSearchResults;
    private SearchResults expectedSearchResults;

    public QueryBenchmark() {
        fetchExpectedResults("resources/cisi/CISI.REL");
    }

    public SearchResults getActualSearchResults() {
        return actualSearchResults;
    }

    public void setActualSearchResults(SearchResults actualSearchResults) {
        this.actualSearchResults = actualSearchResults;
    }

    public void fetchExpectedResults(String filePath) {
        expectedSearchResults = new SearchResults();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                Scanner scanner = new Scanner(sCurrentLine);
                if (!scanner.hasNextInt()) {
                    continue;
                }
                int queryId = scanner.nextInt();
                if (!scanner.hasNextInt()) {
                    continue;
                }
                int articleId = scanner.nextInt();

                expectedSearchResults.addHit(queryId, articleId);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param queryId
     *         ID of the query to get the missed matches for
     *
     * @return the number of articles that should have matched the query, but weren't present in the results, or -1 if invalid queryId
     */
    public int getMissedMatches(int queryId) {

        if (!actualSearchResults.getResults().containsKey(queryId)) {
            return expectedSearchResults.getResults().containsKey(queryId) ? expectedSearchResults.getResults().get(queryId).size() : -1;
        }

        int missedMatches = 0;

        for (int expectedArticleId : expectedSearchResults.getResults().get(queryId)) {
            if (!actualSearchResults.getResults().get(queryId).contains(expectedArticleId)) {
                missedMatches++;
            }
        }

        return missedMatches;
    }

    /**
     * @param queryId
     *         ID of the query to get the bad matches for
     *
     * @return the number of articles that shouldn't have appeared in the results, or -1 if invalid queryId
     */
    public int getBadMacthes(int queryId) {

        if (!actualSearchResults.getResults().containsKey(queryId)) {
            return expectedSearchResults.getResults().containsKey(queryId) ? 0 : -1;
        }

        // we'll copy the actual matching articles, iterate through the expected results and remove the expected ids as we meet them
        List<Integer> actualArticleIds = new ArrayList<Integer>(actualSearchResults.getResults().get(queryId));

        for (Integer expectedArticleId : expectedSearchResults.getResults().get(queryId)) {
            actualArticleIds.remove(expectedArticleId);
        }

        return actualArticleIds.size();
    }

    /**
     * @param queryId
     *         ID of the query to get the number of matches for
     *
     * @return the total number of (actual) matches
     */
    public int getActualMatchesSize(int queryId) {
        if (!actualSearchResults.getResults().containsKey(queryId)) {
            throw new RuntimeException("Unknown query ID in actual results");
        }
        else {
            return actualSearchResults.getResults().get(queryId).size();
        }
    }

    /**
     * @param queryId
     *         ID of the query to get the number of matches for
     *
     * @return the total number of (expected) matches, or -1 if invalid queryId
     */
    public int getExpectedMatchesSize(int queryId) {
        if (!expectedSearchResults.getResults().containsKey(queryId)) {
            throw new RuntimeException("Unknown query ID in expected results");
        }
        else {
            return expectedSearchResults.getResults().get(queryId).size();
        }
    }

    public float getPrecision() {
        double accumulatedPrecision = 0;
        int nbValues = 0;

        for (int queryId : expectedSearchResults.getResults().keySet()) {
            double actualMatchesSize = getActualMatchesSize(queryId);
            double badMatches = getBadMacthes(queryId);
            if (actualMatchesSize > 0) {
                accumulatedPrecision += (actualMatchesSize - badMatches) / actualMatchesSize;
                nbValues++;
            }
        }

        return (float) (accumulatedPrecision / (double) nbValues);
    }

    public float getRecall() {
        double accumulatedRecall = 0;

        for (int queryId : expectedSearchResults.getResults().keySet()) {
            double actualMatchesSize = getActualMatchesSize(queryId);
            double expectedMatchesSize = getExpectedMatchesSize(queryId);
            double badMatches = getBadMacthes(queryId);
            accumulatedRecall += (actualMatchesSize - badMatches) / expectedMatchesSize;
        }

        return (float) (accumulatedRecall / (double) expectedSearchResults.getResults().size());
    }
}
