export type TipoCarteira = 'MODELO' | 'PERSONALIZADA';
export type RiscoCarteira = 'BAIXO' | 'MODERADO' | 'ALTO';

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
  { value: 'MODELO' as const, label: 'Modelo', icon: '📋', description: 'Carteira modelo para replicação' },
  { value: 'PERSONALIZADA' as const, label: 'Personalizada', icon: '⭐', description: 'Carteira personalizada para investidor' }
] as const;

export const RiscoCarteiraOptions = [
  { value: 'BAIXO' as const, label: 'Baixo', color: 'blue', icon: '🛡️', description: 'Investimentos conservadores' },
  { value: 'MODERADO' as const, label: 'Moderado', color: 'yellow', icon: '⚖️', description: 'Equilíbrio entre segurança e rentabilidade' },
  { value: 'ALTO' as const, label: 'Alto', color: 'red', icon: '🚀', description: 'Busca maior rentabilidade com maior exposição' }
] as const;

