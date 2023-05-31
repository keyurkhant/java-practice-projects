package services;

import Utils.Utils;
import repositories.VenueRepository;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a venue (e.g., conference, journal) in the system, with attributes such as name, publisher,
 * and research areas. Provides methods for adding and retrieving information about the venue's publications
 * and authors.
 * */
public class Venue {
    private int venueId;
    private String venueName;
    private String publisherName;
    private String editor;
    private String editorContact;
    private String location;
    private String eventYear;
    private  Set<String> researchAreas;

    private VenueRepository venueRepository = new VenueRepository();

    public Venue(String venueName, Map<String, String> venueInformation, Set<String> researchAreas) {
        this.venueName = venueName;
        this.editor = venueInformation.get("editor");
        this.editorContact = venueInformation.get("editor_contact");
        this.publisherName = venueInformation.get("publisher");
        this.location = venueInformation.get("location");
        this.eventYear = venueInformation.get("conference_year");
        this.researchAreas = researchAreas;
    }

    /**
     * Adds a new venue to the system with the specified name, venue information, and research areas.
     * @return true if the venue was successfully added, false otherwise.
     * */
    public boolean addVenue() {
        if(!Utils.isValidString(venueName)) return false;
        try {
            ResultSet existingVenue = venueRepository.getVenueByName(venueName);
            if(existingVenue.next()) return false;

            return venueRepository.addVenue(this);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getEditorContact() {
        return editorContact;
    }

    public void setEditorContact(String editorContact) {
        this.editorContact = editorContact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventYear() {
        return eventYear;
    }

    public void setEventYear(String eventYear) {
        this.eventYear = eventYear;
    }

    public Set<String> getResearchAreas() {
        return researchAreas;
    }

    public void setResearchAreas(Set<String> researchAreas) {
        this.researchAreas = researchAreas;
    }
}
