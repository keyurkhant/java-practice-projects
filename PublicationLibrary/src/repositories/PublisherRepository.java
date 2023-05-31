package repositories;

import services.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PublisherRepository {

    Connection connection;
    public PublisherRepository() {
        connection = Database.getInstance().connectDatabase();
    }

    /**
     * Adds the given Publisher object to the database.
     * @param publisher the Publisher object to add to the database
     * @return true if the Publisher object was added successfully, false otherwise
     * */
    public boolean addPublisher(Publisher publisher) {
        try {
            if(connection != null) {
                String query = "INSERT INTO Publishers VALUES (NULL, ?, ?, ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, publisher.getContactName());
                preparedStatement.setString(2, publisher.getContactEmail());
                preparedStatement.setString(3, publisher.getLocation());

                return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Retrieves a database result set containing the publisher with the specified email address.
     * @param email the email address of the publisher to retrieve
     * @return a result set containing the publisher with the specified email address
     * */
    public ResultSet getPublisherByEmail(String email) {
        try {
            if(connection != null) {
                String query = "SELECT * FROM Publishers WHERE contact_email = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, email);

                return preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Retrieves a publisher by name from the database.
     * @param name the name of the publisher to retrieve
     * @return a Publisher object with the publisher's information, or null if no matching publisher is found
     * */
    public Publisher getPublisherByName(String name) {
        try {
            if(connection != null) {
                String query = "SELECT * FROM Publishers WHERE contact_name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);

                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    return new Publisher(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4));
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
     * Retrieves a publisher from the database based on the specified publisher ID.
     * @param publisherId the ID of the publisher to retrieve
     * @return the Publisher object corresponding to the specified ID, or null if the publisher was not found
     * */
    public Publisher getPublisherById(int publisherId) {
        try {
            if(connection != null) {
                String query = "SELECT * FROM Publishers WHERE publisher_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, publisherId);

                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    return new Publisher(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4));
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
