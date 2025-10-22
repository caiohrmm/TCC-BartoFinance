export type PerfilInvestidor = 'CONSERVADOR' | 'MODERADO' | 'AGRESSIVO';

export interface InvestidorRequest {
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  perfilInvestidor: PerfilInvestidor;
  patrimonioAtual: number;
  rendaMensal: number;
  objetivos: string;
}

export interface InvestidorResponse {
  id: string;
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  perfilInvestidor: PerfilInvestidor;
  patrimonioAtual: number;
  rendaMensal: number;
  objetivos: string;
  assessorId: string;
  createdAt: string;
  updatedAt: string;
}

export const PerfilInvestidorOptions = [
  { value: 'CONSERVADOR' as const, label: 'Conservador', color: 'blue', icon: '🛡️', description: 'Baixo risco, foco em segurança' },
  { value: 'MODERADO' as const, label: 'Moderado', color: 'yellow', icon: '⚖️', description: 'Risco médio, equilíbrio' },
  { value: 'AGRESSIVO' as const, label: 'Agressivo', color: 'red', icon: '🚀', description: 'Alto risco, maiores retornos' }
] as const;


