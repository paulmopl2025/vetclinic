-- ============================================
-- DATOS DE EJEMPLO PARA VETERINARY CLINIC
-- Compatible con H2 y PostgreSQL
-- ============================================

-- Roles
INSERT INTO roles (name) 
SELECT 'ROLE_USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name) 
SELECT 'ROLE_VET' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_VET');

INSERT INTO roles (name) 
SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name) 
SELECT 'ROLE_RECEPCIONISTA' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_RECEPCIONISTA');

-- Usuarios de ejemplo (password: password123 para todos, codificado con BCrypt)
-- NOTA: El usuario 'admin' se crea automáticamente por DataInitializer con password 'admin123'
-- Para pruebas, puedes usar: admin/admin123, vet1/password123, recepcionista1/password123

INSERT INTO users (username, password, email) 
SELECT 'vet1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK7p2LK', 'maria.gonzalez@vetclinic.com'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'vet1');

INSERT INTO users (username, password, email) 
SELECT 'vet2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK7p2LK', 'carlos.rodriguez@vetclinic.com'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'vet2');

INSERT INTO users (username, password, email) 
SELECT 'recepcionista1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK7p2LK', 'ana.martinez@vetclinic.com'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'recepcionista1');

INSERT INTO users (username, password, email) 
SELECT 'user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK7p2LK', 'juan.perez@email.com'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user1');

-- Asignar roles a usuarios (admin se asigna automáticamente por DataInitializer)

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'vet1' AND r.name = 'ROLE_VET'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'vet2' AND r.name = 'ROLE_VET'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'recepcionista1' AND r.name = 'ROLE_RECEPCIONISTA'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'user1' AND r.name = 'ROLE_USER'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

-- Especialidades
INSERT INTO specialties (name) 
SELECT 'Cirugía General' WHERE NOT EXISTS (SELECT 1 FROM specialties WHERE name = 'Cirugía General');

INSERT INTO specialties (name) 
SELECT 'Medicina Interna' WHERE NOT EXISTS (SELECT 1 FROM specialties WHERE name = 'Medicina Interna');

INSERT INTO specialties (name) 
SELECT 'Dermatología' WHERE NOT EXISTS (SELECT 1 FROM specialties WHERE name = 'Dermatología');

INSERT INTO specialties (name) 
SELECT 'Oftalmología' WHERE NOT EXISTS (SELECT 1 FROM specialties WHERE name = 'Oftalmología');

INSERT INTO specialties (name) 
SELECT 'Cardiología' WHERE NOT EXISTS (SELECT 1 FROM specialties WHERE name = 'Cardiología');

INSERT INTO specialties (name) 
SELECT 'Neurología' WHERE NOT EXISTS (SELECT 1 FROM specialties WHERE name = 'Neurología');

INSERT INTO specialties (name) 
SELECT 'Oncología' WHERE NOT EXISTS (SELECT 1 FROM specialties WHERE name = 'Oncología');

INSERT INTO specialties (name) 
SELECT 'Medicina de Emergencias' WHERE NOT EXISTS (SELECT 1 FROM specialties WHERE name = 'Medicina de Emergencias');

-- Veterinarios
INSERT INTO vets (first_name, last_name) 
SELECT 'María', 'González' 
WHERE NOT EXISTS (SELECT 1 FROM vets WHERE first_name = 'María' AND last_name = 'González');

INSERT INTO vets (first_name, last_name) 
SELECT 'Carlos', 'Rodríguez' 
WHERE NOT EXISTS (SELECT 1 FROM vets WHERE first_name = 'Carlos' AND last_name = 'Rodríguez');

INSERT INTO vets (first_name, last_name) 
SELECT 'Laura', 'Fernández' 
WHERE NOT EXISTS (SELECT 1 FROM vets WHERE first_name = 'Laura' AND last_name = 'Fernández');

INSERT INTO vets (first_name, last_name) 
SELECT 'Pedro', 'Sánchez' 
WHERE NOT EXISTS (SELECT 1 FROM vets WHERE first_name = 'Pedro' AND last_name = 'Sánchez');

INSERT INTO vets (first_name, last_name) 
SELECT 'Ana', 'López' 
WHERE NOT EXISTS (SELECT 1 FROM vets WHERE first_name = 'Ana' AND last_name = 'López');

-- Asignar especialidades a veterinarios
INSERT INTO vet_specialties (vet_id, specialty_id)
SELECT v.id, s.id FROM vets v, specialties s
WHERE v.first_name = 'María' AND v.last_name = 'González' AND s.name IN ('Cirugía General', 'Medicina Interna')
AND NOT EXISTS (SELECT 1 FROM vet_specialties vs WHERE vs.vet_id = v.id AND vs.specialty_id = s.id);

