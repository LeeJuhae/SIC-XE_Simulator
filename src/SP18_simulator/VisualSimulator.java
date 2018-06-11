package SP18_simulator;

import java.io.File;
import javax.swing.JFileChooser;
import java.awt.event.*;


/**
 * VisualSimulator는 사용자와의 상호작용을 담당한다.<br>
 * 즉, 버튼 클릭등의 이벤트를 전달하고 그에 따른 결과값을 화면에 업데이트 하는 역할을 수행한다.<br>
 * 실제적인 작업은 SicSimulator에서 수행하도록 구현한다.
 */
public class VisualSimulator {
	ResourceManager resourceManager = new ResourceManager(); // 리소스매니저 생성
	SicLoader sicLoader = new SicLoader(resourceManager); 
	SicSimulator sicSimulator = new SicSimulator(resourceManager);
	static MyFrame frame;
	public VisualSimulator(){
		frame = new MyFrame();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	/**
	 * 프로그램 로드 명령을 전달한다.
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
	 * 하나의 명령어만 수행할 것을 SicSimulator에 요청한다.
	 */
	public void oneStep(){
		sicSimulator.oneStep();
	};

	/**
	 * 남아있는 모든 명령어를 수행할 것을 SicSimulator에 요청한다.
	 */
	public void allStep(){
		sicSimulator.allStep();
	};
	
	/**
	 * 화면을 최신값으로 갱신하는 역할을 수행한다.
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
		/**open버튼 눌렸을 때 파일 다이얼로그 창 나타나게 함.**/
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
		/**Run(1 step)버튼 눌렸을 때**/
		visualSimulator.frame.btnRunStep.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				visualSimulator.oneStep();
				visualSimulator.update();
			}
		});
		/**Run(All)버튼 눌렸을 때**/
		visualSimulator.frame.btnRunAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				visualSimulator.allStep();
				visualSimulator.update();
			}
		});
		/**End 버튼 눌렸을때 */
		visualSimulator.frame.btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				System.exit(0);
			}
		});
	}
}