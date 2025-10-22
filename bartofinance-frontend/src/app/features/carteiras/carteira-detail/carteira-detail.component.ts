import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { ConfirmModalComponent } from '../../../shared/components/confirm-modal/confirm-modal.component';
import { CurrencyMaskDirective } from '../../../shared/directives/currency-mask.directive';
import { PortfolioService } from '../../../core/services/portfolio.service';
import { AplicacaoService } from '../../../core/services/aplicacao.service';
import { InvestidorService } from '../../../core/services/investidor.service';
import { ToastService } from '../../../core/services/toast.service';
import { AuthService } from '../../../core/services/auth.service';
import { PortfolioResponse, RiscoCarteiraOptions, TipoCarteiraOptions } from '../../../core/models/portfolio.model';
import { AplicacaoResponse, AplicacaoRequest, TipoProdutoOptions, StatusAplicacaoOptions, TipoProduto } from '../../../core/models/aplicacao.model';
import { InvestidorResponse } from '../../../core/models/investidor.model';

@Component({
  selector: 'app-carteira-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    FooterComponent,
    ReactiveFormsModule,
    ConfirmModalComponent,
    CurrencyMaskDirective
  ],
  templateUrl: './carteira-detail.component.html',
  styleUrl: './carteira-detail.component.scss'
})
export class CarteiraDetailComponent implements OnInit {
  carteira = signal<PortfolioResponse | null>(null);
  aplicacoes = signal<AplicacaoResponse[]>([]);
  investidor = signal<InvestidorResponse | null>(null);
  loading = signal(false);
  showAplicacaoModal = signal(false);
  showEncerrarModal = signal(false);
  showDeleteModal = signal(false);
  showLogoutModal = signal(false);
  isEditingAplicacao = signal(false);
  currentAplicacaoId = signal<string | null>(null);
  aplicacaoToDelete = signal<AplicacaoResponse | null>(null);
  aplicacaoToEncerrar = signal<AplicacaoResponse | null>(null);
  
  aplicacaoForm: FormGroup;
  encerrarForm: FormGroup;
  userName = signal('');
  carteiraId: string = '';
  
  tipoProdutoOptions = TipoProdutoOptions;
  riscoOptions = RiscoCarteiraOptions;
  tipoOptions = TipoCarteiraOptions;
  statusOptions = StatusAplicacaoOptions;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private portfolioService: PortfolioService,
    private aplicacaoService: AplicacaoService,
    private investidorService: InvestidorService,
    private toastService: ToastService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.aplicacaoForm = this.fb.group({
      tipoProduto: ['', [Validators.required]],
      codigoAtivo: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
      valorAplicado: [0, [Validators.required, Validators.min(0.01)]],
      quantidade: [0, [Validators.required, Validators.min(0.01)]],
      dataCompra: ['', [Validators.required]],
      rentabilidadeAtual: [0, [Validators.min(-100), Validators.max(1000)]],
      notas: ['', [Validators.maxLength(500)]]
    });

