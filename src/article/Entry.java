package article;

public class Entry {
    private int id;
    private String content;

    public Entry(int id) {
        this.id = id;
    }

    public Entry(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
