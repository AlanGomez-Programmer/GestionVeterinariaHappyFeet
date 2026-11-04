USE happy_feet_veterinaria;

INSERT INTO especies (nombre, descripcion) VALUES
	('Perro', 'Canis lupus familiaris'),
	('Gato', 'Felis catus'),
	('Ave', 'Aves domésticas'),
	('Roedor', 'Pequeños mamíferos roedores'),
	('Reptil', 'Reptiles domésticos'),
	('Conejo', 'Oryctolagus cuniculus');

INSERT INTO razas (especie_id, nombre, caracteristicas) VALUES
	(1, 'Labrador Retriever', 'Tamaño grande, amigable, energético'),
	(1, 'Golden Retriever', 'Tamaño grande, inteligente, leal'),
	(1, 'Pastor Alemán', 'Tamaño grande, inteligente, protector'),
	(1, 'Bulldog Francés', 'Tamaño pequeño, cariñoso, juguetón'),
	(1, 'Chihuahua', 'Tamaño muy pequeño, alerta, valiente'),
	(1, 'Poodle', 'Inteligente, hipoalergénico'),
	(1, 'Mestizo', 'Cruza de razas'),
	(2, 'Persa', 'Pelo largo, tranquilo'),
	(2, 'Siamés', 'Vocal, social, activo'),
	(2, 'Maine Coon', 'Grande, peludo, amigable'),
	(2, 'Bengalí', 'Activo, manchas de leopardo'),
	(2, 'Mestizo', 'Cruza de razas'),
	(3, 'Canario', 'Pequeño, cantante'),
	(3, 'Periquito', 'Pequeño, social'),
	(3, 'Loro', 'Inteligente, longevo'),
	(4, 'Hámster Sirio', 'Pequeño, nocturno'),
	(4, 'Cobayo', 'Mediano, social'),
	(5, 'Iguana Verde', 'Herbívoro, grande'),
	(5, 'Tortuga', 'Lenta, longeva'),
	(6, 'Conejo Enano', 'Pequeño, dócil'),
	(6, 'Conejo Gigante', 'Grande, tranquilo');

INSERT INTO sexos (nombre) VALUES
	('Macho'),
	('Hembra'),
	('Desconocido');

INSERT INTO tipos_alergias (nombre, descripcion) VALUES
	('Alimentaria', 'Reacción a ingredientes de la dieta'),
	('Medicamentosa', 'Reacción a fármacos o productos biológicos'),
	('Ambiental', 'Reacción a sustancias del entorno'),
	('De contacto', 'Reacción por contacto directo de la piel con un material'),
	('Por picaduras', 'Reacción a la saliva o veneno de insectos y parásitos');

INSERT INTO catalogo_alergias (tipo_alergia_id, nombre, descripcion) VALUES
	(1, 'Pollo', 'Proteína de pollo presente en muchos alimentos comerciales'),
	(1, 'Res', 'Proteína de carne de res'),
	(1, 'Lácteos', 'Leche y derivados'),
	(2, 'Penicilina', 'Antibióticos de la familia de las penicilinas'),
	(2, 'Antiinflamatorios no esteroideos', 'AINE como meloxicam o carprofeno'),
	(2, 'Reacción posvacunal', 'Reacción alérgica después de aplicar una vacuna'),
	(3, 'Polen', 'Polen de árboles, pastos y flores'),
	(3, 'Ácaros del polvo', 'Ácaros presentes en el polvo doméstico'),
	(3, 'Moho', 'Esporas de hongos en ambientes húmedos'),
	(4, 'Plástico', 'Comederos o juguetes de plástico'),
	(4, 'Productos de limpieza', 'Desinfectantes y detergentes'),
	(4, 'Champú con fragancia', 'Perfumes y químicos de productos de higiene'),
	(5, 'Saliva de pulga', 'Dermatitis alérgica por picadura de pulga'),
	(5, 'Picadura de abeja o avispa', 'Reacción al veneno de himenópteros'),
	(5, 'Picadura de mosquito', 'Reacción a la saliva del mosquito');

