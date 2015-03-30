package searchstrategy.impl;

import article.Entry;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import searchstrategy.IndexingStrategy;
import searchstrategy.TokenFilterConfig;

import java.io.IOException;
import java.util.HashMap;

public class ConfigurableIndexer extends IndexingStrategy {
    private final String tokenizer;
    private final TokenFilterConfig[] tokenFilterConfigs;
    private boolean optimizeIndex;

    public ConfigurableIndexer(Entry[] articles, String tokenizer, TokenFilterConfig[] tokenFilterConfigs, boolean optimizeIndex) {
        super(articles);
        this.tokenizer = tokenizer;
        this.tokenFilterConfigs = tokenFilterConfigs;
        this.optimizeIndex = optimizeIndex;
    }

    @Override
    protected IndexWriterConfig getIndexWriterConfig() throws IOException {
        CustomAnalyzer.Builder builder = CustomAnalyzer.builder();
        builder.withTokenizer(tokenizer);
        for (TokenFilterConfig tokenFilterConfig : tokenFilterConfigs) {
            builder.addTokenFilter(tokenFilterConfig.getName(), new HashMap<String, String>(tokenFilterConfig.getParameters()));
        }
        return new IndexWriterConfig(builder.build());
    }

    @Override
    protected void afterWriteHook(IndexWriter writer) throws IOException {
        if (optimizeIndex) {
            writer.forceMerge(1);
        }
    }
}
