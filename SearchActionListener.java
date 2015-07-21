import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;



public class SearchActionListener implements ActionListener {

	private TweetUnitedMainFrame tumf;
	
	public SearchActionListener(TweetUnitedMainFrame t) {
		tumf = t;
	}
	
	
	
	public void fillTable() {
		MongoClient mongoClient = null;
		DBCursor cursor = null;

		try {					
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "TweetUnited");
			DBCollection coll = db.getCollection(tumf.getInputSearch().getText());
			cursor = coll.find(); 
			String[] columnNames = {""};
			DefaultTableModel model = new DefaultTableModel(columnNames, 0);
			

			while(cursor.hasNext()){	
				DBObject obj = cursor.next();
		        String tweet = (String)obj.get("tweet_text");
		        tweet = tweet.replaceAll("https://\\S+\\s?", "");
		        tweet = tweet.replaceAll("http://\\S+\\s?", "");
		        tweet = tweet.replaceAll("#", "");
		        tweet = tweet.replaceAll("[\u0000-\u001f]", "");		        
		        model.addRow(new Object[] { tweet});
			}
			tumf.getMatchRTable().setModel(model); 
			cursor.close(); 
			mongoClient.close();

		}catch (UnknownHostException ex) {
			Logger.getLogger(TweetUnitedMainFrame.class.getName()).log(Level.SEVERE, null, ex);
		}finally{
			if (cursor!= null){
				cursor.close();
			}if (mongoClient != null){
				mongoClient.close();
			}
		}

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.fillTable();
	}
}
