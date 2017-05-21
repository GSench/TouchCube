package grisha.support;

import java.util.ArrayList;

public class StringEditor {
	
	public static final ArrayList<String> setEntries(String str){
		  return separate(str, "\n");
	  }
	
	public static final ArrayList<String> separate(String str, String separator){
		ArrayList<String> ret = new ArrayList<String>();
		  int index = 0;
		  String newStr = str+"";
		  while(index!=-1){
			  index = newStr.indexOf(separator);
			  if(index!=-1){
				  ret.add(newStr.substring(0, index));
				  newStr = newStr.substring(index+1);
			  } else {
				  ret.add(newStr);
			  }
		  }
		  return ret;
	}
	
	public static final String getSuffix(String str, char sym){
		String ret = new String();
		char[] chars = str.toCharArray();
		for(int i=str.length()-1; i>=0; i--){
			if(chars[i]==sym){
				ret = str.substring(i+1);
				break;
			}
		}
		return ret;
	}
	
	public static final String allowSymbols(String currentStr, String allowedSymbols){
		String ret = new String();
		char[] curStr = currentStr.toCharArray();
		char[] allSym = allowedSymbols.toCharArray();
		for(int i=0; i<currentStr.length(); i++){
			for(int j=0; j<allowedSymbols.length(); j++){
				if(curStr[i]==allSym[j]) ret=ret+allSym[j];
			}
		}
		return ret;
	}
	
	public static final String allowCiphers(String str){
		return allowSymbols(str, "0123456789");
	}

}
