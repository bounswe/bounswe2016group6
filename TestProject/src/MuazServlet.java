


import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;





/**
 * Servlet implementation class MuazServlet
 */
@WebServlet("/muaz_ekici")
public class MuazServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   

    public class mountain{
    	String name = "";
    	String country = "";
    	double corx ;
    	double cory ;
    	double point;
    	
    	mountain(String name,double cordx,double cordy,String cntry){
    		this.name = name;
    		this.corx = cordx;
    		this.cory = cordy;
    		this.country = cntry;
    		point = 0;
    	}
    }
    
    
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MuazServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private static ArrayList<mountain> mountains = new ArrayList<mountain>();
    
  private void countryPoint(mountain ref){
	  for(mountain s : mountains){
		  if(s.country.equals(ref.country)){
			  s.point += 100;
		  }
	  }
  }
  private double getDistance (double x1,double y1,double x2,double y2){
	  return (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
  }
  
  private void distancePoint(mountain ref){
	  for(mountain s : mountains){
		  double dist = getDistance(ref.corx, ref.cory,s.corx , s.cory);
		  double mult = (dist*(-1));
		  if(mult == 0){
			  s.point += 1000;
		  }else{
		  s.point += mult;}
	  }
  }
    
  
  private void sort(){
	  
	  int size = mountains.size();
	  for(int i=0;i<size;i++){
		  for(int j= 0 ;j < size-1;j++){
			  int k = j + 1;
			  if(mountains.get(j).point < mountains.get(k).point){
				  mountain tmp = mountains.get(j);
				  mountains.remove(j);
				  if(k >= mountains.size()){
					  mountains.add(tmp);
				  }else
					  mountains.add(k,tmp);
			  }
		  }
	  }
  }
    private void semanticProc (String mt){
    	mountain searchMt = mountains.get(0);
    	for(mountain m : mountains){
    		if(m.name.equalsIgnoreCase(mt)){
    			searchMt = m;
    		}
    	}
    	
    	countryPoint(searchMt);
    	distancePoint(searchMt);
    	sort();
    	
    	
    	
    }
    
    private void procQuery(ResultSet res){
    	System.out.println("procQ");
    	
    	while(res.hasNext()){
    		
    	QuerySolution sol = res.nextSolution();
    	String name = sol.get("?objectLabel").toString();
    	
    	String Point = sol.get("?coord").toString();
    	
    	String country = sol.get("?countryLabel").toString();
    	if (country.contains("@")) {
			country = country.substring(0, country.indexOf('@'));
		}
    	if (name.contains("@")) {
			name = name.substring(0, name.indexOf('@'));
		}
    	
    	Point = Point.substring(Point.indexOf("Point("));
    	double x = Double.parseDouble(Point.substring(Point.indexOf("(")+1, Point.indexOf(' ')));
    	double y = Double.parseDouble(Point.substring(Point.indexOf(' ')+1, Point.indexOf(')')));
    	
    	
    	//System.out.println(name+"   "  +x+"    "+y);
    	this.mountains.add(new mountain(name,x,y,country));
    	
    	
    	}
    	
    }
    
 private String sendData (HttpServletRequest request){
    	
    	System.out.println("send data");
    	String checkedBox = request.getParameter("input");
		String[] cb = checkedBox.split(" ");
		ArrayList<mountain> m = new ArrayList<mountain>();
		for(String s : cb){
			System.out.println(s);
			m.add(this.mountains.get(Integer.parseInt(s)));
		}
    	
    	java.sql.Connection connection;
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection ("jdbc:mysql://ec2-54-186-213-92.us-west-2.compute.amazonaws.com:3306/db","root","pembePanter");
			java.sql.Statement sqlstatement = connection.createStatement();
			StringBuffer sendData = new StringBuffer("INSERT INTO db.Mountains VALUES");
			for(int i=0;i<m.size()-1;i++){
				mountain mnow = m.get(i);
				sendData.append("(\""+mnow.name+"\", \""+mnow.corx+"\", "+mnow.cory+", "+mnow.country+"), "); 
			}
			sendData.append("(\""+m.get(m.size()-1).name+"\", \""+m.get(m.size()-1).corx+"\", "+m.get(m.size()-1).cory+", "+m.get(m.size()-1).country+");");
			System.out.println("**-*-*-*-*-*"+sendData.toString());
			
			sqlstatement.executeUpdate(sendData.toString());
			connection.close();
    	}catch (SQLException e ){
    		e.printStackTrace();
    		return "0";
    	}catch (ClassNotFoundException e){
    		e.printStackTrace();
    		return "0";
    	}
    	return "1";
    }
 
 private String tableData (HttpServletRequest request) {
 	java.sql.Connection connection;
		StringBuffer rawdata = new StringBuffer("");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection ("jdbc:mysql://ec2-54-186-213-92.us-west-2.compute.amazonaws.com:3306/db","root","pembePanter");
			java.sql.Statement statement = connection.createStatement();
			String sqlData = "SELECT * FROM db.Mountains;";
			java.sql.ResultSet data = statement.executeQuery(sqlData);
			
			while(data.next()){
				rawdata.append(data.getString("Name")+"||"+data.getString("CordX")+"||"+data.getString("CordY")+"||"+data.getString("Country")+"&&");
			}
			connection.close();
		}catch (SQLException e) {
			e.printStackTrace();
			return "0";
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "0";
		}		
		return rawdata.toString();
			
 }
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String type = request.getParameter("type");
		if(type == null){
			request.getRequestDispatcher("/WEB-INF/Home.jsp").forward(request, response);			
		}else if(type.equals("queryData")){
			String qq = "PREFIX wikibase: <http://wikiba.se/ontology#>\n"+
				    "PREFIX bd: <http://www.bigdata.com/rdf#>\n"+
					"PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"+
					"PREFIX wd: <http://www.wikidata.org/entity/>\n"+
					"select ?objectLabel ?coord ?countryLabel \n"+
					"where {\n"+
					"	?object wdt:P2044 ?elev	filter(?elev > 7000) .\n"+
					"	?object wdt:P625 ?coord .\n"+
					"   ?object wdt:P17 ?country.\n"+
                    "SERVICE wikibase:label {\n"+
                    "bd:serviceParam wikibase:language \"en\" .\n"+
                    "}\n"+
                    "}LIMIT 100\n";
			Query query = QueryFactory.create(qq); 
			QueryExecution qExecution = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
			ResultSet results = qExecution.execSelect();
			StringBuffer lastData = new StringBuffer("Name||CoorX||CoorY||Country&&");
			procQuery(results);
			semanticProc(request.getParameter("input"));
			for(mountain now : mountains){
				lastData.append(now.name+"||"+now.corx+"||"+now.cory+"||"+now.country+"&&" );
			}
			for(mountain tmp : mountains){
	    		System.out.println(tmp.point);
	    	}
			response.getWriter().write(lastData.toString());
		}else if(type.equals("insertData")){
			response.getWriter().write(sendData(request));
		}else if(type.equals("listData")) {
			response.getWriter().write(tableData(request));
		}else
			System.out.println("error");
			
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
