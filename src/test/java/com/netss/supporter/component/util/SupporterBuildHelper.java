package com.netss.supporter.component.util;


import com.netss.supporter.domain.Supporter;

import java.time.LocalDate;

public class SupporterBuildHelper {

    public static Supporter supporterTiao() {
        Supporter officialSupporter = new Supporter();
        officialSupporter.setBirthday(LocalDate.ofYearDay(1995,50));
        officialSupporter.setEmail("tiao@test.com");
        officialSupporter.setName("tiao");
        officialSupporter.setTeamId("timao");
        return officialSupporter;
    }

    public static Supporter supporterMaria() {
        Supporter officialSupporter = new Supporter();
        officialSupporter.setBirthday(LocalDate.ofYearDay(1990,30));
        officialSupporter.setEmail("maria@test.com");
        officialSupporter.setName("maria");
        officialSupporter.setTeamId("spfc");
        return officialSupporter;
    }

    public static Supporter supporterMariaUpdate(Long id) {
        Supporter officialSupporter = new Supporter();
        officialSupporter.setId(id);
        officialSupporter.setBirthday(LocalDate.ofYearDay(1991,30));
        officialSupporter.setEmail("maria@test.com");
        officialSupporter.setName("maria maria");
        officialSupporter.setTeamId("spfc");
        return officialSupporter;
    }
}
