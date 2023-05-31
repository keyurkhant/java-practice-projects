package repositories;

import Utils.Utils;
import services.*;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PublicationRepository {

    Connection connection;
    VenueRepository venueRepository = new VenueRepository();
    public PublicationRepository() {
        connection = Database.getInstance().connectDatabase();
    }

    /**
     * Retrieves the publication from the database that matches the given identifier.
     * @param identifier the identifier of the publication to retrieve
     * @return a ResultSet object containing the publication information, or null if no publication was found
     * */
    public ResultSet getPublicationByIdentifier(String identifier) {
        try {
            if(connection != null) {
                String query = "SELECT * FROM Publications WHERE identifier = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, identifier);

                return preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Adds a new publication to the database.
     *  @param publication the publication object to be added.
     *  @return true if the publication is added successfully, false otherwise.
     * */
    public boolean addPublication(Publication publication) {
        try {
            if(connection != null) {
                ResultSet venue = venueRepository.getVenueByName(publication.getPublicationTitle());
                if(venue == null) return false;

                String query = "INSERT INTO Publications VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, publication.getIdentifier());
                preparedStatement.setString(2, publication.getPaperTitle());
                preparedStatement.setString(3, publication.getPublicationTitle());
                preparedStatement.setString(4, venue.getString("venue_id"));
                preparedStatement.setString(5, publication.getPages());
                preparedStatement.setString(6, publication.getVolume());
                preparedStatement.setString(7, publication.getIssue());
                preparedStatement.setString(8, publication.getMonth());
                preparedStatement.setString(9, publication.getYear());
                preparedStatement.setString(10, publication.getLocation());

                int affectedRows = preparedStatement.executeUpdate();
                int publicationId = -1;
                addAuthors(publication.getAuthors());
                if(affectedRows > 0) {
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if(resultSet.next()) {
                        publicationId = resultSet.getInt(1);
                    }
                }
                if(publicationId != -1) {
                    publicationAuthorsMapping(publicationId, publication.getAuthors());
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Adds the given set of authors to the database if they do not already exist.
     * @param authors a set of author names to be added to the database
     * */
    public void addAuthors(Set<String> authors) {
        try {
            if(connection != null) {
                String query = "INSERT INTO Authors VALUES (NULL, ?)";
                for(String author: authors) {
                    Author authorData = getAuthorByName(author);
                    if(authorData == null) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, author);
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * Maps the given set of author names to the given publication ID in the database.
     * @param publicationId the ID of the publication to map the authors to
     * @param authors the set of author names to map to the publication
     * */
    private void publicationAuthorsMapping(int publicationId, Set<String> authors) {
        try {
            if(connection != null) {
                String query = "INSERT INTO PublicationAuthors VALUES (?, ?)";
                for(String author: authors) {
                    Author authorData = getAuthorByName(author);
                    if(authorData == null) continue;

                    int authorId = authorData.getAuthorId();
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, publicationId);
                    preparedStatement.setInt(2, authorId);

                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves an Author object from the database based on the given author name.
     * If the author is not found in the database, it returns null.
     * @param authorName the name of the author to retrieve
     * @return the Author object with the given name or null if not found
     * */
    public Author getAuthorByName(String authorName) {
        if(!Utils.isValidString(authorName)) return null;

        try {
            String query = "SELECT * FROM Authors WHERE full_name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, authorName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) return null;
            return new Author(resultSet.getInt(1), resultSet.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a new reference to the database and creates a mapping between the given identifier ID and
     * the new reference ID.
     * @param reference The reference object to be added to the database
     * @param identifierId The identifier ID to be mapped with the new reference ID
     * @return true if the reference was added successfully, false otherwise
     * */
    public boolean addReferences(Reference reference, int identifierId) {
        try {
            for(String referenceIdentifier: reference.getReferences()) {
                if(!Utils.isValidString(referenceIdentifier)) return false;
                ResultSet existingRefPublication = getPublicationByIdentifier(referenceIdentifier);

                if(existingRefPublication.next()) {
                    ResultSet referencedData = getMappedReference(identifierId, existingRefPublication.getInt(1));
                    if(!referencedData.next()) {
                        String query = "INSERT INTO PublicationReferences VALUES (?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, identifierId);
                        preparedStatement.setInt(2, existingRefPublication.getInt(1));
                        preparedStatement.executeUpdate();
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a ResultSet of a mapped reference between a given identifier and a referred identifier from the database.
     * @param identifier the identifier of the publication being referred to
     * @param referredIdentifier the identifier being referred to
     * @return a ResultSet containing the mapped reference information
     * */
    public ResultSet getMappedReference(int identifier, int referredIdentifier) {
        try {
            String query = "SELECT * FROM PublicationReferences WHERE publication_id=? AND referenced_publication_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, identifier);
            preparedStatement.setInt(2, referredIdentifier);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves all the references by the given publication identifier
     * @param identifier unique identifier for the publication
     * @return a set of string references associated with the given identifier
     * */
    public Set<String> getAllReferencesById(int identifier) {
        try {
            String query = "SELECT pnew.identifier\n" +
                    "FROM Publications p\n" +
                    "JOIN PublicationReferences pr\n" +
                    "ON p.publication_id = pr.publication_id\n" +
                    "JOIN Publications pnew\n" +
                    "ON pr.referenced_publication_id = pnew.publication_id\n" +
                    "WHERE p.publication_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, identifier);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<String> references = new HashSet<>();
            while (resultSet.next()) {
                references.add(resultSet.getString(1));
            }
            return references;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the set of authors associated with a given publication ID.
     * @param publicationId The ID of the publication to retrieve authors for.
     * @return A set of strings representing the names of the authors associated with the publication.
     * */
    public Set<String> getPublicationAuthors(int publicationId) {
        try {
            String query = "SELECT a.full_name \n" +
                    "FROM Authors a\n" +
                    "JOIN PublicationAuthors pa\n" +
                    "ON a.author_id = pa.author_id\n" +
                    "JOIN Publications p\n" +
                    "ON pa.publication_id = p.publication_id\n" +
                    "WHERE p.publication_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, publicationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<String> authors = new HashSet<>();
            while (resultSet.next()) {
                authors.add(resultSet.getString(1));
            }
            return authors;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the number of citations for the given author.
     * @return the number of citations for the author
     * */
    public int getAuthorCitations(String authorName) {
        try {
            String query = "SELECT COUNT(pa.author_id)\n" +
                    "FROM PublicationAuthors pa\n" +
                    "JOIN Authors a\n" +
                    "ON pa.author_id = a.author_id\n" +
                    "WHERE a.full_name=?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, authorName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) return 0;
            return resultSet.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *  Returns a set of research areas for a given author, based on the number of times they have
     *  been mentioned in their publications. Only areas mentioned at least 'threshold' times will be
     *  included in the result.
     *
     *  @param author    the name of the author to get research areas for
     *  @param threshold the minimum number of times a research area must be mentioned to be included
     *  in the result
     *  @return a set of research areas for the given author
     * */
    public Set<String> getAuthorResearchAreas(String author, int threshold) {
        try {
            String query = "(SELECT ra.research_area_name FROM ResearchAreas ra\n" +
                    "INNER JOIN VenueResearchAreas vra\n" +
                    "ON ra.research_area_id = vra.research_area_id\n" +
                    "INNER JOIN Venues v\n" +
                    "ON vra.venue_id = v.venue_id\n" +
                    "INNER JOIN Publications p\n" +
                    "ON p.venue_id = v.venue_id\n" +
                    "INNER JOIN PublicationAuthors pa\n" +
                    "ON pa.publication_id = p.publication_id\n" +
                    "INNER JOIN Authors a\n" +
                    "ON a.author_id = pa.author_id\n" +
                    "WHERE a.full_name = ?\n" +
                    "GROUP BY ra.research_area_name, ra.research_area_id\n" +
                    "HAVING COUNT(ra.research_area_name) >= ?)\n" +
                    "UNION\n" +
                    "(SELECT distinct(ra.research_area_name)\n" +
                    "FROM research_area_reference rar\n" +
                    "INNER JOIN ResearchAreas ra\n" +
                    "ON ra.research_area_id = rar.parent_research_area_id\n" +
                    "WHERE rar.child_research_area_id IN (SELECT ra.research_area_id FROM ResearchAreas ra\n" +
                    "INNER JOIN VenueResearchAreas vra\n" +
                    "ON ra.research_area_id = vra.research_area_id\n" +
                    "INNER JOIN Venues v\n" +
                    "ON vra.venue_id = v.venue_id\n" +
                    "INNER JOIN Publications p\n" +
                    "ON p.venue_id = v.venue_id\n" +
                    "INNER JOIN PublicationAuthors pa\n" +
                    "ON pa.publication_id = p.publication_id\n" +
                    "INNER JOIN Authors a\n" +
                    "ON a.author_id = pa.author_id\n" +
                    "WHERE a.full_name = ?\n" +
                    "GROUP BY ra.research_area_name, ra.research_area_id\n" +
                    "HAVING COUNT(ra.research_area_name) >= ?));";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, author);
            preparedStatement.setInt(2, threshold);
            preparedStatement.setString(3, author);
            preparedStatement.setInt(4, threshold);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<String> authorResearchAreas = new HashSet<>();
            while (resultSet.next()) {
                authorResearchAreas.add(resultSet.getString(1));
            }
            return authorResearchAreas;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getPublicationAuthorMap() {
        try {
            String query = "SELECT p.identifier, a.full_name\n" +
                    "FROM Publications p\n" +
                    "JOIN PublicationAuthors pa \n" +
                    "ON p.publication_id = pa.publication_id\n" +
                    "JOIN Authors a\n" +
                    "ON a.author_id = pa.author_id;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Papers with at least 'paperCitation' citations in the given area and at least 'otherCitations'
     * citations in other areas will be included in the result.
     *
     * @param area: the name of the research area to get seminal papers for
     * @param paperCitation: the minimum number of citations a paper must have in the given area to be
     * included in the result
     * @param otherCitations the minimum number of citations a paper must have in other areas to be
     * included in the result
     * @return a set of seminal papers for the given research area
     * */
    public Set<String> getSeminalPapers( String area, int paperCitation, int otherCitations ) {
        try {
            Set<String> seminalPapers = new HashSet<>();
            String query = "(SELECT p.publication_id, p.identifier FROM Publications p\n" +
                    "JOIN PublicationReferences pr\n" +
                    "ON pr.publication_id = p.publication_id\n" +
                    "GROUP BY pr.publication_id\n" +
                    "HAVING pr.publication_id IN (SELECT p.publication_id\n" +
                    "FROM ResearchAreas ra\n" +
                    "JOIN VenueResearchAreas vra\n" +
                    "ON vra.research_area_id = ra.research_area_id\n" +
                    "JOIN Venues v\n" +
                    "ON v.venue_id = vra.venue_id\n" +
                    "JOIN Publications p\n" +
                    "ON p.venue_id = v.venue_id WHERE ra.research_area_name = ?) AND COUNT(pr.publication_id) < ?)\n" +
                    "INTERSECT\n" +
                    "(SELECT distinct(p.publication_id), p.identifier FROM (SELECT pr.referenced_publication_id as referenced_publication_id FROM Publications p\n" +
                    "JOIN PublicationReferences pr\n" +
                    "ON pr.publication_id = p.publication_id\n" +
                    "GROUP BY pr.referenced_publication_id\n" +
                    "HAVING pr.referenced_publication_id IN (SELECT p.publication_id\n" +
                    "FROM ResearchAreas ra\n" +
                    "JOIN VenueResearchAreas vra\n" +
                    "ON vra.research_area_id = ra.research_area_id\n" +
                    "JOIN Venues v\n" +
                    "ON v.venue_id = vra.venue_id\n" +
                    "JOIN Publications p\n" +
                    "ON p.venue_id = v.venue_id WHERE ra.research_area_name = ?) AND COUNT(pr.publication_id) > ?) sq\n" +
                    "INNER JOIN PublicationReferences pr\n" +
                    "ON pr.referenced_publication_id = sq.referenced_publication_id\n" +
                    "INNER JOIN Publications p\n" +
                    "ON p.publication_id = pr.publication_id);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, area);
            preparedStatement.setInt(2, paperCitation);
            preparedStatement.setString(3, area);
            preparedStatement.setInt(4, otherCitations);
            preparedStatement.setInt(4, otherCitations);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                seminalPapers.add(resultSet.getString(2));
            }
            return seminalPapers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
