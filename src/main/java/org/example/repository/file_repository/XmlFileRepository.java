package org.example.repository.file_repository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.example.domain.Entity;
import org.example.exceptions.RepositoryException;


import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XmlFileRepository<T extends Entity> extends AbstractFileRepository<T> {
    private final Class<T> entityClass;
    private final JAXBContext jaxbContext;
    private static final Logger logger = Logger.getLogger(XmlFileRepository.class.getName());

    public XmlFileRepository(String fileName, Class<T> entityClass) {
        super(fileName);
        this.entityClass = entityClass;
        this.entities = new ArrayList<>();
        try {
            this.jaxbContext = JAXBContext.newInstance(EntityListWrapper.class, entityClass);
            loadFile();
        } catch (JAXBException | RepositoryException e){
            throw new RuntimeException("Error initializing XML repository", e);
        }
    }

    @Override
    protected void loadFile() throws RepositoryException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            EntityListWrapper<T> wrapper = (EntityListWrapper<T>) unmarshaller.unmarshal(br);
            this.entities = wrapper.getEntities();
        } catch (JAXBException | IOException e) {
            throw new RepositoryException("Error loading XML file", e);
        }
    }

    @Override
    protected void saveFile() throws RepositoryException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName))) {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            EntityListWrapper<T> wrapper = new EntityListWrapper<>();
            wrapper.setEntities(this.entities);
            marshaller.marshal(wrapper, bw);

            logger.info("Saved entities: " + this.entities);
        } catch (JAXBException | IOException e) {
            logger.log(Level.SEVERE, "Error loading XML file", e);
            throw new RepositoryException("Error saving XML file", e);
        }
    }
}