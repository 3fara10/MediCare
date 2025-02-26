package org.example.service;

import org.example.domain.Appointment;
import org.example.domain.Patient;
import org.example.domain.raport_entities.MonthAppointmentsInfo;
import org.example.exceptions.*;
import org.example.factory.validatorFactory.IValidator;
import org.example.factory.validatorFactory.IValidatorFactory;
import org.example.factory.validatorFactory.ValidatorFactory;
import org.example.repository.IRepository;
import org.example.utils.RandomDataGenerator;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppointmentService implements IAppointmentService {
    private final IRepository<Appointment> appointmentIRepository;
    private final IRepository<Patient> patientIRepository;
    private final IValidatorFactory validatorFactory=new ValidatorFactory();

    public AppointmentService(IRepository<Appointment> appointmentIRepository, IRepository<Patient> patientIRepository) {
        this.appointmentIRepository = appointmentIRepository;
        this.patientIRepository = patientIRepository;
        this.generateDataifNeeded();
    }


    @Override
    public void add(int patientId, Date date, String purposeAppointment) throws RepositoryException,EntityException {

        Appointment appointment =new Appointment(this.patientIRepository.getById(patientId),date,purposeAppointment);
        IValidator<Appointment> validator = validatorFactory.getValidator(Appointment.class);
        validator.validate(appointment);
        this.validateOverload(appointment);
        this.appointmentIRepository.add(appointment);
    }

    @Override
    public Appointment get(int id) throws RepositoryException {
        return this.appointmentIRepository.getById(id);
    }

    @Override
    public void update(int idAppointment, int idPatient, Date date, String purposeAppointment) throws RepositoryException,EntityException {

        Appointment appointment = this.get(idAppointment);
        IValidator<Appointment> validator = validatorFactory.getValidator(Appointment.class);
        appointment.setPatient(this.patientIRepository.getById(idPatient));
        appointment.setDate(date);
        appointment.setPurposeAppointment(purposeAppointment);
        validator.validate(appointment);
        this.appointmentIRepository.update(appointment);
    }

    @Override
    public void delete(int id) throws RepositoryException{
        this.appointmentIRepository.delete(this.appointmentIRepository.getById(id));
    }

    @Override
    public List<Appointment> getAll() throws RepositoryException {
        IValidator<Appointment> appointmentValidator = validatorFactory.getValidator(Appointment.class);
        try{
            for (Appointment appointment : appointmentIRepository.getAll()) {
                appointmentValidator.validate(appointment);
            }
            return (List<Appointment>) this.appointmentIRepository.getAll();
        } catch (EntityException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<MonthAppointmentsInfo> getAppointmentsPerMonth() throws RepositoryException {
        try {
            return this.appointmentIRepository.getAll().stream()
                    .collect(Collectors.groupingBy(appointment -> {
                        Date appointmentDate = appointment.getDate();
                        LocalDate localDate = appointmentDate.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        return YearMonth.from(localDate);
                    }))
                    .entrySet().stream()
                    .map(entry -> {
                        YearMonth yearMonth = entry.getKey();
                        long count = entry.getValue().size();
                        return new MonthAppointmentsInfo(yearMonth, count);
                    })
                    .sorted((info1, info2) -> Long.compare(info2.getAppointmentCount(), info1.getAppointmentCount()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RepositoryException("Error getting appointments per month: " + e.getMessage(), e);
        }
    }
    @Override
    public List<MonthAppointmentsInfo> getBusiestMonths() throws RepositoryException {
        try {
            //Is the same thing with getAppointmenntsPerMonth
            return this.appointmentIRepository.getAll().stream()
                    .collect(Collectors.groupingBy(appointment -> {
                        Date appointmentDate = appointment.getDate();
                        LocalDate localDate = appointmentDate.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        return YearMonth.from(localDate);
                    }))
                    .entrySet().stream()
                    .map(entry -> {
                        YearMonth yearMonth = entry.getKey();
                        long count = entry.getValue().size();
                        return new MonthAppointmentsInfo(yearMonth, count);
                    })
                    .sorted((info1, info2) -> Long.compare(info2.getAppointmentCount(), info1.getAppointmentCount()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RepositoryException("Error getting busiest months: " + e.getMessage(), e);
        }
    }
    /***
     * Verify if an appointment doesn't overlap other one.
     * @param appointment:The new appointment we want to verify it
     */
    private void validateOverload(Appointment appointment) throws RepositoryException {
        //Calculate the milliseconds for the appointment date and appointment date + 60 min
        Date startNew = appointment.getDate();
        Date endNew = new Date(startNew.getTime() + 60 * 60 * 1000);

        for (Appointment existingAppointment : appointmentIRepository.getAll()) {
            //Calculate the milliseconds for each existing appointment date and e.a. date + 60 min
            Date startExisting = existingAppointment.getDate();
            Date endExisting = new Date(startExisting.getTime() + 60 * 60 * 1000);

            if (startNew.before(endExisting) && endNew.after(startExisting)) {
                throw new OverlapException("The new appointment overlaps with an existing appointment.");
            }
        }
    }

    private void generateDataifNeeded(){
        RandomDataGenerator dataGenerator = new RandomDataGenerator();
        try {
            if (this.appointmentIRepository.getAll().isEmpty() || this.appointmentIRepository.getAll().size()<100) {
                dataGenerator.generateAppointmentData(patientIRepository,appointmentIRepository,100);
            }
        }catch (RepositoryException e){}
    }

    @Override
    public Appointment findAppointment(int patientId, Date date, String purpose) throws RepositoryException {
        Optional<Appointment> appointment1= this.appointmentIRepository.getAll().stream()
                .filter(appointment -> appointment.getPatient().getId()==patientId
                        && appointment.getDate().equals(date)
                        && appointment.getPurposeAppointment().equalsIgnoreCase(purpose))
                .findFirst();
        return appointment1.orElse(null);
    }
}
