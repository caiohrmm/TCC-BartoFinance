import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { ConfirmModalComponent } from '../../../shared/components/confirm-modal/confirm-modal.component';
import { InvestidorService } from '../../../core/services/investidor.service';
import { PortfolioService } from '../../../core/services/portfolio.service';
import { AplicacaoService } from '../../../core/services/aplicacao.service';
import { ToastService } from '../../../core/services/toast.service';
import { AuthService } from '../../../core/services/auth.service';
import { InvestidorResponse, PerfilInvestidorOptions } from '../../../core/models/investidor.model';
import { PortfolioResponse, RiscoCarteiraOptions, TipoCarteiraOptions } from '../../../core/models/portfolio.model';
import { AplicacaoResponse } from '../../../core/models/aplicacao.model';

@Component({
  selector: 'app-investidor-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    FooterComponent,
    ConfirmModalComponent
  ],
  templateUrl: './investidor-detail.component.html',
  styleUrl: './investidor-detail.component.scss'
})
export class InvestidorDetailComponent implements OnInit {
  investidor = signal<InvestidorResponse | null>(null);
  carteiras = signal<PortfolioResponse[]>([]);
  aplicacoesPorCarteira = signal<Map<string, AplicacaoResponse[]>>(new Map());
  carteiraExpandida = signal<string | null>(null);
  loading = signal(false);
  showLogoutModal = signal(false);
  userName = signal('');
  investidorId: string = '';

  perfilOptions = PerfilInvestidorOptions;
  riscoOptions = RiscoCarteiraOptions;
  tipoOptions = TipoCarteiraOptions;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private investidorService: InvestidorService,
    private portfolioService: PortfolioService,
    private aplicacaoService: AplicacaoService,
    private toastService: ToastService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.userName.set(user.nome);
    }

    this.investidorId = this.route.snapshot.paramMap.get('id')!;
    if (this.investidorId) {
      this.carregarInvestidor();
      this.carregarCarteiras();
    }
  }

  carregarInvestidor(): void {
    this.loading.set(true);
    this.investidorService.buscarPorId(this.investidorId).subscribe({
      next: (response: any) => {
        this.investidor.set(response.data);
        this.loading.set(false);
      },
      error: (error: any) => {
        this.loading.set(false);
        this.toastService.error(error.error?.mensagem || 'Erro ao carregar investidor');
        this.router.navigate(['/investidores']);
      }
    });
  }

  carregarCarteiras(): void {
    this.portfolioService.listarPortfolios().subscribe({
      next: (response) => {
        // Filtrar carteiras deste investidor
        const carteirasDoInvestidor = response.data.filter(
          c => c.investidorId === this.investidorId
        );
        this.carteiras.set(carteirasDoInvestidor);
      },
      error: (error) => {
        this.toastService.error(error.error?.mensagem || 'Erro ao carregar carteiras');
      }
    });
  }

  toggleCarteira(carteiraId: string): void {
    if (this.carteiraExpandida() === carteiraId) {
      this.carteiraExpandida.set(null);
    } else {
      this.carteiraExpandida.set(carteiraId);
      this.carregarAplicacoesDaCarteira(carteiraId);
    }
  }

  carregarAplicacoesDaCarteira(carteiraId: string): void {
    // Verifica se já carregou
    if (this.aplicacoesPorCarteira().has(carteiraId)) {
      return;
    }

    this.aplicacaoService.listarAplicacoes().subscribe({
      next: (response) => {
        // Filtrar aplicações desta carteira
        const aplicacoesCarteira = response.data.filter(app => app.portfolioId === carteiraId);
        const map = new Map(this.aplicacoesPorCarteira());
        map.set(carteiraId, aplicacoesCarteira);
        this.aplicacoesPorCarteira.set(map);
      },
      error: () => {
        // Silencioso
      }
    });
  }

  getAplicacoesDaCarteira(carteiraId: string): AplicacaoResponse[] {
    return this.aplicacoesPorCarteira().get(carteiraId) || [];
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

  getPerfilInfo() {
    const inv = this.investidor();
    if (!inv) return null;
    return this.perfilOptions.find(p => p.value === inv.perfilInvestidor);
  }

  getRiscoInfo(risco: string) {
    return this.riscoOptions.find(r => r.value === risco);
  }

  getTipoInfo(tipo: string) {
    return this.tipoOptions.find(t => t.value === tipo);
  }

  formatarCpf(cpf: string): string {
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  formatarTelefone(telefone: string): string {
    if (telefone.length === 11) {
      return telefone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    }
    return telefone.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
  }

  formatarValor(valor: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
  }

  formatarPercentual(valor: number): string {
    return `${valor >= 0 ? '+' : ''}${valor.toFixed(2)}%`;
  }

  formatarData(data: string): string {
    return new Date(data).toLocaleDateString('pt-BR');
  }

  get totalEmCarteiras(): number {
    return this.carteiras().reduce((sum, c) => sum + c.valorTotal, 0);
  }

  get rentabilidadeMedia(): number {
    const carteiras = this.carteiras();
    if (carteiras.length === 0) return 0;
    const soma = carteiras.reduce((sum, c) => sum + c.rentabilidadeAtual, 0);
    return soma / carteiras.length;
  }

  get carteirasAtivas(): number {
    return this.carteiras().length;
  }
}
