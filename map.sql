USE map_laborator04
GO
select * from Patient
select * from Appointment
select * from Appointment where patient_id=5658

drop table Appointment
drop table Patient

ALTER TABLE Appointment
DROP CONSTRAINT FKfhg8dxc8emt4u70sonm2xqxnr;

ALTER TABLE Appointment
ADD CONSTRAINT FKfhg8dxc8emt4u70sonm2xqxnr
FOREIGN KEY (patient_id)
REFERENCES Patient(id)
ON DELETE CASCADE;
