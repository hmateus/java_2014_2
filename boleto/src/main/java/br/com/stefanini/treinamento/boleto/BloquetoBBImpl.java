package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public abstract class BloquetoBBImpl implements BloquetoBB {

	protected String codigoBanco;
	protected String codigoMoeda;
	protected String fatorVencimento;
	protected Date dataVencimento;
	protected Date dataBase;
	protected BigDecimal valor;
	protected String numeroConvenioBanco;
	protected String complementoNumeroConvenioBancoSemDV;
	protected String numeroAgenciaRelacionamento;
	protected String contaCorrenteRelacionamentoSemDV;
	protected String tipoCarteira;

	private int dvCodigoBarras;

	protected abstract void validaDados() throws ManagerException;

	/**
	 * Inicializa o fator de vencimento
	 */
	protected void setFatorVencimento() {

		long dias = diferencaEmDias(dataBase, dataVencimento);

		// TODO: EXPLICAR O QUE ESTE M�TODO EST� FAZENDO
		/*
		 * Chama o m�todo diferencaEmDias para calcular a qtde de dias passados
		 * e faz a atribui��o do valor em fatorVencimento, utilizando 4 casas, as quais s�o completadas
		 * com zero de acordo com o n� de dias. Ex: dias = 3 -> fatorVencimento = 0003
		 */
		fatorVencimento = String.format("%04d", dias);

	}

	/**
	 * Inicializa os valores, formata
	 */
	protected void init() {

		setFatorVencimento();

	}

	/**
	 * Retorna o valor formatado do boleto banc�rio
	 * 
	 * @return
	 */
	protected String getValorFormatado() {

		// TODO: Explicar o que este m�todo est� fazendo
		/*
		 * Complementa com zeros antes do valor para completar 8 casas e tira
		 * o ponto do decimal, transformando-o em uma String 
		 */
		return String.format(
				"%010d",
				Long.valueOf(valor.setScale(2, RoundingMode.HALF_UP).toString()
						.replace(".", "")));
	}

	/**
	 * Formata o n�mero do conv�nio da Linha Digit�vel
	 * 
	 * @return
	 */
	protected abstract String getLDNumeroConvenio();

	/**
	 * Retorna o c�digo de barras do Bloqueto
	 * 
	 * @return c�digo de barras
	 */
	protected abstract String getCodigoBarrasSemDigito();

	public abstract String getCodigoBarras();

	/**
	 * Campo 5 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo5() {
		// TODO: COMPLETAR
		StringBuilder buffer = new StringBuilder();
		
		buffer.append(fatorVencimento);		//pos 6 a 9
		buffer.append(getValorFormatado());		//pos 10 a 19
		
		return buffer.toString();
	}

	/**
	 * Campo 4 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo4() {
		// TODO: COMPLETAR
		return String.valueOf(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito()));
	}

	/**
	 * Campo 3 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo3() {

		// TODO: COMPLETAR
		return String.format("%s.%s", getCodigoBarras().substring(34, 39),
				getCodigoBarras().substring(39, 44));
	}

	/**
	 * Campo 2 da Linha Digit�vel
	 * 
	 * @return
	 */
	private String ldCampo2() {
		// TODO: COMPLETAR
		return String.format("%s.%s", getCodigoBarras().substring(24, 29),
				getCodigoBarras().substring(29, 34));
	}

	/**
	 * Calcula o digito verificador do campo
	 * 
	 * @param campo
	 * @return
	 */
	protected int digitoVerificadorPorCampo(String campo) {
		// TODO: COMPLETAR
		int soma = 0, j = 0;
		int[] pesos = {2, 1};
		int tamanho = campo.length();
		for(int i = tamanho - 1; i >= 0; i++){
			if(j == 2){
				j = 0;
			}
			int aux = Integer.valueOf(campo.substring(i, i+1));
			//System.out.println(aux);
			int produto = ( aux * pesos[j] );
			if(produto > 9){
				produto = produto/10 + produto%10;
			}
			soma += produto;
			j++;
		}
		j = soma - 10;
		if(j == 10){
			return 0;
		}else{
			return j;
		}
	}

	/**
	 * Calcula o digito verificado do c�digo de barras
	 * 
	 * @param codigoBarras
	 * @return
	 */
	protected int digitoVerificadorCodigoBarras(String codigoBarras) {
		// TODO: COMPLETAR
		/*
		 * Multiplicar os valores das posi��es, exceto da posi��o 5, e somar os valores.
		 * Depois, divide-se essa soma por 11 e pega-se o resto.
		 * Este resto � subtra�do de 11 e se o resultado for: igual a 0 ou 10 ou 11->DV = 1; != de 10 e 11->DV pr�prio resultado
		 * DV NUNCA SER� IGUAL A 0
		 */
		int soma = 0, j = 0, aux = 0;
		int[] pesos = {2, 3, 4, 5, 6, 7, 8, 9};
		int tamanho = codigoBarras.length();
		for(int i = tamanho - 1; i >= 0; i++){
			if(j == 9){
				j = 0;
			}
			aux = Integer.valueOf(codigoBarras.substring(i, i+1));
			soma += (aux * pesos[j]);
			j++;
		}
		int resto = soma % 11;
		
		j = 11 - resto;
		if(j == 0 || j == 10 || j == 11){
			return 1;
		}else{
			return j;
		}
	}

	/**
	 * Campo 1 da Linha Digit�vel
	 * 
	 * - C�digo do Banco - C�digo da Moeda - N�mero do conv�nio
	 * 
	 * @return
	 */
	private String ldCampo1() {
		StringBuilder buffer = new StringBuilder();
		// TODO: COMPLETAR
		buffer.append(codigoBanco);		//pos 1 a 3
		buffer.append(codigoMoeda);		//pos 4
		buffer.append(numeroConvenioBanco);		//pos 20 a 24
		
		return buffer.toString();

	}

	public String getLinhaDigitavel() {

		init();

		StringBuilder buffer = new StringBuilder();
		// TODO: COMPLETAR
		buffer.append(ldCampo1());
		buffer.append(digitoVerificadorPorCampo(ldCampo1()));
		buffer.append(" ");
		
		buffer.append(ldCampo2());
		buffer.append(digitoVerificadorPorCampo(ldCampo2()));
		buffer.append(" ");
		
		buffer.append(ldCampo3());
		buffer.append(digitoVerificadorPorCampo(ldCampo3()));
		buffer.append(" ");

		buffer.append(ldCampo4());
		buffer.append(" ");
		
		buffer.append(ldCampo5());
		
		return buffer.toString();
	}

	/**
	 * Retorna a diferen�a em dias de duas datas
	 * 
	 * @param dataInicial
	 *            Data inicial
	 * @param dataFinal
	 *            Data final
	 * @return
	 */
	protected static long diferencaEmDias(Date dataInicial, Date dataFinal) {

		// TODO: Estude a Math e escreva aqui o que este m�todo est� fazendo
		/*
		 * Transforma-se as datas em milissigundos, faz a subtra��o das mesmas e, depois, 
		 * divide-se o resultado pelo n� de milisigundos de um dia, para transformar
		 * em qtde de dias
		 */

		return Math
				.round((dataFinal.getTime() - dataInicial.getTime()) / 86400000D);
	}

	public int getDvCodigoBarras() {

		getCodigoBarras();

		return dvCodigoBarras;
	}
}
