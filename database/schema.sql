DROP DATABASE IF EXISTS happy_feet_veterinaria;
CREATE DATABASE happy_feet_veterinaria
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE happy_feet_veterinaria;


-- MÓDULO 1: GESTIÓN DE PACIENTES (MASCOTAS Y DUEÑOS)
CREATE TABLE duenios (
    id INT AUTO_INCREMENT,
    nombre_completo VARCHAR(255) NOT NULL,
    documento_identidad VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    contacto_emergencia VARCHAR(255),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    INDEX idx_duenios_activo (activo)
);

CREATE TABLE especies (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE razas (
    id INT AUTO_INCREMENT,
    especie_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    caracteristicas TEXT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_razas_especie_nombre (especie_id, nombre),
    CONSTRAINT fk_razas_especie
        FOREIGN KEY (especie_id) REFERENCES especies(id) ON DELETE RESTRICT
);

CREATE TABLE sexos (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE tipos_alergias (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE catalogo_alergias (
    id INT AUTO_INCREMENT,
    tipo_alergia_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_catalogo_alergias_tipo
        FOREIGN KEY (tipo_alergia_id) REFERENCES tipos_alergias(id) ON DELETE RESTRICT
);

CREATE TABLE mascotas (
    id INT AUTO_INCREMENT,
    duenio_id INT NOT NULL,
    raza_id INT NOT NULL,
    sexo_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    edad_estimada_anios INT,
    peso_actual DECIMAL(5,2),
    microchip VARCHAR(50) UNIQUE,
    tatuaje VARCHAR(50),
    url_foto VARCHAR(255),
    condiciones_preexistentes TEXT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    INDEX idx_mascotas_nombre (nombre),
    INDEX idx_mascotas_activo (activo),
    CONSTRAINT fk_mascotas_duenio
        FOREIGN KEY (duenio_id) REFERENCES duenios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_mascotas_raza
        FOREIGN KEY (raza_id) REFERENCES razas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_mascotas_sexo
        FOREIGN KEY (sexo_id) REFERENCES sexos(id) ON DELETE RESTRICT,
    CONSTRAINT chk_mascotas_edad
        CHECK (fecha_nacimiento IS NOT NULL OR edad_estimada_anios IS NOT NULL),
    CONSTRAINT chk_mascotas_edad_positiva
        CHECK (edad_estimada_anios IS NULL OR edad_estimada_anios >= 0),
    CONSTRAINT chk_mascotas_peso
        CHECK (peso_actual IS NULL OR peso_actual > 0)
);

CREATE TABLE alergias (
    id INT AUTO_INCREMENT,
    mascota_id INT NOT NULL,
    catalogo_alergia_id INT NOT NULL,
    observaciones TEXT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_alergias_mascota_alergia (mascota_id, catalogo_alergia_id),
    CONSTRAINT fk_alergias_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE CASCADE,
    CONSTRAINT fk_alergias_catalogo
        FOREIGN KEY (catalogo_alergia_id) REFERENCES catalogo_alergias(id) ON DELETE RESTRICT
);

-- SEGURIDAD: USUARIOS, ROLES Y MÓDULOS
CREATE TABLE tipos_usuarios (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT,
    tipo_usuario_id INT NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasenia VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    intentos_fallidos INT NOT NULL DEFAULT 0,
    debe_cambiar_contrasenia BOOLEAN NOT NULL DEFAULT TRUE,
    ultimo_acceso DATETIME,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_usuarios_tipo_usuario
        FOREIGN KEY (tipo_usuario_id) REFERENCES tipos_usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_usuarios_intentos CHECK (intentos_fallidos >= 0)
);

-- MÓDULO 2: SERVICIOS MÉDICOS Y CITAS
CREATE TABLE especialidades (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id)
);

CREATE TABLE veterinarios (
    id INT AUTO_INCREMENT,
    usuario_id INT UNIQUE,
    especialidad_id INT NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    documento_identidad VARCHAR(20) NOT NULL UNIQUE,
    licencia_profesional VARCHAR(50) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    fecha_contratacion DATE,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    INDEX idx_veterinarios_activo (activo),
    CONSTRAINT fk_veterinarios_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_veterinarios_especialidad
        FOREIGN KEY (especialidad_id) REFERENCES especialidades(id) ON DELETE RESTRICT
);

CREATE TABLE estados_citas (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE citas (
    id INT AUTO_INCREMENT,
    mascota_id INT NOT NULL,
    veterinario_id INT NOT NULL,
    estado_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    motivo VARCHAR(500),
    observaciones TEXT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_citas_veterinario_fecha (veterinario_id, fecha_hora),
    INDEX idx_citas_fecha (fecha_hora),
    CONSTRAINT fk_citas_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_citas_veterinario
        FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_citas_estado
        FOREIGN KEY (estado_id) REFERENCES estados_citas(id) ON DELETE RESTRICT
);

CREATE TABLE tipos_servicios (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE catalogo_servicios (
    id INT AUTO_INCREMENT,
    tipo_servicio_id INT NOT NULL,
    nombre VARCHAR(200) NOT NULL UNIQUE,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    INDEX idx_catalogo_servicios_activo (activo),
    CONSTRAINT fk_catalogo_servicios_tipo
        FOREIGN KEY (tipo_servicio_id) REFERENCES tipos_servicios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_catalogo_servicios_precio CHECK (precio >= 0)
);

CREATE TABLE estados_consultas (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE consultas (
    id INT AUTO_INCREMENT,
    mascota_id INT NOT NULL,
    veterinario_id INT NOT NULL,
    cita_id INT UNIQUE,
    estado_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    motivo VARCHAR(500) NOT NULL,
    sintomas TEXT,
    diagnostico TEXT,
    recomendaciones TEXT,
    observaciones TEXT,
    peso_registrado DECIMAL(5,2),
    temperatura DECIMAL(4,2),
    PRIMARY KEY (id),
    INDEX idx_consultas_fecha (fecha_hora),
    CONSTRAINT fk_consultas_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_consultas_veterinario
        FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_consultas_cita
        FOREIGN KEY (cita_id) REFERENCES citas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_consultas_estado
        FOREIGN KEY (estado_id) REFERENCES estados_consultas(id) ON DELETE RESTRICT,
    CONSTRAINT chk_consultas_peso
        CHECK (peso_registrado IS NULL OR peso_registrado > 0)
);

CREATE TABLE tipos_procedimientos_especiales (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE catalogo_procedimientos_especiales (
    id INT AUTO_INCREMENT,
    servicio_id INT NOT NULL UNIQUE,
    tipo_procedimiento_id INT NOT NULL,
    duracion_estimada_minutos INT,
    requiere_ayuno BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT fk_catalogo_proc_servicio
        FOREIGN KEY (servicio_id) REFERENCES catalogo_servicios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_catalogo_proc_tipo
        FOREIGN KEY (tipo_procedimiento_id) REFERENCES tipos_procedimientos_especiales(id) ON DELETE RESTRICT,
    CONSTRAINT chk_catalogo_proc_duracion
        CHECK (duracion_estimada_minutos IS NULL OR duracion_estimada_minutos > 0)
);

CREATE TABLE prescripciones_procedimientos (
    id INT AUTO_INCREMENT,
    consulta_id INT NOT NULL,
    catalogo_procedimiento_id INT NOT NULL,
    indicaciones TEXT,
    fecha_prescripcion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_prescripciones_proc_consulta
        FOREIGN KEY (consulta_id) REFERENCES consultas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_prescripciones_proc_catalogo
        FOREIGN KEY (catalogo_procedimiento_id) REFERENCES catalogo_procedimientos_especiales(id) ON DELETE RESTRICT
);

CREATE TABLE estados_procedimientos_especiales (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE procedimientos_especiales (
    id INT AUTO_INCREMENT,
    mascota_id INT NOT NULL,
    veterinario_id INT NOT NULL,
    catalogo_procedimiento_id INT NOT NULL,
    prescripcion_id INT UNIQUE,
    estado_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    informacion_preoperatoria TEXT,
    detalle_procedimiento TEXT,
    complicaciones TEXT,
    seguimiento_postoperatorio TEXT,
    proximo_control DATE,
    PRIMARY KEY (id),
    INDEX idx_procedimientos_fecha (fecha_hora),
    CONSTRAINT fk_procedimientos_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_procedimientos_veterinario
        FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_procedimientos_catalogo
        FOREIGN KEY (catalogo_procedimiento_id) REFERENCES catalogo_procedimientos_especiales(id) ON DELETE RESTRICT,
    CONSTRAINT fk_procedimientos_prescripcion
        FOREIGN KEY (prescripcion_id) REFERENCES prescripciones_procedimientos(id) ON DELETE RESTRICT,
    CONSTRAINT fk_procedimientos_estado
        FOREIGN KEY (estado_id) REFERENCES estados_procedimientos_especiales(id) ON DELETE RESTRICT
);

CREATE TABLE historial_servicios (
    id INT AUTO_INCREMENT,
    servicio_id INT NOT NULL,
    mascota_id INT NOT NULL,
    veterinario_id INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    precio_aplicado DECIMAL(10,2) NOT NULL,
    observaciones TEXT,
    PRIMARY KEY (id),
    INDEX idx_historial_servicios_fecha (fecha),
    CONSTRAINT fk_historial_servicios_servicio
        FOREIGN KEY (servicio_id) REFERENCES catalogo_servicios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_historial_servicios_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_historial_servicios_veterinario
        FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_historial_servicios_precio CHECK (precio_aplicado >= 0)
);

CREATE TABLE historial_servicio_consulta (
    id INT AUTO_INCREMENT,
    historial_servicio_id INT NOT NULL UNIQUE,
    consulta_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_hsc_historial
        FOREIGN KEY (historial_servicio_id) REFERENCES historial_servicios(id) ON DELETE CASCADE,
    CONSTRAINT fk_hsc_consulta
        FOREIGN KEY (consulta_id) REFERENCES consultas(id) ON DELETE RESTRICT
);

CREATE TABLE historial_servicio_procedimiento_especial (
    id INT AUTO_INCREMENT,
    historial_servicio_id INT NOT NULL UNIQUE,
    procedimiento_id INT NOT NULL UNIQUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_hsp_historial
        FOREIGN KEY (historial_servicio_id) REFERENCES historial_servicios(id) ON DELETE CASCADE,
    CONSTRAINT fk_hsp_procedimiento
        FOREIGN KEY (procedimiento_id) REFERENCES procedimientos_especiales(id) ON DELETE RESTRICT
);

-- MÓDULO 3: INVENTARIO Y FARMACIA
CREATE TABLE tipos_productos (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE fabricantes (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL UNIQUE,
    pais VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE proveedores (
    id INT AUTO_INCREMENT,
    nombre_empresa VARCHAR(255) NOT NULL,
    contacto VARCHAR(255),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(255),
    sitio_web VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_proveedores_nombre (nombre_empresa)
);

CREATE TABLE productos (
    id INT AUTO_INCREMENT,
    tipo_producto_id INT NOT NULL,
    fabricante_id INT,
    proveedor_id INT,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    unidad_medida VARCHAR(50) NOT NULL DEFAULT 'unidad',
    stock_minimo INT NOT NULL DEFAULT 0,
    precio_compra DECIMAL(10,2),
    precio_venta DECIMAL(10,2) NOT NULL,
    requiere_receta BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_productos_nombre (nombre),
    INDEX idx_productos_activo (activo),
    CONSTRAINT fk_productos_tipo
        FOREIGN KEY (tipo_producto_id) REFERENCES tipos_productos(id) ON DELETE RESTRICT,
    CONSTRAINT fk_productos_fabricante
        FOREIGN KEY (fabricante_id) REFERENCES fabricantes(id) ON DELETE RESTRICT,
    CONSTRAINT fk_productos_proveedor
        FOREIGN KEY (proveedor_id) REFERENCES proveedores(id) ON DELETE RESTRICT,
    CONSTRAINT chk_productos_stock_minimo CHECK (stock_minimo >= 0),
    CONSTRAINT chk_productos_precios
        CHECK (precio_venta >= 0 AND (precio_compra IS NULL OR precio_compra >= 0))
);

CREATE TABLE tipos_vacunas (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE vacunas (
    id INT AUTO_INCREMENT,
    producto_id INT NOT NULL UNIQUE,
    tipo_vacuna_id INT NOT NULL,
    especie_destino_id INT,
    numero_dosis INT DEFAULT 1,
    dias_entre_dosis INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_vacunas_producto
        FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT,
    CONSTRAINT fk_vacunas_tipo
        FOREIGN KEY (tipo_vacuna_id) REFERENCES tipos_vacunas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_vacunas_especie
        FOREIGN KEY (especie_destino_id) REFERENCES especies(id) ON DELETE RESTRICT,
    CONSTRAINT chk_vacunas_dosis CHECK (numero_dosis > 0)
);

CREATE TABLE medicamentos (
    id INT AUTO_INCREMENT,
    producto_id INT NOT NULL UNIQUE,
    principio_activo VARCHAR(150),
    presentacion VARCHAR(100),
    concentracion VARCHAR(50),
    via_administracion VARCHAR(50),
    PRIMARY KEY (id),
    CONSTRAINT fk_medicamentos_producto
        FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT
);

CREATE TABLE material_medico (
    id INT AUTO_INCREMENT,
    producto_id INT NOT NULL UNIQUE,
    es_desechable BOOLEAN DEFAULT TRUE,
    es_esteril BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT fk_material_medico_producto
        FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT
);

CREATE TABLE lotes (
    id INT AUTO_INCREMENT,
    codigo VARCHAR(50) NOT NULL,
    fecha_fabricacion DATE,
    fecha_vencimiento DATE,
    PRIMARY KEY (id),
    INDEX idx_lotes_codigo (codigo),
    INDEX idx_lotes_vencimiento (fecha_vencimiento),
    CONSTRAINT chk_lotes_fechas
        CHECK (fecha_fabricacion IS NULL OR fecha_vencimiento IS NULL
               OR fecha_vencimiento > fecha_fabricacion)
);

CREATE TABLE inventario (
    id INT AUTO_INCREMENT,
    producto_id INT NOT NULL,
    lote_id INT UNIQUE,
    cantidad_stock INT NOT NULL DEFAULT 0,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_inventario_producto
        FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT,
    CONSTRAINT fk_inventario_lote
        FOREIGN KEY (lote_id) REFERENCES lotes(id) ON DELETE RESTRICT,
    CONSTRAINT chk_inventario_stock CHECK (cantidad_stock >= 0)
);

CREATE TABLE tipos_movimientos_inventario (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    PRIMARY KEY (id)
);

-- MÓDULO 4: FACTURACIÓN Y REPORTES
CREATE TABLE metodos_pago (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE estados_facturas (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE tipos_facturas (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE tipos_items (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE facturas (
    id INT AUTO_INCREMENT,
    duenio_id INT NOT NULL,
    usuario_id INT,
    tipo_factura_id INT NOT NULL,
    metodo_pago_id INT,
    estado_id INT NOT NULL,
    numero_factura VARCHAR(50) NOT NULL UNIQUE,
    fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(10,2) NOT NULL,
    impuesto DECIMAL(10,2) NOT NULL DEFAULT 0,
    descuento DECIMAL(10,2) NOT NULL DEFAULT 0,
    total DECIMAL(10,2) NOT NULL,
    observaciones TEXT,
    PRIMARY KEY (id),
    INDEX idx_facturas_fecha (fecha_emision),
    CONSTRAINT fk_facturas_duenio
        FOREIGN KEY (duenio_id) REFERENCES duenios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_facturas_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_facturas_tipo
        FOREIGN KEY (tipo_factura_id) REFERENCES tipos_facturas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_facturas_metodo_pago
        FOREIGN KEY (metodo_pago_id) REFERENCES metodos_pago(id) ON DELETE RESTRICT,
    CONSTRAINT fk_facturas_estado
        FOREIGN KEY (estado_id) REFERENCES estados_facturas(id) ON DELETE RESTRICT,
    CONSTRAINT chk_facturas_montos
        CHECK (subtotal >= 0 AND impuesto >= 0 AND descuento >= 0 AND total >= 0)
);

CREATE TABLE items_factura (
    id INT AUTO_INCREMENT,
    factura_id INT NOT NULL,
    tipo_item_id INT NOT NULL,
    inventario_id INT,
    servicio_id INT,
    descripcion VARCHAR(255),
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_items_factura_factura
        FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    CONSTRAINT fk_items_factura_tipo
        FOREIGN KEY (tipo_item_id) REFERENCES tipos_items(id) ON DELETE RESTRICT,
    CONSTRAINT fk_items_factura_inventario
        FOREIGN KEY (inventario_id) REFERENCES inventario(id) ON DELETE RESTRICT,
    CONSTRAINT fk_items_factura_servicio
        FOREIGN KEY (servicio_id) REFERENCES catalogo_servicios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_items_factura_un_tipo
        CHECK ((inventario_id IS NULL) <> (servicio_id IS NULL)),
    CONSTRAINT chk_items_factura_valores
        CHECK (cantidad > 0 AND precio_unitario >= 0 AND subtotal >= 0)
);

-- MÓDULO 5: ACTIVIDADES ESPECIALES
CREATE TABLE historial_duenios (
    id INT AUTO_INCREMENT,
    mascota_id INT NOT NULL,
    duenio_id INT NOT NULL,
    fecha_inicio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_fin DATETIME,
    motivo VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_historial_duenios_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_historial_duenios_duenio
        FOREIGN KEY (duenio_id) REFERENCES duenios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_historial_duenios_fechas
        CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
);

CREATE TABLE estados_mascotas_adopcion (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE mascotas_adopcion (
    id INT AUTO_INCREMENT,
    mascota_id INT NOT NULL,
    estado_id INT NOT NULL,
    fecha_ingreso DATE NOT NULL,
    motivo_ingreso TEXT,
    historia TEXT,
    temperamento VARCHAR(255),
    necesidades_especiales TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_mascotas_adopcion_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_mascotas_adopcion_estado
        FOREIGN KEY (estado_id) REFERENCES estados_mascotas_adopcion(id) ON DELETE RESTRICT
);

CREATE TABLE adopciones (
    id INT AUTO_INCREMENT,
    mascota_adopcion_id INT NOT NULL UNIQUE,
    duenio_id INT NOT NULL,
    fecha_adopcion DATE NOT NULL,
    condiciones_especiales TEXT,
    seguimiento_requerido BOOLEAN DEFAULT TRUE,
    fecha_primer_seguimiento DATE,
    PRIMARY KEY (id),
    CONSTRAINT fk_adopciones_mascota_adopcion
        FOREIGN KEY (mascota_adopcion_id) REFERENCES mascotas_adopcion(id) ON DELETE RESTRICT,
    CONSTRAINT fk_adopciones_duenio
        FOREIGN KEY (duenio_id) REFERENCES duenios(id) ON DELETE RESTRICT
);

CREATE TABLE contratos (
    id INT AUTO_INCREMENT,
    adopcion_id INT NOT NULL UNIQUE,
    contenido TEXT NOT NULL,
    fecha_generacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_contratos_adopcion
        FOREIGN KEY (adopcion_id) REFERENCES adopciones(id) ON DELETE RESTRICT
);

CREATE TABLE estados_jornadas_vacunacion (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE jornadas_vacunacion (
    id INT AUTO_INCREMENT,
    estado_id INT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME,
    hora_fin TIME,
    ubicacion VARCHAR(255),
    descripcion TEXT,
    capacidad_maxima INT,
    PRIMARY KEY (id),
    INDEX idx_jornadas_fecha (fecha),
    CONSTRAINT fk_jornadas_estado
        FOREIGN KEY (estado_id) REFERENCES estados_jornadas_vacunacion(id) ON DELETE RESTRICT,
    CONSTRAINT chk_jornadas_horas
        CHECK (hora_fin IS NULL OR hora_inicio IS NULL OR hora_fin > hora_inicio),
    CONSTRAINT chk_jornadas_capacidad
        CHECK (capacidad_maxima IS NULL OR capacidad_maxima > 0)
);

CREATE TABLE registros_jornada_vacunacion (
    id INT AUTO_INCREMENT,
    jornada_id INT NOT NULL,
    mascota_id INT NOT NULL,
    inventario_id INT NOT NULL,
    veterinario_id INT,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    proxima_dosis DATE,
    observaciones TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_registros_jornada_jornada
        FOREIGN KEY (jornada_id) REFERENCES jornadas_vacunacion(id) ON DELETE RESTRICT,
    CONSTRAINT fk_registros_jornada_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_registros_jornada_inventario
        FOREIGN KEY (inventario_id) REFERENCES inventario(id) ON DELETE RESTRICT,
    CONSTRAINT fk_registros_jornada_veterinario
        FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id) ON DELETE RESTRICT
);

CREATE TABLE niveles (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    puntos_minimos INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT chk_niveles_puntos CHECK (puntos_minimos >= 0)
);

CREATE TABLE club_mascotas (
    id INT AUTO_INCREMENT,
    duenio_id INT NOT NULL UNIQUE,
    nivel_id INT NOT NULL,
    puntos_acumulados INT NOT NULL DEFAULT 0,
    puntos_canjeados INT NOT NULL DEFAULT 0,
    fecha_inscripcion DATE NOT NULL,
    fecha_ultima_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_club_duenio
        FOREIGN KEY (duenio_id) REFERENCES duenios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_club_nivel
        FOREIGN KEY (nivel_id) REFERENCES niveles(id) ON DELETE RESTRICT,
    CONSTRAINT chk_club_puntos
        CHECK (puntos_acumulados >= 0 AND puntos_canjeados >= 0
               AND puntos_canjeados <= puntos_acumulados)
);

CREATE TABLE tipos_transacciones_puntos (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE estados_transacciones_puntos (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE transacciones_puntos (
    id INT AUTO_INCREMENT,
    club_id INT NOT NULL,
    tipo_id INT NOT NULL,
    estado_id INT NOT NULL,
    factura_id INT,
    puntos INT NOT NULL,
    saldo_anterior INT NOT NULL,
    saldo_nuevo INT NOT NULL,
    descripcion VARCHAR(255),
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_transacciones_club
        FOREIGN KEY (club_id) REFERENCES club_mascotas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_transacciones_tipo
        FOREIGN KEY (tipo_id) REFERENCES tipos_transacciones_puntos(id) ON DELETE RESTRICT,
    CONSTRAINT fk_transacciones_estado
        FOREIGN KEY (estado_id) REFERENCES estados_transacciones_puntos(id) ON DELETE RESTRICT,
    CONSTRAINT fk_transacciones_factura
        FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE RESTRICT,
    CONSTRAINT chk_transacciones_saldos CHECK (saldo_anterior >= 0 AND saldo_nuevo >= 0)
);

CREATE TABLE tipos_beneficios (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE beneficios_club (
    id INT AUTO_INCREMENT,
    nivel_requerido_id INT NOT NULL,
    tipo_beneficio_id INT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    puntos_necesarios INT NOT NULL,
    valor_beneficio DECIMAL(10,2),
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_beneficios_nivel
        FOREIGN KEY (nivel_requerido_id) REFERENCES niveles(id) ON DELETE RESTRICT,
    CONSTRAINT fk_beneficios_tipo
        FOREIGN KEY (tipo_beneficio_id) REFERENCES tipos_beneficios(id) ON DELETE RESTRICT,
    CONSTRAINT chk_beneficios_puntos CHECK (puntos_necesarios > 0),
    CONSTRAINT chk_beneficios_valor CHECK (valor_beneficio IS NULL OR valor_beneficio >= 0)
);

CREATE TABLE estados_canjes_beneficios (
    id INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE canjes_beneficios (
    id INT AUTO_INCREMENT,
    club_id INT NOT NULL,
    beneficio_id INT NOT NULL,
    estado_id INT NOT NULL,
    factura_id INT,
    puntos_canjeados INT NOT NULL,
    fecha_canje DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion DATE,
    PRIMARY KEY (id),
    CONSTRAINT fk_canjes_club
        FOREIGN KEY (club_id) REFERENCES club_mascotas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_canjes_beneficio
        FOREIGN KEY (beneficio_id) REFERENCES beneficios_club(id) ON DELETE RESTRICT,
    CONSTRAINT fk_canjes_estado
        FOREIGN KEY (estado_id) REFERENCES estados_canjes_beneficios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_canjes_factura
        FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE RESTRICT,
    CONSTRAINT chk_canjes_puntos CHECK (puntos_canjeados > 0)
);

-- TABLAS QUE RELACIONAN MÓDULOS
-- Modulo 1
CREATE TABLE historial_vacunas_previas (
    id INT AUTO_INCREMENT,
    mascota_id INT NOT NULL,
    tipo_vacuna_id INT NOT NULL,
    fecha_aplicacion DATE,
    lugar_aplicacion VARCHAR(255),
    observaciones TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_vacunas_previas_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE CASCADE,
    CONSTRAINT fk_vacunas_previas_tipo
        FOREIGN KEY (tipo_vacuna_id) REFERENCES tipos_vacunas(id) ON DELETE RESTRICT
);

CREATE TABLE procedimientos_quirurgicos_previos (
    id INT AUTO_INCREMENT,
    mascota_id INT NOT NULL,
    tipo_procedimiento_id INT NOT NULL,
    descripcion TEXT,
    fecha_aproximada DATE,
    PRIMARY KEY (id),
    CONSTRAINT fk_quirurgicos_previos_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE CASCADE,
    CONSTRAINT fk_quirurgicos_previos_tipo
        FOREIGN KEY (tipo_procedimiento_id) REFERENCES tipos_procedimientos_especiales(id) ON DELETE RESTRICT
);

-- Modulo 2
CREATE TABLE prescripciones_medicamentos (
    id INT AUTO_INCREMENT,
    consulta_id INT NOT NULL,
    medicamento_id INT NOT NULL,
    cantidad INT NOT NULL,
    dosis VARCHAR(100),
    frecuencia VARCHAR(100),
    duracion_dias INT,
    instrucciones TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_prescripciones_med_consulta
        FOREIGN KEY (consulta_id) REFERENCES consultas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_prescripciones_med_medicamento
        FOREIGN KEY (medicamento_id) REFERENCES medicamentos(id) ON DELETE RESTRICT,
    CONSTRAINT chk_prescripciones_med_cantidad CHECK (cantidad > 0),
    CONSTRAINT chk_prescripciones_med_duracion CHECK (duracion_dias IS NULL OR duracion_dias > 0)
);

CREATE TABLE insumos_consultas (
    id INT AUTO_INCREMENT,
    consulta_id INT NOT NULL,
    inventario_id INT NOT NULL,
    cantidad INT NOT NULL,
    observaciones VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_insumos_consultas_consulta
        FOREIGN KEY (consulta_id) REFERENCES consultas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_insumos_consultas_inventario
        FOREIGN KEY (inventario_id) REFERENCES inventario(id) ON DELETE RESTRICT,
    CONSTRAINT chk_insumos_consultas_cantidad CHECK (cantidad > 0)
);

CREATE TABLE insumos_procedimientos_especiales (
    id INT AUTO_INCREMENT,
    procedimiento_id INT NOT NULL,
    inventario_id INT NOT NULL,
    cantidad INT NOT NULL,
    observaciones VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_insumos_proc_procedimiento
        FOREIGN KEY (procedimiento_id) REFERENCES procedimientos_especiales(id) ON DELETE RESTRICT,
    CONSTRAINT fk_insumos_proc_inventario
        FOREIGN KEY (inventario_id) REFERENCES inventario(id) ON DELETE RESTRICT,
    CONSTRAINT chk_insumos_proc_cantidad CHECK (cantidad > 0)
);

-- Modulo 3
CREATE TABLE movimientos_inventario (
    id INT AUTO_INCREMENT,
    inventario_id INT NOT NULL,
    tipo_movimiento_id INT NOT NULL,
    usuario_id INT NOT NULL,
    cantidad INT NOT NULL,
    stock_anterior INT NOT NULL,
    stock_nuevo INT NOT NULL,
    motivo VARCHAR(255),
    consulta_id INT,
    procedimiento_id INT,
    factura_id INT,
    registro_jornada_id INT,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_movimientos_fecha (fecha_movimiento),
    CONSTRAINT fk_movimientos_inventario
        FOREIGN KEY (inventario_id) REFERENCES inventario(id) ON DELETE RESTRICT,
    CONSTRAINT fk_movimientos_tipo
        FOREIGN KEY (tipo_movimiento_id) REFERENCES tipos_movimientos_inventario(id) ON DELETE RESTRICT,
    CONSTRAINT fk_movimientos_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_movimientos_consulta
        FOREIGN KEY (consulta_id) REFERENCES consultas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_movimientos_procedimiento
        FOREIGN KEY (procedimiento_id) REFERENCES procedimientos_especiales(id) ON DELETE RESTRICT,
    CONSTRAINT fk_movimientos_factura
        FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE RESTRICT,
    CONSTRAINT fk_movimientos_registro_jornada
        FOREIGN KEY (registro_jornada_id) REFERENCES registros_jornada_vacunacion(id) ON DELETE RESTRICT,
    CONSTRAINT chk_movimientos_un_origen
        CHECK ((consulta_id IS NOT NULL) + (procedimiento_id IS NOT NULL)
             + (factura_id IS NOT NULL) + (registro_jornada_id IS NOT NULL) <= 1),
    CONSTRAINT chk_movimientos_valores
        CHECK (cantidad > 0 AND stock_anterior >= 0 AND stock_nuevo >= 0)
);