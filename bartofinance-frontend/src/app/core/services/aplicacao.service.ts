import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/auth.model';
import { AplicacaoRequest, AplicacaoResponse, StatusAplicacao } from '../models/aplicacao.model';

@Injectable({
  providedIn: 'root'
})
export class AplicacaoService {
  private readonly API_URL = `${environment.apiUrl}/applications`;

  constructor(private http: HttpClient) {}

  /**
   * Lista todas as aplicações (com filtros opcionais)
   */
  listarAplicacoes(portfolioId?: string, status?: StatusAplicacao): Observable<ApiResponse<AplicacaoResponse[]>> {
    let params = new HttpParams();
    if (portfolioId) {
      params = params.set('portfolioId', portfolioId);
    }
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<ApiResponse<AplicacaoResponse[]>>(this.API_URL, { params });
  }

  /**
   * Busca uma aplicação por ID
   */
  buscarPorId(id: string): Observable<ApiResponse<AplicacaoResponse>> {
    return this.http.get<ApiResponse<AplicacaoResponse>>(`${this.API_URL}/${id}`);
  }

  /**
   * Cria uma nova aplicação
   */
  criarAplicacao(request: AplicacaoRequest): Observable<ApiResponse<AplicacaoResponse>> {
    return this.http.post<ApiResponse<AplicacaoResponse>>(this.API_URL, request);
  }

  /**
   * Atualiza uma aplicação existente
   */
  atualizarAplicacao(id: string, request: AplicacaoRequest): Observable<ApiResponse<AplicacaoResponse>> {
    return this.http.put<ApiResponse<AplicacaoResponse>>(`${this.API_URL}/${id}`, request);
  }

  /**
   * Deleta uma aplicação
   */
  deletarAplicacao(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`);
  }

  /**
   * Encerra uma aplicação (registra venda)
   */
  encerrarAplicacao(id: string, dataVenda: string, rentabilidadeFinal: number): Observable<ApiResponse<AplicacaoResponse>> {
    const request = {
      dataVenda,
      rentabilidadeAtual: rentabilidadeFinal,
      status: 'ENCERRADA'
    };
    return this.http.patch<ApiResponse<AplicacaoResponse>>(`${this.API_URL}/${id}/encerrar`, request);
  }
}

