import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/auth.model';

export interface AIAnaliseRequest {
  nome?: string;
  perfil?: string;
  rendaMensal?: number;
  patrimonioAtual?: number;
  nomeCarteira?: string;
  tipoCarteira?: string;
  riscoCarteira?: string;
  valorTotal?: number;
  rentabilidadeAtual?: number;
  metaRentabilidade?: number;
  perfilInvestidor?: string;
  valorDisponivel?: number;
  prompt?: string;
}

export interface AIAnaliseResponse {
  analise?: string;
  sugestao?: string;
  insight?: string;
  [key: string]: string | undefined;
}

@Injectable({
  providedIn: 'root'
})
export class AIService {
  private apiUrl = `${environment.apiUrl}/ai`;

  constructor(private http: HttpClient) {}

  /**
   * Analisa perfil de investidor com IA
   */
  analisarPerfil(request: AIAnaliseRequest): Observable<ApiResponse<AIAnaliseResponse>> {
    return this.http.post<ApiResponse<AIAnaliseResponse>>(
      `${this.apiUrl}/analisar-perfil`,
      request
    );
  }

  /**
   * Analisa carteira de investimentos com IA
   */
  analisarCarteira(request: AIAnaliseRequest): Observable<ApiResponse<AIAnaliseResponse>> {
    return this.http.post<ApiResponse<AIAnaliseResponse>>(
      `${this.apiUrl}/analisar-carteira`,
      request
    );
  }

  /**
   * Sugere diversificação de carteira
   */
  sugerirDiversificacao(request: AIAnaliseRequest): Observable<ApiResponse<AIAnaliseResponse>> {
    return this.http.post<ApiResponse<AIAnaliseResponse>>(
      `${this.apiUrl}/sugerir-diversificacao`,
      request
    );
  }

  /**
   * Gera insight genérico
   */
  gerarInsight(request: AIAnaliseRequest): Observable<ApiResponse<AIAnaliseResponse>> {
    return this.http.post<ApiResponse<AIAnaliseResponse>>(
      `${this.apiUrl}/gerar-insight`,
      request
    );
  }
}

