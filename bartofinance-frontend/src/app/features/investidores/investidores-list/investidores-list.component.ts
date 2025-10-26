import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { ConfirmModalComponent } from '../../../shared/components/confirm-modal/confirm-modal.component';
import { CpfMaskDirective } from '../../../shared/directives/cpf-mask.directive';
import { TelefoneMaskDirective } from '../../../shared/directives/telefone-mask.directive';
import { CurrencyMaskDirective } from '../../../shared/directives/currency-mask.directive';
import { InvestidorService } from '../../../core/services/investidor.service';
import { ToastService } from '../../../core/services/toast.service';
import { InvestidorResponse, InvestidorRequest, PerfilInvestidorOptions, PerfilInvestidor } from '../../../core/models/investidor.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-investidores-list',
  standalone: true,
  imports: [
    CommonModule, 
    RouterLink, 
    FooterComponent, 
    ReactiveFormsModule,
    ConfirmModalComponent,
    CpfMaskDirective,
    TelefoneMaskDirective,
    CurrencyMaskDirective
  ],
  templateUrl: './investidores-list.component.html',
  styleUrl: './investidores-list.component.scss'
})
export class InvestidoresListComponent implements OnInit {
  investidores = signal<InvestidorResponse[]>([]); // Lista filtrada (exibida)
  todosInvestidores = signal<InvestidorResponse[]>([]); // Lista completa (para contagens)
  loading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);
  currentInvestidorId = signal<string | null>(null);
  investidorForm: FormGroup;
  filtroPerfil = signal<PerfilInvestidor | ''>('');
  perfilOptions = PerfilInvestidorOptions;
  userName = signal('');
  
  // Modais de confirmação
  showDeleteModal = signal(false);
  showLogoutModal = signal(false);
  investidorToDelete = signal<InvestidorResponse | null>(null);

  constructor(
    private investidorService: InvestidorService,
    private toastService: ToastService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.investidorForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      cpf: ['', [Validators.required, Validators.pattern(/^\d{11}$/)]],
      email: ['', [Validators.email]],
      telefone: ['', [Validators.pattern(/^\d{10,11}$/)]],
      perfilInvestidor: ['', [Validators.required]],
      patrimonioAtual: [0, [Validators.required, Validators.min(0)]],
      rendaMensal: [0, [Validators.required, Validators.min(0)]],
      objetivos: ['', [Validators.maxLength(500)]]
    });
  }

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.userName.set(user.nome);
    }
    this.carregarInvestidores();
  }

  carregarInvestidores(): void {
    this.loading.set(true);
    
    // Sempre carrega todos os investidores (sem filtro)
    this.investidorService.listarInvestidores().subscribe({
      next: (response) => {
        if (response.sucesso && response.data) {
          this.todosInvestidores.set(response.data);
          
          // Aplica o filtro localmente
          const perfil = this.filtroPerfil();
          if (perfil) {
            this.investidores.set(response.data.filter(inv => inv.perfilInvestidor === perfil));
          } else {
            this.investidores.set(response.data);
          }
        } else {
          this.todosInvestidores.set([]);
          this.investidores.set([]);
        }
        
        this.loading.set(false);
      },
      error: (error) => {
        this.loading.set(false);
        this.todosInvestidores.set([]);
        this.investidores.set([]);
        console.error('Erro ao carregar investidores:', error);
        this.toastService.error(error.error?.mensagem || 'Erro ao carregar investidores. Tente atualizar a página.');
      }
    });
  }

  aplicarFiltro(perfil: PerfilInvestidor | ''): void {
    this.filtroPerfil.set(perfil);
    
    // Filtra localmente sem fazer nova requisição
    const todos = this.todosInvestidores();
    if (perfil) {
      this.investidores.set(todos.filter(inv => inv.perfilInvestidor === perfil));
    } else {
      this.investidores.set(todos);
    }
  }

  abrirModal(investidor?: InvestidorResponse): void {
    if (investidor) {
      this.isEditing.set(true);
      this.currentInvestidorId.set(investidor.id);
      this.investidorForm.patchValue(investidor);
    } else {
      this.isEditing.set(false);
      this.currentInvestidorId.set(null);
      this.investidorForm.reset({
        patrimonioAtual: 0,
        rendaMensal: 0
      });
    }
    this.showModal.set(true);
  }

  fecharModal(): void {
    this.showModal.set(false);
    this.investidorForm.reset();
    this.isEditing.set(false);
    this.currentInvestidorId.set(null);
  }

  salvarInvestidor(): void {
    if (this.investidorForm.invalid) {
      this.toastService.warning('Por favor, preencha todos os campos obrigatórios corretamente');
      return;
    }

    this.loading.set(true);
    const request: InvestidorRequest = this.investidorForm.value;

    const operacao = this.isEditing()
      ? this.investidorService.atualizarInvestidor(this.currentInvestidorId()!, request)
      : this.investidorService.criarInvestidor(request);

    operacao.subscribe({
      next: (response) => {
        this.loading.set(false);
        this.toastService.success(
          this.isEditing() ? 'Investidor atualizado com sucesso!' : 'Investidor criado com sucesso!'
        );
        this.fecharModal();
        this.carregarInvestidores();
      },
      error: (error) => {
        this.loading.set(false);
        this.toastService.error(error.error?.mensagem || 'Erro ao salvar investidor');
      }
    });
  }

  abrirModalExclusao(investidor: InvestidorResponse): void {
    this.investidorToDelete.set(investidor);
    this.showDeleteModal.set(true);
  }

  confirmarExclusao(): void {
    const investidor = this.investidorToDelete();
    if (!investidor) return;

    this.loading.set(true);
    this.showDeleteModal.set(false);
    
    this.investidorService.deletarInvestidor(investidor.id).subscribe({
      next: () => {
        this.loading.set(false);
        this.toastService.success('Investidor excluído com sucesso!');
        this.investidorToDelete.set(null);
        this.carregarInvestidores();
      },
      error: (error) => {
        this.loading.set(false);
        this.toastService.error(error.error?.mensagem || 'Erro ao excluir investidor');
      }
    });
  }

  cancelarExclusao(): void {
    this.showDeleteModal.set(false);
    this.investidorToDelete.set(null);
  }

  getPerfilInfo(perfil: PerfilInvestidor) {
    return this.perfilOptions.find(p => p.value === perfil);
  }

  formatarCPF(cpf: string): string {
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

  contarPorPerfil(perfil: PerfilInvestidor): number {
    // Conta sobre TODOS os investidores, não apenas os filtrados
    return this.todosInvestidores().filter(i => i.perfilInvestidor === perfil).length;
  }
}