INSERT INTO tipos_usuarios (nombre, descripcion) VALUES
	('Administrador', 'Acceso total al sistema'),
	('Veterinario', 'Atención clínica: consultas, procedimientos y agenda propia'),
	('Recepcionista', 'Agenda, registro de pacientes, ventas y facturación');

INSERT INTO especialidades (nombre, descripcion) VALUES
	('Medicina general', 'Atención clínica general de pequeñas especies'),
	('Cirugía', 'Procedimientos quirúrgicos de tejidos blandos y ortopedia'),
	('Dermatología', 'Enfermedades de piel, pelo y alergias'),
	('Odontología', 'Salud dental y procedimientos bucales'),
	('Animales exóticos', 'Atención de aves, reptiles y roedores');

INSERT INTO estados_citas (nombre, descripcion) VALUES
	('Programada', 'Cita agendada pendiente de atención'),
	('En proceso', 'La mascota está siendo atendida'),
	('Finalizada', 'La cita terminó');

INSERT INTO estados_consultas (nombre, descripcion) VALUES
	('Programado', 'Consulta agendado pendiente'),
	('En proceso', 'La consulta se está realizando'),
	('Finalizado', 'La consulta esta completada'),
	('Cancelado', 'La consulta esta cancelada');

INSERT INTO estados_procedimientos_especiales (nombre, descripcion) VALUES
	('Programado', 'Procedimiento agendado pendiente'),
	('Preoperatorio', 'Evaluación y preparación previa: exámenes, ayuno y anestesia'),
	('En proceso', 'El procedimiento se está realizando'),
	('Postoperatorio', 'Recuperación y seguimiento después del procedimiento'),
	('Finalizado', 'Procedimiento completado y dado de alta'),
	('Suspendido', 'Detenido temporalmente por una complicación o por indicación médica'),
	('Cancelado', 'Procedimiento cancelado antes de realizarse');

INSERT INTO tipos_servicios (nombre, descripcion) VALUES
	('Consulta', 'Todo tipo de consultas médicas'),
	('Procedimiento especial', 'Cirugías, procedimientos odontológicos y tratamientos complejos'),
	('Medicina preventiva', 'Vacunación, desparasitación e identificación');

INSERT INTO catalogo_servicios (tipo_servicio_id, nombre, descripcion, precio) VALUES
	(1, 'Consulta general', 'Consulta general para cualquier especie', 100.00),
	(1, 'Consulta de emergencia', 'Atención inmediata fuera de agenda', 250.00),
	(1, 'Consulta de control', 'Seguimiento de un tratamiento o cirugía', 75.00),
	(1, 'Consulta especializada', 'Atención por un veterinario especialista', 200.00),
	(2, 'Esterilización canina', 'Castración u ovariohisterectomía en perros', 1200.00),
	(2, 'Esterilización felina', 'Castración u ovariohisterectomía en gatos', 900.00),
	(2, 'Extirpación de masa', 'Cirugía para retirar tumores o quistes', 1500.00),
	(2, 'Cesárea', 'Extracción quirúrgica de crías', 1800.00),
	(2, 'Reparación de fractura', 'Cirugía ortopédica con placa o clavos', 2500.00),
	(2, 'Limpieza dental', 'Profilaxis dental con anestesia', 600.00),
	(2, 'Extracción dental', 'Extracción de piezas dentales dañadas', 500.00),
	(2, 'Fluidoterapia', 'Hidratación intravenosa con hospitalización', 450.00),
	(2, 'Rehabilitación física', 'Sesión de fisioterapia postoperatoria', 300.00),
	(2, 'Quimioterapia', 'Sesión de tratamiento oncológico', 2000.00),
	(3, 'Aplicación de vacuna', 'Aplicación de una dosis de vacuna', 50.00),
	(3, 'Desparasitación', 'Desparasitación interna o externa', 40.00),
	(3, 'Implantación de microchip', 'Colocación de microchip de identificación', 150.00);

