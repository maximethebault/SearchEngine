package searchstrategy;

import benchmark.BenchmarkResult;
import benchmark.SearchBenchmark;

public class SearchingStrategy {
    private static int INSTANCE_NB = 0;

    private String name;
    private IndexingStrategy indexingStrategy;
    private QueryingStrategy queryingStrategy;
    private String indexPath;

    public SearchingStrategy(String name, IndexingStrategy indexingStrategy, QueryingStrategy queryingStrategy) {
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
        for (int i = -5; i < SearchBenchmark.BENCHMARK_ITERATION; i++) {
            indexingStrategy.buildIndex(indexPath);
            if (i >= 0) {
                // the first 5 iterations aren't taken into account, because lucene is warming up!
                totalIndexTime += indexingStrategy.getIndexTime();
            }
            if (i < SearchBenchmark.BENCHMARK_ITERATION - 1) {
                indexingStrategy.destroyIndex(indexPath);
            }
        }
        benchmarkResult.setIndexingTime(((float) totalIndexTime) / (float) SearchBenchmark.BENCHMARK_ITERATION);

        //SearchResult searchResult = queryingStrategy.performQueries();
        return benchmarkResult;
    }
}
