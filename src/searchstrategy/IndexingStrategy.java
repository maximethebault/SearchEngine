package searchstrategy;

import article.Entry;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public abstract class IndexingStrategy {
    private Entry[] articles;
    private long indexTime;

    public IndexingStrategy(Entry[] articles) {
        this.articles = articles;
    }

    /**
     * Get the configuration for the indexer
     *
     * @return the configuration
     */
    protected abstract IndexWriterConfig getIndexWriterConfig() throws IOException;

    /**
     * Method called after the docs have been written to the index
     *
     * @param writer
     *         the index writer
     */
    protected abstract void afterWriteHook(IndexWriter writer) throws IOException;

    protected void defineFields(Entry article, Document doc) throws UnsupportedEncodingException {
        doc.add(new IntField("id", article.getId(), Field.Store.YES));

        // Add the contents of the file to a field named "contents".  Specify a Reader,
        // so that the text of the file is tokenized and indexed, but not stored.
        // Note that FileReader expects the file to be in UTF-8 encoding.
        // If that's not the case searching for special characters will fail.
        doc.add(new TextField("contents", article.getContent(), Field.Store.NO));
    }

    public void destroyIndex(String indexPath) {
        Path directory = Paths.get(indexPath);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });
        }
        catch (IOException e) {
            System.out.println("Couldn't delete index");
            System.exit(1);
        }
    }

    public int getIndexSize(String indexPath) {
        final AtomicLong size = new AtomicLong(0);
        Path directory = Paths.get(indexPath);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    size.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e) {
            System.out.println("Couldn't get index size");
            e.printStackTrace();
        }
        return (int) size.get();
    }

    public void buildIndex(String indexPath) {
        Date start = new Date();
        try {
            Directory dir = FSDirectory.open(new File(indexPath).toPath());
            // :Post-Release-Update-Version.LUCENE_XY:
            IndexWriterConfig iwc = getIndexWriterConfig();

            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            // Optional: for better indexing performance, if you
            // are indexing many documents, increase the RAM
            // buffer.  But if you do this, increase the max heap
            // size to the JVM (eg add -Xmx512m or -Xmx1g):
            //
            // iwc.setRAMBufferSizeMB(256.0);

            IndexWriter writer = new IndexWriter(dir, iwc);
            indexDocs(writer, articles);

            // NOTE: if you want to maximize search performance,
            // you can optionally call forceMerge here.  This can be
            // a terribly costly operation, so generally it's only
            // worth it when your index is relatively static (ie
            // you're done adding documents to it):
            //
            // writer.forceMerge(1);
            afterWriteHook(writer);

            writer.close();

            indexTime = (new Date()).getTime() - start.getTime();
            System.out.println("Indexing iteration on " + indexPath + " took " + indexTime + " milliseconds");

        }
        catch (IOException e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }

    private void indexDocs(IndexWriter writer, Entry[] articles) throws IOException {
        for (Entry article : articles) {
            // make a new, empty document
            Document doc = new Document();

            defineFields(article, doc);

            // New index, so we just add the document (no old document can be there):
            writer.addDocument(doc);
        }
    }

    public long getIndexTime() {
        return indexTime;
    }
}
