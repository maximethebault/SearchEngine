package benchmark;

import article.Article;
import article.ArticleExtractor;
import searchstrategy.SearchingStrategy;
import searchstrategy.TokenFilterConfig;
import searchstrategy.impl.ConfigurableIndexer;
import searchstrategy.impl.ConfigurableQuery;

import java.io.IOException;
import java.util.List;

public class SearchBenchmark {
    public static int BENCHMARK_ITERATION = 50;

    public static void main(String[] args) {
        ArticleExtractor articleExtractor = new ArticleExtractor("resources/cisi/CISI.ALLnettoye");
        List<Article> articles;
        try {
            articles = articleExtractor.getArticles();
        }
        catch (IOException e) {
            System.out.println("Impossible de parser le fichier d'entrée");
            System.exit(1);
            return;
        }
        TokenFilterConfig lowercaseFilterConfig = new TokenFilterConfig("lowercase");
        SearchingStrategy[] searchingStrategies = new SearchingStrategy[] {
                new SearchingStrategy(
                        "Expand query",
                        new ConfigurableIndexer(
                                articles.toArray(new Article[articles.size()]),
                                "whitespace",
                                new TokenFilterConfig[] {
                                        lowercaseFilterConfig
                                },
                                false
                        ),
                        new ConfigurableQuery()
                ),
                new SearchingStrategy(
                        "Expand query",
                        new ConfigurableIndexer(
                                articles.toArray(new Article[articles.size()]),
                                "whitespace",
                                new TokenFilterConfig[] {
                                        lowercaseFilterConfig
                                },
                                false
                        ),
                        new ConfigurableQuery()
                )
        };

        for (SearchingStrategy searchingStrategy : searchingStrategies) {
            System.out.println(searchingStrategy.performBenchmark());
        }
    }
}
