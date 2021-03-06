package benchmark;

import searchstrategy.IndexingStrategy;
import searchstrategy.QueryingStrategy;

public class SearchBenchmark {
    private static int INSTANCE_NB = 0;

    private String name;
    private IndexingStrategy indexingStrategy;
    private QueryingStrategy queryingStrategy;
    private String indexPath;

    public SearchBenchmark(String name, IndexingStrategy indexingStrategy, QueryingStrategy queryingStrategy) {
        this.name = name;
        this.indexingStrategy = indexingStrategy;
        this.queryingStrategy = queryingStrategy;
        this.indexPath = "index/" + (INSTANCE_NB++) + "/";
    }

    public BenchmarkResult performBenchmark() {
        // so that everyone starts at the same spot
        System.gc();

        BenchmarkResult benchmarkResult = new BenchmarkResult();
        benchmarkResult.setBenchmarkName(name);
        long totalIndexTime = 0;
        for (int i = -5; i < Main.BENCHMARK_ITERATION; i++) {
            indexingStrategy.buildIndex(indexPath);
            if (i >= 0) {
                // the first 5 iterations aren't taken into account, because lucene is warming up!
                totalIndexTime += indexingStrategy.getIndexTime();
            }
            if (i < Main.BENCHMARK_ITERATION - 1) {
                indexingStrategy.destroyIndex(indexPath);
            }
        }
        benchmarkResult.setIndexingTime(((float) totalIndexTime) / (float) Main.BENCHMARK_ITERATION);
        benchmarkResult.setIndexSize(indexingStrategy.getIndexSize(indexPath));

        QueryBenchmark queryBenchmark = new QueryBenchmark();
        long totalQueryTime = 0;
        SearchResults searchResults = null;
        for (int i = -5; i < Main.BENCHMARK_ITERATION; i++) {
            searchResults = queryingStrategy.performQueries(indexPath);
            if (i >= 0) {
                // the first 5 iterations aren't taken into account, because lucene is warming up!
                totalQueryTime += queryingStrategy.getQueryTime();
            }
        }
        queryBenchmark.setActualSearchResults(searchResults);

        benchmarkResult.setQueryTime(((float) totalQueryTime) / (float) Main.BENCHMARK_ITERATION);
        benchmarkResult.setQueryPrecision(queryBenchmark.getPrecision());
        benchmarkResult.setQueryRecall(queryBenchmark.getRecall());

        indexingStrategy.destroyIndex(indexPath);

        return benchmarkResult;
    }
}
