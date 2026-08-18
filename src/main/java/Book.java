@Component
public class Book {
    private Author author;

    public Book(Author author) {
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }
}