INSERT INTO tipos_procedimientos_especiales (nombre, descripcion) VALUES
	('Cirugía', 'Procedimiento quirúrgico con anestesia general'),
	('Odontológico', 'Procedimiento bucal con anestesia'),
	('Tratamiento complejo', 'Tratamiento prolongado, por sesiones o con hospitalización');

INSERT INTO catalogo_procedimientos_especiales (servicio_id, tipo_procedimiento_id, duracion_estimada_minutos, requiere_ayuno) VALUES
	(5, 1, 90, TRUE),
	(6, 1, 60, TRUE),
	(7, 1, 120, TRUE),
	(8, 1, 90, FALSE),
	(9, 1, 180, TRUE),
	(10, 2, 60, TRUE),
	(11, 2, 45, TRUE),
	(12, 3, 240, FALSE),
	(13, 3, 45, FALSE),
	(14, 3, 120, FALSE);

INSERT INTO tipos_productos (nombre, descripcion) VALUES
	('Vacuna', 'Vacunas y biológicos'),
	('Medicamento', 'Fármacos veterinarios'),
	('Material médico', 'Insumos médicos y quirúrgicos: gasas, jeringas, guantes');

INSERT INTO tipos_vacunas (nombre, descripcion) VALUES
	('Antirrábica', 'Previene la rabia en perros y gatos'),
	('Parvovirus', 'Previene la parvovirosis canina'),
	('Moquillo', 'Previene el distemper canino'),
	('Polivalente canina', 'Protege contra varias enfermedades caninas en una sola dosis'),
	('Bordetella', 'Previene la tos de las perreras'),
	('Triple felina', 'Protege contra rinotraqueitis, calicivirus y panleucopenia'),
	('Leucemia felina', 'Previene el virus de la leucemia felina');

INSERT INTO tipos_movimientos_inventario (nombre, descripcion) VALUES
	('Entrada', 'Ingreso de producto por compra o reabastecimiento'),
	('Salida', 'Uso en consulta, procedimiento, jornada o venta'),
	('Ajuste', 'Corrección por conteo físico, daño o pérdida'),
	('Vencimiento', 'Retiro de producto vencido'),
	('Devolución', 'Reingreso de producto por factura anulada o procedimiento cancelado');

INSERT INTO metodos_pago (nombre) VALUES
	('Efectivo'),
	('Tarjeta'),
	('Transferencia');

INSERT INTO estados_facturas (nombre) VALUES
	('Pendiente'),
	('Pagada'),
	('Anulada');

INSERT INTO tipos_facturas (nombre) VALUES
	('Consulta'),
	('Procedimiento especial'),
	('Venta directa');

INSERT INTO tipos_items (nombre) VALUES
	('Producto'),
	('Servicio');

INSERT INTO estados_mascotas_adopcion (nombre) VALUES
	('Disponible'),
	('En proceso'),
	('Adoptada'),
	('Retirada');

INSERT INTO estados_jornadas_vacunacion (nombre) VALUES
	('Planificada'),
	('En curso'),
	('Finalizada'),
	('Cancelada');

INSERT INTO niveles (nombre, puntos_minimos) VALUES
	('Bronce', 0),
	('Plata', 500),
	('Oro', 1500);

INSERT INTO tipos_transacciones_puntos (nombre) VALUES
	('Ganados'),
	('Canjeados'),
	('Expirados'),
	('Ajuste');

INSERT INTO estados_transacciones_puntos (nombre) VALUES
	('Aplicada'),
	('Revertida');

INSERT INTO tipos_beneficios (nombre) VALUES
	('Descuento'),
	('Servicio gratis'),
	('Producto gratis'),
	('Puntos extra');

INSERT INTO estados_canjes_beneficios (nombre) VALUES
	('Pendiente'),
	('Aplicado'),
	('Expirado');

INSERT INTO estados_citas (nombre, descripcion) VALUES ('Cancelada', 'La cita fue cancelada');