package services;

import Utils.Utils;
import repositories.PublicationRepository;
import repositories.PublisherRepository;

import java.sql.ResultSet;
import java.util.Set;

/**
 * Represents a reference to a published work.
 * */
public class Reference {

    private String identifier;
    private Set<String> references;
    PublicationRepository publicationRepository = new PublicationRepository();

    public Reference(String identifier, Set<String> references) {
        this.identifier = identifier;
        this.references = references;
    }

    /**
     * Adds a new set of references with the specified identifier to the system.
     * @return True if the reference set was added successfully, false otherwise.
     * */
    public boolean addReferences() {
        if(!Utils.isValidString(identifier) || references == null || references.isEmpty()) return false;
        try {
            ResultSet existingPublication = publicationRepository.getPublicationByIdentifier(identifier);
            if(!existingPublication.next()) return false;
            references.remove(identifier);

            return publicationRepository.addReferences(this, existingPublication.getInt(1));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Set<String> getReferences() {
        return references;
    }

    public void setReferences(Set<String> references) {
        this.references = references;
    }
}
