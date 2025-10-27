import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { ConfirmModalComponent } from '../../../shared/components/confirm-modal/confirm-modal.component';
import { CurrencyMaskDirective } from '../../../shared/directives/currency-mask.directive';
import { PortfolioService } from '../../../core/services/portfolio.service';
import { InvestidorService } from '../../../core/services/investidor.service';
import { ToastService } from '../../../core/services/toast.service';
import { PortfolioResponse, PortfolioRequest, TipoCarteiraOptions, RiscoCarteiraOptions, TipoCarteira, RiscoCarteira } from '../../../core/models/portfolio.model';
import { InvestidorResponse } from '../../../core/models/investidor.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-carteiras-list',
  standalone: true,
  imports: [
    CommonModule, 
    RouterLink, 
    FooterComponent, 
    ReactiveFormsModule,
    ConfirmModalComponent,
    CurrencyMaskDirective
  ],
  templateUrl: './carteiras-list.component.html',
  styleUrl: './carteiras-list.component.scss'
})
export class CarteirasListComponent implements OnInit {
  carteiras = signal<PortfolioResponse[]>([]);
  todasCarteiras = signal<PortfolioResponse[]>([]);
  investidores = signal<InvestidorResponse[]>([]);
  loading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);
  currentCarteiraId = signal<string | null>(null);
  carteiraForm: FormGroup;
  filtroRisco = signal<RiscoCarteira | ''>('');
  filtroTipo = signal<TipoCarteira | ''>('');
  tipoOptions = TipoCarteiraOptions;
  riscoOptions = RiscoCarteiraOptions;
  userName = signal('');
  
  // Modais de confirmação
  showDeleteModal = signal(false);
  showLogoutModal = signal(false);
  carteiraToDelete = signal<PortfolioResponse | null>(null);

  constructor(
    private portfolioService: PortfolioService,
    private investidorService: InvestidorService,
    private toastService: ToastService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.carteiraForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      descricao: ['', [Validators.maxLength(500)]],
      tipo: ['', [Validators.required]],
      risco: ['', [Validators.required]],
      metaRentabilidade: [0, [Validators.required, Validators.min(0), Validators.max(100)]],
      investidorId: ['']
    });
  }

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.userName.set(user.nome);
    }
    this.carregarCarteiras();
    this.carregarInvestidores();
    
    // Validação dinâmica: PERSONALIZADA exige investidor, MODELO não
    this.carteiraForm.get('tipo')?.valueChanges.subscribe(tipo => {
      const investidorControl = this.carteiraForm.get('investidorId');
      if (tipo === 'PERSONALIZADA') {
        investidorControl?.setValidators([Validators.required]);
      } else {
        // MODELO: limpa investidor e remove validação
        investidorControl?.setValue('');
        investidorControl?.clearValidators();
      }
      investidorControl?.updateValueAndValidity();
    });
  }

  carregarCarteiras(): void {
    this.loading.set(true);
    
    this.portfolioService.listarPortfolios().subscribe({
      next: (response) => {
        if (response.sucesso && response.data) {
          this.todasCarteiras.set(response.data);
          this.aplicarFiltros();
        } else {
          this.todasCarteiras.set([]);
          this.carteiras.set([]);
        }
        this.loading.set(false);
      },
      error: (error) => {
        this.loading.set(false);
        this.todasCarteiras.set([]);
        this.carteiras.set([]);
        console.error('Erro ao carregar carteiras:', error);
        this.toastService.error(error.error?.mensagem || 'Erro ao carregar carteiras. Tente atualizar a página.');
      }
    });
  }

  carregarInvestidores(): void {
    this.investidorService.listarInvestidores().subscribe({
      next: (response) => {
        if (response.sucesso && response.data) {
          this.investidores.set(response.data);
        }
      },
      error: (error) => {
        console.error('Erro ao carregar investidores:', error);
        this.investidores.set([]);
        // Não mostra toast - não é crítico para a listagem de carteiras
      }
    });
  }

  aplicarFiltros(): void {
    let carteiras = this.todasCarteiras();
    
    const risco = this.filtroRisco();
    const tipo = this.filtroTipo();
    
    if (risco) {
      carteiras = carteiras.filter(c => c.risco === risco);
    }
    
    if (tipo) {
      carteiras = carteiras.filter(c => c.tipo === tipo);
    }
    
    this.carteiras.set(carteiras);
  }

  aplicarFiltroRisco(risco: RiscoCarteira | ''): void {
    this.filtroRisco.set(risco);
    this.aplicarFiltros();
  }

  aplicarFiltroTipo(tipo: TipoCarteira | ''): void {
    this.filtroTipo.set(tipo);
    this.aplicarFiltros();
  }

  limparFiltros(): void {
    this.filtroRisco.set('');
    this.filtroTipo.set('');
    this.aplicarFiltros();
  }

  abrirModal(carteira?: PortfolioResponse): void {
    if (carteira) {
      this.isEditing.set(true);
      this.currentCarteiraId.set(carteira.id);
      this.carteiraForm.patchValue(carteira);
    } else {
      this.isEditing.set(false);
      this.currentCarteiraId.set(null);
      this.carteiraForm.reset({
        metaRentabilidade: 0
      });
    }
    this.showModal.set(true);
  }

  fecharModal(): void {
    this.showModal.set(false);
    this.carteiraForm.reset();
    this.isEditing.set(false);
    this.currentCarteiraId.set(null);
  }

  salvarCarteira(): void {
    if (this.carteiraForm.invalid) {
      this.toastService.warning('Por favor, preencha todos os campos obrigatórios corretamente');
      return;
    }

    this.loading.set(true);
    const formValue = this.carteiraForm.value;
    
    // Remove investidorId se estiver vazio (carteira MODELO)
    const request: PortfolioRequest = {
      ...formValue,
      investidorId: formValue.investidorId || undefined
    };

    const operacao = this.isEditing()
      ? this.portfolioService.atualizarPortfolio(this.currentCarteiraId()!, request)
      : this.portfolioService.criarPortfolio(request);

    operacao.subscribe({
      next: (response) => {
        this.loading.set(false);
        this.toastService.success(
          this.isEditing() ? 'Carteira atualizada com sucesso!' : 'Carteira criada com sucesso!'
        );
        this.fecharModal();
        this.carregarCarteiras();
      },
      error: (error) => {
        this.loading.set(false);
        this.toastService.error(error.error?.mensagem || 'Erro ao salvar carteira');
      }
    });
  }
  abrirModalExclusao(carteira: PortfolioResponse): void {
    this.carteiraToDelete.set(carteira);
    this.showDeleteModal.set(true);
  }

  confirmarExclusao(): void {
    const carteira = this.carteiraToDelete();
    if (!carteira) return;

    this.loading.set(true);
    this.showDeleteModal.set(false);
    
    this.portfolioService.deletarPortfolio(carteira.id).subscribe({
      next: () => {
        this.loading.set(false);
        this.toastService.success('Carteira excluída com sucesso!');
        this.carteiraToDelete.set(null);
        this.carregarCarteiras();
      },
      error: (error) => {
        this.loading.set(false);
        this.toastService.error(error.error?.mensagem || 'Erro ao excluir carteira');
      }
    });
  }

  cancelarExclusao(): void {
    this.showDeleteModal.set(false);
    this.carteiraToDelete.set(null);
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

  getRiscoInfo(risco: RiscoCarteira) {
    return this.riscoOptions.find(r => r.value === risco);
  }

  getTipoInfo(tipo: TipoCarteira) {
    return this.tipoOptions.find(t => t.value === tipo);
  }

  formatarValor(valor: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
  }

  formatarPercentual(valor: number): string {
    return `${valor.toFixed(2)}%`;
  }

  contarPorRisco(risco: RiscoCarteira): number {
    return this.todasCarteiras().filter(c => c.risco === risco).length;
  }

  contarPorTipo(tipo: TipoCarteira): number {
    return this.todasCarteiras().filter(c => c.tipo === tipo).length;
  }

  getInvestidorNome(investidorId?: string): string {
    if (!investidorId) return '-';
    const investidor = this.investidores().find(i => i.id === investidorId);
    return investidor?.nome || '-';
  }
}
