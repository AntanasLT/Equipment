/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipment;

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
    
//    public MyConnection(String db, String einDateiname, String einDateiname1, String einDateiname_d, String eineTabelle, String eineGerichtstabelle, String eineGruppenID, String einGruppenname, String un, String Ausfuhrfelder) {
//	the_database = db;
//	the_username = un;
//	derDateiname = einDateiname;
//	derDateiname1 = einDateiname1;
//	derDateiname_d = einDateiname_d;
//	dieGerichtstabelle = eineGerichtstabelle;
//	derGruppenID = eineGruppenID;
//	derGruppenname = einGruppenname;
//	dieTabelle = eineTabelle;
//	dieAusfuhrfelder = Ausfuhrfelder;
//    }    

    /**
     *
     * @param password password
     * @return <i>the_username</i> ist an <i>the_database</i> angebunden
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.sql.SQLException
     * @throws java.lang.IllegalAccessException
     */
    public String connect(String password) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {
	String res;
	Class.forName("com.mysql.jdbc.Driver").newInstance();
	myConnection = DriverManager.getConnection("jdbc:mysql://" + the_host + ":3306/"
		+ the_database + "?characterEncoding=utf8", the_username, password);
	statement = myConnection.createStatement();
	res = the_username.concat(" prisijungė prie duombazės „").concat(the_database).concat("“ serveryje ").concat(the_host);
	return res;
    }

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
    
    public PreparedStatement prepareStatement(String statement) throws SQLException {
	PreparedStatement ps = myConnection.prepareStatement(statement);
	return ps;
    }

    public String get_username() {
	return the_username;
    }
    
    public String[][] get_users() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count("Vartotojai")];
	    resultSet = statement.executeQuery("SELECT ID, Vardas FROM Vartotojai ORDER BY Vardas");
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
     * @return [0] – TID, [1] – TName
     * @throws SQLException
     */
    public String[][] getEquipmentTypes() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count("IrangosTipai")];
	    resultSet = statement.executeQuery("SELECT ID, Pavadinimas FROM IrangosTipai ORDER BY Pavadinimas");
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		i++;
	    }
	}
	return result;
    }

    public String[][] getSystems() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count("Sistemos")];
	    resultSet = statement.executeQuery("SELECT ID, Pavadinimas FROM Sistemos ORDER BY Pavadinimas");
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		i++;
	    }
	}
	return result;
    }

    public String[][] getWorkTypes() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count("Darbotipis")];
	    resultSet = statement.executeQuery("SELECT ID, Pavadinimas FROM Darbotipis ORDER BY Pavadinimas");
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		i++;
	    }
	}
	return result;
    }

    public String[][] getStates() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count("Busenos")];
	    resultSet = statement.executeQuery("SELECT ID, Busena FROM Busenos ORDER BY Busena");
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		i++;
	    }
	}
	return result;
    }

    public String[][] getLocations() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count("Vietos")];
	    resultSet = statement.executeQuery("SELECT ID, Pavadinimas FROM Vietos ORDER BY Pavadinimas");
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		i++;
	    }
	}
	return result;
    }

    public String[][] getCodes() throws SQLException {
	int i;
	String[][] result;
	ResultSet resultSet;
	result = null;
	if (myConnection != null) {
	    result = new String[2][get_count("Veiklos")];
	    resultSet = statement.executeQuery("SELECT ID, Pavadinimas FROM Veiklos ORDER BY Pavadinimas");
	    i = 0;
	    while (resultSet.next()) {
		result[0][i] = resultSet.getString(1);
		result[1][i] = resultSet.getString(2);
		i++;
	    }
	}
	return result;
    }

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
     * @return
     * @throws java.sql.SQLException
     */
    public int executeUpdate(String the_statement) throws SQLException {
	return statement.executeUpdate(the_statement);
    }

//    public void fuehre_aus() throws SQLException {
//	statement.executeQuery("SELECT ".concat(dieAusfuhrfelder).concat(" FROM ").concat(dieTabelle).concat(" INTO OUTFILE '").concat(derDateiname).concat("'"));
//    }

