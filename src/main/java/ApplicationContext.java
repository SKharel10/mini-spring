import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private Map<Class<?>, Object> beans;

    public ApplicationContext() {
        beans = new HashMap<>();
    }

    public void register(Class<?> clazz) throws Exception {

        if (!clazz.isAnnotationPresent(Component.class)) {
            return;
        }

        Constructor<?> constructor = clazz.getConstructor();
        Object object = constructor.newInstance();
        beans.put(clazz, object);
    }

    public <T> T getBean(Class<T> clazz) {
        Object object = beans.get(clazz);
        return clazz.cast(object);
    }

}
