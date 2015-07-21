import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import twitter4j.conf.ConfigurationBuilder;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class TweetUnitedMainFrame {

	protected JFrame frmTw;
	private JTextField InputSearch;
	private JButton btnNewButton;
	private JButton btnUpdate;
	private JTable Fixtures;
	private JScrollPane scrollPane;
	private JTabbedPane tabbedPane;
	private JTable MatchRTable;
	private JScrollPane scrollPane_1;
	private JTable MostFollowers;
	private JScrollPane scrollPane_2;
	private ConfigurationBuilder cb;
	private DB db;
	private DBCollection items;
	private JTextPane txtpnPremierLeague;

	private SearchActionListener sal;


	public void setInputSearch(String text) {
		InputSearch.setText(text);
	}

	public JTable getMatchRTable() {
		return MatchRTable;
	}

	public JTextField getInputSearch() {
		return InputSearch;
	}

	public SearchActionListener getSearchActionListener () {
		return sal;
	}


	public TweetUnitedMainFrame() throws InterruptedException {
		sal = new SearchActionListener(this);
		initialize();
	}

	private void initialize() {
		frmTw = new JFrame();
		frmTw.getContentPane().setBackground(new Color(253, 245, 230));
		frmTw.setTitle("Tweet United FC");
		frmTw.setResizable(false);
		frmTw.setBounds(100, 100, 642, 693);
		frmTw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTw.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("New label");
		Image img = new  ImageIcon(this.getClass().getResource("/tufc.png")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img));
		lblNewLabel.setBounds(6, 0, 133, 103);
		frmTw.getContentPane().add(lblNewLabel);

		btnNewButton = new JButton("Search ");
		btnNewButton.addActionListener(sal);

		btnNewButton.setBounds(373, 29, 117, 29);
		frmTw.getContentPane().add(btnNewButton);

		btnUpdate = new JButton("Display Fixtures");
		btnUpdate.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent e) {

				MongoClient mongoClient = null;
				DBCursor cursor = null;


				try {
					mongoClient = new MongoClient( "localhost" , 27017 );
					DB db = mongoClient.getDB( "TweetUnited");
					DBCollection coll = db.getCollection("Fixtures");
					cursor = coll.find();
					String[] columnNames = {"Home","Away","Date","Time"};
					DefaultTableModel model = new DefaultTableModel(columnNames, 0);

					while(cursor.hasNext()) {
						DBObject obj = cursor.next();
						String ht = (String)obj.get("HomeTeam");
						String at = (String)obj.get("AwayTeam");
						String fd = (String)obj.get("FixtureDate");
						String ft = (String)obj.get("FixtureTime");
						model.addRow(new Object[] {ht,at,fd,ft}); 
					}
					Fixtures.setModel(model); 
					cursor.close(); 
					mongoClient.close();

				}catch (UnknownHostException ex) {
					Logger.getLogger(TweetUnitedMainFrame.class.getName()).log(Level.SEVERE, null, ex);
				} finally{

					if (cursor!= null){
						cursor.close();
					}if (mongoClient != null){
						mongoClient.close();
					}
				}

			}
		});

		btnUpdate.setBounds(111, 634, 146, 29);
		frmTw.getContentPane().add(btnUpdate);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 103, 630, 519);
		frmTw.getContentPane().add(tabbedPane);

		scrollPane = new JScrollPane();
		tabbedPane.addTab("Fixtures", null, scrollPane, null);

		Fixtures = new JTable();
		Fixtures.setShowGrid(false);
		Fixtures.setShowHorizontalLines(false);
		Fixtures.setShowVerticalLines(false);
		Fixtures.setGridColor(Color.WHITE);
		scrollPane.setViewportView(Fixtures);

		scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("Match Report", null, scrollPane_1, null);

		MatchRTable = new JTable();
		MatchRTable.setRowSelectionAllowed(false);
		MatchRTable.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(204, 0, 51)));
		MatchRTable.setShowHorizontalLines(false);
		MatchRTable.setShowGrid(false);
		scrollPane_1.setViewportView(MatchRTable);

		scrollPane_2 = new JScrollPane();
		tabbedPane.addTab("Tweet Football Stats ", null, scrollPane_2, null);

		MostFollowers = new JTable();
		scrollPane_2.setViewportView(MostFollowers);

		txtpnPremierLeague = new JTextPane();
		txtpnPremierLeague.setText("Premier League 2014/15 Official Hashtags \n----------------------------------------\n"
				+ "\nHere is the list of official Premier League Hashtags:\n\n1. "
				+ "#AFC - Arsenal FC\n2. #CFC - Chelsea FC\n3. "
				+ "#MUFC - Manchester United FC\n4. "
				+ "#MFCFC - Manchester City FC \n5. "
				+ "#AVFC - Aston Villa FC \n6. "
				+ "#THFC - Tottenham Hotspurs FC\n7. "
				+ "#EVFC - Everton FC \n8. "
				+ "#LFC - Liverpool FC\n9. "
				+ "#SWANS - Swansea City FC\n10. "
				+ "#QPR - Queens Park Rangers FC \n11. "
				+ "#SAINTSFC - Southampton FC\n12. "
				+ "#CPFC - Crystal Palace FC \n13. "
				+ "#HCAC - Hull City Atheletic FC\n14. "
				+ "#LCFC - Leicester City FC \n15. "
				+ "#NUFC - Newcastle United FC\n16. "
				+ "#SCFC - Stoke City FC\n17. "
				+ "#SAFC - Sunderland Atheltic FC\n18. "
				+ "#WBAFC - West Bromwich Albion FC\n19. "
				+ "#WHUFC - West Ham United FC\n20. "
				+ "#Clarets - Burnley FC \n\n\n");
		txtpnPremierLeague.setEditable(false);
		tabbedPane.addTab("Help", null, txtpnPremierLeague, null);
		
		JButton btnNewButton_1 = new JButton("Display Tweet Stats");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				MongoClient mongoClient = null;
				DBCursor cursor = null;
				
				try {
					mongoClient = new MongoClient( "localhost" , 27017 );
					DB db = mongoClient.getDB( "TweetUnited");
					DBCollection coll = db.getCollection("playerTweetData");
					cursor = coll.find().sort(new BasicDBObject("user_followers",-1));
					String[] columnNames = {"Player Name","Followers"};
					DefaultTableModel model1 = new DefaultTableModel(columnNames, 0);

					while(cursor.hasNext()){	
						DBObject obj = cursor.next();
						String name =  (String)obj.get("user_name");
						String followers = (String)obj.get("user_followers");
						model1.addRow(new Object[] {name , followers}); 
					}

					MostFollowers.setModel(model1); 
					cursor.close(); 
					mongoClient.close();

				}catch (UnknownHostException ex){
					Logger.getLogger(TweetUnitedMainFrame.class.getName()).log(Level.SEVERE, null, ex);
				} finally{
					if (cursor!= null){
						cursor.close();
					}if (mongoClient != null){
						mongoClient.close();
					}
				}
				
			}
		});
		
		btnNewButton_1.setBounds(352, 634, 164, 29);
		frmTw.getContentPane().add(btnNewButton_1);
		
		JLabel label = new JLabel("New label");
		Image img2 = new  ImageIcon(this.getClass().getResource("/tufc.png")).getImage();
		label.setIcon(new ImageIcon(img2));
		label.setBounds(503, 6, 133, 103);
		frmTw.getContentPane().add(label);
		
				InputSearch = new JTextField();
				InputSearch.setHorizontalAlignment(SwingConstants.CENTER);
				InputSearch.setText("e.g. MUFCvMCFC");
				InputSearch.setBounds(141, 28, 222, 28);
				frmTw.getContentPane().add(InputSearch);
				InputSearch.setColumns(10);
				
				JPanel panel = new JPanel();
				panel.setBackground(new Color(153, 0, 51));
				panel.setBounds(0, 0, 642, 103);
				frmTw.getContentPane().add(panel);

	}
}
