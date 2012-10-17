/**
 * @author Vipin@RareMile
 * This class is written
 */
package com.couch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import com.fourspaces.couchdb.Session;

public class connection extends Match {
	public StringBuffer listcolour = new StringBuffer();
	public ArrayList<String> responseData = new ArrayList<String>();

	public String retreive(String idIndicator) {
		Session dbSession = new Session("localhost", 5984);

		Database db = dbSession.getDatabase("monitorlog");

		String id = null;
		if (id == null) {
			id = getId(idIndicator);
		}
		try {
			Document innerDoc = db.getDocument(id);
			return innerDoc.getString("apiName").toUpperCase() + "_"
					+ innerDoc.getString("apiStatus");

		} catch (IOException e) {

			return null;
		}
	}

	/**
	 * Data requried for plotting bars on x and y axis
	 * 
	 * @param id
	 * @param field1
	 * @param field2
	 * @return string
	 */
	public String getArrayData(String idIndicator, String field1, String field2) {

		StringBuilder list = new StringBuilder();
		Session dbSession = new Session("localhost", 5984);
		HtpClientJson httpcall = new HtpClientJson();
		Database db = dbSession.getDatabase("monitorlog");

		int threshold = 0;

		String id = getIdFromProp(idIndicator);

		if (id == null) {
			getId(idIndicator);
		}

		try {
			String respondstring = httpcall
					.getResponseString("http://localhost:5984/monitorlog/" + id
							+ "?revs_info=true");
			JSONObject json = new JSONObject(respondstring);
			JSONArray jarr = json.getJSONArray("_revs_info");
			for (int i = 0; i < jarr.length(); i++) {
				if (jarr.getJSONObject(i).get("status").equals("available")) {
					threshold++;
					String revision = jarr.getJSONObject(i).get("rev")
							.toString();
					Document dfe = db.getDocument(id, revision);
					String allListData = "['" + dfe.getString(field1).trim()
							+ "'" + "," + dfe.getInt(field2) + "]";
					list.append(allListData);
					getColor(dfe.getString("apiStatus"), dfe);
					list.append(",");
					listcolour.append(",");
					
					if(threshold==50){
						break;
					}
				}

			}

		} catch (IOException e) {
			return null;
		} catch (JSONException e) {

			e.printStackTrace();
		}
		list.deleteCharAt(list.length()-1);
		return list.toString();
	}

	/**
	 * putting colour of bars based on the status of the api
	 * 
	 * @param status
	 * @param dfe
	 */
	public void getColor(String status, Document dfe) {
		if (status.equals("NORMAL")) {
			listcolour.append("'#66CD00'");
		}
		if (status.equals("POTENTIALLYDOWN")) {
			listcolour.append("'#FF3030'");
			responseData.add("['" + dfe.getString("timeStamp").trim() + "'"
					+ "," + "'" + "responseCode="
					+ dfe.getString("responseCode") + "Response="
					+ dfe.getString("apiResponse") + "']");
		}
		if (status.equals("SLOW")) {
			listcolour.append("'#FFE303'");
		}
		if (status.equals("NORESPONSE")) {
			listcolour.append("'#800000'");
		}
	}

	public String getcolorArray() {
		listcolour.deleteCharAt(listcolour.length()-1);
		return listcolour.toString();
	}

	/**
	 * giving data for mousehover in an string
	 * 
	 * @return
	 */
	public String getMouseOver() {
		StringBuffer datcol = new StringBuffer();
		if (responseData.size() > 0) {
			for (int i = 0; i < responseData.size() - 1; i++) {
				datcol.append(responseData.get(i));
				datcol.append(",");
			}
			datcol.append(responseData.get(responseData.size() - 1));
			return datcol.toString();
		}
		return null;
	}

	/*
	 * 
	 * getting the id from the database
	 */
	public String getId(String idIndicator) {
		String[] twodat = idIndicator.split(",");

		Session dbSession = new Session("localhost", 5984);

		Database db = dbSession.getDatabase("monitorlog");
		ViewResults vv = db.getAllDocuments();

		List<Document> dd = vv.getResults();
		for (Document ddr : dd) {

			String idinner = ddr.getId();

			try {
				Document innerDoc = db.getDocument(idinner);

				if (innerDoc.getString("apiOrgName")
						.equalsIgnoreCase(twodat[0])
						&& innerDoc.getString("apiName").equalsIgnoreCase(
								twodat[1])) {
					// setIdToProp(idIndicator, idinner);
					return idinner;
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return null;

	}

	

}
