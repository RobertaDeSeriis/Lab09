package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	Map<Integer,Country> idMap;
	List<Border> border;
	
	public List<Border> getBorder() {
		return border;
	}
	
	public Map<Integer, Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		idMap = new HashMap<Integer, Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				idMap.put(rs.getInt("ccode"), new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme")));			
				//System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			}
			
			conn.close();
			return idMap;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//aggiunge come vertici, solo gli stati che hanno confini nell'anno indicato 
	//solo confini via terra --> conttype=1
	public List<Country> loadVertex(int anno ) {
		String sql="SELECT state1no, state2no "
				+ "FROM contiguity "
				+ "WHERE YEAR <= ? AND state1no > state2no AND conttype=1 ";
		
		List<Country> result= new LinkedList<Country>(); 
		border= new LinkedList<Border>(); 
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c1= idMap.get(rs.getInt("state1no"));
				Country c2= idMap.get(rs.getInt("state2no"));
				
				if(!result.contains(c1)) {
				result.add(c1);			
				}
				
				if(!result.contains(c2)) {
					result.add(c2);			
					}
				
				border.add(new Border(c1,c2));
				
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	
	public Map<Country, Integer> statiConfinanti() {
		String sql= "SELECT state1no, COUNT(*) AS num "
				+ "	FROM contiguity "
				+ "WHERE conttype=1"
				+ "	GROUP BY state1no ";
		
		Map <Country, Integer> result= new HashMap<Country, Integer>(); 
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			
			while (rs.next()) {
				result.put(idMap.get(rs.getInt("state1no")),rs.getInt("num"));
				}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
				
				
	/*
	public List<Border> getCountryPairs(int anno) {

		System.out.println("TODO -- BordersDAO -- getCountryPairs(int anno)");
		return new ArrayList<Border>();
	}*/
}
