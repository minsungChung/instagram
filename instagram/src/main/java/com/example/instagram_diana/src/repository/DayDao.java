package com.example.instagram_diana.src.repository;


import com.example.instagram_diana.src.dto.DayDetailDto;
import com.example.instagram_diana.src.dto.DayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class DayDao {


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public DayDto monthOfDay(long postId){
        String Query = "SELECT MONTH(updatedAt) as month,week(updatedAt) as day FROM Post WHERE postId=?;";
        return this.jdbcTemplate.queryForObject(Query,
                (rs, rowNum) -> new DayDto(
                        rs.getInt("month"),
                        rs.getInt("day")),
                postId);

    }

    public DayDetailDto postDayDetail(Long postId) {
        String Query = "SELECT *," +
                "case when TIMESTAMPDIFF(DAY,P.updatedAt,now()) >=40 then '작년'" +
                "when TIMESTAMPDIFF(DAY,P.updatedAt,now()) >=8 then '%m월 %d일'" +
                "when TIMESTAMPDIFF(DAY,P.updatedAt,now()) >=7 then '지난주'" +
                "when TIMESTAMPDIFF(DAY,P.updatedAt,now()) >=2 then '그제'" +
                "when TIMESTAMPDIFF(DAY,P.updatedAt,now()) >=1 then '어제'" +
                "when TIMESTAMPDIFF(DAY,P.updatedAt,now()) >1 then '오늘'" +
                "when updatedAt > DATE_ADD(now(), INTERVAL -60 minute) then concat(truncate(TIMESTAMPDIFF(MINUTE,updatedAt,now()),0),'분 전') " +
                "when updatedAt > DATE_ADD(now(), INTERVAL -1 DAY) then concat(truncate(TIMESTAMPDIFF(MINUTE,updatedAt,now())/24,0),'시간 전') " +
                "END as dayPoint " +
                "from Post P WHERE P.postId=?";

        return this.jdbcTemplate.queryForObject(Query,
                (rs, rowNum) -> new DayDetailDto(
                        rs.getString("dayPoint")),
                postId);
    }
}
