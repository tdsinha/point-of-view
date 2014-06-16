package com.pov.file;

public class PovFileException extends RuntimeException {

	private static final long serialVersionUID = -8088832069768533678L;
	private ErrorCodes code ;
	PovFileException (ErrorCodes code, Exception e){
		super (e) ;
		this.code =code; 
	}
	
	@Override
	public String getMessage () {
		return code.message ;
	}
	
	public String getErrorCdoe (){
		return code.code ;
	}

	static enum ErrorCodes {
		
		NOT_EXISTS ("{com.pov.file.FileNotFound}","Could not find the file"),
		FILE_OPERATION ("{com.pov.file.FileOperationError}","Could not operate on file"),
		INVALID_KEY_SERIES_LENGTH ("{com.pov.file.InvalidKeySeriesLength}","Invalid Key Series length");
		
		String code ;
		String message;
		ErrorCodes (String code,String message){
			this.code = code ;
			this.message = message;
		}
	}
}
