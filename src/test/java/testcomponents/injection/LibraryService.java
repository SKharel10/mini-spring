package testcomponents.injection;

import com.project.minispring.Component;

@Component
public class LibraryService {
    private InMemoryRepository inMemoryRepository;

    public LibraryService(InMemoryRepository inMemoryRepository) {
        this.inMemoryRepository = inMemoryRepository;
    }

    public InMemoryRepository getInMemoryRepository() {
        return inMemoryRepository;
    }
}
