export type TipoProduto = 'CDB' | 'TESOURO_DIRETO' | 'ACOES' | 'FUNDOS' | 'CRIPTOMOEDAS' | 'OUTROS';
export type StatusAplicacao = 'ATIVA' | 'RESGATADA' | 'ENCERRADA';

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
  { value: 'CDB' as const, label: 'CDB', icon: '🏦', description: 'Certificado de Depósito Bancário' },
  { value: 'TESOURO_DIRETO' as const, label: 'Tesouro Direto', icon: '🏛️', description: 'Tesouro Direto' },
  { value: 'ACOES' as const, label: 'Ações', icon: '📈', description: 'Ações' },
  { value: 'FUNDOS' as const, label: 'Fundos', icon: '💼', description: 'Fundos de Investimento' },
  { value: 'CRIPTOMOEDAS' as const, label: 'Criptomoedas', icon: '₿', description: 'Criptomoedas' },
  { value: 'OUTROS' as const, label: 'Outros', icon: '📊', description: 'Outros' }
] as const;

export const StatusAplicacaoOptions = [
  { value: 'ATIVA' as const, label: 'Ativa', color: 'green', icon: '✓', description: 'Aplicação Ativa' },
  { value: 'RESGATADA' as const, label: 'Resgatada', color: 'blue', icon: '💰', description: 'Aplicação Resgatada' },
  { value: 'ENCERRADA' as const, label: 'Encerrada', color: 'gray', icon: '■', description: 'Aplicação Encerrada' }
] as const;

