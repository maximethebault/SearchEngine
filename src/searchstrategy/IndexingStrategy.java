package searchstrategy;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.Date;

public abstract class IndexingStrategy {

    /**
     * Get the configuration for the indexer
     *
     * @return the configuration
     */
    protected abstract IndexWriterConfig getIndexWriterConfig();

    /**
     * Method called after the docs have been written to the index
     *
     * @param writer the index writer
     */
    protected abstract void afterWriteHook(IndexWriter writer);

    protected void defineFields(File file, FileInputStream fis, Document doc) throws UnsupportedEncodingException {
        // Add the path of the file as a field named "path".  Use a
        // field that is indexed (i.e. searchable), but don't tokenize
        // the field into separate words and don't index term frequency
        // or positional information:
        Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
        doc.add(pathField);

        // Add the last modified date of the file a field named "modified".
        // Use a LongField that is indexed (i.e. efficiently filterable with
        // NumericRangeFilter).  This indexes to milli-second resolution, which
        // is often too fine.  You could instead create a number based on
        // year/month/day/hour/minutes/seconds, down the resolution you require.
        // For example the long value 2011021714 would mean
        // February 17, 2011, 2-3 PM.
        doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));

        // Add the contents of the file to a field named "contents".  Specify a Reader,
        // so that the text of the file is tokenized and indexed, but not stored.
        // Note that FileReader expects the file to be in UTF-8 encoding.
        // If that's not the case searching for special characters will fail.
        doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))));
    }

    public void performIndexing(String docsPath, String indexPath) {
        final File docDir = new File(docsPath);
        if (!docDir.exists() || !docDir.canRead())

        {
            System.out.println("Document directory '" + docDir.getAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }

        Date start = new Date();
        try

        {
            System.out.println("Indexing to directory '" + indexPath + "'...");

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
            indexDocs(writer, docDir);

            // NOTE: if you want to maximize search performance,
            // you can optionally call forceMerge here.  This can be
            // a terribly costly operation, so generally it's only
            // worth it when your index is relatively static (ie
            // you're done adding documents to it):
            //
            // writer.forceMerge(1);
            afterWriteHook(writer);

            writer.close();

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }

    private void indexDocs(IndexWriter writer, File file) throws IOException {
        if (file.canRead()) {
            if (file.isDirectory()) {
                String[] files = file.list();
                // an IO error could occur
                if (files != null) {
                    for (String file1 : files) {
                        indexDocs(writer, new File(file, file1));
                    }
                }
            } else {

                FileInputStream fis;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException fnfe) {
                    // at least on windows, some temporary files raise this exception with an "access denied" message
                    // checking if the file can be read doesn't help
                    return;
                }

                try {

                    // make a new, empty document
                    Document doc = new Document();

                    defineFields(file, fis, doc);

                    // New index, so we just add the document (no old document can be there):
                    System.out.println("adding " + file);
                    writer.addDocument(doc);

                } finally {
                    fis.close();
                }
            }
        }

    }
}
