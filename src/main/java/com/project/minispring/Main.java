package com.project.minispring;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        applicationContext.scanComponents();
    }
}
