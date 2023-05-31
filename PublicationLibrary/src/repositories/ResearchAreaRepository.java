package repositories;

import services.ResearchArea;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ResearchAreaRepository {

    Connection connection;
    public ResearchAreaRepository() {
        connection = Database.getInstance().connectDatabase();
    }

    /**
     * Adds a research area to the database.
     * @param researchArea The research area to be added.
     * @return true if the research area is added successfully, false otherwise.
     * */
    public boolean addResearchArea(ResearchArea researchArea) {
        try {
            if(connection != null) {
                // Handling research area table
                String query = "INSERT INTO ResearchAreas VALUES (NULL, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, researchArea.getResearchArea());

                int affectedRows = preparedStatement.executeUpdate();
                int researchAreaId = -1;
                if(affectedRows > 0) {
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if(resultSet.next()) {
                        researchAreaId = resultSet.getInt(1);
                    }
                }
                if(researchAreaId != -1 && researchArea.getParentArea().size() > 0) {
                    addResearchAreaReferences(researchAreaId, researchArea.getParentArea());
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
     * Adds a research area references to the database.
     * @param researchAreaId The research area id to handle
     * @param parentAreas List of parent areas
     * @return true if the research area references are added successfully, false otherwise.
     * */
    private boolean addResearchAreaReferences(int researchAreaId, Set<String> parentAreas) {
        try {
            if(connection != null) {
                String query = "INSERT INTO research_area_reference VALUES (?, ?)";
                for(String area: parentAreas) {
                    ResearchArea parentAreaData = getResearchAreaByName(area);
                    if(parentAreaData == null) break;

                    int parentAreaId = parentAreaData.getResearchAreaId();
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, parentAreaId);
                    preparedStatement.setInt(2, researchAreaId);

                    preparedStatement.executeUpdate();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * This method returns a ResearchArea object that matches the given research area name.
     * If no matching research area is found, it returns null.
     * @param researchAreaName the name of the research area to search for
     * @return a ResearchArea object that matches the given research area name, or null if no matching research area is found
     * */
    public ResearchArea getResearchAreaByName(String researchAreaName) {
        try {
            if(connection != null) {
                String query = "SELECT * from ResearchAreas WHERE research_area_name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, researchAreaName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    return new ResearchArea(resultSet.getInt("research_area_id"),
                            resultSet.getString("research_area_name"));
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * This method retrieves a set of research area names associated with a given venue ID.
     * If the given venue ID is not found, it returns an empty set.
     * @param venueId the ID of the venue to search for
     * @return a set of research area names associated with the given venue ID, or an empty set if the venue ID is not found
     * */
    public Set<String> getResearchAreaByVenueId(int venueId) {
        try {
            String query = "SELECT ra.research_area_name\n" +
                    "FROM ResearchAreas ra\n" +
                    "JOIN VenueResearchAreas vra\n" +
                    "ON ra.research_area_id = vra.research_area_id\n" +
                    "JOIN Venues v\n" +
                    "ON vra.venue_id = v.venue_id\n" +
                    "WHERE v.venue_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, venueId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<String> researchAreas = new HashSet<>();
            while (resultSet.next()) {
                researchAreas.add(resultSet.getString(1));
            }
            return researchAreas;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
