package org.example.factory.textFactory;

import org.example.domain.Appointment;
import org.example.domain.Entity;
import org.example.domain.Patient;
import org.example.exceptions.EntityException;
import org.example.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;

public class EntityFactory<T extends Entity>{
        private static final Map<Class<?>, IEntityFactory<?>> factories = new HashMap<>();

        static {
            factories.put(Patient.class, new PatientFactory());
            factories.put(Appointment.class, new AppointmentFactory());
        }

        public static <T extends Entity> IEntityFactory<T> getFactory(Class<T> entityClass) throws EntityException {
            IEntityFactory<T> factory = (IEntityFactory<T>) factories.get(entityClass);
            if (factory == null) {
                throw new ValidationException("No validator found for entity: " + entityClass.getName());
            }
            //play attention here
            return factory;
        }
}
