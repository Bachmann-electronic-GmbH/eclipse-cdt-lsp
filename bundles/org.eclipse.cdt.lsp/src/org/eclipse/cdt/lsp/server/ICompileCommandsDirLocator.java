package org.eclipse.cdt.lsp.server;

import java.net.URI;

import org.eclipse.core.runtime.IPath;

public interface ICompileCommandsDirLocator {
	
	/** 
	 * Path to directory containing the compile_commands.json to be used for the given file URI
	 * 
	 * @param uri of document to be opened by C/C++ language server
	 * @return path to directory containing the compile_commands.json or null if directory cannot be determined
	 */
	public IPath getCompileCommandsDir(URI uri);
}
