/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author a
 */
public class ConnectionEquipment {

    private Connection myConnection;
    private Statement statement;
    private final String the_host, the_database, the_username;
    
    
//    private final String the_database, the_username, derDateiname, derDateiname1, derDateiname_d, dieTabelle, dieGerichtstabelle, derGruppenID, derGruppenname, dieAusfuhrfelder;

    /**
     *
     * @param host
     * @param db databasename
     * @param un useraname
     */
    public ConnectionEquipment(String host, String db, String un) {
	the_database = db;
	the_username = un;
        the_host = host;
    }
    
    /**
     *
     * @param password password
     * @return <i>the_username</i> ist an <i>the_database</i> angebunden
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.sql.SQLException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public String connect(String password) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
	String res;
//	Class.forName("com.mysql.jdbc.Driver").newInstance();
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
	myConnection = DriverManager.getConnection("jdbc:mysql://" + the_host + ":3306/"
		+ the_database + "?characterEncoding=utf8", the_username, password);
	statement = myConnection.createStatement();
	res = the_username.concat(" prisijungė prie duombazės „").concat(the_database).concat("“ serveryje ").concat(the_host);
	return res;
    }

    /**
     *
     * @return
     */
    public String disconnect() {
	String res;
	res = "";
	if (myConnection != null) {
	    try {
		myConnection.close();
		res = "Atsijungta nuo duombazės";
	    } catch (SQLException e) {
		res = e.toString();
	    }
	}
	if (statement != null) {
	    try {
		statement.close();
	    } catch (SQLException e) {
		res = e.toString();
	    }
	}

	return res;
    }
    
    /**
     *
     * @param statement
     * @return
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String statement) throws SQLException {
	PreparedStatement ps = myConnection.prepareStatement(statement);
	return ps;
    }

    /**
     *
     * @return
     */
    public String get_username() {
	return the_username;
    }
    
    /**
     *
     * @return
     * @throws SQLException
     */
    public String[][] get_users() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count("Vartotojai")];
	    resultSet = statement.executeQuery("SELECT ID, Pavadinimas FROM Vartotojai ORDER BY Pavadinimas");
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		i++;
	    }
	}
	return result;
	
    }

    /**
     *
     * @param table
     * @param field1
     * @param field2
     * @param orderBy
     * @return [0] – TID, [1] – TName
     * @throws SQLException
     */
    public String[][] getList(String table, String field1, String field2, String orderBy) throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count(table) + 1];
	    resultSet = statement.executeQuery("SELECT " + field1 + ", " + field2 + " FROM ".concat(table).concat(" GROUP BY ").concat(orderBy));//Pavadinimas"));
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		i++;
	    }
	    result[0][i] = "-";
	    result[1][i] = "-";
	}
	return result;
    }
    
    public String[][] getList(String table, String[] fields, String orderByField) throws SQLException {
	int i, l;
        StringBuilder sb;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
            l = fields.length;
	    result = new String[l][get_count(table) + 1];
            sb = new StringBuilder("SELECT ");
            for (int j = 0; j < l; j++) {
                sb.append(fields[j]);
                if (j < l-1) {
                    sb.append(", ");
                }
            }
            sb.append(" FROM ").append(table).append(" ORDER BY ").append(orderByField);
	    resultSet = statement.executeQuery(sb.toString());
	    i = 0;
	    while (resultSet.next()) {
                for (int j = 0; j < l; j++) {
                    result[j][i] = resultSet.getString(j+1);
                }
		i++;
	    }
	    result[0][i] = "-";
	    result[1][i] = "-";
	}
	return result;
    }

    public String[][] getList(String table, String[] fields, String orderByField, String condition) throws SQLException {
	int i, l;
        StringBuilder sb;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
            l = fields.length;
	    result = new String[l][get_count(table, condition) + 1];
            sb = new StringBuilder("SELECT ");
            for (int j = 0; j < l; j++) {
                sb.append(fields[j]);
                if (j < l-1) {
                    sb.append(", ");
                }
            }
            sb.append(" FROM ").append(table).append(" WHERE ").append(condition).append(" ORDER BY ").append(orderByField);
	    resultSet = statement.executeQuery(sb.toString());
	    i = 0;
	    while (resultSet.next()) {
                for (int j = 0; j < l; j++) {
                    result[j][i] = resultSet.getString(j+1);
                }
		i++;
	    }
	    result[0][i] = "-";
	    result[1][i] = "-";
	}
	return result;
    }
    

    public String[] getNetworks() throws SQLException {
        String TABLE = "Potinkliai";
	int i;
	String[] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[get_count(TABLE)];
	    resultSet = statement.executeQuery("SELECT IP FROM ".concat(TABLE));
	    i = 0;
	    while (resultSet.next()) {
		result[i] = resultSet.getString(1);
		i++;
	    }
	}
	return result;
    }
    
    
    /**
     *
     * @param sortFeldname
     * @return
     * @throws SQLException
     */
    public String[][] getEquipment(String sortFeldname) throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[4][get_count("Irenginiai")];
	    resultSet = statement.executeQuery("SELECT * FROM Equipment ORDER BY ".concat(sortFeldname));
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		result[2][i] = resultSet.getString(3);
		result[3][i] = resultSet.getString(4);
		i++;
	    }
	}
	return result;
    }
    
    
    /**
     * @param the_statement
     * @return
     * @throws java.sql.SQLException
     */
    public int executeUpdate(String the_statement) throws SQLException {
	return statement.executeUpdate(the_statement);
    }

    private int get_count(String table) {
	int n;
	ResultSet rs;
	n = 0;
	try {
	    rs = statement.executeQuery("SELECT COUNT(*) FROM ".concat(table));
	    if (rs.next()) {
		n = rs.getInt(1);
	    }
	    rs.close();
	} catch (SQLException | NullPointerException ex) {
	    n = 0;
	}
	return n;
    }

    private int get_count(String table, String condition) {
	int n;
	ResultSet rs;
	n = 0;
	try {
	    rs = statement.executeQuery("SELECT COUNT(*) FROM ".concat(table).concat(" WHERE ").concat(condition));
	    if (rs.next()) {
		n = rs.getInt(1);
	    }
	    rs.close();
	} catch (SQLException | NullPointerException ex) {
	    n = 0;
	}
	return n;
    }

    /**
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public ResultSet executeQuery(String query) throws SQLException {
	ResultSet rs;
	rs = statement.executeQuery(query);
	return rs;
    }
    
    public String[][] getElevators() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[get_count("Liftai")][2];
	    resultSet = statement.executeQuery("SELECT RegNr, Vieta FROM Liftai");
	    i = 0;
	    while (resultSet.next()) {
		result[i][0] = resultSet.getString(1);
		result[i][1] = resultSet.getString(2);
		i++;
	    }
	}
	return result;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public String[][] getTPtypes() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count("TPrusys")];
	    resultSet = statement.executeQuery("SELECT ID, Pavadinimas FROM TPrusys ORDER BY ID");
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		i++;
	    }
	}
	return result;
    }
    
     

}
