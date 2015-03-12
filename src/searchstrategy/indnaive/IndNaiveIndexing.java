package searchstrategy.indnaive;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import searchstrategy.IndexingStrategy;

public class IndNaiveIndexing extends IndexingStrategy {
    @Override
    protected IndexWriterConfig getIndexWriterConfig() {
        /*
         * Since we want processing at querying time, we use the most basic analyzer
         *
         * StandardAnalyzer/ClassicAnalyzer uses StandardFilter, LowerCaseFilter and StopFilter
         * WhitespaceAnalyzer performs basic tokenization
         * KeywordAnalyzer doesn't tokenize or modify anything
         */
        Analyzer analyzer = new KeywordAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        return iwc;
    }

    @Override
    protected void afterWriteHook(IndexWriter writer) {

    }
}
