import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { ConfirmModalComponent } from '../../shared/components/confirm-modal/confirm-modal.component';

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
  stats = signal({
    investidores: 0,
    carteiras: 0,
    aplicacoes: 0,
    valorTotal: 0
  });

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.userName.set(user.nome);
    }
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


