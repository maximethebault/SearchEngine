package article;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleExtractor {
    private String articleFilePath;

    public ArticleExtractor(String articleFilePath) {
        this.articleFilePath = articleFilePath;
    }

    private String loadFile(File f) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        StringWriter out = new StringWriter();
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        out.flush();
        out.close();
        in.close();
        return out.toString();
    }

    public List<Article> getArticles() throws IOException {
        String fullDocument = loadFile(new File(articleFilePath));
        List<Article> articles = new ArrayList<Article>();
        Article article = null;
        StringBuilder stringBuilder = null;
        int j = 1;
        for (int i = 0; i < fullDocument.length(); i++) {
            if (fullDocument.charAt(i) == '.' && fullDocument.charAt(i + 1) == 'I') {
                j++;
                if (article != null) {
                    article.setContent(stringBuilder.toString());
                    articles.add(article);
                }
                article = new Article(j);
                stringBuilder = new StringBuilder(500);
                // we skip the rest of the line
                i = fullDocument.indexOf("\n", i);
            }
            else {
                if (stringBuilder != null) {
                    stringBuilder.append(fullDocument.charAt(i));
                }
            }

        }
        if (article != null) {
            article.setContent(stringBuilder.toString());
            articles.add(article);
        }

        return articles;
    }

}
