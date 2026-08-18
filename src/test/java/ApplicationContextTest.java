import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {
    @Test
    public void componentIsRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.register(Author.class);
        assertInstanceOf(Author.class, applicationContext.getBean(Author.class));
    }

    @Test
    public void unannotatedClassIsNotRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.register(UnannotatedClass.class);
        assertNull(applicationContext.getBean(UnannotatedClass.class));
    }

    @Test
    public void sameBeanIsRetrievedWhenRetrievingMultipleTimes() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.register(Author.class);

        Author author1 = applicationContext.getBean(Author.class);
        Author author2 = applicationContext.getBean(Author.class);

        assertSame(author1, author2);
    }

    @Test
    public void registeringSameComponentTwiceDoesNotCreateNewInstance() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.register(Author.class);
        Author author1 = applicationContext.getBean(Author.class);
        applicationContext.register(Author.class);
        Author author2 = applicationContext.getBean(Author.class);

        assertSame(author1, author2);
    }

    @Test
    public void componentWithDependencyCanBeRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.register(Book.class);

        assertInstanceOf(Book.class, applicationContext.getBean(Book.class));
    }

    @Test
    public void dependencyIsInjectedIntoComponent() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.register(Book.class);

        Book book = applicationContext.getBean(Book.class);

        assertInstanceOf(Author.class, book.getAuthor());
    }

    @Test
    public void injectedDependencyIsManagedBean() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.register(Book.class);

        Book book = applicationContext.getBean(Book.class);
        assertSame(book.getAuthor(), applicationContext.getBean(Author.class));
    }

    @Test
    public void throwsWhenDependencyIsNotAComponent() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        assertThrows(IllegalStateException.class, () -> applicationContext.register(TestComponent.class));
    }

}