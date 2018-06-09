package SP18_simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SicLoader는 프로그램을 해석해서 메모리에 올리는 역할을 수행한다. 이 과정에서 linker의 역할 또한 수행한다. 
 * <br><br>
 * SicLoader가 수행하는 일을 예를 들면 다음과 같다.<br>
 * - program code를 메모리에 적재시키기<br>
 * - 주어진 공간만큼 메모리에 빈 공간 할당하기<br>
 * - 과정에서 발생하는 symbol, 프로그램 시작주소, control section 등 실행을 위한 정보 생성 및 관리
 */
public class SicLoader {
	ResourceManager rMgr;
	SymbolTable symTab;
	
	public SicLoader(ResourceManager resourceManager) {
		// 필요하다면 초기화
		setResourceManager(resourceManager);
		symTab = new SymbolTable();
	}

	/**
	 * Loader와 프로그램을 적재할 메모리를 연결시킨다.
	 * @param rMgr
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.rMgr=resourceManager;
	}
	
	/**
	 * object code를 읽어서 load과정을 수행한다. load한 데이터는 resourceManager가 관리하는 메모리에 올라가도록 한다.
	 * load과정에서 만들어진 symbol table 등 자료구조 역시 resourceManager에 전달한다.
	 * @param objectCode 읽어들인 파일
	 */
	public void load(File objectCode){
		try{
			String line; 
			int memoryNum=0; // Resource Manager의 memory번지수
			int csAddr = 0; // Control Section 의 시작주소를 저장하는 변수
			int temp; // Text Record 처리할 때 사용하는 임시 저장 변수
			int csNum = -1; //Control Section 번호 
			int x=1; // Header Record에서 program Name을 파싱할때 사용하는 변수
			int num; // Modification에서 수정하고자 하는 메모리의 번지수를 저장하는 변수
			int modiCount = 0;
			char[] tempChar;
			int y=0;
			ArrayList<String> mRecord = new ArrayList<String>(); // Modification Record 문장을 저장하는 list
			ArrayList<Integer> mCsNum = new ArrayList<Integer>(); // Modification Record 문장이 해당하는 Control Section 번호를 저장하는 list 
			FileReader filereader = new FileReader(objectCode);
			BufferedReader bufReader = new BufferedReader(filereader); 
			//pass1 
			while((line = bufReader.readLine())!= null){
				if(line.length() != 0){
					switch(line.charAt(0)){
						case 'H' : 
							for( ; x <7 ; x++){
								if(line.charAt(x) == ' '){
									break;
								}
							}
							rMgr.setProgName(line.substring(1, x));
							rMgr.setStartAddr(Integer.parseInt(line.substring(7,13),16)+csAddr);
							rMgr.setProgLength(line.substring(13));
							csNum++; 
							break;
						case 'D' :
							line = line.substring(1);
							for(int i = 0 ; i < line.split(" ").length ; i++){
								symTab.putSymbol(line.split(" ")[i++], Integer.parseInt(line.split(" ")[i],16));
							}
							break;
						case 'T' :
							memoryNum = Integer.parseInt(line.substring(1,7), 16)+rMgr.startAddr.get(rMgr.startAddr.size()-1);
							for(int i = 0 ; i < line.substring(9).length(); i++){
								temp = Integer.parseInt(line.substring(9+i,10+i),16);
								if((i % 2) == 0){
									rMgr.memory[memoryNum] += (char)(temp<<4);
								} else if((i % 2) == 1){
									rMgr.memory[memoryNum] += (char)(temp);
									memoryNum++;
								}
							}
							break;
						case 'M' :
							mRecord.add(line);
							mCsNum.add(csNum);
							break;
						case 'E' :
							csAddr += Integer.parseInt(rMgr.progLength.get(rMgr.progLength.size()-1),16);
							break;
					}
				}
			}
			//pass2 : Modification Record 처리
			for(int i = 0 ; i < mRecord.size() ; i++){
				modiCount = Integer.parseInt(mRecord.get(i).substring(7,9),16);
				if(modiCount %2 == 0)
					modiCount/=2;
				else
					modiCount = (modiCount+1) / 2;
				tempChar = new char[modiCount];
				num = rMgr.startAddr.get(mCsNum.get(i))+Integer.parseInt(mRecord.get(i).substring(1, 7),16);
				tempChar = rMgr.getMemory(num, modiCount);

				for(int k = 0; k < modiCount ; k++){
					y += (int)tempChar[k];
					if(k == (modiCount -1))
						break;
					y = y<<8;
				}
				for(int j = 0 ; j < rMgr.progName.size() ; j++){
					if(mRecord.get(i).substring(10).equals(rMgr.progName.get(j))){
						if(mRecord.get(i).charAt(9)=='+'){
							y+=rMgr.startAddr.get(j);
						}else if(mRecord.get(i).charAt(9) == '-'){
							y-=rMgr.startAddr.get(j);
						}
						break;
					}
				}
				for(int j = 0 ; j < symTab.symbolList.size() ; j++){
					if(mRecord.get(i).substring(10).equals(symTab.symbolList.get(j))){
						if(mRecord.get(i).charAt(9)=='+'){
							y+=symTab.addressList.get(j);
						}else if(mRecord.get(i).charAt(9) == '-'){
							y-=symTab.addressList.get(j);
						}
						break;
					}
				}
				for(int k = modiCount-1 ; k >=0 ; k--){
					tempChar[k] = (char) (y & 255);
					y = y>>8;
				}
				rMgr.setMemory(num, tempChar, modiCount);
				y = 0;
			}
//			for(int i = 0 ; i < 4219 ; i++){
//				System.out.println(Integer.toHexString(i)+ " " + Integer.toHexString((int)rMgr.memory[i]));
//			}
			bufReader.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}
}