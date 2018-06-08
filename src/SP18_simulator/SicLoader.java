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
			int memoryNum=0;
			int csAddr = 0;
			int temp;
			int csNum = -1;
			int x=1;
			int num;
			ArrayList<String> mRecord = new ArrayList<String>();
			ArrayList<Integer> mCsNum = new ArrayList<Integer>();
			ArrayList<Integer> memNum = new ArrayList<Integer>(); //Modification Record 처리할때 사용할 메모리번호를 저장하는 list
			FileReader filereader = new FileReader(objectCode);
			BufferedReader bufReader = new BufferedReader(filereader); 
			while((line = bufReader.readLine())!= null){
				if(line.length() != 0){
					switch(line.charAt(0)){
						case 'H' : 
							for( ; x <7 ; x++){
								if(line.charAt(x) == ' '){
									//x++;
									break;
								}
							}
								
							rMgr.setProgName(line.substring(1, x));
							rMgr.setStartAddr(Integer.parseInt(line.substring(7,13),16)+csAddr);
							rMgr.setProgLength(line.substring(13));
							memNum.add(memoryNum);
							csNum++; 
							break;
						case 'D' :
							line = line.substring(1);
							for(int i = 0 ; i < line.split(" ").length ; i++){
								symTab.putSymbol(line.split(" ")[i++], Integer.parseInt(line.split(" ")[i],16));
							}
							break;
						case 'T' :
							for(int i = 0 ; i < line.substring(9).length(); i++){
								temp = Integer.parseInt(line.substring(9+i,10+i),16);
								if((i % 2) == 0){
									rMgr.memory[memoryNum] += (char)(temp<<4);
								} else if((i % 2) == 1){
									rMgr.memory[memoryNum] += (char)(temp);
									System.out.println(memoryNum + " " +Integer.toHexString((int)rMgr.memory[memoryNum]));
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
			for(int i = 0 ; i < mRecord.size() ; i++){
				for(int j = 0 ; j < rMgr.progName.size() ; j++){
					if(mRecord.get(i).substring(10).equals(rMgr.progName.get(j))){
						num = memNum.get(mCsNum.get(i))+Integer.parseInt(mRecord.get(i).substring(1, 7),16);
						//System.out.println(num);
						if(mRecord.get(i).charAt(9)=='+'){
						
						}else if(mRecord.get(i).charAt(9) == '-'){
							
						}
						//rMgr.memory[memNum.get(mCsNum.get(i))+Integer.parseInt(mRecord.get(i).substring(1, 7),16)] = 0;
						break;
					}
				}
				for(int j = 0 ; j < symTab.symbolList.size() ; j++){
					if(mRecord.get(i).substring(10).equals(symTab.symbolList.get(j))){
						num = memNum.get(mCsNum.get(i))+Integer.parseInt(mRecord.get(i).substring(1, 7),16);
						System.out.println(num);
					}
				}
			}
//			for(int i = 0 ; i < rMgr.progName.size() ; i++){
//				
//				System.out.println(rMgr.progName.get(i));
//				System.out.println(Integer.toHexString(rMgr.startAddr.get(i)));
//			}
//			for(int i = 0 ; i < symTab.symbolList.size(); i++){
//				System.out.println(symTab.symbolList.get(i));
//				System.out.println(Integer.toHexString(symTab.addressList.get(i)));
//			}
//				
			bufReader.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}
}
