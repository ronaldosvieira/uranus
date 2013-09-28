package simulador;

public class Constante{
	String con;
	public static final String[] syntax = {"add" ,	"addi" ,"addiu" ,"addu" ,"and" ,"andi" ,"bgt" ,	"blt" ,	"bge" ,	"ble" ,	"bgtu" ,"bgtz" ,"beq" ,	"bne" ,	"div" ,	"divu" ,"j" ,"jal" ,"jr" ,	"la" ,	"lb" ,	"lbu" ,	"lh"  ,	"lhu" ,	"li" ,	"lui" ,"lw" ,	"lwcZ" ,"mfhi" ,"mflo" ,"mthi" ,"mtlo" ,"mfc0" ,"mfcZ" ,"mtcZ" ,"mult" ,"multu" ,"nor" ,"xor" ,	"or" ,	"ori" ,	"sb" ,"sh" ,"slt" ,	"slti" ,"sltiu" ,"sltu" ,"sll" ,"srl" ,	"sra" ,	"sub" ,	"subu" ,"sb" ,"sw" ,"swcZ" ,"sh","syscall"};
	public Constante(){}
	
	public String toBinary(int num){
		String aux = "0";
		if(num<0){
			con = Integer.toBinaryString(num);
		}
		else{
			con = Integer.toBinaryString(num);			
			if(con.length()<32){
				int d = 32-con.length();
				while(aux.length()!=d){
					aux += "0";
				}
			}
			con = aux+con;
		}
		return con;
	}
	
	public String toGreaterBinary(long num){
		String aux = "0";
		if(num<0) con = Long.toBinaryString(num);
		else{
			con = Long.toBinaryString(num);
			if(con.length()<64){
				int d = 64-con.length();
				while(aux.length()!=d){
					aux+="0";
				}
			}
			con = aux+con;
		}
		return con;
	}
	
	public String toHexadecimal(int num){
		String aux = "0";
		if(num<0){
			con = Integer.toHexString(num);
		}
		else {
			con = Integer.toHexString(num);
			if(con.length()<8){
				int d = 8-con.length();
				while(aux.length()!=d) aux += "0";
			}
			con = aux+con;
		}
		return con;
	}
	
	public int toInteger(String str){
		int i = 0;
		if(str.charAt(0)=='1'){
			String aux = "0";
			while(i<str.length()){
				if(str.charAt(i)=='1') aux += "0";
				else aux += "1";
				i++;
				}
				return -1*(Integer.parseInt(aux, 2)+1);
		}
		else{
			return Integer.parseInt(str, 2);
		}
	}
	
	public long toUnsignedInteger(String str){
		return Long.parseLong(str, 2);
	}
}