export interface LoginRequest {
  email: string;
  senha: string;
}

export interface RegisterRequest {
  nome: string;
  email: string;
  senha: string;
}

export interface UserToken {
  token: string;
  tipo: string;
  assessorId: string;
  nome: string;
  email: string;
  mensagem?: string;
}

export interface ApiResponse<T> {
  sucesso: boolean;
  mensagem: string;
  data: T;
  timestamp?: string;
}

export type AuthResponse = ApiResponse<UserToken>;


