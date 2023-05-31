package repositories;

import Utils.Utils;
import services.Publisher;
import services.ResearchArea;
import services.Venue;

import java.sql.*;
import java.util.Set;

public class VenueRepository {
    Connection connection;
    private PublisherRepository publisherRepository = new PublisherRepository();
    private ResearchAreaRepository researchAreaRepository = new ResearchAreaRepository();
    public VenueRepository() {
        connection = Database.getInstance().connectDatabase();
    }

    /**
     * Adds a new venue to the system.
     * @param venue the venue object to be added
     * @return true if the venue was successfully added, false otherwise (e.g., if the venue already exists in the system)
     * */
    public boolean addVenue(Venue venue) {
        try {
            if(connection != null) {
                Publisher publisher = publisherRepository.getPublisherByName(venue.getPublisherName());
                if (publisher == null) return false;
                String query = "INSERT INTO Venues VALUES (NULL, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, venue.getVenueName());
                preparedStatement.setInt(2, publisher.getPublisherId());
                preparedStatement.setString(3, venue.getEditor());
                preparedStatement.setString(4, venue.getEditorContact());
                preparedStatement.setString(5, venue.getLocation());
                preparedStatement.setString(6, venue.getEventYear());

                for(String researchArea: venue.getResearchAreas()) {
                    ResearchArea parentAreaData = researchAreaRepository.getResearchAreaByName(researchArea);
                    if(parentAreaData == null) return false;
                }
                int affectedRows = preparedStatement.executeUpdate();
                int venueId = -1;
                if(affectedRows > 0) {
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if(resultSet.next()) {
                        venueId = resultSet.getInt(1);
                    }
                }
                if(venueId != -1) {
                    addVenueResearchAreaMapping(venueId, venue.getResearchAreas());
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
     * Adds a mapping between a venue and a set of research areas in the system.
     * @param venueId the ID of the venue to map to the research areas
     * @param researchAreas a set of research area names to be mapped to the venue
     * */
    private void addVenueResearchAreaMapping(int venueId, Set<String> researchAreas) {
        try {
            if(connection != null) {
                String query = "INSERT INTO VenueResearchAreas VALUES (?, ?)";
                for(String researchArea: researchAreas) {
                    ResearchArea researchAreaData = researchAreaRepository.getResearchAreaByName(researchArea);
                    if(researchAreaData == null) break;

                    int researchAreaId = researchAreaData.getResearchAreaId();
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, venueId);
                    preparedStatement.setInt(2, researchAreaId);

                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a result set of venues that match the given venue name.
     * If no matching venues are found, it returns an empty result set.
     * @param venueName the name of the venue to search for
     * @return a result set of venues that match the given venue name, or an empty result set if no matching venues are found
     * */
    public ResultSet getVenueByName(String venueName) {
        if(!Utils.isValidString(venueName)) return null;

        try {
            String query = "SELECT * FROM Venues WHERE venue_name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, venueName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) return null;
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
