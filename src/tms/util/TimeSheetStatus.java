package tms.util;

public enum TimeSheetStatus {
	NEW('N'),SUBMITTED('S'),APPROVED('A'),REJECTED('R');
	
	private char status ;
	private TimeSheetStatus(char s){
		this.status = s;
	}
	
	public TimeSheetStatus getType(char s){
		TimeSheetStatus ret = TimeSheetStatus.NEW;
		switch(s){
			case 'A' : ret = TimeSheetStatus.APPROVED;break;
			case 'S' : ret = TimeSheetStatus.SUBMITTED;break;
			case 'R' : ret = TimeSheetStatus.REJECTED;break;
			
		}
		return ret;
	}
	public char getStatus(){
		return status;
	}
	
}
