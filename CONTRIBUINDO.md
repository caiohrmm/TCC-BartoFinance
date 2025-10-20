# 🤝 Guia de Contribuição - BartoFinance

Obrigado por considerar contribuir para o BartoFinance! Este documento fornece diretrizes para contribuir com o projeto.

---

## 📋 Código de Conduta

Ao participar deste projeto, você concorda em manter um ambiente respeitoso e colaborativo.

---

## 🚀 Como Contribuir

### 1. Fork do Projeto

Faça um fork do repositório para sua conta GitHub.

```bash
git clone https://github.com/seu-usuario/bartofinance.git
cd bartofinance
```

### 2. Crie uma Branch

Crie uma branch para sua feature ou correção:

```bash
git checkout -b feature/nova-feature
# ou
git checkout -b fix/correcao-bug
```

### 3. Desenvolva

Faça suas modificações seguindo os padrões do projeto.

### 4. Commit

Use mensagens de commit descritivas seguindo o padrão:

```bash
git commit -m "feat(investidores): adiciona endpoint de busca por CPF"
git commit -m "fix(auth): corrige validação de token expirado"
git commit -m "docs(readme): atualiza instruções de instalação"
```

#### Padrões de Commit

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Mudanças na documentação
- `style`: Formatação, ponto e vírgula faltando, etc
- `refactor`: Refatoração de código
- `test`: Adição de testes
- `chore`: Atualizações de build, configs, etc

### 5. Push

```bash
git push origin feature/nova-feature
```

### 6. Pull Request

Abra um Pull Request explicando suas mudanças.

---

## 📝 Padrões de Código

### Java

- Use **Java 17** ou superior
- Siga as convenções de nomenclatura Java
- Use **Lombok** para reduzir boilerplate
- Documente classes e métodos públicos com **JavaDoc**
- Mantenha métodos pequenos e focados

### Estrutura de Pacotes

```
com.bartofinance/
├── config/           # Configurações
├── controller/       # Controllers REST
├── dto/              # Data Transfer Objects
├── exception/        # Exceções customizadas
├── model/            # Entidades do domínio
├── repository/       # Repositories
├── security/         # Configurações de segurança
└── service/          # Lógica de negócio
```

### Exemplo de Código

```java
/**
 * Serviço responsável por gerenciar investidores
 * 
 * @author Seu Nome
 * @version 1.0.0
 */
@Service
@Slf4j
public class InvestidorService {

    @Autowired
    private InvestidorRepository repository;

    /**
     * Busca um investidor por ID
     * 
     * @param id ID do investidor
     * @return Investidor encontrado
     * @throws ResourceNotFoundException se não encontrado
     */
    public Investidor buscarPorId(String id) {
        log.info("Buscando investidor com ID: {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Investidor", "id", id));
    }
}
```

---

## 🧪 Testes

- Escreva testes para novas funcionalidades
- Mantenha a cobertura de testes acima de 70%
- Use **JUnit 5** e **Mockito**

```java
@Test
void deveRetornarInvestidorQuandoIdExistir() {
    // Given
    String id = "123";
    Investidor investidor = new Investidor();
    when(repository.findById(id)).thenReturn(Optional.of(investidor));

    // When
    Investidor resultado = service.buscarPorId(id);

    // Then
    assertNotNull(resultado);
    verify(repository).findById(id);
}
```

---

## 📖 Documentação

- Atualize o **README.md** se necessário
- Adicione exemplos no **POSTMAN_EXAMPLES.md**
- Atualize o **CHANGELOG.md** com suas mudanças
- Documente endpoints com anotações **Swagger/OpenAPI**

---

## ✅ Checklist antes do PR

- [ ] Código compila sem erros
- [ ] Testes passam
- [ ] Código segue os padrões do projeto
- [ ] Documentação atualizada
- [ ] Commits seguem o padrão
- [ ] Branch atualizada com a main

---

## 🐛 Reportando Bugs

Ao reportar bugs, inclua:

1. **Descrição clara** do problema
2. **Passos para reproduzir**
3. **Comportamento esperado**
4. **Comportamento atual**
5. **Screenshots** (se aplicável)
6. **Versão** do Java, MongoDB, etc

---

## 💡 Sugerindo Melhorias

Sugestões são bem-vindas! Abra uma issue com:

1. **Descrição da melhoria**
2. **Motivação** (por que seria útil?)
3. **Exemplos de uso** (se aplicável)

---

## 📞 Contato

Dúvidas? Entre em contato:

- Email: dev@bartofinance.com
- GitHub Issues: [Issues](https://github.com/seu-usuario/bartofinance/issues)

---

<p align="center">
  Obrigado por contribuir! ❤️
</p>