INSERT INTO vet_specialties (vet_id, specialty_id)
SELECT v.id, s.id FROM vets v, specialties s
WHERE v.first_name = 'Carlos' AND v.last_name = 'Rodríguez' AND s.name IN ('Dermatología', 'Oftalmología')
AND NOT EXISTS (SELECT 1 FROM vet_specialties vs WHERE vs.vet_id = v.id AND vs.specialty_id = s.id);

INSERT INTO vet_specialties (vet_id, specialty_id)
SELECT v.id, s.id FROM vets v, specialties s
WHERE v.first_name = 'Laura' AND v.last_name = 'Fernández' AND s.name IN ('Cardiología', 'Neurología')
AND NOT EXISTS (SELECT 1 FROM vet_specialties vs WHERE vs.vet_id = v.id AND vs.specialty_id = s.id);

INSERT INTO vet_specialties (vet_id, specialty_id)
SELECT v.id, s.id FROM vets v, specialties s
WHERE v.first_name = 'Pedro' AND v.last_name = 'Sánchez' AND s.name IN ('Medicina de Emergencias', 'Cirugía General')
AND NOT EXISTS (SELECT 1 FROM vet_specialties vs WHERE vs.vet_id = v.id AND vs.specialty_id = s.id);

INSERT INTO vet_specialties (vet_id, specialty_id)
SELECT v.id, s.id FROM vets v, specialties s
WHERE v.first_name = 'Ana' AND v.last_name = 'López' AND s.name IN ('Oncología', 'Medicina Interna')
AND NOT EXISTS (SELECT 1 FROM vet_specialties vs WHERE vs.vet_id = v.id AND vs.specialty_id = s.id);

-- Dueños
INSERT INTO owners (first_name, last_name, phone, email, created_at, updated_at) 
SELECT 'Juan', 'Pérez', '555-0101', 'juan.perez@email.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM owners WHERE first_name = 'Juan' AND last_name = 'Pérez');

INSERT INTO owners (first_name, last_name, phone, email, created_at, updated_at) 
SELECT 'María', 'García', '555-0102', 'maria.garcia@email.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM owners WHERE first_name = 'María' AND last_name = 'García');

INSERT INTO owners (first_name, last_name, phone, email, created_at, updated_at) 
SELECT 'Carlos', 'Martínez', '555-0103', 'carlos.martinez@email.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM owners WHERE first_name = 'Carlos' AND last_name = 'Martínez');

INSERT INTO owners (first_name, last_name, phone, email, created_at, updated_at) 
SELECT 'Ana', 'López', '555-0104', 'ana.lopez@email.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM owners WHERE first_name = 'Ana' AND last_name = 'López');

INSERT INTO owners (first_name, last_name, phone, email, created_at, updated_at) 
SELECT 'Pedro', 'Sánchez', '555-0105', 'pedro.sanchez@email.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM owners WHERE first_name = 'Pedro' AND last_name = 'Sánchez');

INSERT INTO owners (first_name, last_name, phone, email, created_at, updated_at) 
SELECT 'Laura', 'Fernández', '555-0106', 'laura.fernandez@email.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM owners WHERE first_name = 'Laura' AND last_name = 'Fernández');

-- Mascotas
INSERT INTO pets (name, species, breed, birth_date, owner_id, created_at, updated_at)
SELECT 
    'Max', 'Perro', 'Labrador Retriever', '2020-03-15', o.id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM owners o WHERE o.first_name = 'Juan' AND o.last_name = 'Pérez'
AND NOT EXISTS (SELECT 1 FROM pets p WHERE p.name = 'Max' AND p.owner_id = o.id);

INSERT INTO pets (name, species, breed, birth_date, owner_id, created_at, updated_at)
SELECT 
    'Luna', 'Gato', 'Persa', '2021-07-20', o.id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM owners o WHERE o.first_name = 'María' AND o.last_name = 'García'
AND NOT EXISTS (SELECT 1 FROM pets p WHERE p.name = 'Luna' AND p.owner_id = o.id);

INSERT INTO pets (name, species, breed, birth_date, owner_id, created_at, updated_at)
SELECT 
    'Rocky', 'Perro', 'Bulldog Francés', '2019-11-10', o.id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM owners o WHERE o.first_name = 'Carlos' AND o.last_name = 'Martínez'
AND NOT EXISTS (SELECT 1 FROM pets p WHERE p.name = 'Rocky' AND p.owner_id = o.id);

INSERT INTO pets (name, species, breed, birth_date, owner_id, created_at, updated_at)
SELECT 
    'Mimi', 'Gato', 'Siames', '2022-01-05', o.id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM owners o WHERE o.first_name = 'Ana' AND o.last_name = 'López'
