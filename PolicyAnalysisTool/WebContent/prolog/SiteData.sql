-- phpMyAdmin SQL Dump
-- version 2.6.0-pl2
-- http://www.phpmyadmin.net
-- 
-- Σύστημα: localhost
-- Χρόνος δημιουργίας: 19 Απρ 2006, στις 05:29 PM
-- Έκδοση Διακομιστή: 4.0.18
-- Έκδοση PHP: 5.1.2
-- 
-- Βάση: `SiteData`
-- 

-- --------------------------------------------------------

-- 
-- Δομή Πίνακα για τον Πίνακα `swc_organisations`
-- 

CREATE TABLE `swc_organisations` (
  `oid` int(11) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL default '',
  `description` longtext,
  `address` varchar(255) default NULL,
  `homepage` varchar(255) default NULL,
  `type` varchar(255) NOT NULL default '',
  `part_of` int(11) default NULL,
  PRIMARY KEY  (`oid`),
  KEY `title` (`title`)
) TYPE=MyISAM AUTO_INCREMENT=65 ;

-- 
-- 'Αδειασμα δεδομένων του πίνακα `swc_organisations`
-- 

INSERT INTO `swc_organisations` VALUES (1, 'IEEE', NULL, 'US', 'http://www.computer.org/', 'International Engineering Association', NULL);
INSERT INTO `swc_organisations` VALUES (2, 'BBN Technologies / Verizon', NULL, 'BBN Technologies / Verizon 1827 S. Liberty Dr. Liberty Lake, WA 99019', 'http://www.bbn.com', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (3, 'World Wide Web Consortium', NULL, '', 'http://www.w3.org', 'Consortium', NULL);
INSERT INTO `swc_organisations` VALUES (4, 'Institute for Learning and Research Technology University of Bristol', NULL, '8-10 Berkeley Square Bristol BS8 1HH', 'http://www.ilrt.bristol.ac.uk', 'Institute', NULL);
INSERT INTO `swc_organisations` VALUES (5, 'HP Labs', NULL, 'Bristol, UK', 'http://www.hpl.hp.com/', 'Company Unit - Lab', NULL);
INSERT INTO `swc_organisations` VALUES (6, 'Rensselaer Polytechnic Institute', NULL, '110 8th Street Troy, NY 12180-3590 USA', 'http://www.cs.rpi.edu/~puninj/rdfeditor/', 'Polytechnic Institute', NULL);
INSERT INTO `swc_organisations` VALUES (7, 'Mondeca', NULL, '', 'www.mondeca.com', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (8, 'Netscape', NULL, '', '', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (9, 'University of Trento', NULL, 'Italy', '', 'University', NULL);
INSERT INTO `swc_organisations` VALUES (10, 'DERI Innsbruck', NULL, 'Innsbruck, Austria', 'http://www.deri.at/', 'Research Institute - University affiliated', NULL);
INSERT INTO `swc_organisations` VALUES (11, 'Institute of Computer Science,FORTH', NULL, 'Chania, Greece', 'http://www.ics.forth.gr', 'Institute', NULL);
INSERT INTO `swc_organisations` VALUES (12, 'Institute AIFB University of Karlsruhe (TH)', NULL, '', 'http://www.aifb.uni-karlsruhe.de', 'Institute', NULL);
INSERT INTO `swc_organisations` VALUES (13, 'DFKI: German Research center for Artificial Intelligence Gmbh', NULL, 'DFKI GmbH Erwin-Schrodinger-Strasse D-67608 Kaiserslautern', 'http://www.dfki.uni-kl.de/', '', NULL);
INSERT INTO `swc_organisations` VALUES (14, 'Institute of Informatics, Innsbruck Unvicersity', NULL, 'Innsburg, Austria', 'http://informatik.uibk.ac.at', 'Research institute, University affiliated', NULL);
INSERT INTO `swc_organisations` VALUES (15, 'Free University of Brussels - STARLab', NULL, 'Brussels, Belgium', 'http://www.starlab.vub.ac.be', 'University Lab', NULL);
INSERT INTO `swc_organisations` VALUES (16, 'INRIA Alpes', NULL, 'Inria, France', 'http://www.inrialpes.fr', 'Research Institute - W3C Europe', NULL);
INSERT INTO `swc_organisations` VALUES (17, 'UPM, Polytechnic University of Madrid', NULL, 'Madrid, Spain', 'http://www.upm.es/', 'University', NULL);
INSERT INTO `swc_organisations` VALUES (18, 'Stanford Medical Informatics (SMI)', NULL, '', 'http://www.smi.stanford.edu/', 'Research Group', NULL);
INSERT INTO `swc_organisations` VALUES (19, 'Intellidimension', NULL, '', 'http://www.intellidimension.com', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (20, 'CYCORP', NULL, '', 'http://www.cyc.com/', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (21, 'France Telecom', NULL, '', '', '', NULL);
INSERT INTO `swc_organisations` VALUES (22, 'Ontoprise', NULL, '', 'http://www.ontoprise.de/', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (23, 'Semantic Web Science Association', NULL, 'Karlsruhe, Germany', 'http://www.iswsa.org/', 'Non-profit Organisation, Scientific Association', NULL);
INSERT INTO `swc_organisations` VALUES (24, 'National University of Ireland Galway - Digital Enterprise Research Institute (DERI),', NULL, 'Galway, Ireland', 'www.deri.ie', 'Research Institute, University affiliated', NULL);
INSERT INTO `swc_organisations` VALUES (25, 'OntoText (SiRMA)', NULL, 'Sofia, Bulgaria', 'http://www.ontotext.com/', 'R&D Lab, within Sirma AI Ltd.', NULL);
INSERT INTO `swc_organisations` VALUES (26, 'University of Sheffield', NULL, 'UK', 'http://www.shef.ac.uk/', 'University', NULL);
INSERT INTO `swc_organisations` VALUES (27, 'Profium', NULL, '', 'http://www.profium.com/', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (28, 'DAPRA', NULL, '', '', 'Research agency (Defence)', NULL);
INSERT INTO `swc_organisations` VALUES (29, 'Univercity of Manchester', NULL, '', '', 'Univercity', NULL);
INSERT INTO `swc_organisations` VALUES (30, 'Network Inference', NULL, '', 'http://www.networkinference.com/', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (31, 'Free university of Amsterdam', NULL, '', 'http://www.vu.nl/', 'University', NULL);
INSERT INTO `swc_organisations` VALUES (32, 'DATI Software Group', NULL, 'Latvia', '', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (33, 'University of Essen', NULL, 'Gernamy', '', '', NULL);
INSERT INTO `swc_organisations` VALUES (34, 'FIPA: Foundation for Intelligent Physical Agents', NULL, '', 'http://www.fipa.org/', 'Non-profit organisation aimed at producing standards for the interoperation of heterogeneous software agents.', NULL);
INSERT INTO `swc_organisations` VALUES (35, 'The Japanese Society for Artificial Intelligence', NULL, 'Japan', 'http://www.ai-gakkai.or.jp/jsai/english.html', 'Scientific Association - Society', NULL);
INSERT INTO `swc_organisations` VALUES (36, 'Web Intelligence Consortium', NULL, '', 'http://wi-consortium.org/', 'Scientific Association - Consortium', NULL);
INSERT INTO `swc_organisations` VALUES (37, 'Stanford university database group', NULL, '', 'http://www-db.stanford.edu', 'University lab', NULL);
INSERT INTO `swc_organisations` VALUES (38, 'FZI: Research center for Information technology', NULL, 'Karsruhe, Germany', 'http://www.fzi.de', 'Research Institute, University affiliated', NULL);
INSERT INTO `swc_organisations` VALUES (39, 'Centre for Research and Technology Hellas - Institute of Telecommunications and Informatics', NULL, 'Thessaloniki, Greece', 'www.iti.gr', 'Research Institute', NULL);
INSERT INTO `swc_organisations` VALUES (40, 'Teknowledge Corporation', NULL, '', 'http://reliant.teknowledge.com', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (41, 'ACM', NULL, 'US', 'www.acm.org', 'International Computing Professionals Association', NULL);
INSERT INTO `swc_organisations` VALUES (42, 'National technical University of Athens (NTUA). Dept. of Electrical Engineering', NULL, '', 'www.ntua.gr', 'University', NULL);
INSERT INTO `swc_organisations` VALUES (43, 'British Telecommunications plc.', NULL, 'UK', 'http://www.bt.com/', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (44, 'University of Munich, Institute of Informatics', NULL, 'Muncih, Germany', 'http://www.pms.ifi.lmu.de/', 'University', NULL);
INSERT INTO `swc_organisations` VALUES (45, 'Artificial Intelligence Center at SRI International', NULL, 'CA, USA', 'http://www.ai.sri.com', 'Research Center, Non Profit Organisation', NULL);
INSERT INTO `swc_organisations` VALUES (46, 'Dublin Core Metadata Initiative', NULL, '', 'http://www.dublincore.org/', 'Research Forum', NULL);
INSERT INTO `swc_organisations` VALUES (47, 'Online Computer Library Center, Inc', NULL, '', 'http://www.oclc.org', 'Non Profit Membership Organisation', NULL);
INSERT INTO `swc_organisations` VALUES (48, 'Aidministratror Nederlands BV', NULL, '', 'http://sesame.aidministrator.nl/', 'Compnay', NULL);
INSERT INTO `swc_organisations` VALUES (49, 'Knowledge Management Group - IBM Almaden', NULL, '', 'http://www.almaden.ibm.com/software/km/index.shtml', '', NULL);
INSERT INTO `swc_organisations` VALUES (50, 'Knowledge Systems Laboratory Stanford University', NULL, '', 'http://www.ksl.stanford.edu/', 'University Lab', NULL);
INSERT INTO `swc_organisations` VALUES (51, 'Semantic World', NULL, '', 'www.semanticworl.org', 'Community - Portal', NULL);
INSERT INTO `swc_organisations` VALUES (52, '', NULL, '', '', '', NULL);
INSERT INTO `swc_organisations` VALUES (53, 'Fourthought Inc', NULL, '', 'http://fourthought.com/', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (54, 'Stanford University,. Computer Sci, Dept.', NULL, 'Stanford CA', 'http://www-db.stanford.edu', 'University', NULL);
INSERT INTO `swc_organisations` VALUES (55, 'Empolis', NULL, '', 'http://www.semanticwebserver.com/', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (56, 'European Coordinating Committee for Artificial Intelligence', NULL, '', 'http://www.eccai.org/', 'International Committee', NULL);
INSERT INTO `swc_organisations` VALUES (57, 'Versatile Information Systems Inc', NULL, '', 'http://vis.home.mindspring.com/', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (58, 'University of Maryuland', NULL, '', '', '', NULL);
INSERT INTO `swc_organisations` VALUES (59, 'ISOCO (Intelligent Software Components S. A)', NULL, 'Spain', 'http://www.isoco.com/en/', 'Company', NULL);
INSERT INTO `swc_organisations` VALUES (60, 'AT&T Government Solutions Advanced Systems Group', NULL, 'Virginia', 'http://www.grci.com/main.htm', 'Company Unit', NULL);
INSERT INTO `swc_organisations` VALUES (61, 'Intelligent Time-Critical Systems Laboratory,Drexel University', NULL, 'Drexel University', 'http://plan.mcs.drexel.edu/', 'University', NULL);
INSERT INTO `swc_organisations` VALUES (62, 'Information Sciences Institute University of Southern California', NULL, '', 'www.isi.edu', 'Research Institute', NULL);
INSERT INTO `swc_organisations` VALUES (63, 'Distributed systems technology center', NULL, 'Australia', 'http://www.dstc.edu.au/', 'Research Organisation', NULL);
INSERT INTO `swc_organisations` VALUES (64, 'Mike Dean', NULL, '', '', '', NULL);

-- --------------------------------------------------------

-- 
-- Δομή Πίνακα για τον Πίνακα `swc_researchers`
-- 

CREATE TABLE `swc_researchers` (
  `rid` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `homepage` varchar(255) default NULL,
  PRIMARY KEY  (`rid`),
  KEY `name` (`name`)
) TYPE=MyISAM AUTO_INCREMENT=56 ;

-- 
-- 'Αδειασμα δεδομένων του πίνακα `swc_researchers`
-- 

INSERT INTO `swc_researchers` VALUES (2, 'Mike Dean', 'http://www.daml.org/people/mdean/');
INSERT INTO `swc_researchers` VALUES (3, 'Kelly Barber', '');
INSERT INTO `swc_researchers` VALUES (4, 'John Punin', '');
INSERT INTO `swc_researchers` VALUES (5, 'Chris Waterson', 'waterson@netscape.com');
INSERT INTO `swc_researchers` VALUES (6, 'David Hyatt', 'hyatt@netscape.com');
INSERT INTO `swc_researchers` VALUES (7, 'Robert Churchill', '(rjc@netscape.com');
INSERT INTO `swc_researchers` VALUES (8, 'Dan Brickley', '');
INSERT INTO `swc_researchers` VALUES (9, 'Grigoris Antoniou', '');
INSERT INTO `swc_researchers` VALUES (10, 'Siegfried Handschuh', '');
INSERT INTO `swc_researchers` VALUES (11, 'R.V. Guha', '');
INSERT INTO `swc_researchers` VALUES (12, 'York Sure', '');
INSERT INTO `swc_researchers` VALUES (13, 'Brian McBride', '');
INSERT INTO `swc_researchers` VALUES (14, 'John Flynn', '');
INSERT INTO `swc_researchers` VALUES (15, 'Chris Bussler', 'http://hometown.aol.com/chbussler/');
INSERT INTO `swc_researchers` VALUES (16, 'Hamish Cunningham', 'http://www.dcs.shef.ac.uk/~hamish/');
INSERT INTO `swc_researchers` VALUES (17, 'Michael Groove', 'mhgrove@hotmail.com');
INSERT INTO `swc_researchers` VALUES (18, 'Daniel Krech', 'http://eikeon.com/');
INSERT INTO `swc_researchers` VALUES (19, 'Sean Bechhofer', '');
INSERT INTO `swc_researchers` VALUES (20, 'Gary Ng', '');
INSERT INTO `swc_researchers` VALUES (21, 'Kalvis Apsitis', '');
INSERT INTO `swc_researchers` VALUES (22, 'Reinhold Klapsing', '');
INSERT INTO `swc_researchers` VALUES (23, 'Wolfram Conen/Xonar', '');
INSERT INTO `swc_researchers` VALUES (24, 'Vassilis Christophides', 'christop@ics.forth.gr');
INSERT INTO `swc_researchers` VALUES (25, 'Stefan Decker', '');
INSERT INTO `swc_researchers` VALUES (26, 'Michael Sintek', '');
INSERT INTO `swc_researchers` VALUES (27, 'Luis Argerich Rogerio', '');
INSERT INTO `swc_researchers` VALUES (28, 'Paolo Bouquet', '');
INSERT INTO `swc_researchers` VALUES (29, 'Jeremy Carroll', '');
INSERT INTO `swc_researchers` VALUES (30, 'Dave Reynolds', '');
INSERT INTO `swc_researchers` VALUES (31, 'Andy Seaborne', '');
INSERT INTO `swc_researchers` VALUES (32, 'Ian Dickinson', '');
INSERT INTO `swc_researchers` VALUES (33, 'Dave Ragger', '');
INSERT INTO `swc_researchers` VALUES (34, 'Harold Boley', '');
INSERT INTO `swc_researchers` VALUES (35, 'Sandro Hawke', 'http://www.w3.org/People/Sandro');
INSERT INTO `swc_researchers` VALUES (36, 'Adam Pease', '');
INSERT INTO `swc_researchers` VALUES (37, 'Jason Diamond', '');
INSERT INTO `swc_researchers` VALUES (38, 'Atanas Kiryakov', '');
INSERT INTO `swc_researchers` VALUES (39, 'David Megginson', 'http://www.megginson.com');
INSERT INTO `swc_researchers` VALUES (40, 'Dave Beckett', 'http://www.ilrt.bristol.ac.uk/people/cmdjb/');
INSERT INTO `swc_researchers` VALUES (41, 'Jan Winkler', '');
INSERT INTO `swc_researchers` VALUES (42, 'Ying Ding', 'http://www.cs.vu.nl/~ying/');
INSERT INTO `swc_researchers` VALUES (43, 'Emmanuel Pietriga', '');
INSERT INTO `swc_researchers` VALUES (44, 'Libby Miller', '');
INSERT INTO `swc_researchers` VALUES (45, 'Greg Carvounarakis', '');
INSERT INTO `swc_researchers` VALUES (46, 'Dimitris Plexousakis', 'dp@ics.forth.gr');
INSERT INTO `swc_researchers` VALUES (47, 'Art Barstow.', 'http://www.w3.org/People/Barstow/');
INSERT INTO `swc_researchers` VALUES (48, 'Sergey Melnik', 'http://www-db.stanford.edu/~melnik/');
INSERT INTO `swc_researchers` VALUES (49, 'Dieter Fensel (Prof.)', 'dieter.fensel@uibk.ac.at');
INSERT INTO `swc_researchers` VALUES (50, 'Sean Palmer', 'http://infomesh.net/sbp/');
INSERT INTO `swc_researchers` VALUES (51, 'Aditya Kalyanpur', '');
INSERT INTO `swc_researchers` VALUES (52, 'David Martin', '');
INSERT INTO `swc_researchers` VALUES (53, 'Joe Kopena', 'joe@plan.mcs.drexel.edu');
INSERT INTO `swc_researchers` VALUES (54, 'Mike Olson', '');
INSERT INTO `swc_researchers` VALUES (55, 'Uche Ogbuji', '');

-- --------------------------------------------------------

-- 
-- Δομή Πίνακα για τον Πίνακα `swc_researchers_organisations`
-- 

CREATE TABLE `swc_researchers_organisations` (
  `rid` int(11) NOT NULL default '0',
  `oid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`rid`,`oid`)
) TYPE=MyISAM COMMENT='N-to-N Researchers work for Organisations';

-- 
-- 'Αδειασμα δεδομένων του πίνακα `swc_researchers_organisations`
-- 

INSERT INTO `swc_researchers_organisations` VALUES (2, 2);
INSERT INTO `swc_researchers_organisations` VALUES (3, 2);
INSERT INTO `swc_researchers_organisations` VALUES (4, 6);
INSERT INTO `swc_researchers_organisations` VALUES (5, 8);
INSERT INTO `swc_researchers_organisations` VALUES (6, 8);
INSERT INTO `swc_researchers_organisations` VALUES (7, 8);
INSERT INTO `swc_researchers_organisations` VALUES (9, 11);
INSERT INTO `swc_researchers_organisations` VALUES (10, 12);
INSERT INTO `swc_researchers_organisations` VALUES (12, 12);
INSERT INTO `swc_researchers_organisations` VALUES (13, 5);
INSERT INTO `swc_researchers_organisations` VALUES (14, 2);
INSERT INTO `swc_researchers_organisations` VALUES (15, 24);
INSERT INTO `swc_researchers_organisations` VALUES (16, 26);
INSERT INTO `swc_researchers_organisations` VALUES (18, 29);
INSERT INTO `swc_researchers_organisations` VALUES (20, 30);
INSERT INTO `swc_researchers_organisations` VALUES (21, 32);
INSERT INTO `swc_researchers_organisations` VALUES (22, 33);
INSERT INTO `swc_researchers_organisations` VALUES (23, 33);
INSERT INTO `swc_researchers_organisations` VALUES (24, 11);
INSERT INTO `swc_researchers_organisations` VALUES (25, 37);
INSERT INTO `swc_researchers_organisations` VALUES (26, 13);
INSERT INTO `swc_researchers_organisations` VALUES (28, 9);
INSERT INTO `swc_researchers_organisations` VALUES (29, 5);
INSERT INTO `swc_researchers_organisations` VALUES (30, 5);
INSERT INTO `swc_researchers_organisations` VALUES (31, 5);
INSERT INTO `swc_researchers_organisations` VALUES (32, 5);
INSERT INTO `swc_researchers_organisations` VALUES (33, 2);
INSERT INTO `swc_researchers_organisations` VALUES (35, 3);
INSERT INTO `swc_researchers_organisations` VALUES (36, 40);
INSERT INTO `swc_researchers_organisations` VALUES (38, 25);
INSERT INTO `swc_researchers_organisations` VALUES (40, 4);
INSERT INTO `swc_researchers_organisations` VALUES (42, 31);
INSERT INTO `swc_researchers_organisations` VALUES (43, 3);
INSERT INTO `swc_researchers_organisations` VALUES (44, 4);
INSERT INTO `swc_researchers_organisations` VALUES (45, 11);
INSERT INTO `swc_researchers_organisations` VALUES (46, 11);
INSERT INTO `swc_researchers_organisations` VALUES (47, 3);
INSERT INTO `swc_researchers_organisations` VALUES (48, 54);
INSERT INTO `swc_researchers_organisations` VALUES (49, 14);
INSERT INTO `swc_researchers_organisations` VALUES (51, 58);
INSERT INTO `swc_researchers_organisations` VALUES (52, 45);
INSERT INTO `swc_researchers_organisations` VALUES (53, 61);
INSERT INTO `swc_researchers_organisations` VALUES (55, 53);
