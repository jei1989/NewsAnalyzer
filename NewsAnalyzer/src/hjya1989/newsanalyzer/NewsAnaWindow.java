package hjya1989.newsanalyzer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import hjya1989.newsanalyzer.nlp.NLPRequest;
import hjya1989.newsanalyzer.nlp.NLPWriter;
import hjya1989.newsanalyzer.util.Log;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JInternalFrame;
import javax.swing.ScrollPaneConstants;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.beans.PropertyChangeEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.event.TreeExpansionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Frame;

import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import datechooser.beans.DateChooserCombo;
import datechooser.beans.DateChooserPanel;
import datechooser.beans.DateChooserDialog;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class NewsAnaWindow extends JFrame {

	private NewsAnaMain newsAnaMain;
	private JPanel contentPane;
	public JTree tree;
	public DefaultTreeModel treeModel;
	public JTree rssTree;
	public DefaultTreeModel rssTreeModel;
	
	public DefaultMutableTreeNode rootTreeNode;
	public DefaultMutableTreeNode grandTreeNode;
	public DefaultMutableTreeNode motherTreeNode;

	public DefaultMutableTreeNode rootRssTreeNode;
	public DefaultMutableTreeNode grandRssTreeNode;
	public DefaultMutableTreeNode motherRssTreeNode;
	
	public JEditorPane editorPane;
	
	public JButton btnTwitterstart;
	public JButton btnRSSstart;
	
	public JTextPane textPane;
	public JTextPane textPaneRequest;
	public JButton btnNLPRequest;
	public JButton btnNLPStart;
	
	public DateChooserCombo dateChooserStart;
	public DateChooserCombo dateChooserEnd;
	public JCheckBox chkboxDateCurrent;
	
	public StringBuffer strbuff;
	private int caretStart = 0;
	private int caretEnd;
	public JTextField txtLastChkTime;
	
	private JComboBox comboBox;
	
	private Calendar cal;
	/**
	 * @wbp.nonvisual location=134,219
	 */
	
	
	/**
	 * Launch the application.

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewsAnalyzerMain frame = new NewsAnalyzerMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	 */
	/**
	 * Create the frame.
	 */
	public NewsAnaWindow(NewsAnaMain newsAnaMain) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try{
					newsAnaMain.setProperty("LastCheckTime",txtLastChkTime.getText().trim());
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
				try{
					newsAnaMain.setProperty("StartCheckTime",dateChooserStart.getText().trim());
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
				try{
					newsAnaMain.setProperty("EndCheckTime",dateChooserEnd.getText().trim());
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
				try{
					newsAnaMain.setProperty("TextPaneRequest",textPaneRequest.getText().trim());
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				try{
					txtLastChkTime.setText( newsAnaMain.getProperty("LastCheckTime") );
				}catch(Exception ex){
					Log.errorLog(this, "windowOpened :: " + ex);
				}	
				try{
					//dateChooserStart.setText( newsAnaMain.getProperty("StartCheckTime") );
					cal.setTime( df.parse( newsAnaMain.getProperty("StartCheckTime")) );
					dateChooserStart.setSelectedDate( cal );
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
				try{
					cal.setTime( df.parse( newsAnaMain.getProperty("EndCheckTime")) );
					dateChooserEnd.setSelectedDate( cal );
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
				try{
					textPaneRequest.setText( newsAnaMain.getProperty("TextPaneRequest") );
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
				
				//btnRSSstart.doClick();
				doBtnRssParsing();
				
			}
		});
		setTitle("New Analyzer V1.0");
		
		this.newsAnaMain = newsAnaMain;
		this.strbuff = new StringBuffer();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		
		setLocation((int)(dim.getWidth()/2 - this.getWidth()/2), (int)(dim.getHeight()/2 - this.getHeight()/2));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setPreferredSize(new Dimension(189, 600));
		splitPane.setLeftComponent(splitPane_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setMinimumSize(new Dimension(230, 23));
		splitPane_1.setLeftComponent(scrollPane_2);
		
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(230, 10));
		panel.setPreferredSize(new Dimension(200, 20));
		scrollPane_2.setViewportView(panel);
	
		panel.setLayout(new MigLayout("", "[120px,grow][5px][103px,grow]", "[10px][25px][21px][21px][21px][23px][23px]"));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Check Duration", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 139)));
		panel_5.setBackground(new Color(135, 206, 250));
		panel.add(panel_5, "cell 0 0 3 1,aligny top");
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_4.setForeground(Color.WHITE);
		panel_5.add(panel_4);
		panel_4.setBackground(Color.WHITE);
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.setLayout(new MigLayout("", "[][][grow]", "[grow][][][][]"));
		
		JLabel lblNewLabel = new JLabel("Start Date");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		panel_4.add(lblNewLabel, "cell 0 0");
		
		dateChooserStart = new DateChooserCombo();
		dateChooserStart.setFieldFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		dateChooserStart.setNavigateFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		dateChooserStart.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		panel_4.add(dateChooserStart, "cell 2 0,grow");
		
		dateChooserEnd = new DateChooserCombo();
		dateChooserEnd.setFieldFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		dateChooserEnd.setNavigateFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		dateChooserEnd.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		panel_4.add(dateChooserEnd, "cell 2 1,grow");
		
		JLabel lblNewLabel_1 = new JLabel("End Date");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		panel_4.add(lblNewLabel_1, "cell 0 1");
		
		chkboxDateCurrent = new JCheckBox("Continue   ");
		chkboxDateCurrent.setSelected(true);
		chkboxDateCurrent.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if( chkboxDateCurrent.isSelected() ){
					Date date = Calendar.getInstance().getTime();
					//dateChooserEnd.setCurrent(date);
					dateChooserEnd.setEnabled(false);
					dateChooserEnd.updateUI();
					
				}else{
					
					dateChooserEnd.setEnabled(true);
				}
				
			}
		});
		panel_4.add(chkboxDateCurrent, "cell 0 2 3 1,alignx right");
		
		btnTwitterstart = new JButton("TwitterStart");
		btnTwitterstart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newsAnaMain.twitterParsingStart();
			}
		});
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(new Color(255, 222, 173));
		panel_6.setBorder(new TitledBorder(null, "RSS Feeder", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_6, "cell 0 1 3 5");
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.WHITE);
		panel_6.add(panel_7);
		panel_7.setLayout(new MigLayout("", "[grow][]", "[][][]"));
		
		btnRSSstart = new JButton("RssParseStart");
		panel_7.add(btnRSSstart, "flowy,cell 0 0,growx");
		
		btnRSSstart.setBackground(UIManager.getColor("Button.light"));
		btnRSSstart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				doBtnRssParsing();
				
			}
		});
		btnRSSstart.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		
		JLabel lblNewLabel_2 = new JLabel(": RSS Parsing Set");
		panel_7.add(lblNewLabel_2, "cell 1 0");
		lblNewLabel_2.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 10));
		
		JButton btnNLPParse = new JButton("NLPParseStart");
		btnNLPParse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if(btnNLPParse.getText().contains("Start")){
					
					btnNLPParse.setForeground(Color.GRAY);
					btnNLPParse.setText("NLPParseStop");
					newsAnaMain.NLPAnalyzerStart();

				}else if(btnNLPParse.getText().contains("Stop")){

					btnNLPParse.setForeground(Color.BLACK);
					btnNLPParse.setText("NLPParseStart");
					newsAnaMain.NLPAnalyzerStop();
				}
				
			}
		});
		btnNLPParse.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		panel_7.add(btnNLPParse, "cell 0 1,growx");
		
		JLabel lblNlpParse = new JLabel(": NLP Parse Set");
		lblNlpParse.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 10));
		panel_7.add(lblNlpParse, "cell 1 1");
		
		txtLastChkTime = new JTextField();

		panel_7.add(txtLastChkTime, "cell 0 2 2 1,growx");
		txtLastChkTime.setColumns(10);
		panel.add(btnTwitterstart, "cell 0 6,growx,aligny center");
		
		JButton btnRssanalyzer = new JButton("RssAnalyzer");
		btnRssanalyzer.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		btnRssanalyzer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				newsAnaMain.RSSFeedAnalyzerStart();
				
			}
		});
		panel.add(btnRssanalyzer, "cell 2 6,alignx center,aligny center");
		
		JPanel panel_1 = new JPanel();
		splitPane_1.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_1.add(tabbedPane);
		
		JSplitPane splitPane_3 = new JSplitPane();
		tabbedPane.addTab("RSS Feed", null, splitPane_3, null);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setMinimumSize(new Dimension(200, 20));
		scrollPane_3.setPreferredSize(new Dimension(300, 20));
		splitPane_3.setLeftComponent(scrollPane_3);
		
		rssTree = new JTree();
		rssTree.setPreferredSize(new Dimension(20, 60));
		rssTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				try{
					if(rssTree.getSelectionPath().getPath().length >= 4 ){
						
						String writeText = editorPane.getText() + rssTree.getSelectionPath().getPathComponent(1).toString() + " :: (" + rssTree.getSelectionPath().getPathComponent(2).toString() + ")\r\n" + rssTree.getSelectionPath().getPathComponent(3).toString() + "\r\n" + "--------------------\r\n";
						//editorPane.setText( editorPane.getText() + tree.getSelectionPath().getPathComponent(1).toString() + " :: (" + tree.getSelectionPath().getPathComponent(2).toString() + ")\r\n" + tree.getSelectionPath().getPathComponent(3).toString() + "\r\n" + "--------------------\r\n" );
						//editorPane.setText("\r\n");
						editorPane.setText( writeText );
						
					}
				}catch(Exception ex)
				{
					Log.errorLog(this, "rssTree MouseClicked :: " + ex);
				}				
				
			}
		});
		rssTreeModel = (DefaultTreeModel)rssTree.getModel();
		
		scrollPane_3.setViewportView(rssTree);
		
		JPanel panel_2 = new JPanel();
		
		splitPane_3.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane_4 = new JSplitPane();
		panel_2.add(splitPane_4);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		splitPane_4.setLeftComponent(scrollPane_5);
		
		textPane = new JTextPane();
		textPane.setBackground(Color.BLACK);
		textPane.setForeground(Color.WHITE);
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				/********************
				if( e.getKeyCode() == e.VK_ENTER ){
					//String temp = textPane.getText() + "\r\n";
					//textPane.setText( temp + e.getKeyChar() );
				}else{
					System.out.println( e.getKeyChar() );
				}
				/********************/
			}
			@Override
			public void keyPressed(KeyEvent e) {
				/********************
				if( e.getKeyCode() != e.VK_ENTER )
				{
					if( caretStart == 0 ){
						caretStart = textPane.getCaretPosition();
					}
					caretEnd = textPane.getCaretPosition();
				}else if( e.getKeyCode() == e.VK_ENTER ){
					try {
						String temp = textPane.getText(caretStart, caretEnd-caretStart);
						textPane.setText( textPane.getText() + "\r\n \\>");
						textPane.setCaretPosition(caretEnd-1);
						caretStart = 0;
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				//System.out.println( textPane.getCaretPosition() );
				/********************/
			}
		});
		textPane.setPreferredSize(new Dimension(500, 21));
		scrollPane_5.setViewportView(textPane);
		
		JPanel panel_3 = new JPanel();
		splitPane_4.setRightComponent(panel_3);
		panel_3.setLayout(new MigLayout("", "[grow]", "[][][grow][][][][][][][][][][][][][][][][]"));
		
		btnNLPStart = new JButton("Parsing NLP");
		btnNLPStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				NLPWriter nlpwriter = new NLPWriter();
				nlpwriter.setNLPMessage(textPane.getText());
				new Thread(nlpwriter).start();
				
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.errorLog(this, "btnNLPStart :: " + e.toString());
				}
				
				String msg = textPane.getText() + "\r\n===============\r\n" + nlpwriter.getNLPRequestMsg();
				textPane.setText( msg );
				
			}
		});
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"max", "recent"}));
		panel_3.add(comboBox, "cell 0 0,growx");
		panel_3.add(btnNLPStart, "cell 0 1,alignx center");
		
		JScrollPane scrollPane_6 = new JScrollPane();
		panel_3.add(scrollPane_6, "cell 0 2 1 2,grow");
		
		textPaneRequest = new JTextPane();
		scrollPane_6.setViewportView(textPaneRequest);
		
		btnNLPRequest = new JButton("Request NLP");
		btnNLPRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				doNLPRequest();
				
				
			}
		});
		panel_3.add(btnNLPRequest, "cell 0 4,alignx center");
		
		JSplitPane splitPane_2 = new JSplitPane();
		tabbedPane.addTab("New tab", null, splitPane_2, null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setPreferredSize(new Dimension(300, 20));
		splitPane_2.setLeftComponent(scrollPane_1);
		
		tree = new JTree();
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try{
					if(tree.getSelectionPath().getPath().length == 4 ){
						
						String writeText = editorPane.getText() + tree.getSelectionPath().getPathComponent(1).toString() + " :: (" + tree.getSelectionPath().getPathComponent(2).toString() + ")\r\n" + tree.getSelectionPath().getPathComponent(3).toString() + "\r\n" + "--------------------\r\n";
						//editorPane.setText( editorPane.getText() + tree.getSelectionPath().getPathComponent(1).toString() + " :: (" + tree.getSelectionPath().getPathComponent(2).toString() + ")\r\n" + tree.getSelectionPath().getPathComponent(3).toString() + "\r\n" + "--------------------\r\n" );
						//editorPane.setText("\r\n");
						editorPane.setText( writeText );
						
					}
				}catch(Exception ex)
				{
					Log.errorLog(this, "tree MouseClicked :: " + ex);
				}
				
			}
		});
		//editorPane.setText( editorPane.getText() + tree.getSelectionPath().getPathComponent(1).toString() + " :: (" + tree.getSelectionPath().getPathComponent(2).toString() + ")\r\n" + tree.getSelectionPath().getPathComponent(3).toString() + "\r\n" + "--------------------\r\n" );
		//tree.setPreferredSize(new Dimension(200, 20));
		treeModel = (DefaultTreeModel)tree.getModel();
		
		scrollPane_1.setViewportView(tree);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		splitPane_2.setRightComponent(scrollPane_4);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		editorPane = new JEditorPane();
		editorPane.setInheritsPopupMenu(true);
		editorPane.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 14));
		scrollPane.setViewportView(editorPane);
		
		//kit = editorPane.getEditorKit();
		//doc = kit.createDefaultDocument();
		
		rootTreeNode = new DefaultMutableTreeNode("TwitterRoot");
		treeModel.setRoot(rootTreeNode);
		
		rootRssTreeNode = new DefaultMutableTreeNode("RSS Reader");
		rssTreeModel.setRoot(rootRssTreeNode);
		
		//contentPane.setVisible(true);
		this.setVisible(true);
	}
	
	public void doNLPRequest()
	{
		NLPRequest nlprequest = new NLPRequest();
		nlprequest.parsingParam = comboBox.getSelectedItem().toString();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cal = Calendar.getInstance();
		//Thread thr = new Thread(nlprequest);
		//while(true){
			try{
				nlprequest.parsingParam = comboBox.getSelectedItem().toString();
				String retmsg = nlprequest.requestMessage( textPaneRequest.getText().trim() );
				textPane.setText(  "["+ df.format(cal.getTime() ) + "]\r\n" + retmsg  + "\r\n ============================ \r\n" + textPane.getText() );
				//Thread.sleep(10000);
				//thr.start();
				
				
			}catch(Exception ex)
			{
				Log.errorLog(this, "thread start :: " + ex);
			}
		//}		
	}
	
	private void doBtnRssParsing()
	{
		if(btnRSSstart.getText().contains("Start")){
			
			btnRSSstart.setForeground(Color.GRAY);
			btnRSSstart.setText("RSSParseStop");
			newsAnaMain.RSSFeedParsingStart();
			

		}else if(btnRSSstart.getText().contains("Stop")){

			btnRSSstart.setForeground(Color.BLACK);
			btnRSSstart.setText("RSSParseStart");
			newsAnaMain.RSSFeedParsingStop();					
		}		
	}

}
