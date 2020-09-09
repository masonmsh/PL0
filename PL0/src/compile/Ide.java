package compile;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class Ide extends JFrame {
	private JPanel contentPane;
	private JTextField textField, jtb;
	private JTabbedPane jtp;
	private String aprog, asym, atable, acode, arde;
	private JPanel panel;
	private JLabel jl, jb, jx, jm;
	private JMenuBar jmb;
	private JMenu file, source, mrun, window;
	private JMenuItem open, close, build, cmd, format, save, saveas, demo, run, debug, newf, help, exit, msym, mtable,
			mcode, mrde;
	private ArrayList<JTextArea> jta1;
	private JTextArea jta2, jta3;
	private JScrollPane jsp2, jsp3;
	private ArrayList<String> title;
	int curj = -1, now = -1;
	private FileOutputStream fos;
	private OutputStreamWriter osw;
	private TListener tl = new TListener();
	private MListener ml = new MListener();
	private Analyse ae;
	private String[] ins;
	private String[] re = { "const", "var", "procedure", "begin", "end", "odd", "if", "then", "call", "while", "do",
			"read", "write" };

	private String dem = "const a=10;\nvar b,c;\nprocedure p;\nbegin\nc:=b+a\nend;\nbegin\nread(b);\nwhile b#0 do\nbegin\ncall  p;\nwrite(2*c);\nread(b)\nend\nend .";

	public Ide() {
		super("PL0 - IDE");
		setBounds(new Rectangle(0, 0, 50, 50));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 50, 1020, 630);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		panel = new JPanel();
		panel.setBounds(new Rectangle(0, 0, 10, 10));
		contentPane.add(panel);
		panel.setLayout(null);

		jl = new JLabel("file ");
		jl.setBounds(0, 0, 60, 20);
		jb = new JLabel("building ");
		jb.setBounds(500, 0, 60, 20);
		panel.add(jl);
		panel.add(jb);
		jx = new JLabel("Compiler[version 2.0] @2016 Mr. Mei all rights reserved.");
		jx.setBounds(0, 550, 850, 15);
		panel.add(jx);
		jm = new JLabel("1:1");
		jm.setBounds(850, 550, 50, 15);
		panel.add(jm);

		textField = new JTextField();
		textField.setBounds(60, 0, 430, 20);
		panel.add(textField);
		textField.setEditable(false);
		textField.setColumns(10);
		jtb = new JTextField();
		jtb.setBounds(560, 0, 440, 20);
		panel.add(jtb);
		jtb.setEditable(false);
		jtb.setColumns(10);

		jta1 = new ArrayList<JTextArea>();
		title = new ArrayList<String>();

		jtp = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		jtp.setBounds(0, 25, 500, 525);
		panel.add(jtp);

		jta2 = new JTextArea();
		jta2.setEditable(false);
		jsp2 = new JScrollPane(jta2);
		jsp2.setBounds(500, 25, 400, 525);
		panel.add(jsp2);

		jta3 = new JTextArea();
		jsp3 = new JScrollPane(jta3);
		jsp3.setBounds(900, 25, 100, 525);
		panel.add(jsp3);

		jmb = new JMenuBar();

		file = new JMenu("File");
		source = new JMenu("Source");
		mrun = new JMenu("Run");
		window = new JMenu("Window");

		newf = new JMenuItem("New");
		cmd = new JMenuItem("Command");
		msym = new JMenuItem("Sym");
		mtable = new JMenuItem("Table");
		mcode = new JMenuItem("Code");
		mrde = new JMenuItem("Result");
		format = new JMenuItem("Format");
		format.setEnabled(false);
		save = new JMenuItem("Save");
		save.setEnabled(false);
		saveas = new JMenuItem("Save As...");
		saveas.setEnabled(false);
		demo = new JMenuItem("Demo");
		run = new JMenuItem("Run");
		run.setEnabled(false);
		debug = new JMenuItem("Debug");
		debug.setEnabled(false);
		open = new JMenuItem("Open");
		close = new JMenuItem("Close");
		build = new JMenuItem("Build");
		build.setEnabled(false);
		help = new JMenuItem("Help");
		exit = new JMenuItem("Exit");

		file.add(newf);
		file.add(open);
		file.add(close);
		file.add(save);
		file.add(saveas);
		file.add(exit);
		source.add(demo);
		source.add(format);
		mrun.add(build);
		mrun.add(run);
		mrun.add(debug);
		window.add(msym);
		window.add(mtable);
		window.add(mcode);
		window.add(mrde);
		window.add(cmd);
		window.add(help);
		jmb.add(file);
		jmb.add(source);
		jmb.add(mrun);
		jmb.add(window);
		setJMenuBar(jmb);

		msym.addActionListener(tl);
		mtable.addActionListener(tl);
		mcode.addActionListener(tl);
		mrde.addActionListener(tl);
		cmd.addActionListener(tl);
		format.addActionListener(tl);
		demo.addActionListener(tl);
		save.addActionListener(tl);
		saveas.addActionListener(tl);
		run.addActionListener(tl);
		debug.addActionListener(tl);
		newf.addActionListener(tl);
		open.addActionListener(tl);
		close.addActionListener(tl);
		build.addActionListener(tl);
		help.addActionListener(tl);
		exit.addActionListener(tl);
		jtp.addMouseListener(ml);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	class MListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			curj = jtp.getSelectedIndex();
			if (curj >= 0)
				textField.setText(title.get(curj));
		}

		public void mouseEntered(MouseEvent arg0) {
			curj = jtp.getSelectedIndex();
			if (curj >= 0)
				textField.setText(title.get(curj));
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}

	}

	class TListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(build)) {
				if (textField.getText().equals(""))
					return;
				jtb.setText(textField.getText());
				now = curj;
				jta1.get(curj).setText("");
				jta2.setText("");
				jta3.setText("");
				save.setEnabled(true);
				saveas.setEnabled(true);
				run.setEnabled(false);
				debug.setEnabled(false);
				format.setEnabled(false);
				ae = new Analyse(textField.getText());
				ae.getch();
				aprog = ae.prog;
				jta1.get(curj).setText(aprog);
				asym = ae.output1();
				arde = ae.se;
				if (ae.gflag) {
					ae.s();
					atable = ae.output2();
					acode = ae.output3();
					arde = ae.se;
					if (ae.yflag) {
						run.setEnabled(true);
						debug.setEnabled(true);
						format.setEnabled(true);
					}
				}
				JOptionPane.showMessageDialog(null, "Builded!", "System message", JOptionPane.INFORMATION_MESSAGE);
			} else if (e.getSource().equals(open)) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setDialogTitle("Load program");
				int result = jfc.showOpenDialog(null);
				if (result == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(null, "No program is selected!", "System message",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				File fn = jfc.getSelectedFile();
				if (fn == null || fn.getName().equals("")) {
					JOptionPane.showMessageDialog(null, "Illegal name!", "System message", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					textField.setText(fn.getPath());
				}
				curj = jtp.getTabCount();
				jta1.add(new JTextArea());
				title.add(textField.getText());
				jta1.get(curj).addMouseListener(ml);
				jta1.get(curj).addCaretListener(new CaretListener() {
					public void caretUpdate(CaretEvent e) {
						try {
							int pos = jta1.get(curj).getCaretPosition();
							int row = jta1.get(curj).getLineOfOffset(pos) + 1;
							int col = pos - jta1.get(curj).getLineStartOffset(row - 1) + 1;
							jm.setText(row + ":" + col);
							String[] s1 = jta1.get(curj).getText().split("\n");
							String[] s2 = s1[s1.length - 1].split("\t");
							String[] s3 = s2[s2.length - 1].split(" ");
							String s = s3[s3.length - 1];
							String sm = "";
							for (int i = 0; i < re.length; i++) {
								if (s.length() == 0 || s.length() > re[i].length())
									break;
								if (re[i].substring(0, s.length()).equalsIgnoreCase(s)) {
									sm += re[i] + " ";
								}
							}
							jta1.get(curj).setToolTipText(sm);
						} catch (Exception ex) {
							jm.setText("0:0");
						}
					}
				});
				JScrollPane jsp = new JScrollPane(jta1.get(curj));
				String[] s0 = textField.getText().split("\\\\");
				jtp.addTab(s0[s0.length - 1], jsp);

				save.setEnabled(true);
				saveas.setEnabled(true);
				build.setEnabled(true);
				run.setEnabled(false);
				debug.setEnabled(false);
				format.setEnabled(false);
			} else if (e.getSource().equals(close)) {
				curj = jtp.getSelectedIndex();
				if (curj >= 0) {
					jtp.remove(curj);
					jta1.remove(curj);
					title.remove(curj);
					textField.setText("");
					now = -1;
				}
				if (jtp.getTabCount() == 0) {
					save.setEnabled(false);
					saveas.setEnabled(false);
					build.setEnabled(false);
					run.setEnabled(false);
					debug.setEnabled(false);
					format.setEnabled(false);
				}

			} else if (e.getSource().equals(msym)) {
				jta2.setText(asym);
			} else if (e.getSource().equals(mtable)) {
				jta2.setText(atable);
			} else if (e.getSource().equals(mcode)) {
				jta2.setText(acode);
			} else if (e.getSource().equals(mrde)) {
				jta2.setText(arde);
			} else if (e.getSource().equals(cmd)) {
				int n = JOptionPane.showConfirmDialog(null, "Sure to change to cmd?", "System message",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					dispose();
					new Cmd();
				}
			} else if (e.getSource().equals(format)) {
				if (now != curj)
					return;
				int tab = 0;
				boolean be = false;
				jta1.get(curj).setText("");
				for (int i = 0; i < ae.sym.size(); i++) {
					if (ae.sym.get(i).type == 1) {
						jta1.get(curj).append(ae.id.get(ae.sym.get(i).addr).toLowerCase());
						if (ae.sym.get(i + 1).type == 7) {
							jta1.get(curj).append("\n");
							tab--;
							for (int j = 0; j < tab; j++) {
								jta1.get(curj).append("    ");
							}
						}
					} else if (ae.sym.get(i).type == 2) {
						jta1.get(curj).append(String.valueOf(ae.num.get(ae.sym.get(i).addr)));
						if (ae.sym.get(i + 1).type == 7) {
							jta1.get(curj).append("\n");
							tab--;
							for (int j = 0; j < tab; j++) {
								jta1.get(curj).append("    ");
							}
						}
					} else if (ae.sym.get(i).type == 8 || ae.sym.get(i).type >= 14 && ae.sym.get(i).type <= 29) {
						jta1.get(curj).append(ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase());
						if (ae.sym.get(i + 1).type == 7) {
							jta1.get(curj).append("\n");
							tab--;
							for (int j = 0; j < tab; j++) {
								jta1.get(curj).append("    ");
							}
						}
					} else if (ae.sym.get(i).type == 3 || ae.sym.get(i).type == 4 || ae.sym.get(i).type == 5
							|| ae.sym.get(i).type == 9 || ae.sym.get(i).type == 11 || ae.sym.get(i).type == 12) {
						jta1.get(curj).append(ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase() + " ");
					} else if (ae.sym.get(i).type == 6) {
						jta1.get(curj).append(ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase() + "\n");
						if (ae.sym.get(i + 1).type != 7) {
							tab++;
						}
						for (int j = 0; j < tab; j++) {
							jta1.get(curj).append("    ");
						}
					} else if (ae.sym.get(i).type == 7) {
						jta1.get(curj).append(ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase());
						if (ae.sym.get(i + 1).type == 30 || ae.sym.get(i + 1).type == 31) {
							continue;
						} else if (ae.sym.get(i + 1).type == 7) {
							jta1.get(curj).append("\n");
							tab--;
						} else {
							jta1.get(curj).append("\n");
						}
						for (int j = 0; j < tab; j++) {
							jta1.get(curj).append("    ");
						}
					} else if (ae.sym.get(i).type == 10 || ae.sym.get(i).type == 13) {
						jta1.get(curj).append(" " + ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase() + "\n");
						if (ae.sym.get(i + 1).type != 6) {
							tab++;
							be = true;
						}
						for (int j = 0; j < tab; j++) {
							jta1.get(curj).append("    ");
						}
					} else if (ae.sym.get(i).type == 30) {
						jta1.get(curj).append(ae.reserved[ae.sym.get(i).type - 3].s + "\n");
						if (ae.sym.get(i + 1).type == 7) {
							tab--;
						}
						if (be) {
							tab--;
							be = false;
						}
						for (int j = 0; j < tab; j++) {
							jta1.get(curj).append("    ");
						}
					} else if (ae.sym.get(i).type == 31) {
						jta1.get(curj).append(ae.reserved[ae.sym.get(i).type - 3].s + "\n");
					}
				}
				format.setEnabled(false);
			} else if (e.getSource().equals(save)) {
				if (textField.getText().equals("")) {
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.setDialogTitle("Save program");
					int result = jfc.showOpenDialog(null);
					if (result == JFileChooser.CANCEL_OPTION) {
						JOptionPane.showMessageDialog(null, "No program is selected!", "System message",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					File fn = jfc.getSelectedFile();
					if (fn == null || fn.getName().equals("")) {
						JOptionPane.showMessageDialog(null, "Illegal name!", "System message",
								JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						textField.setText(fn.getPath());
					}
				}
				int n = JOptionPane.showConfirmDialog(null, "Sure to save as " + textField.getText() + "?",
						"System message", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					try {
						fos = new FileOutputStream(textField.getText());
						osw = new OutputStreamWriter(fos);
						osw.write(jta1.get(curj).getText());
						title.set(curj, textField.getText());
						String[] s0 = title.get(curj).split("\\\\");
						jtp.setTitleAt(curj, s0[s0.length - 1]);
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						try {
							osw.flush();
							osw.close();
							fos.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					build.setEnabled(true);
					run.setEnabled(false);
					debug.setEnabled(false);
					format.setEnabled(false);
				}
			} else if (e.getSource().equals(demo)) {
				textField.setText(System.getProperty("user.dir") + "\\demo.txt");
				try {
					fos = new FileOutputStream(textField.getText());
					osw = new OutputStreamWriter(fos);
					osw.write(dem);
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					try {
						osw.flush();
						osw.close();
						fos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				curj = jtp.getTabCount();
				jta1.add(new JTextArea());
				title.add(textField.getText());
				jta1.get(curj).addMouseListener(ml);
				jta1.get(curj).addCaretListener(new CaretListener() {
					public void caretUpdate(CaretEvent e) {
						try {
							int pos = jta1.get(curj).getCaretPosition();
							int row = jta1.get(curj).getLineOfOffset(pos) + 1;
							int col = pos - jta1.get(curj).getLineStartOffset(row - 1) + 1;
							jm.setText(row + ":" + col);
							String[] s1 = jta1.get(curj).getText().split("\n");
							String[] s2 = s1[s1.length - 1].split("\t");
							String[] s3 = s2[s2.length - 1].split(" ");
							String s = s3[s3.length - 1];
							String sm = "";
							for (int i = 0; i < re.length; i++) {
								if (s.length() == 0 || s.length() > re[i].length())
									break;
								if (re[i].substring(0, s.length()).equalsIgnoreCase(s)) {
									sm += re[i] + " ";
								}
							}
							jta1.get(curj).setToolTipText(sm);
						} catch (Exception ex) {
							jm.setText("0:0");
						}
					}
				});
				JScrollPane jsp = new JScrollPane(jta1.get(curj));
				jtp.addTab("demo.txt", jsp);

				save.setEnabled(true);
				saveas.setEnabled(true);
				build.setEnabled(true);
				run.setEnabled(false);
				debug.setEnabled(false);
				format.setEnabled(false);
			} else if (e.getSource().equals(saveas)) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setDialogTitle("Save program");
				int result = jfc.showOpenDialog(null);
				if (result == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(null, "No program is selected!", "System message",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				File fn = jfc.getSelectedFile();
				if (fn == null || fn.getName().equals("")) {
					JOptionPane.showMessageDialog(null, "Illegal name!", "System message", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					textField.setText(fn.getPath());
				}
				int n = JOptionPane.showConfirmDialog(null, "Sure to save as " + textField.getText() + "?",
						"System message", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					try {
						fos = new FileOutputStream(textField.getText());
						osw = new OutputStreamWriter(fos);
						osw.write(jta1.get(curj).getText());
						title.set(curj, textField.getText());
						String[] s0 = textField.getText().split("\\\\");
						jtp.setTitleAt(curj, s0[s0.length - 1]);
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						try {
							osw.flush();
							osw.close();
							fos.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					format.setEnabled(false);
				}
			} else if (e.getSource().equals(run)) {
				ins = jta3.getText().split("\n");
				ae.run(ins);
				arde = ae.run;
				format.setEnabled(false);
			} else if (e.getSource().equals(debug)) {
				ins = jta3.getText().split("\n");
				ae.debug(ins);
				arde = ae.debug;
				format.setEnabled(false);
			} else if (e.getSource().equals(newf)) {
				textField.setText("");
				jta2.setText("");
				jta3.setText("");

				curj = jtp.getTabCount();
				jta1.add(new JTextArea());
				title.add("");
				jta1.get(curj).addMouseListener(ml);
				jta1.get(curj).addCaretListener(new CaretListener() {
					public void caretUpdate(CaretEvent e) {
						try {
							int pos = jta1.get(curj).getCaretPosition();
							int row = jta1.get(curj).getLineOfOffset(pos) + 1;
							int col = pos - jta1.get(curj).getLineStartOffset(row - 1) + 1;
							jm.setText(row + ":" + col);
							String[] s1 = jta1.get(curj).getText().split("\n");
							String[] s2 = s1[s1.length - 1].split("\t");
							String[] s3 = s2[s2.length - 1].split(" ");
							String s = s3[s3.length - 1];
							String sm = "";
							for (int i = 0; i < re.length; i++) {
								if (s.length() == 0 || s.length() > re[i].length())
									break;
								if (re[i].substring(0, s.length()).equalsIgnoreCase(s)) {
									sm += re[i] + " ";
								}
							}
							jta1.get(curj).setToolTipText(sm);
						} catch (Exception ex) {
							jm.setText("0:0");
						}
					}
				});
				JScrollPane jsp = new JScrollPane(jta1.get(curj));
				jtp.addTab("Untitle", jsp);

				save.setEnabled(true);
				saveas.setEnabled(true);
				build.setEnabled(true);
				run.setEnabled(false);
				debug.setEnabled(false);
				format.setEnabled(false);
			} else if (e.getSource().equals(help)) {
				JOptionPane.showMessageDialog(null, "Please contact the author for help.\nTelephone:110",
						"Help message", JOptionPane.INFORMATION_MESSAGE);
			} else if (e.getSource().equals(exit)) {
				System.exit(0);
			}

		}

	}

	public static void main(String[] args) {
		new Ide();
	}

}
