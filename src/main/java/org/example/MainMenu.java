package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.domain.Appointment;
import org.example.domain.Patient;
import org.example.repository.sql_repository.noSqlWrapper.NoSqlRepositoryFactory;
import org.example.repository.IRepository;
import org.example.repository.Repository;
import org.example.repository.file_repository.BinaryFileRepository;
import org.example.repository.file_repository.JsonFileRepository;
import org.example.repository.file_repository.TextFileRepository;
import org.example.repository.file_repository.XmlFileRepository;
import org.example.repository.sql_repository.SQLRepository;
import org.example.service.AppointmentService;
import org.example.service.PatientService;
import org.example.ui.IUi;
import org.example.ui.Ui;
import org.example.utils.Settings;

public class MainMenu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/laborator04/MainMenu.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    private static void startCommandLine(){
        IRepository<Patient> patientIRepository = getRepository(Patient.class);
        IRepository<Appointment> appointmentIRepository = getRepository(Appointment.class);
        PatientService patientService = new PatientService(patientIRepository, appointmentIRepository);
        AppointmentService appointmentService = new AppointmentService(appointmentIRepository, patientIRepository);
        IUi ui=new Ui(patientService,appointmentService);
        ui.run();
    }

    public static IRepository getRepository(Class entityClass) {
        Settings s = Settings.getInstance(entityClass);
        if ("memory".equals(s.getRepoType())) {
            return new Repository<>(); // Memory repository
        } else if ("binary".equals(s.getRepoType())) {
            return new BinaryFileRepository<>(s.getFileName()); // Binary repository
        } else if ("text".equals(s.getRepoType())) {
            return new TextFileRepository<>(s.getFileName(), entityClass); // Text repository
        }else if("json".equals(s.getRepoType())) {
            return new JsonFileRepository<>(s.getFileName(), entityClass);
        }else if("xml".equals(s.getRepoType())) {
            return new XmlFileRepository<>(s.getFileName(), entityClass);
        }else if("sql".equals(s.getRepoType())) {
            return new SQLRepository(entityClass, org.example.utils.HibernateUtils.getSessionFactory());
        }else if("NoSql".equals(s.getRepoType())) {
            return new NoSqlRepositoryFactory(s.getFileName(),s.getDatabaseName()).createRepository(entityClass);
        }
        throw new IllegalArgumentException("Fisierul de setari e gresit!");
    }

    public static void main(String[] args) {
        try {
            Settings s = Settings.getInstance();
            if ("command".equals(s.getStartType())) {
                startCommandLine();
            } else if ("javafx".equals(s.getStartType())) {
                launch(args);
            }else{
                throw new Exception("Fisierul de setari e gresit!");
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        Platform.exit();
    }
}