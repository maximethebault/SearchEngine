package benchmark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResults {
    /**
     * Associates a query identifier to the list of resulting texts' IDs
     */
    private Map<Integer, List<Integer>> results;

    public SearchResults() {
        this.results = new HashMap<Integer, List<Integer>>();
    }

    public SearchResults(Map<Integer, List<Integer>> results) {
        this.results = results;
    }

    public Map<Integer, List<Integer>> getResults() {
        return results;
    }

    public void setResults(Map<Integer, List<Integer>> results) {
        this.results = results;
    }

    public void addHit(int queryId, int articleId) {

    }
}