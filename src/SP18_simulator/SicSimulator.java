package SP18_simulator;

import java.io.File;

import javax.swing.DefaultListModel;

/**
 * 시뮬레이터로서의 작업을 담당한다. VisualSimulator에서 사용자의 요청을 받으면 이에 따라
 * ResourceManager에 접근하여 작업을 수행한다.  
 * 
 * 작성중의 유의사항 : <br>
 *  1) 새로운 클래스, 새로운 변수, 새로운 함수 선언은 얼마든지 허용됨. 단, 기존의 변수와 함수들을 삭제하거나 완전히 대체하는 것은 지양할 것.<br>
 *  2) 필요에 따라 예외처리, 인터페이스 또는 상속 사용 또한 허용됨.<br>
 *  3) 모든 void 타입의 리턴값은 유저의 필요에 따라 다른 리턴 타입으로 변경 가능.<br>
 *  4) 파일, 또는 콘솔창에 한글을 출력시키지 말 것. (채점상의 이유. 주석에 포함된 한글은 상관 없음)<br>
 * 
 * <br><br>
 *  + 제공하는 프로그램 구조의 개선방법을 제안하고 싶은 분들은 보고서의 결론 뒷부분에 첨부 바랍니다. 내용에 따라 가산점이 있을 수 있습니다.
 */
public class SicSimulator {
	ResourceManager rMgr;
	InstTable instTable;
	int ta;
	//n,i,x,b,p,e 처리 플래그
	public static final int nFlag = 2;
	public static final int iFlag = 1;
	public static final int xFlag = 128;
	public static final int bFlag = 64;
	public static final int pFlag = 32;
	public static final int eFlag = 16;
	//레지스터 번호 
	public static final int regA = 0;
	public static final int regX = 1;
	public static final int regL = 2;
	public static final int regB = 3;
	public static final int regS = 4;
	public static final int regT = 5;
	public static final int regF = 6;
	public static final int regPC = 8;
	public static final int regSW = 9;
	
	public SicSimulator(ResourceManager resourceManager) {
		this.rMgr = resourceManager;
		instTable = new InstTable("C:\\Users\\samsung\\Desktop\\inst.data");
	}

	/**
	 * 레지스터, 메모리 초기화 등 프로그램 load와 관련된 작업 수행.
	 * 단, object code의 메모리 적재 및 해석은 SicLoader에서 수행하도록 한다. 
	 */
	public void load(File program) {
	}

