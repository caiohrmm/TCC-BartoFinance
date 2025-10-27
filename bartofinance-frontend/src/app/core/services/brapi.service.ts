import { Injectable } from '@angular/core';
import axios, { AxiosError } from 'axios';
import { Observable, from, of, forkJoin } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
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

  constructor() {}

  /**
   * Busca cotações de múltiplos ativos usando Axios
   * IMPORTANTE: O plano FREE só permite 1 ação por requisição
   * Fazemos uma requisição por ação e combinamos os resultados
   */
  getQuotes(symbols: string[]): Observable<BrapiResponse> {
    console.log('📡 [BRAPI] Buscando:', symbols.join(', '));
    console.log('⚠️ [BRAPI] Free tier: fazendo 1 requisição por ação');
    
    // Para o plano free, fazemos uma requisição por ação
    const requests = symbols.map(symbol => {
      const url = `${this.API_URL}/quote/${symbol}?token=${this.API_KEY}`;
      
      return from(axios.get<BrapiResponse>(url))
        .pipe(
          map(response => response.data.results[0]),
          catchError((error: AxiosError) => {
            console.error(`❌ [BRAPI] Erro ao buscar ${symbol}:`, error.response?.status);
            return of(this.getMockData([symbol]).results[0]);
          })
        );
    });
    
    // forkJoin combina todos os Observables e retorna quando todos completarem
    return forkJoin(requests)
      .pipe(
        map((results: BrapiQuote[]) => ({
          results: results.filter(r => r !== null && r !== undefined),
          requestedAt: new Date().toISOString(),
          took: '0ms'
        })),
        catchError(() => of(this.getMockData(symbols)))
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
      'GBPBRL=X': {
        symbol: 'GBPBRL=X',
        shortName: 'Libra Esterlina',
        longName: 'Libra Esterlina / Real',
        currency: 'BRL',
        regularMarketPrice: 7.08,
        regularMarketChange: 0.05,
        regularMarketChangePercent: 0.71,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 7.03,
        regularMarketOpen: 7.05,
        regularMarketDayHigh: 7.10,
        regularMarketDayLow: 7.02
      },
      'JPYBRL=X': {
        symbol: 'JPYBRL=X',
        shortName: 'Iene Japonês',
        longName: 'Iene Japonês / Real',
        currency: 'BRL',
        regularMarketPrice: 0.038,
        regularMarketChange: 0.0002,
        regularMarketChangePercent: 0.53,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 0.0378,
        regularMarketOpen: 0.0379,
        regularMarketDayHigh: 0.0385,
        regularMarketDayLow: 0.0377
      },
      'CNYBRL=X': {
        symbol: 'CNYBRL=X',
        shortName: 'Yuan Chinês',
        longName: 'Yuan Chinês / Real',
        currency: 'BRL',
        regularMarketPrice: 0.78,
        regularMarketChange: -0.01,
        regularMarketChangePercent: -1.27,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 0.79,
        regularMarketOpen: 0.785,
        regularMarketDayHigh: 0.80,
        regularMarketDayLow: 0.77
      },
      'CHFBRL=X': {
        symbol: 'CHFBRL=X',
        shortName: 'Franco Suíço',
        longName: 'Franco Suíço / Real',
        currency: 'BRL',
        regularMarketPrice: 6.35,
        regularMarketChange: 0.03,
        regularMarketChangePercent: 0.47,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 6.32,
        regularMarketOpen: 6.33,
        regularMarketDayHigh: 6.38,
        regularMarketDayLow: 6.30
      },
      'CADBRL=X': {
        symbol: 'CADBRL=X',
        shortName: 'Dólar Canadense',
        longName: 'Dólar Canadense / Real',
        currency: 'BRL',
        regularMarketPrice: 4.18,
        regularMarketChange: 0.02,
        regularMarketChangePercent: 0.48,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 4.16,
        regularMarketOpen: 4.17,
        regularMarketDayHigh: 4.20,
        regularMarketDayLow: 4.15
      },
      'AUDBRL=X': {
        symbol: 'AUDBRL=X',
        shortName: 'Dólar Australiano',
        longName: 'Dólar Australiano / Real',
        currency: 'BRL',
        regularMarketPrice: 3.78,
        regularMarketChange: -0.01,
        regularMarketChangePercent: -0.26,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 3.79,
        regularMarketOpen: 3.785,
        regularMarketDayHigh: 3.82,
        regularMarketDayLow: 3.76
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
      'BBDC4': {
        symbol: 'BBDC4',
        shortName: 'Bradesco PN',
        longName: 'Banco Bradesco S.A.',
        currency: 'BRL',
        regularMarketPrice: 13.85,
        regularMarketChange: 0.15,
        regularMarketChangePercent: 1.09,
        regularMarketTime: new Date().toISOString(),
        marketCap: 135000000000,
        regularMarketVolume: 20000000,
        regularMarketPreviousClose: 13.70,
        regularMarketOpen: 13.75,
        regularMarketDayHigh: 13.90,
        regularMarketDayLow: 13.68
      },
      'B3SA3': {
        symbol: 'B3SA3',
        shortName: 'B3 ON',
        longName: 'B3 S.A. - Brasil, Bolsa, Balcão',
        currency: 'BRL',
        regularMarketPrice: 11.25,
        regularMarketChange: -0.10,
        regularMarketChangePercent: -0.88,
        regularMarketTime: new Date().toISOString(),
        marketCap: 60000000000,
        regularMarketVolume: 18000000,
        regularMarketPreviousClose: 11.35,
        regularMarketOpen: 11.30,
        regularMarketDayHigh: 11.40,
        regularMarketDayLow: 11.20
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
      },
      'ETH-USD': {
        symbol: 'ETH-USD',
        shortName: 'Ethereum',
        longName: 'Ethereum USD',
        currency: 'USD',
        regularMarketPrice: 3456.78,
        regularMarketChange: 56.78,
        regularMarketChangePercent: 1.67,
        regularMarketTime: new Date().toISOString(),
        marketCap: 420000000000,
        regularMarketVolume: 18000000000,
        regularMarketPreviousClose: 3400.00,
        regularMarketOpen: 3420.00,
        regularMarketDayHigh: 3480.00,
        regularMarketDayLow: 3380.00
      },
      'GC=F': {
        symbol: 'GC=F',
        shortName: 'Ouro',
        longName: 'Gold Futures',
        currency: 'USD',
        regularMarketPrice: 2654.80,
        regularMarketChange: 12.40,
        regularMarketChangePercent: 0.47,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 2642.40,
        regularMarketOpen: 2645.00,
        regularMarketDayHigh: 2660.00,
        regularMarketDayLow: 2640.00
      },
      '^IXIC': {
        symbol: '^IXIC',
        shortName: 'Nasdaq',
        longName: 'Nasdaq Composite Index',
        currency: 'USD',
        regularMarketPrice: 17234.56,
        regularMarketChange: 89.23,
        regularMarketChangePercent: 0.52,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 17145.33,
        regularMarketOpen: 17180.00,
        regularMarketDayHigh: 17280.00,
        regularMarketDayLow: 17120.00
      },
      '^N225': {
        symbol: '^N225',
        shortName: 'Nikkei 225',
        longName: 'Nikkei 225 Index (Japão)',
        currency: 'JPY',
        regularMarketPrice: 38543.90,
        regularMarketChange: 234.56,
        regularMarketChangePercent: 0.61,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 38309.34,
        regularMarketOpen: 38380.00,
        regularMarketDayHigh: 38680.00,
        regularMarketDayLow: 38250.00
      },
      '^FTSE': {
        symbol: '^FTSE',
        shortName: 'FTSE 100',
        longName: 'FTSE 100 Index (UK)',
        currency: 'GBP',
        regularMarketPrice: 8234.56,
        regularMarketChange: -45.23,
        regularMarketChangePercent: -0.55,
        regularMarketTime: new Date().toISOString(),
        marketCap: 0,
        regularMarketVolume: 0,
        regularMarketPreviousClose: 8279.79,
        regularMarketOpen: 8260.00,
        regularMarketDayHigh: 8290.00,
        regularMarketDayLow: 8210.00
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
   */
  getTopStocks(): Observable<BrapiResponse> {
    return this.getQuotes([
      'PETR4',  // Petrobras
      'VALE3',  // Vale
      'ITUB4',  // Itaú
      'BBDC4',  // Bradesco
      'MGLU3',  // Magazine Luiza
      'B3SA3'   // B3
    ]);
  }

  /**
   * Busca moedas (câmbio)
   */
  getTopCurrencies(): Observable<BrapiResponse> {
    return this.getQuotes([
      'USDBRL=X',  // Dólar
      'EURBRL=X',  // Euro
      'GBPBRL=X',  // Libra Esterlina
      'JPYBRL=X',  // Iene Japonês
      'CNYBRL=X',  // Yuan Chinês
      'CHFBRL=X',  // Franco Suíço
      'CADBRL=X',  // Dólar Canadense
      'AUDBRL=X'   // Dólar Australiano
    ]);
  }

  /**
   * Busca índices internacionais
   */
  getInternationalIndices(): Observable<BrapiResponse> {
    return this.getQuotes([
      '^GSPC',   // S&P 500
      '^DJI',    // Dow Jones
      '^IXIC',   // Nasdaq Composite
      '^N225',   // Nikkei 225 (Japão)
      '^FTSE',   // FTSE 100 (Reino Unido)
      'BTC-USD', // Bitcoin
      'ETH-USD', // Ethereum
      'GC=F'     // Ouro (Gold)
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

