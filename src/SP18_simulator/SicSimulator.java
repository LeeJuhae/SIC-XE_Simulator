package SP18_simulator;

import java.io.File;

/**
 * �ùķ����ͷμ��� �۾��� ����Ѵ�. VisualSimulator���� ������� ��û�� ������ �̿� ����
 * ResourceManager�� �����Ͽ� �۾��� �����Ѵ�.  
 * 
 * �ۼ����� ���ǻ��� : <br>
 *  1) ���ο� Ŭ����, ���ο� ����, ���ο� �Լ� ������ �󸶵��� ����. ��, ������ ������ �Լ����� �����ϰų� ������ ��ü�ϴ� ���� ������ ��.<br>
 *  2) �ʿ信 ���� ����ó��, �������̽� �Ǵ� ��� ��� ���� ����.<br>
 *  3) ��� void Ÿ���� ���ϰ��� ������ �ʿ信 ���� �ٸ� ���� Ÿ������ ���� ����.<br>
 *  4) ����, �Ǵ� �ܼ�â�� �ѱ��� ��½�Ű�� �� ��. (ä������ ����. �ּ��� ���Ե� �ѱ��� ��� ����)<br>
 * 
 * <br><br>
 *  + �����ϴ� ���α׷� ������ ��������� �����ϰ� ���� �е��� ������ ��� �޺κп� ÷�� �ٶ��ϴ�. ���뿡 ���� �������� ���� �� �ֽ��ϴ�.
 */
public class SicSimulator {
	ResourceManager rMgr;
	InstTable instTable;
	
	public static final int nFlag = 2;
	public static final int iFlag = 1;
	public static final int xFlag = 128;
	public static final int bFlag = 64;
	public static final int pFlag = 32;
	public static final int eFlag = 16;
	
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
		// �ʿ��ϴٸ� �ʱ�ȭ ���� �߰�
		this.rMgr = resourceManager;
		instTable = new InstTable("C:\\Users\\samsung\\Desktop\\inst.data");
	}

	/**
	 * ��������, �޸� �ʱ�ȭ �� ���α׷� load�� ���õ� �۾� ����.
	 * ��, object code�� �޸� ���� �� �ؼ��� SicLoader���� �����ϵ��� �Ѵ�. 
	 */
	public void load(File program) {
		/* �޸� �ʱ�ȭ, �������� �ʱ�ȭ ��*/
	}

	/**
	 * 1���� instruction�� ����� ����� ���δ�. 
	 */
	public void oneStep() {
		int ta=0;
		int disp = 0;
		char[] tempChar1 =new char[1];
		char[] tempChar2 = new char[2];
		//char[] tempChar3 = new char[3];
		 if(instTable.instMap.containsKey(String.format("%02X",rMgr.memory[rMgr.register[regPC]]&252))){
			System.out.println(Integer.toHexString(rMgr.register[regPC])+" "+instTable.instMap.get(String.format("%02X",rMgr.memory[rMgr.register[regPC]]&252)).instruction);
			//System.out.println(Integer.toHexString((int)(rMgr.memory[rMgr.register[regPC]]&252)));
			//System.out.println(String.format("%02X",rMgr.memory[rMgr.register[regPC]]&252));
			//Target Address ���ϱ�
			switch(rMgr.memory[rMgr.register[regPC]+1] & (bFlag |pFlag)){
				case bFlag : // Base
					;
					break;
				case pFlag ://3���ĸ� �ش��
					disp += (((int)rMgr.memory[rMgr.register[regPC]+1]) & 15)<<8;
					tempChar1[0] = rMgr.memory[rMgr.register[regPC]+2];
					disp += rMgr.charToInt(tempChar1);
					
					if(disp >2048){
						ta = (rMgr.register[regPC] + 3) +(disp-4096);
					}
					else{
						ta = (rMgr.register[regPC] + 3) + disp;
					}
					//System.out.println(disp + " " + ta);
					break;
				case 0 :
					if((rMgr.memory[rMgr.register[regPC]+1] & eFlag )== 0){// 3���� �϶�
						ta+=(((int)rMgr.memory[rMgr.register[regPC]+1]) & 15)<<8;
						tempChar1[0] = rMgr.memory[rMgr.register[regPC]+2];
						ta+=rMgr.charToInt(tempChar1);
					} else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag )== 16){ // 4���� �϶�
						ta+=(((int)rMgr.memory[rMgr.register[regPC]+1]) & 15)<<16;
						for(int i = 0 ; i < 2 ; i++){
							tempChar2[i] = rMgr.memory[rMgr.register[regPC]+2+i];
						}
						ta+=rMgr.charToInt(tempChar2);
					}
					break;
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
						System.out.println(rMgr.register[regL]);
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
				//System.out.println(nFlag|iFlag);
				switch(rMgr.memory[rMgr.register[regPC]] & (nFlag |iFlag)){
				case nFlag ://indirect addressing
					break;
				case iFlag ://immediate addressing
					break;
				case nFlag|iFlag : // simple addressing
					//System.out.println(rMgr.memory[rMgr.register[regPC]+1] & eFlag);
					//rMgr.setMemory(ta, rMgr.intToChar(rMgr.register[regL]),3);
					if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 0){
						rMgr.setRegister(regL, rMgr.register[regPC]+3);
						//System.out.println(rMgr.register[regL]);
					}
					else if((rMgr.memory[rMgr.register[regPC]+1] & eFlag) == 16){
						rMgr.setRegister(regL, rMgr.register[regPC]+4);
						//System.out.println(rMgr.register[regL]);
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
				else if((rMgr.memory[rMgr.register[regPC]] & (nFlag|iFlag)) == 2){
									
								}
				else if((rMgr.memory[rMgr.register[regPC]] & (nFlag|iFlag)) == 3){
					
					rMgr.setRegister(regA, rMgr.charToInt(rMgr.getMemory(ta, 3)));
				}
//				rMgr.setRegister(regA, rMgr.charToInt(rMgr.getMemory(ta, 3)));
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
					System.out.println(rMgr.register[regA]);
					System.out.println(ta);
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
					//rMgr.setRegister(regPC,rMgr.memory[rMgr.charToInt(rMgr.getMemory(ta, 3))]);
					System.out.println(ta);
					System.out.println(rMgr.charToInt(rMgr.getMemory(rMgr.memory[ta], 3)));
					System.out.println(rMgr.register[regPC]);
					rMgr.setRegister(regPC, rMgr.charToInt(rMgr.getMemory(rMgr.memory[ta], 3)));
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
				//System.out.println(tempChar1[0]);
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
					//System.out.println("stop");
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
				//System.out.println(rMgr.charToInt(tempChar1));
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
		}
		for(int i = 0 ; i < 4219 ; i++){
		System.out.println(Integer.toHexString(i)+ " " + Integer.toHexString((int)rMgr.memory[i]));
		}
//		for(int i = 0 ; i < 10 ; i++){
//			System.out.println("reg"+i+" " +rMgr.register[i]);
//		}
	}
	
	/**
	 * ���� ��� instruction�� ����� ����� ���δ�.
	 */
	public void allStep() {
	}
	
	/**
	 * �� �ܰ踦 ������ �� ���� ���õ� ����� ���⵵�� �Ѵ�.
	 */
	public void addLog(String log) {
	}	
}
