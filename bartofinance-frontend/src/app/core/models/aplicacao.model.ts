export type TipoProduto = 'ACOES' | 'FUNDOS' | 'RENDA_FIXA' | 'CRIPTOMOEDAS' | 'OUTROS';
export type StatusAplicacao = 'ATIVA' | 'ENCERRADA' | 'SIMULADA';

export interface AplicacaoRequest {
  portfolioId: string;
  tipoProduto: TipoProduto;
  codigoAtivo: string;
  valorAplicado: number;
  quantidade: number;
  dataCompra: string; // ISO date
  rentabilidadeAtual?: number;
  notas?: string;
}

export interface AplicacaoResponse {
  id: string;
  portfolioId: string;
  tipoProduto: TipoProduto;
  codigoAtivo: string;
  valorAplicado: number;
  quantidade: number;
  dataCompra: string;
  dataVenda?: string;
  rentabilidadeAtual: number;
  status: StatusAplicacao;
  notas?: string;
  createdAt: string;
  updatedAt: string;
}

export const TipoProdutoOptions = [
  { value: 'ACOES' as const, label: 'Ações', icon: '📈', description: 'Ações na bolsa' },
  { value: 'FUNDOS' as const, label: 'Fundos', icon: '💼', description: 'Fundos de investimento' },
  { value: 'RENDA_FIXA' as const, label: 'Renda Fixa', icon: '🏦', description: 'CDB, LCI, LCA, Tesouro' },
  { value: 'CRIPTOMOEDAS' as const, label: 'Criptomoedas', icon: '₿', description: 'Bitcoin, Ethereum, etc' },
  { value: 'OUTROS' as const, label: 'Outros', icon: '📊', description: 'Outros ativos' }
] as const;

export const StatusAplicacaoOptions = [
  { value: 'ATIVA' as const, label: 'Ativa', color: 'green', icon: '✓' },
  { value: 'ENCERRADA' as const, label: 'Encerrada', color: 'gray', icon: '■' },
  { value: 'SIMULADA' as const, label: 'Simulada', color: 'blue', icon: '🎯' }
] as const;

