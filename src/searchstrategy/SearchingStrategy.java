package searchstrategy;

import javax.naming.directory.SearchResult;

public class SearchingStrategy {
    private IndexingStrategy indexingStrategy;
    private QueryingStrategy queryingStrategy;

    public void performEvaluation() {
        indexingStrategy.performIndexing(null, null);
        SearchResult searchResult = queryingStrategy.performQuerying();
    }
}
