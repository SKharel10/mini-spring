package com.project.minispring;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private Class<?> main;
    private Map<Class<?>, Object> beans;

    public ApplicationContext(Class<?> main) {
        this.main = main;
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
            return new Object[0];
        }

        Class<?>[] dependencyTypes = clazz.getConstructors()[0].getParameterTypes();
        Object[] dependencies = new Object[dependencyTypes.length];

        for (int i = 0; i < dependencyTypes.length; i++) {
            if (beans.containsKey(dependencyTypes[i])){
                dependencies[i] = beans.get(dependencyTypes[i]);
            } else {
                register(dependencyTypes[i]);
                // Edge case: dependency isn't annotated as com.project.minispring.Component.
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

    public void scanComponents() throws Exception {
        List<Class<?>> classes = getClasses();

        for (Class<?> clazz: classes) {
            if (!clazz.isAnnotationPresent(Component.class)) {
                continue;
            }
            register(clazz);
        }
    }

    public List<Class<?>> getClasses() throws ClassNotFoundException, IOException {
        String packageName = main.getPackageName();

        ClassLoader classLoader = main.getClassLoader();
        InputStream resourcesStream = classLoader.getResourceAsStream(packageName.replace(".", "/"));
        String resources = new String(resourcesStream.readAllBytes());

        List<Class<?>> classes = new ArrayList<>();
        for (String resource: resources.split("\n")) {
            String classPath = packageName + "." + resource.replace(".class", "");
            classes.add(Class.forName(classPath));
        }

        return classes;
    }

}
