package com.project.minispring;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.scanComponents();
    }
}
