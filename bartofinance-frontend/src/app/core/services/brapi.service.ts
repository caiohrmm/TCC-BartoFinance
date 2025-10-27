import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface BrapiQuote {
  symbol: string;
  shortName: string;
  longName: string;
  currency: string;
  regularMarketPrice: number;
  regularMarketChange: number;
  regularMarketChangePercent: number;
  regularMarketTime: string;
  marketCap: number;
  regularMarketVolume: number;
  regularMarketPreviousClose: number;
  regularMarketOpen: number;
  regularMarketDayHigh: number;
  regularMarketDayLow: number;
  logourl?: string;
}

export interface BrapiResponse {
  results: BrapiQuote[];
  requestedAt: string;
  took: string;
}

@Injectable({
  providedIn: 'root'
})
export class BrapiService {
  private readonly API_URL = 'https://brapi.dev/api';
  private readonly API_KEY = environment.brapiApiKey;

  constructor(private http: HttpClient) {}

  /**
   * Busca cotações de múltiplos ativos
   * @param symbols Array de símbolos (ex: ['PETR4', 'VALE3', 'ITUB4'])
   */
  getQuotes(symbols: string[]): Observable<BrapiResponse> {
    const symbolsStr = symbols.join(',');
    
    // Ações gratuitas que não precisam de token (conforme doc Brapi)
    const freeStocks = ['PETR4', 'MGLU3', 'VALE3', 'ITUB4'];
    const allFree = symbols.every(s => freeStocks.includes(s));

    // Se todas as ações são gratuitas, não usa token
    if (allFree) {
      console.log('🆓 Buscando ações gratuitas (free tier):', symbolsStr);
      return this.http.get<BrapiResponse>(`${this.API_URL}/quote/${symbolsStr}`)
        .pipe(
          catchError(error => {
            console.error('❌ Erro ao buscar free stocks:', error);
            console.warn('ℹ️ Usando dados de exemplo');
            return of(this.getMockData(symbols));
          })
        );
    }

    // Para outros ativos, usa query param ?token= (conforme doc Brapi)
    if (!this.API_KEY) {
      console.warn('⚠️ API Key não configurada. Usando dados de exemplo.');
      return of(this.getMockData(symbols));
    }

    console.log('🔐 Buscando com token:', symbolsStr);
    
    // Brapi usa query parameter, não Bearer token
    return this.http.get<BrapiResponse>(`${this.API_URL}/quote/${symbolsStr}?token=${this.API_KEY}`)
      .pipe(
        catchError(error => {
          console.error('❌ Erro ao buscar cotações:', error);
          console.warn('ℹ️ Usando dados de exemplo. Verifique se o token está válido em https://brapi.dev/dashboard');
          return of(this.getMockData(symbols));
        })
      );
  }

