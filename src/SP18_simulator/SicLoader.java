package SP18_simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SicLoader�� ���α׷��� �ؼ��ؼ� �޸𸮿� �ø��� ������ �����Ѵ�. �� �������� linker�� ���� ���� �����Ѵ�. 
 * <br><br>
 * SicLoader�� �����ϴ� ���� ���� ��� ������ ����.<br>
 * - program code�� �޸𸮿� �����Ű��<br>
 * - �־��� ������ŭ �޸𸮿� �� ���� �Ҵ��ϱ�<br>
 * - �������� �߻��ϴ� symbol, ���α׷� �����ּ�, control section �� ������ ���� ���� ���� �� ����
 */
public class SicLoader {
	ResourceManager rMgr;
	SymbolTable symTab;
	
	public SicLoader(ResourceManager resourceManager) {
		// �ʿ��ϴٸ� �ʱ�ȭ
		setResourceManager(resourceManager);
		symTab = new SymbolTable();
	}

	/**
	 * Loader�� ���α׷��� ������ �޸𸮸� �����Ų��.
	 * @param rMgr
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.rMgr=resourceManager;
	}
	
	/**
	 * object code�� �о load������ �����Ѵ�. load�� �����ʹ� resourceManager�� �����ϴ� �޸𸮿� �ö󰡵��� �Ѵ�.
	 * load�������� ������� symbol table �� �ڷᱸ�� ���� resourceManager�� �����Ѵ�.
	 * @param objectCode �о���� ����
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
			ArrayList<Integer> memNum = new ArrayList<Integer>(); //Modification Record ó���Ҷ� ����� �޸𸮹�ȣ�� �����ϴ� list
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
