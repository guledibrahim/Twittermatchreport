
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.awt.EventQueue;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.JFrame;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;




public class TweetStreamMongoDB{

	protected ConfigurationBuilder cb;
	private DB db;
	private static DBCollection items;
	private static DBCollection playerstats;
	private TweetUnitedMainFrame window;
	private Twitter twitter;
	
	public TweetStreamMongoDB() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new TweetUnitedMainFrame();
					window.frmTw.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main (String args[]) throws TwitterException{

		TweetStreamMongoDB twitter = new TweetStreamMongoDB();

		try {
			twitter.TwitterStream();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

	}


	public void TwitterStream()throws InterruptedException{

		System.out.println("Search\t");
		Scanner input = new Scanner(System.in);
		String search = input.nextLine();
		
		

		connectdb(search);

		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("sKVHhb4HeDZlNkqPBufhISLIP");
		cb.setOAuthConsumerSecret("QNlAHXXP14prUl2fly7K1Z5cHLs4JlPJ3zoYKIM9zPVAthkW68");
		cb.setOAuthAccessToken("3013389983-RPTIpMiHIgmYSk7HhQDBuCODBVzU7IsscT1eL68");
		cb.setOAuthAccessTokenSecret("XXvhXxadXks4oDtajPq4CC6x25XtyIFdZUEFMRp8Zdwvw");
		
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		StatusListener listener = new StatusListener(){
			
				public void onStatus(Status status){	
				System.out.println(status.getText() + "" + status.getUser().getCreatedAt());
				
		
				if (status.isRetweet()==false) {
					BasicDBObject basicObj = new BasicDBObject();
					basicObj.put("tweet_ID", status.getId());
					basicObj.put("tweet_text", status.getText());
					basicObj.put("tweet_created", status.getCreatedAt());

					

					
					try{
						items.insert(basicObj);
					}catch(Exception e){
						System.out.println("MongoDB Connection Error : " + e.getMessage());
					} 

					window.getSearchActionListener().fillTable();
				}
			}
			
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice)
			{
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) 
			{
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}
			public void onScrubGeo(long userId, long upToStatusId)
			{
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}
			@Override
			public void onStallWarning(StallWarning stallWarning) 
			{
                //To change body of implemented methods use File | Settings | File Templates.
			}
			public void onException(Exception ex) 
			{
				ex.printStackTrace();
			}

		};
		
			FilterQuery fq = new FilterQuery();
			String keywords[] = {search};
			fq.track(keywords);
			fq.language(new String[]{"en"});
			twitterStream.addListener(listener);
			twitterStream.filter(fq);		
	}


	public void connectdb(String search){

		try {
			initMongoDB();
			items = db.getCollection(search);
			playerstats = db.getCollection("playerTweetData");
			BasicDBObject index = new BasicDBObject("tweet_ID", 1);
			items.createIndex(index, new BasicDBObject("unique", true));
		}catch (MongoException ex) {
			System.out.println("MongoException :" + ex.getMessage());
		}
		window.setInputSearch(search);
	}
	
	
	public void initMongoDB() throws MongoException {
		try { 
			System.out.println("Connecting to Mongo DB..");
			Mongo mongo;
			mongo = new Mongo("127.0.0.1");
			db = mongo.getDB("TweetUnited"); 
		}catch (UnknownHostException ex) {
			System.out.println("MongoDB Connection Error :" + ex.getMessage());
		}
	}

}





