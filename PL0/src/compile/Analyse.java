package compile;

import java.io.*;
import java.util.*;

public class Analyse {
	ArrayList<Codef> sym = new ArrayList<Codef>();
	ArrayList<String> id = new ArrayList<String>();
	ArrayList<Integer> num = new ArrayList<Integer>();
	Reserve[] reserved = { new Reserve("const", 3), new Reserve("var", 4), new Reserve("procedure", 5),
			new Reserve("begin", 6), new Reserve("end", 7), new Reserve("odd", 8), new Reserve("if", 9),
			new Reserve("then", 10), new Reserve("call", 11), new Reserve("while", 12), new Reserve("do", 13),
			new Reserve("read", 14), new Reserve("write", 15), new Reserve("+", 16), new Reserve("-", 17),
			new Reserve("*", 18), new Reserve("/", 19), new Reserve("=", 20), new Reserve("#", 21),
			new Reserve("<", 22), new Reserve("<=", 23), new Reserve(">", 24), new Reserve(">=", 25),
			new Reserve(":=", 26), new Reserve("(", 27), new Reserve(")", 28), new Reserve(",", 29),
			new Reserve(";", 30), new Reserve(".", 31) };
	boolean gflag, yflag;
	private FileInputStream fis;
	private InputStreamReader isr;
	private BufferedReader br;
	private String file;
	private int ei;
	private int i, tx, dx, tp;
	private ArrayList<Table> table = new ArrayList<Table>();
	private ArrayList<Objcode> code = new ArrayList<Objcode>();
	private ArrayList<Childp> cp = new ArrayList<Childp>();
	private ArrayList<Integer> jad = new ArrayList<Integer>();
	private Stack<Codef> stack = new Stack<Codef>();
	private int[] s;
	private final int length = 300;
	private Objcode l;
	private int p, t, b;
	String se, debug, prog, run;

	public Analyse(String file) {
		sym.clear();
		id.clear();
		num.clear();
		gflag = true;
		this.file = file;
		ei = 0;
		yflag = true;
		table.clear();
		code.clear();
		cp.clear();
		jad.clear();
		stack.clear();
		i = 0;
		tx = 0;
		dx = 3;
		tp = 0;
		s = new int[length];
		p = 0;
		t = -1;
		b = 0;
		s[0] = -1;
		s[1] = -1;
		s[2] = -1;
	}

