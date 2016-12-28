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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JInternalFrame;
import javax.swing.ScrollPaneConstants;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeExpansionEvent;

public class NewsAnaWindow extends JFrame {

	private JPanel contentPane;
	public JTree tree;
	public DefaultTreeModel treeModel;
	public DefaultMutableTreeNode rootTreeNode;
	public DefaultMutableTreeNode grandTreeNode;
	public DefaultMutableTreeNode motherTreeNode;

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
	public NewsAnaWindow() {
		
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
		//tree.setPreferredSize(new Dimension(200, 20));
		treeModel = (DefaultTreeModel)tree.getModel();
		
		scrollPane_1.setViewportView(tree);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		splitPane_2.setRightComponent(scrollPane_4);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setPreferredSize(new Dimension(30, 21));
		scrollPane_4.setViewportView(editorPane);
		
		JTextArea textArea = new JTextArea();
		splitPane.setRightComponent(textArea);
		
		rootTreeNode = new DefaultMutableTreeNode("TwitterRoot");
		treeModel.setRoot(rootTreeNode);
		
		//contentPane.setVisible(true);
		this.setVisible(true);
	}

}
