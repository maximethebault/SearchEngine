package searchstrategy;

import javax.naming.directory.SearchResult;

abstract class QueryingStrategy {
    public abstract SearchResult performQuerying();
}
