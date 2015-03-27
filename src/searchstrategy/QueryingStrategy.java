package searchstrategy;

import article.Entry;
import benchmark.SearchResults;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public abstract class QueryingStrategy {
    private Entry[] queries;
    private double scoreThreshold;

    private long queryTime;

    public QueryingStrategy(Entry[] queries, double scoreThreshold) {
        this.queries = queries;
        this.scoreThreshold = scoreThreshold;
    }

    protected abstract QueryParser getQueryParser(IndexSearcher searcher) throws IOException;

    public SearchResults performQueries(String indexPath) {
        SearchResults searchResults = new SearchResults();

        Date start = new Date();

        IndexReader reader;
        try {
            reader = DirectoryReader.open(FSDirectory.open(new File(indexPath).toPath()));
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = getQueryParser(searcher);
            for (Entry storedQuery : queries) {
                Query query = parser.parse(QueryParser.escape(storedQuery.getContent()));
                TopDocs results = searcher.search(query, null, 20000);
                int nbHit = 0;
                for (ScoreDoc hit : results.scoreDocs) {
                    if (hit.score < scoreThreshold) {
                        break;
                    }
                    Document document = searcher.doc(hit.doc);
                    searchResults.addHit(storedQuery.getId(), Integer.valueOf(document.get("id")));
                    nbHit++;
                }
                if (nbHit == 0) {
                    searchResults.noHit(storedQuery.getId());
                }
            }

            reader.close();

            queryTime = (new Date()).getTime() - start.getTime();
            System.out.println("Query iteration on " + indexPath + " took " + queryTime + " milliseconds");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return searchResults;
    }

    public long getQueryTime() {
        return queryTime;
    }
}
