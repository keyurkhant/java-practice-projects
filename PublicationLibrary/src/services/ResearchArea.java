package services;

import Utils.Utils;
import repositories.ResearchAreaRepository;

import java.util.Set;

/**
 * Represents a research area or field of study.
 * */
public class ResearchArea {
    private String researchArea;
    private int researchAreaId;
    private Set<String> parentArea;
    private ResearchAreaRepository researchAreaRepository = new ResearchAreaRepository();

    public ResearchArea(String researchArea, Set<String> parentArea) {
        this.researchArea = researchArea;
        this.parentArea = parentArea;
    }

    public ResearchArea(int researchAreaId, String researchArea) {
        this.researchArea = researchArea;
        this.researchAreaId = researchAreaId;
    }

    public int getResearchAreaId() {
        return researchAreaId;
    }

    public void setResearchAreaId(int researchAreaId) {
        this.researchAreaId = researchAreaId;
    }

    public String getResearchArea() {
        return researchArea;
    }

    public void setResearchArea(String researchArea) {
        this.researchArea = researchArea;
    }

    public Set<String> getParentArea() {
        return parentArea;
    }

    public void setParentArea(Set<String> parentArea) {
        this.parentArea = parentArea;
    }

    /**
     * Adds a new set of research areas to the system.
     * @return True if the research areas were added successfully, false otherwise.
     * */
    public boolean addResearchAreas() {
        if(!Utils.isValidString(researchArea) && parentArea == null) return false;

        return researchAreaRepository.addResearchArea(this);
    }
}
