package testcomponents.injection;

import com.project.minispring.Component;
import testcomponents.basic.Author;

import javax.swing.undo.AbstractUndoableEdit;

@Component
public class Book {
    private Author author;

    public Book(Author author) {
        this.author = author;
    }

    public Author getAuthor() {
        return this.author;
    }
}
