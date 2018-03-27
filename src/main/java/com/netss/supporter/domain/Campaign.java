package com.netss.supporter.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Observable;

public class Campaign extends Observable implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String teamId;

    private LocalDate campaignStart;

    private LocalDate campaignEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public LocalDate getCampaignStart() {
        return campaignStart;
    }

    public void setCampaignStart(LocalDate campaignStart) {
        this.campaignStart = campaignStart;
    }

    public LocalDate getCampaignEnd() {
        return campaignEnd;
    }

    public void setCampaignEnd(LocalDate campaignEnd) {
        this.campaignEnd = campaignEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Campaign)) return false;

        Campaign campaign = (Campaign) o;

        if (getId() != null ? !getId().equals(campaign.getId()) : campaign.getId() != null) return false;
        if (getName() != null ? !getName().equals(campaign.getName()) : campaign.getName() != null) return false;
        if (getTeamId() != null ? !getTeamId().equals(campaign.getTeamId()) : campaign.getTeamId() != null)
            return false;
        if (getCampaignStart() != null ? !getCampaignStart().equals(campaign.getCampaignStart()) : campaign.getCampaignStart() != null) return false;
        return getCampaignEnd() != null ? getCampaignEnd().equals(campaign.getCampaignEnd()) : campaign.getCampaignEnd() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getTeamId() != null ? getTeamId().hashCode() : 0);
        result = 31 * result + (getCampaignStart() != null ? getCampaignStart().hashCode() : 0);
        result = 31 * result + (getCampaignEnd() != null ? getCampaignEnd().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Campaign{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", teamId='" + teamId + '\'' +
            ", campaignStart=" + campaignStart +
            ", campaignEnd=" + campaignEnd +
            '}';
    }
}
