public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ApplicationContext();

        applicationContext.register(Book.class);
    }
}
