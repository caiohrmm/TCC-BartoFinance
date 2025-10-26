import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { InvestidorService } from '../../core/services/investidor.service';
import { PortfolioService } from '../../core/services/portfolio.service';
import { AplicacaoService } from '../../core/services/aplicacao.service';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { ConfirmModalComponent } from '../../shared/components/confirm-modal/confirm-modal.component';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, FooterComponent, ConfirmModalComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  userName = signal('');
  showLogoutModal = signal(false);
  loading = signal(true);
  stats = signal({
    investidores: 0,
    carteiras: 0,
    aplicacoes: 0,
    valorTotal: 0
  });

  constructor(
    private authService: AuthService,
    private investidorService: InvestidorService,
    private portfolioService: PortfolioService,
    private aplicacaoService: AplicacaoService
  ) {}

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.userName.set(user.nome);
    }
    this.carregarDashboard();
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
}


