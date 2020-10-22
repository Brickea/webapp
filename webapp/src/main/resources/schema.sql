DROP TABLE IF EXISTS `Answer`;
CREATE TABLE `Answer` (
  `answerId` char(50) NOT NULL,
  `createdTimestamp` varchar(45) NOT NULL,
  `updatedTimestamp` varchar(45) NOT NULL,
  `userId` char(50) NOT NULL,
  `answerText` varchar(45) NOT NULL,
  PRIMARY KEY (`answerId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `Category`;
CREATE TABLE `Category` (
  `categoryId` char(50) NOT NULL,
  `category` varchar(45) NOT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `Question`;
CREATE TABLE `Question` (
  `questionId` char(50) NOT NULL,
  `createdTimestamp` varchar(45) NOT NULL,
  `updatedTimestamp` varchar(45) NOT NULL,
  `userId` char(50) NOT NULL,
  `questionText` varchar(45) NOT NULL,
  PRIMARY KEY (`questionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `QuestionAnswer`;
CREATE TABLE `QuestionAnswer` (
  `questionId` char(50) NOT NULL,
  `answerId` char(50) NOT NULL,
  PRIMARY KEY (`answerId`,`questionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `QuestionCategory`;
CREATE TABLE `QuestionCategory` (
  `questionId` char(50) NOT NULL,
  `categoryId` char(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `id` char(50) NOT NULL,
  `userName` varchar(45) NOT NULL,
  `password` char(60) NOT NULL,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `accountCreated` varchar(45) NOT NULL,
  `accountUpdated` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `emailAddress_UNIQUE` (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `AnswerFile`;
CREATE TABLE `AnswerFile` (
  `answerId` char(50) NOT NULL,
  `fileId` varchar(45) NOT NULL,
  PRIMARY KEY (`answerId`,`fileId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `QuestionFile`;
CREATE TABLE `QuestionFile` (
  `questionId` char(50) NOT NULL,
  `fileId` char(50) NOT NULL,
  PRIMARY KEY (`questionId`,`fileId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `WebappFile`;
CREATE TABLE `WebappFile` (
  `fileId` char(50) NOT NULL,
  `fileName` varchar(45) NOT NULL,
  `s3ObjectName` varchar(255) NOT NULL,
  `createdTimestamp` varchar(45) NOT NULL,
  PRIMARY KEY (`fileId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
