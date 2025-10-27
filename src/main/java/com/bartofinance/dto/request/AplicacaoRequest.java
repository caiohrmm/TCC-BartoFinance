package com.bartofinance.dto.request;

import com.bartofinance.model.enums.StatusAplicacao;
import com.bartofinance.model.enums.TipoProduto;
import com.bartofinance.validation.ValidCodigoAtivo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para requisição de criação/atualização de Aplicação
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AplicacaoRequest {

    @NotBlank(message = "Portfolio ID é obrigatório")
    private String portfolioId;

    @NotNull(message = "Tipo de produto é obrigatório")
    private TipoProduto tipoProduto;

    @NotBlank(message = "Código do ativo é obrigatório")
    @Size(max = 20, message = "Código do ativo deve ter no máximo 20 caracteres")
    private String codigoAtivo;

    @NotNull(message = "Valor aplicado é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor aplicado deve ser maior que zero")
    private BigDecimal valorAplicado;

    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
    private BigDecimal quantidade;

    @NotNull(message = "Data de compra é obrigatória")
    private LocalDateTime dataCompra;

    private LocalDateTime dataVenda;

    @DecimalMin(value = "0.0", message = "Rentabilidade deve ser maior ou igual a zero")
    private BigDecimal rentabilidadeAtual;

    private StatusAplicacao status;

    @Size(max = 500, message = "Notas devem ter no máximo 500 caracteres")
    private String notas;
}

