package com.example.instagram_diana.src.repository;

import com.example.instagram_diana.src.dto.FollowUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FollowDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<FollowUserDto> followingList(long loginUserId, long pageUserId){
        String Query = "SELECT u.userId,u.name,u.username,u.profileUrl,(select true FROM Follow where fromUserId=? AND toUserId=u.userId) followState," +
                "(?=u.userId) equalUserState FROM User u INNER JOIN Follow f ON u.userId=toUserId WHERE f.fromUserId=?;";

        Object[] Params = new Object[]{loginUserId,loginUserId,pageUserId};
        return this.jdbcTemplate.query(Query,
                (rs, rowNum) -> new FollowUserDto(
                        rs.getLong("userId"),
                        rs.getString("name"),
                        rs.getString("userName"),
                        rs.getString("profileUrl"),
                        rs.getInt("followState"),
                        rs.getInt("equalUserState")),
                Params);
    }

    public List<FollowUserDto> followerList(long loginUserId, long pageUserId){
        String Query = "SELECT u.userId,u.name,u.username,u.profileUrl,(select true FROM Follow where toUserId=? AND fromUserId=u.userId) followState," +
                "(?=u.userId) equalUserState FROM User u INNER JOIN Follow f ON u.userId=fromUserId WHERE f.toUserId=?;";

        Object[] Params = new Object[]{pageUserId,loginUserId,pageUserId};
        return this.jdbcTemplate.query(Query,
                (rs, rowNum) -> new FollowUserDto(
                        rs.getLong("userId"),
                        rs.getString("name"),
                        rs.getString("userName"),
                        rs.getString("profileUrl"),
                        rs.getInt("followState"),
                        rs.getInt("equalUserState")),
                Params);
    }

}
