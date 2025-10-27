import { Component, OnInit, signal, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { InvestidorService } from '../../core/services/investidor.service';
import { PortfolioService } from '../../core/services/portfolio.service';
import { AplicacaoService } from '../../core/services/aplicacao.service';
import { BrapiService, BrapiQuote } from '../../core/services/brapi.service';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { ConfirmModalComponent } from '../../shared/components/confirm-modal/confirm-modal.component';
import { ThemeToggleComponent } from '../../shared/components/theme-toggle/theme-toggle.component';
import { forkJoin, interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, FooterComponent, ConfirmModalComponent, ThemeToggleComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit, OnDestroy {
  userName = signal('');
  showLogoutModal = signal(false);
  loading = signal(true);
  stats = signal({
    investidores: 0,
    carteiras: 0,
    aplicacoes: 0,
    valorTotal: 0
  });

  // Cotações da Brapi
  marketQuotes = signal<BrapiQuote[]>([]);
  topStocks = signal<BrapiQuote[]>([]);
  currencies = signal<BrapiQuote[]>([]);
  internationalIndices = signal<BrapiQuote[]>([]);
  loadingQuotes = signal(true);
  
  // Controles de expansão "Ver mais"
  expandedStocks = signal(false);
  expandedCurrencies = signal(false);
  expandedInternational = signal(false);
  
  private quotesSubscription?: Subscription;

  constructor(
    private authService: AuthService,
    private investidorService: InvestidorService,
    private portfolioService: PortfolioService,
    private aplicacaoService: AplicacaoService,
    public brapiService: BrapiService
  ) {}

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.userName.set(user.nome);
    }
    this.carregarDashboard();
    this.carregarCotacoes();
    
    // Atualizar cotações a cada 1 minuto
    this.quotesSubscription = interval(60000).subscribe(() => {
      this.carregarCotacoes();
    });
  }

  ngOnDestroy(): void {
    if (this.quotesSubscription) {
      this.quotesSubscription.unsubscribe();
    }
  }

  carregarDashboard(): void {
    this.loading.set(true);
    
    forkJoin({
      investidores: this.investidorService.listarInvestidores(),
      carteiras: this.portfolioService.listarPortfolios(),
      aplicacoes: this.aplicacaoService.listarAplicacoes()
    }).subscribe({
      next: (response) => {
        const investidores = response.investidores.data || [];
        const carteiras = response.carteiras.data || [];
        const aplicacoes = response.aplicacoes.data || [];
        
        // Calcular valor total somando valorTotal de cada carteira
        const valorTotal = carteiras.reduce((sum, cart) => {
          const valor = cart.valorTotal || 0;
          console.log(`Carteira ${cart.nome}: R$ ${valor}`);
          return sum + valor;
        }, 0);
        
        console.log(`Total de carteiras: ${carteiras.length}, Valor total: R$ ${valorTotal}`);
        
        this.stats.set({
          investidores: investidores.length,
          carteiras: carteiras.length,
          aplicacoes: aplicacoes.length,
          valorTotal: valorTotal
        });
        
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Erro ao carregar dashboard:', error);
        this.loading.set(false);
      }
    });
  }

  abrirModalLogout(): void {
    this.showLogoutModal.set(true);
  }

  confirmarLogout(): void {
    this.showLogoutModal.set(false);
    this.authService.logout();
  }

  cancelarLogout(): void {
    this.showLogoutModal.set(false);
  }

  carregarCotacoes(): void {
    this.loadingQuotes.set(true);

    forkJoin({
      indices: this.brapiService.getMainIndices(),
      stocks: this.brapiService.getTopStocks(),
      currencies: this.brapiService.getTopCurrencies(),
      international: this.brapiService.getInternationalIndices()
    }).subscribe({
      next: (response) => {
        this.marketQuotes.set(response.indices.results || []);
        this.topStocks.set(response.stocks.results || []);
        this.currencies.set(response.currencies.results || []);
        this.internationalIndices.set(response.international.results || []);
        this.loadingQuotes.set(false);
      },
      error: (error) => {
        console.error('Erro ao carregar cotações:', error);
        this.loadingQuotes.set(false);
      }
    });
  }

  // Métodos para expandir/recolher listas
  toggleExpandStocks(): void {
    this.expandedStocks.set(!this.expandedStocks());
  }

  toggleExpandCurrencies(): void {
    this.expandedCurrencies.set(!this.expandedCurrencies());
  }

  toggleExpandInternational(): void {
    this.expandedInternational.set(!this.expandedInternational());
  }

  // Método para pegar apenas os primeiros 3 itens ou todos se expandido
  getFirstItems<T>(items: T[], expanded: boolean): T[] {
    return expanded ? items : items.slice(0, 3);
  }

  // Verifica se tem mais itens para mostrar
  hasMoreItems(items: any[], expanded: boolean): boolean {
    return items.length > 3 && !expanded;
  }

  // Verifica se está expandido e pode recolher
  canCollapse(items: any[], expanded: boolean): boolean {
    return items.length > 3 && expanded;
  }
}