//    /**
//     *
//     * @param dieAnweisung
//     * @throws SQLException
//     */
//    public void fuehrt_aus(String dieAnweisung) throws SQLException {
//	statement.executeQuery(dieAnweisung);
//    }

//    /**
//     *
//     * @param dieAnweisung
//     * @throws SQLException
//     */
//    public void fuehrt_ein(String dieAnweisung) throws SQLException {
//	statement.executeUpdate(dieAnweisung);
//    }
//
//    /**
//     *
//     * @param derTabellenname
//     * @param derAusfuhrdateiname
//     * @throws SQLException
//     */
//    public void fuehre_aus(String derTabellenname, String derAusfuhrdateiname) throws SQLException {
//	statement.executeQuery("SELECT * FROM ".concat(derTabellenname).concat(" INTO OUTFILE '").concat(derAusfuhrdateiname).concat("'"));
//    }
//
//    /**
//     *
//     * @param abDatum
//     * @throws SQLException
//     */
//    public void fuehre_aus_ab(String abDatum) throws SQLException {
//	statement.executeQuery("SELECT ".concat(dieAusfuhrfelder).concat(" FROM ").concat(dieTabelle).concat(" WHERE VdieZeit >= '").concat(abDatum).concat("' INTO OUTFILE '").concat(derDateiname1).concat("'"));
//    }
//
//    /**
//     *
//     * @param dasDatum
//     * @throws SQLException
//     */
//    public void fuehre_aus_am(String dasDatum) throws SQLException {
//	statement.executeQuery("SELECT ".concat(dieAusfuhrfelder).concat(" FROM ").concat(dieTabelle).concat(" WHERE VdieZeit LIKE '").concat(dasDatum).concat("%' INTO OUTFILE '").concat(derDateiname_d).concat("'"));
//    }
//
//    public int loescht_alles() throws SQLException {
//	return statement.executeUpdate(("TRUNCATE ").concat(dieTabelle));
//    }
//
//    public int loescht_alles_aus_derTabelle(String derTabellenname) throws SQLException {
//	return statement.executeUpdate(("TRUNCATE ").concat(derTabellenname));
//    }
//
//    public int loescht_entsprechend_demDatum(String einDatum) throws SQLException {
//	return statement.executeUpdate(("DELETE FROM ").concat(dieTabelle).concat(" WHERE VdieZeit LIKE '%").concat(einDatum).concat("%'"));
//    }
//
//    public int loescht_ab_Datum(String einDatum) throws SQLException {
//	return statement.executeUpdate(("DELETE FROM ").concat(dieTabelle).concat(" WHERE VdieZeit >= '").concat(einDatum).concat("'"));
//    }
//
//    public int loescht_aus_derTabelle(String dieAnweisung) throws SQLException {
//	return statement.executeUpdate(dieAnweisung);
//    }
//
    /**
     * Es wird versucht dieVerzerrung.dat und dieVerzerrung1.dat einzuführen
     *
     * @param einDateiname
     * @return die Anzahl der Datensätze; falls wurde keine Datei gefunden –
     * eine Fehlermeldung
     * @throws SQLException
     */
