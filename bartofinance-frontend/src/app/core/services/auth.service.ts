import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequest, RegisterRequest, AuthResponse, UserToken } from '../models/auth.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'bartofinance_token';
  private readonly USER_KEY = 'bartofinance_user';
  
  // Signals para reatividade
  isAuthenticated = signal<boolean>(this.hasToken());
  currentUser = signal<UserToken | null>(this.getUserData());

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, credentials)
      .pipe(
        tap(response => {
          console.log('Login response:', response);
          if (response.sucesso && response.data?.token) {
            this.setToken(response.data.token);
            this.setUserData(response.data);
            this.isAuthenticated.set(true);
            this.currentUser.set(response.data);
            console.log('Token saved:', response.data.token);
            console.log('isAuthenticated:', this.isAuthenticated());
          }
        })
      );
  }

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, data)
      .pipe(
        tap(response => {
          console.log('Register response:', response);
          if (response.sucesso && response.data?.token) {
            this.setToken(response.data.token);
            this.setUserData(response.data);
            this.isAuthenticated.set(true);
            this.currentUser.set(response.data);
            console.log('Token saved:', response.data.token);
            console.log('isAuthenticated:', this.isAuthenticated());
          }
        })
      );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.isAuthenticated.set(false);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  private setUserData(data: UserToken): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(data));
  }

  private getUserData(): UserToken | null {
    const data = localStorage.getItem(this.USER_KEY);
    return data ? JSON.parse(data) : null;
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }
}