  /**
   * Retorna dados mockados para demonstração
   */
  private getMockData(symbols: string[]): BrapiResponse {
    const mockQuotes: { [key: string]: BrapiQuote } = {
      '^BVSP': {
        symbol: '^BVSP',
        shortName: 'IBOVESPA',
        longName: 'Ibovespa Índice',
        currency: 'BRL',
        regularMarketPrice: 127845.50,
        regularMarketChange: 1234.50,
        regularMarketChangePercent: 0.97,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 126611.00,
        regularMarketOpen: 126800.00,
        regularMarketDayHigh: 128000.00,
        regularMarketDayLow: 126500.00
      },
      'USDBRL=X': {
        symbol: 'USDBRL=X',
        shortName: 'Dólar',
        longName: 'Dólar Americano / Real',
        currency: 'BRL',
        regularMarketPrice: 5.67,
        regularMarketChange: 0.03,
        regularMarketChangePercent: 0.53,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 5.64,
        regularMarketOpen: 5.65,
        regularMarketDayHigh: 5.68,
        regularMarketDayLow: 5.64
      },
      'EURBRL=X': {
        symbol: 'EURBRL=X',
        shortName: 'Euro',
        longName: 'Euro / Real',
        currency: 'BRL',
        regularMarketPrice: 6.12,
        regularMarketChange: -0.02,
        regularMarketChangePercent: -0.33,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 6.14,
        regularMarketOpen: 6.13,
        regularMarketDayHigh: 6.15,
        regularMarketDayLow: 6.11
      },
      'PETR4': {
        symbol: 'PETR4',
        shortName: 'Petrobras PN',
        longName: 'Petróleo Brasileiro S.A. - Petrobras',
        currency: 'BRL',
        regularMarketPrice: 38.45,
        regularMarketChange: 0.75,
        regularMarketChangePercent: 1.99,
        regularMarketTime: new Date().toISOString(),
        marketCap: 500000000000,
        regularMarketVolume: 50000000,
        regularMarketPreviousClose: 37.70,
        regularMarketOpen: 37.80,
        regularMarketDayHigh: 38.60,
        regularMarketDayLow: 37.65
      },
      'VALE3': {
        symbol: 'VALE3',
        shortName: 'Vale ON',
        longName: 'Vale S.A.',
        currency: 'BRL',
        regularMarketPrice: 65.32,
        regularMarketChange: -0.45,
        regularMarketChangePercent: -0.68,
        regularMarketTime: new Date().toISOString(),
        marketCap: 300000000000,
        regularMarketVolume: 30000000,
        regularMarketPreviousClose: 65.77,
        regularMarketOpen: 65.50,
        regularMarketDayHigh: 66.00,
        regularMarketDayLow: 65.10
      },
      'ITUB4': {
        symbol: 'ITUB4',
        shortName: 'Itaú Unibanco PN',
        longName: 'Itaú Unibanco Holding S.A.',
        currency: 'BRL',
        regularMarketPrice: 28.90,
        regularMarketChange: 0.30,
        regularMarketChangePercent: 1.05,
        regularMarketTime: new Date().toISOString(),
        marketCap: 280000000000,
        regularMarketVolume: 25000000,
        regularMarketPreviousClose: 28.60,
        regularMarketOpen: 28.65,
        regularMarketDayHigh: 29.00,
        regularMarketDayLow: 28.55
      },
      'MGLU3': {
        symbol: 'MGLU3',
        shortName: 'Magazine Luiza ON',
        longName: 'Magazine Luiza S.A.',
        currency: 'BRL',
        regularMarketPrice: 8.75,
        regularMarketChange: -0.25,
        regularMarketChangePercent: -2.78,
        regularMarketTime: new Date().toISOString(),
        marketCap: 60000000000,
        regularMarketVolume: 15000000,
        regularMarketPreviousClose: 9.00,
        regularMarketOpen: 8.95,
        regularMarketDayHigh: 9.10,
        regularMarketDayLow: 8.70
      },
      // FIIs
      'HGLG11': {
        symbol: 'HGLG11',
        shortName: 'CSHG Logística',
        longName: 'CSHG Logística FII',
        currency: 'BRL',
        regularMarketPrice: 165.50,
        regularMarketChange: 1.20,
        regularMarketChangePercent: 0.73,
        regularMarketTime: new Date().toISOString(),
        marketCap: 5000000000,
        regularMarketVolume: 500000,
        regularMarketPreviousClose: 164.30,
        regularMarketOpen: 164.50,
        regularMarketDayHigh: 166.00,
        regularMarketDayLow: 164.00
      },
      'KNRI11': {
        symbol: 'KNRI11',
        shortName: 'Kinea Renda',
        longName: 'Kinea Renda Imobiliária FII',
        currency: 'BRL',
        regularMarketPrice: 98.75,
        regularMarketChange: -0.35,
        regularMarketChangePercent: -0.35,
        regularMarketTime: new Date().toISOString(),
        marketCap: 3500000000,
        regularMarketVolume: 300000,
        regularMarketPreviousClose: 99.10,
        regularMarketOpen: 99.00,
        regularMarketDayHigh: 99.50,
        regularMarketDayLow: 98.50
      },
      'VISC11': {
        symbol: 'VISC11',
        shortName: 'Vinci Shopping',
        longName: 'Vinci Shopping Centers FII',
        currency: 'BRL',
        regularMarketPrice: 115.20,
        regularMarketChange: 0.80,
        regularMarketChangePercent: 0.70,
        regularMarketTime: new Date().toISOString(),
        marketCap: 4000000000,
        regularMarketVolume: 400000,
        regularMarketPreviousClose: 114.40,
        regularMarketOpen: 114.60,
        regularMarketDayHigh: 115.50,
        regularMarketDayLow: 114.30
      },
      'MXRF11': {
        symbol: 'MXRF11',
        shortName: 'Maxi Renda',
        longName: 'Maxi Renda FII',
        currency: 'BRL',
        regularMarketPrice: 10.45,
        regularMarketChange: 0.05,
        regularMarketChangePercent: 0.48,
        regularMarketTime: new Date().toISOString(),
        marketCap: 2000000000,
        regularMarketVolume: 200000,
        regularMarketPreviousClose: 10.40,
        regularMarketOpen: 10.42,
        regularMarketDayHigh: 10.50,
        regularMarketDayLow: 10.38
      },
      'BCFF11': {
        symbol: 'BCFF11',
        shortName: 'BTG Fundo de Fundos',
        longName: 'BTG Pactual Fundo de Fundos FII',
        currency: 'BRL',
        regularMarketPrice: 87.30,
        regularMarketChange: -0.20,
        regularMarketChangePercent: -0.23,
        regularMarketTime: new Date().toISOString(),
        marketCap: 3000000000,
        regularMarketVolume: 250000,
        regularMarketPreviousClose: 87.50,
        regularMarketOpen: 87.45,
        regularMarketDayHigh: 87.80,
        regularMarketDayLow: 87.10
      },
      // Índices Internacionais
      '^GSPC': {
        symbol: '^GSPC',
        shortName: 'S&P 500',
        longName: 'S&P 500 Index',
        currency: 'USD',
        regularMarketPrice: 5632.45,
        regularMarketChange: 15.30,
        regularMarketChangePercent: 0.27,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 5617.15,
        regularMarketOpen: 5620.00,
        regularMarketDayHigh: 5640.00,
        regularMarketDayLow: 5615.00
      },
      '^DJI': {
        symbol: '^DJI',
        shortName: 'Dow Jones',
        longName: 'Dow Jones Industrial Average',
        currency: 'USD',
        regularMarketPrice: 43275.90,
        regularMarketChange: -125.50,
        regularMarketChangePercent: -0.29,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 43401.40,
        regularMarketOpen: 43380.00,
        regularMarketDayHigh: 43450.00,
        regularMarketDayLow: 43200.00
      },
      'BTC-USD': {
        symbol: 'BTC-USD',
        shortName: 'Bitcoin',
        longName: 'Bitcoin USD',
        currency: 'USD',
        regularMarketPrice: 67234.50,
        regularMarketChange: 1234.50,
        regularMarketChangePercent: 1.87,
        regularMarketTime: new Date().toISOString(),
        marketCap: 1320000000000,
        regularMarketVolume: 25000000000,
        regularMarketPreviousClose: 66000.00,
        regularMarketOpen: 66500.00,
        regularMarketDayHigh: 67500.00,
        regularMarketDayLow: 66200.00
      }
    };

    const results = symbols
      .map(symbol => mockQuotes[symbol])
      .filter(quote => quote !== undefined);

    return {
      results,
      requestedAt: new Date().toISOString(),
      took: '0ms'
    };
  }

