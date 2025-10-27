import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ThemeToggleComponent } from '../../../shared/components/theme-toggle/theme-toggle.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, ThemeToggleComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = signal(false);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toastService: ToastService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading.set(true);

      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          console.log('Login component - success, navigating to dashboard');
          this.toastService.success('Login realizado com sucesso! 🎉');
          this.loading.set(false);
          
          // Pequeno delay para garantir que o signal está atualizado
          setTimeout(() => {
            this.router.navigate(['/dashboard']).then(success => {
              console.log('Navigation result:', success);
              if (!success) {
                console.error('Navigation failed! Trying again...');
                // Tenta novamente
                setTimeout(() => {
                  this.router.navigate(['/dashboard']);
                }, 100);
              }
            });
          }, 100);
        },
        error: (error) => {
          console.error('Login error:', error);
          this.loading.set(false);
          const errorMsg = error.error?.mensagem || 'Credenciais inválidas. Verifique seu email e senha.';
          this.toastService.error(errorMsg);
        }
      });
    } else {
      this.toastService.warning('Por favor, preencha todos os campos corretamente.');
    }
  }

  get email() {
    return this.loginForm.get('email');
  }

  get senha() {
    return this.loginForm.get('senha');
  }
}
