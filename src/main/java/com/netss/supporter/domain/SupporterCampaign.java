package com.netss.supporter.domain;

import lombok.Builder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "supporter_campaign")
public class SupporterCampaign {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "supporter_id")
    @NotNull
    private Long supporterId;

    @Column(name = "campaign_id")
    @NotNull
    private Long campaignId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Long getSupporterId() {
        return supporterId;
    }

    public void setSupporterId(Long supporterId) {
        this.supporterId = supporterId;
    }

    public @NotNull Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupporterCampaign)) return false;

        SupporterCampaign that = (SupporterCampaign) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getSupporterId() != null ? !getSupporterId().equals(that.getSupporterId()) : that.getSupporterId() != null)
            return false;
        return getCampaignId() != null ? getCampaignId().equals(that.getCampaignId()) : that.getCampaignId() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getSupporterId() != null ? getSupporterId().hashCode() : 0);
        result = 31 * result + (getCampaignId() != null ? getCampaignId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SupporterCampaign{" +
            "id=" + id +
            ", supporterId=" + supporterId +
            ", campaignId=" + campaignId +
            '}';
    }
}
