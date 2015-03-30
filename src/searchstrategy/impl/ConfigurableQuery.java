package searchstrategy.impl;

import article.Entry;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import searchstrategy.QueryingStrategy;
import searchstrategy.TokenFilterConfig;

import java.io.IOException;
import java.util.HashMap;

public class ConfigurableQuery extends QueryingStrategy {
    private final String tokenizer;
    private final TokenFilterConfig[] tokenFilterConfigs;

    public ConfigurableQuery(Entry[] entries, double scoreThreshold, String tokenizer, TokenFilterConfig[] tokenFilterConfigs) {
        super(entries, scoreThreshold);
        this.tokenizer = tokenizer;
        this.tokenFilterConfigs = tokenFilterConfigs;
    }

    @Override
    protected QueryParser getQueryParser(IndexSearcher searcher) throws IOException {
        CustomAnalyzer.Builder builder = CustomAnalyzer.builder();
        builder.withTokenizer(tokenizer);
        for (TokenFilterConfig tokenFilterConfig : tokenFilterConfigs) {
            builder.addTokenFilter(tokenFilterConfig.getName(), new HashMap<String, String>(tokenFilterConfig.getParameters()));
        }


        return new QueryParser("contents", builder.build());
    }
}
