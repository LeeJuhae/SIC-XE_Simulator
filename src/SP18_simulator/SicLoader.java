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
			int memoryNum=0; // Resource Manager�� memory������
			int csAddr = 0; // Control Section �� �����ּҸ� �����ϴ� ����
			int temp; // Text Record ó���� �� ����ϴ� �ӽ� ���� ����
			int csNum = -1; //Control Section ��ȣ 
			int x=1; // Header Record���� program Name�� �Ľ��Ҷ� ����ϴ� ����
			int num; // Modification���� �����ϰ��� �ϴ� �޸��� �������� �����ϴ� ����
			int modiCount = 0;
			char[] tempChar;
			int y=0;
			ArrayList<String> mRecord = new ArrayList<String>(); // Modification Record ������ �����ϴ� list
			ArrayList<Integer> mCsNum = new ArrayList<Integer>(); // Modification Record ������ �ش��ϴ� Control Section ��ȣ�� �����ϴ� list 
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
			//pass2 : Modification Record ó��
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