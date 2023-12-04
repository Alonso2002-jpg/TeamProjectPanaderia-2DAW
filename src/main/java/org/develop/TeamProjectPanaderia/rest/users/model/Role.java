package org.develop.TeamProjectPanaderia.rest.users.model;

/**
 * Enumeración que representa los roles disponibles en el sistema.
 * Pueden ser asignados a los usuarios para definir sus permisos y nivel de acceso.
 */
public enum Role {
    /**
     * Rol básico para usuarios normales.
     */
    USER,
    SELLER,
    /**
     * Rol para administradores con privilegios adicionales.
     */
    ADMIN
}
