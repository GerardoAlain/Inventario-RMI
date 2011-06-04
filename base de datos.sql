# phpMyAdmin SQL Dump
# version 2.5.3-rc2
# http://www.phpmyadmin.net
#
# Servidor: localhost
# Tiempo de generación: 30-11-2004 a las 21:59:48
# Versión del servidor: 3.23.57
# Versión de PHP: 4.3.3
# 
# Base de datos : `inventario`
# 

# --------------------------------------------------------

#
# Estructura de tabla para la tabla `stock`
#
# Creación: 30-11-2004 a las 11:14:54
# Última actualización: 30-11-2004 a las 11:15:10
# Última revisión: 30-11-2004 a las 11:15:17
#

CREATE TABLE `stock` (
  `id` smallint(6) NOT NULL auto_increment,
  `Descripcion` tinytext NOT NULL,
  `CantidadDisponible` int(11) NOT NULL default '0',
  `CantidadMinima` int(11) NOT NULL default '0',
  `PrecioVenta` int(11) NOT NULL default '0',
  `Proveedor` tinytext NOT NULL,
  `Observaciones` text,
  `UsuarioModifico` tinytext NOT NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM PACK_KEYS=0 COMMENT='Lista de los productos existentes en el Inventario' AUTO_INCREMENT=17 ;

# --------------------------------------------------------

#
# Estructura de tabla para la tabla `usuarios`
#
# Creación: 19-11-2004 a las 16:15:52
# Última actualización: 30-11-2004 a las 10:04:08
# Última revisión: 29-11-2004 a las 15:37:32
#

CREATE TABLE `usuarios` (
  `IdentificadorRemoto` varchar(4) NOT NULL default '0',
  `Usuario` varchar(8) NOT NULL default '',
  `Password` varchar(8) NOT NULL default '',
  `Nombre` tinytext NOT NULL,
  `UltimoAcceso` tinytext NOT NULL,
  PRIMARY KEY  (`IdentificadorRemoto`)
) TYPE=MyISAM COMMENT='Lista de los usuarios permitidos por la aplicacion';