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

import simulador.Assembler;
import simulador.Constant;
import simulador.ErroAcesso;
import simulador.IntegerOutofRangeException;
import simulador.Memory;
import simulador.OverflowException;
import simulador.Processador;
import simulador.UC;

public class UI {

	private JFrame frmUranus;
	private JTable table;
	private JTextPane console;
	private ArrayList<RSyntaxTextArea> editor;
	private ArrayList<RTextScrollPane> editorSP;
	private JTabbedPane editorTB;
	
	private JButton btnNovo,btnAbrir,btnSalvar,btnFechar,btnConstruir,btnExecutar,btnExecutarPasso,btnResetar;
	
	private Processador p;
	private Memory m;
	private Assembler mont;
	private UC uc;

	/**
	 * Create the application.
	 */
	public UI(Processador p, Memory m, Assembler mont, UC uc){
		this.p = p;
		this.m = m;
		this.mont = mont;
		this.uc = uc;
		initialize();
		this.getFrmUranus().setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrmUranus(new JFrame());
		getFrmUranus().setTitle("URANUS 1.0");
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
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.setIcon(new ImageIcon(getClass().getResource("/img/novo.png")));
		toolBar.add(btnNovo);
		btnNovo.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				addNewEditorTab("Sem T�tulo");
			}
		});
		
		btnAbrir = new JButton("Abrir");
		btnAbrir.setIcon(new ImageIcon(getClass().getResource("/img/abrir.png")));
		toolBar.add(btnAbrir);
		btnAbrir.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser janelaAbrir = new JFileChooser();
				
				int returnVal = janelaAbrir.showOpenDialog(janelaAbrir);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File arquivo = new File(janelaAbrir.getSelectedFile().getAbsolutePath());
					String nomeArquivo = janelaAbrir.getSelectedFile().getName();
					
					if(arquivo.exists()){
						FileReader fr;
						BufferedReader br;
						try {
							fr = new FileReader(arquivo);
							br = new BufferedReader(fr);
							
							addNewEditorTab(nomeArquivo);
							
							while(br.ready()){
								editor.get(editorTB.getSelectedIndex()).setText((editor.get(editorTB.getSelectedIndex()).getText()+br.readLine()+"\n"));
							}
							
							br.close();
							fr.close();
							
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						
					}else{
						
					}
				}
				
			}
		});
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.setIcon(new ImageIcon(getClass().getResource("/img/salvar.png")));
		toolBar.add(btnSalvar);
		btnSalvar.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser janelaSalvar = new JFileChooser();
				
				janelaSalvar.setSelectedFile(new File("mips1.asm"));
				int returnVal = janelaSalvar.showSaveDialog(janelaSalvar);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
				   File novoArquivo = new File(janelaSalvar.getSelectedFile().getAbsolutePath());
				   String nomeNovoArquivo = janelaSalvar.getSelectedFile().getName();
				   
				   if(novoArquivo.exists()) novoArquivo.delete();
				   
				   try {
					   novoArquivo.createNewFile();
				   } catch (IOException e1) {
					   e1.printStackTrace();
				   }
				   
				   	FileWriter fw;
				   	BufferedWriter bw;
				   	
				   	editorTB.setTitleAt(editorTB.getSelectedIndex(), nomeNovoArquivo);
				   	
					try {
						fw = new FileWriter(novoArquivo);
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
		
		btnFechar = new JButton("Fechar");
		btnFechar.setIcon(new ImageIcon(getClass().getResource("/img/fechar.png")));
		toolBar.add(btnFechar);
		btnFechar.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(editorTB.getComponentCount() > 0){
					int indexEditor = editorTB.getSelectedIndex();
					editorTB.remove(indexEditor);
					editorSP.remove(indexEditor);
					editor.remove(indexEditor);
				}
			}
		});
		
		btnConstruir = new JButton("Construir");
		btnConstruir.setIcon(new ImageIcon(getClass().getResource("/img/construir.png")));
		toolBar.add(btnConstruir);
		btnConstruir.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ArrayList<String> a = new ArrayList<String>();
				
				m.clear();
				p.resetRegisters();
				refreshTable();
				
				if(editorTB.getComponentCount() > 0){
					getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().removeAllHighlights();
					
					String[] texto = editor.get(editorTB.getSelectedIndex()).getText().split("\n");
					for(int i=0; i<texto.length;i++) {
						if(!(texto[i].isEmpty())) a.add(texto[i]);
					}
					try {
						console.setText("");
						mont.setNewList(a);
						boolean construiu = mont.setInstrucao();
						mont.retirarLabels();
						if(construiu){
							addNewRunTab(editorTB.getTitleAt(editorTB.getSelectedIndex())+" (construido)");
							String construido = "";
							for(int i = 0; i < mont.instrucoes.size(); i++){
								construido += mont.instrucoes.get(i);
								if(i != mont.instrucoes.size()-1){
									construido += "\n";
								}
							}
							editor.get(editor.size()-1).setText(construido);
							
							
							writeln("Constru�do.");
						}else{
							writeError("N�o constru�do.");
						}
					} catch (IntegerOutofRangeException e) {
						writeError("Erro: "+e.getMessage());
						writeError("N�o constru�do.");
					}
					
					
				}
				
			}
		});
		
		btnExecutar = new JButton("Executar");
		btnExecutar.setIcon(new ImageIcon(getClass().getResource("/img/executar.png")));
		toolBar.add(btnExecutar);
		btnExecutar.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				while(uc.pc/4 <= m.size() && m.getWord(uc.pc)!=null){
					int start,end;
					
					try {
						getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().removeAllHighlights();
						start = getEditor().get(editorTB.getSelectedIndex()).getLineStartOffset(uc.pc/4);
						end = getEditor().get(editorTB.getSelectedIndex()).getLineEndOffset(uc.pc/4);
						getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.green));
						
					} catch (BadLocationException e1) {
						writeError("Erro: "+e1.getMessage());
					}
					
					try {
						if(!(uc.getInstruction())) break;
					} catch (ErroAcesso | IntegerOutofRangeException | OverflowException e2) {
						writeError(e2.getMessage());
					}
					
					refreshTable();
				}
			}
		});
		
		btnExecutarPasso = new JButton("Executar Passo");
		btnExecutarPasso.setIcon(new ImageIcon(getClass().getResource("/img/executar_passo.png")));
		toolBar.add(btnExecutarPasso);
		btnExecutarPasso.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int start,end;
				if(uc.pc/4 <= m.size() && m.getWord(uc.pc)!=null){
					try {
						getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().removeAllHighlights();
						start = getEditor().get(editorTB.getSelectedIndex()).getLineStartOffset(uc.pc/4);
						end = getEditor().get(editorTB.getSelectedIndex()).getLineEndOffset(uc.pc/4);
						getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.green));
					} catch (BadLocationException e1) {
						writeError("Erro: "+e1.getMessage());
					}
					
					try {
						uc.getInstruction();
					} catch (ErroAcesso | IntegerOutofRangeException e) {
						writeError("Erro: "+e.getMessage());
					} catch (OverflowException e) {
						writeError("Erro: "+e.getMessage());
					}
					
					refreshTable();
				}
			}
		});
		
		btnResetar = new JButton("Resetar");
		btnResetar.setIcon(new ImageIcon(getClass().getResource("/img/resetar.png")));
		toolBar.add(btnResetar);
		btnResetar.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				m.clear();
				p.resetRegisters();
				uc.pc = 0;
				mont.instrucoes.clear();
				uc.list.clear();
				refreshTable();
				console.setText("");
				
				if(editorTB.getComponentCount() > 0){
					getEditor().get(getEditorTB().getSelectedIndex()).getHighlighter().removeAllHighlights();					
					for(int i = 0; i < editorTB.getTabCount(); i++){
						if((editorTB.getTitleAt(i)).contains("(")){
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
		tabbedPane_1.addTab("Registradores", null, tableContainer, null);
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
		frmUranus.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{toolBar, btnNovo, btnAbrir, btnSalvar, btnConstruir, btnExecutar, btnExecutarPasso, btnResetar, splitPane, splitPane_1, console, tabbedPane_2}));
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
				"Nome", "Valor"
			}
		
		){
			/**
			 * 
			 */
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
				String novoValor = (String) table.getModel().getValueAt(row, column);
				String nomeReg = (String) table.getModel().getValueAt(row, column-1);
				p.regs.get(nomeReg).setValue(Integer.decode(novoValor));
				
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
		
		if(reg.equals("pc")) s2 = Integer.toHexString(uc.pc);
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
