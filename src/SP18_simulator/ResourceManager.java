package SP18_simulator;

//import java.awt.List;
//import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.StandardOpenOption;



/**
 * ResourceManager�� ��ǻ���� ���� ���ҽ����� �����ϰ� �����ϴ� Ŭ�����̴�.
 * ũ�� �װ����� ���� �ڿ� ������ �����ϰ�, �̸� ������ �� �ִ� �Լ����� �����Ѵ�.<br><br>
 * 
 * 1) ������� ���� �ܺ� ��ġ �Ǵ� device<br>
 * 2) ���α׷� �ε� �� ������ ���� �޸� ����. ���⼭�� 64KB�� �ִ밪���� ��´�.<br>
 * 3) ������ �����ϴµ� ����ϴ� �������� ����.<br>
 * 4) SYMTAB �� simulator�� ���� �������� ���Ǵ� �����͵��� ���� ������. 
 * <br><br>
 * 2���� simulator������ ����Ǵ� ���α׷��� ���� �޸𸮰����� �ݸ�,
 * 4���� simulator�� ������ ���� �޸� �����̶�� ������ ���̰� �ִ�.
 */
public class ResourceManager{
	/**
	 * deviceManager��  ����̽��� �̸��� �Է¹޾��� �� �ش� ����̽��� ���� ����� ���� Ŭ������ �����ϴ� ������ �Ѵ�.
	 * ���� ���, 'A1'�̶�� ����̽����� ������ read���� ������ ���, hashMap�� <"A1", scanner(A1)> ���� �������μ� �̸� ������ �� �ִ�.
	 * <br><br>
	 * ������ ���·� ����ϴ� �� ���� ����Ѵ�.<br>
	 * ���� ��� key������ String��� Integer�� ����� �� �ִ�.
	 * ���� ������� ���� ����ϴ� stream ���� �������� ����, �����Ѵ�.
	 * <br><br>
	 * �̰͵� �����ϸ� �˾Ƽ� �����ؼ� ����ص� �������ϴ�.
	 */
	HashMap<String,Object> deviceManager = new HashMap<String,Object>();
	char[] memory = new char[65536]; // String���� �����ؼ� ����Ͽ��� ������.
	int[] register = new int[10];
	double register_F;
	int noOfBytesRead =0;
	SymbolTable symtabList;
	// �̿ܿ��� �ʿ��� ���� �����ؼ� ����� ��.
	ArrayList<String> progName = new ArrayList<String>();
	ArrayList<String> progLength = new ArrayList<String>();
	ArrayList<Integer> startAddr = new ArrayList<Integer>();
	FileChannel fileChannel;
	/**
	 * �޸�, �������͵� ���� ���ҽ����� �ʱ�ȭ�Ѵ�.
	 */
	public void initializeResource(){
		register[9] = -1;//regSW = -1�� �ʱ�ȭ
		//register[2] = 3;
	}
	
	/**
	 * deviceManager�� �����ϰ� �ִ� ���� ����� stream���� ���� �����Ű�� ����.
	 * ���α׷��� �����ϰų� ������ ���� �� ȣ���Ѵ�.
	 */
	public void closeDevice() {
		
	}
	
