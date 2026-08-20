package testcomponents.injection;

import com.project.minispring.Component;
import testcomponents.basic.Author;

import java.util.ArrayList;

@Component
public class InMemoryRepository {
    private ArrayList<Author> books;

    public InMemoryRepository() {
        this.books = new ArrayList<>();
    }
}
