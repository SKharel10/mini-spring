package testcomponents.interfaceresolution;

import com.project.minispring.Component;
import testcomponents.injection.Book;

import java.util.ArrayList;

@Component
public class InMemoryRepository implements BookRepository {
    private final ArrayList<Book> books;

    public InMemoryRepository() {
        this.books = new ArrayList<>();
    }

    @Override
    public void save(Book book) {
        books.add(book);
    }
}
