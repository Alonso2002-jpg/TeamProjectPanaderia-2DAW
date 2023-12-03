db.createUser({
  user: 'admin',
  pwd: 'admin123',
  roles: [
    {
      role: 'readWrite',
      db: 'panaderia',
    },
  ],
});
db = db.getSiblingDB('panaderia');

// Creamos la coleccion city
db.createCollection('pedidos');