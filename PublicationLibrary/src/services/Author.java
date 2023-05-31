package services;

import repositories.PublicationRepository;

import java.sql.ResultSet;
import java.util.*;

/**
 * Represents an author in the system, with attributes such as name, email, and affiliation.
 * Provides methods for adding and retrieving information about the author's publications, collaborators,
 * and research areas.
 * */
public class Author {
    private int authorId;
    private String fullName;

    PublicationRepository publicationRepository = new PublicationRepository();

    public Author(int authorId, String fullName) {
        this.authorId = authorId;
        this.fullName = fullName;
    }

    public Author(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Returns a Set of Strings representing the collaborators of the author within a specified distance.
     * The distance parameter specifies the maximum number of degrees of separation between the author and
     * their collaborators. For example, a distance of 1 would return the author's immediate collaborators,
     * while a distance of 2 would return the author's collaborators and their collaborators. The returned
     * Set includes the names of the collaborators, sorted alphabetically.
     * @param author A String representing the name of the author. Must not be null or empty.
     * @param distance An int representing the maximum distance between the author and their collaborators.
     * @return A Set of Strings representing the collaborators of the author within the specified distance.
     * */
    public Set<String> getCollaborators(String author, int distance) {
        Map<String, Set<String>> adjacencyList = generateAdjacencyList(publicationRepository.getPublicationAuthorMap());
        Set<String> collaborators = new HashSet<>();
        if (!adjacencyList.containsKey(author)) return collaborators;

        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> parents = new HashMap<>();

        queue.offer(author);
        distances.put(author, 0);
        parents.put(author, null);

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            int currentDistance = distances.get(curr);
            if (currentDistance > distance) break;

            for (String neighbor : adjacencyList.getOrDefault(curr, new HashSet<>())) {
                if (!distances.containsKey(neighbor)) {
                    queue.offer(neighbor);
                    distances.put(neighbor, currentDistance + 1);
                    parents.put(neighbor, curr);
                }
            }
        }

        for (String collaborator : distances.keySet()) {
            if (!collaborator.equals(author) && distances.get(collaborator) <= distance) {
                collaborators.add(collaborator);
            }
        }

        return collaborators;
    }

    /**
     * Generates an adjacency list representing the relationships between authors and their publications.
     * The adjacency list is stored in a Map, where the keys are Strings representing the authors' names and
     * the values are Sets of Strings representing the identifiers of the publications authored by each author.
     * The adjacency list is generated from a ResultSet containing information about the mapping between
     * publications and authors.
     * @param publicationAuthorMap A ResultSet containing information about the mapping between publications
     * @return A Map representing the adjacency list of authors and their publications.
     * */
    private Map<String, Set<String>> generateAdjacencyList(ResultSet publicationAuthorMap) {
        Map<String, Set<String>> adjacencyList = new HashMap<>();
        Map<String, Set<String>> groups = new HashMap<>();
        try {
            while(publicationAuthorMap.next()) {
                String identifier = publicationAuthorMap.getString(1);
                String author = publicationAuthorMap.getString(2);
                if (!groups.containsKey(identifier)) {
                    groups.put(identifier, new HashSet<>() );
                }
                groups.get(identifier).add(author);
            }
            for (Set<String> authors : groups.values()) {
                List<String> authorList = new ArrayList<>(authors);
                for (int i = 0; i < authorList.size(); i++) {
                    String author1 = authorList.get(i);
                    for (int j = i + 1; j < authorList.size(); j++) {
                        String author2 = authorList.get(j);
                        if (!adjacencyList.containsKey(author1)) {
                            adjacencyList.put(author1, new HashSet<>());
                        }
                        if (!adjacencyList.containsKey(author2)) {
                            adjacencyList.put(author2, new HashSet<>());
                        }
                        adjacencyList.get(author1).add(author2);
                        adjacencyList.get(author2).add(author1);
                    }
                }
            }
            return adjacencyList;
        } catch (Exception e) {
            return adjacencyList;
        }
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
