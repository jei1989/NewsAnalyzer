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
import javax.swing.JTable;
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
import javax.swing.table.DefaultTableModel;
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
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.LineBorder;
import javax.swing.JList;
import javax.swing.border.BevelBorder;
import javax.swing.AbstractListModel;


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
	
	public JScrollPane scrollPane_5;
	public JScrollPane scrollPane;
	public JTable table;
	public DefaultTableModel model;
	
	public JScrollPane scrollPane_10;
	public JList list;
	public DefaultListModel listModel;
	
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
				try{
					newsAnaMain.setProperty("chkboxDateCurrent", String.valueOf( chkboxDateCurrent.isSelected() ));
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
					chkboxDateCurrent.setSelected( Boolean.getBoolean( newsAnaMain.getProperty("chkboxDateCurrent") ));
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
				
				try{
					if( chkboxDateCurrent.isSelected() ){
						cal.setTime(  cal.getInstance().getTime() );
						dateChooserEnd.setSelectedDate( cal );
					}
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
				
				try{
					textPaneRequest.setText( newsAnaMain.getProperty("TextPaneRequest") );
				}catch(Exception ex){
					Log.errorLog(this, "windowClosing :: " + ex);
				}
				
				//btnRSSstart.doClick();
				//doBtnRssParsing();
				
			}
		});
		setTitle("New Analyzer V1.0");
		
		this.newsAnaMain = newsAnaMain;
		this.strbuff = new StringBuffer();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1800, 900);
		
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
		scrollPane_2.setMinimumSize(new Dimension(270, 23));
		splitPane_1.setLeftComponent(scrollPane_2);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(null, "News Analyzer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setMinimumSize(new Dimension(230, 10));
		panel.setPreferredSize(new Dimension(230, 20));
		scrollPane_2.setViewportView(panel);
	
		panel.setLayout(new MigLayout("", "[120px,grow][5px][120px,grow]", "[10px][25px][21px][21px][21px][23px][23px][]"));
		
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
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(new Color(255, 222, 173));
		panel_6.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel.add(panel_6, "cell 0 1 3 5");
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "News Action", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setBackground(new Color(221, 160, 221));
		panel_6.add(panel_7);
		panel_7.setLayout(new MigLayout("", "[][][]", "[][][][][grow]"));
		
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
		
		btnTwitterstart = new JButton("TwitterStart");
		panel_7.add(btnTwitterstart, "cell 1 0,growx");
		btnTwitterstart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newsAnaMain.twitterParsingStart();
			}
		});
		btnNLPParse.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		panel_7.add(btnNLPParse, "cell 2 0,growx");
				
				txtLastChkTime = new JTextField();
				panel_7.add(txtLastChkTime, "cell 1 1 2 1,growx");
				txtLastChkTime.setColumns(10);
		
		btnRSSstart = new JButton("ParseStart");
		panel_7.add(btnRSSstart, "flowy,cell 1 3,growx");
		
		btnRSSstart.setBackground(UIManager.getColor("Button.light"));
		btnRSSstart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				doBtnRssParsing();
				
			}
		});
		btnRSSstart.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		
		JButton btnRssanalyzer = new JButton("NewsAnalyzer");
		panel_7.add(btnRssanalyzer, "cell 2 3,growx");
		btnRssanalyzer.setFont(new Font("Trebuchet MS", Font.BOLD, 12));

		btnRssanalyzer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				newsAnaMain.RSSFeedAnalyzerStart();
				
			}
		});
		
		JPanel panel_1 = new JPanel();
		splitPane_1.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_1.add(tabbedPane);
		
		JSplitPane splitPane_3 = new JSplitPane();
		tabbedPane.addTab("NewsAnalyzer", null, splitPane_3, null);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBackground(new Color(154, 205, 50));
		scrollPane_3.setBorder(new TitledBorder(null, "Article List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane_3.setMinimumSize(new Dimension(20, 2));
		scrollPane_3.setPreferredSize(new Dimension(400, 2));
		splitPane_3.setLeftComponent(scrollPane_3);
		
		model = new DefaultTableModel();//
				
		//model.addColumn("No");
		//model.addColumn("TITLE");
		//model.addColumn("URL");
		model.addColumn("NO");
		model.addColumn("Title");
		model.addColumn("url");
		
		table = new JTable(model);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(25);
		table.getColumnModel().getColumn(1).setPreferredWidth(400);
		table.getColumnModel().getColumn(2).setPreferredWidth(0);
		
		table.setMinimumSize(new Dimension(0, 0));
		table.setFont(new Font("Dialog", Font.BOLD, 12));
		table.setRowHeight(25);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane_3.setViewportView(table);
		
		rssTree = new JTree();
		rssTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				try{
					if(rssTree.getSelectionPath().getPath().length >= 4 ){
						
						
						String writeText = editorPane.getText() + rssTree.getSelectionPath().getPathComponent(1).toString() + " :: (" + rssTree.getSelectionPath().getPathComponent(2).toString() + ")\r\n" + rssTree.getSelectionPath().getPathComponent(3).toString() + "\r\n" + "--------------------\r\n";
						//editorPane.setText( editorPane.getText() + tree.getSelectionPath().getPathComponent(1).toString() + " :: (" + tree.getSelectionPath().getPathComponent(2).toString() + ")\r\n" + tree.getSelectionPath().getPathComponent(3).toString() + "\r\n" + "--------------------\r\n" );
						//editorPane.setText("\r\n");
						editorPane.setText( writeText );
						System.out.println(rssTree.getSelectionPath().getPathComponent(3).toString());
						newsAnaMain.setWebEngine( rssTree.getSelectionPath().getPathComponent(3).toString() );
						
						
					}
				}catch(Exception ex)
				{
					Log.errorLog(this, "rssTree MouseClicked :: " + ex);
				}				
				
			}
		});
		rssTreeModel = (DefaultTreeModel)rssTree.getModel();
		
		//scrollPane_3.setViewportView(rssTree);
		
		JPanel panel_2 = new JPanel();
		
		splitPane_3.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		scrollPane_5 = new JScrollPane();
		scrollPane_5.setBackground(new Color(255, 204, 102));
		scrollPane_5.setBorder(new TitledBorder(null, "Web Browser", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(scrollPane_5, BorderLayout.CENTER);
		scrollPane_5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_5.setMinimumSize(new Dimension(1100, 22));
		scrollPane_5.setPreferredSize(new Dimension(400, 3));
		
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
		//scrollPane_5.setViewportView(textPane);
		
		JPanel panel_3 = new JPanel();
		
		panel_3.setLayout(new MigLayout("", "[grow]", "[][][grow][][][][][][][][][][][][][][][][]"));
		
		btnNLPStart = new JButton("Parsing NLP");
		btnNLPStart.setPreferredSize(new Dimension(50, 28));
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
		btnNLPRequest.setPreferredSize(new Dimension(50, 28));
		btnNLPRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				doNLPRequest();
				
				
			}
		});
		panel_3.add(btnNLPRequest, "cell 0 4,alignx center");
		
		editorPane = new JEditorPane();
		editorPane.setInheritsPopupMenu(true);
		editorPane.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 14));
		
		JSplitPane splitPane_2 = new JSplitPane();
		tabbedPane.addTab("Data", null, splitPane_2, null);

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
		
		JSplitPane splitPane_5 = new JSplitPane();
		scrollPane_4.setViewportView(splitPane_5);
		
		JScrollPane scrollPane_7 = new JScrollPane();
		scrollPane_7.setPreferredSize(new Dimension(230, 3));
		scrollPane_7.setViewportView(rssTree);
		splitPane_5.setLeftComponent(scrollPane_7);
		
		JScrollPane scrollPane_8 = new JScrollPane();
		splitPane_5.setRightComponent(scrollPane_8);
		
		JSplitPane splitPane_6 = new JSplitPane();
		scrollPane_8.setViewportView(splitPane_6);
		
		splitPane_6.setLeftComponent(editorPane);
		splitPane_6.setRightComponent(panel_3);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		splitPane.setRightComponent(scrollPane);
		
		JSplitPane splitPane_4 = new JSplitPane();
		scrollPane.setViewportView(splitPane_4);
		
		JPanel panel_8 = new JPanel();
		splitPane_4.setLeftComponent(panel_8);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_9 = new JScrollPane();
		scrollPane_9.setPreferredSize(new Dimension(655, 3));
		panel_8.add(scrollPane_9);
		
		listModel = new DefaultListModel(); 
		list = new JList();
		list.setFixedCellHeight(25);
		list.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
				newsAnaMain.setWebEngine( String.valueOf( listModel.getElementAt( list.getSelectedIndex() ) ) );
				
			}
		});
		list.setModel(listModel);
		
		
		list.setBackground(Color.WHITE);
		list.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "List", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		scrollPane_9.setViewportView(list);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBackground(Color.LIGHT_GRAY);
		panel_10.setBorder(new TitledBorder(null, "List Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane_9.setRowHeaderView(panel_10);
		
		JButton btnListReload = new JButton("Reload");
		btnListReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				newsAnaMain.setCustomList();
				
			}
		});
		panel_10.add(btnListReload);
		
		JPanel panel_9 = new JPanel();
		splitPane_4.setRightComponent(panel_9);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		scrollPane_10 = new JScrollPane();
		scrollPane_10.setBackground(new Color(204, 204, 255));
		scrollPane_10.setBorder(new TitledBorder(null, "Static PieChart", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_9.add(scrollPane_10, BorderLayout.CENTER);
		
		//kit = editorPane.getEditorKit();
		//doc = kit.createDefaultDocument();
		
		rootTreeNode = new DefaultMutableTreeNode("TwitterRoot");
		treeModel.setRoot(rootTreeNode);
		
		rootRssTreeNode = new DefaultMutableTreeNode("News Reader");
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
