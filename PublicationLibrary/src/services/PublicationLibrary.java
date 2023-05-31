package services;

import Utils.Utils;
import repositories.PublicationRepository;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublicationLibrary {
    private PublicationRepository publicationRepository = new PublicationRepository();
    public static enum CITATION_TYPE {JOURNAL, CONFERENCE};

    /**
     * Adds a publication with the specified identifier and publication information to the collection.
     * @param identifier The identifier of the publication to be added. Must not be null.
     * @param publicationInformation A Map containing the publication information for the publication to be added.
     * @return true if the publication was added successfully, false otherwise.
     * */
    public boolean addPublication( String identifier, Map<String, String> publicationInformation ) {
        Publication publication = new Publication(identifier, publicationInformation);
        return publication.addPublication();
    }

    /**
     * Adds a set of references to the specified identifier.
     * @param identifier The identifier to which the references will be added. Must not be null.
     * @param references A Set of Strings representing the references to be added. The Set must not be null
     * @return true if the references were added successfully, false otherwise.
     * */
    public boolean addReferences( String identifier, Set<String > references ) {
        Reference reference = new Reference(identifier, references);
        return reference.addReferences();
    }

    /**
     * Adds a venue to the system with the specified name, information, and research areas.
     * @param venueName The name of the venue to be added. Must not be null or empty.
     * @param venueInformation A Map containing the information about the venue to be added.
     * @param researchAreas A Set of Strings representing the research areas associated with the venue to be added.
     * @return true if the venue was added successfully, false otherwise.
     * */
    public boolean addVenue( String venueName, Map<String, String> venueInformation, Set<String> researchAreas ) {
        Venue venue = new Venue(venueName, venueInformation, researchAreas);
        return venue.addVenue();
    }

    /**
     * Adds a publisher to the system with the specified identifier and publisher information.
     * @param identifier The identifier of the publisher to be added. Must not be null or empty.
     * @param publisherInformation A Map containing the information about the publisher to be added.
     * @return true if the publisher was added successfully, false otherwise.
     * */
    public boolean addPublisher( String identifier, Map<String, String> publisherInformation ) {
        Publisher publisher = new Publisher(publisherInformation);
        return publisher.addPublisher(identifier);
    }

    /**
     * Adds a research area to the system with the specified name and parent areas.
     * @param researchArea The name of the research area to be added. Must not be null or empty.
     * @param parentArea A Set of Strings representing the parent research areas of the research area to be added.
     * @return true if the research area was added successfully, false otherwise.
     * */
    public boolean addArea( String researchArea, Set<String> parentArea ) {
        ResearchArea researchAreaObj = new ResearchArea(researchArea, parentArea);
        return researchAreaObj.addResearchAreas();
    }

    /**
     * Retrieves a Map of publication information for the publications matching the specified key.
     * @param key The search key used to match publications. Must not be null or empty.
     * @return A Map containing the publication information for the publications matching the specified key.
     * */
    public Map<String, String> getPublications( String key ) {
        Publication publication = new Publication(key);
        return publication.getAllPublicationInformation(key);
    }

    /**
     * Retrieves the number of citations for the specified author.
     * @param author The name of the author for whom to retrieve the citation count. Must not be null or empty.
     * @return An integer representing the number of citations for the specified author. Returns 0 if the author has no
     * citations or cannot be found.
     * */
    public int authorCitations( String author ) {
        return publicationRepository.getAuthorCitations(author);
    }

    /**
     * Retrieves a Set of the seminal papers in the specified research area based on the citation counts.
     * @param area The name of the research area for which to retrieve the seminal papers. Must not be null or empty.
     * @param paperCitation The minimum number of citations a paper must have in order to be considered seminal.
     * @param otherCitations The maximum number of citations allowed for papers in other research areas in order to be
     * considered seminal. Must be a non-negative integer.
     * @return A Set of Strings representing the identifiers of the seminal papers in the specified research area.
     * */
    public Set<String> seminalPapers( String area, int paperCitation, int otherCitations ) {
        return publicationRepository.getSeminalPapers(area, paperCitation, otherCitations);
    }

    /**
     * Retrieves a Set of the collaborators for the specified author up to the specified distance in terms of co-authorship.
     * @param author The name of the author for whom to retrieve the collaborators. Must not be null or empty.
     * @param distance The maximum distance between the author and the collaborators to be retrieved.
     * @return A Set of Strings representing the names of the collaborators of the specified author.
     * */
    public Set<String> collaborators( String author, int distance ) {
        Author authorObj = new Author(author);
        return authorObj.getCollaborators(author, distance);
    }

    /**
     * Retrieves a Set of the research areas of the specified author based on the threshold of the number of publications.
     * @param author The name of the author for whom to retrieve the research areas. Must not be null or empty.
     * @param threshold The minimum number of publications an author must have in a research area to be considered
     * a researcher of that area. Must be a non-negative integer.
     * @return A Set of Strings representing the research areas of the specified author. Returns an empty Set if the
     * author has no publications or if no research areas meet the threshold.
     * */
    public Set<String> authorResearchAreas( String author, int threshold ) {
        return publicationRepository.getAuthorResearchAreas(author, threshold);
    }

    /**
     * Converts the citations in the specified input file to a different format and saves the result to the specified output file.
     * @param inputFileName The name of the input file containing the citations to be converted. Must not be null or empty.
     * @param outputFileName The name of the output file to which the converted citations should be saved. Must not be null
     * @return A boolean indicating whether the conversion and saving operation was successful.
     * */
    public boolean convertCitations(String inputFileName, String outputFileName) {
        if(!Utils.isValidFileName(inputFileName)) return false;
        try {
            BufferedReader input = new BufferedReader(new FileReader(inputFileName));
            PrintWriter output = new PrintWriter(outputFileName);
            Map<String, String> referenceList = new LinkedHashMap<>();

            String line = null;
            List<String> uniqueIdentifierList = new ArrayList<>();
            while ((line = input.readLine()) != null) {
                if(line.contains("\\cite{")) {
                    String citationRegex = "\\\\cite\\{([a-zA-z\\d, ]*)\\}";
                    Pattern pattern = Pattern.compile(citationRegex);
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        String[] identifierList = matcher.group(1).trim().split(",");
                        for(String identifier: identifierList) {
                            if(!uniqueIdentifierList.contains(identifier.trim())) {
                                uniqueIdentifierList.add(identifier.trim());
                            }
                        }
                        String citationString = getInlineCitationString(identifierList, uniqueIdentifierList);
                        line = line.replaceFirst(citationRegex, citationString);
                    }
                }
                output.println(line);
            }
            handleReferenceList(output, uniqueIdentifierList);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Returns a String representing an inline citation based on the specified identifier list and unique identifier list.
     * @param identifierList An array of Strings representing the identifiers to be included in the inline citation.
     * @param uniqueIdentifierList A List of Strings representing the unique identifiers for the cited works.
     * @return A String representing an inline citation based on the specified identifier list and unique identifier list.
     * */
    private String getInlineCitationString(String[] identifierList, List<String> uniqueIdentifierList) {
        StringBuilder result = new StringBuilder();
        for(String identifier: identifierList) {
            result.append("[" + (uniqueIdentifierList.indexOf(identifier.trim()) + 1) + "]");
        }
        return  result.toString();
    }

    /**
     * Handles the reference list for the specified PrintWriter and identifier list.
     * @param output The PrintWriter to which the reference list should be written. Must not be null.
     * @param identifierList A List of Strings representing the identifiers for the cited works in the reference list.
     * */
    private void handleReferenceList(PrintWriter output, List<String> identifierList) {
        output.println("\nReferences:");
        for(String identifier: identifierList) {
            String referenceString = getReferenceString(identifier);
            String formattedString = "[" + (identifierList.indexOf(identifier) + 1) + "]\t" + referenceString;
            output.println(formattedString);
        }
    }

    /**
     * Returns a String representing a reference citation based on the specified identifier.
     * @param identifier A String representing the identifier for the cited work. Must not be null or empty.
     * @return A String representing a reference citation based on the specified identifier.
     * */
    private String getReferenceString(String identifier) {
        Map<String, String> citationMap = getPublications(identifier);
        CITATION_TYPE type = Utils.isValidString(citationMap.get("volume")) ? CITATION_TYPE.JOURNAL : CITATION_TYPE.CONFERENCE;
        StringBuilder referenceString = new StringBuilder();
        switch(type) {
            case JOURNAL :
                referenceString.append(getJournalCitationString(citationMap));
                break;
            case CONFERENCE:
                referenceString.append(getConferenceCitationString(citationMap));
                break;
        }
        return referenceString.toString();
    }

    /**
     * Returns a StringBuilder object representing a citation for a journal article based on the specified citation map.
     * @param citationMap A Map of key-value pairs representing the citation information for the journal article.
     * @return A StringBuilder object representing a citation for a journal article based on the specified citation map.
     * */
    private StringBuilder getJournalCitationString(Map<String, String> citationMap) {
        StringBuilder journalCitationString = new StringBuilder();
        // Authors
        if(Utils.isValidString(citationMap.get("authors"))) {
            journalCitationString.append(Utils.getAbbreviatedAuthorsString(citationMap.get("authors")));
        }
        // Paper title
        if(Utils.isValidString(citationMap.get("paper_title"))) {
            journalCitationString.append("\"" + citationMap.get("paper_title") + ",\" ");
        }
        // Publication title & location
        if(Utils.isValidString(citationMap.get("publication_title(Venue)"))) {
            journalCitationString.append(citationMap.get("publication_title(Venue)") + ", ");
        }
        // Volume
        if(Utils.isValidString(citationMap.get("volume"))) {
            journalCitationString.append("vol. " + citationMap.get("volume") + ", ");
        }
        // Issue no
        if(Utils.isValidString(citationMap.get("issue"))) {
            journalCitationString.append("no. " + citationMap.get("issue") + ", ");
        }
        // Page vs page range
        if(Utils.isValidString(citationMap.get("pages"))) {
            String pagesString = citationMap.get("pages");
            journalCitationString.append(
                    (pagesString.contains("-") ? "pp. " : "p. " ) +
                            citationMap.get("pages") + ", ");
        }
        // Month
        if(Utils.isValidString(citationMap.get("month"))) {
            journalCitationString.append(citationMap.get("month") + ", ");
        }
        // Year
        if(Utils.isValidString(citationMap.get("year"))) {
            journalCitationString.append(citationMap.get("year") + ".");
        }

        return journalCitationString;
    }

    /**
     * Returns a StringBuilder object representing a citation for a conference paper based on the specified citation map.
     * @param citationMap A Map of key-value pairs representing the citation information for the conference paper.
     * @return A StringBuilder object representing a citation for a conference paper based on the specified citation map.
     * */
    private StringBuilder getConferenceCitationString(Map<String, String> citationMap) {
        StringBuilder conferenceCitationString = new StringBuilder();
        // Authors
        if(Utils.isValidString(citationMap.get("authors"))) {
            conferenceCitationString.append(Utils.getAbbreviatedAuthorsString(citationMap.get("authors")));
        }
        // Paper title
        if(Utils.isValidString(citationMap.get("paper_title"))) {
            conferenceCitationString.append("\"" + citationMap.get("paper_title") + ",\" ");
        }
        // Publication title & location
        if(Utils.isValidString(citationMap.get("publication_title(Venue)"))) {
            conferenceCitationString.append("presented at " + citationMap.get("publication_title(Venue)"));
            if(Utils.isValidString(citationMap.get("venue_location"))) {
                conferenceCitationString.append("(" + citationMap.get("venue_location") + ")");
            }
            conferenceCitationString.append(", ");
        }
        // Month
        if(Utils.isValidString(citationMap.get("month"))) {
            conferenceCitationString.append(citationMap.get("month") + ", ");
        }
        // Year
        if(Utils.isValidString(citationMap.get("year"))) {
            conferenceCitationString.append(citationMap.get("year") + ", ");
        }
        // Page vs page range
        if(Utils.isValidString(citationMap.get("pages"))) {
            String pagesString = citationMap.get("pages");
            conferenceCitationString.append(
                    (pagesString.contains("-") ? "pp. " : "p. ") +
                            citationMap.get("pages") + ".");
        }
        return conferenceCitationString;
    }
}