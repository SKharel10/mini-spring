import com.project.minispring.*;
import org.junit.jupiter.api.Test;
import testcomponents.invalid.ComponentWithNonComponentDependency;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {
    @Test
    public void componentIsRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.register(Author.class);
        assertInstanceOf(Author.class, applicationContext.getBean(Author.class));
    }

    @Test
    public void unannotatedClassIsNotRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.register(UnannotatedClass.class);
        assertNull(applicationContext.getBean(UnannotatedClass.class));
    }

    @Test
    public void sameBeanIsRetrievedWhenRetrievingMultipleTimes() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.register(Author.class);

        Author author1 = applicationContext.getBean(Author.class);
        Author author2 = applicationContext.getBean(Author.class);

        assertSame(author1, author2);
    }

    @Test
    public void registeringSameComponentTwiceDoesNotCreateNewInstance() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.register(Author.class);
        Author author1 = applicationContext.getBean(Author.class);
        applicationContext.register(Author.class);
        Author author2 = applicationContext.getBean(Author.class);

        assertSame(author1, author2);
    }

    @Test
    public void componentWithDependencyCanBeRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.register(Book.class);

        assertInstanceOf(Book.class, applicationContext.getBean(Book.class));
    }

    @Test
    public void dependencyIsInjectedIntoComponent() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.register(Book.class);

        Book book = applicationContext.getBean(Book.class);

        assertInstanceOf(Author.class, book.getAuthor());
    }

    @Test
    public void injectedDependencyIsManagedBean() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
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
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        assertNull(applicationContext.getBean(Book.class));
        assertNull(applicationContext.getBean(Author.class));
        applicationContext.scanComponents();
        assertNotNull(applicationContext.getBean(Book.class));
        assertNotNull(applicationContext.getBean(Author.class));
    }

    @Test
    public void unannotatedClassIsNotRegisteredDuringScan() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        assertNull(applicationContext.getBean(UnannotatedClass.class));
        applicationContext.scanComponents();
        assertNull(applicationContext.getBean(UnannotatedClass.class));
    }

    @Test
    public void scannedComponentHasDependencyInjected() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.scanComponents();
        Book book = applicationContext.getBean(Book.class);
        assertNotNull(book.getAuthor());
    }

    @Test
    public void scanningDoesNotRegisterSameComponentTwice() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.scanComponents();
        Book book1 = applicationContext.getBean(Book.class);
        applicationContext.scanComponents();
        Book book2 = applicationContext.getBean(Book.class);

        assertSame(book1, book2);
    }

}