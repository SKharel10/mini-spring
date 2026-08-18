import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {
    @Test
    public void componentIsRegistered() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.register(Book.class);
        assertInstanceOf(Book.class, applicationContext.getBean(Book.class));
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
        applicationContext.register(Book.class);

        Book book1 = applicationContext.getBean(Book.class);
        Book book2 = applicationContext.getBean(Book.class);

        assertSame(book1, book2);
    }

    @Test
    public void registeringSameComponentTwiceDoesNotCreateNewInstance() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.register(Book.class);
        Book book1 = applicationContext.getBean(Book.class);
        applicationContext.register(Book.class);
        Book book2 = applicationContext.getBean(Book.class);

        assertSame(book1, book2);
    }


}