## Sobre você
**Nome**: Daniel Cardoso Assumpção <br>
**Sede(s) que gostaria de trabalhar (BH, Curitiba, Recife, SP)**: São Paulo<br>
**Gostaria de receber feedback sobre seu desafio? (sim/não)**: Sim, por favor.
## Alguma consideração sobre o seu projeto?

Para o desenvolvimento da minha solução utilizei a arquitetura MVP.
Utilizando a biblioteca Retrofit para fazer a comunicação da API do GitHub, modelei parcialmente a resposta do serviço, passeando apenas o que iria ser utilizado na minha solução. Utilizei a biblioteca Glide para processamento de imagens e GSON para a serialização de objetos JSON. Foram desenvolvidos também alguns testes e o cache para imagens.

Devido a simplicidade do dataset dessa solução, optei por uma persistência local utilizando SharedPreferences para salvar as listas de repositórios e as listas de pull request de cada repositório. Implementei também a feature “Pull-to-refresh” para atualizar a lista de repositórios e de pull requests. 

Devido a limitações da API o número máximo de repositórios exibidos na lista da página inicial é de 1000.  Outra limitação da API é o número de chamadas que podem ser feitas por um espaço de tempo. Para contornar essa situação, deve ser gerar um “personal access token” seguindo os passos deste [link](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token).

Após gerado, adicionar o token no padrão ‘token *seu-token-aqui*’ no arquivo variante.gradle no campo AUTHORIZATION.