  /**
   * Busca cotação de um único ativo
   * @param symbol Símbolo do ativo (ex: 'PETR4')
   */
  getQuote(symbol: string): Observable<BrapiResponse> {
    return this.getQuotes([symbol]);
  }

  /**
   * Busca índices principais (Ibovespa, Dólar, etc)
   */
  getMainIndices(): Observable<BrapiResponse> {
    return this.getQuotes(['^BVSP', 'USDBRL=X', 'EURBRL=X']);
  }

  /**
   * Busca ações mais negociadas do dia
   * Usando apenas ações GRATUITAS (PETR4, VALE3, ITUB4, MGLU3)
   */
  getTopStocks(): Observable<BrapiResponse> {
    return this.getQuotes([
      'PETR4',  // Petrobras ⭐ GRÁTIS
      'VALE3',  // Vale ⭐ GRÁTIS
      'ITUB4',  // Itaú ⭐ GRÁTIS
      'MGLU3'   // Magazine Luiza ⭐ GRÁTIS
    ]);
  }

  /**
   * Busca FIIs populares
   */
  getTopFIIs(): Observable<BrapiResponse> {
    return this.getQuotes([
      'HGLG11',  // CSHG Logística
      'KNRI11',  // Kinea Renda Imobiliária
      'VISC11',  // Vinci Shopping Centers
      'MXRF11',  // Maxi Renda
      'BCFF11'   // BTG Pactual Fundo de Fundos
    ]);
  }

  /**
   * Busca índices internacionais
   */
  getInternationalIndices(): Observable<BrapiResponse> {
    return this.getQuotes([
      '^GSPC',   // S&P 500
      '^DJI',    // Dow Jones
      'BTC-USD'  // Bitcoin
    ]);
  }

  /**
   * Formata o preço com moeda
   */
  formatPrice(price: number, currency: string = 'BRL'): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: currency
    }).format(price);
  }

  /**
   * Formata a variação percentual
   */
  formatPercent(percent: number): string {
    const sign = percent >= 0 ? '+' : '';
    return `${sign}${percent.toFixed(2)}%`;
  }

  /**
   * Retorna a classe CSS baseada na variação
   */
  getVariationClass(change: number): string {
    if (change > 0) return 'text-green-600';
    if (change < 0) return 'text-red-600';
    return 'text-gray-600';
  }

  /**
   * Retorna o ícone baseado na variação
   */
  getVariationIcon(change: number): string {
    if (change > 0) return '📈';
    if (change < 0) return '📉';
    return '➡️';
  }

  /**
   * Formata volume em formato reduzido (K, M, B)
   */
  formatVolume(volume: number): string {
    if (volume >= 1000000000) {
      return (volume / 1000000000).toFixed(1) + 'B';
    }
    if (volume >= 1000000) {
      return (volume / 1000000).toFixed(1) + 'M';
    }
    if (volume >= 1000) {
      return (volume / 1000).toFixed(1) + 'K';
    }
    return volume.toString();
  }

  /**
   * Formata valor de mercado (market cap) em formato reduzido
   */
  formatMarketCap(marketCap: number): string {
    if (!marketCap || marketCap === 0) return 'N/D';
    
    if (marketCap >= 1000000000000) {
      return 'R$ ' + (marketCap / 1000000000000).toFixed(1) + 'T';
    }
    if (marketCap >= 1000000000) {
      return 'R$ ' + (marketCap / 1000000000).toFixed(1) + 'B';
    }
    if (marketCap >= 1000000) {
      return 'R$ ' + (marketCap / 1000000).toFixed(1) + 'M';
    }
    return 'R$ ' + marketCap.toFixed(0);
  }
}

