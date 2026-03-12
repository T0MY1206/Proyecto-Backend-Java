-- Ejecutar en la base de datos (Supabase SQL Editor o psql) si aparecen errores
-- "there is no primary key for referenced table". Las tablas existían sin PK;
-- al borrarlas, al reiniciar la app Hibernate las creará bien.
-- Orden: primero las que tienen FK, luego las referenciadas.

DROP TABLE IF EXISTS detalle_venta;
DROP TABLE IF EXISTS venta;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS sucursal;
