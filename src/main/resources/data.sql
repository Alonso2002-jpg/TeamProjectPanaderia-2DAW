-- Inserts Categorias
INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Dulces', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Bebidas', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Utensilios', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Repostería', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Atención al cliente', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Limpieza', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Reparto', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('VIP', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Normal', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Distribuidor', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Mayorista', true);

INSERT INTO Categorias (nameCategory, isActive)
VALUES ('Proveedor', true);

--INSERTS PROVEEDORES
INSERT INTO Proveedores (nif, tipo, numero, nombre)
VALUES ('12345678A', 'Distribuidor', '123456789', 'Distribuciones Dulces');

INSERT INTO Proveedores (nif, tipo, numero, nombre)
VALUES ('98765432B', 'Mayorista', '987654321', 'Harina y Más');

INSERT INTO Proveedores (nif, tipo, numero, nombre)
VALUES ('87654321C', 'Proveedor', '123987456', 'Proveedores Bebidas');

INSERT INTO Proveedores (nif, tipo, numero, nombre)
VALUES ('34567891D', 'Distribuidor', '654321789', 'Proveedores Utensilios');

--INSERTS PRODUCTOS
INSERT INTO Productos (nombre, stock, precio, is_activo, categoria, proveedor)
VALUES ('Pan de Leche', 100, 2.5, true, 'Dulces', '12345678A');

INSERT INTO Productos (nombre, stock, precio, is_activo, categoria, proveedor)
VALUES ('Galletas de Chocolate', 150, 3.0, true, 'Dulces', '98765432B');

INSERT INTO Productos (nombre, stock, precio, is_activo, categoria, proveedor)
VALUES ('Agua Mineral', 200, 1.0, true, 'Bebidas', '87654321C');

INSERT INTO Productos (nombre, stock, precio, is_activo, categoria, proveedor)
VALUES ('Taza de Café', 50, 5.0, true, 'Utensilios', '34567891D');

--INSERTS PERSONAL
INSERT INTO Personal (dni, nombre, seccion, is_active)
VALUES ('12345678A', 'Juan Pérez', 'Cocina', true);

INSERT INTO Personal (dni, nombre, seccion, is_active)
VALUES ('23456789B', 'María López', 'Repostería', true);

INSERT INTO Personal (dni, nombre, seccion, is_active)
VALUES ('34567891C', 'Pedro Rodríguez', 'Atención al cliente', true);

INSERT INTO Personal (dni, nombre, seccion, is_active)
VALUES ('45678912D', 'Laura García', 'Limpieza', true);

INSERT INTO Personal (dni, nombre, seccion, is_active)
VALUES ('56789123E', 'Carlos Martínez', 'Reparto', true);

--INSERTS CLIENTES
INSERT INTO Cliente (nombre_completo, correo, dni, telefono, imagen, categoria, is_active)
VALUES ('Laura González', 'laura@example.com', '12345678A', '612345678', 'imagen1.jpg', 'Normal', true);

INSERT INTO Cliente (nombre_completo, correo, dni, telefono, imagen, categoria, is_active)
VALUES ('Pablo Pérez', 'pablo@example.com', '23456789B', '655432167', 'imagen2.jpg', 'Normal', true);

INSERT INTO Cliente (nombre_completo, correo, dni, telefono, imagen, categoria, is_active)
VALUES ('María Rodríguez', 'maria@example.com', '34567891C', '699876543', 'imagen3.jpg', 'VIP', true);

INSERT INTO Cliente (nombre_completo, correo, dni, telefono, imagen, categoria, is_active)
VALUES ('Carlos Martínez', 'carlos@example.com', '45678912D', '666123456', 'imagen4.jpg', 'Normal', true);

INSERT INTO Cliente (nombre_completo, correo, dni, telefono, imagen, categoria, is_active)
VALUES ('Lucía Sánchez', 'lucia@example.com', '56789123E', '677654321', 'imagen5.jpg', 'VIP', true);