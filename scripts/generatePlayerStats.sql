truncate table player_stats;

insert into player_stats

WITH player_stats_tmp as
 (
	 WITH game_stat_results as
		(select id as game_id, case when team_one_scored > team_two_scored then team_one_name when team_one_scored < team_two_scored then team_two_name else 'Draw' end as game_result
			from game where team_one_name is not null and team_two_name is not null
		)
	select p.id as player_id,  p.first_name,  p.last_name,
		 sum(case when te.team_name = gsr.game_result then 1 else 0 end ) as total_win,
		 sum(case when te.team_name != gsr.game_result and gsr.game_result != 'Draw' then 1 else 0 end) as total_loss,
		 sum(case when gsr.game_result = 'Draw' then 1 else 0 end) as total_draw,
		count(*) total_games
	 from player p
		left join team_entry te on te.player_id = p.id
        join game g on g.game_split_id = te.game_split_id
        join game_stat_results gsr on g.id=gsr.game_id
	 group by p.id,p.first_name, p.last_name
)
 select ps.player_id, ps.first_name, ps.last_name, ps.total_win, ps.total_loss, ps.total_draw, ps.total_games
 from  player_stats_tmp ps;