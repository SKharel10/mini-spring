package com.project.minispring;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

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

    private Object[] resolveDependencies(Class<?> clazz) throws Exception {
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

    public List<Class<?>> getClasses() throws ClassNotFoundException, IOException, URISyntaxException {
        List<Class<?>> classes = new ArrayList<>();
        collectClasses(main.getPackageName(), classes);
        return classes;

    }

    public void collectClasses(String packageName, List<Class<?>> classes) throws URISyntaxException, ClassNotFoundException {
        ClassLoader classLoader = main.getClassLoader();
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) return;
        File file = new File(url.toURI());
        File[] files = file.listFiles();

        for (File currentFile : files) {
            if (currentFile.isDirectory()) {
                collectClasses(packageName + "." + currentFile.getName(), classes);
            } else if (currentFile.getName().endsWith(".class")) {
                String currentFileName = packageName + "." + currentFile.getName().replace(".class", "");
                Class<?> clazz = Class.forName(currentFileName);
                classes.add(clazz);
            }
        }
    }
}
