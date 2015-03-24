package article;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EntryExtractor {
    private String filePath;

    public EntryExtractor(String filePath) {
        this.filePath = filePath;
    }

    public List<Entry> getEntries() throws IOException {
        List<Entry> entries = new ArrayList<Entry>();
        Entry entry = null;
        StringBuilder stringBuilder = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

        int j = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith(".I")) {
                j++;
                if (entry != null) {
                    entry.setContent(stringBuilder.toString());
                    entries.add(entry);
                }
                entry = new Entry(j);
                stringBuilder = new StringBuilder(500);
            }
            else if (line.startsWith(".W")) {
                continue;
            }
            else {
                if (stringBuilder != null) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(" ");
                    }
                    stringBuilder.append(line);
                }
            }
        }
        br.close();

        if (entry != null) {
            entry.setContent(stringBuilder.toString());
            entries.add(entry);
        }

        return entries;
    }

}
