![Static Badge](https://img.shields.io/badge/SpringBoot-3.4.3-lightgray?logoColor=blue&logoSize=blue&labelColor=orange&color=gray)
![Static Badge](https://img.shields.io/badge/Angular-19.2.1-lightgray?logoColor=blue&logoSize=blue&labelColor=darkred&color=gray)
![Static Badge](https://img.shields.io/badge/PostgreSQL-17.2-lightgray?logoColor=blue&logoSize=blue&labelColor=yellow&color=gray)

# PushItDown é uma aplicação para registro de entradas e saídas de expediente! ⏱️

# 📁 Estrutura de diretórios

* `authenticationserver`: diretório que contém o servidor de autenticação, que é executado em `auth-server.local:9000`;

* `pushitdown`: aplicação que cria os endpoints, opera na base de dados e contém o frontend básico;

* `frontend`: diretório que contém o projeto do frontend em *Angular*;

* `sql`: diretório com as manipulações básicas da base de dados;

> **📢OBS**: alguns diretórios podem não ter sido implementados ou removidos.

# 🪄 Tecnologias do projeto

### 👤 Autenticação com Oauth2

A autenticação funciona com dois projetos: o `authenticationserver` e o `pushitdown`. O primeiro é responsável por criar dois endpoints importantes na porta `9000`:

* `/oauth2/authorize/` -> endpoint para obter o código de autorização. Nesse estágio do fluxo, o cliente envia o seu *id* para obter um código que poderá ser trocado por um token.
* `/oauth2/token` -> com o código de autorização, um cliente pode enviar seu *id*, *token* e outras informações para obter um token.

É importante notar que esses endpoints do servidor são protegidos e podem ser acessados por um formulário de login básico do Spring Security. No entanto, para que a própria aplicação possa validar e aceitar esses tokens ao receber requisições protegidas, ela precisa atuar também como um Resource Server.


```
@Bean
@Order(1)
public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    http
    .authorizeHttpRequests((authorize) -> authorize
        .anyRequest().authenticated()
    )
    .oauth2ResourceServer(resourceServer -> resourceServer
        .jwt(Customizer.withDefaults())
    )
    ⁝
    return http.build();
}
```
> 💻 ***Código**: endpoints do servidor protegidos com `authenticated()` e com suporte para validação de tokens com `oauth2ResourceServer`.*

```
@Bean
@Order(2)
public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
        throws Exception {
    http
    .authorizeHttpRequests((authorize) -> authorize
        .anyRequest().authenticated()
    )
    .formLogin(Customizer.withDefaults());
    return http.build();
}
```
> 💻 ***Código**: formulário padrão ativado com `formLogin(Customizer.withDefautls())`.*

No lado da aplicação API, esse processo de acessar os dois endpoints para obter o token e permitir o acesso foi feito automaticamente, através da configuração:

```
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http
    .oauth2Login(oauth2Login -> oauth2Login.defaultSuccessUrl("/redirect", true))
    .oauth2Client(Customizer.withDefaults());
    ⁝
    return http.build();
}
```
> 💻 ***Código**: configuração do a aplicação para ser autenticado automaticamente.*

onde `oauth2Login` permite que o usuário utilize um provedor (authentication server) para se conectar, estabelecendo a comunicação entre os endpoints para gerar o token. O `oauth2Client` habilita o suporte para OAuth2 Client, que permite que a aplicação atue como um cliente OAuth2 e obtenha tokens de acesso para chamar APIs protegidas.

Mas para que a API siga corretamente o fluxo de autenticação, é preciso conhecer as informações do cliente, as suas permissões e configurar o redirecionamento. Isso pode ser feito através do arquivo `resources/application.yml`, que configura:

```
spring:
  security:
    oauth2:
      client:
        registration:
          pushitdown:
            provider: authserver-provider
            client-id: pushitdown
            client-secret: pushitdown
            authorization-grant-type: authorization_code
            redirect-uri: "http://client.local:8080/login/oauth2/code/pushitdown"
            scope: openid, profile
        provider:
          authserver-provider:
            issuer-uri: http://auth-server.local:9000
            authorization-uri: http://auth-server.local:9000/oauth2/authorize
            token-uri: http://auth-server.local:9000/oauth2/token
            user-name-attribute: sub
```
> 💻 ***Código**: configuração do das informações de autenticação.*

### 🎲 Busca por usuários na base de dados para autenticação

Para que um usuário possa ser buscado na base de dados para autenticação, é preciso configurar um `UserDetailsService`, uma interface do Spring Security usada para carregar detalhes do usuário durante o processo de autenticação. Ele é responsável por buscar os dados do usuário (como nome, senha e permissões) a partir de uma fonte de dados, como um banco de dados, um serviço externo ou até mesmo uma lista em memória.

Dessa forma, é necessário criar o serviço `CustomUserDetailsService`:


```
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException("Usuário não encontrado")
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getSenha(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
```
> 💻 ***Código**: criação do serviço customizado que implementa UserDetailsService.*

Para que o serviço customizado seja usado, é necessário adicionar nas configurações:

```
@Bean
public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
}
```
> 💻 ***Código**: configuração do UserDetailService customizado.*

Por fim, para que tudo funcione corretamente, é preciso configurar um `AuthenticatorManager`. Ele recebe as credenciais do usuário (como nome de usuário e senha), verifica se são válidas e retorna um objeto de autenticação se a validação for bem-sucedida.

```
@Bean
public AuthenticationManager authenticationManager(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) throws Exception {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(authProvider);
}
```
> 💻 ***Código**: configuração do AuthenticationManager.*

### 🌐 Criação dos endpoints