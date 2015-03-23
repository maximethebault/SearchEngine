package searchstrategy.impl;

import article.Article;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import searchstrategy.IndexingStrategy;
import searchstrategy.TokenFilterConfig;

import java.io.IOException;

public class ConfigurableIndexer extends IndexingStrategy {
    private final String tokenizer;
    private final TokenFilterConfig[] tokenFilterConfigs;
    private boolean optimizeIndex;

    public ConfigurableIndexer(Article[] articles, String tokenizer, TokenFilterConfig[] tokenFilterConfigs, boolean optimizeIndex) {
        super(articles);
        this.tokenizer = tokenizer;
        this.tokenFilterConfigs = tokenFilterConfigs;
        this.optimizeIndex = optimizeIndex;
    }

    @Override
    protected IndexWriterConfig getIndexWriterConfig() throws IOException {
        /*
         * Since we want processing at querying time, we use the most basic analyzer
         *
         * StandardAnalyzer/ClassicAnalyzer uses StandardFilter, LowerCaseFilter and StopFilter
         * WhitespaceAnalyzer performs basic tokenization
         * KeywordAnalyzer doesn't tokenize or modify anything
         */
        CustomAnalyzer.Builder builder = CustomAnalyzer.builder();
        builder.withTokenizer(tokenizer);
        for (TokenFilterConfig tokenFilterConfig : tokenFilterConfigs) {
            builder.addTokenFilter(tokenFilterConfig.getName(), tokenFilterConfig.getParameters());
        }
        /*
                .addTokenFilter("standard")
                .addTokenFilter("lowercase")
                .addTokenFilter("stop", "ignoreCase", "false", "words", "stopwords.txt", "format", "wordset")
                */
        return new IndexWriterConfig(builder.build());
    }

    @Override
    protected void afterWriteHook(IndexWriter writer) throws IOException {
        if (optimizeIndex) {
            writer.forceMerge(1);
        }
    }
}
