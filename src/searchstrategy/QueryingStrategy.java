package searchstrategy;

import javax.naming.directory.SearchResult;

public abstract class QueryingStrategy {
    public abstract SearchResult performQueries();
}
