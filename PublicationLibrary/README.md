
# Publication Library

The issue is the creation of a system to control data about academic publications, with a concentration on journal and conference publications. The system should be able to handle various levels of granularity in research fields and allow for the storage and retrieval of data regarding publications, authors, venues, research topics, and citations.

The set of authors, article title, journal name, page range, volume and issue numbers, publication month and year are the bare minimum details needed for a journal publishing. The required details for a conference publication are the group of authors, paper title, conference name, venue and year, and page range. Each venue for publication has a group that oversees the publication, a field of study it focuses on, and an editor or organizer with their contact information.

A class called "PublicationLibrary" that can store and manage data on publications, authors, venues, study fields, and citations should be included in the system's architecture. However, for this project, the class should only accept the IEEE citation format. The class should be capable of handling various publications and citation formats. Information can be stored by the class via internal data structures, databases, or files.

Overall, the system should provide a comprehensive and organized way to manage and analyze publication information in academia, making it easier for researchers to track their own work and that of others, as well as identify potential collaborators and seminal papers in their field.

## Application setup

#### Github Repository
https://git.cs.dal.ca/courses/2023-winter/csci-3901/project/kpkhant.git

#### SQL setup
It has a publication_library_db.sql file. Which is the structured database file with few pre-filled data. Import that SQL file into whatever the SQL UI interface.

#### Credentials.properties file
Setup your credentials according to your database connection into that file.

#### Main.java file
It will be the main calling file and will handle all method calls.

## Flow of the application
    1. Add research areas
    2. Add publisher
    3. Add Venue
    4. Add Publication
    5. Add references for the publication


