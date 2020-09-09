package compile;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

public class Cmd extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	private JPanel panel;
	private JLabel jl;
	private JTextArea jta1;
	private JScrollPane jsp1;
	private FileOutputStream fos;
	private OutputStreamWriter osw;
	private TListener tl = new TListener();
	private KListener kl = new KListener();
	private Analyse ae;
	private String ed;

	public Cmd() {
		super("PL0 - CMD");
		setBounds(new Rectangle(0, 0, 50, 50));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 605, 405);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		panel = new JPanel();
		panel.setBounds(new Rectangle(0, 0, 10, 10));
		contentPane.add(panel);
		panel.setLayout(null);

		jl = new JLabel("compile> ");
		jl.setBounds(0, 0, 60, 25);
		panel.add(jl);

		textField = new JTextField();
		textField.setBounds(60, 0, 540, 25);
		panel.add(textField);
		textField.setColumns(10);

		jta1 = new JTextArea();
		jta1.setEditable(false);
		jsp1 = new JScrollPane(jta1);
		jsp1.setBounds(0, 25, 600, 352);
		jta1.setText("Compiler[version 2.0]\n@2016 Mr. Mei all rights reserved.\n\ncompile> ");
		panel.add(jsp1);

		textField.addActionListener(tl);
		jta1.addKeyListener(kl);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	class KListener implements KeyListener {
		public void keyPressed(KeyEvent arg0) {
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				textField.setEditable(true);
				jta1.setEditable(false);
				ed = jta1.getText();
				jta1.append("\ncompile> ");
			}
		}

		public void keyTyped(KeyEvent arg0) {
		}

	}

	class TListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(textField)) {
				String[] args = textField.getText().split(" ");
				int length = args.length;
				jta1.append(textField.getText() + "\n");
				if (length == 0) {
					jta1.append("Input help to get help.\ncompile> ");
				} else if (args[0].equals("help")) {
					jta1.append(
							"help\ninfo\nexit\nide\nclc\nopen [filename]\nsym\ntable\ncode\nrun [parameter1][parameter2]...\ndebug [parameter1][parameter2]...\nedit\nsave [filename]\nformat\ncompile> ");
				} else if (args[0].equals("info")) {
					jta1.append("Compiler[version 2.0]\n@2016 Mr. Mei all rights reserved.\ncompile> ");
				} else if (args[0].equals("exit")) {
					System.exit(0);
				} else if (args[0].equals("ide")) {
					dispose();
					new Ide();
				} else if (args[0].equals("clc")) {
					jta1.setText("compile> ");
				} else if (args[0].equals("open")) {
					if (length != 2) {
						jta1.append("Syntax Error.\ncompile> ");
					} else {
						ae = new Analyse(args[1]);
						ae.getch();
						if (ae.prog != null)
							jta1.append(ae.prog + "\ncompile> ");
						else
							jta1.append("compile> ");
						if (ae.gflag) {
							ae.s();
						}
					}
				} else if (args[0].equals("sym")) {
					if (ae == null) {
						jta1.append("Please load program.\ncompile> ");
						textField.setText("");
						return;
					} else {
						jta1.append(ae.output1() + "\n");
					}
					if (ae.se != null) {
						jta1.append(ae.se + "\ncompile> ");
					} else {
						jta1.append("compile> ");
					}
				} else if (args[0].equals("table")) {
					if (ae == null) {
						jta1.append("Please load program.\ncompile> ");
						textField.setText("");
						return;
					} else {
						if (ae.yflag) {
							jta1.append(ae.output2() + "\ncompile> ");
						} else {
							jta1.append(ae.se + "\ncompile> ");
						}
					}
				} else if (args[0].equals("code")) {
					if (ae == null) {
						jta1.append("Please load program.\ncompile> ");
						textField.setText("");
						return;
					} else {
						if (ae.yflag) {
							jta1.append(ae.output3() + "\ncompile> ");
						} else {
							jta1.append(ae.se + "\ncompile> ");
						}
					}
				} else if (args[0].equals("run")) {
					if (ae == null) {
						jta1.append("Please load program.\ncompile> ");
						textField.setText("");
						return;
					} else {
						if (!ae.gflag) {
							jta1.append("compile> ");
							textField.setText("");
							return;
						}
						if (ae.yflag) {
							String[] sr = new String[length - 1];
							for (int i = 1; i < length; i++) {
								sr[i - 1] = args[i];
							}
							ae.run(sr);
							jta1.append(ae.run + "\ncompile> ");
						} else {
							jta1.append(ae.se + "\ncompile> ");
						}
					}
				} else if (args[0].equals("debug")) {
					if (ae == null) {
						jta1.append("Please load program.\ncompile> ");
						textField.setText("");
						return;
					} else {
						if (!ae.gflag) {
							jta1.append("compile> ");
							textField.setText("");
							return;
						}
						if (ae.yflag) {
							String[] sr = new String[length - 1];
							for (int i = 1; i < length; i++) {
								sr[i - 1] = args[i];
							}
							ae.debug(sr);
							jta1.append(ae.debug + "\ncompile> ");
						} else {
							jta1.append(ae.se + "\ncompile> ");
						}
					}
				} else if (args[0].equals("edit")) {
					jta1.setEditable(true);
					textField.setEditable(false);
					if (ae == null) {
						jta1.setText("");
					} else {
						jta1.setText(ae.prog);
					}
				} else if (args[0].equals("save")) {
					if (length != 2) {
						jta1.append("Syntax Error.\ncompile> ");
					} else {
						if (ed != null) {
							try {
								fos = new FileOutputStream(args[1]);
								osw = new OutputStreamWriter(fos);
								osw.write(ed);
								osw.flush();
								osw.close();
								fos.close();
								jta1.append("Save successfully.\ncompile> ");
							} catch (IOException e1) {
								jta1.append("\nNot exist file.\ncompile> ");
								return;
							}
						} else
							jta1.append("\ncompile> ");
					}
				} else if (args[0].equals("format")) {
					int tab = 0;
					boolean be = false;
					if (ae == null) {
						jta1.append("Please load program\ncompile> ");
						textField.setText("");
						return;
					}
					jta1.setText("");
					jta1.setEditable(true);
					textField.setEditable(false);
					for (int i = 0; i < ae.sym.size(); i++) {
						if (ae.sym.get(i).type == 1) {
							jta1.append(ae.id.get(ae.sym.get(i).addr).toLowerCase());
							if (ae.sym.get(i + 1).type == 7) {
								jta1.append("\n");
								tab--;
								for (int j = 0; j < tab; j++) {
									jta1.append("    ");
								}
							}
						} else if (ae.sym.get(i).type == 2) {
							jta1.append(String.valueOf(ae.num.get(ae.sym.get(i).addr)));
							if (ae.sym.get(i + 1).type == 7) {
								jta1.append("\n");
								tab--;
								for (int j = 0; j < tab; j++) {
									jta1.append("    ");
								}
							}
						} else if (ae.sym.get(i).type == 8 || ae.sym.get(i).type >= 14 && ae.sym.get(i).type <= 29) {
							jta1.append(ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase());
							if (ae.sym.get(i + 1).type == 7) {
								jta1.append("\n");
								tab--;
								for (int j = 0; j < tab; j++) {
									jta1.append("    ");
								}
							}
						} else if (ae.sym.get(i).type == 3 || ae.sym.get(i).type == 4 || ae.sym.get(i).type == 5
								|| ae.sym.get(i).type == 9 || ae.sym.get(i).type == 11 || ae.sym.get(i).type == 12) {
							jta1.append(ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase() + " ");
						} else if (ae.sym.get(i).type == 6) {
							jta1.append(ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase() + "\n");
							if (ae.sym.get(i + 1).type != 7) {
								tab++;
							}
							for (int j = 0; j < tab; j++) {
								jta1.append("    ");
							}
						} else if (ae.sym.get(i).type == 7) {
							jta1.append(ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase());
							if (ae.sym.get(i + 1).type == 30 || ae.sym.get(i + 1).type == 31) {
								continue;
							} else if (ae.sym.get(i + 1).type == 7) {
								jta1.append("\n");
								tab--;
							} else {
								jta1.append("\n");
							}
							for (int j = 0; j < tab; j++) {
								jta1.append("    ");
							}
						} else if (ae.sym.get(i).type == 10 || ae.sym.get(i).type == 13) {
							jta1.append(" " + ae.reserved[ae.sym.get(i).type - 3].s.toUpperCase() + "\n");
							if (ae.sym.get(i + 1).type != 6) {
								tab++;
								be = true;
							}
							for (int j = 0; j < tab; j++) {
								jta1.append("    ");
							}
						} else if (ae.sym.get(i).type == 30) {
							jta1.append(ae.reserved[ae.sym.get(i).type - 3].s + "\n");
							if (ae.sym.get(i + 1).type == 7) {
								tab--;
							}
							if (be) {
								tab--;
								be = false;
							}
							for (int j = 0; j < tab; j++) {
								jta1.append("    ");
							}
						} else if (ae.sym.get(i).type == 31) {
							jta1.append(ae.reserved[ae.sym.get(i).type - 3].s + "\n");
						}
					}
				} else {
					jta1.append("Input help to get help.\ncompile> ");
				}
				textField.setText("");
			}
		}
	}

}
