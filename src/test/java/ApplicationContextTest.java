import com.project.minispring.*;
import invalidtestcomponents.ComponentWithMissingInterfaceDependency;
import org.junit.jupiter.api.Test;
import testcomponents.TestApplication;
import testcomponents.basic.Author;
import testcomponents.basic.UnannotatedClass;
import testcomponents.injection.Book;
import invalidtestcomponents.ComponentWithNonComponentDependency;
import testcomponents.interfaceresolution.InMemoryRepository;
import testcomponents.interfaceresolution.LibraryService;
import testcomponents.recursive.deeper.DeepComponent;
import testcomponents.recursive.deeper.DeepComponentDependency;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {

    @Test
    public void componentIsRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.register(Author.class);
        assertNotNull(applicationContext.getBean(Author.class));
    }

    @Test
    public void unannotatedClassIsNotRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.register(UnannotatedClass.class);
        assertNull(applicationContext.getBean(UnannotatedClass.class));
    }

    @Test
    public void sameBeanIsRetrievedWhenRetrievingMultipleTimes() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.register(Author.class);

        Author author1 = applicationContext.getBean(Author.class);
        Author author2 = applicationContext.getBean(Author.class);

        assertSame(author1, author2);
    }

    @Test
    public void registeringSameComponentTwiceDoesNotCreateNewInstance() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.register(Author.class);
        Author author1 = applicationContext.getBean(Author.class);
        applicationContext.register(Author.class);
        Author author2 = applicationContext.getBean(Author.class);

        assertSame(author1, author2);
    }

    @Test
    public void componentWithDependencyCanBeRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.register(Author.class);

        assertInstanceOf(Author.class, applicationContext.getBean(Author.class));
    }

    @Test
    public void dependencyIsInjectedIntoComponent() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.register(Book.class);

        Book book = applicationContext.getBean(Book.class);

        assertInstanceOf(Author.class, book.getAuthor());
    }

    @Test
    public void injectedDependencyIsManagedBean() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.register(Book.class);

        Book book = applicationContext.getBean(Book.class);
        assertSame(book.getAuthor(), applicationContext.getBean(Author.class));
    }

    @Test
    public void throwsWhenDependencyIsNotAComponent() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(ComponentWithNonComponentDependency.class);
        assertThrows(IllegalStateException.class, () -> applicationContext.register(ComponentWithNonComponentDependency.class));
    }

    @Test
    public void componentIsDiscoveredAndRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        assertNull(applicationContext.getBean(Author.class));
        assertNull(applicationContext.getBean(Author.class));
        applicationContext.scanComponents();
        assertNotNull(applicationContext.getBean(Author.class));
        assertNotNull(applicationContext.getBean(Author.class));
    }

    @Test
    public void unannotatedClassIsNotRegisteredDuringScan() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        assertNull(applicationContext.getBean(UnannotatedClass.class));
        applicationContext.scanComponents();
        assertNull(applicationContext.getBean(UnannotatedClass.class));
    }

    @Test
    public void scannedComponentHasDependencyInjected() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.scanComponents();
        Book book = applicationContext.getBean(Book.class);
        assertNotNull(book.getAuthor());
    }

    @Test
    public void scanningDoesNotRegisterSameComponentTwice() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.scanComponents();
        Author book1 = applicationContext.getBean(Author.class);
        applicationContext.scanComponents();
        Author book2 = applicationContext.getBean(Author.class);

        assertSame(book1, book2);
    }

    @Test
    public void componentInSubPackageIsDetectedAndRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.scanComponents();
        // Author class is nested in domain subpackage
        assertNotNull(applicationContext.getBean(Author.class));
    }

    @Test
    public void deeplyNestedComponentIsDetectedAndRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.scanComponents();
        // Deeply nested within recursive -> deeper -> DeepComponent.class
        assertNotNull(applicationContext.getBean(DeepComponentDependency.class));
    }

    @Test
    public void unannotatedClassInSubPackageIsNotRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.scanComponents();
        assertNull(applicationContext.getBean(UnannotatedClass.class));
    }

    @Test
    public void dependencyInSubPackageHasDependencyInjected() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.scanComponents();

        DeepComponent component = applicationContext.getBean(DeepComponent.class);
        assertInstanceOf(DeepComponentDependency.class, component.getDependency());
    }

    @Test
    public void interfaceDependencyIsResolved() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.scanComponents();

        assertNotNull(applicationContext.getBean(LibraryService.class));
    }

    @Test
    public void interfaceImplementationIsInjected() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.scanComponents();

        LibraryService libraryService = applicationContext.getBean(LibraryService.class);
        assertInstanceOf(InMemoryRepository.class, libraryService.getRepository());
    }

    @Test
    public void implementationIsRegisteredAsBean() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        applicationContext.scanComponents();

        LibraryService libraryService = applicationContext.getBean(LibraryService.class);

        assertSame(libraryService.getRepository(), applicationContext.getBean(InMemoryRepository.class));
    }

    @Test
    public void throwsWhenNoImplementationExists() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(TestApplication.class);
        assertThrows(IllegalStateException.class, () -> applicationContext.register(ComponentWithMissingInterfaceDependency.class));
    }
}