![Banner](./banner.jpg)

<p align="center">

![Static Badge](https://img.shields.io/badge/SpringBoot-3.4.3-lightgray?logoColor=blue&logoSize=blue&labelColor=orange&color=gray)
![Static Badge](https://img.shields.io/badge/Angular-19.2.1-lightgray?logoColor=blue&logoSize=blue&labelColor=darkred&color=gray)
![Static Badge](https://img.shields.io/badge/PostgreSQL-17.2-lightgray?logoColor=blue&logoSize=blue&labelColor=yellow&color=gray)

</p>
# PushItDown é uma aplicação para registro de entradas e saídas de expediente! ⏱️

# 📁 Estrutura de diretórios

* `backend`: contém um projeto em SpringBoot 3.4.3 responsável por cuidar da API e da sua proteção. Ele está configurado na porta 8080;

* `frontend`: diretório que contém o projeto do frontend em *Angular*;

* `sql`: diretório com as manipulações básicas da base de dados;

> **📢OBS**: alguns diretórios podem não ter sido implementados ou removidos.

# 🪄 Tecnologias do projeto

### 👤 Autenticação com Oauth2

O projeto do backend implementa o fluxo Oauth2, possuindo os endpoints responsáveis pelo fluxo de autenticação:

* `/oauth2/authorization`: retorna o código de autenticação para trocá-lo por um token;
* `oauth2/jwks`: dado o código de autenticação, retorna o token.

Com o token obtido, ao realizar requisições com `Bearer {Token}`, o usuário é permitido de acessar os endpoints protegidos.

Para implementar esse fluxo, é preciso adicionar os componentes de código que permitem a proteção e a autenticação por formulário. 

A função `authorizationServerSecurityFilterChain` é um filtro responsável por proteger os endpoints do *authorization server*. É importante notar que ele, naturalmente, não é responsável por validar tokens, mas por fornecê-los. Como o backend também possui a API protegida, é preciso que os tokens sejam validados e, para isso, há a implementação da função `oauth2ResourceServer`. Logo, toda requisição com um token válido será aceita. Também é importante notar a função `authorizeHttpRequests`, que, ao configurar `.anyRequest().authenticated()`, faz com que o acesso aos endpoints do *authorization server* seja protegido.

```
@Bean
@Order(1)
public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
        throws Exception {
    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
            OAuth2AuthorizationServerConfigurer.authorizationServer();

    http
        ⁝
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
> 💻 ***Código:** função filtro authorizationServerSecurityFilterChain resumida.*


Para proteger a API, a função filtro `defaultSecurityFilterChain` é aplicada, com implementação parecida com o filtro do *authorization server*. Aqui, a diferença está na implementação de `formLogin(Customizer.withDefaults())`, que apresenta um formulário padrão básico para que o usuário possa se autenticar com *usuário* e *senha*. O usuário é buscado na base de dados por meio de um `CustomUserDetailsService`.

```
@Bean
@Order(2)
public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
        throws Exception {
    http
        ⁝
        .authorizeHttpRequests((authorize) -> authorize
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(resourceServer -> resourceServer
            .jwt(Customizer.withDefaults())
        )
        .formLogin(Customizer.withDefaults());
    return http.build();
}
```
> 💻 ***Código:** função filtro defaultSecurityFilterChain resumida.*
---

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

---

### 🌐 Os endpoints

Os endpoints da API são:

- `/auth/cadastrar`: enviando um JSON do tipo {username, nome, senha}, o backend consegue cadastrar o usuário na base de dados. A resposta pode ser ***201 (CREATED)***, caso o usuário seja corretamente cadastrado, ou ***409 (CONFLICT)***, caso o usuário já esteja cadastrado;

- `/home/{username}/entrace`: para registrar uma **entrada** do usuário de username *{username}*;

- `/home/{username}/exit`: para registrar uma **saída** do usuário de username *{username}*;

- `/home/{username}/registros`: para recuperar todos os registros do usuário de username *{username}*;

- `/home/{username}/expedientes`: para recuperar todos os expedientes diários do usuário de username *{username}*;