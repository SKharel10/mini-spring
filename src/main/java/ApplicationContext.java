import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private Map<Class<?>, Object> beans;

    public ApplicationContext() {
        beans = new HashMap<>();
    }

    public void register(Class<?> clazz) throws Exception {

        if (!clazz.isAnnotationPresent(Component.class) || beans.containsKey(clazz)) {
            return;
        }

        Constructor<?> constructor = clazz.getConstructors()[0];
        Object[] dependencies = resolveDependencies(clazz);
        Object object = constructor.newInstance(dependencies);
        beans.put(clazz, object);
    }

    public <T> T getBean(Class<T> clazz) {
        Object object = beans.get(clazz);
        return clazz.cast(object);
    }

    public Object[] resolveDependencies(Class<?> clazz) throws Exception {
        // No-arg constructor -> just return null, no dependencies needed.
        if (clazz.getConstructors()[0].getParameterCount() == 0) {
            return null;
        }

        Class<?>[] dependencyTypes = clazz.getConstructors()[0].getParameterTypes();
        Object[] dependencies = new Object[dependencyTypes.length];

        for (int i = 0; i < dependencyTypes.length; i++) {
            if (beans.containsKey(dependencyTypes[i])){
                dependencies[i] = beans.get(dependencyTypes[i]);
            } else {
                register(dependencyTypes[i]);
                // Edge case: dependency isn't annotated as Component.
                Object dependency = beans.get(dependencyTypes[i]);

                if (dependency == null) {
                    throw new IllegalStateException("Dependency of class: " + clazz.getName() + ": " +
                            dependencyTypes[i].getName() +  "is not annotated as component");
                }
                dependencies[i] = beans.get(dependencyTypes[i]);
            }
        }

        return dependencies;
    }

}