AND NOT EXISTS (SELECT 1 FROM pets p WHERE p.name = 'Mimi' AND p.owner_id = o.id);

INSERT INTO pets (name, species, breed, birth_date, owner_id, created_at, updated_at)
SELECT 
    'Bella', 'Perro', 'Golden Retriever', '2020-09-12', o.id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM owners o WHERE o.first_name = 'Pedro' AND o.last_name = 'Sánchez'
AND NOT EXISTS (SELECT 1 FROM pets p WHERE p.name = 'Bella' AND p.owner_id = o.id);

INSERT INTO pets (name, species, breed, birth_date, owner_id, created_at, updated_at)
SELECT 
    'Charlie', 'Perro', 'Beagle', '2021-05-18', o.id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM owners o WHERE o.first_name = 'Laura' AND o.last_name = 'Fernández'
AND NOT EXISTS (SELECT 1 FROM pets p WHERE p.name = 'Charlie' AND p.owner_id = o.id);

INSERT INTO pets (name, species, breed, birth_date, owner_id, created_at, updated_at)
SELECT 
    'Simba', 'Gato', 'Maine Coon', '2019-12-25', o.id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM owners o WHERE o.first_name = 'Juan' AND o.last_name = 'Pérez'
AND NOT EXISTS (SELECT 1 FROM pets p WHERE p.name = 'Simba' AND p.owner_id = o.id);

-- Servicios Veterinarios
INSERT INTO veterinary_services (name, description, service_type, base_cost, estimated_duration_minutes, active, created_at, updated_at) 
SELECT 'Consulta General', 'Consulta veterinaria general para revisión de salud', 'CHECKUP', 50.00, 30, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM veterinary_services WHERE name = 'Consulta General');

INSERT INTO veterinary_services (name, description, service_type, base_cost, estimated_duration_minutes, active, created_at, updated_at) 
SELECT 'Vacunación Anual', 'Vacunación anual completa', 'VACCINATION', 75.00, 15, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM veterinary_services WHERE name = 'Vacunación Anual');

INSERT INTO veterinary_services (name, description, service_type, base_cost, estimated_duration_minutes, active, created_at, updated_at) 
SELECT 'Cirugía de Esterilización', 'Procedimiento quirúrgico para esterilización', 'SURGERY', 300.00, 120, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM veterinary_services WHERE name = 'Cirugía de Esterilización');

INSERT INTO veterinary_services (name, description, service_type, base_cost, estimated_duration_minutes, active, created_at, updated_at) 
SELECT 'Emergencia', 'Atención de emergencia veterinaria', 'EMERGENCY', 150.00, 60, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM veterinary_services WHERE name = 'Emergencia');

INSERT INTO veterinary_services (name, description, service_type, base_cost, estimated_duration_minutes, active, created_at, updated_at) 
SELECT 'Limpieza Dental', 'Limpieza y cuidado dental profesional', 'DENTAL', 200.00, 90, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM veterinary_services WHERE name = 'Limpieza Dental');

INSERT INTO veterinary_services (name, description, service_type, base_cost, estimated_duration_minutes, active, created_at, updated_at) 
SELECT 'Análisis de Sangre', 'Análisis completo de sangre', 'LABORATORY', 80.00, 20, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM veterinary_services WHERE name = 'Análisis de Sangre');

INSERT INTO veterinary_services (name, description, service_type, base_cost, estimated_duration_minutes, active, created_at, updated_at) 
SELECT 'Radiografía', 'Estudio radiológico', 'IMAGING', 120.00, 30, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM veterinary_services WHERE name = 'Radiografía');

INSERT INTO veterinary_services (name, description, service_type, base_cost, estimated_duration_minutes, active, created_at, updated_at) 
SELECT 'Aseo y Peluquería', 'Servicio de aseo y peluquería canina/felina', 'GROOMING', 40.00, 60, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM veterinary_services WHERE name = 'Aseo y Peluquería');

-- Configuración de la Clínica
INSERT INTO clinic (name, address, phone, email, opening_time, closing_time, max_daily_appointments, active, created_at, updated_at) 
SELECT 'Veterinary Clinic San Francisco', 'Av. Principal 123, San Francisco', '555-0000', 'info@vetclinic.com', '09:00:00', '18:00:00', 30, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM clinic);

-- Citas (Appointments)
INSERT INTO appointments (appointment_date, status, notes, pet_id, vet_id, service_id, created_at, updated_at)
SELECT 
    DATEADD('DAY', 2, CURRENT_TIMESTAMP), 
    'PENDING',
    'Primera consulta para revisión general',
    p.id,
    v.id,
    s.id,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM pets p, vets v, veterinary_services s