	void getch() {
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			int cl = 1;
			prog = "";
			String line = br.readLine();
			String temp = "";
			while (line != null) {
				prog += line + "\n";
				char[] ch = line.toCharArray();
				int n = ch.length;
				int i = 0;
				while (i < n) {
					temp = "";
					if (isLetter(ch[i])) {
						boolean b = false;
						Reserve r = null;
						temp += ch[i];
						i++;
						while (i < n && (isLetter(ch[i]) || isDigit(ch[i]))) {
							temp += ch[i];
							i++;
						}
						for (int j = 0; j < reserved.length; j++) {
							if (temp.equalsIgnoreCase(reserved[j].s)) {
								b = true;
								r = reserved[j];
								break;
							}
						}
						if (!b) {
							boolean b1 = false;
							int j1 = -1;
							for (int j = 0; j < id.size(); j++) {
								if (temp.equalsIgnoreCase(id.get(j))) {
									b1 = true;
									j1 = j;
									break;
								}
							}
							if (!b1) {
								id.add(temp.toLowerCase());
								sym.add(new Codef(1, id.size() - 1));
							} else {
								sym.add(new Codef(1, j1));
							}
						} else {
							sym.add(new Codef(r.type, 0));
						}
					} else if (isDigit(ch[i])) {
						temp += ch[i];
						i++;
						while (i < n && isDigit(ch[i])) {
							temp += ch[i];
							i++;
						}
						num.add(Integer.valueOf(temp));
						sym.add(new Codef(2, num.size() - 1));
					} else if (ch[i] == '+') {
						sym.add(new Codef(16, 0));
						i++;
					} else if (ch[i] == '-') {
						sym.add(new Codef(17, 0));
						i++;
					} else if (ch[i] == '*') {
						sym.add(new Codef(18, 0));
						i++;
					} else if (ch[i] == '/') {
						sym.add(new Codef(19, 0));
						i++;
					} else if (ch[i] == '=') {
						sym.add(new Codef(20, 0));
						i++;
					} else if (ch[i] == '#') {
						sym.add(new Codef(21, 0));
						i++;
					} else if (ch[i] == '<') {
						i++;
						if (ch[i] == '=') {
							sym.add(new Codef(23, 0));
							i++;
						} else
							sym.add(new Codef(22, 0));
					} else if (ch[i] == '>') {
						i++;
						if (ch[i] == '=') {
							sym.add(new Codef(25, 0));
							i++;
						} else
							sym.add(new Codef(24, 0));
					} else if (ch[i] == '(') {
						sym.add(new Codef(27, 0));
						i++;
					} else if (ch[i] == ')') {
						sym.add(new Codef(28, 0));
						i++;
					} else if (ch[i] == ',') {
						sym.add(new Codef(29, 0));
						i++;
					} else if (ch[i] == ';') {
						sym.add(new Codef(30, 0));
						i++;
					} else if (ch[i] == '.') {
						sym.add(new Codef(31, 0));
						i++;
					} else if (ch[i] == ':') {
						i++;
						if (ch[i] == '=') {
							sym.add(new Codef(26, 0));
							i++;
						} else {
							error(ch[i - 1], cl, i);
						}
					} else if (ch[i] == ' ' || ch[i] == '\t') {
						i++;
					} else {
						error(ch[i], cl, i + 1);
						i++;
					}
				}
				line = br.readLine();
				cl++;
			}
			br.close();
			isr.close();
			fis.close();
		} catch (IOException e) {
			gflag = false;
		}

	}

	String output1() {
		String s;
		s = "[sym]\nindex\tkind\tval\n";
		for (int i = 0; i < sym.size(); i++) {
			if (sym.get(i).type == 1) {
				s += i + "\tID\t" + id.get(sym.get(i).addr) + "\n";
			} else if (sym.get(i).type == 2) {
				s += i + "\tNUM\t" + num.get(sym.get(i).addr) + "\n";
			} else {
				s += i + "\tRW\t" + reserved[sym.get(i).type - 3].s + "\n";
			}
		}
		return s;
	}

	private boolean isDigit(char c) {
		if (c >= '0' && c <= '9')
			return true;
		return false;
	}

	private boolean isLetter(char c) {
		if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')
			return true;
		return false;
	}

	private void error(char c, int l, int i) {
		gflag = false;
		if (ei++ == 0) {
			se = "[error]\nchar\trow\tcol\n";
		}
		se += c + "\t" + l + "\t" + i + "\n";
	}

	class Reserve {
		String s;
		int type;

		public Reserve(String s, int type) {
			this.s = s;
			this.type = type;
		}
	}

	class Codef {
		int type;
		int addr;

		public Codef(int type, int addr) {
			this.type = type;
			this.addr = addr;
		}
	}

	void s() {
		if (sym.size() == 0) {
			error(0, 0);
			sym.add(new Codef(-1, 0));
		}
		if (!(sym.get(sym.size() - 1).type == 31)) {
			error(sym.size() - 1, 0);
			sym.add(new Codef(-1, 0));
		}
		code.add(gen(6, 0, -1));
		sp();
		if (sym.get(i).type == 31) {
			i++;
		} else {
			error(i, 0);
		}
		if (i != sym.size()) {
			error(i, 1);
		}
		int ad = -1;
		for (int j = 0; j < cp.size(); j++) {
			if (cp.get(j).lev == 0) {
				ad = cp.get(j).index;
				break;
			}
		}
		code.get(0).a = ad;
	}

	private void sp() {
		if (sym.get(i).type == 3) {
			i++;
			sp1();
		}
		if (sym.get(i).type == 4) {
			i++;
			sp2();
		}
		sp3();
		int as = 3;
		for (int j = 0; j < table.size(); j++) {
			if (table.get(j).kind == 1 && table.get(j).valev == tx && table.get(j).tp == tp) {
				as++;
			}
		}
		code.add(gen(5, 0, as));
		if (tx > 0) {
			for (int j = 0; j < table.size(); j++) {
				if (table.get(j).kind == 2 && table.get(j).valev == tx - 1 && table.get(j).tp == tp) {
					table.get(j).adr = code.size() - 1;
				}
			}
		}
		cp.add(new Childp(tx, code.size() - 1, tp));
		st();
		code.add(gen(8, 0, 0));
	}

	private void sp1() {
		Table t = new Table();
		t.kind = 0;
		t.tp = tp;
		if (sym.get(i).type == 1) {
			t.name = sym.get(i).addr;
			i++;
			if (sym.get(i).type == 20) {
				i++;
				if (sym.get(i).type == 2) {
					t.valev = num.get(sym.get(i).addr);
					i++;
					boolean b = true;
					for (int j = 0; j < table.size(); j++) {
						if (t.name == table.get(j).name) {
							b = false;
							break;
						}
					}
					if (b) {
						table.add(t);
					} else {
						error(i - 3, 2);
					}
					if (sym.get(i).type == 29) {
						i++;
						sp1();
					} else if (sym.get(i).type == 30) {
						i++;
						return;
					} else {
						error(i, 3);
					}
				} else {
					error(i, 3);
				}
			} else {
				error(i, 3);
			}
		} else {
			error(i, 3);
		}
	}

	private void sp2() {
		Table t = new Table();
		t.kind = 1;
		t.valev = tx;
		t.adr = dx;
		t.tp = tp;
		if (sym.get(i).type == 1) {
			t.name = sym.get(i).addr;
			i++;
			dx++;
			boolean b = true;
			for (int j = 0; j < table.size(); j++) {
				if (t.name == table.get(j).name) {
					b = false;
				}
			}
			if (b) {
				table.add(t);
			} else {
				error(i - 1, 2);
			}
			if (sym.get(i).type == 29) {
				i++;
				sp2();
			} else if (sym.get(i).type == 30) {
				i++;
				return;
			} else {
				error(i, 3);
			}
		} else {
			error(i, 3);
		}
	}

	private void sp3() {
		if (sym.get(i).type == 5) {
			i++;
			Table t = new Table();
			t.kind = 2;
			t.valev = tx++;
			t.tp = ++tp;
			dx = 3;
			if (sym.get(i).type == 1) {
				t.name = sym.get(i).addr;
				i++;
				boolean b = true;
				for (int j = 0; j < table.size(); j++) {
					if (t.name == table.get(j).name) {
						b = false;
					}
				}
				if (b) {
					table.add(t);
				} else {
					error(i - 1, 2);
				}
				if (sym.get(i).type == 30) {
					i++;
					sp();
					if (sym.get(i).type == 30) {
						i++;
						tx--;
						sp3();
						tp--;
						return;
					} else {
						error(i, 3);
					}
				} else {
					error(i, 3);
				}
			} else {
				error(i, 3);
			}
		} else {
			return;
		}
	}

	private void st() {
		if (sym.get(i).type == 1) {
			Codef cf = sym.get(i);
			i++;
			if (sym.get(i).type == 26) {
				i++;
				int ti = i;
				stack.clear();
				es();
				while (!stack.isEmpty()) {
					Codef c = stack.pop();
					if (c.type == 16) {
						code.add(gen(8, 0, 1));
					} else if (c.type == 17) {
						code.add(gen(8, 0, 2));
					}
				}
				int k = -1, j1 = -1;
				for (int j = 0; j < table.size(); j++) {
					if (table.get(j).name == cf.addr) {
						k = table.get(j).kind;
						j1 = j;
						break;
					}
				}
				if (k == 1) {
					code.add(gen(3, tx - table.get(j1).valev, table.get(j1).adr));
				} else {
					error(ti - 2, 4);
				}
				return;
			} else {
				error(i, 3);
			}
		} else if (sym.get(i).type == 11) {
			i++;
			if (sym.get(i).type == 1) {
				Codef cf = sym.get(i);
				int k = -1, j1 = -1;
				for (int j = 0; j < table.size(); j++) {
					if (table.get(j).name == cf.addr) {
						k = table.get(j).kind;
						j1 = j;
						break;
					}
				}
				if (k == 2) {
					int ad = -1;
					for (int j = 0; j < cp.size(); j++) {
						if (table.get(j1).valev + 1 == cp.get(j).lev && table.get(j1).tp == cp.get(j).tp) {
							ad = cp.get(j).index;
						}
					}
					if (tx - table.get(j1).valev >= 0) {
						code.add(gen(4, tx - table.get(j1).valev, ad));
					} else {
						error(i, 5);
					}
				} else {
					error(i, 6);
				}
				i++;
				return;
			} else {
				error(i, 6);
			}
		} else if (sym.get(i).type == 6) {
			i++;
			st();
			st1();
			if (sym.get(i).type == 7) {
				i++;
				return;
			} else {
				error(i, 3);
			}
		} else if (sym.get(i).type == 9) {
			i++;
			cn();
			if (sym.get(i).type == 10) {
				i++;
				st();
				code.get(jad.remove(jad.size() - 1)).a = code.size();
				return;
			} else {
				error(i, 3);
			}
		} else if (sym.get(i).type == 12) {
			i++;
			int wad = code.size();
			cn();
			if (sym.get(i).type == 13) {
				i++;
				st();
				code.add(gen(6, 0, wad));
				code.get(jad.remove(jad.size() - 1)).a = code.size();
				return;
			} else {
				error(i, 3);
			}
		} else if (sym.get(i).type == 14) {
			i++;
			if (sym.get(i).type == 27) {
				i++;
				st2();
				if (sym.get(i).type == 28) {
					i++;
					return;
				} else {
					error(i, 3);
				}
			} else {
				error(i, 3);
			}
		} else if (sym.get(i).type == 15) {
			i++;
			if (sym.get(i).type == 27) {
				i++;
				st3();
				if (sym.get(i).type == 28) {
					i++;
					return;
				} else {
					error(i, 3);
				}

			} else {
				error(i, 3);
			}
		} else
			return;

	}

	private void st1() {
		if (sym.get(i).type == 30) {
			i++;
			st();
			st1();
		} else
			return;
	}

	private void st2() {
		if (sym.get(i).type == 1) {
			Codef cf = sym.get(i);
			int k = -1, j1 = -1;
			for (int j = 0; j < table.size(); j++) {
				if (table.get(j).name == cf.addr) {
					k = table.get(j).kind;
					j1 = j;
					break;
				}
			}
			if (k == 1) {
				code.add(gen(8, 0, 12));
				code.add(gen(3, tx - table.get(j1).valev, table.get(j1).adr));
			} else {
				error(i, 4);
			}
			i++;
			if (sym.get(i).type == 29) {
				i++;
				st2();
			} else
				return;
		} else {
			error(i, 4);
		}
	}

	private void st3() {
		es();
		code.add(gen(8, 0, 13));
		if (sym.get(i).type == 29) {
			i++;
			st3();
		} else
			return;
	}

	private void cn() {
		if (sym.get(i).type == 8) {
			i++;
			es();
			code.add(gen(8, 0, 5));
			code.add(gen(7, 0, -1));
			jad.add(code.size() - 1);
			return;
		} else {
			es();
			if (sym.get(i).type == 20 || sym.get(i).type == 21 || sym.get(i).type == 22 || sym.get(i).type == 23
					|| sym.get(i).type == 24 || sym.get(i).type == 25) {
				Codef cf = sym.get(i);
				i++;
				es();
				code.add(gen(8, 0, cf.type - 14));
				code.add(gen(7, 0, -1));
				jad.add(code.size() - 1);
				return;
			} else {
				error(i, 7);
			}
		}

	}

	private void es() {
		if (sym.get(i).type == 16) {
			i++;
		} else if (sym.get(i).type == 17) {
			code.add(gen(1, 0, 0));
			stack.push(sym.get(i));
			i++;
		}
		im();
		es1();
	}

	private void es1() {
		if (sym.get(i).type == 16) {
			i++;
			im();
			code.add(gen(8, 0, 1));
			es1();
		} else if (sym.get(i).type == 17) {
			i++;
			im();
			code.add(gen(8, 0, 2));
			es1();
		} else
			return;
	}

	private void im() {
		ft();
		im1();

	}

	private void im1() {
		if (sym.get(i).type == 18) {
			i++;
			ft();
			code.add(gen(8, 0, 3));
			im1();
		} else if (sym.get(i).type == 19) {
			i++;
			ft();
			code.add(gen(8, 0, 4));
			im1();
		} else
			return;
	}

	private void ft() {
		if (sym.get(i).type == 1) {
			int k = -1, j1 = -1;
			for (int j = 0; j < table.size(); j++) {
				if (table.get(j).name == sym.get(i).addr) {
					k = table.get(j).kind;
					j1 = j;
					break;
				}
			}
			if (k == 0) {
				code.add(gen(1, 0, table.get(j1).valev));
			} else if (k == 1) {
				code.add(gen(2, tx - table.get(j1).valev, table.get(j1).adr));
			} else {
				error(i, 8);
			}
			i++;
			return;
		} else if (sym.get(i).type == 2) {
			code.add(gen(1, 0, num.get(sym.get(i).addr)));
			i++;
			return;
		} else if (sym.get(i).type == 27) {
			stack.push(sym.get(i));
			i++;
			es();
			if (sym.get(i).type == 28) {
				while (stack.peek().type != 27) {
					Codef c = stack.pop();
					if (c.type == 16) {
						code.add(gen(8, 0, 1));
					} else if (c.type == 17) {
						code.add(gen(8, 0, 2));
					}
				}
				stack.pop();
				i++;
				return;
			} else {
				error(i, 3);
			}
		} else {
			error(i, 3);
		}
	}

	private void error(int i, int tp) {
		yflag = false;
		se = "[error]\nindex\ttype\n" + i + "\t";
		switch (tp) {
		case 0:
			se += "<The end of the program is not '.'>\n";
			break;
		case 1:
			se += "<After the end of the program there are characters>\n";
			break;
		case 2:
			se += "<The identifier has been defined>\n";
			break;
		case 3:
			se += "<Character definition error>\n";
			break;
		case 4:
			se += "<There is no such variable>\n";
			break;
		case 5:
			se += "<The subroutine can not be called>\n";
			break;
		case 6:
			se += "<Not a subroutine name>\n";
			break;
		case 7:
			se += "<There is no such operator>\n";
			break;
		case 8:
			se += "<There is no such variable or constant>\n";
			break;
		default:
			se += "<Unnamed error>\n";
			break;
		}

	}

	private Objcode gen(int f, int l, int a) {
		return new Objcode(f, l, a);
	}

	String output2() {
		String s;
		s = "[table]\nindex\tkind\tname\tval/lev\tadr\n";
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).kind == 0) {
				s += i + "\tconst\t" + id.get(table.get(i).name) + "\t" + table.get(i).valev + "\t-\n";
			} else if (table.get(i).kind == 1) {
				s += i + "\tvar\t" + id.get(table.get(i).name) + "\t" + table.get(i).valev + "\t" + table.get(i).adr
						+ "\n";
			} else if (table.get(i).kind == 2) {
				s += i + "\tproc\t" + id.get(table.get(i).name) + "\t" + table.get(i).valev + "\t" + table.get(i).adr
						+ "\n";
			} else {
				s += i + "\t?\n";
			}
		}
		return s;
	}

	String output3() {
		String s;
		s = "[code]\nindex\tf\tl\ta\n";
		for (int i = 0; i < code.size(); i++) {
			switch (code.get(i).f) {
			case 1:
				s += i + "\tlit\t" + code.get(i).l + "\t" + code.get(i).a + "\n";
				break;
			case 2:
				s += i + "\tlod\t" + code.get(i).l + "\t" + code.get(i).a + "\n";
				break;
			case 3:
				s += i + "\tsto\t" + code.get(i).l + "\t" + code.get(i).a + "\n";
				break;
			case 4:
				s += i + "\tcal\t" + code.get(i).l + "\t" + code.get(i).a + "\n";
				break;
			case 5:
				s += i + "\tint\t" + code.get(i).l + "\t" + code.get(i).a + "\n";
				break;
			case 6:
				s += i + "\tjmp\t" + code.get(i).l + "\t" + code.get(i).a + "\n";
				break;
			case 7:
				s += i + "\tjpc\t" + code.get(i).l + "\t" + code.get(i).a + "\n";
				break;
			case 8:
				String s1;
				switch (code.get(i).a) {
				case 0:
					s1 = "return";
					break;
				case 1:
					s1 = "+";
					break;
				case 2:
					s1 = "-";
					break;
				case 3:
					s1 = "*";
					break;
				case 4:
					s1 = "/";
					break;
				case 5:
					s1 = "odd";
					break;
				case 6:
					s1 = "=";
					break;
				case 7:
					s1 = "#";
					break;
				case 8:
					s1 = "<";
					break;
				case 9:
					s1 = "<=";
					break;
				case 10:
					s1 = ">";
					break;
				case 11:
					s1 = ">=";
					break;
				case 12:
					s1 = "read";
					break;
				case 13:
					s1 = "write";
					break;
				default:
					s1 = "?";
				}
				s += i + "\topr\t" + code.get(i).l + "\t" + code.get(i).a + "\t" + s1 + "\n";
				break;
			default:
				s += i + "\t???\t?\t?\n";
			}
		}
		return s;
	}

	class Table {
		int kind;
		int name;
		int valev;
		int adr;
		int tp;

		public Table() {
			kind = -1;
			name = -1;
			valev = -1;
			adr = -1;
			tp = 0;
		}
	}

	class Childp {
		int lev;
		int index;
		int tp;

		public Childp(int lev, int index, int tp) {
			this.lev = lev;
			this.index = index;
			this.tp = tp;
		}
	}

	class Objcode {
		int f, l, a;

		public Objcode(int f, int l, int a) {
			this.f = f;
			this.l = l;
			this.a = a;
		}
	}

	private void clc() {
		s = new int[length];
		p = 0;
		t = -1;
		b = 0;
		s[0] = -1;
		s[1] = -1;
		s[2] = -1;
	}

	void debug(String[] ins) {
		debug = "[debug]\n";
		int insi = 0;
		clc();
		while (true) {
			if (p == -1) {
				break;
			}
			try {
				l = code.get(p);
				p++;
				String s0;
				switch (l.f) {
				case 1:
					s0 = "lit\t" + l.l + "\t" + l.a;
					t++;
					s[t] = l.a;
					break;
				case 2:
					s0 = "lod\t" + l.l + "\t" + l.a;
					t++;
					s[t] = s[base(l.l) + l.a];
					break;
				case 3:
					s0 = "sto\t" + l.l + "\t" + l.a;
					s[base(l.l) + l.a] = s[t];
					t--;
					break;
				case 4:
					s0 = "cal\t" + l.l + "\t" + l.a;
					s[t + 1] = base(l.l);
					s[t + 2] = b;
					s[t + 3] = p;
					b = t + 1;
					p = l.a;
					break;
				case 5:
					s0 = "int\t" + l.l + "\t" + l.a;
					t = t + l.a;
					break;
				case 6:
					s0 = "jmp\t" + l.l + "\t" + l.a;
					p = l.a;
					break;
				case 7:
					s0 = "jpc\t" + l.l + "\t" + l.a;
					if (s[t] == 0) {
						p = l.a;
					}
					t--;
					break;
				case 8:
					String s1;
					switch (l.a) {
					case 0:
						s1 = "return";
						t = b - 1;
						p = s[t + 3];
						b = s[t + 2];
						break;
					case 1:
						s1 = "+";
						t--;
						s[t] = s[t] + s[t + 1];
						break;
					case 2:
						s1 = "-";
						t--;
						s[t] = s[t] - s[t + 1];
						break;
					case 3:
						s1 = "*";
						t--;
						s[t] = s[t] * s[t + 1];
						break;
					case 4:
						s1 = "/";
						t--;
						try {
							s[t] = s[t] / s[t + 1];
						} catch (ArithmeticException e) {
							p = -1;
							debug += "The divisor is zero!\n";
							break;
						}
						break;
					case 5:
						s1 = "odd";
						if (s[t] % 2 == 1) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 6:
						s1 = "=";
						t--;
						if (s[t] == s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 7:
						s1 = "#";
						t--;
						if (s[t] != s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 8:
						s1 = "<";
						t--;
						if (s[t] < s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 9:
						s1 = "<=";
						t--;
						if (s[t] <= s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 10:
						s1 = ">";
						t--;
						if (s[t] > s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 11:
						s1 = ">=";
						t--;
						if (s[t] >= s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 12:
						s1 = "read";
						t++;
						if (insi >= ins.length) {
							p = -1;
							debug += "Parameter is not enough!\n";
							break;
						}
						try {
							s[t] = Integer.parseInt(ins[insi]);
							debug += "? " + s[t] + "\n";
						} catch (Exception e) {
							t--;
							p--;
						} finally {
							insi++;
						}
						break;
					case 13:
						s1 = "write";
						debug += s[t] + "\n";
						t--;
						break;
					default:
						s1 = "?";
						break;
					}
					s0 = "opr\t" + l.l + "\t" + l.a + "\t" + s1;
					break;
				default:
					s0 = "???\t?\t?";
					break;
				}
				debug += "l:" + s0 + "\np:" + p + "\tt:" + t + "\tb:" + b + "\nStack:\nindex\ts\n";
				for (int i = t; i >= 0; i--) {
					debug += i + "\t" + s[i] + "\n";
				}
				debug += "\n";
			} catch (ArrayIndexOutOfBoundsException e) {
				debug += "Out of memory!\n";
				break;
			}
		}

	}

	void run(String[] ins) {
		run = "[run]\n";
		int insi = 0;
		clc();
		try {
			while (p != -1) {
				l = code.get(p);
				p++;
				switch (l.f) {
				case 1:
					t++;
					s[t] = l.a;
					break;
				case 2:
					t++;
					s[t] = s[base(l.l) + l.a];
					break;
				case 3:
					s[base(l.l) + l.a] = s[t];
					t--;
					break;
				case 4:
					s[t + 1] = base(l.l);
					s[t + 2] = b;
					s[t + 3] = p;
					b = t + 1;
					p = l.a;
					break;
				case 5:
					t = t + l.a;
					break;
				case 6:
					p = l.a;
					break;
				case 7:
					if (s[t] == 0) {
						p = l.a;
					}
					t--;
					break;
				case 8:
					switch (l.a) {
					case 0:
						t = b - 1;
						p = s[t + 3];
						b = s[t + 2];
						break;
					case 1:
						t--;
						s[t] = s[t] + s[t + 1];
						break;
					case 2:
						t--;
						s[t] = s[t] - s[t + 1];
						break;
					case 3:
						t--;
						s[t] = s[t] * s[t + 1];
						break;
					case 4:
						t--;
						try {
							s[t] = s[t] / s[t + 1];
						} catch (ArithmeticException e) {
							run += "The divisor is zero!\n";
							p = -1;
						}
						break;
					case 5:
						if (s[t] % 2 == 1) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 6:
						t--;
						if (s[t] == s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 7:
						t--;
						if (s[t] != s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 8:
						t--;
						if (s[t] < s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 9:
						t--;
						if (s[t] <= s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 10:
						t--;
						if (s[t] > s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 11:
						t--;
						if (s[t] >= s[t + 1]) {
							s[t] = 1;
						} else {
							s[t] = 0;
						}
						break;
					case 12:
						t++;
						if (insi >= ins.length) {
							p = -1;
							run += "Parameter is not enough!\n";
							break;
						}
						try {
							s[t] = Integer.parseInt(ins[insi]);
							run += "? " + s[t] + "\n";
						} catch (Exception e) {
							t--;
							p--;
						} finally {
							insi++;
						}
						break;
					case 13:
						run += s[t] + "\n";
						t--;
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			run += "Out of memory!\n";
		}
	}

	private int base(int ll) {
		int bc = b;
		while (ll > 0) {
			bc = s[bc];
			ll--;
		}
		return bc;
	}

}