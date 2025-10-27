import { Injectable, signal, computed } from '@angular/core';

export type Theme = 'light' | 'dark';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  // Signal para o tema atual
  private _theme = signal<Theme>(this.getInitialTheme());
  
  // Computed signal para tema dark
  isDark = computed(() => this._theme() === 'dark');

  constructor() {
    // Aplica o tema inicial
    this.applyTheme(this._theme());
  }

  /**
   * Obtém o tema inicial do localStorage ou preferência do sistema
   */
  private getInitialTheme(): Theme {
    const saved = localStorage.getItem('theme');
    if (saved === 'light' || saved === 'dark') {
      return saved;
    }
    
    // Detecta preferência do sistema
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    return prefersDark ? 'dark' : 'light';
  }

  /**
   * Alterna entre tema claro e escuro
   */
  toggleTheme(): void {
    const newTheme = this._theme() === 'light' ? 'dark' : 'light';
    this.setTheme(newTheme);
  }

  /**
   * Define o tema manualmente
   */
  setTheme(theme: Theme): void {
    this._theme.set(theme);
    localStorage.setItem('theme', theme);
    this.applyTheme(theme);
  }

  /**
   * Retorna o tema atual
   */
  getTheme(): Theme {
    return this._theme();
  }

  /**
   * Aplica o tema no DOM
   */
  private applyTheme(theme: Theme): void {
    const html = document.documentElement;
    
    if (theme === 'dark') {
      html.classList.add('dark');
    } else {
      html.classList.remove('dark');
    }
  }

  /**
   * Listener para mudanças na preferência do sistema
   */
  watchSystemPreference(): void {
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
      // Só segue preferência do sistema se não houver tema salvo
      if (!localStorage.getItem('theme')) {
        this.setTheme(e.matches ? 'dark' : 'light');
      }
    });
  }
}

