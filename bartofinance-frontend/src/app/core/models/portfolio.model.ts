export type TipoCarteira = 'SUGERIDA' | 'PERSONALIZADA';
export type RiscoCarteira = 'BAIXO' | 'MEDIO' | 'ALTO';

export interface PortfolioRequest {
  nome: string;
  descricao: string;
  tipo: TipoCarteira;
  risco: RiscoCarteira;
  metaRentabilidade: number;
  investidorId?: string;
}

export interface PortfolioResponse {
  id: string;
  nome: string;
  descricao: string;
  tipo: TipoCarteira;
  risco: RiscoCarteira;
  metaRentabilidade: number;
  rentabilidadeAtual: number;
  valorTotal: number;
  investidorId?: string;
  assessorId: string;
  createdAt: string;
  updatedAt: string;
}

export const TipoCarteiraOptions = [
  { value: 'SUGERIDA' as const, label: 'Sugerida', icon: '💡', description: 'Carteira modelo sugerida' },
  { value: 'PERSONALIZADA' as const, label: 'Personalizada', icon: '⭐', description: 'Carteira personalizada para investidor' }
] as const;

export const RiscoCarteiraOptions = [
  { value: 'BAIXO' as const, label: 'Baixo', color: 'blue', icon: '🛡️', description: 'Risco baixo' },
  { value: 'MEDIO' as const, label: 'Médio', color: 'yellow', icon: '⚖️', description: 'Risco médio' },
  { value: 'ALTO' as const, label: 'Alto', color: 'red', icon: '🚀', description: 'Risco alto' }
] as const;

