export interface InvestidorRelatorioResponse {
  // Dados básicos do investidor
  id: string;
  nome: string;
  cpf: string;
  email: string;
  perfilInvestidor: string;
  patrimonioAtual: number;
  rendaMensal: number;
  objetivos: string;
  
  // Estatísticas gerais
  totalCarteiras: number;
  totalAplicacoes: number;
  valorTotalInvestido: number;
  rentabilidadeMedia: number;
  
  // Estatísticas por tipo de produto
  valorEmAcoes: number;
  valorEmFII: number;
  valorEmRendaFixa: number;
  valorEmFundos: number;
  valorEmCripto: number;
  valorEmOutros: number;
  
  // Estatísticas por status
  aplicacoesAtivas: number;
  aplicacoesEncerradas: number;
  aplicacoesResgatadas: number;
  
  // Alertas e recomendações
  nivelAlerta: string; // BAIXO, MEDIO, ALTO
  recomendacaoPrincipal: string;
  
  // Metadados
  createdAt: string;
  updatedAt: string;
}