    this.encerrarForm = this.fb.group({
      dataVenda: ['', [Validators.required]],
      rentabilidadeFinal: [0, [Validators.required]]
    });
  }

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.userName.set(user.nome);
    }

    this.carteiraId = this.route.snapshot.paramMap.get('id')!;
    if (this.carteiraId) {
      this.carregarCarteira();
      this.carregarAplicacoes();
    }
  }

  carregarCarteira(): void {
    this.loading.set(true);
    this.portfolioService.buscarPorId(this.carteiraId).subscribe({
      next: (response) => {
        this.carteira.set(response.data);
        
        // Carregar investidor se houver
        if (response.data.investidorId) {
          this.carregarInvestidor(response.data.investidorId);
        }
        
        this.loading.set(false);
      },
      error: (error) => {
        this.loading.set(false);
        this.toastService.error(error.error?.mensagem || 'Erro ao carregar carteira');
        this.router.navigate(['/carteiras']);
      }
    });
  }

  carregarInvestidor(investidorId: string): void {
    this.investidorService.buscarPorId(investidorId).subscribe({
      next: (response: any) => {
        this.investidor.set(response.data);
      },
      error: () => {
        // Silencioso
      }
    });
  }

  carregarAplicacoes(): void {
    this.aplicacaoService.listarAplicacoes(this.carteiraId).subscribe({
      next: (response) => {
        this.aplicacoes.set(response.data);
      },
      error: (error) => {
        this.toastService.error(error.error?.mensagem || 'Erro ao carregar aplicações');
      }
    });
  }

  abrirModalAplicacao(aplicacao?: AplicacaoResponse): void {
    if (aplicacao) {
      this.isEditingAplicacao.set(true);
      this.currentAplicacaoId.set(aplicacao.id);
      this.aplicacaoForm.patchValue({
        ...aplicacao,
        dataCompra: this.formatDateForInput(aplicacao.dataCompra)
      });
    } else {
      this.isEditingAplicacao.set(false);
      this.currentAplicacaoId.set(null);
      this.aplicacaoForm.reset({
        valorAplicado: 0,
        quantidade: 0,
        rentabilidadeAtual: 0,
        dataCompra: this.formatDateForInput(new Date().toISOString())
      });
    }
    this.showAplicacaoModal.set(true);
  }

  fecharModalAplicacao(): void {
    this.showAplicacaoModal.set(false);
    this.aplicacaoForm.reset();
  }

  salvarAplicacao(): void {
    if (this.aplicacaoForm.invalid) {
      this.toastService.warning('Preencha todos os campos obrigatórios corretamente');
      this.aplicacaoForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    const formValue = this.aplicacaoForm.value;
    const request: AplicacaoRequest = {
      portfolioId: this.carteiraId,
      ...formValue
    };

    const operacao = this.isEditingAplicacao()
      ? this.aplicacaoService.atualizarAplicacao(this.currentAplicacaoId()!, request)
      : this.aplicacaoService.criarAplicacao(request);

    operacao.subscribe({
      next: () => {
        this.loading.set(false);
        this.toastService.success(
          this.isEditingAplicacao() ? 'Aplicação atualizada!' : 'Aplicação criada!'
        );
        this.fecharModalAplicacao();
        this.carregarAplicacoes();
        this.carregarCarteira(); // Atualiza totais
      },
      error: (error) => {
        this.loading.set(false);
        this.toastService.error(error.error?.mensagem || 'Erro ao salvar aplicação');
      }
    });
  }

  abrirModalEncerrar(aplicacao: AplicacaoResponse): void {
    this.aplicacaoToEncerrar.set(aplicacao);
    this.encerrarForm.patchValue({
      dataVenda: this.formatDateForInput(new Date().toISOString()),
      rentabilidadeFinal: aplicacao.rentabilidadeAtual || 0
    });
    this.showEncerrarModal.set(true);
  }

  confirmarEncerrar(): void {
    if (this.encerrarForm.invalid) {
      this.toastService.warning('Preencha todos os campos');
      return;
    }

    const aplicacao = this.aplicacaoToEncerrar();
    if (!aplicacao) return;

    this.loading.set(true);
    this.showEncerrarModal.set(false);

    const { dataVenda, rentabilidadeFinal } = this.encerrarForm.value;

    this.aplicacaoService.encerrarAplicacao(aplicacao.id, dataVenda, rentabilidadeFinal).subscribe({
      next: () => {
        this.loading.set(false);
        this.toastService.success('Aplicação encerrada com sucesso!');
        this.aplicacaoToEncerrar.set(null);
        this.carregarAplicacoes();
        this.carregarCarteira();
      },
      error: (error) => {
        this.loading.set(false);
        this.toastService.error(error.error?.mensagem || 'Erro ao encerrar aplicação');
      }
    });
  }

  cancelarEncerrar(): void {
    this.showEncerrarModal.set(false);
    this.aplicacaoToEncerrar.set(null);
  }

  abrirModalExclusao(aplicacao: AplicacaoResponse): void {
    this.aplicacaoToDelete.set(aplicacao);
    this.showDeleteModal.set(true);
  }

  confirmarExclusao(): void {
    const aplicacao = this.aplicacaoToDelete();
    if (!aplicacao) return;

    this.loading.set(true);
    this.showDeleteModal.set(false);

    this.aplicacaoService.deletarAplicacao(aplicacao.id).subscribe({
      next: () => {
        this.loading.set(false);
        this.toastService.success('Aplicação excluída!');
        this.aplicacaoToDelete.set(null);
        this.carregarAplicacoes();
        this.carregarCarteira();
      },
      error: (error) => {
        this.loading.set(false);
        this.toastService.error(error.error?.mensagem || 'Erro ao excluir aplicação');
      }
    });
  }

  cancelarExclusao(): void {
    this.showDeleteModal.set(false);
    this.aplicacaoToDelete.set(null);
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

  getTipoProdutoInfo(tipo: TipoProduto) {
    return this.tipoProdutoOptions.find(t => t.value === tipo);
  }

  getStatusInfo(status: string) {
    return this.statusOptions.find(s => s.value === status);
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

  formatDateForInput(isoDate: string): string {
    return isoDate.split('T')[0];
  }

  calcularLucro(aplicacao: AplicacaoResponse): number {
    return aplicacao.valorAplicado * (aplicacao.rentabilidadeAtual / 100);
  }

  calcularValorAtual(aplicacao: AplicacaoResponse): number {
    return aplicacao.valorAplicado + this.calcularLucro(aplicacao);
  }

  get totalAplicado(): number {
    return this.aplicacoes().reduce((sum, a) => sum + a.valorAplicado, 0);
  }

  get totalAtual(): number {
    return this.aplicacoes().reduce((sum, a) => sum + this.calcularValorAtual(a), 0);
  }

  get rentabilidadeGeral(): number {
    if (this.totalAplicado === 0) return 0;
    return ((this.totalAtual - this.totalAplicado) / this.totalAplicado) * 100;
  }

  get aplicacoesAtivas(): number {
    return this.aplicacoes().filter(a => a.status === 'ATIVA').length;
  }

  get aplicacoesEncerradas(): number {
    return this.aplicacoes().filter(a => a.status === 'ENCERRADA').length;
  }
}