WHERE p.name = 'Max' AND v.first_name = 'María' AND s.name = 'Consulta General'
AND NOT EXISTS (SELECT 1 FROM appointments a WHERE a.pet_id = p.id AND a.vet_id = v.id AND a.appointment_date = DATEADD('DAY', 2, CURRENT_TIMESTAMP));

INSERT INTO appointments (appointment_date, status, notes, pet_id, vet_id, service_id, created_at, updated_at)
SELECT 
    DATEADD('DAY', 5, CURRENT_TIMESTAMP), 
    'CONFIRMED',
    'Vacunación anual programada',
    p.id,
    v.id,
    s.id,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM pets p, vets v, veterinary_services s
WHERE p.name = 'Luna' AND v.first_name = 'Carlos' AND s.name = 'Vacunación Anual'
AND NOT EXISTS (SELECT 1 FROM appointments a WHERE a.pet_id = p.id AND a.vet_id = v.id AND a.appointment_date = DATEADD('DAY', 5, CURRENT_TIMESTAMP));

INSERT INTO appointments (appointment_date, status, notes, pet_id, vet_id, service_id, created_at, updated_at)
SELECT 
    DATEADD('DAY', 1, CURRENT_TIMESTAMP), 
    'PENDING',
    'Consulta de seguimiento',
    p.id,
    v.id,
    s.id,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM pets p, vets v, veterinary_services s
WHERE p.name = 'Rocky' AND v.first_name = 'Laura' AND s.name = 'Consulta General'
AND NOT EXISTS (SELECT 1 FROM appointments a WHERE a.pet_id = p.id AND a.vet_id = v.id AND a.appointment_date = DATEADD('DAY', 1, CURRENT_TIMESTAMP));

INSERT INTO appointments (appointment_date, status, notes, pet_id, vet_id, service_id, created_at, updated_at)
SELECT 
    DATEADD('DAY', -3, CURRENT_TIMESTAMP), 
    'COMPLETED',
    'Consulta completada exitosamente',
    p.id,
    v.id,
    s.id,
    DATEADD('DAY', -5, CURRENT_TIMESTAMP),
    DATEADD('DAY', -3, CURRENT_TIMESTAMP)
FROM pets p, vets v, veterinary_services s
WHERE p.name = 'Bella' AND v.first_name = 'Pedro' AND s.name = 'Consulta General'
AND NOT EXISTS (SELECT 1 FROM appointments a WHERE a.pet_id = p.id AND a.vet_id = v.id AND a.appointment_date = DATEADD('DAY', -3, CURRENT_TIMESTAMP));

-- Registros Médicos (Medical Records)
INSERT INTO medical_records (record_date, diagnosis, treatment, notes, weight, temperature, vaccine_administered, pet_id, vet_id, appointment_id, created_at, updated_at)
SELECT 
    DATEADD('DAY', -3, CURRENT_TIMESTAMP),
    'Salud general buena, sin problemas detectados',
    'Revisión rutinaria, continuar con dieta actual',
    'Mascota en buen estado de salud. Peso adecuado para su edad y raza.',
    25.5,
    38.5,
    NULL,
    p.id,
    v.id,
    a.id,
    DATEADD('DAY', -3, CURRENT_TIMESTAMP),
    DATEADD('DAY', -3, CURRENT_TIMESTAMP)
FROM pets p, vets v, appointments a
WHERE p.name = 'Bella' AND v.first_name = 'Pedro' AND a.status = 'COMPLETED'
AND NOT EXISTS (SELECT 1 FROM medical_records mr WHERE mr.pet_id = p.id AND mr.vet_id = v.id AND mr.appointment_id = a.id);

INSERT INTO medical_records (record_date, diagnosis, treatment, notes, weight, temperature, vaccine_administered, pet_id, vet_id, created_at, updated_at)
SELECT 
    DATEADD('DAY', -10, CURRENT_TIMESTAMP),
    'Vacunación anual aplicada',
    'Vacuna polivalente anual',
    'Vacunación aplicada correctamente. Próxima dosis en un año.',
    4.2,
    38.3,
    'Polivalente anual',
    p.id,
    v.id,
    DATEADD('DAY', -10, CURRENT_TIMESTAMP),
    DATEADD('DAY', -10, CURRENT_TIMESTAMP)
FROM pets p, vets v
WHERE p.name = 'Max' AND v.first_name = 'María'
AND NOT EXISTS (SELECT 1 FROM medical_records mr WHERE mr.pet_id = p.id AND mr.vet_id = v.id AND mr.record_date = DATEADD('DAY', -10, CURRENT_TIMESTAMP));
