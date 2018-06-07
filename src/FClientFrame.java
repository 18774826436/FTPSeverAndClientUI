import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.GridBagConstraints.*;
import javax.swing.*;


public class FClientFrame extends MouseAdapter implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	String local = null;

	JFrame mainframe;
	JMenuBar menubar;
	JPanel toolbarpanel;
	JPanel contentpanel;

	JMenuItem open = new JMenuItem("连接");
	JMenuItem close = new JMenuItem("断开");
	JTextField address;
	JTextField portfield;
	JTextField username;
	JTextField password;
	JButton connectbutton;
	JButton closebutton;
	JButton cdupbutton;
	JButton refreshbutton;
	JButton downloadbutton;
	JButton uploadbutton;

	JList currentfilelist;
	public static JList serverfilelist;
	public static JTextArea info;
	public static Map<String, String> listmap = new HashMap<String, String>();

	public Map<String, Command> commands = new HashMap<String, Command>();
	public Command loginCMD, listCMD, retrCMD, storCMD, cdCMD, typeCMD,
			cdupCMD;
	public Socket socket;
	public PrintWriter output;
	public BufferedReader input;
	BufferedReader in;
	String cmd, param = "";
	String localdir = "D:/";
	String localparentdir = null;

	Color c = new Color(238, 238, 238);

	public FClientFrame() {
		init();
		addCommand();
		loadlocalfile();
	}

	public void init() {
		mainframe = new JFrame("FTP客户端(JAVA)");
		mainframe.setSize(700, 500);
		mainframe.setLocation(400, 300);
		mainframe.setLayout(new BorderLayout());
		MenuSetting();
		mainframe.setJMenuBar(menubar);
		toolbarSetting();
		mainframe.add(toolbarpanel, BorderLayout.NORTH);
		contentSetting();
		mainframe.add(contentpanel, BorderLayout.CENTER);

		mainframe.setVisible(true);
		mainframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent w) {
				System.exit(0);

			}
		});
	}

	public void MenuSetting() {
		menubar = new JMenuBar();
		JMenu file = new JMenu("文件");
		file.add(open);
		file.add(close);
		menubar.add(file);

		close.setEnabled(false);

		open.addActionListener(this);
		close.addActionListener(this);

	}

	public void toolbarSetting() {
		toolbarpanel = new JPanel();

		try {
			 local = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// 下面是地址栏等信息
		JPanel infopanel = new JPanel();
		address = new JTextField(local);
		portfield = new JTextField("4500");
		username = new JTextField();
		password = new JTextField();
		JLabel a = new JLabel("地址：");
		JLabel p = new JLabel("端口：");
		JLabel u = new JLabel("用户名：");
		JLabel pa = new JLabel("密码：");
		GridBagLayout layout = new GridBagLayout();
		infopanel.setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		Constraints(constraints, 0, 0, 1, 1, 0.05, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
		layout.setConstraints(a, constraints);
		infopanel.add(a);
		Constraints(constraints, 1, 0, 1, 1, 0.6, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
		layout.setConstraints(address, constraints);
		infopanel.add(address);
		Constraints(constraints, 2, 0, 1, 1, 0.05, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
		layout.setConstraints(p, constraints);
		infopanel.add(p);
		Constraints(constraints, 3, 0, 1, 1, 0.1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
		layout.setConstraints(portfield, constraints);
		infopanel.add(portfield);
		Constraints(constraints, 4, 0, 1, 1, 0.05, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
		layout.setConstraints(u, constraints);
		infopanel.add(u);
		Constraints(constraints, 5, 0, 1, 1, 0.15, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
		layout.setConstraints(username, constraints);
		infopanel.add(username);
		Constraints(constraints, 6, 0, 1, 1, 0.05, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
		layout.setConstraints(pa, constraints);
		infopanel.add(pa);
		Constraints(constraints, 7, 0, 1, 1, 0.15, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
		layout.setConstraints(password, constraints);
		infopanel.add(password);

		// 下面是快捷键等工具栏
		JPanel toolpanel = new JPanel();
		toolpanel.setBorder(BorderFactory.createEtchedBorder());
		Icon connect = new ImageIcon("src/images/connect.jpg");
		connectbutton = new JButton(connect);

		connectbutton.setBorder(BorderFactory.createEtchedBorder());
		connectbutton.setBackground(c);
		connectbutton.setPreferredSize(new Dimension(30, 30));
		Icon close = new ImageIcon("src/images/close.jpg");
		closebutton = new JButton(close);
		closebutton.setBorder(BorderFactory.createEtchedBorder());
		closebutton.setBackground(c);
		closebutton.setPreferredSize(new Dimension(30, 30));
		cdupbutton = new JButton("上一级");
		cdupbutton.setBackground(c);
		cdupbutton.setBorder(BorderFactory.createEtchedBorder());
		refreshbutton = new JButton("刷新");
		refreshbutton.setBackground(c);
		refreshbutton.setBorder(BorderFactory.createEtchedBorder());
		downloadbutton = new JButton("下载");
		downloadbutton.setBackground(c);
		downloadbutton.setBorder(BorderFactory.createEtchedBorder());
		uploadbutton = new JButton("上传");
		uploadbutton.setBackground(c);
		uploadbutton.setBorder(BorderFactory.createEtchedBorder());

		toolpanel.setLayout(layout);
		Constraints(constraints, 0, 0, 1, 1, 0.02, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(connectbutton, constraints);
		toolpanel.add(connectbutton);
		Constraints(constraints, 1, 0, 1, 1, 0.02, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(closebutton, constraints);
		toolpanel.add(closebutton);
		Constraints(constraints, 2, 0, 1, 1, 0.1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(cdupbutton, constraints);
		toolpanel.add(cdupbutton);
		Constraints(constraints, 3, 0, 1, 1, 0.1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(refreshbutton, constraints);
		toolpanel.add(refreshbutton);
		Constraints(constraints, 4, 0, 1, 1, 0.1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(downloadbutton, constraints);
		toolpanel.add(downloadbutton);
		Constraints(constraints, 5, 0, 1, 1, 0.1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(uploadbutton, constraints);
		toolpanel.add(uploadbutton);
		JLabel space = new JLabel();
		Constraints(constraints, 6, 0, 1, 1, 0.56, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
		layout.setConstraints(space, constraints);
		toolpanel.add(space);

		toolbarpanel.setLayout(layout);
		Constraints(constraints, 0, 0, 1, 1, 0.55, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(toolpanel, constraints);
		toolbarpanel.add(toolpanel);
		Constraints(constraints, 0, 1, 1, 1, 0.45, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(infopanel, constraints);
		toolbarpanel.add(infopanel);

		closebutton.setEnabled(false);
		cdupbutton.setEnabled(false);
		refreshbutton.setEnabled(false);
		downloadbutton.setEnabled(false);
		uploadbutton.setEnabled(false);

		connectbutton.addActionListener(this);
		closebutton.addActionListener(this);
		cdupbutton.addActionListener(this);
		refreshbutton.addActionListener(this);
		downloadbutton.addActionListener(this);
		uploadbutton.addActionListener(this);
	}

	public void contentSetting() {
		contentpanel = new JPanel();
		currentfilelist = new JList();
		serverfilelist = new JList();
		JPanel panel = new JPanel();

		info = new JTextArea();
		info.setEditable(false);
		info.setBackground(c);
		JScrollPane infopanel = new JScrollPane(info);
		infopanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		JScrollPane scrolpanel1 = new JScrollPane(currentfilelist);
		scrolpanel1
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrolpanel1.setBorder(BorderFactory.createLineBorder(Color.gray));
		JScrollPane scrolpanel2 = new JScrollPane(serverfilelist);
		scrolpanel2
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrolpanel2.setBorder(BorderFactory.createLineBorder(Color.gray));
		panel.setLayout(layout);
		Constraints(constraints, 0, 0, 1, 1, 0.3, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(scrolpanel1, constraints);
		panel.add(scrolpanel1);
		Constraints(constraints, 1, 0, 1, 1, 0.7, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(scrolpanel2, constraints);
		panel.add(scrolpanel2);

		contentpanel.setLayout(layout);
		Constraints(constraints, 0, 0, 1, 1, 1, 0.65, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(panel, constraints);
		contentpanel.add(panel);
		Constraints(constraints, 0, 1, 1, 1, 1, 0.35, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		layout.setConstraints(infopanel, constraints);
		contentpanel.add(infopanel);

		currentfilelist.addMouseListener(this);
	}

	public void Constraints(GridBagConstraints gbc, int gx, int gy, int gw,
			int gh, double wx, double wy, int fill, int anchor) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.fill = fill;
		gbc.anchor = anchor;
	}

	public void addCommand() {
		listCMD = new ListCommand();
		retrCMD = new GetfileCommand();
		storCMD = new UpfileCommand();
		cdCMD = new CdCommand();
		typeCMD = new TypeCommand();
		cdupCMD = new CdupCommand();
		commands.put("ls", listCMD);
		commands.put("get", retrCMD);
		commands.put("put", storCMD);
		commands.put("cd", cdCMD);
		commands.put("TYPE", typeCMD);
		commands.put("CDUP", cdupCMD);
	}

	public void Login() {

		String loginname = "";
		String pwd = "";
		loginname = username.getText().trim();

		output.println("USER " + loginname);
		output.flush();

		pwd = password.getText().trim();
		output.println("PASS " + pwd);
		output.flush();

	}

	public boolean openConnection() {

		if (!address.getText().equals(local))
		{
			System.out.println(local+address.getText());
			return false;
		}
		try {
			socket = new Socket(address.getText().trim(), Integer
					.parseInt(portfield.getText().trim()));
			output = new PrintWriter(socket.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			getMsg();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public void getMsg() {
		try {
			CtrlListen listener = new CtrlListen(input);
			Thread Listenerthread = new Thread(listener);
			Listenerthread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadlocalfile() {
		File file = new File(localdir);
		File[] filelist = file.listFiles();
		Vector<String> v = new Vector<String>();

		currentfilelist.removeAll();
		if (file.getParent() == null) {
		} else {
			v.add(file.getParent());
		}
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].isDirectory()) {
				v.add(filelist[i].getName() + "/");
			} else {
				v.add(filelist[i].getName());
			}
		}
		currentfilelist.setListData(v);
	}

	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();
		if (source == connectbutton || source == open) {
			if (openConnection()) {
				Login();
				serverfilelist.addMouseListener(this);
				Command command = commands.get("ls");
				command.execute(param, output);

				close.setEnabled(true);
				closebutton.setEnabled(true);
				cdupbutton.setEnabled(true);
				refreshbutton.setEnabled(true);
				downloadbutton.setEnabled(true);
				uploadbutton.setEnabled(true);
			} else {
				System.out.println("登录信息错误");
				//System.exit(1);
				FClientFrame fm2=new FClientFrame();
			}
		} else {
			System.out.println("服务器在运行中");
		}

		if (source == closebutton || source == close) {
			output.println("QUIT");
			Vector<String> ve = new Vector<String>();
			serverfilelist.removeAll();
			serverfilelist.setListData(ve);
			serverfilelist.removeMouseListener(this);

		}
		if (source == cdupbutton) {
			Command command = commands.get("CDUP");
			command.execute(param, output);
		}
		if (source == refreshbutton) {
			Command command = commands.get("ls");
			command.execute(param, output);
		}
		if (source == uploadbutton) {
			if (!currentfilelist.isSelectionEmpty()) {
				String s = (String) currentfilelist.getSelectedValue();
				param = localdir + s;
				File file = new File(param);
				if (!file.isDirectory()) {
					int i = JOptionPane.showConfirmDialog(mainframe, "确定上传？",
							"提醒", JOptionPane.YES_NO_OPTION);
					if (i == JOptionPane.YES_OPTION) {
						// System.out.println(param);
						Command command = commands.get("put");
						command.execute(param, output);
					}
				} else {
				}
			} else {
				JOptionPane.showMessageDialog(mainframe, "没有选中本地文件");
			}
		}
		if (source == downloadbutton) {
			if (!serverfilelist.isSelectionEmpty()) {
				String s = (String) serverfilelist.getSelectedValue();
				if (listmap.get(s).startsWith("-")) {
					int i = JOptionPane.showConfirmDialog(mainframe, "确定下载？",
							"提醒", JOptionPane.YES_NO_OPTION);
					if (i == JOptionPane.YES_OPTION) {
						param = localdir + s;
						// System.out.println(param);
						Command command = commands.get("get");
						command.execute(param, output);
					}
				} else {
				}
				param = localdir + s;
			} else {
				JOptionPane.showMessageDialog(mainframe, "没有选中要下载的文件");
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		Vector<String> v = new Vector<String>();
		Object source = e.getSource();
		int clickcount = e.getClickCount();
		if ((clickcount == 2) && (source == currentfilelist)) {
			String s = (String) currentfilelist.getSelectedValue();
			if (s.equals(localparentdir)) {
				File file = new File(localparentdir);
				if (file.isDirectory()) {
					localdir = s;
					localparentdir = file.getParent();
					File[] files = file.listFiles();
					v.add(file.getParent());
					for (int i = 0; i < files.length; i++) {
						if (files[i].isDirectory()) {
							v.add(files[i].getName() + "/");
						} else {
							v.add(files[i].getName());
						}
					}
					currentfilelist.removeAll();
					currentfilelist.setListData(v);
				}
			} else {
				File file = new File(localdir + s);
				if (file.isDirectory()) {
					localdir += s;
					localparentdir = file.getParent();
					File[] files = file.listFiles();
					v.add(file.getParent());
					for (int i = 0; i < files.length; i++) {
						if (files[i].isDirectory()) {
							v.add(files[i].getName() + "/");
						} else {
							v.add(files[i].getName());
						}
					}
					currentfilelist.removeAll();
					currentfilelist.setListData(v);
				}
			}
		}
		if ((clickcount == 2) && (source == serverfilelist)) {
			String s = (String) serverfilelist.getSelectedValue();
			if (listmap.get(s).startsWith("d")) {
				System.out.println("目录");
				param = s;
				Command command1 = commands.get("cd");
				command1.execute(param, output);
				Command command2 = commands.get("ls");
				command2.execute(param, output);
			} else if (listmap.get(s).startsWith("-")) {
				System.out.println("文件");
			}
		}
	}

	// 测试用
	public static void main(String[] art) {

		FClientFrame fm =new FClientFrame();

	}

}

/**
 * 
 * @author 似水年华
 * 
 */
class CtrlListen implements Runnable {
	BufferedReader buffer = null;

	public CtrlListen(BufferedReader in) {
		buffer = in;
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				String s = buffer.readLine();
				FClientFrame.info.append(s + "\n");
				String[] s1 = s.trim().split(" ");
				if (s1[0].equals("530")) {
					System.out.println("密码或用户名不正确，系统退出");
					System.exit(1);
				}
			} catch (Exception e) {
				System.exit(1);
			}
		}

	}

}
