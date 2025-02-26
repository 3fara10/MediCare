package org.example.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class Settings {
    private String repoType;
    private String fileName;
    private String startType;


    private String databaseName;

    public Settings() {
    }

    public String getRepoType() {
        return this.repoType;
    }
    public String getDatabaseName() {return databaseName;}
    public String getFileName() {
        return this.fileName;
    }
    public String getStartType() {return this.startType;}
    private static Settings instance ;

    //de refactorizat gasit o metoda mai frumoasa!!
    public static Settings getInstance() {
        Properties settings = new Properties();
        try {
            settings.load(new FileReader("C:\\Users\\anton\\IdeaProjects\\laborator04\\src\\main\\java\\org\\example\\settings_properties.txt"));
        } catch (IOException e) {
            throw new RuntimeException("Error reading settings file", e);
        }
        instance = new Settings();
        instance.startType = settings.getProperty("start.type");
        return instance;
    }

    public static Settings getInstance(Class<?> entityClass) {
            Properties settings = new Properties();
            try {
                settings.load(new FileReader("C:\\Users\\anton\\IdeaProjects\\laborator04\\src\\main\\java\\org\\example\\settings_properties.txt"));
            } catch (IOException e) {
                throw new RuntimeException("Error reading settings file", e);
            }

            instance = new Settings();
            instance.startType = settings.getProperty("start.type");
            instance.repoType = settings.getProperty("repo.type");
            instance.fileName = settings.getProperty("file." + entityClass.getSimpleName().toLowerCase());
            instance.databaseName = settings.getProperty("repo.databaseName");
        return instance;
    }
}