	/**
	 * ����̽��� ����� �� �ִ� ��Ȳ���� üũ. TD��ɾ ������� �� ȣ��Ǵ� �Լ�.
	 * ����� stream�� ���� deviceManager�� ���� ������Ų��.
	 * @param devName Ȯ���ϰ��� �ϴ� ����̽��� ��ȣ,�Ǵ� �̸�
	 * @throws IOException 
	 */
	public void testDevice(String devName){
		if(!deviceManager.containsKey(devName)){
			try {
				fileChannel = FileChannel.open(
					    Paths.get("C:\\Users\\samsung\\Desktop\\"+devName+".txt"),
					    StandardOpenOption.CREATE,
					    StandardOpenOption.READ, 
					    StandardOpenOption.WRITE
					);
				deviceManager.put(devName, fileChannel);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				register[9] = -1; // �������� regSW���� -1�� ����
				
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����̽��κ��� ���ϴ� ������ŭ�� ���ڸ� �о���δ�. RD��ɾ ������� �� ȣ��Ǵ� �Լ�.
	 * @param devName ����̽��� �̸�
	 * @param num �������� ������ ����
	 * @return ������ ������
	 */
	public char readDevice(String devName){
		//System.out.println(noOfBytesRead);
		FileChannel fc = (FileChannel)deviceManager.get(devName);
		ByteBuffer buffer = ByteBuffer.allocate(1);
		
		try {
			noOfBytesRead  = fc.read(buffer);
			//System.out.println(buffer);
			buffer.flip();
			if(noOfBytesRead == -1)
				return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (char)buffer.get();
	}

	/**
	 * ����̽��� ���ϴ� ���� ��ŭ�� ���ڸ� ����Ѵ�. WD��ɾ ������� �� ȣ��Ǵ� �Լ�.
	 * @param devName ����̽��� �̸�
	 * @param data ������ ������
	 * @param num ������ ������ ����
	 */
	public void writeDevice(String devName, char[] data, int num){
		
	}
	
	/**
	 * �޸��� Ư�� ��ġ���� ���ϴ� ������ŭ�� ���ڸ� �����´�.
	 * @param location �޸� ���� ��ġ �ε���
	 * @param num ������ ����
	 * @return �������� ������
	 */
	public char[] getMemory(int location, int num){
		char[] mem = new char[num];
		for(int i = 0 ; i < num ; i++){
			mem[i] = memory[location+i];
		}
		return mem;
	}

	/**
	 * �޸��� Ư�� ��ġ�� ���ϴ� ������ŭ�� �����͸� �����Ѵ�. 
	 * @param locate ���� ��ġ �ε���
	 * @param data �����Ϸ��� ������
	 * @param num �����ϴ� �������� ����
	 */
	public void setMemory(int locate, char[] data, int num){
		for(int i= 0 ; i < num ; i++)
		memory[locate+i] = data[i];
	}

	/**
	 * ��ȣ�� �ش��ϴ� �������Ͱ� ���� ��� �ִ� ���� �����Ѵ�. �������Ͱ� ��� �ִ� ���� ���ڿ��� �ƴԿ� �����Ѵ�.
	 * @param regNum �������� �з���ȣ
	 * @return �������Ͱ� ������ ��
	 */
	public int getRegister(int regNum){
		if(regNum == 6)
			return (int)register_F;
		else
			return register[regNum];
	}

	/**
	 * ��ȣ�� �ش��ϴ� �������Ϳ� ���ο� ���� �Է��Ѵ�. �������Ͱ� ��� �ִ� ���� ���ڿ��� �ƴԿ� �����Ѵ�.
	 * @param regNum ���������� �з���ȣ
	 * @param value �������Ϳ� ����ִ� ��
	 */
	public void setRegister(int regNum, int value){
		if(regNum == 6)
			register_F = value;
		else
			register[regNum] = value;
	}

	/**
	 * �ַ� �������Ϳ� �޸𸮰��� ������ ��ȯ���� ���ȴ�. int���� char[]���·� �����Ѵ�.
	 * @param data
	 * @return
	 */
//	public char[] intToChar(int data){
//		char[] tempChar = new char[3];
//		for(int k = 1 ; k >=0 ; k--){
//			tempChar[k] = (char) (data & 255);
//			data = data>>8;
//		}
//		return tempChar;
//	}
	public char[] intToChar(int data){
		char[] tempChar = new char[3];
		for(int k = 2 ; k >=0 ; k--){
			tempChar[k] = (char) (data & 255);
			data = data>>8;
		}
		return tempChar;
	}

	/**
	 * �ַ� �������Ϳ� �޸𸮰��� ������ ��ȯ���� ���ȴ�. char[]���� int���·� �����Ѵ�.
	 * @param data
	 * @return
	 */
//	public int charToInt(char[] data){
//		int num=0;
//		for(int i = 0 ; i < data.length ; i++){
//			if(i == 0)
//				num+= data[i]&15;
//			else
//				num+=(int)data[i];
//			if(i == data.length-1)
//				break;
//			num = num<<8;
//		}
//		return num;
//	}
	public int charToInt(char[] data){
		int num=0;
		for(int i = 0 ; i < data.length ; i++){
			num+=(int)data[i];
			if(i == data.length-1)
				break;
			num = num<<8;
		}
		return num;
	}
	
	public void setProgName(String name){
		progName.add(name);
	}
	public void setProgLength(String length){
		progLength.add(length);
	}
	public void setStartAddr(int address){
		startAddr.add(address);
	}
}