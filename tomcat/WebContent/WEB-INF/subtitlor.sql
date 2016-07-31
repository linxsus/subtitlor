-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Sam 23 Avril 2016 à 16:09
-- Version du serveur :  5.7.9
-- Version de PHP :  5.6.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Base de données :  `subtitlor`
--
CREATE DATABASE IF NOT EXISTS `subtitlor` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `subtitlor`;

-- --------------------------------------------------------

--
-- Structure de la table `original`
--

DROP TABLE IF EXISTS `original`;
CREATE TABLE IF NOT EXISTS `original` (
  `numLigne` int(11) NOT NULL,
  `text` varchar(100) NOT NULL,
  KEY `numLigne` (`numLigne`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `times`
--

DROP TABLE IF EXISTS `times`;
CREATE TABLE IF NOT EXISTS `times` (
  `numLigne` int(11) NOT NULL,
  `time` varchar(40) NOT NULL,
  KEY `num` (`numLigne`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `traduit`
--

DROP TABLE IF EXISTS `traduit`;
CREATE TABLE IF NOT EXISTS `traduit` (
  `numLigne` int(11) NOT NULL,
  `text` varchar(100) NOT NULL,
  KEY `numLigne` (`numLigne`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;