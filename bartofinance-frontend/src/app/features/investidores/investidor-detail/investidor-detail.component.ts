import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { ConfirmModalComponent } from '../../../shared/components/confirm-modal/confirm-modal.component';
import { InvestidorService } from '../../../core/services/investidor.service';
import { PortfolioService } from '../../../core/services/portfolio.service';
import { AplicacaoService } from '../../../core/services/aplicacao.service';
import { AIService } from '../../../core/services/ai.service';
import { RelatorioService } from '../../../core/services/relatorio.service';
import { ToastService } from '../../../core/services/toast.service';
import { AuthService } from '../../../core/services/auth.service';
import { InvestidorResponse, PerfilInvestidorOptions } from '../../../core/models/investidor.model';
import { PortfolioResponse, RiscoCarteiraOptions, TipoCarteiraOptions } from '../../../core/models/portfolio.model';
import { AplicacaoResponse } from '../../../core/models/aplicacao.model';
import { InvestidorRelatorioResponse } from '../../../core/models/relatorio.model';
import { NgApexchartsModule } from 'ng-apexcharts';

@Component({
  selector: 'app-investidor-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    FooterComponent,
    ConfirmModalComponent,
    NgApexchartsModule
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
  
  // IA Insights
  aiInsight = signal<string>('');
  loadingInsight = signal(false);

  // Relatório
  relatorio = signal<InvestidorRelatorioResponse | null>(null);
  loadingRelatorio = signal(false);

  // Gráficos
  chartOptions: any;
  performanceChartOptions: any;
  distribTipoChartOptions: any;

  perfilOptions = PerfilInvestidorOptions;
  riscoOptions = RiscoCarteiraOptions;
  tipoOptions = TipoCarteiraOptions;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private investidorService: InvestidorService,
    private portfolioService: PortfolioService,
    private aplicacaoService: AplicacaoService,
    private aiService: AIService,
    private relatorioService: RelatorioService,
    private toastService: ToastService,
    private authService: AuthService
  ) {
    this.initializeCharts();
  }

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.userName.set(user.nome);
    }

    this.investidorId = this.route.snapshot.paramMap.get('id')!;
    if (this.investidorId) {
      this.carregarInvestidor();
      this.carregarCarteiras();
      this.carregarRelatorio();
    }
  }

  carregarRelatorio(): void {
    this.loadingRelatorio.set(true);
    this.relatorioService.gerarRelatorioInvestidor(this.investidorId).subscribe({
      next: (relatorio) => {
        this.relatorio.set(relatorio);
        this.loadingRelatorio.set(false);
      },
      error: (error) => {
        console.error('Erro ao carregar relatório:', error);
        this.loadingRelatorio.set(false);
        // Não exibe erro para não impactar experiência
      }
    });
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
        this.atualizarGraficos();
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

  // Métodos públicos para acesso no template
  formatarValorRelatorio(valor: number): string {
    return this.relatorioService.formatarValor(valor);
  }

  getIconeAlertaRelatorio(nivel: string): string {
    return this.relatorioService.getIconeAlerta(nivel);
  }

  initializeCharts(): void {
    // Gráfico de Distribuição por Tipo de Carteira
    this.chartOptions = {
      series: [],
      chart: {
        type: 'donut',
        height: 300
      },
      labels: [],
      colors: ['#3B82F6', '#10B981', '#F59E0B', '#EF4444'],
      legend: {
        position: 'bottom'
      },
      dataLabels: {
        enabled: true,
        formatter: (val: number) => val.toFixed(1) + '%'
      }
    };

    // Gráfico de Desempenho das Carteiras
    this.performanceChartOptions = {
      series: [{
        name: 'Rentabilidade',
        data: []
      }],
      chart: {
        type: 'bar',
        height: 300
      },
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: '55%',
          dataLabels: {
            position: 'top'
          }
        }
      },
      dataLabels: {
        enabled: true,
        formatter: (val: number) => val.toFixed(2) + '%',
        offsetY: -20,
        style: {
          fontSize: '12px',
          colors: ['#304758']
        }
      },
      xaxis: {
        categories: []
      },
      yaxis: {
        title: {
          text: 'Rentabilidade (%)'
        }
      },
      colors: ['#10B981'],
      fill: {
        type: 'gradient',
        gradient: {
          shade: 'light',
          type: 'vertical',
          shadeIntensity: 0.25,
          gradientToColors: undefined,
          inverseColors: true,
          opacityFrom: 0.85,
          opacityTo: 0.85,
          stops: [50, 0, 100]
        }
      }
    };
  }

  atualizarGraficos(): void {
    const carteiras = this.carteiras();
    
    // Atualizar gráfico de distribuição por tipo
    const tiposCount: { [key: string]: number } = {};
    carteiras.forEach(c => {
      tiposCount[c.tipo] = (tiposCount[c.tipo] || 0) + 1;
    });

    this.chartOptions = {
      ...this.chartOptions,
      series: Object.values(tiposCount),
      labels: Object.keys(tiposCount).map(tipo => 
        this.tipoOptions.find(t => t.value === tipo)?.label || tipo
      )
    };

    // Atualizar gráfico de desempenho
    this.performanceChartOptions = {
      ...this.performanceChartOptions,
      series: [{
        name: 'Rentabilidade',
        data: carteiras.map(c => c.rentabilidadeAtual)
      }],
      xaxis: {
        categories: carteiras.map(c => c.nome)
      }
    };
  }

  gerarInsightIA(): void {
    const inv = this.investidor();
    if (!inv) return;

    this.loadingInsight.set(true);

    const request = {
      nome: inv.nome,
      perfil: inv.perfilInvestidor,
      rendaMensal: inv.rendaMensal,
      patrimonioAtual: inv.patrimonioAtual
    };

    this.aiService.analisarPerfil(request).subscribe({
      next: (response) => {
        if (response.sucesso && response.data?.analise) {
          this.aiInsight.set(response.data.analise);
        }
        this.loadingInsight.set(false);
      },
      error: (error) => {
        this.toastService.error('Erro ao gerar insight com IA');
        this.loadingInsight.set(false);
      }
    });
  }

  formatarInsight(texto: string): string {
    // Converter markdown básico para HTML
    let html = texto
      // Títulos com # (processar antes de listas)
      .replace(/^### (.+)$/gm, '<h3 class="text-lg font-bold text-gray-900 mt-4 mb-2">$1</h3>')
      .replace(/^## (.+)$/gm, '<h2 class="text-xl font-bold text-gray-900 mt-4 mb-2">$1</h2>')
      .replace(/^# (.+)$/gm, '<h1 class="text-2xl font-bold text-gray-900 mt-4 mb-3">$1</h1>')
      // Negrito com **texto** (processar antes de itálico)
      .replace(/\*\*([^*]+)\*\*/g, '<strong class="text-gray-900 font-semibold">$1</strong>')
      // Lista com * item (apenas no início da linha, após negrito)
      .replace(/^\* (.+)$/gm, '<li class="ml-4 mb-2 text-gray-700">$1</li>')
      // Itálico com *texto* (processar por último)
      .replace(/\*([^*<>]+)\*/g, '<em class="text-gray-700 italic">$1</em>')
      // Quebras de linha duplas para parágrafos
      .replace(/\n\n/g, '</p><p class="mb-4 text-gray-700 leading-relaxed">')
      // Quebras de linha simples
      .replace(/\n/g, '<br>');

    // Envolver listas em <ul>
    html = html.replace(/(<li[^>]*>.*?<\/li>(\s*<li[^>]*>.*?<\/li>)*)/gs, '<ul class="mb-4 space-y-2 list-none">$1</ul>');
    
    // Envolver todo o conteúdo em parágrafo se necessário
    if (!html.startsWith('<h') && !html.startsWith('<p') && !html.startsWith('<ul')) {
      html = '<p class="mb-4 text-gray-700 leading-relaxed">' + html + '</p>';
    }

    return html;
  }
}
