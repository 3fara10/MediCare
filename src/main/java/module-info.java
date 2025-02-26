module org.example.laborator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.persistence;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires java.xml.bind;
    requires com.fasterxml.jackson.databind;
    requires org.mongodb.driver.core;
    requires org.hibernate.orm.core;
    requires javafaker;
    requires java.logging;
    requires java.naming;
    requires java.sql;

    opens org.example.utils to java.xml.bind;
    opens org.example.laborator04 to javafx.fxml;
    opens org.example.domain to java.xml.bind, org.hibernate.orm.core;
    opens org.example.ui.controller to javafx.fxml;


    exports org.example.ui.controller;
    exports org.example.domain;
    exports org.example.exceptions;
    exports org.example.repository;
    exports org.example.repository.sql_repository.noSqlWrapper;
    exports org.example.factory.textFactory;
    exports org.example.factory.validatorFactory;
    exports org.example.service;
    exports org.example.ui;
    exports org.example.utils;
    exports org.example.repository.file_repository;
    opens org.example.repository.file_repository to java.xml.bind;
    exports org.example;
    opens org.example to javafx.fxml;
    exports org.example.domain.raport_entities;
    opens org.example.domain.raport_entities to java.xml.bind, org.hibernate.orm.core;

}