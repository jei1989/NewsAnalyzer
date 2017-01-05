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
import java.beans.PropertyChangeEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.event.TreeExpansionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

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
	public Document doc;
	public EditorKit kit;
	
	public JButton btnTwitterstart;
	public JButton btnRSSstart;
	
	public JTextPane textPane;
	public JTextPane textPaneRequest;
	public JButton btnNLPRequest;
	public JButton btnNLPStart;
	
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
		
		this.newsAnaMain = newsAnaMain;
				
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
		splitPane_1.setLeftComponent(scrollPane_2);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 10));
		scrollPane_2.setViewportView(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		btnTwitterstart = new JButton("TwitterStart");
		btnTwitterstart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newsAnaMain.twitterParsingStart();
			}
		});
		GridBagConstraints gbc_btnTwitterstart = new GridBagConstraints();
		gbc_btnTwitterstart.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTwitterstart.insets = new Insets(0, 0, 5, 5);
		gbc_btnTwitterstart.gridx = 0;
		gbc_btnTwitterstart.gridy = 1;
		panel.add(btnTwitterstart, gbc_btnTwitterstart);
		
		btnRSSstart = new JButton("RSSStart");
		btnRSSstart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newsAnaMain.RSSFeedParsingStart();
			}
		});
		GridBagConstraints gbc_btnRSSstart = new GridBagConstraints();
		gbc_btnRSSstart.insets = new Insets(0, 0, 0, 5);
		gbc_btnRSSstart.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRSSstart.gridx = 0;
		gbc_btnRSSstart.gridy = 2;
		panel.add(btnRSSstart, gbc_btnRSSstart);
		
		JButton btnRssanalyzer = new JButton("RssAnalyzer");
		btnRssanalyzer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				newsAnaMain.RSSFeedAnalyzerStart();
				
			}
		});
		GridBagConstraints gbc_btnRssanalyzer = new GridBagConstraints();
		gbc_btnRssanalyzer.gridx = 1;
		gbc_btnRssanalyzer.gridy = 2;
		panel.add(btnRssanalyzer, gbc_btnRssanalyzer);
		
		JPanel panel_1 = new JPanel();
		splitPane_1.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_1.add(tabbedPane);
		
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
		
		JSplitPane splitPane_3 = new JSplitPane();
		tabbedPane.addTab("New tab", null, splitPane_3, null);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setPreferredSize(new Dimension(300, 20));
		splitPane_3.setLeftComponent(scrollPane_3);
		
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
		panel_3.add(btnNLPStart, "cell 0 1,alignx center");
		
		JScrollPane scrollPane_6 = new JScrollPane();
		panel_3.add(scrollPane_6, "cell 0 2 1 2,grow");
		
		textPaneRequest = new JTextPane();
		scrollPane_6.setViewportView(textPaneRequest);
		
		btnNLPRequest = new JButton("Request NLP");
		btnNLPRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				NLPRequest nlprequet = new NLPRequest();
				
				
				
			}
		});
		panel_3.add(btnNLPRequest, "cell 0 4,alignx center");
		
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

}
