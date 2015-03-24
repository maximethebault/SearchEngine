package benchmark;

import article.Entry;
import article.EntryExtractor;
import searchstrategy.SearchingStrategy;
import searchstrategy.TokenFilterConfig;
import searchstrategy.impl.ConfigurableIndexer;
import searchstrategy.impl.ConfigurableQuery;

import java.io.IOException;
import java.util.List;

public class SearchBenchmark {
    public static int BENCHMARK_ITERATION = 50;

    public static void main(String[] args) {
        EntryExtractor articleExtractor = new EntryExtractor("resources/cisi/CISI.ALLnettoye");
        List<Entry> articles;
        try {
            articles = articleExtractor.getEntries();
        }
        catch (IOException e) {
            System.out.println("Impossible de parser le fichier d'articles en entrée");
            System.exit(1);
            return;
        }

        EntryExtractor queryExtractor = new EntryExtractor("resources/cisi/CISI.QRY");
        List<Entry> queries;
        try {
            queries = queryExtractor.getEntries();
        }
        catch (IOException e) {
            System.out.println("Impossible de parser le fichier de requêtes en entrée");
            System.exit(1);
            return;
        }


        TokenFilterConfig lowercaseFilterConfig = new TokenFilterConfig("lowercase");
        SearchingStrategy[] searchingStrategies = new SearchingStrategy[] {
                new SearchingStrategy(
                        "Expand query",
                        new ConfigurableIndexer(
                                articles.toArray(new Entry[articles.size()]),
                                "whitespace",
                                new TokenFilterConfig[] {
                                        lowercaseFilterConfig
                                },
                                false
                        ),
                        new ConfigurableQuery(
                                queries.toArray(new Entry[queries.size()]),
                                "whitespace",
                                new TokenFilterConfig[] {
                                        lowercaseFilterConfig
                                }
                        )
                )/*,
                new SearchingStrategy(
                        "Expand query",
                        new ConfigurableIndexer(
                                articles.toArray(new Entry[articles.size()]),
                                "whitespace",
                                new TokenFilterConfig[] {
                                        lowercaseFilterConfig
                                },
                                false
                        ),
                        new ConfigurableQuery(
                                queries.toArray(new Entry[articles.size()]),
                                "whitespace",
                                new TokenFilterConfig[] {
                                        lowercaseFilterConfig
                                }
                        )
                )*/
        };

        for (SearchingStrategy searchingStrategy : searchingStrategies) {
            System.out.println(searchingStrategy.performBenchmark());
        }
    }
}
