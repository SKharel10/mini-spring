package testcomponents.interfaceresolution;

import com.project.minispring.Component;

@Component
public class LibraryService {
    private BookRepository repository;

    public LibraryService(BookRepository repository) {
        this.repository = repository;
    }

    public BookRepository getRepository() {
        return repository;
    }
}
