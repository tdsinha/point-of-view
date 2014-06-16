package com.pov.file;

import com.google.inject.AbstractModule;

public class FileModule extends AbstractModule{

	@Override
	protected void configure() {
		bind (ParseFile.class).to(ParseFileImpl.class);
	}
	
}
