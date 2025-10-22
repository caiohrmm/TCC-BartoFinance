import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/auth.model';
import { PortfolioResponse, PortfolioRequest } from '../models/portfolio.model';

@Injectable({
  providedIn: 'root'
})
export class PortfolioService {
  private readonly API_URL = `${environment.apiUrl}/portfolios`;

  constructor(private http: HttpClient) {}

  /**
   * Lista todos os portfolios do assessor autenticado
   */
  listarPortfolios(): Observable<ApiResponse<PortfolioResponse[]>> {
    return this.http.get<ApiResponse<PortfolioResponse[]>>(this.API_URL);
  }

  /**
   * Busca um portfolio por ID
   */
  buscarPorId(id: string): Observable<ApiResponse<PortfolioResponse>> {
    return this.http.get<ApiResponse<PortfolioResponse>>(`${this.API_URL}/${id}`);
  }

  /**
   * Cria um novo portfolio
   */
  criarPortfolio(request: PortfolioRequest): Observable<ApiResponse<PortfolioResponse>> {
    return this.http.post<ApiResponse<PortfolioResponse>>(this.API_URL, request);
  }

  /**
   * Atualiza um portfolio existente
   */
  atualizarPortfolio(id: string, request: PortfolioRequest): Observable<ApiResponse<PortfolioResponse>> {
    return this.http.put<ApiResponse<PortfolioResponse>>(`${this.API_URL}/${id}`, request);
  }

  /**
   * Deleta um portfolio
   */
  deletarPortfolio(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`);
  }

  /**
   * Simula um portfolio
   */
  simularPortfolio(request: PortfolioRequest): Observable<ApiResponse<PortfolioResponse>> {
    return this.http.post<ApiResponse<PortfolioResponse>>(`${this.API_URL}/simulate`, request);
  }
}

