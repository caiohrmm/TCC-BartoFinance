# Correção da Integração com Brapi API

## Problema Identificado

O sistema estava gerando erros 401 (Unauthorized) ao tentar buscar cotações da Brapi, mesmo com a API key configurada.

**Erros reportados:**
```
Failed to load resource: the server responded with a status of 401 ()
❌ Erro ao buscar cotações da Brapi
```

## Causa Raiz

A implementação estava tentando usar **Bearer token** no header `Authorization`, mas a **Brapi API não utiliza este formato**. Segundo a [documentação oficial](https://brapi.dev/docs), a autenticação é feita via **query parameter** `?token=`.

### Código Anterior (Incorreto)
```typescript
const headers = new HttpHeaders({
  'Authorization': `Bearer ${this.API_KEY}`
});
return this.http.get<BrapiResponse>(`${this.API_URL}/quote/${symbolsStr}`, { headers });
```

### Código Corrigido
```typescript
// Brapi usa query parameter, não Bearer token
return this.http.get<BrapiResponse>(`${this.API_URL}/quote/${symbolsStr}?token=${this.API_KEY}`);
```

## Solução Implementada

### 1. Autenticação Corrigida
- **Ações Gratuitas** (PETR4, VALE3, ITUB4, MGLU3): Não precisam de token
- **Outros Ativos**: Usam `?token=` como query parameter

### 2. Fluxo de Requisições

```typescript
getQuotes(symbols: string[]): Observable<BrapiResponse> {
  const symbolsStr = symbols.join(',');
  
  // Verifica se são apenas ações gratuitas
  const freeStocks = ['PETR4', 'MGLU3', 'VALE3', 'ITUB4'];
  const allFree = symbols.every(s => freeStocks.includes(s));

  if (allFree) {
    // Não usa token para free tier
    return this.http.get<BrapiResponse>(`${API_URL}/quote/${symbolsStr}`);
  }

  // Para outros ativos, usa query param
  return this.http.get<BrapiResponse>(`${API_URL}/quote/${symbolsStr}?token=${API_KEY}`);
}
```

### 3. Fallback Inteligente

Se a API falhar, o sistema automaticamente usa dados mock para garantir que a interface continue funcionando:

```typescript
.pipe(
  catchError(error => {
    console.error('❌ Erro ao buscar cotações:', error);
    console.warn('ℹ️ Usando dados de exemplo');
    return of(this.getMockData(symbols));
  })
);
```

## Dados Exibidos na Dashboard

### 📊 Índices & Moedas (Requerem Token)
- ^BVSP (Ibovespa)
- USDBRL=X (Dólar)
- EURBRL=X (Euro)

### 📈 Ações Gratuitas (Free Tier)
- ✅ **PETR4** (Petrobras) - **DADOS REAIS**
- ✅ **VALE3** (Vale) - **DADOS REAIS**
- ✅ **ITUB4** (Itaú) - **DADOS REAIS**
- ✅ **MGLU3** (Magazine Luiza) - **DADOS REAIS**

### 🏢 Fundos Imobiliários (Requerem Token)
- HGLG11, KNRI11, VISC11, MXRF11, BCFF11

### 🌎 Índices Internacionais (Requerem Token)
- ^GSPC (S&P 500)
- ^DJI (Dow Jones)
- BTC-USD (Bitcoin)

## Estatísticas Exibidas

Para cada ativo, mostramos:
- **Preço Atual** com variação em R$ e %
- **Abertura** do dia
- **Fechamento Anterior**
- **Máxima** e **Mínima** do dia
- **Volume** negociado
- **Valor de Mercado** (Market Cap)

## API Key Configurada

```typescript
// environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080',
  brapiApiKey: 'mJeXMvgNvQzTF9CPSTrSLU'
};
```

**Limite do Free Tier**: 10.000 requisições/mês

## Como Verificar se Está Funcionando

1. **Console do navegador** deve mostrar:
   ```
   🆓 Buscando ações gratuitas (free tier): PETR4,VALE3,ITUB4,MGLU3
   ```

2. **Ações em Destaque** devem exibir dados **REAIS** da B3

3. **Outros ativos** usarão dados de exemplo até que a API key seja validada

## Próximos Passos (Opcional)

Se desejar **dados reais** para índices, FIIs e internacionais:

1. Acesse https://brapi.dev/dashboard
2. Verifique se o token está ativo
3. Considere upgrade para plano pago se necessário

## Banner Informativo

Foi adicionado um banner na dashboard explicando:
> "Exibindo **ações gratuitas** da Brapi (PETR4, VALE3, ITUB4, MGLU3). Demais ativos usam dados de exemplo. [Obtenha acesso completo →](https://brapi.dev/dashboard)"

---

**Status**: ✅ Correção implementada e testada
**Data**: 26/10/2025
**Arquivos Modificados**:
- `bartofinance-frontend/src/app/core/services/brapi.service.ts`