//    public String fuehre_ein_in_eineTabelle(String einDateiname) throws SQLException {
//	String eingefuehrt;
//	eingefuehrt = "";
//	if (Files.exists(Paths.get(derDateiname))) {
//            loescht_alles_aus_derTabelle();
//	    eingefuehrt = fuehre_ein_in_eineTabelle(derDateiname);
//	}
//	if (Files.exists(Paths.get(derDateiname_d))) {
//	    eingefuehrt = eingefuehrt.concat(fuehre_ein_in_eineTabelle(derDateiname_d));
//	}
//	if (Files.exists(Paths.get(derDateiname1))) {
//	    eingefuehrt = eingefuehrt.concat(fuehre_ein_in_eineTabelle(derDateiname1));
//	}
//	if (eingefuehrt.isEmpty()) {
//	    eingefuehrt = "Es wurde weder ".concat(derDateiname).concat(" noch ").concat(derDateiname1).concat(" gefunden!");
//	}
//        return eingefuehrt;
//    }
//    public String fuehre_ein_in_eineTabelle(String einDateiname) throws SQLException {
//	String eingefuehrt;
//	eingefuehrt = String.valueOf(statement.executeUpdate("LOAD DATA INFILE '".concat(einDateiname).concat("' INTO TABLE ").concat(dieTabelle).concat(" (").concat(dieAusfuhrfelder).concat(")")));
//	if (eingefuehrt.isEmpty()) {
//	    eingefuehrt = "Es wurde weder ".concat(derDateiname).concat(" noch ").concat(derDateiname1).concat(" gefunden!");
//	}
//	return eingefuehrt;
//    }
//
//    public String fuehre_ein(String derTabellenname, String derAusfuhrdateiname) throws SQLException {
//	String eingefuehrt;
//	statement.executeUpdate(("TRUNCATE ").concat(derTabellenname));
////    eingefuehrt = String.valueOf(statement.executeUpdate("LOAD DATA INFILE '/home/a/Dokumente/Dateisicherungen/pagr.dat' INTO TABLE pagr (pdata, ppavad, pkiekis, pkaina, pskyrius)"));
//	eingefuehrt = String.valueOf(statement.executeUpdate("LOAD DATA INFILE '".concat(derAusfuhrdateiname).concat("' INTO TABLE ").concat(derTabellenname)));
//	return eingefuehrt;
//    }

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

//    public String bekommt_letzteGID() {
//	String res;
//	ResultSet resultSet;
//	res = "";
//	try {
//	    resultSet = statement.executeQuery("SELECT GID FROM dasGericht ORDER BY GID DESC LIMIT 1");
//	    if (resultSet.next()) {
//		res = resultSet.getString("GID");
//	    }
//	    resultSet.close();
//	} catch (SQLException | NullPointerException ex) {
//	    res = ex.getMessage();
//	}
//	return res;
//    }

    public ResultSet executeQuery(String query) throws SQLException {
	ResultSet rs;
	rs = statement.executeQuery(query);
	return rs;
    }

    String[][] getTPtypes() throws SQLException {
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

//    private String die_anzahl_der_tage(String dasdatum, String dieTabelle) {
//	return "SELECT COUNT( DISTINCT DATE_FORMAT( VdieZeit, '%Y-%m-%d' )) FROM " + dieTabelle + " WHERE VdieZeit LIKE '" + dasdatum + "%'";
//    }
//
//    public int bekomme_die_anzahl_der_tage(String dasdatum, String dieTabelle) throws SQLException {
//	Statement stmt1 = null;
//	ResultSet resultSet = null;
//	int res;
//	res = 0;
//	stmt1 = myConnection.createStatement();
//	resultSet = stmt1.executeQuery(die_anzahl_der_tage(dasdatum, dieTabelle));
//	resultSet.next();
//	res = Integer.valueOf(String.valueOf(resultSet.getObject(1)));
//	resultSet.close();
//	stmt1.close();
//	return res;
//    }

    /**
     *
     * @param gid
     * @return 0 – GdieEiweisse,<br>1 – GdieFette,<br>2 –
     * GdieKohlenhydrate,<br>3 – GdieAnmerkung,<br>4 – kcal
     * @throws SQLException
     */
//    public String[] bekommt_dieGerichtsangaben(String gid) throws SQLException {
//	String[] res;
//	ResultSet resultSet;
//	res = new String[6];
//	resultSet = statement.executeQuery("SELECT GdieEiweisse, GdieFette, GdieKohlenhydrate, GdieAnmerkung, GdieBenennung FROM dasGericht WHERE GID=".concat(gid));
//	while (resultSet.next()) {
//	    res[0] = String.valueOf(resultSet.getObject("GdieEiweisse"));
//	    res[1] = String.valueOf(resultSet.getObject("GdieFette"));
//	    res[2] = String.valueOf(resultSet.getObject("GdieKohlenhydrate"));
//	    res[3] = String.valueOf(resultSet.getObject("GdieAnmerkung"));
//	    res[4] = String.valueOf(Math.round((Float.parseFloat(res[0]) + Float.parseFloat(res[2])) * 4.1 + Float.parseFloat(res[1]) * 9.3));
//	    res[5] = String.valueOf(resultSet.getObject("GdieBenennung"));
//	}
//	resultSet.close();
//	return res;
//    }

}
