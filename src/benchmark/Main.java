package benchmark;

import article.Entry;
import article.EntryExtractor;
import searchstrategy.TokenFilterConfig;
import searchstrategy.impl.ConfigurableIndexer;
import searchstrategy.impl.ConfigurableQuery;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static int BENCHMARK_ITERATION = 25;

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

        /*
         * For each strategy, for index as well as query, we've to set up the analyzer
         *
         * The configuration of the Analyzer is done in 2 steps:
         * 1) Tokenizer
         * We've got to choose a tokenizer, between the 3 following (from the most sophicasted to the most basic one):
         * - StandardTokenizer performs tokenization on usual word-separators (whitespaces, -, some poncutation)
         *  e.g. "Hello Marc-Antoine World.Fine of?" -> Hello, Marc, Antoine, World.Fine, of
         * - WhitespaceTokenizer performs basic tokenization on whitespaces
         *  e.g. "Hello Marc-Antoine World.Fine of?" -> Hello, Marc-Antoine, World.Fine, of (or something like this, idk how it really works on ponctuation)
         * - KeywordTokenizer doesn't tokenize or modify anything
         *  e.g. "Hello Marc-Antoine World.Fine of?" -> Hello Marc-Antoine World.Fine of?
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

        Map<String, String> paramsSynExpanded = new HashMap<String, String>();
        paramsSynExpanded.put("synonyms", "prolog/wn_s.pl");
        paramsSynExpanded.put("expand", "true");
        paramsSynExpanded.put("format", "wordnet");
        TokenFilterConfig synonymFilterConfigExpanded = new TokenFilterConfig("synonym", paramsSynExpanded);

        Map<String, String> paramsSynNotExpanded = new HashMap<String, String>();
        paramsSynNotExpanded.put("synonyms", "prolog/wn_s.pl");
        paramsSynNotExpanded.put("expand", "false");
        paramsSynNotExpanded.put("format", "wordnet");
        TokenFilterConfig synonymFilterConfigNotExpanded = new TokenFilterConfig("synonym", paramsSynNotExpanded);

        TokenFilterConfig apostropheFilterConfig = new TokenFilterConfig("apostrophe");

        TokenFilterConfig classicFilterConfig = new TokenFilterConfig("classic");

        TokenFilterConfig trimFilterConfig = new TokenFilterConfig("trim");

        Map<String, String> paramsEdgeNgram = new HashMap<String, String>();
        paramsEdgeNgram.put("minGramSize", "5");
        paramsEdgeNgram.put("maxGramSize", "15");
        TokenFilterConfig edgeNGramFilterConfig = new TokenFilterConfig("edgengram", paramsEdgeNgram);

        TokenFilterConfig removeDuplicatesFilterConfig = new TokenFilterConfig("removeduplicates");

        Map<String, String> paramsASCIIFolding = new HashMap<String, String>();
        paramsASCIIFolding.put("preserveOriginal", "true");
        TokenFilterConfig ASCIIFoldingFilterConfig = new TokenFilterConfig("ASCIIFolding", paramsASCIIFolding);

        Map<String, String> paramsWordDelimiter = new HashMap<String, String>();
        paramsWordDelimiter.put("splitOnCaseChange", "1");
        paramsWordDelimiter.put("splitOnNumerics", "1");
        paramsWordDelimiter.put("stemEnglishPossessive", "1");
        paramsWordDelimiter.put("generateWordParts", "1");
        paramsWordDelimiter.put("generateNumberParts", "1");
        paramsWordDelimiter.put("preserveOriginal", "1");
        TokenFilterConfig worddelimiterFilterConfig = new TokenFilterConfig("worddelimiter", paramsWordDelimiter);

        Map<String, String> paramsSnowBallPorter = new HashMap<String, String>();
        paramsSnowBallPorter.put("language", "English");
        TokenFilterConfig SnowBallPorterFilterConfig = new TokenFilterConfig("SnowBallPorter", paramsSnowBallPorter);

        SearchBenchmark[] searchBenchmarks = new SearchBenchmark[] {
                new SearchBenchmark(
                        "Custom analyzer",
                        new ConfigurableIndexer(
                                articles.toArray(new Entry[articles.size()]),
                                "lowercase",
                                new TokenFilterConfig[] {
                                        stopwordsFilterConfig,
                                        SnowBallPorterFilterConfig
                                },
                                false
                        ),
                        new ConfigurableQuery(
                                queries.toArray(new Entry[queries.size()]),
                                0,
                                "lowercase",
                                new TokenFilterConfig[] {
                                        stopwordsFilterConfig,
                                        SnowBallPorterFilterConfig,
                                }
                        )
                )
        };

        for (SearchBenchmark searchBenchmark : searchBenchmarks) {
            System.out.println(searchBenchmark.performBenchmark());
        }
    }
}