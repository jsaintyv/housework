package org.housework.server.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;


@Repository
public class ValueByUserService  extends JdbcDaoSupport  {
	
	
	@Autowired
	DataSource dataSource;
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	
	
	public List<ValueByUser> list(int houseId) {
		System.out.println(houseId);
		List<ValueByUser> list;
		try {
			list = new ArrayList<>();
			try(Connection connection = getDataSource().getConnection()) {
				try(Statement st =  connection.createStatement()) {
					try(ResultSet rs = st.executeQuery(
							"SELECT u.LOGIN, SUM(t.VALUE) as VALUE "
							+ "FROM WORKS w "
							+ " INNER JOIN USERS u ON w.USER_ID = u.ID "
							+ " INNER JOIN TASK_TYPE t ON w.TYPE_ID = t.ID "
							+ " WHERE w.HOUSE_ID =" + houseId
							+ " GROUP BY u.LOGIN")) {
						while(rs.next()) {
							String login = rs.getString(1);
							double d = rs.getDouble(2);
							
							ValueByUser v = new ValueByUser();
							v.setLogin(login);
							v.setValue(d);
							
							System.out.println(login + "=" + d);
							
							list.add(v);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}
}
