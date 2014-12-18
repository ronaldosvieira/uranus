package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import simulator.Parser;
import simulator.Constant;
import simulator.exceptions.AccessErrorException;
import simulator.exceptions.IntegerOutofRangeException;
import simulator.Memory;
import simulator.exceptions.OverflowException;
import simulator.Processor;
import simulator.ControlUnit;

public class UI {

	private static String programName = "Uranus";
	private static String programVersion = "1.0";

	private JFrame frmUranus;
	private JTable table;
	private JTextPane console;
	private ArrayList<RSyntaxTextArea> editor;
	private ArrayList<RTextScrollPane> editorSP;
	private JTabbedPane editorTB;
	
	private JButton btnNew, btnOpen, btnSave, btnClose, btnBuild, btnExecute, btnExecuteStep, btnReset;
	
	private Processor p;
	private Memory m;
	private Parser mont;
	private ControlUnit controlUnit;

	/**
	 * Create the application.
	 */
	public UI(Processor p, Memory m, Parser mont, ControlUnit controlUnit){
		this.p = p;
		this.m = m;
		this.mont = mont;
		this.controlUnit = controlUnit;
		initialize();
		this.getFrmUranus().setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrmUranus(new JFrame());
		getFrmUranus().setTitle(programName + " " + programVersion);
		getFrmUranus().setBounds(100, 100, 800, 600);
		getFrmUranus().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.7);
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		JToolBar toolBar = new JToolBar();
		GroupLayout groupLayout = new GroupLayout(getFrmUranus().getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(0)
							.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
							.addGap(1))
						.addComponent(splitPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE))
					.addGap(0))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
		);
		
		JButton btnNovo = new JButton("New");
		btnNovo.setIcon(new ImageIcon(getClass().getResource("/img/novo.png")));
		toolBar.add(btnNovo);
		btnNovo.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				addNewEditorTab("Untitled");
			}
		});
		
		btnOpen = new JButton("Open");
		btnOpen.setIcon(new ImageIcon(getClass().getResource("/img/abrir.png")));
		toolBar.add(btnOpen);
		btnOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser openWindow = new JFileChooser();

				int returnVal = openWindow.showOpenDialog(openWindow);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File(openWindow.getSelectedFile().getAbsolutePath());
					String fileName = openWindow.getSelectedFile().getName();

					if (file.exists()) {
						FileReader fr;
						BufferedReader br;
						try {
							fr = new FileReader(file);
							br = new BufferedReader(fr);

							addNewEditorTab(fileName);

							while (br.ready()) {
								editor.get(editorTB.getSelectedIndex()).setText((editor.get(editorTB.getSelectedIndex()).getText() + br.readLine() + "\n"));
							}

							br.close();
							fr.close();

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}


					} else {

					}
				}

			}
		});
		
		btnSave = new JButton("Save");
		btnSave.setIcon(new ImageIcon(getClass().getResource("/img/salvar.png")));
		toolBar.add(btnSave);
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser saveWindow = new JFileChooser();

				saveWindow.setSelectedFile(new File("mips1.asm"));
				int returnVal = saveWindow.showSaveDialog(saveWindow);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File(saveWindow.getSelectedFile().getAbsolutePath());
					String fileName = saveWindow.getSelectedFile().getName();

					if (file.exists()) file.delete();

					try {
						file.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					FileWriter fw;
					BufferedWriter bw;

					editorTB.setTitleAt(editorTB.getSelectedIndex(), fileName);

					try {
						fw = new FileWriter(file);
						bw = new BufferedWriter(fw);
						bw.write(editor.get(editorTB.getSelectedIndex()).getText());
						bw.close();
						fw.close();
					} catch (IOException e1) {
						writeError(e1.getMessage());
					}
				}
			}
		});
		
		btnClose = new JButton("Close");
		btnClose.setIcon(new ImageIcon(getClass().getResource("/img/fechar.png")));
		toolBar.add(btnClose);
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (editorTB.getComponentCount() > 0) {
					int indexEditor = editorTB.getSelectedIndex();
					editorTB.remove(indexEditor);
					editorSP.remove(indexEditor);
					editor.remove(indexEditor);
				}
			}
		});
		
		btnBuild = new JButton("Build");
		btnBuild.setIcon(new ImageIcon(getClass().getResource("/img/construir.png")));
		toolBar.add(btnBuild);
		btnBuild.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ArrayList<String> a = new ArrayList<String>();

				m.clear();
				p.resetRegisters();
				refreshTable();

				if (editorTB.getComponentCount() > 0) {
					getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().removeAllHighlights();

					String[] text = editor.get(editorTB.getSelectedIndex()).getText().split("\n");
					for (int i = 0; i < text.length; i++) {
						if (!(text[i].isEmpty())) a.add(text[i]);
					}
					try {
						console.setText("");
						mont.setNewList(a);
						boolean isBuilt = mont.setInstrucao();
						mont.retirarLabels();
						if (isBuilt) {
							addNewRunTab(editorTB.getTitleAt(editorTB.getSelectedIndex()) + " (build)");
							String built = "";
							for (int i = 0; i < mont.instrucoes.size(); i++) {
								built += mont.instrucoes.get(i);
								if (i != mont.instrucoes.size() - 1) {
									built += "\n";
								}
							}
							editor.get(editor.size() - 1).setText(built);


							writeln("Built.");
						} else {
							writeError("Couldn't build.");
						}
					} catch (IntegerOutofRangeException e) {
						writeError("Error: " + e.getMessage());
						writeError("Couldn't build.");
					}


				}

			}
		});
		
		btnExecute = new JButton("Execute");
		btnExecute.setIcon(new ImageIcon(getClass().getResource("/img/executar.png")));
		toolBar.add(btnExecute);
		btnExecute.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				while (controlUnit.pc / 4 <= m.size() && m.getWord(controlUnit.pc) != null) {
					int start, end;

					try {
						getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().removeAllHighlights();
						start = getEditor().get(editorTB.getSelectedIndex()).getLineStartOffset(controlUnit.pc / 4);
						end = getEditor().get(editorTB.getSelectedIndex()).getLineEndOffset(controlUnit.pc / 4);
						getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.green));

					} catch (BadLocationException e1) {
						writeError("Error: " + e1.getMessage());
					}

					try {
						if (!(controlUnit.getInstruction())) break;
					} catch (AccessErrorException | IntegerOutofRangeException | OverflowException e2) {
						writeError(e2.getMessage());
					}

					refreshTable();
				}
			}
		});
		
		btnExecuteStep = new JButton("Execute Step");
		btnExecuteStep.setIcon(new ImageIcon(getClass().getResource("/img/executar_passo.png")));
		toolBar.add(btnExecuteStep);
		btnExecuteStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int start, end;
				if (controlUnit.pc / 4 <= m.size() && m.getWord(controlUnit.pc) != null) {
					try {
						getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().removeAllHighlights();
						start = getEditor().get(editorTB.getSelectedIndex()).getLineStartOffset(controlUnit.pc / 4);
						end = getEditor().get(editorTB.getSelectedIndex()).getLineEndOffset(controlUnit.pc / 4);
						getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.green));
					} catch (BadLocationException e1) {
						writeError("Error: " + e1.getMessage());
					}

					try {
						controlUnit.getInstruction();
					} catch (AccessErrorException | IntegerOutofRangeException e) {
						writeError("Error: " + e.getMessage());
					} catch (OverflowException e) {
						writeError("Error: " + e.getMessage());
					}

					refreshTable();
				}
			}
		});
		
		btnReset = new JButton("Reset");
		btnReset.setIcon(new ImageIcon(getClass().getResource("/img/resetar.png")));
		toolBar.add(btnReset);
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				m.clear();
				p.resetRegisters();
				controlUnit.pc = 0;
				mont.instrucoes.clear();
				controlUnit.list.clear();
				refreshTable();
				console.setText("");

				if (editorTB.getComponentCount() > 0) {
					getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().removeAllHighlights();
					for (int i = 0; i < editorTB.getTabCount(); i++) {
						if ((editorTB.getTitleAt(i)).contains("(")) {
							editorTB.remove(i);
							editorSP.remove(i);
							editor.remove(i);
						}
					}
				}

			}
		});
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.9);
		splitPane_1.setOneTouchExpandable(true);
		splitPane.setLeftComponent(splitPane_1);
		
		editorTB = new JTabbedPane(JTabbedPane.TOP);
		splitPane_1.setLeftComponent(editorTB);
		
		editorSP = new ArrayList<RTextScrollPane>();
		
		editor = new ArrayList<RSyntaxTextArea>();
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		scrollPane.setViewportView(tabbedPane_1);
		
		table = new JTable();
		table.setCellSelectionEnabled(true);
		this.refreshTable();
		JTableHeader tableHeader = table.getTableHeader();
		JPanel tableContainer = new JPanel();
		tableContainer.setLayout(new BorderLayout());
		tabbedPane_1.addTab("Registers", null, tableContainer, null);
		tableContainer.add(tableHeader,BorderLayout.NORTH);
		tableContainer.add(table,BorderLayout.CENTER);
		
		
		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabbedPane_2);
		
		JScrollPane scrollPane_console = new JScrollPane();
		splitPane.setRightComponent(scrollPane_console);
		
		console = new JTextPane();
		console.setEditable(false);
		console.setFont(new Font("Arial", 10, 13));
		tabbedPane_2.addTab("Console", null, console, null);
		
		scrollPane_console.setViewportView(tabbedPane_2);
		
		frmUranus.getContentPane().setLayout(groupLayout);
		frmUranus.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{toolBar, btnNovo, btnOpen, btnSave, btnBuild, btnExecute, btnExecuteStep, btnReset, splitPane, splitPane_1, console, tabbedPane_2}));
	}

	public void refreshTable() {
		this.table.setModel(new DefaultTableModel(
		new Object[][] {
				{"$zero", getHexRegValue("$zero")},
				{"$at", getHexRegValue("$at")},
				{"$v0", getHexRegValue("$v0")},
				{"$v1", getHexRegValue("$v1")},
				{"$a0", getHexRegValue("$a0")},
				{"$a1", getHexRegValue("$a1")},
				{"$a2", getHexRegValue("$a2")},
				{"$a3", getHexRegValue("$a3")},
				{"$t0", getHexRegValue("$t0")},
				{"$t1", getHexRegValue("$t1")},
				{"$t2", getHexRegValue("$t2")},
				{"$t3", getHexRegValue("$t3")},
				{"$t4", getHexRegValue("$t4")},
				{"$t5", getHexRegValue("$t5")},
				{"$t6", getHexRegValue("$t6")},
				{"$t7", getHexRegValue("$t7")},
				{"$s0", getHexRegValue("$s0")},
				{"$s1", getHexRegValue("$s1")},
				{"$s2", getHexRegValue("$s2")},
				{"$s3", getHexRegValue("$s3")},
				{"$s4", getHexRegValue("$s4")},
				{"$s5", getHexRegValue("$s5")},
				{"$s6", getHexRegValue("$s6")},
				{"$s7", getHexRegValue("$s7")},
				{"$t8", getHexRegValue("$t8")},
				{"$t9", getHexRegValue("$t9")},
				{"$k0", getHexRegValue("$k0")},
				{"$k1", getHexRegValue("$k1")},
				{"$gp", getHexRegValue("$gp")},
				{"$sp", getHexRegValue("$sp")},
				{"$fp", getHexRegValue("$fp")},
				{"$ra", getHexRegValue("$ra")},
				{"pc", getHexRegValue("pc")},
				{"hi", getHexRegValue("hi")},
				{"lo", getHexRegValue("lo")},
			},
			new String[] {
				"Name", "Value"
			}
		
		){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex){
				return columnIndex == 1;
				}
		});
		
		table.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				String newValue = (String) table.getModel().getValueAt(row, column);
				String registerName = (String) table.getModel().getValueAt(row, column-1);
				p.regs.get(registerName).setValue(Integer.decode(newValue));
				
			}
		});
		
		
	}
	
	public void addNewRunTab(String nomeTab){
		addNewEditorTab(nomeTab);
		editor.get(editor.size()-1).setEditable(false);
	}
	
	public void addNewEditorTab(String nomeTab){
		editor.add(new RSyntaxTextArea());
		editorSP.add(new RTextScrollPane(editor.get(editor.size()-1)));
		editorTB.addTab(nomeTab, null, editorSP.get(editorSP.size()-1), null);
		
		CompletionProvider provider = new DefaultCompletionProvider(Constant.syntax);
		AutoCompletion ac = new AutoCompletion(provider);
		ac.install(editor.get(editor.size()-1));
		
		editor.get(editor.size()-1).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
		editor.get(editor.size()-1).setCodeFoldingEnabled(true);
		editor.get(editor.size()-1).setAntiAliasingEnabled(true);
		editor.get(editor.size()-1).setAutoIndentEnabled(true);
		editor.get(editor.size()-1).setAutoscrolls(true);
		editor.get(editor.size()-1).setDragEnabled(true);
		editor.get(editor.size()-1).setRoundedSelectionEdges(true);
		editorSP.get(editor.size()-1).setLineNumbersEnabled(true);
		editorSP.get(editor.size()-1).setFoldIndicatorEnabled(true);
		
		editorTB.setSelectedIndex(editor.size()-1);
	}
	
	public JTextPane getConsole() {
		return console;
	}
	
	public void write(String out){
		StyledDocument doc = this.console.getStyledDocument();

        Style style = this.console.addStyle("Black", null);
        StyleConstants.setForeground(style, Color.black);

        try { doc.insertString(doc.getLength(), out+"\n", style); }
        catch (BadLocationException e){}
	}
	
	public void writeln(String out){
		StyledDocument doc = this.console.getStyledDocument();

        Style style = this.console.addStyle("Black", null);
        StyleConstants.setForeground(style, Color.black);

        try { doc.insertString(doc.getLength(), out+"\n", style); }
        catch (BadLocationException e){}
	}
	
	public void writeError(String str){
		StyledDocument doc = this.console.getStyledDocument();

        Style style = this.console.addStyle("Red", null);
        StyleConstants.setForeground(style, Color.red);

        try { doc.insertString(doc.getLength(), str+"\n", style); }
        catch (BadLocationException e){}
	}
	
	private String getHexRegValue(String reg){
		String s1 = "0x";
		String s2;
		
		if(reg.equals("pc")) s2 = Integer.toHexString(controlUnit.pc);
		else s2 = Integer.toHexString(p.getRegister().get(reg).getValue());
		
		for(int i=0; i < 8 - s2.length(); i++){
			s1 = s1.concat("0");
		}
		
		s1 = s1.concat(s2);
		
		return s1;
	}

	public JTable getTable() {
		return table;
	}
	
	public ArrayList<RSyntaxTextArea> getEditor() {
		return editor;
	}
	
	public JTabbedPane getEditorTB() {
		return editorTB;
	}
	
	public JFrame getFrmUranus() {
		return frmUranus;
	}

	public void setFrmUranus(JFrame frmUranus) {
		this.frmUranus = frmUranus;
	}
}