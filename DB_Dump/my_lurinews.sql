--
-- Database: `my_lurinews`
--

-- --------------------------------------------------------
--
-- Struttura della tabella `Utente`
--

CREATE TABLE IF NOT EXISTS `Utente` (
  `IDUtente` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `dataRegistrazione` date NOT NULL,
  PRIMARY KEY (`IDUtente`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Struttura della tabella `Notizia`
--

CREATE TABLE IF NOT EXISTS `Notizia` (
  `IDNotizia` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `titoloPrimo` text NOT NULL,
  `testoPrimo` text NOT NULL,
  `testoSecondo` text NOT NULL,
  `dataPubblicato` datetime NOT NULL,
  `IDUtente` varchar(255) NOT NULL,
  `numeroYes` int(11) NOT NULL,
  `numeroNo` int(11) NOT NULL,
  PRIMARY KEY (`IDNotizia`),
  foreign key(IDUtente) references Utente(IDUtente) on update cascade on delete cascade
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=44 ;

--
-- Struttura della tabella `Commento`
--

CREATE TABLE IF NOT EXISTS `Commento` (
  `IDCommento` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `IDUtente` varchar(255) NOT NULL,
  `IDNotizia` int(10) unsigned NOT NULL,
  `testo` text NOT NULL,
  `dataCommento` datetime NOT NULL,
  PRIMARY KEY (`IDCommento`),
  foreign key(IDUtente) references Utente(IDUtente) on update cascade on delete cascade,
  foreign key(IDNotizia) ref erences Notizia(IDNotizia) on update cascade on delete cascade
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;


--
-- Struttura della tabella `Interessante`
--

CREATE TABLE IF NOT EXISTS `Interessante` (
  `IDNotizia` int(10) unsigned NOT NULL,
  `IDUtente` varchar(255) NOT NULL,
  PRIMARY KEY (`IDNotizia`,`IDUtente`),
  foreign key(IDUtente) references Utente(IDUtente) on update cascade on delete cascade
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


--
-- Struttura della tabella `Valutazione`
--

CREATE TABLE IF NOT EXISTS `Valutazione` (
  `IDNotizia` int(10) unsigned NOT NULL,
  `IDUtente` varchar(255) NOT NULL,
  `YesNo` enum('Yes','No') NOT NULL,
  PRIMARY KEY (`IDUtente`,`IDNotizia`),
  foreign key(IDNotizia) references Notizia(IDNotizia) on update cascade on delete cascade
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