	/**
	 * 1개의 instruction이 수행된 모습을 보인다. 
	 */
	public void oneStep() {
		 ta=0;
		int disp = 0;
		char[] tempChar1 =new char[1];
		char[] tempChar2 = new char[2];
		if(instTable.instMap.containsKey(String.format("%02X",rMgr.memory[rMgr.register[regPC]]&252))){
			 addLog(instTable.instMap.get((String.format("%02X",rMgr.memory[rMgr.register[regPC]]&252))).instruction);
			 rMgr.device="";
			//Target Address 구하기
			switch(rMgr.memory[rMgr.register[regPC]+1] & (bFlag |pFlag)){
				case bFlag : // Base
					;
					break;
				case pFlag ://3형식만 해당됨
					disp += (((int)rMgr.memory[rMgr.register[regPC]+1]) & 15)<<8;
					tempChar1[0] = rMgr.memory[rMgr.register[regPC]+2];
					disp += rMgr.charToInt(tempChar1);
					
					if(disp >2048){
						ta = (rMgr.register[regPC] + 3) +(disp-4096);
					}
					else{
						ta = (rMgr.register[regPC] + 3) + disp;
					}
					break;
				case 0 :
					if((rMgr.memory[rMgr.register[regPC]+1] & eFlag )== 0){// 3형식 일때
						ta+=(((int)rMgr.memory[rMgr.register[regPC]+1]) & 15)<<8;
						tempChar1[0] = rMgr.memory[rMgr.register[regPC]+2];
						ta+=rMgr.charToInt(tempChar1);
					} else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag )== 16){ // 4형식 일때
						ta+=(((int)rMgr.memory[rMgr.register[regPC]+1]) & 15)<<16;
						for(int i = 0 ; i < 2 ; i++){
							tempChar2[i] = rMgr.memory[rMgr.register[regPC]+2+i];
						}
						ta+=rMgr.charToInt(tempChar2);
					}
					break;
			}
			//instruction list에 메모리 상황 보여주기 위해 형식에 따라 나누어 해당 바이트만큼을 리스트에 띄워준다.
			if(instTable.instMap.get((String.format("%02X",rMgr.memory[rMgr.register[regPC]]&252))).format.equals("2")){
				 VisualSimulator.frame.im.addElement(Integer.toHexString(rMgr.charToInt(rMgr.getMemory(rMgr.register[regPC], 2))));
				 ta = 0;
			 }
			 else if(instTable.instMap.get((String.format("%02X",rMgr.memory[rMgr.register[regPC]]&252))).format.equals("3/4")){
				 if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == eFlag) // 4형식
					 VisualSimulator.frame.im.addElement(Integer.toHexString(rMgr.charToInt(rMgr.getMemory(rMgr.register[regPC], 4))));
				 else
					 VisualSimulator.frame.im.addElement(Integer.toHexString(rMgr.charToInt(rMgr.getMemory(rMgr.register[regPC], 3))));
			 }
			//STL
			if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("14")){
				switch(rMgr.memory[rMgr.register[regPC]] & (nFlag |iFlag)){
					case nFlag ://indirect addressing
						break;
					case iFlag ://immediate addressing
						break;
					case nFlag|iFlag : // simple addressing
						rMgr.setMemory(ta, rMgr.intToChar(rMgr.register[regL]),3);
						if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
							rMgr.register[regPC] +=3;
						}
						else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
							rMgr.register[regPC] +=4;
						}
						break;
				}
			}
			//JSUB
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("48")){
				switch(rMgr.memory[rMgr.register[regPC]] & (nFlag |iFlag)){
				case nFlag ://indirect addressing
					break;
				case iFlag ://immediate addressing
					break;
				case nFlag|iFlag : // simple addressing
					if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
						rMgr.setRegister(regL, rMgr.register[regPC]+3);
					}
					else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
						rMgr.setRegister(regL, rMgr.register[regPC]+4);
					}
					break;
				}
				rMgr.setRegister(regPC,ta);
			}
			//LDA
			else if(String.format("%02X",rMgr.memory[rMgr.register[regPC]]&252).equals("00")){
				if((rMgr.memory[rMgr.register[regPC]] & (nFlag|iFlag)) == 1){
					rMgr.setRegister(regA, ta);					
				}
				else if((rMgr.memory[rMgr.register[regPC]] & (nFlag|iFlag)) == 2);
				else if((rMgr.memory[rMgr.register[regPC]] & (nFlag|iFlag)) == 3){
					rMgr.setRegister(regA, rMgr.charToInt(rMgr.getMemory(ta, 3)));
				}
				if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
					rMgr.register[regPC] +=3;
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
					rMgr.register[regPC] +=4;
				}
			}
			//COMP
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("28")){
				if((rMgr.memory[rMgr.register[regPC]] & iFlag) == 1){
					if(rMgr.register[regA] == ta)
						rMgr.register[regSW] = -1;
					else
						rMgr.register[regSW] = 0;
				}
				else{
					if(rMgr.register[regA] == rMgr.charToInt(rMgr.getMemory(rMgr.register[regPC], 3)))
						rMgr.register[regSW] = 0;
				}
				if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
					rMgr.register[regPC] +=3;
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
					rMgr.register[regPC] +=4;
				}
			}
			//JEQ
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("30")){
				if(rMgr.register[regSW] == -1){
					rMgr.setRegister(regPC,ta);
				}
				else{
					if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
						rMgr.register[regPC] +=3;
					}
					else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
						rMgr.register[regPC] +=4;
					}
				}
			}
			//J
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("3c")){
				if((rMgr.memory[rMgr.register[regPC]] & (nFlag|iFlag)) == 3){ //n, i = 1
					rMgr.setRegister(regPC,ta);
				}
				else if((rMgr.memory[rMgr.register[regPC]] & (nFlag|iFlag)) == 1){ //i = 1
									;
				}
				else if((rMgr.memory[rMgr.register[regPC]] & (nFlag|iFlag)) == 2){ //n = 1
					rMgr.setRegister(regPC, rMgr.charToInt(rMgr.getMemory(ta, 3)));
				}
			}
			//STA
			else if(String.format("%02X",rMgr.memory[rMgr.register[regPC]]&252).equals("0C")){
				switch(rMgr.memory[rMgr.register[regPC]] & (nFlag |iFlag)){
				case nFlag ://indirect addressing
					break;
				case iFlag ://immediate addressing
					break;                                                                                                  
				case nFlag|iFlag : // simple addressing
					rMgr.setMemory(ta, rMgr.intToChar(rMgr.register[regA]),3);
					if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
						rMgr.register[regPC] +=3;
					}
					else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
						rMgr.register[regPC] +=4;
					}
					break;
				}
			}
			//CLEAR
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("b4")){
				tempChar1[0] = rMgr.memory[rMgr.register[regPC]+1];
				tempChar1[0] = (char)(tempChar1[0]>>>4);
				rMgr.setRegister(rMgr.charToInt(tempChar1), 0);
				rMgr.register[regPC] +=2;
			}
			//LDT
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("74")){
				rMgr.setRegister(regT, rMgr.charToInt(rMgr.getMemory(ta, 3)));
				if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
					rMgr.register[regPC] +=3;
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
					rMgr.register[regPC] +=4;
				}
			}
			//TD
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("e0")){
				
				tempChar1[0] = rMgr.memory[ta];
				rMgr.register[regSW] = 0;
				rMgr.testDevice(Integer.toHexString(rMgr.charToInt(tempChar1)));
				if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
					rMgr.register[regPC] +=3;
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
					rMgr.register[regPC] +=4;
				}
			}
			//RD
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("d8")){
				
				tempChar1[0]= rMgr.readDevice(Integer.toHexString(rMgr.charToInt(rMgr.getMemory(ta, 1))));
				rMgr.setRegister(regA, rMgr.charToInt(tempChar1));
				if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
					rMgr.register[regPC] +=3;
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
					rMgr.register[regPC] +=4;
				}
			}
			//COMPR
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("a0")){
				
				if(rMgr.register[regA] == rMgr.register[regS]){
					rMgr.register[regSW]= -1;
				}
				else
					rMgr.register[regSW] = 0;
				rMgr.register[regPC] +=2;
			}
			//STCH
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("54")){
				tempChar1[0] = (char)(rMgr.getRegister(regA) & 0xff);
				if((rMgr.memory[rMgr.register[regPC]+1] & xFlag) == 0){
					rMgr.setMemory(ta, tempChar1, 1);
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & xFlag) == 128){
					rMgr.setMemory(ta+rMgr.register[regX], tempChar1, 1);
				}
				if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
					rMgr.register[regPC] +=3;
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
					rMgr.register[regPC] +=4;
				}
			}
			//TIXR
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("b8")){
				rMgr.register[regX]+=1;
				tempChar1[0] = rMgr.memory[rMgr.register[regPC]+1];
				tempChar1[0] = (char)(tempChar1[0]>>>4);
				if(rMgr.register[regX] < rMgr.register[rMgr.charToInt(tempChar1)])
					rMgr.register[regSW] = 1;
				else if(rMgr.register[regX] == rMgr.register[rMgr.charToInt(tempChar1)])
					rMgr.register[regSW] = 0;
				else if(rMgr.register[regX] > rMgr.register[rMgr.charToInt(tempChar1)])
					rMgr.register[regSW] = 2;
				rMgr.register[regPC] +=2;
			}
			//JLT
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("38")){
				if(rMgr.register[regSW] == 1)
					rMgr.setRegister(regPC,ta);
				else{
					if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
						rMgr.register[regPC] +=3;
					}
					else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
						rMgr.register[regPC] +=4;
					}
				}
			}
			//STX
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("10")){
				switch(rMgr.memory[rMgr.register[regPC]] & (nFlag |iFlag)){
				case nFlag ://indirect addressing
					break;
				case iFlag ://immediate addressing
					break;
				case nFlag|iFlag : // simple addressing
					rMgr.setMemory(ta, rMgr.intToChar(rMgr.register[regX]),3);
					if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
						rMgr.register[regPC] +=3;
					}
					else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
						rMgr.register[regPC] +=4;
					}
					break;
				}
			}
			//RSUB
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("4c")){
				rMgr.setRegister(regPC, rMgr.register[regL]);
			}
			//LDCH
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("50")){
				tempChar1[0] = rMgr.memory[ta];
				if((rMgr.memory[rMgr.register[regPC]+1] & xFlag) == 0){
					rMgr.setRegister(regA, rMgr.charToInt(tempChar1));
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & xFlag) == 128){
					rMgr.setRegister(regA, rMgr.memory[ta+rMgr.register[regX]]);
				}
				
				if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
					rMgr.register[regPC] +=3;
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
					rMgr.register[regPC] +=4;
				}
			}

			//WD
			else if(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)).equals("dc")){
				rMgr.writeDevice(Integer.toHexString((int)rMgr.memory[ta]), (char)(rMgr.register[regA] & 0xff));
				if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
					rMgr.register[regPC] +=3;
				}
				else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
					rMgr.register[regPC] +=4;
				}
			}
			if(rMgr.register[regPC] == 0){
				VisualSimulator.frame.btnRunStep.setEnabled(false);
			}
		}
	}
	
	/**
	 * 남은 모든 instruction이 수행된 모습을 보인다.
	 */
	public void allStep() {
		while(true){
			oneStep();
			if(VisualSimulator.frame.btnRunStep.isEnabled() == false){
				VisualSimulator.frame.btnRunAll.setEnabled(false);
				break;
			}
		}
	}
	
	/**
	 * 각 단계를 수행할 때 마다 관련된 기록을 남기도록 한다.
	 */
	public void addLog(String log) {
		VisualSimulator.frame.lm.addElement(log);
	}	
}
