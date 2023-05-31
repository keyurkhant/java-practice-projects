import Utils.Utils;
import services.PublicationLibrary;

import java.util.*;

/**
 * This is the main class of the publication library
 * CAUTION
 * Please uncomment code for each section to execute complete flow of the application. It will provide smooth execution.
 * */
public class Main {
    public static void main(String [] args) {
        PublicationLibrary publicationLibrary = new PublicationLibrary();

        // Add Research Areas
//        String researchArea = "Text Processing with GPT";
//        Set<String> emptyParentArea = new HashSet<>();
//        Set<String> parentArea = new HashSet<>();
//        parentArea.add("Deep Learning");
//        parentArea.add("Machine Learning");
//        parentArea.add("Artificial Intelligence");
//
//        System.out.println(publicationLibrary.addArea(researchArea, parentArea));


        // Add Publisher
//        Map<String, String> publisherInfo = new HashMap<>();
//        publisherInfo.put("contact_name", "Blackwall");
//        publisherInfo.put("contact_email", "info@blackwall.com");
//        publisherInfo.put("location", "Halifax");
//
//        System.out.println(publicationLibrary.addPublisher("KeyurKhushal2021", publisherInfo));


        // Add Venue
//        String venueName = "Shift Key conference of new ML ideas";
//        Map<String, String> venueInformation = new HashMap<>();
//        venueInformation.put("editor", "MS Dhoni");
//        venueInformation.put("editor_contact", "+1 9119999221");
//        venueInformation.put("publisher", "Springer");
//        venueInformation.put("location", "Toronto");
//        venueInformation.put("conference_year", "2023");
//        Set<String> researchAreas = new HashSet<>();
//        researchAreas.add("Generative Learning");
//        researchAreas.add("Image Processing");
//
//        System.out.println(publicationLibrary.addVenue(venueName, venueInformation, researchAreas));

        // Add Publication
//        String identifier = "McAllister2023";
//        Map<String, String> publicationInformation = new HashMap<>();
//        publicationInformation.put("title", "ItemSage: Learning Product Embeddings for Shopping Recommendations");
//        publicationInformation.put("publication", "Shift Key conference of new ML ideas");
//        publicationInformation.put("pages", "143-222");
//        publicationInformation.put("volume", "98");
//        publicationInformation.put("issue", "5");
//        publicationInformation.put("month", "October");
//        publicationInformation.put("year", "2023");
//        publicationInformation.put("location", "Halifax");
//        publicationInformation.put("authors", "Mike McAllister,Roger McAllister,Andrew Cochran");
//
//        System.out.println(publicationLibrary.addPublication(identifier, publicationInformation));

        // Add References
//        String identifier = "KhushalParth2022";
//        Set<String> references = new HashSet<>();
//        references.add("Recommender2022");
//        System.out.println(publicationLibrary.addReferences(identifier, references));

        // Get publication
//        String identifier = "KhushalParth2022";
//        Map<String, String> result = publicationLibrary.getPublications(identifier);
//        if(result != null) {
//            for (Map.Entry<String, String> entry : result.entrySet()) {
//                System.out.println(entry.getKey() + "=====>" + entry.getValue());
//            }
//        }

        // Author citations
//        String authorName = "Keyur Khant";
//        System.out.println(publicationLibrary.authorCitations(authorName));

        // Collaborator finder
//        Set<String> data = publicationLibrary.collaborators("Khushal Gondaliya",1);
//        for(String author: data) {
//            System.out.println(author);
//        }

        // seminalPapers
//        String area = "Generative Learning";
//        int paperCitations = 3;
//        int otherCitations = 1;
//        Set<String> seminalPapers = publicationLibrary.seminalPapers(area, paperCitations, otherCitations);
//        for(String seminalPaper: seminalPapers) {
//            System.out.println(seminalPaper);
//        }

        // Author research areas
//        String author = "Khushal Gondaliya";
//        int threshold = 2;
//        Set<String> authorResearchAreas = publicationLibrary.authorResearchAreas(author, threshold);
//        for(String authorResearchArea: authorResearchAreas) {
//            System.out.println(authorResearchArea);
//        }


        // IEEE conversion
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Please enter input filename: ");
//        String inputFileName = scanner.nextLine();
//        System.out.print("Please enter output filename: ");
//        String outputFileName = scanner.nextLine();
//
//        publicationLibrary.convertCitations(inputFileName, outputFileName);
    }
}
