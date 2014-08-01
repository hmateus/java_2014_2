/*
 * Implementação do Bloqueto de Cobranças do Banco do Brasil
 * - Convênio com 1000000
 * 
 * scoelho@stefanini.com
 */
package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public class BloquetoBBConvenioAcima1000000 extends BloquetoBBImpl implements
		BloquetoBB {

	@Override
	protected void validaDados() throws ManagerException {

		// TODO: COMPLETAR

		if (codigoBanco == null || codigoBanco.length() != 3) {
			throw new ManagerException(
					"Código do Banco não informado ou com tamanho diferente de 3 posições");
		}

		if (codigoMoeda == null || codigoMoeda.length() != 1) {
			throw new ManagerException(
					"Código de moeda não informado ou inválido");
		}

		if (dataVencimento == null) {
			throw new ManagerException("Data de vencimento não informada");
		}

		if (valor == null) {
			throw new ManagerException(
					"Valor do bloqueto bancÃ¡rio não informado");
		}

		if (numeroConvenioBanco == null || numeroConvenioBanco.length() != 7) {
			throw new ManagerException(
					"número de convênio não informado ou o convênio informado é inválido. O convênio deve ter 7 posições");
		}

		if (complementoNumeroConvenioBancoSemDV == null
				&& complementoNumeroConvenioBancoSemDV.length() != 10) {
			throw new ManagerException(
					"Complemento do número do convênio não informado. O complemento deve ter 10 posições");
		}

		if (numeroAgenciaRelacionamento == null
				|| numeroAgenciaRelacionamento.length() != 4) {
			throw new ManagerException(
					"número da agência de Relacionamento não informado. O número da agência deve ter 4 posições");
		}

		if (contaCorrenteRelacionamentoSemDV == null
				|| contaCorrenteRelacionamentoSemDV.length() != 8) {
			throw new ManagerException(
					"Conta corrente de relacionamento não informada. O número da conta deve ter 8 posições");
		}

		if (tipoCarteira == null || tipoCarteira.length() != 2) {
			throw new ManagerException(
					"Tipo carteira não informado ou o valor é inválido");
		}

		if (dataBase == null) {
			throw new ManagerException("A database não foi informada.");
		}

	}

	public BloquetoBBConvenioAcima1000000(String codigoBanco,
			String codigoMoeda, Date dataVencimento, Date dataBase,
			BigDecimal valor, String numeroConvenioBanco,
			String complementoNumeroConvenioBancoSemDV,
			String numeroAgenciaRelacionamento,
			String contaCorrenteRelacionamentoSemDV, String tipoCarteira)
			throws ManagerException {

		// TODO: COMPLETAR
		this.codigoBanco = codigoBanco;
		this.codigoMoeda = codigoMoeda;
		this.dataVencimento = dataVencimento;
		this.valor = valor;
		this.numeroConvenioBanco = numeroConvenioBanco;
		this.complementoNumeroConvenioBancoSemDV = complementoNumeroConvenioBancoSemDV;
		this.numeroAgenciaRelacionamento = numeroAgenciaRelacionamento;
		this.contaCorrenteRelacionamentoSemDV = contaCorrenteRelacionamentoSemDV;
		this.tipoCarteira = tipoCarteira;
		this.dataBase = dataBase;

		validaDados();

	}

	// TODO: @sandro - refatorar os mÃ©todos getCodigoBarrasSemDigito() e
	// getCodigoBarras()
	@Override
	protected String getCodigoBarrasSemDigito() {

		init();

		StringBuilder buffer = new StringBuilder();

		// TODO: COMPLETAR
		buffer.append(codigoBanco);		//pos 1 a 3
		buffer.append(codigoMoeda);		//pos 4
		buffer.append(fatorVencimento);		//pos 6 a 9
		buffer.append(getValorFormatado());		//pos 10 a 19
		buffer.append("000000");		//pos 20 a 25
		buffer.append(numeroConvenioBanco);		//pos 26 a 32
		buffer.append(complementoNumeroConvenioBancoSemDV);		//pos 33 a 42
		buffer.append(tipoCarteira);		//pos 43 a 44
		
		return buffer.toString();
	}

	@Override
	public String getCodigoBarras() {

		init();

		StringBuilder buffer = new StringBuilder();

		// TODO: COMPLETAR
		buffer.append(codigoBanco);		//pos 1 a 3
		buffer.append(codigoMoeda);		//pos 4
		buffer.append(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito()));		//pos 5
		buffer.append(fatorVencimento);		//pos 6 a 9
		buffer.append(getValorFormatado());		//pos 10 a 19
		buffer.append("000000");//ou String.format("%06d", 0) pros 6 zeros	//pos 20 a 25 
		buffer.append(numeroConvenioBanco);		//pos 26 a 32
		buffer.append(complementoNumeroConvenioBancoSemDV);		//pos 33 a 42
		buffer.append(tipoCarteira);		//pos 43 a 44

		return buffer.toString();
	}

	@Override
	protected String getLDNumeroConvenio() {

		String convenio = String.format("%07d", Long.valueOf(numeroConvenioBanco));
		
		return String.format("%s.%s", convenio.substring(0, 1), convenio.substring(1, 5));

	}

}
