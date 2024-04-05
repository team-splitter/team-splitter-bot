package com.max.team.splitter.core.service;

import com.max.team.splitter.core.model.PlayerStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class PlayerStatService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final JdbcTemplate jdbcTemplate;

    public PlayerStatService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PlayerStat> getPlayerStats(LocalDate startDate, LocalDate endDate) {
        log.info("Get player stats  startDate={}, endDate={}", startDate, endDate);
        List<PlayerStat> stats = jdbcTemplate.query("WITH player_stats_tmp as\n" +
                " (\n" +
                "\t WITH game_stat_results as\n" +
                "\t\t(select id as game_id, case when team_one_scored > team_two_scored then team_one_name when team_one_scored < team_two_scored then team_two_name else 'Draw' end as game_result\n" +
                "\t\t\tfrom game where team_one_name is not null and team_two_name is not null and cast(creation_ts as DATE) >= ? and cast(creation_ts as DATE) <= ?\n" +
                "\t\t)\n" +
                "\tselect p.id as player_id,  p.first_name,  p.last_name,\n" +
                "\t\t sum(case when te.team_name = gsr.game_result then 1 else 0 end ) as total_win,\n" +
                "\t\t sum(case when te.team_name != gsr.game_result and gsr.game_result != 'Draw' then 1 else 0 end) as total_loss,\n" +
                "\t\t sum(case when gsr.game_result = 'Draw' then 1 else 0 end) as total_draw,\n" +
                "\t\t count(DISTINCT g.game_split_id) AS total_days,\n" +
                "\t\tcount(*) total_games\n" +
                "\t from player p\n" +
                "\t\tleft join team_entry te on te.player_id = p.id\n" +
                "        join game g on g.game_split_id = te.game_split_id and (g.team_one_name = te.team_name or g.team_two_name = te.team_name)\n" +
                "        join game_stat_results gsr on g.id=gsr.game_id\n" +
                "\t group by p.id,p.first_name, p.last_name\n" +
                ")\n" +
                " select ps.player_id, ps.first_name, ps.last_name, ps.total_win, ps.total_loss, ps.total_draw, ps.total_games, ps.total_days\n" +
                " from  player_stats_tmp ps", new PlayerStatMapper(), toSqlDate(startDate), toSqlDate(endDate));
        return stats;
    }

    private Date toSqlDate(LocalDate localDate) {
        return new Date(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
    private Timestamp toTimestamp(LocalDate localDate) {
        return Timestamp.from(toInstant(localDate));
    }
    private Instant toInstant(LocalDate localDate) {

        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    static class PlayerStatMapper implements RowMapper<PlayerStat> {
        @Override
        public PlayerStat mapRow(ResultSet rs, int i) throws SQLException {
            PlayerStat model = new PlayerStat();
            //map all fields
            model.setPlayerId(rs.getLong(1));
            model.setFirstName(rs.getString(2));
            model.setLastName(rs.getString(3));
            model.setTotalWin(rs.getInt(4));
            model.setTotalLoss(rs.getInt(5));
            model.setTotalDraw(rs.getInt(6));
            model.setTotalGames(rs.getInt(7));
            model.setTotalDays(rs.getInt(8));
            return model;
        }
    }
}
