package searchstrategy;

import org.apache.lucene.index.IndexWriterConfig;

abstract class IndexingStrategy {
    public IndexingStrategy() {

    }

    public abstract IndexWriterConfig getIndexWriterConfig();

    public abstract void performIndexing();
}
