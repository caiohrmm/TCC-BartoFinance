import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerForm: FormGroup;
  loading = signal(false);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toastService: ToastService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.loading.set(true);

      this.authService.register(this.registerForm.value).subscribe({
        next: (response) => {
          console.log('Register component - success, navigating to dashboard');
          this.toastService.success('Conta criada com sucesso! Bem-vindo ao BartoFinance! 🎉');
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
          console.error('Register error:', error);
          this.loading.set(false);
          const errorMsg = error.error?.mensagem || 'Erro ao criar conta. Tente novamente.';
          this.toastService.error(errorMsg);
        }
      });
    } else {
      this.toastService.warning('Por favor, preencha todos os campos corretamente.');
    }
  }

  get nome() {
    return this.registerForm.get('nome');
  }

  get email() {
    return this.registerForm.get('email');
  }

  get senha() {
    return this.registerForm.get('senha');
  }
}
