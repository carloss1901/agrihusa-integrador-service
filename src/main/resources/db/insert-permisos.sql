/*=============================================================================
  BASE DE DATOS : BD_AGRIHUSA
  MOTOR         : Microsoft SQL Server
  CONVENCIÓN    : MAYÚSCULAS_CON_GUION_BAJO

  Objetos:
    PK_<TABLA>                    Primary Key
    FK_<TABLA>_<TABLA_REFERIDA>   Foreign Key
    UQ_<TABLA>_<COLUMNAS>         Unique Constraint
    CK_<TABLA>_<REGLA>            Check Constraint
    DF_<TABLA>_<COLUMNA>          Default Constraint
    IX_<TABLA>_<COLUMNAS>         Index
=============================================================================*/

SET NOCOUNT ON;
SET XACT_ABORT ON;
GO

IF DB_ID(N'BD_AGRIHUSA') IS NULL
BEGIN
    CREATE DATABASE BD_AGRIHUSA;
END;
GO

USE BD_AGRIHUSA;
GO

/*=============================================================================
  SEGURIDAD
=============================================================================*/

IF OBJECT_ID(N'dbo.usuario', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.usuario
    (
        USUARIO_ID          INT IDENTITY(1,1) NOT NULL,
        DNI                 VARCHAR(20)        NOT NULL,
        USUARIO             VARCHAR(30)        NOT NULL,
        NOMBRES             NVARCHAR(80)       NOT NULL,
        APELLIDO_PATERNO    NVARCHAR(80)       NOT NULL,
        APELLIDO_MATERNO    NVARCHAR(80)       NOT NULL,
        CORREO              VARCHAR(120)       NOT NULL,
        TELEFONO            VARCHAR(20)        NULL,
        CONTRASENIA         VARCHAR(255)       NOT NULL,
        ES_SISTEMA          BIT                NOT NULL CONSTRAINT DF_USUARIO_ES_SISTEMA DEFAULT (0),
        RESET_CONTRASENIA   BIT                NOT NULL CONSTRAINT DF_USUARIO_RESET_CONTRASENIA DEFAULT (1),
        ULTIMO_ACCESO       DATETIME2(0)       NULL,
        ACTIVO              BIT                NOT NULL CONSTRAINT DF_USUARIO_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)       NOT NULL CONSTRAINT DF_USUARIO_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)       NULL,

        CONSTRAINT PK_USUARIO PRIMARY KEY CLUSTERED (USUARIO_ID),
        CONSTRAINT UQ_USUARIO_DNI UNIQUE (DNI),
        CONSTRAINT UQ_USUARIO_USUARIO UNIQUE (USUARIO),
        CONSTRAINT UQ_USUARIO_CORREO UNIQUE (CORREO),
        CONSTRAINT CK_USUARIO_DNI_NO_VACIO CHECK (LEN(LTRIM(RTRIM(DNI))) > 0),
        CONSTRAINT CK_USUARIO_USUARIO_NO_VACIO CHECK (LEN(LTRIM(RTRIM(USUARIO))) > 0),
        CONSTRAINT CK_USUARIO_CORREO_NO_VACIO CHECK (LEN(LTRIM(RTRIM(CORREO))) > 0)
    );
END;
GO

