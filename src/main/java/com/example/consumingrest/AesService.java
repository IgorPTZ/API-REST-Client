package com.example.consumingrest;

public class AesService {
	private static final int KEY_SIZE = 256;
	public static final int PSWD_ITERATIONS = 2;
	public static final String SECRET_KEY_SPEC = "@solos#zaq#2019@";
	public static final String INICIALIZACAO_VECTOR_SERVICOS_ONLINE = "dc0da04af8fee58593442bf834b30739";
	public static final String SALT_SERVICOS_ONLINE = INICIALIZACAO_VECTOR_SERVICOS_ONLINE;
	

	private static final AesUtil aesUtil = new AesUtil(KEY_SIZE, PSWD_ITERATIONS, SECRET_KEY_SPEC);
	
	public static String decrypt(final String encodedMsg){
		return aesUtil.decrypt(SALT_SERVICOS_ONLINE, INICIALIZACAO_VECTOR_SERVICOS_ONLINE, encodedMsg);
	}
	
	public static String encrypt(final String plaintext){
		return aesUtil.encrypt(SALT_SERVICOS_ONLINE, INICIALIZACAO_VECTOR_SERVICOS_ONLINE, plaintext);
	}
	
}
