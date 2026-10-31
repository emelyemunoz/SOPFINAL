DROP DATABASE IF EXISTS mydb;

CREATE DATABASE mydb
DEFAULT CHARACTER SET utf8mb3;

USE mydb;

SET FOREIGN_KEY_CHECKS = 0;


-- =====================================================
-- CLIENTE
-- =====================================================

CREATE TABLE cliente (
    idCliente INT NOT NULL AUTO_INCREMENT,
    Nombre_usuario VARCHAR(45) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    NombreCompleto VARCHAR(50) NOT NULL,
    Telefono INT NOT NULL,
    Correo_electronico VARCHAR(45) NOT NULL,
    NIT VARCHAR(45) NOT NULL,
    Rol VARCHAR(20) NOT NULL DEFAULT 'CLIENTE',

    PRIMARY KEY (idCliente),

    UNIQUE KEY NombreUsuario_UNIQUE (Nombre_usuario),
    UNIQUE KEY Correo_UNIQUE (Correo_electronico),
    UNIQUE KEY NIT_UNIQUE (NIT)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


-- =====================================================
-- LOTE
-- =====================================================

CREATE TABLE lote (
    idLote INT NOT NULL AUTO_INCREMENT,
    Estilo VARCHAR(45) DEFAULT NULL,
    Stock INT DEFAULT NULL,
    Talla DOUBLE DEFAULT NULL,

    PRIMARY KEY (idLote)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


-- =====================================================
-- PEDIDO
-- =====================================================

CREATE TABLE pedido (
    idPedido INT NOT NULL AUTO_INCREMENT,
    Fecha DATE NOT NULL,
    Estado VARCHAR(45) NOT NULL DEFAULT 'PENDIENTE',
    Total DOUBLE NOT NULL,
    Cliente_idCliente INT NOT NULL,

    PRIMARY KEY (idPedido),

    CONSTRAINT fk_Pedido_Cliente
        FOREIGN KEY (Cliente_idCliente)
        REFERENCES cliente (idCliente)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


-- =====================================================
-- PRODUCTO
-- =====================================================

CREATE TABLE producto (
    idProducto INT NOT NULL AUTO_INCREMENT,
    Nombre_producto VARCHAR(45) NOT NULL,
    Descripcion LONGTEXT NOT NULL,
    Precio DOUBLE NOT NULL,
    Lote_idLote INT NOT NULL,

    PRIMARY KEY (idProducto),

    CONSTRAINT fk_Producto_Lote
        FOREIGN KEY (Lote_idLote)
        REFERENCES lote (idLote)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


-- =====================================================
-- DETALLE PEDIDO
-- =====================================================

CREATE TABLE detalle_pedido (
    idDetalle INT NOT NULL AUTO_INCREMENT,
    Precio_unitario DOUBLE NOT NULL,
    Cantidad INT NOT NULL,
    Sub_total DOUBLE NOT NULL,
    Pedido_idPedido INT NOT NULL,
    Producto_idProducto INT NOT NULL,

    PRIMARY KEY (idDetalle),

    CONSTRAINT fk_Detalle_Pedido
        FOREIGN KEY (Pedido_idPedido)
        REFERENCES pedido (idPedido),

    CONSTRAINT fk_Detalle_Producto
        FOREIGN KEY (Producto_idProducto)
        REFERENCES producto (idProducto)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


-- =====================================================
-- FACTURA
-- =====================================================

CREATE TABLE factura (
    idFactura INT NOT NULL AUTO_INCREMENT,
    Direccion VARCHAR(50) NOT NULL,
    Fecha_emision DATE NOT NULL,
    Nombre_Archivo VARCHAR(45) NOT NULL,
    rutaSFTP LONGTEXT NOT NULL,
    Pedido_idPedido INT NOT NULL,

    PRIMARY KEY (idFactura),

    CONSTRAINT fk_Factura_Pedido
        FOREIGN KEY (Pedido_idPedido)
        REFERENCES pedido (idPedido)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


SET FOREIGN_KEY_CHECKS = 1;


-- =====================================================
-- LOTES DE PRUEBA
-- =====================================================

INSERT INTO lote (Estilo, Stock, Talla)
VALUES
('Air Max', 10, 38),
('Running', 15, 39),
('Casual', 8, 40),
('Dunk', 12, 41);


-- =====================================================
-- PRODUCTOS DE PRUEBA
-- =====================================================

INSERT INTO producto
(Nombre_producto, Descripcion, Precio, Lote_idLote)
VALUES
('Nike Air Max', 'Tenis para uso diario', 850.00, 1),
('Adidas Run', 'Tenis para correr', 650.00, 2),
('New Balance 550', 'Tenis de estilo casual', 900.00, 3),
('Nike Dunk', 'Tenis de estilo urbano', 795.00, 4);


-- =====================================================
-- VERIFICACIÓN
-- =====================================================

-- -----------------------------------------------------
-- Table structure for table auditoria
-- -----------------------------------------------------

DROP TABLE IF EXISTS auditoria;

CREATE TABLE auditoria (
    idAuditoria INT NOT NULL AUTO_INCREMENT,

    idUsuario INT DEFAULT NULL,
    usuario VARCHAR(45) NOT NULL,
    rol VARCHAR(20) NOT NULL,

    accion VARCHAR(50) NOT NULL,
    entidad VARCHAR(50) NOT NULL,
    idEntidad INT DEFAULT NULL,

    idUsuarioAfectado INT DEFAULT NULL,
    usuarioAfectado VARCHAR(45) DEFAULT NULL,

    descripcion VARCHAR(500) NOT NULL,

    valorAnterior TEXT DEFAULT NULL,
    valorNuevo TEXT DEFAULT NULL,

    fechaHora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (idAuditoria),

    INDEX idx_auditoria_usuario (idUsuario),
    INDEX idx_auditoria_fecha (fechaHora),
    INDEX idx_auditoria_accion (accion),
    INDEX idx_auditoria_entidad (entidad)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
SHOW TABLES;

DESCRIBE cliente;


SELECT * FROM lote;
SELECT * from cliente;
SELECT * from auditoria;
SELECT * FROM producto;
SELECT * FROM detalle_pedido;
SELECT * FROM pedido;
