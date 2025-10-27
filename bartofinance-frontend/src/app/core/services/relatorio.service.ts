import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { InvestidorRelatorioResponse } from '../models/relatorio.model';
import { ApiResponse } from '../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class RelatorioService {
  private readonly apiUrl = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) {}

  /**
   * Gera relatório completo do investidor
   */
  gerarRelatorioInvestidor(investidorId: string): Observable<InvestidorRelatorioResponse> {
    return this.http.get<ApiResponse<InvestidorRelatorioResponse>>(
      `${this.apiUrl}/investor/${investidorId}`
    ).pipe(
      map(response => response.data)
    );
  }

  /**
   * Formata valor monetário
   */
  formatarValor(valor: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  }

  /**
   * Formata percentual
   */
  formatarPercentual(valor: number): string {
    if (!valor || valor === 0) return '0,00%';
    
    const sinal = valor >= 0 ? '+' : '';
    return `${sinal}${valor.toFixed(2)}%`.replace('.', ',');
  }

  /**
   * Retorna cor do alerta
   */
  getCorAlerta(nivel: string): string {
    switch (nivel) {
      case 'BAIXO':
        return 'green';
      case 'MEDIO':
        return 'yellow';
      case 'ALTO':
        return 'red';
      default:
        return 'gray';
    }
  }

  /**
   * Retorna ícone do alerta
   */
  getIconeAlerta(nivel: string): string {
    switch (nivel) {
      case 'BAIXO':
        return '✅';
      case 'MEDIO':
        return '⚠️';
      case 'ALTO':
        return '🚨';
      default:
        return 'ℹ️';
    }
  }
}

