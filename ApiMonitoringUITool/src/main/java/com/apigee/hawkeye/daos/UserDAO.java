package com.apigee.hawkeye.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.apigee.hawkeye.beans.User;

public class UserDAO
{
	Connection con;
	public Connection getConnection()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/apimonitering", "root","root");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
	}
	
	public boolean findUser(User user)
	{
		Connection con=getConnection();
		boolean found=false;
		String query="select * from user where username=? and passwords=?";
		try {
			PreparedStatement pstmt=con.prepareStatement(query);
			pstmt.setString(1, user.getuName());
			pstmt.setString(2, user.getPwd());
			ResultSet res=pstmt.executeQuery();
			if(res.next())
			{
				found=true;
			}
			
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return found;
		
		
	}

}