IF OBJECT_ID(N'dbo.rol', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.rol
    (
        ROL_ID              INT IDENTITY(1,1) NOT NULL,
        NOMBRE              NVARCHAR(100)     NOT NULL,
        DESCRIPCION         NVARCHAR(250)     NULL,
        ES_SISTEMA          BIT               NOT NULL CONSTRAINT DF_ROL_ES_SISTEMA DEFAULT (0),
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_ROL_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_ROL_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_ROL PRIMARY KEY CLUSTERED (ROL_ID),
        CONSTRAINT UQ_ROL_NOMBRE UNIQUE (NOMBRE)
    );
END;
GO

IF OBJECT_ID(N'dbo.usuario_rol', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.usuario_rol
    (
        USUARIO_ROL_ID      INT IDENTITY(1,1) NOT NULL,
        USUARIO_ID          INT               NOT NULL,
        ROL_ID              INT               NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_USUARIO_ROL_ACTIVO DEFAULT (1),
        FECHA_ASIGNACION    DATETIME2(0)      NOT NULL CONSTRAINT DF_USUARIO_ROL_FECHA_ASIGNACION DEFAULT (SYSDATETIME()),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_USUARIO_ROL_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_USUARIO_ROL PRIMARY KEY CLUSTERED (USUARIO_ROL_ID),
        CONSTRAINT UQ_USUARIO_ROL_USUARIO_ROL UNIQUE (USUARIO_ID, ROL_ID),
        CONSTRAINT FK_USUARIO_ROL_USUARIO FOREIGN KEY (USUARIO_ID) REFERENCES dbo.usuario (USUARIO_ID),
        CONSTRAINT FK_USUARIO_ROL_ROL FOREIGN KEY (ROL_ID) REFERENCES dbo.rol (ROL_ID)
    );
END;
GO

IF OBJECT_ID(N'dbo.modulo', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.modulo
    (
        MODULO_ID           INT IDENTITY(1,1) NOT NULL,
        CODIGO              VARCHAR(50)       NOT NULL,
        NOMBRE              NVARCHAR(100)     NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_MODULO_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_MODULO_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_MODULO PRIMARY KEY CLUSTERED (MODULO_ID),
        CONSTRAINT UQ_MODULO_CODIGO UNIQUE (CODIGO),
        CONSTRAINT UQ_MODULO_NOMBRE UNIQUE (NOMBRE)
    );
END;
GO

IF OBJECT_ID(N'dbo.permiso', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.permiso
    (
        PERMISO_ID          INT IDENTITY(1,1) NOT NULL,
        ACCION              VARCHAR(50)       NOT NULL,
        DESCRIPCION         NVARCHAR(150)     NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_PERMISO_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_PERMISO_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_PERMISO PRIMARY KEY CLUSTERED (PERMISO_ID),
        CONSTRAINT UQ_PERMISO_ACCION UNIQUE (ACCION)
    );
END;
GO

IF OBJECT_ID(N'dbo.rol_permiso', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.rol_permiso
    (
        ROL_PERMISO_ID      INT IDENTITY(1,1) NOT NULL,
        ROL_ID              INT               NOT NULL,
        MODULO_ID           INT               NOT NULL,
        PERMISO_ID          INT               NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_ROL_PERMISO_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_ROL_PERMISO_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_ROL_PERMISO PRIMARY KEY CLUSTERED (ROL_PERMISO_ID),
        CONSTRAINT UQ_ROL_PERMISO_ROL_MODULO_PERMISO UNIQUE (ROL_ID, MODULO_ID, PERMISO_ID),
        CONSTRAINT FK_ROL_PERMISO_ROL FOREIGN KEY (ROL_ID) REFERENCES dbo.rol (ROL_ID),
        CONSTRAINT FK_ROL_PERMISO_MODULO FOREIGN KEY (MODULO_ID) REFERENCES dbo.modulo (MODULO_ID),
        CONSTRAINT FK_ROL_PERMISO_PERMISO FOREIGN KEY (PERMISO_ID) REFERENCES dbo.permiso (PERMISO_ID)
    );
END;
GO

/*=============================================================================
  MAESTROS
=============================================================================*/

IF OBJECT_ID(N'dbo.bitacora', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.bitacora
    (
        BITACORA_ID         BIGINT IDENTITY(1,1) NOT NULL,
        FECHA               DATETIME2(0)         NOT NULL CONSTRAINT DF_BITACORA_FECHA DEFAULT (SYSDATETIME()),
        USUARIO_ID          INT                  NULL,
        MODULO              NVARCHAR(50)         NOT NULL,
        ACCION              VARCHAR(50)          NOT NULL,
        ENTIDAD             NVARCHAR(100)        NOT NULL,
        REGISTRO_ID         INT                  NULL,
        DETALLE             NVARCHAR(500)        NOT NULL,
        RESULTADO           VARCHAR(20)          NOT NULL,
        ACTIVO              BIT                  NOT NULL CONSTRAINT DF_BITACORA_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)         NOT NULL CONSTRAINT DF_BITACORA_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)         NULL,

        CONSTRAINT PK_BITACORA PRIMARY KEY CLUSTERED (BITACORA_ID),
        CONSTRAINT FK_BITACORA_USUARIO FOREIGN KEY (USUARIO_ID) REFERENCES dbo.usuario (USUARIO_ID)
    );
END;
GO

IF OBJECT_ID(N'dbo.cliente', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.cliente
    (
        CLIENTE_ID          INT IDENTITY(1,1) NOT NULL,
        TIPO_DOCUMENTO      VARCHAR(30)       NOT NULL,
        NUMERO_DOCUMENTO    VARCHAR(30)       NOT NULL,
        RAZON_SOCIAL        NVARCHAR(200)     NOT NULL,
        NOMBRE_COMERCIAL    NVARCHAR(200)     NULL,
        CONTACTO            NVARCHAR(150)     NULL,
        CORREO              VARCHAR(150)      NULL,
        TELEFONO            VARCHAR(30)       NULL,
        DIRECCION           NVARCHAR(250)     NULL,
        PAIS                NVARCHAR(100)     NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_CLIENTE_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_CLIENTE_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_CLIENTE PRIMARY KEY CLUSTERED (CLIENTE_ID),
        CONSTRAINT UQ_CLIENTE_NUMERO_DOCUMENTO UNIQUE (NUMERO_DOCUMENTO),
        CONSTRAINT UQ_CLIENTE_RAZON_SOCIAL UNIQUE (RAZON_SOCIAL)
    );
END;
GO

IF OBJECT_ID(N'dbo.naviera', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.naviera
    (
        NAVIERA_ID          INT IDENTITY(1,1) NOT NULL,
        CODIGO              VARCHAR(30)       NOT NULL,
        NOMBRE              NVARCHAR(150)     NOT NULL,
        PAIS                NVARCHAR(100)     NOT NULL,
        CONTACTO            NVARCHAR(150)     NULL,
        CORREO              VARCHAR(150)      NULL,
        TELEFONO            VARCHAR(30)       NULL,
        SITIO_WEB           VARCHAR(250)      NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_NAVIERA_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_NAVIERA_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_NAVIERA PRIMARY KEY CLUSTERED (NAVIERA_ID),
        CONSTRAINT UQ_NAVIERA_CODIGO UNIQUE (CODIGO),
        CONSTRAINT UQ_NAVIERA_NOMBRE UNIQUE (NOMBRE)
    );
END;
GO

IF OBJECT_ID(N'dbo.destino', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.destino
    (
        DESTINO_ID          INT IDENTITY(1,1) NOT NULL,
        PAIS                NVARCHAR(100)     NOT NULL,
        CIUDAD              NVARCHAR(100)     NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_DESTINO_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_DESTINO_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_DESTINO PRIMARY KEY CLUSTERED (DESTINO_ID),
        CONSTRAINT UQ_DESTINO_PAIS_CIUDAD UNIQUE (PAIS, CIUDAD)
    );
END;
GO

IF OBJECT_ID(N'dbo.operador_logistico', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.operador_logistico
    (
        OPERADOR_LOGISTICO_ID INT IDENTITY(1,1) NOT NULL,
        RUC                   VARCHAR(11)       NOT NULL,
        RAZON_SOCIAL          NVARCHAR(200)     NOT NULL,
        NOMBRE_COMERCIAL      NVARCHAR(200)     NULL,
        CONTACTO              NVARCHAR(150)     NULL,
        CORREO                VARCHAR(150)      NULL,
        TELEFONO              VARCHAR(30)       NULL,
        DIRECCION             NVARCHAR(250)     NULL,
        ACTIVO                BIT               NOT NULL CONSTRAINT DF_OPERADOR_LOGISTICO_ACTIVO DEFAULT (1),
        FECHA_CREACION        DATETIME2(0)      NOT NULL CONSTRAINT DF_OPERADOR_LOGISTICO_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION    DATETIME2(0)      NULL,

        CONSTRAINT PK_OPERADOR_LOGISTICO PRIMARY KEY CLUSTERED (OPERADOR_LOGISTICO_ID),
        CONSTRAINT UQ_OPERADOR_LOGISTICO_RUC UNIQUE (RUC)
    );
END;
GO

IF OBJECT_ID(N'dbo.puerto_llegada', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.puerto_llegada
    (
        PUERTO_LLEGADA_ID   INT IDENTITY(1,1) NOT NULL,
        CODIGO              VARCHAR(30)       NOT NULL,
        PUERTO              NVARCHAR(150)     NOT NULL,
        PAIS                NVARCHAR(100)     NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_PUERTO_LLEGADA_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_PUERTO_LLEGADA_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_PUERTO_LLEGADA PRIMARY KEY CLUSTERED (PUERTO_LLEGADA_ID),
        CONSTRAINT UQ_PUERTO_LLEGADA_CODIGO UNIQUE (CODIGO)
    );
END;
GO

IF OBJECT_ID(N'dbo.producto', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.producto
    (
        PRODUCTO_ID         INT IDENTITY(1,1) NOT NULL,
        CODIGO              VARCHAR(30)       NOT NULL,
        NOMBRE              NVARCHAR(150)     NOT NULL,
        DESCRIPCION         NVARCHAR(500)     NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_PRODUCTO_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_PRODUCTO_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_PRODUCTO PRIMARY KEY CLUSTERED (PRODUCTO_ID),
        CONSTRAINT UQ_PRODUCTO_CODIGO UNIQUE (CODIGO),
        CONSTRAINT UQ_PRODUCTO_NOMBRE UNIQUE (NOMBRE)
    );
END;
GO

IF OBJECT_ID(N'dbo.variedad', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.variedad
    (
        VARIEDAD_ID         INT IDENTITY(1,1) NOT NULL,
        PRODUCTO_ID         INT               NOT NULL,
        NOMBRE              NVARCHAR(150)     NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_VARIEDAD_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_VARIEDAD_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_VARIEDAD PRIMARY KEY CLUSTERED (VARIEDAD_ID),
        CONSTRAINT UQ_VARIEDAD_PRODUCTO_NOMBRE UNIQUE (PRODUCTO_ID, NOMBRE),
        CONSTRAINT UQ_VARIEDAD_PRODUCTO_VARIEDAD UNIQUE (PRODUCTO_ID, VARIEDAD_ID),
        CONSTRAINT FK_VARIEDAD_PRODUCTO FOREIGN KEY (PRODUCTO_ID) REFERENCES dbo.producto (PRODUCTO_ID)
    );
END;
GO

IF OBJECT_ID(N'dbo.via', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.via
    (
        VIA_ID              INT IDENTITY(1,1) NOT NULL,
        DESCRIPCION         NVARCHAR(100)     NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_VIA_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_VIA_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_VIA PRIMARY KEY CLUSTERED (VIA_ID),
        CONSTRAINT UQ_VIA_DESCRIPCION UNIQUE (DESCRIPCION)
    );
END;
GO

IF OBJECT_ID(N'dbo.situacion', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.situacion
    (
        SITUACION_ID        INT IDENTITY(1,1) NOT NULL,
        DESCRIPCION         NVARCHAR(100)     NOT NULL,
        ACTIVO              BIT               NOT NULL CONSTRAINT DF_SITUACION_ACTIVO DEFAULT (1),
        FECHA_CREACION      DATETIME2(0)      NOT NULL CONSTRAINT DF_SITUACION_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION  DATETIME2(0)      NULL,

        CONSTRAINT PK_SITUACION PRIMARY KEY CLUSTERED (SITUACION_ID),
        CONSTRAINT UQ_SITUACION_DESCRIPCION UNIQUE (DESCRIPCION)
    );
END;
GO

/*=============================================================================
  OPERACIÓN
=============================================================================*/

IF OBJECT_ID(N'dbo.despacho', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.despacho
    (
        DESPACHO_ID             INT IDENTITY(1,1) NOT NULL,
        CODIGO                  VARCHAR(30)       NOT NULL,
        FECHA_DESPACHO          DATE              NOT NULL,
        FECHA_ESTIMADA_LLEGADA  DATE              NOT NULL,
        CLIENTE_ID              INT               NOT NULL,
        NAVIERA_ID              INT               NOT NULL,
        DESTINO_ID              INT               NOT NULL,
        OPERADOR_LOGISTICO_ID   INT               NOT NULL,
        PUERTO_LLEGADA_ID       INT               NOT NULL,
        PRODUCTO_ID             INT               NOT NULL,
        VARIEDAD_ID             INT               NOT NULL,
        VIA_ID                  INT               NOT NULL,
        SITUACION_ID            INT               NOT NULL,
        CANTIDAD                DECIMAL(12,2)     NOT NULL,
        UNIDAD_MEDIDA           VARCHAR(30)       NOT NULL,
        NUMERO_CONTENEDOR       VARCHAR(20)       NOT NULL,
        OBSERVACIONES           NVARCHAR(500)     NULL,
        ACTIVO                  BIT               NOT NULL CONSTRAINT DF_DESPACHO_ACTIVO DEFAULT (1),
        FECHA_CREACION          DATETIME2(0)      NOT NULL CONSTRAINT DF_DESPACHO_FECHA_CREACION DEFAULT (SYSDATETIME()),
        FECHA_MODIFICACION      DATETIME2(0)      NULL,

        CONSTRAINT PK_DESPACHO PRIMARY KEY CLUSTERED (DESPACHO_ID),
        CONSTRAINT UQ_DESPACHO_CODIGO UNIQUE (CODIGO),
        CONSTRAINT FK_DESPACHO_CLIENTE FOREIGN KEY (CLIENTE_ID) REFERENCES dbo.cliente (CLIENTE_ID),
        CONSTRAINT FK_DESPACHO_NAVIERA FOREIGN KEY (NAVIERA_ID) REFERENCES dbo.naviera (NAVIERA_ID),
        CONSTRAINT FK_DESPACHO_DESTINO FOREIGN KEY (DESTINO_ID) REFERENCES dbo.destino (DESTINO_ID),
        CONSTRAINT FK_DESPACHO_OPERADOR_LOGISTICO FOREIGN KEY (OPERADOR_LOGISTICO_ID) REFERENCES dbo.operador_logistico (OPERADOR_LOGISTICO_ID),
        CONSTRAINT FK_DESPACHO_PUERTO_LLEGADA FOREIGN KEY (PUERTO_LLEGADA_ID) REFERENCES dbo.puerto_llegada (PUERTO_LLEGADA_ID),
        CONSTRAINT FK_DESPACHO_PRODUCTO FOREIGN KEY (PRODUCTO_ID) REFERENCES dbo.producto (PRODUCTO_ID),
        CONSTRAINT FK_DESPACHO_PRODUCTO_VARIEDAD FOREIGN KEY (PRODUCTO_ID, VARIEDAD_ID) REFERENCES dbo.variedad (PRODUCTO_ID, VARIEDAD_ID),
        CONSTRAINT FK_DESPACHO_VIA FOREIGN KEY (VIA_ID) REFERENCES dbo.via (VIA_ID),
        CONSTRAINT FK_DESPACHO_SITUACION FOREIGN KEY (SITUACION_ID) REFERENCES dbo.situacion (SITUACION_ID),
        CONSTRAINT CK_DESPACHO_CANTIDAD_POSITIVA CHECK (CANTIDAD > 0),
        CONSTRAINT CK_DESPACHO_FECHAS CHECK (FECHA_ESTIMADA_LLEGADA >= FECHA_DESPACHO)
    );
END;
GO

/*=============================================================================
  ÍNDICES
  Nota: SQL Server crea índices para PK y UNIQUE. Aquí solo se agregan índices
  adicionales útiles para búsquedas y claves foráneas.
=============================================================================*/

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_USUARIO_ACTIVO' AND object_id = OBJECT_ID(N'dbo.usuario'))
    CREATE INDEX IX_USUARIO_ACTIVO ON dbo.usuario (ACTIVO);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_USUARIO_ROL_ROL_ID' AND object_id = OBJECT_ID(N'dbo.usuario_rol'))
    CREATE INDEX IX_USUARIO_ROL_ROL_ID ON dbo.usuario_rol (ROL_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_ROL_PERMISO_MODULO_ID' AND object_id = OBJECT_ID(N'dbo.rol_permiso'))
    CREATE INDEX IX_ROL_PERMISO_MODULO_ID ON dbo.rol_permiso (MODULO_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_ROL_PERMISO_PERMISO_ID' AND object_id = OBJECT_ID(N'dbo.rol_permiso'))
    CREATE INDEX IX_ROL_PERMISO_PERMISO_ID ON dbo.rol_permiso (PERMISO_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_BITACORA_FECHA' AND object_id = OBJECT_ID(N'dbo.bitacora'))
    CREATE INDEX IX_BITACORA_FECHA ON dbo.bitacora (FECHA DESC);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_BITACORA_USUARIO_ID' AND object_id = OBJECT_ID(N'dbo.bitacora'))
    CREATE INDEX IX_BITACORA_USUARIO_ID ON dbo.bitacora (USUARIO_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_BITACORA_ENTIDAD_REGISTRO' AND object_id = OBJECT_ID(N'dbo.bitacora'))
    CREATE INDEX IX_BITACORA_ENTIDAD_REGISTRO ON dbo.bitacora (ENTIDAD, REGISTRO_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_VARIEDAD_PRODUCTO_ID' AND object_id = OBJECT_ID(N'dbo.variedad'))
    CREATE INDEX IX_VARIEDAD_PRODUCTO_ID ON dbo.variedad (PRODUCTO_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_DESPACHO_CLIENTE_ID' AND object_id = OBJECT_ID(N'dbo.despacho'))
    CREATE INDEX IX_DESPACHO_CLIENTE_ID ON dbo.despacho (CLIENTE_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_DESPACHO_NAVIERA_ID' AND object_id = OBJECT_ID(N'dbo.despacho'))
    CREATE INDEX IX_DESPACHO_NAVIERA_ID ON dbo.despacho (NAVIERA_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_DESPACHO_DESTINO_ID' AND object_id = OBJECT_ID(N'dbo.despacho'))
    CREATE INDEX IX_DESPACHO_DESTINO_ID ON dbo.despacho (DESTINO_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_DESPACHO_OPERADOR_LOGISTICO_ID' AND object_id = OBJECT_ID(N'dbo.despacho'))
    CREATE INDEX IX_DESPACHO_OPERADOR_LOGISTICO_ID ON dbo.despacho (OPERADOR_LOGISTICO_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_DESPACHO_PUERTO_LLEGADA_ID' AND object_id = OBJECT_ID(N'dbo.despacho'))
    CREATE INDEX IX_DESPACHO_PUERTO_LLEGADA_ID ON dbo.despacho (PUERTO_LLEGADA_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_DESPACHO_PRODUCTO_VARIEDAD' AND object_id = OBJECT_ID(N'dbo.despacho'))
    CREATE INDEX IX_DESPACHO_PRODUCTO_VARIEDAD ON dbo.despacho (PRODUCTO_ID, VARIEDAD_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_DESPACHO_VIA_ID' AND object_id = OBJECT_ID(N'dbo.despacho'))
    CREATE INDEX IX_DESPACHO_VIA_ID ON dbo.despacho (VIA_ID);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_DESPACHO_SITUACION_ID' AND object_id = OBJECT_ID(N'dbo.despacho'))
    CREATE INDEX IX_DESPACHO_SITUACION_ID ON dbo.despacho (SITUACION_ID);
GO

/*=============================================================================
  DATOS MAESTROS INICIALES (IDEMPOTENTES)
=============================================================================*/

INSERT INTO dbo.modulo (CODIGO, NOMBRE)
SELECT V.CODIGO, V.NOMBRE
FROM (VALUES
    ('roles',                 N'Roles'),
    ('usuarios',              N'Usuarios'),
    ('bitacora',              N'Bitácora'),
    ('perfil-usuario',        N'Perfil de usuario'),
    ('registro-despacho',     N'Registro de despacho'),
    ('reporte-despacho',      N'Reporte de despacho'),
    ('clientes',              N'Clientes'),
    ('navieras',              N'Navieras'),
    ('destinos',              N'Destinos'),
    ('operadores-logisticos', N'Operadores logísticos'),
    ('puertos-llegada',       N'Puertos de llegada'),
    ('productos',             N'Productos'),
    ('variedades',            N'Variedades'),
    ('vias',                  N'Vías'),
    ('situaciones',           N'Situaciones')
) AS V(CODIGO, NOMBRE)
WHERE NOT EXISTS
(
    SELECT 1
    FROM dbo.modulo M
    WHERE M.CODIGO = V.CODIGO
);
GO

INSERT INTO dbo.permiso (ACCION, DESCRIPCION)
SELECT V.ACCION, V.DESCRIPCION
FROM (VALUES
    ('consultar', N'Consultar'),
    ('crear',     N'Crear'),
    ('editar',    N'Editar'),
    ('eliminar',  N'Eliminar'),
    ('exportar',  N'Exportar')
) AS V(ACCION, DESCRIPCION)
WHERE NOT EXISTS
(
    SELECT 1
    FROM dbo.permiso P
    WHERE P.ACCION = V.ACCION
);
GO

INSERT INTO dbo.rol (NOMBRE, DESCRIPCION, ES_SISTEMA, ACTIVO)
SELECT N'Administrador', N'Administrador del sistema', 1, 1
WHERE NOT EXISTS
(
    SELECT 1
    FROM dbo.rol
    WHERE NOMBRE = N'Administrador'
);
GO

INSERT INTO dbo.usuario
(
    DNI,
    USUARIO,
    NOMBRES,
    APELLIDO_PATERNO,
    APELLIDO_MATERNO,
    CORREO,
    TELEFONO,
    CONTRASENIA,
    ES_SISTEMA,
    RESET_CONTRASENIA,
    ACTIVO
)
SELECT
    '71477205',
    '71477205',
    N'Carlos Jesus',
    N'Chinchay',
    N'Pecho',
    'carlos@yopmail.com',
    '982697870',
    '$2a$10$Efa0BQyjCWDzNwwTwzcs6.6A2sceUD4fDCZXqDuDYbAjmX4HXlNjK',
    1,
    0,
    1
WHERE NOT EXISTS
(
    SELECT 1
    FROM dbo.usuario
    WHERE DNI = '71477205'
       OR USUARIO = '71477205'
       OR CORREO = 'carlos@yopmail.com'
);
GO

INSERT INTO dbo.usuario_rol (USUARIO_ID, ROL_ID, ACTIVO)
SELECT U.USUARIO_ID, R.ROL_ID, 1
FROM dbo.usuario U
INNER JOIN dbo.rol R ON R.NOMBRE = N'Administrador'
WHERE U.USUARIO = '71477205'
  AND NOT EXISTS
  (
      SELECT 1
      FROM dbo.usuario_rol UR
      WHERE UR.USUARIO_ID = U.USUARIO_ID
        AND UR.ROL_ID = R.ROL_ID
  );
GO

/* Permisos iniciales del Administrador del sistema (idempotente) */
INSERT INTO dbo.rol_permiso (ROL_ID, MODULO_ID, PERMISO_ID, ACTIVO)
SELECT R.ROL_ID, M.MODULO_ID, P.PERMISO_ID, 1
FROM dbo.rol R
CROSS JOIN dbo.modulo M
CROSS JOIN dbo.permiso P
WHERE R.NOMBRE = N'Administrador'
  AND NOT EXISTS
  (
      SELECT 1
      FROM dbo.rol_permiso RP
      WHERE RP.ROL_ID = R.ROL_ID
        AND RP.MODULO_ID = M.MODULO_ID
        AND RP.PERMISO_ID = P.PERMISO_ID
  );
GO


