-- Crear secuencia para categorias
CREATE SEQUENCE categorias_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

-- Crear tabla para categorias
CREATE TABLE categorias (
    id bigint DEFAULT nextval('categorias_id_seq') NOT NULL,
    name character varying(255),
    active boolean DEFAULT TRUE,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT categorias_pkey PRIMARY KEY (id)
);

-- Crear secuencia para proveedores
CREATE SEQUENCE proveedores_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

-- Crear tabla para proveedores
CREATE TABLE proveedores (
    id bigint DEFAULT nextval('proveedores_id_seq') NOT NULL,
    nif character varying(255),
    tipo bigint,
    numero character varying(255),
    name character varying(255),
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT proveedores_pkey PRIMARY KEY (id),
    CONSTRAINT fk_tipo FOREIGN KEY (tipo) REFERENCES categorias (id) NOT DEFERRABLE
);


-- Crear tabla para productos
CREATE TABLE productos (
    id uuid NOT NULL,
    nombre character varying(255),
    stock integer,
    precio double precision,
    is_activo boolean DEFAULT TRUE,
    categoria_id bigint,
    proveedor_id bigint,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT productos_pkey PRIMARY KEY (id),
    CONSTRAINT fk_categoria FOREIGN KEY (categoria_id) REFERENCES categorias (id) NOT DEFERRABLE,
    CONSTRAINT fk_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores (id) NOT DEFERRABLE
);

-- Crear secuencia para usuarios
CREATE SEQUENCE usuarios_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

-- Crear tabla para usuarios
CREATE TABLE users (
    id bigint DEFAULT nextval('usuarios_id_seq') NOT NULL,
    name character varying(255),
    username character varying(255),
    email character varying(255),
    password character varying(255),
    is_active boolean DEFAULT TRUE,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT usuarios_email_key UNIQUE (email),
    CONSTRAINT usuarios_username_key UNIQUE (username)
);

-- Crear tabla para user_roles
CREATE TABLE user_roles (
    user_id bigint NOT NULL,
    roles character varying(255)
);


-- Crear tabla para personal
CREATE TABLE personal (
    id uuid NOT NULL,
    dni character varying(255),
    nombre character varying(255),
    email character varying(255),
    user_id bigint,
    seccion bigint,
    active boolean DEFAULT TRUE,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT personal_pkey PRIMARY KEY (id),
    CONSTRAINT fk_seccion FOREIGN KEY (seccion) REFERENCES categorias (id) NOT DEFERRABLE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) NOT DEFERRABLE
);

-- Crear secuencia para clientes
CREATE SEQUENCE clientes_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

-- Crear tabla para clientes
CREATE TABLE clientes (
    id bigint DEFAULT nextval('clientes_id_seq') NOT NULL,
    nombre_completo character varying(255),
    correo character varying(255),
    dni character varying(255),
    telefono text ,
    imagen text DEFAULT 'https://www.realmadrid.com/img/vertical_380px/cristiano_550x650_20180917025046.jpg',
    categoria_id bigint,
    direccion jsonb,
    is_active boolean DEFAULT TRUE,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT clientes_pkey PRIMARY KEY (id),
    CONSTRAINT fk_categoria_cliente FOREIGN KEY (categoria_id) REFERENCES categorias (id) NOT DEFERRABLE
);

