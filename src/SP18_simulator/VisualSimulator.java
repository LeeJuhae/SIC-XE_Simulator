package SP18_simulator;

import java.io.File;
import javax.swing.JFileChooser;
import java.awt.event.*;


/**
 * VisualSimulator�� ����ڿ��� ��ȣ�ۿ��� ����Ѵ�.<br>
 * ��, ��ư Ŭ������ �̺�Ʈ�� �����ϰ� �׿� ���� ������� ȭ�鿡 ������Ʈ �ϴ� ������ �����Ѵ�.<br>
 * �������� �۾��� SicSimulator���� �����ϵ��� �����Ѵ�.
 */
public class VisualSimulator {
	ResourceManager resourceManager = new ResourceManager(); // ���ҽ��Ŵ��� ����
	SicLoader sicLoader = new SicLoader(resourceManager); 
	SicSimulator sicSimulator = new SicSimulator(resourceManager);
	static MyFrame frame;
	public VisualSimulator(){
		frame = new MyFrame();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	/**
	 * ���α׷� �ε� ����� �����Ѵ�.
	 */
	public void load(File program){
		//...
		sicLoader.load(program);
		sicSimulator.load(program);
		frame.regA_dec.setText("0");
		frame.regA_hex.setText("0");
		frame.regX_dec.setText("0");
		frame.regX_hex.setText("0");
		frame.regL_dec.setText("0");
		frame.regL_hex.setText("0");
		frame.regPC_dec.setText("0");
		frame.regPC_hex.setText("0");
		frame.regSW.setText("0");
		frame.regB_dec.setText("0");
		frame.regB_hex.setText("0");
		frame.regS_dec.setText("0");
		frame.regS_hex.setText("0");
		frame.regT_dec.setText("0");
		frame.regT_hex.setText("0");
		frame.regF.setText("0");
		
	};

	/**
	 * �ϳ��� ��ɾ ������ ���� SicSimulator�� ��û�Ѵ�.
	 */
	public void oneStep(){
		sicSimulator.oneStep();
	};

	/**
	 * �����ִ� ��� ��ɾ ������ ���� SicSimulator�� ��û�Ѵ�.
	 */
	public void allStep(){
		sicSimulator.allStep();
	};
	
	/**
	 * ȭ���� �ֽŰ����� �����ϴ� ������ �����Ѵ�.
	 */
	public void update(){
		frame.regA_dec.setText(Integer.toString(resourceManager.register[0]));
		frame.regX_dec.setText(Integer.toString(resourceManager.register[1]));
		frame.regL_dec.setText(Integer.toString(resourceManager.register[2]));
		frame.regPC_dec.setText(Integer.toString(resourceManager.register[8]));
		frame.regSW.setText(Integer.toString(resourceManager.register[9]));
		frame.regB_dec.setText(Integer.toString(resourceManager.register[3]));
		frame.regS_dec.setText(Integer.toString(resourceManager.register[4]));
		frame.regT_dec.setText(Integer.toString(resourceManager.register[5]));
		frame.regF.setText(Double.toString(resourceManager.register_F));
		
		frame.regA_hex.setText(Integer.toHexString(resourceManager.register[0]));
		frame.regX_hex.setText(Integer.toHexString(resourceManager.register[1]));
		frame.regL_hex.setText(Integer.toHexString(resourceManager.register[2]));
		frame.regPC_hex.setText(Integer.toHexString(resourceManager.register[8]));
		frame.regB_hex.setText(Integer.toHexString(resourceManager.register[3]));
		frame.regS_hex.setText(Integer.toHexString(resourceManager.register[4]));
		frame.regT_hex.setText(Integer.toHexString(resourceManager.register[5]));
		
		frame.progNameText.setText(resourceManager.progName.get(0));
		frame.startAddrMem.setText("0000000");
		frame.TA.setText(Integer.toHexString(sicSimulator.ta));
		frame.startAddrOP.setText(String.format("%06X", resourceManager.startAddr.get(0)));
		frame.firstInstAddr.setText("000000");
		frame.progLength.setText(Integer.toHexString(Integer.parseInt(resourceManager.progLength.get(resourceManager.progLength.size()-1),16)+resourceManager.startAddr.get(resourceManager.startAddr.size()-1)));
		frame.usingDevice.setText(resourceManager.device);
	};
	public static void main(String[] args) {
		VisualSimulator visualSimulator = new VisualSimulator();
		/**open��ư ������ �� ���� ���̾�α� â ��Ÿ���� ��.**/
		visualSimulator.frame.btnOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				JFileChooser chooser = new JFileChooser();
				int ret = chooser.showOpenDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					try{
						visualSimulator.load(chooser.getSelectedFile());
						visualSimulator.frame.fileNameText.setText(chooser.getSelectedFile().getName());
						visualSimulator.update();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		/**Run(1 step)��ư ������ ��**/
		visualSimulator.frame.btnRunStep.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				visualSimulator.oneStep();
				visualSimulator.update();
			}
		});
		/**Run(All)��ư ������ ��**/
		visualSimulator.frame.btnRunAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				visualSimulator.allStep();
				visualSimulator.update();
			}
		});
		/**End ��ư �������� */
		visualSimulator.frame.btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				System.exit(0);
			}
		});
	}
}