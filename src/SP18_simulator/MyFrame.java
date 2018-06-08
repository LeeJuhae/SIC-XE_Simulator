package SP18_simulator;
import javax.swing.*;
import javax.swing.border.*;

public class MyFrame extends JFrame {

	JPanel contentPane;
	JButton btnOpen;
	JButton btnRunStep;
	JButton btnRunAll;
	JButton btnExit;
	JTextField fileNameText;
	JTextField progNameText;
	JTextField startAddrOP;
	JTextField progLength;
	JTextField regA_dec;
	JTextField regA_hex;
	JTextField regX_dec;
	JTextField regX_hex;
	JTextField regL_dec;
	JTextField regL_hex;
	JTextField regPC_dec;
	JTextField regPC_hex;
	JTextField regSW;
	JTextField regB_dec;
	JTextField regB_hex;
	JTextField regS_dec;
	JTextField regS_hex;
	JTextField regT_dec;
	JTextField regT_hex;
	JTextField regF;
	JTextField firstInstAddr;
	JTextField startAddrMem;
	JTextField TA;
	JTextField usingDevice;

	/**
	 * Create the frame.
	 */
	public MyFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 900);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "H (Header Record)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(14, 42, 342, 140);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblProgramName = new JLabel("Program Name :");
		lblProgramName.setBounds(14, 26, 105, 18);
		panel.add(lblProgramName);
		
		progNameText = new JTextField();
		progNameText.setEditable(false);
		progNameText.setBounds(133, 23, 116, 24);
		panel.add(progNameText);
		progNameText.setColumns(10);
		
		JLabel lblStartAddressOf = new JLabel("Start Address of");
		lblStartAddressOf.setBounds(14, 55, 116, 18);
		panel.add(lblStartAddressOf);
		
		JLabel lblObjectProgram = new JLabel("Object Program :");
		lblObjectProgram.setBounds(37, 72, 116, 18);
		panel.add(lblObjectProgram);
		
		startAddrOP = new JTextField();
		startAddrOP.setEditable(false);
		startAddrOP.setBounds(159, 69, 116, 24);
		panel.add(startAddrOP);
		startAddrOP.setColumns(10);
		
		JLabel lblLengthOfProgram = new JLabel("Length of Program :");
		lblLengthOfProgram.setBounds(14, 102, 131, 18);
		panel.add(lblLengthOfProgram);
		
		progLength = new JTextField();
		progLength.setEditable(false);
		progLength.setBounds(159, 102, 116, 24);
		panel.add(progLength);
		progLength.setColumns(10);
		
		JLabel lblFilename = new JLabel("FileName :");
		lblFilename.setBounds(14, 12, 77, 18);
		contentPane.add(lblFilename);
		
		fileNameText = new JTextField();
		fileNameText.setEditable(false);
		fileNameText.setBounds(105, 9, 91, 24);
		contentPane.add(fileNameText);
		fileNameText.setColumns(10);
		
		btnOpen = new JButton("open");
		btnOpen.setBounds(212, 8, 77, 27);
		contentPane.add(btnOpen);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Register", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(14, 194, 342, 233);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Dec");
		lblNewLabel.setBounds(124, 23, 62, 18);
		panel_1.add(lblNewLabel);
		
		JLabel lblHex = new JLabel("Hex");
		lblHex.setBounds(247, 23, 62, 18);
		panel_1.add(lblHex);
		
		JLabel lblA = new JLabel("A (#0)");
		lblA.setBounds(14, 53, 62, 18);
		panel_1.add(lblA);
		
		regA_dec = new JTextField();
		regA_dec.setEditable(false);
		regA_dec.setBounds(88, 50, 116, 24);
		panel_1.add(regA_dec);
		regA_dec.setColumns(10);
		
		regA_hex = new JTextField();
		regA_hex.setEditable(false);
		regA_hex.setBounds(218, 50, 116, 24);
		panel_1.add(regA_hex);
		regA_hex.setColumns(10);
		
		JLabel lblX = new JLabel("X (#1)");
		lblX.setBounds(14, 89, 62, 18);
		panel_1.add(lblX);
		
		regX_dec = new JTextField();
		regX_dec.setEditable(false);
		regX_dec.setBounds(88, 86, 116, 24);
		panel_1.add(regX_dec);
		regX_dec.setColumns(10);
		
		regX_hex = new JTextField();
		regX_hex.setEditable(false);
		regX_hex.setBounds(218, 86, 116, 24);
		panel_1.add(regX_hex);
		regX_hex.setColumns(10);
		
		JLabel lblL = new JLabel("L (#2)");
		lblL.setBounds(14, 125, 62, 18);
		panel_1.add(lblL);
		
		JLabel lblNewLabel_1 = new JLabel("PC (#8)");
		lblNewLabel_1.setBounds(14, 164, 62, 18);
		panel_1.add(lblNewLabel_1);
		
		JLabel lblSw = new JLabel("SW (#9)");
		lblSw.setBounds(14, 201, 62, 18);
		panel_1.add(lblSw);
		
		regL_dec = new JTextField();
		regL_dec.setEditable(false);
		regL_dec.setBounds(88, 122, 116, 24);
		panel_1.add(regL_dec);
		regL_dec.setColumns(10);
		
		regL_hex = new JTextField();
		regL_hex.setEditable(false);
		regL_hex.setBounds(218, 122, 116, 24);
		panel_1.add(regL_hex);
		regL_hex.setColumns(10);
		
		regPC_dec = new JTextField();
		regPC_dec.setEditable(false);
		regPC_dec.setBounds(88, 161, 116, 24);
		panel_1.add(regPC_dec);
		regPC_dec.setColumns(10);
		
		regPC_hex = new JTextField();
		regPC_hex.setEditable(false);
		regPC_hex.setBounds(218, 161, 116, 24);
		panel_1.add(regPC_hex);
		regPC_hex.setColumns(10);
		
		regSW = new JTextField();
		regSW.setEditable(false);
		regSW.setBounds(90, 198, 244, 24);
		panel_1.add(regSW);
		regSW.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(null, "Register(for XE)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(14, 439, 342, 203);
		contentPane.add(panel_2);
		
		JLabel label = new JLabel("Dec");
		label.setBounds(124, 23, 62, 18);
		panel_2.add(label);
		
		JLabel label_1 = new JLabel("Hex");
		label_1.setBounds(247, 23, 62, 18);
		panel_2.add(label_1);
		
		JLabel lblB = new JLabel("B (#3)");
		lblB.setBounds(14, 53, 62, 18);
		panel_2.add(lblB);
		
		regB_dec = new JTextField();
		regB_dec.setEditable(false);
		regB_dec.setColumns(10);
		regB_dec.setBounds(88, 50, 116, 24);
		panel_2.add(regB_dec);
		
		regB_hex = new JTextField();
		regB_hex.setEditable(false);
		regB_hex.setColumns(10);
		regB_hex.setBounds(218, 50, 116, 24);
		panel_2.add(regB_hex);
		
		JLabel lblS = new JLabel("S (#4)");
		lblS.setBounds(14, 89, 62, 18);
		panel_2.add(lblS);
		
		regS_dec = new JTextField();
		regS_dec.setEditable(false);
		regS_dec.setColumns(10);
		regS_dec.setBounds(88, 86, 116, 24);
		panel_2.add(regS_dec);
		
		regS_hex = new JTextField();
		regS_hex.setEditable(false);
		regS_hex.setColumns(10);
		regS_hex.setBounds(218, 86, 116, 24);
		panel_2.add(regS_hex);
		
		JLabel lblT = new JLabel("T (#5)");
		lblT.setBounds(14, 125, 62, 18);
		panel_2.add(lblT);
		
		JLabel lblF = new JLabel("F (#6)");
		lblF.setBounds(14, 161, 62, 18);
		panel_2.add(lblF);
		
		regT_dec = new JTextField();
		regT_dec.setEditable(false);
		regT_dec.setColumns(10);
		regT_dec.setBounds(88, 122, 116, 24);
		panel_2.add(regT_dec);
		
		regT_hex = new JTextField();
		regT_hex.setEditable(false);
		regT_hex.setColumns(10);
		regT_hex.setBounds(218, 122, 116, 24);
		panel_2.add(regT_hex);
		
		regF = new JTextField();
		regF.setEditable(false);
		regF.setColumns(10);
		regF.setBounds(88, 158, 244, 24);
		panel_2.add(regF);
		
		JLabel lblLogaboutPerforming = new JLabel("Log (About Performing Instructions)");
		lblLogaboutPerforming.setBounds(14, 654, 239, 18);
		contentPane.add(lblLogaboutPerforming);
		
		JList list = new JList();
		list.setBounds(14, 684, 697, 163);
		contentPane.add(list);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "E (End Record)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(369, 53, 325, 78);
		contentPane.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Address of First Instruction");
		lblNewLabel_2.setBounds(14, 22, 184, 18);
		panel_3.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("in Object Program :");
		lblNewLabel_3.setBounds(24, 42, 137, 18);
		panel_3.add(lblNewLabel_3);
		
		firstInstAddr = new JTextField();
		firstInstAddr.setEditable(false);
		firstInstAddr.setBounds(175, 39, 116, 24);
		panel_3.add(firstInstAddr);
		firstInstAddr.setColumns(10);
		
		JLabel lblStartAddressIn = new JLabel("Start Address in Memory :");
		lblStartAddressIn.setBounds(370, 153, 171, 18);
		contentPane.add(lblStartAddressIn);
		
		startAddrMem = new JTextField();
		startAddrMem.setEditable(false);
		startAddrMem.setBounds(560, 150, 116, 24);
		contentPane.add(startAddrMem);
		startAddrMem.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Target Address :");
		lblNewLabel_4.setBounds(370, 183, 116, 18);
		contentPane.add(lblNewLabel_4);
		
		TA = new JTextField();
		TA.setEditable(false);
		TA.setBounds(490, 183, 116, 24);
		contentPane.add(TA);
		TA.setColumns(10);
		
		JLabel lblInstructions = new JLabel("Instructions :");
		lblInstructions.setBounds(372, 217, 104, 18);
		contentPane.add(lblInstructions);
		
		JList list_1 = new JList();
		list_1.setBounds(377, 252, 181, 397);
		contentPane.add(list_1);
		
		JLabel lblUsingDevice = new JLabel("Using Device");
		lblUsingDevice.setBounds(603, 293, 96, 18);
		contentPane.add(lblUsingDevice);
		
		usingDevice = new JTextField();
		usingDevice.setEditable(false);
		usingDevice.setBounds(595, 323, 116, 24);
		contentPane.add(usingDevice);
		usingDevice.setColumns(10);
		
		btnRunStep = new JButton("Run(1 Step)");
		btnRunStep.setBounds(595, 443, 116, 27);
		contentPane.add(btnRunStep);
		
		btnRunAll = new JButton("Run(All)");
		btnRunAll.setBounds(595, 498, 116, 27);
		contentPane.add(btnRunAll);
		
		btnExit = new JButton("Exit");
		btnExit.setBounds(595, 557, 116, 27);
		contentPane.add(btnExit);
	}
}