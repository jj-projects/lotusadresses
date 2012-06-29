# NotesAdress2SQLite

## Description

This java application fetches basic date from a Lotus Notes Address Database and creates a new SQLite Database containig this data.
This data can be used by a iPhone app called "xxxxx" written by Bart-Jan Schuman.

## Ant Build

On project level execute:
	ant -buildfile build/build.xml -propertyfile build/build_NotesAdress2SQLite.properties run
	

## Usage

java -classpath NotesAdress2SQLite.jar de.jjprojects.backoffice.NotesAdress2SQLite NotesAdress2SQLite.properties -Djava.util.logging.config.file=NotesAdress2SQLite_logging.properties


## Author

Joerg Juenger ( jjuenger, joerg@jj-projects.de ), JJ-Projects Joerg Juenger