-- Crear claves foráneas
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles FOREIGN KEY (user_id) REFERENCES users (id) NOT DEFERRABLE;
ALTER TABLE personal ADD CONSTRAINT fk_personal_user FOREIGN KEY (user_id) REFERENCES users (id) NOT DEFERRABLE;
-- Inserts Categorias
INSERT INTO CATEGORIAS (name, active) VALUES ('Dulces', TRUE);--1
INSERT INTO CATEGORIAS (name, active) VALUES ('Bebidas', TRUE);--2
INSERT INTO CATEGORIAS (name, active) VALUES ('Utensilios', TRUE);--3
INSERT INTO CATEGORIAS (name, active) VALUES ('Repostería', TRUE);--4
INSERT INTO CATEGORIAS (name, active) VALUES ('Atención al cliente', TRUE);--5
INSERT INTO CATEGORIAS (name, active) VALUES ('Limpieza', TRUE);--6
INSERT INTO CATEGORIAS (name, active) VALUES ('Reparto', TRUE);--7
INSERT INTO CATEGORIAS (name, active) VALUES ('VIP', TRUE);--8
INSERT INTO CATEGORIAS (name, active) VALUES ('Normal', TRUE);--9
INSERT INTO CATEGORIAS (name, active) VALUES ('Distribuidor', TRUE);--10
INSERT INTO CATEGORIAS (name, active) VALUES ('Mayorista', TRUE);--11
INSERT INTO CATEGORIAS (name, active) VALUES ('Proveedor', TRUE);--12
--INSERTS PROVEEDORES
INSERT INTO Proveedores (nif, tipo, numero, name)--1
VALUES ('12345678A', 10, '123456789', 'Distribuciones Dulces');

INSERT INTO Proveedores (nif, tipo, numero, name)--2
VALUES ('98765432B', 11, '987654321', 'Harina y Más');

INSERT INTO Proveedores (nif, tipo, numero, name)--3
VALUES ('87654321C', 12, '123987456', 'Proveedores Bebidas');

INSERT INTO Proveedores (nif, tipo, numero, name)--4
VALUES ('34567891D', 10, '654321789', 'Proveedores Utensilios');
--INSERTS PRODUCTOS
INSERT INTO Productos (id,nombre, stock, precio, is_activo, categoria_id, proveedor_id)
VALUES ('fc334e19-c0f6-46a0-910c-4a1e9a82230e','Pan de Leche', 100, 2.5, true, 1, 1);

INSERT INTO Productos (id,nombre, stock, precio, is_activo, categoria_id, proveedor_id)
VALUES ('8cf17c52-52f4-4b1b-a7c8-eb9b073b3f9c','Galletas de Chocolate', 150, 3.0, true, 1, 2);

INSERT INTO Productos (id,nombre, stock, precio, is_activo, categoria_id, proveedor_id)
VALUES ('50a3e2cb-eb04-4c63-a7db-1b7e398f5ab1','Agua Mineral', 200, 1.0, true, 2, 3);

INSERT INTO Productos (id,nombre, stock, precio, is_activo, categoria_id, proveedor_id)
VALUES ('dd3e3c46-6a86-4c08-90a9-04026960e962','Taza de Café', 50, 5.0, true, 3, 4);
--USERS
--Password: Admin1
insert into USERS(name, username, email, password, is_active)
values ('Admin Admin', 'admin', 'admin@prueba.net',
        '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2', true);
-- Contraseña: User1
insert into USERS(name, username, email, password, is_active)
values ('User User', 'user', 'user@prueba.net',
        '$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.', true);
