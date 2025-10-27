import { Component, computed, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastComponent } from './shared/components/toast/toast.component';
import { AiChatbotComponent } from './shared/components/ai-chatbot/ai-chatbot.component';
import { AuthService } from './core/services/auth.service';
import { ThemeService } from './core/services/theme.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastComponent, AiChatbotComponent, CommonModule],
  template: `
    <router-outlet></router-outlet>
    <app-toast></app-toast>
    @if (isAuthenticated()) {
      <app-ai-chatbot></app-ai-chatbot>
    }
  `
})
export class AppComponent implements OnInit {
  title = 'BartoFinance';
  
  // Usa computed para reagir às mudanças do AuthService
  isAuthenticated = computed(() => this.authService.isAuthenticated());

  constructor(
    private authService: AuthService,
    private themeService: ThemeService
  ) {}

  ngOnInit(): void {
    // Observa mudanças na preferência do sistema
    this.themeService.watchSystemPreference();
  }
}


