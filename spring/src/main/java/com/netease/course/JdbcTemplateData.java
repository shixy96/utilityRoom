package com.netease.course;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateData {
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void creatTable() {
		jdbcTemplate.execute("create table user (id integer, first_name varchar(100), last_name varchar(100));");
	}
	
	public void insertData() {
		jdbcTemplate.update("insert into user values (1, ?, ?)", "Meimei", "Han");
		jdbcTemplate.update("insert into user values (1, ?, ?)", "Lei", "Liu");
	}
	
	public int count() {
		return jdbcTemplate.queryForObject("select count(*) from user", Integer.class);
	}
	
}
