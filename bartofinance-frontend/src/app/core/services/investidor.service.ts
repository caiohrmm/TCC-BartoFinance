import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/auth.model';
import { InvestidorResponse, InvestidorRequest } from '../models/investidor.model';

@Injectable({
  providedIn: 'root'
})
export class InvestidorService {
  private readonly API_URL = `${environment.apiUrl}/investors`;

  constructor(private http: HttpClient) {}

  /**
   * Lista todos os investidores do assessor autenticado
   */
  listarInvestidores(perfilInvestidor?: string): Observable<ApiResponse<InvestidorResponse[]>> {
    let params = new HttpParams();
    if (perfilInvestidor) {
      params = params.set('perfilInvestidor', perfilInvestidor);
    }
    return this.http.get<ApiResponse<InvestidorResponse[]>>(this.API_URL, { params });
  }

  /**
   * Busca um investidor por ID
   */
  buscarPorId(id: string): Observable<ApiResponse<InvestidorResponse>> {
    return this.http.get<ApiResponse<InvestidorResponse>>(`${this.API_URL}/${id}`);
  }

  /**
   * Cria um novo investidor
   */
  criarInvestidor(request: InvestidorRequest): Observable<ApiResponse<InvestidorResponse>> {
    return this.http.post<ApiResponse<InvestidorResponse>>(this.API_URL, request);
  }

  /**
   * Atualiza um investidor existente
   */
  atualizarInvestidor(id: string, request: InvestidorRequest): Observable<ApiResponse<InvestidorResponse>> {
    return this.http.put<ApiResponse<InvestidorResponse>>(`${this.API_URL}/${id}`, request);
  }

  /**
   * Deleta um investidor
   */
  deletarInvestidor(id: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`);
  }
}

