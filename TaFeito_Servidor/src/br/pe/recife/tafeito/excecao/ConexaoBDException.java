package br.pe.recife.tafeito.excecao;


public class ConexaoBDException extends Exception {
	
	/**
	 * 
	 */
	public ConexaoBDException() {
		super();
	}

	/**
	 * @param message
	 */
	public ConexaoBDException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConexaoBDException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConexaoBDException(String message, Throwable cause) {
		super(message, cause);
	}

}
