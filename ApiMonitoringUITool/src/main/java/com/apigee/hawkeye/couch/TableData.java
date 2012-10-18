package com.apigee.hawkeye.couch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;

public class TableData extends Match{
	public static Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
	
	public  String getTableBody() {
		StringBuffer htmlTable=new StringBuffer();
		Map<String, ArrayList<String>> map=retreive();
		Iterator<Map.Entry<String, ArrayList<String>>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	    	htmlTable.append("<tr>");
	        Map.Entry<String, ArrayList<String>> pairs = it.next();
	        htmlTable.append("<td>"+pairs.getKey()+"</td>");
	        ArrayList<String> arr=(ArrayList<String>) pairs.getValue();
	        for(String str:arr){
	        	htmlTable.append("<td class=\"status\">"+str+"</td>");
	        }
	        htmlTable.append("</tr>");
	        it.remove(); 
	    }
		return htmlTable.toString();
	}
	
	
	public    Map<String, ArrayList<String>> retreive() {
		
		
		Session dbSession = new Session("localhost", 5984);

		Database db = dbSession.getDatabase("monitorlog");
        ViewResults vv = db.getAllDocuments();
		
		List<Document> dd = vv.getResults();
		
			for(Document ddr:dd){
			String idinner=ddr.getId();
			
			try {
				Document innerDoc=db.getDocument(idinner);
				String organization=innerDoc.getString("apiOrgName").toUpperCase();
				if(organization!=null&&!map.containsKey(organization)){
					ArrayList< String> values=new ArrayList<String>();
					String storeData=organization+","+innerDoc.getString("apiName").toUpperCase();
					setIdToProp(storeData, idinner);
					values.add(storeData+","+innerDoc.getString("apiStatus").toUpperCase());
					map.put(organization, values);
					
				}
				else if(organization!=null&&map.containsKey(organization))
				{
					ArrayList< String> values=map.get(organization);
					String storeData=organization+","+innerDoc.getString("apiName").toUpperCase();
					setIdToProp(storeData, idinner);
					values.add(storeData+","+innerDoc.getString("apiStatus").toUpperCase());
					map.put(organization, values);
					
				}
				
				
			} catch (IOException e) {
				
				return null;
			}
		}
			return map;
}
	
	
}