--USUARIOS DE PERSONAL
--Password: juPerez678A
insert into USERS(name, username, email, password, is_active)
values ('Juan Perez', '12345678A', 'juan@pepitos.org', '$2a$12$yFVUheT4Sh6zk2F2nmeSEO1ZGB6Z.SbAq64F6A7jdG4BCak84Z1l.',true);
--Passowrd: maRodriguez789B
insert into USERS(name, username, email, password, is_active)
values ('Maria Rodriguez', '23456789B', 'maria@pepitos.org', '$2a$12$.8gf55T8bFOE2wMkHq9LseFDJcOW2DZkJ9iPbRKAm0qNFCK010nl6',true);
--Password: peRodríguez891C
insert into USERS(name, username, email, password, is_active)
values ('Pedro Gutierrez', '34567891C', 'pedro@pepitos.org', '$2a$12$BIBuT5L8h5Rp/NkPrldBC.D/feWjJ.p/JraA8J8XZUI9ejoMhTHKi',true);
--Password: laGarcía912D
insert into USERS(name, username, email, password, is_active)
values ('Laura Gimenez', '45678912D', 'laura@pepitos.org', '$2a$12$9FdxEUKBbzXa2awg2YtcPOH1Os6APCFtQ6cm4nRCu7KUEUWb/Igri',true);
--Password: caMartínez123E
insert into USERS(name, username, email, password, is_active)
values ('Carlos Martinez', '56789123E', 'carlos@pepitos.org', '$2a$12$UG0m1nm34QgqOxKYnAWIwOEUX9.g3q4p/GM.jEQbzN8.DVtIcme8O',true);
--USER ROLES
insert into USER_ROLES (user_id, roles)
values (1, 'USER');
insert into USER_ROLES (user_id, roles)
values (1, 'ADMIN');
insert into USER_ROLES (user_id, roles)
values (2, 'USER');
insert into USER_ROLES (user_id, roles)
values (3, 'USER');
insert into USER_ROLES (user_id, roles)
values (4, 'USER');
insert into USER_ROLES (user_id, roles)
values (4, 'SELLER');
insert into USER_ROLES (user_id, roles)
values (5, 'USER');
insert into USER_ROLES (user_id, roles)
values (5, 'SELLER');
insert into USER_ROLES (user_id, roles)
values (6, 'USER');
insert into USER_ROLES (user_id, roles)
values (7, 'USER');
--INSERTS PERSONAL
INSERT INTO Personal (id,dni, nombre,email,user_id, seccion, active)
VALUES ('b0f8931b-94b8-49e3-a4b0-64d19486a6f5','12345678A', 'Juan Pérez','juan@pepitos.org', 3,4, true);

INSERT INTO Personal (id,dni, nombre,email,user_id, seccion, active)
VALUES ('edc1c52b-7856-4f20-a23e-42f6154a6938','23456789B', 'María López','maria@pepitos.org',4, 5, true);

INSERT INTO Personal (id, dni, nombre,email,user_id, seccion, active)
VALUES ('a7c5a309-8247-4a0f-a97e-d8051f21f768','34567891C', 'Pedro Rodríguez','pedro@pepitos.org',5, 5, true);

INSERT INTO Personal (id, dni, nombre,email,user_id, seccion, active)
VALUES ('cef32a51-0a56-4e5c-b5a1-95d9eaa4a456','45678912D', 'Laura García','laura@pepitos.org', 6,6, true);

INSERT INTO Personal (id, dni, nombre,email,user_id, seccion, active)
VALUES ('76a63d82-5063-4c3f-8c47-62db5757b915','56789123E', 'Carlos Martínez','carlos@pepitos.org',7, 7, true);
--INSERTS CLIENTES
INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono,categoria_id,direccion, is_active)
VALUES ('Laura González', 'laura@example.com', '12345678A', '612345678',9,'{"calle":"Calle A","numero":"123","ciudad":"Ciudad X","provincia":"Provincia Y","pais":"Pais Z","codPostal":"12345"}', true);

INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono,categoria_id, direccion,is_active)
VALUES ('Pablo Pérez', 'pablo@example.com', '23456789B', '655432167', 9, '{"calle":"Avenida B","numero":"456","ciudad":"Ciudad Y","provincia":"Provincia X","pais":"Pais Z","codPostal":"54321"}',true);

INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono,categoria_id, direccion,is_active)
VALUES ('María Rodríguez', 'maria@example.com', '34567891C', '699876543', 8, '{"calle":"Calle Principal","numero":"789","ciudad":"Ciudad Z","provincia":"Provincia Z","pais":"Pais X","codPostal":"67890"}',true);

INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono, categoria_id, direccion,is_active)
VALUES ('Carlos Martínez', 'carlos@example.com', '45678912D', '666123456', 9, '{"calle":"Calle Central","numero":"234","ciudad":"Ciudad X","provincia":"Provincia Y","pais":"Pais Z","codPostal":"34567"}',true);

INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono, categoria_id, direccion,is_active)
VALUES ('Lucía Sánchez', 'lucia@example.com', '56789123E', '677654321',  8, '{"calle":"Avenida Principal","numero":"567","ciudad":"Ciudad Z","provincia":"Provincia X","pais":"Pais Y","codPostal":"45678"}',true);
