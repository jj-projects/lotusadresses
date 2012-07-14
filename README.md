# NotesAdress2SQLite

## Description

This java application fetches basic date from a Lotus Notes Address Database and creates a new SQLite Database containig this data.<br>
Additionally it will export a PHP page containing the recent database file and version information 
to enable an easy download of the database file.<BR>
This data can be used by a iPhone app called "DHaddresses" written by Bart-Jan Schuman.

See the [Wiki](wiki) for more information.

## Ant Build

On project level execute:
	ant -buildfile build/build.xml -propertyfile build/build_NotesAdress2SQLite.properties run
	

## Usage

java -classpath NotesAdress2SQLite.jar de.jjprojects.backoffice.NotesAdress2SQLite NotesAdress2SQLite.properties -Djava.util.logging.config.file=NotesAdress2SQLite_logging.properties


## Author

Joerg Juenger ( jj-projects, joerg@jj-projects.de ), JJ-Projects Joerg Juenger

## Licenses

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
