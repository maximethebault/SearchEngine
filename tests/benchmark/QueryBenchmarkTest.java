package benchmark;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryBenchmarkTest {

    @Test
    public void testPrecisionAndRecall() throws Exception {
        SearchResults expectedSearchResults = new SearchResults();
        expectedSearchResults.addHit(1, 1);
        expectedSearchResults.addHit(1, 2);
        expectedSearchResults.addHit(1, 3);
        expectedSearchResults.addHit(1, 4);
        expectedSearchResults.addHit(1, 5);
        expectedSearchResults.addHit(1, 6);
        SearchResults actualSearchResults = new SearchResults();
        actualSearchResults.addHit(1, 1);
        actualSearchResults.addHit(1, 2);
        actualSearchResults.addHit(1, 3);
        actualSearchResults.addHit(1, 4);
        actualSearchResults.addHit(1, 5);
        actualSearchResults.addHit(1, 6);
        actualSearchResults.addHit(1, 7);
        actualSearchResults.addHit(1, 8);
        actualSearchResults.addHit(1, 9);
        actualSearchResults.addHit(1, 10);
        QueryBenchmark queryBenchmark = new QueryBenchmark();
        queryBenchmark.setExpectedSearchResults(expectedSearchResults);
        queryBenchmark.setActualSearchResults(actualSearchResults);
        assertEquals(0.6, queryBenchmark.getPrecision(), 0.001);
        assertEquals(1, queryBenchmark.getRecall(), 0.001);
    }

    @Test
    public void testPrecisionAndRecall2() throws Exception {
        SearchResults expectedSearchResults = new SearchResults();
        expectedSearchResults.addHit(1, 1);
        expectedSearchResults.addHit(1, 2);
        expectedSearchResults.addHit(1, 3);
        expectedSearchResults.addHit(1, 4);
        expectedSearchResults.addHit(1, 5);
        expectedSearchResults.addHit(1, 6);
        expectedSearchResults.addHit(1, 7);
        expectedSearchResults.addHit(1, 8);
        expectedSearchResults.addHit(1, 9);
        expectedSearchResults.addHit(1, 10);
        SearchResults actualSearchResults = new SearchResults();
        actualSearchResults.addHit(1, 1);
        actualSearchResults.addHit(1, 2);
        actualSearchResults.addHit(1, 3);
        actualSearchResults.addHit(1, 4);
        actualSearchResults.addHit(1, 5);
        actualSearchResults.addHit(1, 6);
        QueryBenchmark queryBenchmark = new QueryBenchmark();
        queryBenchmark.setExpectedSearchResults(expectedSearchResults);
        queryBenchmark.setActualSearchResults(actualSearchResults);
        assertEquals(1, queryBenchmark.getPrecision(), 0.001);
        assertEquals(0.6, queryBenchmark.getRecall(), 0.001);
    }
}