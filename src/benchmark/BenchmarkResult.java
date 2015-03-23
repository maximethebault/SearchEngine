package benchmark;

public class BenchmarkResult {
    private String benchmarkName;
    /**
     * In milliseconds
     */
    private float indexingTime;
    /**
     * In bytes
     */
    private float indexSize;
    /**
     * In milliseconds
     */
    private float queryTime;
    private float queryRecall;
    private float queryPrecision;

    public String getBenchmarkName() {
        return benchmarkName;
    }

    public void setBenchmarkName(String benchmarkName) {
        this.benchmarkName = benchmarkName;
    }

    public float getIndexingTime() {
        return indexingTime;
    }

    public void setIndexingTime(float indexingTime) {
        this.indexingTime = indexingTime;
    }

    public float getIndexSize() {
        return indexSize;
    }

    public void setIndexSize(float indexSize) {
        this.indexSize = indexSize;
    }

    public float getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(float queryTime) {
        this.queryTime = queryTime;
    }

    public float getQueryRecall() {
        return queryRecall;
    }

    public void setQueryRecall(float queryRecall) {
        this.queryRecall = queryRecall;
    }

    public float getQueryPrecision() {
        return queryPrecision;
    }

    public void setQueryPrecision(float queryPrecision) {
        this.queryPrecision = queryPrecision;
    }

    @Override
    public String toString() {
        return "BenchmarkResult{" +
               "benchmarkName=" + benchmarkName +
               ", indexingTime=" + indexingTime +
               ", indexSize=" + indexSize +
               ", queryTime=" + queryTime +
               ", queryRecall=" + queryRecall +
               ", queryPrecision=" + queryPrecision +
               '}';
    }
}
