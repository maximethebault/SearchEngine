package benchmark;

import article.Entry;
import article.EntryExtractor;
import searchstrategy.SearchBenchmark;
import searchstrategy.TokenFilterConfig;
import searchstrategy.impl.ConfigurableIndexer;
import searchstrategy.impl.ConfigurableQuery;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static int BENCHMARK_ITERATION = 50;

    public static void main(String[] args) {
        EntryExtractor articleExtractor = new EntryExtractor("resources/cisi/CISI.ALLnettoye");
        List<Entry> articles;
        try {
            articles = articleExtractor.getEntries();
        }
        catch (IOException e) {
            System.out.println("Impossible de parser le fichier d'articles en entr�e");
            System.exit(1);
            return;
        }

        EntryExtractor queryExtractor = new EntryExtractor("resources/cisi/CISI.QRY");
        List<Entry> queries;
        try {
            queries = queryExtractor.getEntries();
        }
        catch (IOException e) {
            System.out.println("Impossible de parser le fichier de requ�tes en entr�e");
            System.exit(1);
            return;
        }

        /*
         * For each strategy, for index as well as query, we've to set up the analyzer
         *
         * The configuration of the Analyzer is done in 2 steps:
         * 1) Tokenizer
         * We've got to choose a tokenizer, between the 3 following (from the most sophicasted to the most basic one):
         * - StandardAnalyzer/ClassicAnalyzer uses StandardFilter, LowerCaseFilter and StopFilter (pre-defined filters, see "2) TokenFilter"
         *  e.g. "Hello World.Fine of?" -> hello, world, fine ("of" gets deleted by the StopWords filter)
         * - WhitespaceAnalyzer performs basic tokenization on whitespaces
         *  e.g. "Hello World.Fine of?" -> Hello, World.Fine, of (or something like this, idk how it really works on ponctuation)
         * - KeywordAnalyzer doesn't tokenize or modify anything
         *  e.g. "Hello World.Fine of?" -> Hello World.Fine of?
         *
         * 2) TokenFilter
         * Once we've got the tokens, any transformations (from zero to *) can be applied before they're written into the index:
         * - ClassicFilter, which removes dots from acronyms and 's from the end of tokens
         * - LowercaseFilter, which lowercases the letters in each token (obvious? nah)
         * - StopFilter (stop refers to stop words), which removes a predefined list of tokens (usually the most common & meaningless words in languages, like "of")
         * - ...
         *
         * Full list at (very well done doc!):
         * https://wiki.apache.org/solr/AnalyzersTokenizersTokenFilters
         *
         *
         * See the following example on how to configure an analyzer:
         *
         * new ConfigurableIndexer(
               articles.toArray(new Entry[articles.size()]),
               "whitespace", <-- ****** Name of the tokenizer here *******
               new TokenFilterConfig[] { <-- ****** Different tokenfilters here *******
                     lowercaseFilterConfig,
                     stopwordsFilterConfig
               },
               false
           )
         */

        TokenFilterConfig lowercaseFilterConfig = new TokenFilterConfig("lowercase");
        Map<String, String> params = new HashMap<String, String>();
        params.put("ignoreCase", "true");
        params.put("words", "cisi/motsvides.txt");
        params.put("format", "wordset");
        TokenFilterConfig stopwordsFilterConfig = new TokenFilterConfig("stop", params);
        SearchBenchmark[] searchBenchmarks = new SearchBenchmark[] {
                new SearchBenchmark(
                        "Expand query",
                        new ConfigurableIndexer(
                                articles.toArray(new Entry[articles.size()]),
                                "whitespace",
                                new TokenFilterConfig[] {
                                        lowercaseFilterConfig,
                                        stopwordsFilterConfig
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
                ),
                new SearchBenchmark(
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
                )
        };

        for (SearchBenchmark searchBenchmark : searchBenchmarks) {
            System.out.println(searchBenchmark.performBenchmark());
        }
    }
}
