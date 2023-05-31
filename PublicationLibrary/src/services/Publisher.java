package services;

import Utils.Utils;
import repositories.PublicationRepository;
import repositories.PublisherRepository;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Represents a publisher of academic publications.
 * */
public class Publisher {

    private int publisherId;
    private String contactName;
    private String contactEmail;
    private String location;

    private PublisherRepository publisherRepository = new PublisherRepository();
    private PublicationRepository publicationRepository = new PublicationRepository();

    public Publisher(Map<String, String> publisher) {
        this.contactName = publisher.get("contact_name");
        this.contactEmail = publisher.get("contact_email");
        this.location = publisher.get("location");
    }
    public Publisher(String contactName, String contactEmail, String location) {
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.location = location;
    }

    public Publisher(int publisherId, String contactName, String contactEmail, String location) {
        this.publisherId = publisherId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.location = location;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Adds a new publisher with the specified identifier to the system.
     * @param identifier The unique identifier of the publisher to add. Must not be null or empty.
     * @return True if the publisher was added successfully, false otherwise.
     * */
    public boolean addPublisher(String identifier) {
        if(!validatePublisher()) {
            return false;
        }
        try {
            ResultSet existingPublication = publicationRepository.getPublicationByIdentifier(identifier);
            if(existingPublication == null || existingPublication.next()) return false;

            ResultSet existingPublisher = publisherRepository.getPublisherByEmail(contactEmail);
            if(existingPublisher == null || existingPublisher.next()) return false;

            return publisherRepository.addPublisher(new Publisher(contactName, contactEmail, location));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates whether the current publisher object is valid.
     * @return True if the publisher object is valid, false otherwise.
     * */
    private boolean validatePublisher() {
        return Utils.isValidString(contactName) && Utils.isValidString(contactEmail) && Utils.isValidString(location);
    }
}