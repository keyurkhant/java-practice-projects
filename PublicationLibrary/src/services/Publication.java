package services;

import Utils.Utils;
import repositories.PublicationRepository;
import repositories.PublisherRepository;
import repositories.ResearchAreaRepository;
import repositories.VenueRepository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * The Publication class represents a publication entity in a bibliographic system.
 * It contains attributes such as title, authors, publication date, and publication venue.
 * The class provides methods for retrieving and modifying these attributes, as well as for
 * calculating citation metrics and generating formatted citations.
 * */
public class Publication {
    private int publicationId;
    private String identifier;
    private String paperTitle;
    private String publicationTitle;
    private Set<String> authors;
    private String pages;
    private String volume;
    private String issue;
    private String month;
    private String year;
    private String location;
    private PublicationRepository publicationRepository = new PublicationRepository();
    private VenueRepository venueRepository = new VenueRepository();
    private PublisherRepository publisherRepository = new PublisherRepository();
    private ResearchAreaRepository researchAreaRepository = new ResearchAreaRepository();

    public Publication(String identifier, Map<String, String> publicationInformation ) {
        this.identifier = identifier;
        this.paperTitle = publicationInformation.get("title");
        this.publicationTitle = publicationInformation.get("publication");
        this.authors = Utils.getSetFromString(publicationInformation.get("authors"), ",");
        this.pages = publicationInformation.get("pages");
        this.volume = publicationInformation.get("volume");
        this.issue = publicationInformation.get("issue");
        this.month = publicationInformation.get("month");
        this.year = publicationInformation.get("year");
        this.location = publicationInformation.get("location");
    }

    public Publication(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Adds a new publication to the bibliographic system. The publication information is provided
     * as parameters to the method, including an identifier and a Map containing publication information
     * such as title, authors, publication date, and publication venue. Returns a boolean value indicating
     * whether the operation was successful or not.
     * @return A boolean value indicating whether the operation was successful or not.
     * */
    public boolean addPublication() {
        if(!Utils.isValidString(identifier)) return false;
        if(authors.isEmpty()) return false;

        try {
            ResultSet existingPublication = publicationRepository.getPublicationByIdentifier(identifier);
            if(existingPublication.next()) return false;

            return publicationRepository.addPublication(this);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a Map object containing all the publication information for the publication
     * with the specified identifier. The publication information includes attributes such
     * as title, authors, publication date, and publication venue.
     * @param identifier A String representing the unique identifier for the publication. Must not be null or empty.
     * @return A Map object containing all the publication information for the publication with the specified identifier.
     * */
    public Map<String, String> getAllPublicationInformation(String identifier) {
        Map<String, String> allPublicationInformation = new HashMap<>();
        try {
            ResultSet publicationSet = publicationRepository.getPublicationByIdentifier(identifier);
            if(!publicationSet.next()) return null;

            // Authors
            Set<String> authors = publicationRepository.getPublicationAuthors(publicationSet.getInt(1));

            // References
            Set<String> references = publicationRepository.getAllReferencesById(publicationSet.getInt(1));

            // Venue
            ResultSet venueSet = venueRepository.getVenueByName(publicationSet.getString("publication_title"));

            // Publisher
            Publisher publisher = publisherRepository.getPublisherById(venueSet.getInt("publisher_id"));

            // Research Area
            Set<String> researchAreas = researchAreaRepository.getResearchAreaByVenueId(venueSet.getInt(1));

            return handlePublicationInformationMap(publicationSet, venueSet, authors, references, researchAreas, publisher);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Private helper method that handles the processing of publication information retrieved from a database.
     * The method takes as input ResultSets for the publication and venue tables, as well as Sets of authors,
     * references, and research areas associated with the publication, and a Publisher object representing the
     * publisher of the publication. The method returns a Map object containing the processed publication information,
     * including attributes such as title, authors, publication date, and publication venue.
     * @param publicationSet A ResultSet representing the publication information retrieved from the database. Must not be null.
     * @param venueSet A ResultSet representing the venue information retrieved from the database. Must not be null.
     * @param authors A Set of Strings representing the authors associated with the publication. Must not be null.
     * @param references A Set of Strings representing the references cited in the publication. Must not be null.
     * @param researchAreas A Set of Strings representing the research areas associated with the publication. Must not be null.
     * @param publisher A Publisher object representing the publisher of the publication. Must not be null.
     * @return A Map object containing the processed publication information, including attributes
     * */
    private Map<String, String> handlePublicationInformationMap(ResultSet publicationSet, ResultSet venueSet,
                                                                Set<String> authors, Set<String> references,
                                                                Set<String> researchAreas, Publisher publisher) {
        Map<String, String> publicationInformation = new LinkedHashMap<>();
        try {
            publicationInformation.put("identifier", publicationSet.getString("identifier"));
            publicationInformation.put("paper_title", publicationSet.getString("paper_title"));
            publicationInformation.put("publication_title(Venue)", publicationSet.getString("publication_title"));
            publicationInformation.put("pages", publicationSet.getString("pages"));
            publicationInformation.put("volume", publicationSet.getString("volume"));
            publicationInformation.put("issue", publicationSet.getString("issue"));
            publicationInformation.put("month", publicationSet.getString("month"));
            publicationInformation.put("year", publicationSet.getString("year"));
            publicationInformation.put("location", publicationSet.getString("location"));

            publicationInformation.put("authors", Utils.getStringFromSet(authors, ","));

            publicationInformation.put("references", Utils.getStringFromSet(references, ","));

            publicationInformation.put("researchAreas", Utils.getStringFromSet(researchAreas, ","));

            publicationInformation.put("publisher_contact_name", publisher.getContactName());
            publicationInformation.put("publisher_contact_email", publisher.getContactEmail());
            publicationInformation.put("publisher_location", publisher.getLocation());

            publicationInformation.put("editor", venueSet.getString("editor"));
            publicationInformation.put("editor_contact", venueSet.getString("editor_contact"));
            publicationInformation.put("venue_location", venueSet.getString("location"));
            publicationInformation.put("venue_event_year", venueSet.getString("event_year"));

            while (publicationInformation.values().remove(null));
            return publicationInformation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public String getPublicationTitle() {
        return publicationTitle;
    }

    public void setPublicationTitle(String publicationTitle) {
        this.publicationTitle = publicationTitle;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
