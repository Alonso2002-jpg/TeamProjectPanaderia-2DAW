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
VALUES (UUID(),'Pan de Leche', 100, 2.5, true, 1, 1);

INSERT INTO Productos (id,nombre, stock, precio, is_activo, categoria_id, proveedor_id)
VALUES (UUID(),'Galletas de Chocolate', 150, 3.0, true, 1, 2);

INSERT INTO Productos (id,nombre, stock, precio, is_activo, categoria_id, proveedor_id)
VALUES (UUID(),'Agua Mineral', 200, 1.0, true, 2, 3);

INSERT INTO Productos (id,nombre, stock, precio, is_activo, categoria_id, proveedor_id)
VALUES (UUID(),'Taza de Café', 50, 5.0, true, 3, 4);

--INSERTS PERSONAL
INSERT INTO Personal (id,dni, nombre, seccion, active)
VALUES (UUID(),'12345678A', 'Juan Pérez', 4, true);

INSERT INTO Personal (id,dni, nombre, seccion, active)
VALUES (UUID(),'23456789B', 'María López', 4, true);

INSERT INTO Personal (id, dni, nombre, seccion, active)
VALUES (UUID(),'34567891C', 'Pedro Rodríguez', 5, true);

INSERT INTO Personal (id, dni, nombre, seccion, active)
VALUES (UUID(),'45678912D', 'Laura García', 6, true);

INSERT INTO Personal (id, dni, nombre, seccion, active)
VALUES (UUID(),'56789123E', 'Carlos Martínez', 7, true);

--INSERTS CLIENTES
INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono,categoria_id, is_active)
VALUES ('Laura González', 'laura@example.com', '12345678A', '612345678',9, true);

INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono,categoria_id, is_active)
VALUES ('Pablo Pérez', 'pablo@example.com', '23456789B', '655432167', 9, true);

INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono,categoria_id, is_active)
VALUES ('María Rodríguez', 'maria@example.com', '34567891C', '699876543', 8, true);

INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono, categoria_id, is_active)
VALUES ('Carlos Martínez', 'carlos@example.com', '45678912D', '666123456', 9, true);

INSERT INTO CLIENTES (nombre_completo, correo, dni, telefono, categoria_id, is_active)
VALUES ('Lucía Sánchez', 'lucia@example.com', '56789123E', '677654321',  8, true);