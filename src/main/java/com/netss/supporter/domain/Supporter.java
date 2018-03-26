package com.netss.supporter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Observable;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "supporter")
public class Supporter extends Observable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 100)
    @Column(name = "supporter_name", length = 50)
    @NotNull
    private String name;

    @Size(max = 50)
    @Column(name = "team_id", length = 50)
    @NotNull
    private String teamId;

    @NotNull
    @Column(name = "supporter_birthday")
    private LocalDate birthday;

    @Email
    @Size(min = 5, max = 100)
    @Column(name = "supporter_email", length = 100, unique = true)
    private String supporterEmail;

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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getSupporterEmail() {
        return supporterEmail;
    }

    public void setSupporterEmail(String supporterEmail) {
        this.supporterEmail = supporterEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supporter)) return false;

        Supporter supporter = (Supporter) o;

        if (getId() != null ? !getId().equals(supporter.getId()) : supporter.getId() != null) return false;
        if (getName() != null ? !getName().equals(supporter.getName()) : supporter.getName() != null) return false;
        if (getTeamId() != null ? !getTeamId().equals(supporter.getTeamId()) : supporter.getTeamId() != null)
            return false;
        if (getBirthday() != null ? !getBirthday().equals(supporter.getBirthday()) : supporter.getBirthday() != null)
            return false;
        return getSupporterEmail() != null ? getSupporterEmail().equals(supporter.getSupporterEmail()) : supporter.getSupporterEmail() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getTeamId() != null ? getTeamId().hashCode() : 0);
        result = 31 * result + (getBirthday() != null ? getBirthday().hashCode() : 0);
        result = 31 * result + (getSupporterEmail() != null ? getSupporterEmail().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Supporter{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", teamId='" + teamId + '\'' +
            ", birthday=" + birthday +
            ", supporterEmail='" + supporterEmail + '\'' +
            '}';
    }
}
